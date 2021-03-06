/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.service.findings;

import java.util.List;

import gov.nih.nci.caintegrator.dto.query.ClassComparisonQueryDTO;
import gov.nih.nci.caintegrator.dto.query.HierarchicalClusteringQueryDTO;
import gov.nih.nci.caintegrator.dto.query.PrincipalComponentAnalysisQueryDTO;
import gov.nih.nci.caintegrator.dto.query.QueryDTO;
import gov.nih.nci.caintegrator.exceptions.FrameworkException;

/**
 * This interface will provide the method signatures of the
 * finding services available and pakaged in the caIntegrator
 * specification.
 * 
 * Whether this will use proxies will be determined by the 
 * finding strategies that it's impl uses.
 * 
 * @author BauerD
 *
 */


/**
* 
* 
*/

public interface FindingsFactory {
	/**
	 * Creates and returns a Kaplan-Meier Survival Plot finding result of
	 * the patients returned from the given query. 
	 * 
	 * @param query
	 * @return
	 */
	public KMFinding createKMFinding(QueryDTO query);
	/**
	 * Creates and returns a CopyNumberFinding for the query using
	 * the parameters for the given query.
	 * 
	 * @param query
	 * @return
	 */
	public CopyNumberFinding createCopyNumberFinding(QueryDTO query);
	
	/**
	 * Creates and returns a ClinicalFinding using the parameters
	 * of the given query described in the passed QueryDTOold.
	 * @param query
	 * @return
	 */
	public ClinicalFinding createClinicalFinding(QueryDTO query);
	
	/**
	 * Creates and returns a ClassComparisonFinding using the parameters passed in the given query
	 * @param query
	 * @return
	 * @throws FrameworkException 
	 */
	public ClassComparisonFinding createClassComparisonFinding(ClassComparisonQueryDTO query, String sessionID, String taskID) throws FrameworkException;
	/**
	 * Creates and returns a PCAFinding using the parameters of the given query 
	 * described in the passed QueryDTOold.
	 * @param query
	 * @return
	 */
	public PrincipalComponentAnalysisFinding createPCAFinding(PrincipalComponentAnalysisQueryDTO queryDTO, String sessionID, String taskID) throws FrameworkException;

	/**
	 * Creates and returns an HCAFinding from the given query described 
	 * in the passed QueryDTOold.
	 * 
	 * @param query
	 * @return
	 */
	public HCAFinding createHCAFinding(HierarchicalClusteringQueryDTO queryDTO, String sessionID, String taskID) throws FrameworkException;
	/**
	 * Creates and returns a Gene Expression Intensity finding
	 * (GEIntensityFinding) from the given query described 
	 * in the passed QueryDTOold.
	 * 
	 * @param query
	 * @return
	 */
	public GEIntensityFinding createGEIntensityFinding(QueryDTO query);
	
	/**
	 * I added this because I was thinking of a framework that would allow
	 * the creation at runtime of new strategies and queries without having
	 * to overwrite the caIntegrator framework.  Custom strategies/queries and
	 * associated parameterDTOs and validators that are considered more then
	 * novel or trivial can be added to the framework in subsequent releases
	 * but this will provide a mechanism whereby a person can write and use
	 * effectively a new query at any time.
	 * 
	 * I imagine that this will use reflection to instantiate the property file
	 * defined validators/findings/strategy.
	 * 
	 * @param query
	 * @return
	 */
	public Object createCustomFinding(QueryDTO query);
	/**
	  * Creates and returns a List<ClassComparisonFinding> using the parameters passed in the given query
	  * @param query
	  * @return
	  * @throws FrameworkException 
	  */
	 public CompoundClassComparisonFinding createCompoundClassComparisonFinding(List<ClassComparisonQueryDTO> queryList, String sessionID, String taskID, List<String> reporterList) throws FrameworkException;
    
	 
		
	
}
