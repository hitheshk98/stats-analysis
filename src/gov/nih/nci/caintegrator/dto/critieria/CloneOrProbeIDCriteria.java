/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.dto.critieria;

import gov.nih.nci.caintegrator.dto.de.CloneIdentifierDE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author BhattarR, BauerD
 */


/**
* 
* 
*/

public class CloneOrProbeIDCriteria extends Criteria implements Serializable,
		Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5366799350346928581L;
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
	private Collection identifiers;

	public Collection getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(Collection cloneIdentifiersObjs) {
		for (Iterator iterator = cloneIdentifiersObjs.iterator(); iterator
				.hasNext();) {
			Object obj = iterator.next();
			if (obj instanceof CloneIdentifierDE) {
				getIdentifiersMember().add(obj);
			}
		}
	}

	public void setCloneIdentifier(CloneIdentifierDE cloneIdentifier) {
		getIdentifiersMember().add(cloneIdentifier);
	}

	private Collection getIdentifiersMember() {
		if (identifiers == null)
			identifiers = new ArrayList();
		return identifiers;
	}

	public CloneOrProbeIDCriteria() {
	}

	public boolean isValid() {
		// TODO: see if we need any validation
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
		CloneOrProbeIDCriteria myClone = null;
		myClone = (CloneOrProbeIDCriteria) super.clone();
		myClone.identifiers = new ArrayList();
		if(identifiers!=null) {
			for (Iterator i = identifiers.iterator(); i.hasNext();) {
				myClone.identifiers.add(((CloneIdentifierDE) i.next()).clone());
			}
		}
		return myClone;
	}
}
