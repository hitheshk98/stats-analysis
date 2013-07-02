/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

/*
 * Created on Jun 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.system.applicationservice;

/**
 * This exception is thrown by all the methods of the {@link ApplicationService}
 * interface. This exception contains the actual error or the error message of 
 * the business error that occured during processing the request.
 * 
 * @author Ekagra Software Technologies Ltd.
 */
public class AuthorizationException extends SecurityException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Default constructor. Constructs the (@link AuthorizationException) object 
	 */
	public AuthorizationException()
	{
		super();
	}
	/**
	 * Constructs the {@link AuthorizationException} object with the passed message 
	 * @param message The message which is describes the exception caused
	 */
	public AuthorizationException(String message)
	{
		super(message);
	}
	/**
	 * Constructs the {@link AuthorizationException} object with the passed message.
	 * It also stores the actual exception that occured 
	 * @param message The message which describes the exception
	 * @param cause The actual exception that occured
	 */
	public AuthorizationException(String message, Throwable cause)
	{
		super(message, cause);
	}
	/**
	 * Constructs the {@link AuthorizationException} object storing the actual 
	 * exception that occured 
	 * @param cause The actual exception that occured
	 */
	public AuthorizationException(Throwable cause)
	{
		super(cause);
	}
}
