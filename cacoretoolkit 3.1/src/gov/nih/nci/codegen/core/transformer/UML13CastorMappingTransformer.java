/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.codegen.core.transformer;

import gov.nih.nci.codegen.core.BaseArtifact;
import gov.nih.nci.codegen.core.ConfigurationException;
import gov.nih.nci.codegen.core.XMLConfigurable;
import gov.nih.nci.codegen.core.filter.UML13ClassifierFilter;
import gov.nih.nci.codegen.core.filter.UML13ModelElementFilter;
import gov.nih.nci.codegen.core.util.UML13Utils;
import gov.nih.nci.codegen.core.util.XMLUtils;
import gov.nih.nci.codegen.framework.FilteringException;
import gov.nih.nci.codegen.framework.TransformationException;
import gov.nih.nci.codegen.framework.Transformer;
import gov.nih.nci.common.exception.XMLUtilityException;
import gov.nih.nci.common.util.caCOREMarshaller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.Iterator;
import java.util.List;
import javax.jmi.reflect.RefObject;
import org.apache.log4j.Logger;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.UmlPackage;



/**
 * Produces an XML schema for the project object model
 * <p>
 * <p>
 * The content model for this transformer's configuration element is as follows:
 * <p>
 * <code>
 * <pre>
 *
 *
 *
 *    &lt;!ELEMENT transformer (param, filter)&gt;
 *    &lt;!ATTLIST transformer
 *       name CDATA #REQUIRED
 *       className CDATA #FIXED gov.nih.nci.codegen.core.transformer.UML13CastorMappingTransformer&gt;
 *    &lt;!ELEMENT param EMPTY&gt;
 *    &lt;!ATTLIST param
 *       name CDATA #FIXED packageName
 *       value CDATA #REQUIRED&gt;
 *    &lt;!ELEMENT filter ... see {@link gov.nih.nci.codegen.core.filter.UML13ClassifierFilter#configure(org.w3c.dom.Element)} ...
 *
 *
 *
 * </pre>
 * </code>
 * <p>
 * As you can see, this transformer expects a nested filter element. The reason
 * is that this transformer produces a single Artifact (an XML file) from a
 * collection of model elements.
 * <p>
 * UML13OJBRepTransformer expects to be passed an instance of
 * org.omg.uml.modelmanagement.Model. It uses UML13ModelElementFilter to obtain
 * all model elements in the model. Then it use UML13Classifier to obtain the
 * classifiers selected by the contents of the nested filter element. Then it
 * iterates through these classifiers, building the class-descriptor elements.
 * <p>
 * A Collection containing a single Artifact is returned by this transformer's
 * execute method. The name attribute of the Artifact is set to "ojb_repository"
 * and its source attribute is set to the String that represents the XML
 * document.
 * <p>
 *
 * @author caBIO Team
 * @version 1.0
 */
public class UML13CastorMappingTransformer implements Transformer, XMLConfigurable {

    private static Logger log = Logger.getLogger(UML13CastorMappingTransformer.class);
    public static final String PROPERTIES_FILENAME = "xml.properties";
    public static final String PROPERTIES_CONTEXT_KEY = "context";
    public static final String PROPERTIES_CLASSIFICATION_KEY = "classification";
    public static final String PROPERTIES_VERSION_KEY = "version";
    public static final String PROPERTIES_NS_PREFIX_KEY = "ns_prefix";
    private UML13ClassifierFilter _classifierFilt;
    private String _pkgName;
    private Properties _properties;
    private String context;
    private String classification;
    private String version;
    private String nsprefix;
    
