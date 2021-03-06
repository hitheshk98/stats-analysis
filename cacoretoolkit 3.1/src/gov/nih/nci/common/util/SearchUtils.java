/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.common.util;


//import org.hibernate.criterion.*;
//import org.hibernate.impl.*;
//import org.hibernate.impl.*;
//
//import org.hibernate.*;

//import net.sf.hibernate.expression.*;
//import net.sf.hibernate.*;
//import net.sf.hibernate.impl.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.reflect.*;

import org.apache.log4j.*;

/**
 * SearchUtils presents various methods to build and modify a hibernate criteria.
 * @author caBIO Team
 * @version 1.0
 */

public class SearchUtils {
    
    private static Logger log= Logger.getLogger(SearchUtils.class.getName());
    
	private static Properties roleLookupProp;
	 
	{
		try{	
			
			roleLookupProp = RoleLookupProperties.getInstance().getProperties();
		}
		catch(Exception e)
		{
		    log.error("IOException: " + e.getMessage());
			System.out.println("Could not find roleLookup.properties ");
		}
	}

/**
 	* Returns the role name for the specified class and object
 	* @param searchClass 	Specifies a class
 	* @param criterion		Specifies an object
 	* return				Returns the role between the specified class and object
 */
    public String getRoleName(Class searchClass, Object criterion) throws Exception{

	   		 		String searchClassName 		= searchClass.getName();
			        String searchBeanName		= searchClassName.substring(searchClassName.lastIndexOf(".")+1);
			        String criterionClassName 	= criterion.getClass().getName();
			        String criterionBeanName	= criterionClassName.substring(criterionClassName.lastIndexOf(".")+1);

			        String roleName 			= null;
			        Field[] fields 				= searchClass.getDeclaredFields();

			        // first, check if the super class has the association with criterion object
			        if (searchClass.getSuperclass() != null)
			        {
			        	Class superClass = searchClass.getSuperclass();
			        	roleName = getRoleName(searchClass.getSuperclass(), criterion);
			        }
			        
			        // if the superclass has association with the criterionobject,
			        // use the superclass's asscoiation as the subclass.
			        if (roleName != null)
			        {
			        	return roleName;
			        }
			        
			        try{
			            for (int i = 0; i < fields.length; i++) {
			                fields[i].setAccessible(true);

			                    String fieldName 	= fields[i].getName();
			                    String fieldType 	= fields[i].getType().getName();
			                    Class typeClass 	= fields[i].getType();

			                    String match = null;
//			                    if(criterionBeanName.indexOf("Impl")>0){
//					    		    match = criterionBeanName.substring(0,criterionBeanName.indexOf("Impl"));
//					    		}
//					    	    else{
					    	        match = criterionBeanName;
//					    	    }

			                    if(!typeClass.isPrimitive())
			                    {
			                        if(!typeClass.isArray())
			                        {
			                        	if(isCollectionType(typeClass))
			                        	{
			                        		String returnType = roleLookupProp.getProperty(fieldName+searchClass.getName());
		                                	if ((returnType != null) && (returnType.equals(criterionClassName)))
			                                {
			                                	roleName = fieldName;
			                                	break;
			                                }

			                            } 
			                            else
			                            {
			                            	if (fieldType.equals(criterionClassName))
											{
												roleName = fieldName;
                                                break;
			                                }
			                            }
			                        }
			                    }
			            }
			        }catch(Exception ex){
			            log.error("ERROR: " + ex.getMessage());
			        	ex.printStackTrace();
			            throw new Exception(ex.getMessage());
			        }
	        return roleName;
        }

//	private String getFullyQualifiedObjectName(String targetName)
//	{
//		if (targetName.indexOf("impl") < 0)
//		{
//			String packageName = targetName.substring(0, targetName.lastIndexOf("."))+".impl.";
//			String beanName = targetName.substring(targetName.lastIndexOf(".")+1)+"Impl";
//			String newName = packageName + beanName;
//			return newName;
//		}
//		else
//		{
//			return targetName;
//		}
//	}

