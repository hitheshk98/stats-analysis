/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.dto.de;

import gov.nih.nci.caintegrator.enumeration.MultiGroupComparisonAdjustmentType;

import java.io.Serializable;

/**
 * This class encapsulates the properties of an caintergator MultiGroupComparisonAdjustmentTypeDE object.
 * 
 * @author SahniH
 */


/**
* 
* 
*/

public class MultiGroupComparisonAdjustmentTypeDE extends DomainElement implements Serializable, Cloneable {

	/**
	 * IMPORTANT! This class requires a clone method! This requires that any new
	 * data field that is added to this class also be cloneable and be added to
	 * clone calls in the clone method.If you do not do this, you will not
	 * seperate the references of at least one data field when we generate a
	 * copy of this object.This means that if the data field ever changes in one
	 * copy or the other it will affect both instances... this will be hell to
	 * track down if you aren't ultra familiar with the code base, so add those
	 * methods now! (Not necesary for primitives.)
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Initializes a newly created <code>MultiGroupComparisonAdjustmentTypeDE</code> object so that it
	 * represents an MultiGroupComparisonAdjustmentTypeDE.
	 */
	public MultiGroupComparisonAdjustmentTypeDE(MultiGroupComparisonAdjustmentType multiGroupComparisonAdjustmentType) {
		super(multiGroupComparisonAdjustmentType);
	}

	/**
	 * Sets the value for this <code>MultiGroupComparisonAdjustmentTypeDE</code> object
	 * 
	 * @param object
	 *            the value
	 */
	public void setValue(Object obj) throws Exception {
		if (!(obj instanceof MultiGroupComparisonAdjustmentType))
			throw new Exception(
					"Could not set the value.  Parameter is of invalid data type: "
							+ obj);
		setValueObject((MultiGroupComparisonAdjustmentType) obj);
	}

	/**
	 * Returns the multiGroupComparisonAdjustmentType for this MultiGroupComparisonAdjustmentTypeDE obect.
	 * 
	 * @return the multiGroupComparisonAdjustmentType for this <code>MultiGroupComparisonAdjustmentTypeDE</code> object
	 */
	public MultiGroupComparisonAdjustmentType getValueObject() {
		return (MultiGroupComparisonAdjustmentType) getValue();
	}

	/**
	 * Sets the multiGroupComparisonAdjustmentType for this <code>MultiGroupComparisonAdjustmentTypeDE</code> object
	 * 
	 * @param multiGroupComparisonAdjustmentType
	 *            the multiGroupComparisonAdjustmentType
	 */
	public void setValueObject(MultiGroupComparisonAdjustmentType multiGroupComparisonAdjustmentType) {
		if (multiGroupComparisonAdjustmentType != null) {
			value = multiGroupComparisonAdjustmentType;
		}
	}
	/**
	 * Overrides the protected Object.clone() method exposing it as public.
	 * It performs a 2 tier copy, that is, it does a memcopy of the instance
	 * and then sets all the non-primitive data fields to clones of themselves.
	 * 
	 * @return -A minimum 2 deep copy of this object.
	 */
	public Object clone() {
		MultiGroupComparisonAdjustmentTypeDE myClone = (MultiGroupComparisonAdjustmentTypeDE) super.clone();
		return myClone;
	}
}
