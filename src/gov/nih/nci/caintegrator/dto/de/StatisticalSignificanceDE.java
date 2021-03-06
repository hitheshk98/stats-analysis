/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.dto.de;

import gov.nih.nci.caintegrator.enumeration.Operator;
import gov.nih.nci.caintegrator.enumeration.StatisticalSignificanceType;



/**
* 
* 
*/

public class StatisticalSignificanceDE extends DomainElement {
	private StatisticalSignificanceType statisticType;
	private Operator operator = Operator.GE;
	/**
	 * @param value
	 * @param operator
	 * @param type
	 */
	public StatisticalSignificanceDE(Double value, Operator operator, StatisticalSignificanceType type) {
		super(value);
		this.operator = operator;
		statisticType = type;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void setValue(Object obj) throws Exception {
		if (!(obj instanceof Double))
			throw new Exception(
					"Could not set the value.  Parameter is of invalid data type: "
							+ obj);
		setValueObject((Double) obj);
	}
	/**
	 * Sets the pValue for this <code>StatisticalSignificanceDE</code> object
	 * 
	 * @param pValue
	 *            the pValue
	 */
	public void setValueObject(Double pValue) {
		if (pValue != null) {
			value = pValue;
		}
	}
	/**
	 * Returns the pValue for this StatisticalSignificanceDE obect.
	 * 
	 * @return the pValue for this <code>StatisticalSignificanceDE</code> object
	 */
	public Double getValueObject() {
		return (Double) getValue();
	}
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	public StatisticalSignificanceType getStatisticType() {
		return statisticType;
	}
	public void setStatisticType(StatisticalSignificanceType statisticType) {
		this.statisticType = statisticType;
	}
	/**
	 * Overrides the protected Object.clone() method exposing it as public.
	 * It performs a 2 tier copy, that is, it does a memcopy of the instance
	 * and then sets all the non-primitive data fields to clones of themselves.
	 * 
	 * @return -A minimum 2 deep copy of this object.
	 */
	public Object clone() {
		StatisticalSignificanceDE myStatisticalSignificanceDE = (StatisticalSignificanceDE) super.clone();
		return myStatisticalSignificanceDE;
	}

}