	private boolean isCollectionType(Class inputClass)
	{
		boolean flag = false;
		if (inputClass.getName().equals("java.util.Collection"))
		{
			flag = true;
		}
		else
		{
			Class[] interfaces = inputClass.getInterfaces();
			for(int i= 0; i<interfaces.length; i++)
			{
				if (interfaces[i].getName().equals("java.util.Collection"))
				{
					flag = true;
					break;
				}
			}			
		}
		return flag;
	}

/**
 * Generates nested search criteria
 * @param packageName
 * @param criteriaList
 * @return
 * @throws Exception
 */    
    private Object buildCriteria(String packageName, List criteriaList) throws Exception{


       Object criteriaObject = null;

        try{
            criteriaObject = getCriteria((String)criteriaList.get(0),packageName);
            if(criteriaList.size()>0){
                for(int i=1; i<criteriaList.size(); i++){
                    String critString = (String)criteriaList.get(i);                    
                    Object assObject = getCriteria(critString, packageName);
                    if(criteriaObject.getClass().getName().equals(assObject.getClass().getName())){
                        throw new Exception( critString +" is not an association of "+ criteriaObject.getClass().getName());
                    }
                    Method method = getRoleMethod(criteriaObject, assObject);                   
                     if(method != null){
                            method.invoke(criteriaObject, new Object[]{assObject});
                            }
                        }


                    }

            }catch(Exception ex){
                log.error("ERROR : "+ ex.getMessage());
                throw new Exception("ERROR : "+ ex.getMessage());
            }

       return criteriaObject;
        }
    
    /**
     * Returns the criteria value 
     * @param assObject
     * @param critObject
     * @return
     * @throws Exception
     */
    public Object getCriteriaValue(Object assObject, Object critObject) throws Exception{
        if(critObject.getClass().getName().equals(assObject.getClass().getName())){
            Field[] assFields = getAllFields(assObject.getClass());
            for(int i=0; i<assFields.length; i++){                
                if(assFields[i].getName().equalsIgnoreCase("serialVersionUID")){
                    continue;
                }
                try{
                    if(assFields[i].get(assObject)!=null){                        
                        Object value = assFields[i].get(assObject);
                        Field critField = getField(critObject.getClass(), assFields[i].getName());
                        if(value != null){                           
                           critField.set(critObject, value);                           
                        }
                    }  
                }catch(Exception ex){
                    log.error(ex.getMessage());
                }
                
            }
        }
        
        return critObject;
    }
    

/**
 * Generates nested search criteria
 * @param packageName
 * @param criteriaList
 * @return
 * @throws Exception
 */
    public Object buildSearchCriteria(String packageName, List criteriaList) throws Exception{

        Object criteriaObject = null;
        Object assObject = null;
        int counter = criteriaList.size();
         try{

             if(criteriaList.size()>1){
                 criteriaObject = getCriteria((String)criteriaList.get(counter-1),packageName);
                 for(int i=counter-2; i>=0; i--){
                     assObject = criteriaObject;
                     String critString = (String)criteriaList.get(i);
                     System.out.println("Crit string: "+ critString);
                     criteriaObject= getCriteria(critString, packageName);
                     if(criteriaObject.getClass().getName().equals(assObject.getClass().getName())){
                         criteriaObject = getCriteriaValue(criteriaObject, assObject);                         
                     }
                     else{
                         Method method = null;
                         try{
                             method = getRoleMethod(criteriaObject, assObject);
                         }catch(Exception ex){
                             throw new Exception( critString +" is not an association of "+ criteriaObject.getClass().getName());
                         }
                         
                         
                         if(method != null){                          
                             Class[] types = method.getParameterTypes();
                             if(types.length > 0){
                                 if(types[0].getName().endsWith("Collection")){                                  
                                     List assObjectList = new ArrayList();
                                     assObjectList.add(assObject);
                                     method.invoke(criteriaObject, new Object[]{assObjectList});
                                 }
                                 else{
                                     method.invoke(criteriaObject, new Object[]{assObject});
                                 }
                             }
                               
                            }
                     }
                     
                       }

                     }
             else if(criteriaList.size()==1){
                 criteriaObject = getCriteria((String)criteriaList.get(0),packageName);
             }
             else{
                 throw new Exception("Criteria not defined");
             }

            }catch(Exception ex){
                log.error("ERROR : "+ ex.getMessage());
                throw new Exception("ERROR :  "+ ex.getMessage());
             }
        return criteriaObject;
         }

/**
 * Generates Search Criteria
 * @param criteriaString
 * @param packageName
 * @return
 * @throws Exception
 */
    private Object getCriteria(String criteriaString, String packageName) throws Exception{
      
        Object critObject = null;
        try{
            String critClassName = null;
            if(criteriaString.indexOf("[")>0){
                critClassName = criteriaString.substring(0,criteriaString.indexOf("["));
            }
            else{
                critClassName = criteriaString;
            }
            
            if(critClassName.indexOf(".")>0){
                critObject = Class.forName(critClassName).newInstance();
            }
            else{
                critObject = Class.forName(packageName +"."+critClassName).newInstance();
            }
            
            String attString = null;
            List attList = new ArrayList();
            if(criteriaString.indexOf("[")>=0){
                attString = criteriaString.substring(criteriaString.indexOf("["),criteriaString.lastIndexOf("]")+1);
                }

            if(attString!= null){
                attList = getAttributeCollection(attString);
                }

            for(int i=0; i<attList.size(); i++){
                String att = (String)attList.get(i);               
                critObject = getAttributeCriteria(att, critObject, packageName);
            }
           }catch(Exception ex){
               throw new Exception(ex.getMessage());
            }

        return critObject;
    }

