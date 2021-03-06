/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.dto.critieria;

import gov.nih.nci.caintegrator.dto.de.RadiationTherapyDE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class encapsulates Prior Therapy RadiationTherapy Criteria. 
 * It contains a collection of one or more
 * Prior Therapy Section's RadiationTherapyDE.
 *
 * Dana Zhang Date: August 30, 2004 Version 1.0
 */



/**
* 
* 
*/

public class RadiationTherapyCriteria extends Criteria implements Serializable,
		Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3120508083268649171L;

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
	
	
	private RadiationTherapyDE radiationTherapyDE;
	
	/**
	 * Represents a collection of one or more 
	 * Prior Therapy RadiationTherapyDE object.
	 */
	private Collection radiationSites;

    /**
     * Default constructor
     *
     */
	public RadiationTherapyCriteria() {
	}

	/**
	 * Sets the prior therapy radiation object by adding it to the collection
	 * one RadiationTherapyDE at a time
	 * 
	 */
	public void setRadiationTherapyDE(RadiationTherapyDE radiationTherapyDE) {
		if (radiationTherapyDE != null) {
			getRadiationMembers().add(radiationTherapyDE);
			
		}
	}

   	
	/**
	 * this is to deal with setting multiple prior therapy radiation  entries
	 */
   	public void setRadiations(Collection multiRadiations) {
   			if (multiRadiations != null) {
   				Iterator iter = multiRadiations.iterator();
   				while (iter.hasNext()) {
   					RadiationTherapyDE radiationTherapyDE = (RadiationTherapyDE) iter.next();
   					getRadiationMembers().add(radiationTherapyDE);
   				}
   			}
   	}

   	
   	/**
   	 * private method to get a collection of prior therapy radiation objects
   	 */
   private Collection getRadiationMembers() {
		if (radiationSites == null) {
			radiationSites = new ArrayList();
		}
		return radiationSites;
	}

   /**
    * Returns a collection of priorTherapyRadiationDE objects.
    * 
    */
   public Collection getRadiations() {
   		return radiationSites;
	}
   
   /**
    * Returns a single priorTherapyRadiationDE object.
    * 
    */
	public RadiationTherapyDE getRadiationTherapyDE() {
		return radiationTherapyDE;
	}

	/**
	 *  Used to validate Prior Therapy RadiationTherapyDE, returns true for now
	 */
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
		RadiationTherapyCriteria myClone = null;
		myClone = (RadiationTherapyCriteria) super.clone();
		if(this.radiationTherapyDE!=null) {
			myClone.radiationTherapyDE = (RadiationTherapyDE) radiationTherapyDE
					.clone();
		}
		return myClone;
	}

}
