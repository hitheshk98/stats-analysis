/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.dto.critieria;

import gov.nih.nci.caintegrator.dto.de.ChemoAgentDE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class encapsulates Prior Therapy ChemoAgent Criteria. 
 * It contains a collection of one or more
 * Prior Therapy Section's ChemoAgentDE
 *
 * @author Dana Zhang
 */



/**
* 
* 
*/

public class ChemoAgentCriteria extends Criteria implements Serializable,
		Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7352645457245871430L;

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
	
	
	private ChemoAgentDE chemoAgentDE;
	
	/**
	 * Represents a collection of one or more 
	 * Prior Therapy ChemoAgentDE object.
	 */
	private Collection agents;

	/**
	 * Default constructor
	 *
	 */
	public ChemoAgentCriteria() {
	}


	/**
	 * Sets the prior therapy chemoAgentDE object by adding it to the collection
	 * one ChemoAgentDE at a time
	 * 
	 */
	public void setChemoAgentDE(ChemoAgentDE chemoAgentDE) {
		if (chemoAgentDE != null) {			
			getAgentMembers().add(chemoAgentDE);
		}
	}
    
	/**
	 * this is to deal with setting multiple prior therapy chemoAgentDE  entries
	 */
 
		
	public void setAgents(Collection multiAgents) {
			if (multiAgents != null) {
				Iterator iter = multiAgents.iterator();
				while (iter.hasNext()) {
					ChemoAgentDE chemoAgentDE = (ChemoAgentDE) iter.next();
					getAgentMembers().add(chemoAgentDE);
				}
			}
	}

	/**
   	 * private method to get a colletion of  prior therapy chemoAgent objects.
   	 */
  private Collection getAgentMembers() {
		if (agents == null) {
			agents = new ArrayList();
		}
		return agents;
	}
  /**
   * Returns a collection of priorTherapyChemoAgentDE objects.
   * 
   */
	public Collection getAgents() {
		return agents;
	}
	
	/**
	   * Returns a single priorTherapyChemoAgentDE object.
	   * 
	   */
	public ChemoAgentDE getChemoAgentDE() {
		return chemoAgentDE;
	}

	/**
	  *  Used to validate Prior Therapy ChemoAgentDE, returns true for now
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
		ChemoAgentCriteria myClone = null;
		myClone = (ChemoAgentCriteria) super.clone();
		if(chemoAgentDE!=null) {
			myClone.chemoAgentDE = (ChemoAgentDE) chemoAgentDE.clone();
		}
		return myClone;
	}
}
