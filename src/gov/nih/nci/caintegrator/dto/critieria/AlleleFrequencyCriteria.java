/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.dto.critieria;

import gov.nih.nci.caintegrator.dto.de.AlleleFrequencyDE;

import java.io.Serializable;

/**
 * This class encapsulates the properties of an caintergator
 * AlleleFrequencyCriteria object.
 * 
 * @author Dana Zhang, BauerD
 */



/**
* 
* 
*/

public class AlleleFrequencyCriteria extends Criteria implements Serializable,
		Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7326625175475607449L;
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
	private AlleleFrequencyDE alleleFrequencyDE;

	public AlleleFrequencyCriteria() {
	}

	public void setAlleleFrequencyDE(AlleleFrequencyDE alleleFrequencyDE) {
		if (alleleFrequencyDE != null) {
			this.alleleFrequencyDE = alleleFrequencyDE;
		}
	}

	public AlleleFrequencyDE getAlleleFrequencyDE() {
		return alleleFrequencyDE;
	}

	public boolean isValid() {
		return true;
	}
	/**
	 * Overrides the protected Object.clone() method exposing it as public.
	 * It performs a 2 tier copy, that is, it does a memcopy of the instance
	 * and then sets all the non-primitive data fields to clones of themselves.
	 * 
	 * @return -A minimum 2 deep copy of this object.
	 */
	public Object clone() {
		AlleleFrequencyCriteria myClone = null;
		
		myClone = (AlleleFrequencyCriteria) super.clone();
		if(alleleFrequencyDE!=null) {
			myClone.alleleFrequencyDE = (AlleleFrequencyDE) alleleFrequencyDE
				.clone();
		}
		return myClone;
	}
}
