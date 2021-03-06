/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.studyQueryService.germline;

import gov.nih.nci.caintegrator.domain.finding.bean.Finding;
import gov.nih.nci.caintegrator.domain.finding.variation.germline.bean.GenotypeFinding;
import gov.nih.nci.caintegrator.domain.study.bean.Specimen;
import gov.nih.nci.caintegrator.domain.annotation.snp.bean.SNPAnnotation;
import gov.nih.nci.caintegrator.studyQueryService.dto.FindingCriteriaDTO;
import gov.nih.nci.caintegrator.studyQueryService.dto.annotation.AnnotationCriteria;
import gov.nih.nci.caintegrator.studyQueryService.dto.germline.GenotypeFindingCriteriaDTO;
import gov.nih.nci.caintegrator.studyQueryService.dto.study.StudyParticipantCriteria;
import gov.nih.nci.caintegrator.studyQueryService.dto.study.StudyCriteria;
import gov.nih.nci.caintegrator.util.ArithematicOperator;
import gov.nih.nci.caintegrator.util.HQLHelper;
import gov.nih.nci.caintegrator.util.HibernateUtil;

import java.util.*;
import java.text.MessageFormat;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;


/**
 * Author: Ram Bhattaru
 * Date:   Jul 5, 2006
 * Time:   3:08:00 PM
 */
public class GenotypeFindingsHandler extends FindingsHandler {
    protected StringBuffer getTargetFindingHQL() {
         return new StringBuffer( " FROM GenotypeFinding " + TARGET_FINDING_ALIAS + " WHERE {0} {1} {2} ") ;
    }

    protected Collection<? extends Finding> executeFindingSetQuery(FindingCriteriaDTO critDTO, StringBuffer targetHQL,
                                                                Session session, int start, int end) throws Exception {
        GenotypeFindingCriteriaDTO findingCritDTO = (GenotypeFindingCriteriaDTO) critDTO;

        HashMap params = new HashMap();
        StringBuffer snpAnnotCond = new StringBuffer();

        /* 0. if AnnotationCrit is specified, then append required HQL (to snpAnnotCondition) for handling AnnotationCrit*/
        appendAnnotationCritHQL(critDTO, params, snpAnnotCond);

        /* 1. Handle GenoType Attributes Criteria itself  and populate targetHQL/params */
        StringBuffer selfHQL = new StringBuffer("");
        addGenoTypeAttributeCriteria(findingCritDTO, selfHQL, params);

        /* 2. get HQL to include StudyParticipantCriteria in the final query */
        String specimenCondition = new String("");
        StringBuffer specimenHQL;
        StudyParticipantCriteria spCrit = findingCritDTO.getStudyParticipantCriteria();
        if (spCrit != null) {
            specimenHQL = StudyParticipantCriteriaHandler.getSpecimenHQLWithParams(spCrit, session, params);
            if (specimenHQL != null && specimenHQL.length() > 0) {
                String condition = TARGET_FINDING_ALIAS + ".specimen.id IN ( {0} ) AND ";
                specimenCondition = MessageFormat.format(condition, new Object[] {specimenHQL});
            }
        }

        String formatedhql  = MessageFormat.format(targetHQL.toString(), new Object[] {
                                                                snpAnnotCond.toString(), specimenCondition, selfHQL} );

        String targetHQLWhereRemoved = HQLHelper.removeTrailingToken(new StringBuffer(formatedhql), "WHERE");
        String targetHQLANDRemoved = HQLHelper.removeTrailingToken(new StringBuffer(targetHQLWhereRemoved), "AND");

        Query q = session.createQuery(targetHQLANDRemoved);
        q.setFirstResult(start);
        q.setMaxResults(end - start);
        HQLHelper.setParamsOnQuery(params, q);
        List<GenotypeFinding> genotypeObjs =  q.list();

        HashSet<GenotypeFinding> findingsSet = new HashSet<GenotypeFinding>();
        findingsSet.addAll(genotypeObjs);
        return findingsSet;

    }