    /**
     *
     */
    public UML13CastorMappingTransformer() {
        super();
    }
    private String loadProperty(String key) throws IOException{
        if(_properties == null){
            try {
                _properties = new Properties();
                _properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(this.PROPERTIES_FILENAME));
            } catch (IOException e) {
                throw new IOException("Error loading " + caCOREMarshaller.PROPERTIES_FILENAME + " file. Please make sure the file is in your classpath.");
            }
        }
         return _properties.getProperty(key);
    }
    /**
     * @see gov.nih.nci.codegen.framework.Transformer#execute(javax.jmi.reflect.RefObject,
     *      java.util.Collection)
     */
    public Collection execute(RefObject modelElement, Collection artifacts) throws TransformationException {
        if (modelElement == null) {
        	log.error("model element is null");
            throw new TransformationException("model element is null");
        }
        if (!(modelElement instanceof Model)) {
        	log.error("model element not instance of Model");
            throw new TransformationException(
                    "model element not instance of Model");
        }
        ArrayList newArtifacts = new ArrayList();
        UML13ModelElementFilter meFilt = new UML13ModelElementFilter();
        ArrayList umlExtentCol = new ArrayList();
        umlExtentCol.add(modelElement.refOutermostPackage());
        Collection classifiers = null;
        try {
            classifiers = _classifierFilt.execute(meFilt.execute(umlExtentCol));
        } catch (FilteringException ex) {
        	log.error("couldn't filter model elements" + ex.getMessage());
            throw new TransformationException("couldn't filter model elements",
                    ex);
        }
      
        Document doc = generateRepository(classifiers);
        XMLOutputter p = new XMLOutputter();
        p.setFormat(Format.getPrettyFormat());

        newArtifacts.add(new BaseArtifact("castormapping", modelElement, p.outputString(doc)));
        return newArtifacts;
    }

    private Document generateRepository(Collection classifiers) {


            Element mappingEl = new Element("mapping");
            //DocType docType = new DocType("mapping","SYSTEM","mapping.dtd");
            DocType docType = new DocType("mapping","-//EXOLAB/Castor Object Mapping DTD Version 1.0//EN","http://www.castor.org/mapping.dtd");

            Document doc = new Document();
			doc.setDocType(docType);
	        for (Iterator i = classifiers.iterator(); i.hasNext();) {
	            UmlClass klass = (UmlClass) i.next();
	            try {
	            	doMapping(klass, mappingEl);
	            } catch (XMLUtilityException ex) {
	            }
	        }
            doc.setRootElement(mappingEl);
            return doc;
	    }

     private String getNamespaceURI(UmlClass klass) throws XMLUtilityException{
    	 StringBuffer nsURI = new StringBuffer();
    	 try {
    	 context = loadProperty(this.PROPERTIES_CONTEXT_KEY);
    	 classification = loadProperty(this.PROPERTIES_CLASSIFICATION_KEY);
    	 version = loadProperty(this.PROPERTIES_VERSION_KEY);
    	 nsprefix= loadProperty(this.PROPERTIES_NS_PREFIX_KEY);
     } catch (IOException e) {
         log.error("Error reading default xml mapping file " + e.getMessage());  //To change body of catch statement use File | Settings | File Templates.
         throw new XMLUtilityException("Error reading default xml mapping file " + e.getMessage(), e);
     }
    	 String packageName = getPackage(klass);
    	 nsURI.append(nsprefix);
    	 nsURI.append(classification);
    	 nsURI.append(".");
    	 nsURI.append(context);
    	 nsURI.append("/");
    	 nsURI.append(version);
    	 nsURI.append("/");
    	 nsURI.append(packageName);
    	 return nsURI.toString();
     }

     private void doMapping(UmlClass klass, Element mappingEl) throws XMLUtilityException {
    	 String superClassName =null;
    	 UmlClass superClass = UML13Utils.getSuperClass(klass);
         if (superClass != null) {
 		   superClassName = getPackage(superClass)+ "."+superClass.getName();
         } 	    
         String classElName = "class";
         Element classEl = new Element(classElName);
         mappingEl.addContent(classEl);
         classEl.setAttribute("name", getPackage(klass)+"."+klass.getName());
         if (superClassName!=null){
        	 classEl.setAttribute("extends", superClassName);
         }
         Element maptoelement = new Element("map-to");
         maptoelement.setAttribute("xml", klass.getName());
         String nsURI = getNamespaceURI(klass);
         maptoelement.setAttribute("ns-uri",nsURI);
         classEl.addContent(maptoelement);
         //Do properties
         for (Iterator i = UML13Utils.getAttributes(klass).iterator(); i.hasNext();) {
             org.omg.uml.foundation.core.Attribute att = (org.omg.uml.foundation.core.Attribute) i.next();
             Element field = new Element("field");
             field.setAttribute("name", att.getName());
             if (getQualifiedName(att.getType()).equals("collection")) {
            	 field.setAttribute("type", "string");
            	 field.setAttribute("collection", "collection");
             } else {
             field.setAttribute("type", getQualifiedName(att.getType()));
             }
             Element bind = new Element("bind-xml");
             bind.setAttribute("name", att.getName());
             bind.setAttribute("node", "attribute");
             field.addContent(bind);
             classEl.addContent(field);
	      }
         /*
         for (Iterator i = UML13Utils.getAssociationEnds(klass).iterator(); i.hasNext();) {
             AssociationEnd thisEnd = (AssociationEnd) i.next();
             AssociationEnd otherEnd = UML13Utils.getOtherAssociationEnd(thisEnd);
             addSequenceAssociationElement(classEl, klass,thisEnd, otherEnd);
         }
         */
     }

     private void addSequenceAssociationElement(Element mappingEl, UmlClass klass, AssociationEnd thisEnd, AssociationEnd otherEnd) {
         if (otherEnd.isNavigable()) {
             /** If classes belong to the same package then do not qualify the association
              *
              */
        	 if (UML13Utils.isMany2One(thisEnd, otherEnd)) {
             if (UML13Utils.getLowerBound(thisEnd, otherEnd).toString().equals("1")) {
            	 Element field = new Element("field");
            	 field.setAttribute("name", otherEnd.getName());
            	 String associationPackage = getPackage(otherEnd.getType());
            	 field.setAttribute("type", associationPackage+"." + otherEnd.getType().getName());
            	 Element bind = new Element("bind-xml");
                 bind.setAttribute("name", otherEnd.getName());
                 bind.setAttribute("node", "element");
                 field.addContent(bind);
                 mappingEl.addContent(field); 
             }
        	 } else if (UML13Utils.isMany2Many(thisEnd, otherEnd) || UML13Utils.isOne2Many(thisEnd, otherEnd)){
        		 if (UML13Utils.getLowerBound(thisEnd, otherEnd).toString().equals("1")) {
                	 Element field = new Element("field");
                	 field.setAttribute("name", otherEnd.getName());
                	 field.setAttribute("type", "java.util.Collection" );
                	 Element bind = new Element("bind-xml");
                     bind.setAttribute("name", otherEnd.getName());
                     bind.setAttribute("node", "element");
                     field.addContent(bind);
                     mappingEl.addContent(field);
                 } 
        		 
        	 }
        	 
         }
     }
    /**
     * @param classifiers
     * @return
     */
    private Document generateConfig(Collection classifiers) {
        Element configEl = new Element("DAOConfiguration");

        for (Iterator i = classifiers.iterator(); i.hasNext();) {
            Classifier klass = (Classifier) i.next();
            UmlPackage pkg = null;
            if (_pkgName != null) {
                pkg = UML13Utils.getPackage(UML13Utils.getModel(klass),
                        _pkgName);
            } else {
                pkg = UML13Utils.getModel(klass);
            }
        }

        Document doc = new Document();
        doc.setRootElement(configEl);
        return doc;
    }

    /**
     * @see gov.nih.nci.codegen.core.JDOMConfigurable#configure(org.jdom.Element)
     */
    public void configure(org.w3c.dom.Element config)
            throws ConfigurationException {

        org.w3c.dom.Element filterEl = XMLUtils.getChild(config, "filter");
        if (filterEl == null) {
        	log.error("no child filter element found");
            throw new ConfigurationException("no child filter element found");
        }

        String className = filterEl.getAttribute("className");
        if (className == null) {
        	log.error("no filter class name specified");
            throw new ConfigurationException("no filter class name specified");
        }
        _pkgName = getParameter(config, "basePackage");
        log.debug("basePackage: " + _pkgName);

        try {
            _classifierFilt = (UML13ClassifierFilter) Class.forName(className)
                    .newInstance();
        } catch (Exception ex) {
        	log.error("Couldn't instantiate "
                    + className);
            throw new ConfigurationException("Couldn't instantiate "
                    + className);
        }

        _classifierFilt.configure(filterEl);
    }