    /**
     * Returns the get method  for the given association
     * @param criteriaObject
     * @param assObject
     * @return
     * @throws Exception
     */
    private Method getRoleMethod(Object criteriaObject, Object assObject) throws Exception{
        Method roleMethod = null;
        String roleName = getRoleName(criteriaObject.getClass(), assObject);
        if(roleName != null){
            String setMethod = "set"+roleName.substring(0,1).toUpperCase() + roleName.substring(1);
            roleMethod = getMethod(criteriaObject.getClass(), setMethod);
        }
        return roleMethod;
        }

    /**
     * Returns the method specified by the method name
     * @param critClass
     * @param methodName
     * @return
     */
    private Method getMethod(Class critClass, String methodName){
        Method[] methods = getAllMethods(critClass);
        Method method = null;
        for(int i=0; i<methods.length; i++){
            if(methods[i].getName().equalsIgnoreCase(methodName)){
                method = methods[i];            
                break;
            }
        }
        return method;
        }

    /**
     * Returns list of search attributes
     * @param attString
     * @return
     * @throws Exception
     */
    private List getAttributeCollection(String attString) throws Exception{
        
        List attList = new ArrayList();
        int startCounter =0;
        int startIndex =0;
        int endCounter =0;
        for(int i=0; i<attString.length(); i++){
            if(attString.charAt(i)=='['){
                startCounter++;
            }
            else if(attString.charAt(i)==']'){
                endCounter++;
            }
        }
        if(startCounter != endCounter){
            throw new Exception("Invalid format: '[' parenthesis does not match number of ']' parenthesis");
        }
        try{
            if(attString.indexOf("][")<1){
                String att = attString.substring(1,attString.lastIndexOf("]"));           
                attList.add(att);
            }
            else{
                if(attString.charAt(0)=='['){                
                    startCounter = 1;
                    endCounter =0;
                    startIndex = 1;
                }
                else{
                    throw new Exception("Invalid Query format " + attString);
                }

                int count = attString.length();
                for(int i=1; i<count;i++){                
                    if(attString.charAt(i)==']'){
                        endCounter++;
                        if(startCounter == endCounter){                        
                            String att = attString.substring(startIndex,i);
                            attList.add(att);                        
                            startIndex = i+2;
                            if(startIndex < count){
                                startCounter = 0;
                                endCounter =0;
                            }

                        }
                    }
                    else if(attString.charAt(i)=='['){
                        startCounter++;
                    }                
                }
            }

        }catch(Exception ex){
            throw new Exception(ex.getMessage());
        }
        return attList;
    }

