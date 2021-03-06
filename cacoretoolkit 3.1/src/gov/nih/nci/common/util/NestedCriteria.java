/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

/*
 * Created on May 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * @author zengje
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NestedCriteria implements Serializable{

	/**
	 * 
	 */
	private String targetObjectName;
	private String sourceObjectName;
	private String roleName;
	
//	private HashMap criterionMap;
	
//	private Object sourceObject;
	
	private List sourceObjectList = new ArrayList();
	
	private NestedCriteria internalNestedCriteria;
	
	protected boolean caseSensitivityFlag = false;
	
	public void setTargetObjectName(String targetName)
	{
		this.targetObjectName = targetName;
	}
	
	public String getTargetObjectName()
	{
		return this.targetObjectName;
	}
	
	public void setSourceObjectName(String sourceName)
	{
		this.sourceObjectName = sourceName;
	}
	
	public String getSourceName()
	{
		return this.sourceObjectName;
	}
	
	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}
	
	public String getRoleName()
	{
		return this.roleName;
	}
	
	public void setInternalNestedCriteria(NestedCriteria nestedCriteria)
	{
		this.internalNestedCriteria = nestedCriteria;
	}
	
	public NestedCriteria getInternalNestedCriteria()
	{
		return this.internalNestedCriteria;
	}
	
//	public void setSourceObject(Object obj)
//	{
//		this.sourceObject = obj;
//	}
//	
//	public Object getSourceObject()
//	{
//		return this.sourceObject;
//	}
	public void setSourceObjectList(List objList)
	{
		this.sourceObjectList = objList;
	}
	
	public List getSourceObjectList()
	{
		return this.sourceObjectList;
	}
	public void addSourceObject(Object obj)
	{
		sourceObjectList.add(obj);
	}
	
	public void setSearchCaseSensitivity(boolean caseSensitivity)
	{
		this.caseSensitivityFlag = caseSensitivity;
	}
}

