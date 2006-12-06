package gov.nih.nci.caintegrator.studyQueryService.ihc;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import gov.nih.nci.breastCancer.dto.StudyParticipantCriteria;
import gov.nih.nci.breastCancer.service.StudyParticipantHandler;
import gov.nih.nci.caintegrator.domain.finding.bean.SpecimenBasedMolecularFinding;
import gov.nih.nci.caintegrator.domain.finding.clinical.bean.ClinicalFinding;
import gov.nih.nci.caintegrator.domain.finding.protein.ihc.bean.IHCFinding;
import gov.nih.nci.caintegrator.studyQueryService.dto.finding.SpecimenBasedMolecularFindingCriteria;
import gov.nih.nci.caintegrator.studyQueryService.dto.ihc.IHCFindingCriteria;
import gov.nih.nci.caintegrator.studyQueryService.dto.protein.ProteinBiomarkerCriteia;
import gov.nih.nci.caintegrator.studyQueryService.dto.study.SpecimenCriteria;
import gov.nih.nci.caintegrator.studyQueryService.finding.SpecimenBasedMolecularFindingHandler;
import gov.nih.nci.caintegrator.studyQueryService.protein.ProteinBiomarkerHandler;
import gov.nih.nci.caintegrator.util.HQLHelper;
import gov.nih.nci.caintegrator.util.HibernateUtil;

public abstract class IHCFindingHandler extends SpecimenBasedMolecularFindingHandler{
	
	 private static Logger logger = Logger.getLogger(IHCFindingHandler.class);
	 
	 protected abstract StringBuilder handleCriteria(SpecimenBasedMolecularFindingCriteria inCriteria,
             HashMap<String, Object> inParams,
             Session inSession);
	 
	 
	  protected abstract Class getFindingType();
	  
	  public Collection<? extends IHCFinding> getFindings(IHCFindingCriteria inCriteria){
		  
		  Set<? extends IHCFinding> theResults = new HashSet<IHCFinding>();
		  try {
		    logger.debug("Entering IHCFinding");
		    ProteinBiomarkerCriteia theProteinCriteria = inCriteria.getProteinBiomarkerCrit();
		    HashMap<String, Object> theParams = new HashMap<String, Object>();
	           

	        /////////////////////////////////////////////////////////////////////////////
	        // prepare HQL required to for handling corresponding subtype type finding
	        /////////////////////////////////////////////////////////////////////////////
	        StringBuilder theHQL = handleCriteria(inCriteria, theParams, null);
	        String theConnectorString = " WHERE ";
	        // Already a where clause
            if (theHQL.lastIndexOf("WHERE") != -1)
            {
                theConnectorString = " AND ";
            }
            
            if (theProteinCriteria != null)
            {
                theHQL.append(theConnectorString + " f.protein IN (");

                ProteinBiomarkerHandler theProteinHandler = theProteinCriteria.getHandler();
                StringBuilder theStudyHQL = theProteinHandler.handleCriteria(theProteinCriteria, theParams);
                theHQL.append(theStudyHQL + ")");
               
            }
		    
            Session theSession = HibernateUtil.getSession();
            theSession.beginTransaction();


            Query theQuery = theSession.createQuery(theHQL.toString());
            HQLHelper.setParamsOnQuery(theParams, theQuery);

            logger.info("HQL: " + theHQL.toString());
            long theStartTime = System.currentTimeMillis();
            Collection objs = theQuery.list();
            long theElapsedTime = System.currentTimeMillis() - theStartTime;

            logger.info("Elapsed time: " + theElapsedTime);

            theResults.addAll(objs);
           }
		  catch (Exception e)
	        {
	            logger.error("Error getting findings: ", e);
	            e.printStackTrace();
	        }

	        logger.debug("Exiting getFindings");

	        return theResults;		

     }
}