    /**
     * Generates a criteria object
     * @param att - specifies the attribute
     * @param critObject - specifies the criteria 
     * @param packageName - specifies the package name
     * @return
     * @throws Exception
     */
    private Object getAttributeCriteria(String att, Object critObject, String packageName)throws Exception{
        
        try{
            String attRole = null;
            if(att.indexOf("[")>1){
                attRole = att.substring(0,att.indexOf("["));    
            }

            Method critAttMethod = null;
                if(attRole == null){
                    String attName = att.substring(att.indexOf("@")+1, att.indexOf("="));
                    String attValue = att.substring(att.indexOf("=")+1);
                    Field critField = getField(critObject.getClass(), attName);
                    critAttMethod = getAttributeSetMethodName(critObject, attName);                
                    Object value = getFieldValue(critField, attValue);
                    if( critAttMethod!= null){                    
                        critAttMethod.invoke(critObject,new Object[]{value});
                        }                
                }
                else{
                    String ontologyRole = null;
                    List attValueList = new ArrayList();
                    if(attRole.indexOf("Ontology")>0 && (attRole.startsWith("parent")|| attRole.startsWith("child"))){
                        Object ontologyObject = buildOntology(att, packageName);
                        Method ontologyMethod = getMethod(critObject.getClass(), "set" + attRole.substring(0,1).toUpperCase() + attRole.substring(1));
                        
                        if(ontologyMethod != null){
                            if(attRole.endsWith("Collection")){
                                List ontologyList = new ArrayList();
                                ontologyList.add(ontologyObject);
                                ontologyMethod.invoke(critObject,new Object[]{ontologyList});                                                               
                            }
                            else{
                                ontologyMethod.invoke(critObject,new Object[]{ontologyObject});
                            }
                        }                                           
                    }
                    else{
                        String roleClassName = getRoleClassName(attRole);

                        if(roleClassName.indexOf(".")<0){
                            roleClassName = packageName +"." + roleClassName;                            
                        }                
                        Method roleMethod = null;
                        if(ontologyRole != null){                    
                            roleMethod = getMethod(critObject.getClass(), "set"+ attRole.substring(0,1).toUpperCase() + attRole.substring(1));
                            }
                        else{
                            roleMethod = getMethod(critObject.getClass(), "set"+ attRole.substring(0,1).toUpperCase() + attRole.substring(1));
                            }

                        Object roleObject = Class.forName(roleClassName).newInstance();
                        List roleClassCollection = new ArrayList();
                        int count = 0;
                        for(int i=0; i< att.length(); i++){
                            if(att.charAt(i)=='['){
                                count++;
                            }
                        }
                        if(count>1){                    
                            List attList = getAttributeCollection(att.substring(att.indexOf("[")));
                            for(int i=0; i<attList.size(); i++){
                                String critAtt = (String)attList.get(i);                        
                                String attName = critAtt.substring(1, critAtt.indexOf("="));
                                String attValue = critAtt.substring(critAtt.indexOf("=")+1);
                                Field roleAttField = getField(Class.forName(roleClassName), attName);
                                Method roleAttMethod = getAttributeSetMethodName(Class.forName(roleClassName).newInstance(), attName);
                                if(attValue.indexOf(",")<1){
                                    if(roleMethod != null){
                                        try{
                                        Object value = new Object();
                                        value = getFieldValue(roleAttField, attValue);
                                        roleAttMethod.invoke(roleObject,new Object[]{getFieldValue(roleAttField, attValue)});
                                        }catch(Exception e){
                                            throw new Exception("Unable to set value for " + attName +" - "+e.getMessage());
                                        }
                                    }
                                }

                                if(roleObject != null && attRole.indexOf("Collection")>0){
                                    roleClassCollection.add(roleObject);
                                }

                            }
                        }
                        else{
                            String attName = att.substring(att.indexOf("@")+1, att.indexOf("="));
                            String attValue = att.substring(att.indexOf("=")+1, att.indexOf("]"));
                            Field roleAttField = getField(roleObject.getClass(), attName);
                            Method roleAttMethod = getAttributeSetMethodName(roleObject, attName);
                            if(attRole.indexOf("Collection")>0){
                                Object value = getFieldValue(roleAttField,attValue);
                                roleAttMethod.invoke(roleObject,new Object[]{value});
                                roleClassCollection.add(roleObject);
                            }
                            else{                        
                                Object value = getFieldValue(roleAttField, attValue);
                                roleAttMethod.invoke(roleObject,new Object[]{value});
                            }

                        }

                        if(attRole.indexOf("Collection")<1 && roleObject != null){                    
                                roleMethod.invoke(critObject, new Object[]{roleObject});                        
                        }else if(roleClassCollection.size()>0){                    
                            try{
                                roleMethod.invoke(critObject, new Object[] {roleClassCollection});
                            }catch(Exception ex){
                                throw new Exception("Cannot invoke method - " + roleMethod.getName());
                            }
                        }else{
                            throw new Exception("Unable to generate search criteria");
                        }

                    }

               }

            }catch(Exception ex){
            throw new Exception(ex.getMessage());
            }
        return critObject;
        }