private String getQualifiedName(ModelElement me) {
        String qName = null;
        UmlPackage pkg = null;
        if (_pkgName != null) {
            pkg = UML13Utils.getPackage(UML13Utils.getModel(me), _pkgName);
        } else {
            pkg = UML13Utils.getModel(me);
        }
        qName = UML13Utils.getNamespaceName(pkg, me) + "." + me.getName();
        int i = qName.lastIndexOf(".");
        //System.out.println("Attribute type name: " + qName + "\n");
        if (qName.startsWith(".") || qName.startsWith("java")) {
			qName = qName.substring(i+1);
		}
        return qName.toLowerCase();
    }

    /**
     * @param klass
     * @return
     */
/*
    private String getPackage(UmlClass klass) {
        UmlPackage pkg = null;
        if (_pkgName != null) {
            pkg = UML13Utils.getPackage(UML13Utils.getModel(klass), _pkgName);
        } else {
            pkg = UML13Utils.getModel(klass);
        }
        return UML13Utils.getNamespaceName(pkg, klass);

    }
    */
    private String getPackage(ModelElement klass) {
        UmlPackage pkg = null;
        if (_pkgName != null) {
            pkg = UML13Utils.getPackage(UML13Utils.getModel(klass), _pkgName);
        } else {
            pkg = UML13Utils.getModel(klass);
        }
        return UML13Utils.getNamespaceName(pkg, klass);

    }
    

    private String getParameter(org.w3c.dom.Element config, String paramName) {
        String param = null;

        List params = XMLUtils.getChildren(config, "param");
        for (Iterator i = params.iterator(); i.hasNext();) {
            org.w3c.dom.Element paramEl = (org.w3c.dom.Element) i.next();
            if (paramName.equals(paramEl.getAttribute("name"))) {
                param = paramEl.getAttribute("value");
                break;
            }
        }

        return param;
    }

}
