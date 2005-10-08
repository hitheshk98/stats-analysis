package gov.nih.nci.caintegrator.ui.dtos;

import gov.nih.nci.caintegrator.enumeration.Operator;
import gov.nih.nci.caintegrator.exceptions.ValidationException;
import gov.nih.nci.caintegrator.query.Validatable;
/**
 * This ENUM will contain and describe the UI's definition of
 * the query to be performed.
 * 
 * @author BauerD
 *
 */
public enum QueryDTO implements Validatable{
	//Set the ENUM types
	CLINICAL_QUERY(),
	GENE_EXPRESSION_QUERY(),
	COPY_NUMBER_QUERY(),
	CUSTOM_QUERY();
	
	//Contains all the parameters for the query type
	private QueryParametersDTO parameters;
	private String queryID;
	private QueryDTO siblingQueryDTO;
	private Operator operator;
	
	public boolean validate() throws ValidationException{
		/*
		 * I am thinking that a validator factory might be a good
		 * idea.  We could replace the switch statement here with
		 * a single call to the validator factory passing this... 
		 * in the factory we could even allow for property defintions
		 * to allow us to change out the Validators as needed.
		 * -DB
		 */
		switch(this) {
		case CLINICAL_QUERY:
			//call ClinicalQueryValidator for the ParameterMap
			break;
		case GENE_EXPRESSION_QUERY:
			//call ClinicalQueryValidator for the ParameterMap
			break;
		case COPY_NUMBER_QUERY:
			//call CopyNumberQueryValidator for the ParameterMap
			break;
		default:
			throw new ValidationException("Unknown QueryDTOType");
		}
		return true;
	}
	public void setSiblingQueryDTO(QueryDTO sibling) {
		this.siblingQueryDTO = sibling;
	}
	public QueryDTO getSiblingQueryDTO() {
		return this.siblingQueryDTO;
	}
	
	public void setQueryParameters(QueryParametersDTO queryParameters) {
		this.parameters = queryParameters;
	}
	
	public QueryParametersDTO getQueryParameters(){
		return this.parameters;
	}

	/**
	 * @return Returns the uniqueID.
	 */
	public String getQueryID() {
		return queryID;
	}

	/**
	 * @param uniqueID The uniqueID to set.
	 */
	public void setQueryID(String uniqueID) {
		this.queryID = uniqueID;
	}
	/**
	 * @return Returns the operator.
	 */
	public Operator getOperator() {
		return operator;
	}
	/**
	 * @param operator The operator to set.
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

}