    /**
     * Returns the field for a given attribute name
     * @param className specifies the class name
     * @param attributeName - specifies the attribute name
     * @return
     * @throws Exception
     */
    public Field getField(Class className, String attributeName)throws Exception{  
        Field attribute = null;
            Field[] fields = getAllFields(className);
            for(int i=0; i<fields.length; i++){           
                if(fields[i].getName().equalsIgnoreCase(attributeName)){
                    fields[i].setAccessible(true);
                    attribute = fields[i];
                    break;
                    }
                }
        if(attribute == null){
            throw new Exception ("Invalid field name - "+ attributeName);
        }
        return attribute;
        }

    /**
     * Returns a Method for a given attribute in the specified class
     * @param attObject 
     * @param attName
     * @return
     */
    private Method getAttributeSetMethodName(Object attObject, String attName){    
        Method m = getMethod(attObject.getClass(), "set"+ attName.substring(0,1).toUpperCase() + attName.substring(1));
        return m;
    }

    /**
     * Returns a value for the specified field
     * @param field
     * @param attValue
     * @return
     * @throws Exception
     */
    private Object getFieldValue(Field field, String attValue) throws Exception{       
        Object value = null;
        if(field.getType().getName().equalsIgnoreCase("java.lang.String")){
            value = attValue;
            }
        else{
            value = convertValues(field, attValue);
            }
    return value;
    }

    
    /**
    * Converts the specified value to the filed class type
    * @param field  Specifies the field
    * @param value  Specifies the values that needs to be stored
    * @return  returns an object with the new value
    * @throws Exception
    */
    public Object convertValues(Field field, Object value) throws Exception{
    String fieldType = field.getType().getName();
    String valueType = value.getClass().getName();
    Object convertedValue = null;
    try{
        if(fieldType.equals("java.lang.Long")){
            if(valueType.equals("java.lang.String")){
                convertedValue = new Long((String)value);
                }
            }
        else if(fieldType.equals("java.lang.Integer")){
            if(valueType.equals("java.lang.String")){
                convertedValue = new Integer((String)value);
                }
            }
        else if(fieldType.equals("java.lang.Float")){
            if(valueType.equals("java.lang.String")){
                convertedValue = new Float((String)value);
                }
            }
        else if(fieldType.equals("java.lang.Double")){
            if(valueType.equals("java.lang.String")){
                convertedValue = new Double((String)value);
                }
            }
        else if(fieldType.equals("java.lang.Boolean")){
            if(valueType.equals("java.lang.String")){
                convertedValue = new Boolean((String)value);
                }
            }
        else if(fieldType.equals("java.util.Date")){
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
            if(valueType.equals("java.lang.String")){
                convertedValue = format.parse((String)value);
                }
        }
        else{
            throw new Exception("type mismatch - "+valueType);
            }


    }catch(Exception ex){
        String msg = "type mismatch " + field.getName() + " is of type - "+ fieldType + " \n "+ ex.getMessage();
        log.error("ERROR : "+ msg);
        throw new Exception(msg);
        }

    return convertedValue;
    }

