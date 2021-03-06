/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.caintegrator.domain.analysis.snp.bean;

import gov.nih.nci.caintegrator.domain.study.bean.Study;

/**
 * @author sahnih
 *
 */
public class SNPAnalysisMethod {
	private Long id;
	private String methodDescription;
	private String methodName;
	private String methodType;
	private String represensitiveCode;
	private Study study;
	private Long displayOrder;
	private Double methodTypeOrder;
	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return Returns the methodDescription.
	 */
	public String getMethodDescription() {
		return methodDescription;
	}
	/**
	 * @param methodDescription The methodDescription to set.
	 */
	public void setMethodDescription(String methodDescription) {
		this.methodDescription = methodDescription;
	}
	/**
	 * @return Returns the methodName.
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @param methodName The methodName to set.
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	/**
	 * @return Returns the methodType.
	 */
	public String getMethodType() {
		return methodType;
	}
	/**
	 * @param methodType The methodType to set.
	 */
	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
	/**
	 * @return Returns the represensitiveCode.
	 */
	public String getRepresensitiveCode() {
		return represensitiveCode;
	}
	/**
	 * @param represensitiveCode The represensitiveCode to set.
	 */
	public void setRepresensitiveCode(String represensitiveCode) {
		this.represensitiveCode = represensitiveCode;
	}
	/**
	 * @return Returns the displayOrder.
	 */
	public Long getDisplayOrder() {
		return displayOrder;
	}
	/**
	 * @param displayOrder The displayOrder to set.
	 */
	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}
	/**
	 * @return Returns the study.
	 */
	public Study getStudy() {
		return study;
	}
	/**
	 * @param study The study to set.
	 */
	public void setStudy(Study study) {
		this.study = study;
	}
	/**
	 * @return the methodTypeOrder
	 */
	public Double getMethodTypeOrder() {
		return methodTypeOrder;
	}
	/**
	 * @param methodTypeOrder the methodTypeOrder to set
	 */
	public void setMethodTypeOrder(Double methodTypeOrder) {
		this.methodTypeOrder = methodTypeOrder;
	}
}
