/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.common.net;

/**
 * Provides components to hold the resultset of a query request  
 * @author caBIO Team 
 * @version 1.0 
 */

public class Response implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;
	public Object response;
	private Integer rowCount;

	/**
	 * Create a Response instance
	 */
	public Response()
	{
	
	}
	/**
	 * Creates a Response instance for a specified response object 
	 * @param response
	 */
	public Response(Response response)
	{
		this.response=response;
	}	
	
	/**
	 * Creates a Response instance for a specified object
	 * @param response Specifies the object
	 */
	public Response(Object response)
	{
		this.response=response;
	}	
	
	/**
	 * Sets the value for this response object
	 * @param response	Specifies the object
	 */
	public void setResponse(Object response)
	{
		this.response=response;
	}
	
	/**
	 * Returns a response object
	 * @return Returns a response object
	 */
	public Object getResponse()
	{
		return response;
	}

	/**
	 * Set recordsCount value
	 * @param rc
	 */
	public void setRowCount(Integer rc)
	{
	    this.rowCount = rc;
	}
	/**
	 * Return recordsCount
	 * @return recordsCount
	 */
	public Integer getRowCount()
	{
	    return this.rowCount;
	}
}