    /**
     * Returns a class name for a given role
     * @param attRole
     * @return
     */
    public String getOntologyRoleName (String attRole){    
        String ontologyRole = null;
        if(attRole.startsWith("child")){
            ontologyRole = attRole.substring("child".length());
            }
        else if(attRole.startsWith("parent")){
            ontologyRole = attRole.substring("parent".length());
            }
        if(ontologyRole != null){
            if(ontologyRole.indexOf("Relationship")>=0){
                ontologyRole = ontologyRole.substring(0, ontologyRole.indexOf("Relationship"));
                }
            else if(ontologyRole.endsWith("Collection")){
                ontologyRole = ontologyRole.substring(0, ontologyRole.indexOf("Collection"));
                }
        }

        return ontologyRole;
    }

    /**
     * Returns the class name for a given role
     * @param attRole
     * @return
     */
    public String getRoleClassName(String attRole){
        
        String attClassName = null;
        if(attRole.indexOf("Ontology")>=0 && (attRole.startsWith("child")|| attRole.startsWith("parent"))){        
            attClassName  = getOntologyRoleName(attRole);
            }
        else if(attRole.indexOf("Collection")>0){
            attClassName = attRole.substring(0,1).toUpperCase() + attRole.substring(1,attRole.indexOf("Collection"));
        }else{
            attClassName = attRole.substring(0,1).toUpperCase() + attRole.substring(1);
        }        
      return attClassName;
    }

  
    /**
     * Gets all the fields for a given class
     * @param resultClass - Specifies the class name
     * @return - returns all the fields of a class
     */
    public Field[] getAllFields(Class resultClass){
        List fieldList = new ArrayList();
        try{

        while(resultClass != null && !resultClass.isInterface() && !resultClass.isPrimitive()){
            Field[] fields = resultClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                        String fieldName = fields[i].getName();
                        fieldList.add(fields[i]);
                }


            if(!resultClass.getSuperclass().getName().equalsIgnoreCase("java.lang.Object")){
                resultClass = resultClass.getSuperclass();
                }
            else{
                break;
                }

            }
        }catch(Exception ex){
            log.error("ERROR: " + ex.getMessage());
            }