    protected List<? extends Finding> getConcreteTypedFindingList() {
        return new ArrayList<GenotypeFinding>();
    }

    protected Set getConcreteTypedFindingSet() {
        return new HashSet<GenotypeFinding>();
    }

    protected Class getTargeFindingType() {
        return GenotypeFinding.class;
    }
    private void addGenoTypeAttributeCriteria(GenotypeFindingCriteriaDTO crit, StringBuffer hql, HashMap params) {
         StudyCriteria studyCrit = crit.getStudyCriteria();

        Float score = crit.getQualityScore();
        String status = crit.getQCStatus();
        ArithematicOperator oper =
           (crit.getOperatorType() == null) ? ArithematicOperator.EQ : crit.getOperatorType();

        if (status != null) {
           hql.append(TARGET_FINDING_ALIAS + ".status = :status AND ");
           params.put("status", status);
        }

        if (score != null) {
            String clause =  TARGET_FINDING_ALIAS + ".qualityScore {0} :score AND ";
            String condition = HQLHelper.prepareCondition(oper);
            String formattedClause = MessageFormat.format(clause, new Object[] {condition} );
            hql.append(formattedClause);
            params.put("score", score);
        }

        if (studyCrit != null) {
                if (studyCrit.getId() != null) {
                    hql.append( TARGET_FINDING_ALIAS + ".study.id = :studyId AND ");
                    params.put("studyId", studyCrit.getId());
                }
        }

    }

    protected void initializeProxies(Collection<? extends Finding> findings, Session session) {

        /* 1. initialize SNPAnnotations */
        Collection<Long> snpAnnotsIDs = new HashSet<Long>();
        for (Iterator<? extends Finding> iterator = findings.iterator(); iterator.hasNext();) {
                GenotypeFinding finding =  (GenotypeFinding) iterator.next();
                snpAnnotsIDs.add(finding.getSnpAnnotation().getId());
        }
        if (snpAnnotsIDs.size() > 0) {
              ArrayList arrayIDs = new ArrayList(snpAnnotsIDs );
              for (int i = 0; i < arrayIDs.size();) {
                  Collection values = new ArrayList();
                  int begIndex = i;
                  i += BatchFindingsHandler.IN_PARAMETERS ;
                  int lastIndex = (i < arrayIDs.size()) ? i : (arrayIDs.size());
                  values.addAll(arrayIDs.subList(begIndex,  lastIndex));
                  Criteria crit = session.createCriteria(SNPAnnotation.class).add(Restrictions.in("id", values));
                  crit.list();
              }
        }

        /* 2. initialize Specimens along with associated StudyParticipants */
        Collection<String> specimenIDs = new HashSet<String>();
        for (Iterator<? extends Finding> iterator = findings.iterator(); iterator.hasNext();) {
            GenotypeFinding finding =  (GenotypeFinding) iterator.next();
            specimenIDs.add(finding.getSpecimen().getSpecimenIdentifier());
        }
        
        if (specimenIDs.size() > 0)  {
            ArrayList arrayIDs = new ArrayList(specimenIDs );
            for (int i = 0; i < arrayIDs.size();) {
                Collection values = new ArrayList();
                int begIndex = i;
                i += BatchFindingsHandler.IN_PARAMETERS ;
                int lastIndex = (i < arrayIDs.size()) ? i : (arrayIDs.size());
                values.addAll(arrayIDs.subList(begIndex,  lastIndex));
                Criteria specimenCrit = session.createCriteria(Specimen.class)
                                        .add(Restrictions.in("specimenIdentifier", values))
                                        .setFetchMode("studyParticipant", FetchMode.EAGER);

                specimenCrit .list();
            }
        }
    }
    public Collection<? extends Finding> getFindings(FindingCriteriaDTO critDTO, int fromIndex, int toIndex)
    throws Exception {
            Collection<? extends Finding> findings ;
            Session session = getSessionFactory().getCurrentSession();
            findings = getMyFindings(critDTO, session, fromIndex, toIndex);
            initializeProxies(findings, session);
            session.clear();
            return findings;
    }
}
