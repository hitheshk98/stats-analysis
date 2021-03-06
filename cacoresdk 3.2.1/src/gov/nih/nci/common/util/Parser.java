/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.common.util;


import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.IOException;
import java.util.Hashtable;


import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.jdom.filter.*;
import org.apache.log4j.*;



/**
 * Parser provides components to read and extract data from an XML file
 * @author caBIO Team
 * @version 1.0
 */

public class Parser {

public Document doc;
private static Logger log= Logger.getLogger(Parser.class.getName());
public Hashtable elemAttr = new Hashtable();


/**
 * Creates a Parser instance for a given XML file
 * @param fileName
 */
public Parser(String fileName)
{
	SAXBuilder builder = new SAXBuilder();
	try {
		 doc = builder.build(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
	}
	catch (JDOMException e) {
	    log.error("JDOMException: " + e.getMessage());
	    }
	catch (IOException e) {
	    log.error("IOException: " + e.getMessage());
	    }

}
/**
 * Returns a list of child elements stored in this document
 * @return
 */
 public List getRootElementChildren(){

 	List rootChildren = doc.getRootElement().getChildren();
  	Element domainObjects  = (Element)rootChildren.get(1);
  	domainObjects.getChild("proxy").setText("http.ncicb.nci.gov/getProtein");
 	domainObjects.getChild("urn").setText("urn.getProtein");
 	return rootChildren;
 }

 /**
  * Updates an element in the doucment
  * @param index 	Specifies the index of the element need to be updated
  * @param table	Specifies the new values for this element
  */
 	public void updateElements(int index, String[][] table)
 	{
 		List rootChildren = doc.getRootElement().getChildren();
 		Element updateElement  = (Element)rootChildren.get(index);
 		for (int i = 0; i<table.length; i++){
 			updateElement.getChild(table[i][0]).setText(table[i][1]);
 		}

 	}


	/**
	 * Generates a Hashtable for a list of elements
	 * @param list	List of elements
	 * @return		Returns a Hashtable with a list of elements
	 */
 	public Hashtable listElements(List list)
 	{

		  for (Iterator i = list.iterator(); i.hasNext();) {
		    Element e = (Element) i.next();
		    listElement(e);
		  }
		  return elemAttr;
	}

	/**
	 * Generates a Hashtable of child elements
	 * @param e		Specifies the element
	 * @return		Returns a Hashtable with a list of child elements
	 */
	public  Hashtable listElement(Element e)
	{
			elemAttr.put(e.getName(), e.getText().trim());
			List as = e.getAttributes();
			listAttributes(as);
			List c = e.getChildren();
			listElements(c);
			return elemAttr;
	}


	/**
	 * Generates a vector with the attribute name and value
	 * @param ls	List of elements
	 * @return
	 */
	public  Vector listAttributes(List ls)
	{
		Vector attributes = new Vector();

		  for (Iterator i = ls.iterator(); i.hasNext();) {
		    Attribute att = (Attribute) i.next();
		    attributes.add(att.getValue());
		    elemAttr.put(att.getName(), att.getValue());
		  }
		  return attributes;
    }



	/**
	 * returns a list of elements that matches a particular pattern
	 * @param xPathExpression	Specifies the search string
	 * return					Returns a list that matches the specified expression
	 */
	public List getList(String xPathExpression)
	{
		List list=null;
		try
		{
			XPath x = XPath.newInstance(xPathExpression);
		 	list = x.selectNodes(doc);

		} catch (JDOMException e){
		    log.error("JDOMException: " + e.getMessage());
		}

	  	return list;
	  }
	
	/**
	 * 
	 * TODO To change the template for this generated type comment go to
	 * Window - Preferences - Java - Code Style - Code Templates
	 */
	public Iterator getElements(String tagName)
	{
		Element root = doc.getRootElement();
		
		ElementFilter elementFilter = new ElementFilter(tagName);
        Iterator allElements = root.getDescendants(elementFilter);
            
        return allElements;
	}
}