        Field[] fields = new Field[fieldList.size()];
        for(int i=0;i<fieldList.size(); i++){
            fields[i]= (Field)fieldList.get(i);
            }
            return fields;
        }

    /**
     * Gets all the methods for a given class
     * @param resultClass - Specifies the class name
     * @return - Returns all the methods
     */


    public Method[] getAllMethods(Class resultClass){

        List methodList = new ArrayList();
        try{
        while(resultClass != null && !resultClass.isInterface() && !resultClass.isPrimitive()){
            Method[] method = resultClass.getDeclaredMethods();
            for (int i = 0; i < method.length; i++) {
                        method[i].setAccessible(true);
                        String methodName = method[i].getName();
                        methodList.add(method[i]);
                }


            if(!resultClass.getSuperclass().getName().equalsIgnoreCase("java.lang.Object")){
                resultClass = resultClass.getSuperclass();
                }
            else{
                break;
                }
            }
            }catch(Exception ex){
                log.error("ERROR: " + ex.getMessage());
            }

            Method[] methods = new Method[methodList.size()];
                    for(int i=0;i<methodList.size(); i++){
                        methods[i]= (Method)methodList.get(i);
                    }
        return methods;
        }

   
   
    /**
     * Generates ontology search criteria
     * @param queryString - Specifies the query string
     * @param packageName - specifies the package name
     * @return ontology criteria object
     * @throws Exception
     */
    public Object buildOntology(String queryString, String packageName) throws Exception{
        
        String attName = queryString.substring(queryString.indexOf("@")+1,queryString.indexOf("="));
        String attValue = queryString.substring(queryString.indexOf("=")+1,queryString.lastIndexOf("]"));
        String setMethodName = "set"+attName.substring(0,1).toUpperCase() + attName.substring(1);
        String roleName = queryString.substring(0, queryString.indexOf("["));
        
        Object ontologyCriteria = null;
        String ontologyClassName = null;
        
        if(roleName.startsWith("child")){
            ontologyClassName = roleName.substring("child".length());
        }
        else if(roleName.startsWith("parent")){
            ontologyClassName = roleName.substring("parent".length());
        }
        if(ontologyClassName.endsWith("Collection")){
            ontologyClassName = ontologyClassName.substring(0,ontologyClassName.indexOf("Collection"));
        }

        if(ontologyClassName.indexOf(".")<1){
            ontologyClassName = packageName +"."+ontologyClassName;
        }
        
        Field field = getField(Class.forName(ontologyClassName), attName);
        
        Method method = getMethod(Class.forName(ontologyClassName), setMethodName);
        
        Object value = null;
        if(method != null){
            if(!field.getType().getName().endsWith("String")){
                value = convertValues(field, attValue);
            }
            else{
                value = attValue;
            }
            ontologyCriteria = Class.forName(ontologyClassName).newInstance();
            method.invoke(ontologyCriteria, new Object[]{value});
            
            Field[] ofields = this.getAllFields(Class.forName(ontologyClassName)); 
            for(int i=0; i<ofields.length; i++){
                Field ofield = ofields[i];
                ofield.setAccessible(true);
            }
        }
        return ontologyCriteria;
    }
    /**
     * Returns the target class name for the specified class and role names
     * @param className specifies the class name
     * @param roleName specifies role name
     * @return
     * @throws Exception
     */
    public String getTargetClassName(String className, String roleName) throws Exception{
        String targetClassName = null;
        String target = null;
        Set roles = roleLookupProp.keySet();
        if(roleLookupProp.getProperty(roleName+className)!= null){
            String searchName = roleName+className;
            targetClassName = roleLookupProp.getProperty(searchName);            
            if(targetClassName == null){
                for(Iterator i= roles.iterator(); i.hasNext();){
                    String key = (String)i.next();
                    String value = (String)roleLookupProp.get(key);
                    if(key.equals(searchName)){
                        targetClassName = value;                        
                        break;
                    }
                }
            }
        }
                    
            String searchBean = roleName;
            if(targetClassName == null){            
                if(roleName.indexOf("Collction")>0){
                    searchBean = searchBean.substring(0,searchBean.lastIndexOf("Collection"));
                }
                searchBean = searchBean.substring(0,1).toUpperCase() + searchBean.substring(1);
                    
                    for(Iterator i= roles.iterator(); i.hasNext();){
                        String key = (String)i.next();
                        String value = (String)roleLookupProp.get(key);
                        if(key.endsWith(searchBean) && value.equals(className)){
                            target= key;                            
                            break;
                        }
                    }             
            }
            if(target != null){
                if(target.indexOf("Collection")>0){
                    targetClassName = target.substring(target.indexOf("Collection")+"Collection".length());                    
                }
            }      
            if(targetClassName == null){
                Class superClass = Class.forName(className).getSuperclass();
                if(!superClass.getName().endsWith("Object") && superClass != null){
                    targetClassName = getTargetClassName(superClass.getName(), roleName);
                }
            }  
        return targetClassName;
        
    }

}

