package gov.nih.nci.caintegrator.studyQueryService.germline;

import gov.nih.nci.caintegrator.domain.analysis.snp.bean.SNPAssociationAnalysis;
import gov.nih.nci.caintegrator.domain.analysis.snp.bean.SNPAssociationFinding;
import gov.nih.nci.caintegrator.domain.annotation.snp.bean.SNPAnnotation;
import gov.nih.nci.caintegrator.domain.annotation.gene.bean.GeneBiomarker;
import gov.nih.nci.caintegrator.domain.finding.bean.Finding;
import gov.nih.nci.caintegrator.studyQueryService.dto.FindingCriteriaDTO;
import gov.nih.nci.caintegrator.studyQueryService.dto.study.StudyCriteria;
import gov.nih.nci.caintegrator.studyQueryService.dto.annotation.AnnotationCriteria;
import gov.nih.nci.caintegrator.studyQueryService.dto.germline.*;
import gov.nih.nci.caintegrator.util.ArithematicOperator;
import gov.nih.nci.caintegrator.util.HQLHelper;

import java.util.*;
import java.text.MessageFormat;

import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;

/**
 * Author: Ram Bhattaru
 * Date:   Jul 21, 2006
 * Time:   2:20:11 PM
 */

public class SNPAssociationFindingsHandler extends FindingsHandler {

    protected Class getTargeFindingType() {
         return SNPAssociationFinding.class;
    }

    protected Collection<? extends Finding> getMyFindings(FindingCriteriaDTO critDTO, Set<String> snpAnnotationIDs,  Session session, int startIndex, int endIndex) {
        List<SNPAssociationFinding>  snpAssociationFindings =
                            Collections.synchronizedList(new ArrayList<SNPAssociationFinding>());
        HashSet<SNPAssociationFinding>  snpAssociationFindingsSet =
                            new HashSet<SNPAssociationFinding>();


        /* if AnnotationCriteria results in no SNPs then return no findings */
          if (snpAnnotationIDs != null && snpAnnotationIDs.size() == 0)
              return snpAssociationFindings;

        SNPAssociationFindingCriteriaDTO findingCritDTO = (SNPAssociationFindingCriteriaDTO) critDTO;
        StringBuffer targetHQL = new StringBuffer(
                            " FROM SNPAssociationFinding " + TARGET_FINDING_ALIAS +
                             " JOIN "+ TARGET_FINDING_ALIAS +
                              ".snpAnnotation snpAnnot " + " JOIN "+ TARGET_FINDING_ALIAS +
                              ".snpAssociationAnalysis analysis "  + " {0} {1} " +
                              " WHERE {2} {3} {4} "
                            );

        if (snpAnnotationIDs != null && snpAnnotationIDs.size() > 0)
        {
            ArrayList arrayIDs = new ArrayList(snpAnnotationIDs);
            for (int i = critDTO.getIndex(); i < arrayIDs.size();) {
                StringBuffer hql = new StringBuffer("").append(targetHQL);
                Collection values = new ArrayList();
                int begIndex = i;
                i += IN_PARAMETERS ;
                int lastIndex = (i < arrayIDs.size()) ? i : (arrayIDs.size());
                values.addAll(arrayIDs.subList(begIndex,  lastIndex));
                Collection<SNPAssociationFinding> batchFindings =
                        executeAnnotationQueryForFindingSets(
                                findingCritDTO, values, session, hql, startIndex, endIndex);
                snpAssociationFindingsSet.addAll(batchFindings);
                if (snpAssociationFindingsSet.size() >= (endIndex - startIndex + 1)) {
                    snpAssociationFindings.addAll(snpAssociationFindingsSet);
                	//critDTO.setIndex(lastIndex);
                    snpAssociationFindingsSet =  new HashSet<SNPAssociationFinding>();
                    return snpAssociationFindings;
                }
            }
            /* means each time it never gotten more than 500 results.  So add to final results */
            snpAssociationFindings.addAll(snpAssociationFindingsSet);

        } else { /* means no AnnotationCriteria was specified in the FindingCriteriaDTO  */
             Collection<SNPAssociationFinding> findings = executeQueryForFindingSets(
                    critDTO, session, targetHQL, startIndex, endIndex);
              snpAssociationFindings.addAll(findings);
        }
        return snpAssociationFindings;
    }

    /**
     *
     * @param critDTO
     * @param snpAnnotationIDs
     * @param session
     * @param targetHQL
     * @param start This is the start index.  A value of -1 represents not to use this param
     * in the actual query to Hibernate
     * @param end This is the end Index. A value of -1 represents not to use this param
     * in the actual query to Hibernate
     * @return  Collection of SNPAssociationFindings
     */
    protected Collection<SNPAssociationFinding> executeAnnotationQueryForFindingSets(FindingCriteriaDTO critDTO,
                                                                                     Collection<String> snpAnnotationIDs, Session session,
                                                                                     StringBuffer targetHQL, int start, int end ) {

        SNPAssociationFindingCriteriaDTO  findingCritDTO= (SNPAssociationFindingCriteriaDTO) critDTO;
        Collection<SNPAssociationFinding>  snpAssociationFindings =
                                    new ArrayList<SNPAssociationFinding>();

        final HashMap params = new HashMap();

        /* 1.  Include SNPAssociationFinding attribute criteria in  TargetFinding query */
        StringBuffer myAttributeHql = new StringBuffer("");
        addSNPAssociationFindingAttriuteCrit(findingCritDTO, myAttributeHql, params);

        /* 2. Include HQL to handle Annotation Criteria to snpAnnotJoin & snpAnnotCond */
        StringBuffer snpAnnotJoin = new StringBuffer("");
        StringBuffer snpAnnotCond = new StringBuffer("");
        appendAnnotationCriteriaHQL(snpAnnotationIDs, snpAnnotJoin, snpAnnotCond, params);

        /* 3. Include SNPAnalysisGroup Criteria in TargetFinding query by converting and adding to AnalysisGroupCriteria */
        handleAnalysisGroupCriteria(findingCritDTO, session);

        /* 4. Include SNPAssociationAnalysisCriteria Criteria in TargetFinding query */
        HashMap h = applySnpassociationAnalysisCriteria(findingCritDTO, params);
        String analysisJoin = (String) h.get("ANALYSIS_JOIN");
        String analysisCondition = h.get("ANALYSIS_COND").toString();

        /* 5. Assemble the Query with all criteria conditions together */
        String tmpHQL = MessageFormat.format(targetHQL.toString(), new Object[] {
                 snpAnnotJoin.toString(), "", myAttributeHql, snpAnnotCond.toString(), analysisCondition});
        String hql= HQLHelper.removeTrailingToken(new StringBuffer(tmpHQL), "AND");
        String noORHQL = HQLHelper.removeTrailingToken(new StringBuffer(hql), "OR");
        String finalHQL = HQLHelper.removeTrailingToken(new StringBuffer(noORHQL), "WHERE");

        /* 6. Now execute the final TargetFinding query and return results  */
        Query q = session.createQuery(finalHQL);
        HQLHelper.setParamsOnQuery(params, q);

        if (start == -1 || end == -1) {
            // do not use these indexes.  Just retrieve everything
        }
        else { // set the index values
            q.setFirstResult(0); //RAM: 09/22/06 changed back to original before ftp bug
            q.setMaxResults(end - start);
        }
        Iterator triplets = q.list().iterator();
        while(triplets.hasNext()) {
            Object[] triplet = (Object[]) triplets.next();
            SNPAssociationFinding finding = (SNPAssociationFinding) triplet[0];
            SNPAnnotation snpAnnot = (SNPAnnotation) triplet[1];
            finding.setSnpAnnotation(snpAnnot);
            SNPAssociationAnalysis analysis = (SNPAssociationAnalysis) triplet[2];
            finding.setSnpAssociationAnalysis(analysis);
            snpAssociationFindings.add(finding);
        }

        return snpAssociationFindings ;
    }

    protected Collection<SNPAssociationFinding> executeQueryForFindingSets(
                        FindingCriteriaDTO critDTO, Session session,
                        StringBuffer targetHQL, int start, int end) {

        SNPAssociationFindingCriteriaDTO  findingCritDTO= (SNPAssociationFindingCriteriaDTO) critDTO;
        Collection<SNPAssociationFinding>  snpAssociationFindings =
                                    new ArrayList<SNPAssociationFinding>();

        final HashMap params = new HashMap();

        /* 1.  Include SNPAssociationFinding attribute criteria in  TargetFinding query */
        StringBuffer myAttributeHql = new StringBuffer("");
        addSNPAssociationFindingAttriuteCrit(findingCritDTO, myAttributeHql, params);

        /* 2. Include HQL to handle Annotation Criteria to snpAnnotJoin & snpAnnotCond */
        StringBuffer snpAnnotJoin = new StringBuffer("");
        StringBuffer snpAnnotCond = new StringBuffer("");

        /* 3. Include SNPAnalysisGroup Criteria in TargetFinding query by converting and adding to AnalysisGroupCriteria */
        handleAnalysisGroupCriteria(findingCritDTO, session);

        /* 4. Include SNPAssociationAnalysisCriteria Criteria in TargetFinding query */
        HashMap h = applySnpassociationAnalysisCriteria(findingCritDTO, params); // TARGETHQL //
        String analysisJoin = (String) h.get("ANALYSIS_JOIN");
        String analysisCondition = h.get("ANALYSIS_COND").toString();

        /* 5. Assemble the Query with all criteria conditions together */
        String tmpHQL = MessageFormat.format(targetHQL.toString(), new Object[] {
                 snpAnnotJoin.toString(), analysisJoin, myAttributeHql, snpAnnotCond.toString(), analysisCondition});
        String hql= HQLHelper.removeTrailingToken(new StringBuffer(tmpHQL), "AND");
        String noORHQL = HQLHelper.removeTrailingToken(new StringBuffer(hql), "OR");
        String finalHQL = HQLHelper.removeTrailingToken(new StringBuffer(noORHQL), "WHERE");

        /* 6. Now execute the final TargetFinding query and return results  */
        Query q = session.createQuery(finalHQL);
        HQLHelper.setParamsOnQuery(params, q);
        q.setFirstResult(start);
        q.setMaxResults(end - start);
        Iterator triplets = q.list().iterator();
        while(triplets.hasNext()) {
            Object[] triplet = (Object[]) triplets.next();
            SNPAssociationFinding finding = (SNPAssociationFinding) triplet[0];
            SNPAnnotation snpAnnot = (SNPAnnotation) triplet[1];
            finding.setSnpAnnotation(snpAnnot);
            SNPAssociationAnalysis analysis = (SNPAssociationAnalysis) triplet[2];
            finding.setSnpAssociationAnalysis(analysis);
            snpAssociationFindings.add(finding);
        }

        return snpAssociationFindings ;
    }

    private void addSNPAssociationFindingAttriuteCrit(SNPAssociationFindingCriteriaDTO findingCritDTO, StringBuffer myHql,HashMap params) {
        //StringBuffer myHql = new StringBuffer("");
        StudyCriteria studyCrit = findingCritDTO.getStudyCriteria();

        Float pValue = findingCritDTO.getpValue();
        if (pValue != null) {
            ArithematicOperator pValueOP = findingCritDTO.getpValueOperator();
            if (pValueOP == null) pValueOP = ArithematicOperator.EQ; // (default)
            StringBuffer hql = new StringBuffer("");
            String condition = HQLHelper.prepareCondition(pValueOP);
            String clause = TARGET_FINDING_ALIAS + ".pvalue {0} :pValue AND ";
            String formattedClause = MessageFormat.format(clause, new Object[] {condition} );
            hql.append(formattedClause);
            params.put("pValue", pValue);
            myHql.append(hql);
        }

        Integer rank = findingCritDTO.getRank();
        if (rank != null) {
            ArithematicOperator rankOP = findingCritDTO.getRankOperator();
            if (rankOP == null) rankOP = ArithematicOperator.EQ; // (default)
            StringBuffer hql = new StringBuffer("");
            String condition = HQLHelper.prepareCondition(rankOP);
            String clause = TARGET_FINDING_ALIAS + ".rank {0} :rank AND ";
            String formattedClause = MessageFormat.format(clause, new Object[] {condition} );
            hql.append(formattedClause);
            params.put("rank", rank);
            myHql.append(hql);
        }
        if (studyCrit != null) {
            if (studyCrit.getName() != null) {
                myHql.append( TARGET_FINDING_ALIAS + ".study.name = :studyName AND ");
                params.put("studyName", studyCrit.getName().trim());
            }
        }
       // return myHql;
    }

    private void handleAnalysisGroupCriteria(SNPAssociationFindingCriteriaDTO findingCritDTO, Session session) {
        AnalysisGroupCriteria groupCrit = findingCritDTO.getAnalysisGroupCriteria();
        StudyCriteria studyCrit = findingCritDTO.getStudyCriteria();
        if (groupCrit != null) {
            String[] names = groupCrit.getNames();
            if (names != null && names.length > 0) {
               Criteria c = session.createCriteria(SNPAssociationAnalysis.class).createAlias("analysisGroupCollection", "analysisGroup").
                                          add(Restrictions.in("analysisGroup.name", names));

               List<SNPAssociationAnalysis> assocAnalysisObjs = c.list();

               /* 1. Convert these SNPAssociationAnalysis in to SNPAssociationAnalysisCriteria */
               Collection analysisCrits = new ArrayList<SNPAssociationAnalysisCriteria>();
               for (int i = 0; i < assocAnalysisObjs.size(); i++) {
                    SNPAssociationAnalysis assocAnalysisObj =  assocAnalysisObjs.get(i);
                    SNPAssociationAnalysisCriteria methodAndNameCrit =
                                                    new SNPAssociationAnalysisCriteria(studyCrit.getName());
                    methodAndNameCrit.setMethods(assocAnalysisObj.getMethods());
                    methodAndNameCrit.setName(assocAnalysisObj.getName());
                    analysisCrits.add(methodAndNameCrit);
               }

               /* 2. Now set these SNPAssociationAnalysisCriteria objects back to SNPAssociationFindingCriteriaDTO */
               Collection<SNPAssociationAnalysisCriteria> anaCrits = findingCritDTO.getSnpAssociationAnalysisCriteriaCollection();
               if (anaCrits == null) {
                 /* means no SNPAssociationAnalysisCriteria was specified in the Query DTO
                    (SNPAssociationFindingCriteriaDTO) So intantiate one and add it to QueryDTO */
                  anaCrits = new ArrayList<SNPAssociationAnalysisCriteria>();
                  findingCritDTO.setSnpAssociationAnalysisCriteriaCollection(anaCrits);
               }
               anaCrits.addAll(analysisCrits);
            }
        }
    }

    /**
     * This method returns SNPAssociationAnalysis Condition & Join in a hashMap
     * @param findingCritDTO
     * @param params
     * @param targetHQL
     * @return Map containing Condition & Join
     */
    private HashMap applySnpassociationAnalysisCriteria(SNPAssociationFindingCriteriaDTO findingCritDTO,
                                                        HashMap params) {
        //String analysisJoin = "";
        StringBuffer analysisCond = new StringBuffer("");
        Collection<SNPAssociationAnalysisCriteria>  anaCrits =
                findingCritDTO.getSnpAssociationAnalysisCriteriaCollection();
        HashMap h = new HashMap();

        if (anaCrits != null && anaCrits.size() > 0) {
            int suffix = 0;
            for (SNPAssociationAnalysisCriteria anaCritObj : anaCrits) {
                String methods = anaCritObj.getMethods();
                String name = anaCritObj.getName();
                if ((methods == null) && (name == null)) continue;
                StringBuffer methodTrailingAND = new StringBuffer("");
                if (methods != null) {
                    String methodParam = "methods" + suffix;
                    StringBuffer tmpAnalysisCond = new StringBuffer("");
                    tmpAnalysisCond.append(TARGET_FINDING_ALIAS + ".snpAssociationAnalysis.methods = :{0} ");
                    analysisCond.append(MessageFormat.format(tmpAnalysisCond.toString(), new Object[]{methodParam}));
                    params.put(methodParam, methods);
                    methodTrailingAND.append(" AND ");
                }

                if (name != null) {
                    analysisCond.append(methodTrailingAND);
                    String nameParam = "name" + suffix;
                    StringBuffer tmpAnalysisCond = new StringBuffer("");
                    tmpAnalysisCond.append(TARGET_FINDING_ALIAS + ".snpAssociationAnalysis.name = :{0} ");
                    analysisCond.append(MessageFormat.format(tmpAnalysisCond.toString(), new Object[]{nameParam}));
                    params.put(nameParam, name);
                }
                analysisCond.append(" OR ");
                suffix++;
            }
            String analCondition = HQLHelper.removeTrailingToken(new StringBuffer(analysisCond), " OR");
            String andAppendedhql = (new StringBuffer(analCondition).append(" AND ")).toString();
            h.put("ANALYSIS_JOIN", new String(""));
            //h.put("ANALYSIS_COND", finalAnalysisCond);
            h.put("ANALYSIS_COND", andAppendedhql);

        } else  {
            h.put("ANALYSIS_JOIN", new String(""));
            h.put("ANALYSIS_COND", analysisCond);
        }


        return h;
    }

    protected void initializeProxies(Collection<? extends Finding> findings, Session session) {
        List<GeneBiomarker> gbObjs = new ArrayList<GeneBiomarker>();
        for (Iterator<? extends Finding> iterator = findings.iterator(); iterator.hasNext();) {
           SNPAssociationFinding finding = (SNPAssociationFinding) iterator.next();
           gbObjs.addAll(finding.getSnpAnnotation().getGeneBiomarkerCollection());
        }
        Hibernate.initialize(gbObjs);
        gbObjs = null;
    }

     protected void sendMyFindings(FindingCriteriaDTO critDTO, Set<String> snpAnnotationIDs,
                                   Session session, List toBePopulated) {

        /* if AnnotationCriteria results in no SNPs then return no findings */
          if (snpAnnotationIDs != null && snpAnnotationIDs.size() == 0)
              return ;

        SNPAssociationFindingCriteriaDTO findingCritDTO = (SNPAssociationFindingCriteriaDTO) critDTO;
        StringBuffer targetHQL = new StringBuffer(
                            " FROM SNPAssociationFinding " + TARGET_FINDING_ALIAS +
                             " JOIN "+ TARGET_FINDING_ALIAS +
                              ".snpAnnotation snpAnnot " + " JOIN "+ TARGET_FINDING_ALIAS +
                              ".snpAssociationAnalysis analysis "  + " {0} {1} " +
                              " WHERE {2} {3} {4} "
                            );

        if (snpAnnotationIDs != null && snpAnnotationIDs.size() > 0) {
            sendeFindingsWithAnnotationCriteria(snpAnnotationIDs, targetHQL, findingCritDTO,
                                                                             session, toBePopulated);
        }  else { /* means no AnnotationCriteria was specified in the FindingCriteriaDTO */
            sendFindingsWithoutAnnotationCriteria(critDTO, session, targetHQL, toBePopulated);
        }
        return ;
    }

    protected List<? extends Finding> getConcreteTypedFindingList() {
        return new ArrayList<SNPAssociationFinding>();
    }
    protected Set getConcreteTypedFindingSet() {
        return new HashSet<SNPAssociationFinding>();
    }

    private void sendeFindingsWithAnnotationCriteria(
                                Set<String> snpAnnotationIDs, StringBuffer targetHQL,
                                FindingCriteriaDTO findingCritDTO,
                                Session session, List toBePopulated) {
        getConcreteTypedFindingList();
        List  snpAssociationFindings = getConcreteTypedFindingList();
        ArrayList arrayIDs = new ArrayList(snpAnnotationIDs);
        for (int i = 0; i < arrayIDs.size();) {
             StringBuffer hql = new StringBuffer("").append(targetHQL);
             Collection values = new ArrayList();
             int begIndex = i;
             i += IN_PARAMETERS ;
             int lastIndex = (i < arrayIDs.size()) ? i : (arrayIDs.size());
             values.addAll(arrayIDs.subList(begIndex,  lastIndex));

             /* send -1 for start & end index to indicate these values should not be included
                in the final Hibernate Query */
             Collection<? extends Finding> currentFindings =
                                    executeQueryForPopulating(findingCritDTO, values,
                                      session, hql, -1, -1);

             /* convert these  currentFindings in to a List for convenience */
             snpAssociationFindings.addAll(currentFindings );
             initializeProxies(snpAssociationFindings, session);

             while (snpAssociationFindings.size() >= BATCH_OBJECT_INCREMENT )
                 populateCurrentResultSet(snpAssociationFindings, toBePopulated, session);
        }

        /* Now write remaining findings i.e. less than 500 in one call */
        if (snpAssociationFindings != null)
            populateCurrentResultSet(snpAssociationFindings, toBePopulated, session);

        /* Finally after all the results were written, write an empty Object (HashSet of size=0
          to indicate the caller that all results were written */
         populateCurrentResultSet(getConcreteTypedFindingList(), toBePopulated, session);
    }

    protected void sendFindingsWithoutAnnotationCriteria(FindingCriteriaDTO critDTO,
                                              Session session, StringBuffer targetHQL,
                                              List toBePopulated) {
        Collection findings = null;
        int start = 0;
        int end = BATCH_OBJECT_INCREMENT ;
        Set toBeSent = null;

        do {
            findings =  executeQueryForPopulating(
                      critDTO, null, session, targetHQL, start, end);

            initializeProxies(findings, session);

            toBeSent = new HashSet<SNPAssociationFinding>();
            toBeSent.addAll(findings);
            process(toBePopulated,  toBeSent, session);
            start += BATCH_OBJECT_INCREMENT;
            end += BATCH_OBJECT_INCREMENT;;

        }  while(findings.size() >= BATCH_OBJECT_INCREMENT );

        /* send empty data object to let the client know that no more results are present */
        process(toBePopulated, getConcreteTypedFindingSet(),session);
    }



    /**
     *
     * @param critDTO
     * @param snpAnnotationIDs
     * @param session
     * @param targetHQL
     * @param start This is the start index.  A value of -1 represents not to use this param
     * in the actual query to Hibernate
     * @param end This is the end Index. A value of -1 represents not to use this param
     * in the actual query to Hibernate
     * @return  Collection of SNPAssociationFindings
     */
    protected Collection<? extends Finding> executeQueryForPopulating(FindingCriteriaDTO critDTO,
                            Collection<String> snpAnnotationIDs,
                            Session session, StringBuffer targetHQL
                            ,int start, int end
                            ) {

        SNPAssociationFindingCriteriaDTO  findingCritDTO= (SNPAssociationFindingCriteriaDTO) critDTO;
        Collection<SNPAssociationFinding>  snpAssociationFindings =
                                    new ArrayList<SNPAssociationFinding>();

         HashMap params = new HashMap();

        /* 1.  Include SNPAssociationFinding attribute criteria in  TargetFinding query */
        StringBuffer myAttributeHql = new StringBuffer("");
        addSNPAssociationFindingAttriuteCrit(findingCritDTO, myAttributeHql,params);

        /* 2. Include HQL to handle Annotation Criteria to snpAnnotJoin & snpAnnotCond */
        StringBuffer snpAnnotJoin = new StringBuffer("");
        StringBuffer snpAnnotCond = new StringBuffer("");
        appendAnnotationCriteriaHQL(snpAnnotationIDs, snpAnnotJoin, snpAnnotCond, params);

        /* 3. Include SNPAnalysisGroup Criteria in TargetFinding query by converting and adding to AnalysisGroupCriteria */
        handleAnalysisGroupCriteria(findingCritDTO, session);

        /* 4. Include SNPAssociationAnalysisCriteria Criteria in TargetFinding query */
        HashMap h = applySnpassociationAnalysisCriteria(findingCritDTO, params);  //, targetHQL
        String analysisJoin = (String) h.get("ANALYSIS_JOIN");
        String analysisCondition = h.get("ANALYSIS_COND").toString();

        /* 5. Assemble the Query with all criteria conditions together */
        String tmpHQL = MessageFormat.format(targetHQL.toString(), new Object[] {
                 snpAnnotJoin.toString(), analysisJoin, myAttributeHql, snpAnnotCond.toString(), analysisCondition});
        String hql= HQLHelper.removeTrailingToken(new StringBuffer(tmpHQL), "AND");
        String noORHQL = HQLHelper.removeTrailingToken(new StringBuffer(hql), "OR");
        String finalHQL = HQLHelper.removeTrailingToken(new StringBuffer(noORHQL), "WHERE");

        /* 6. Now execute the final TargetFinding query and return results  */
        Query q = session.createQuery(finalHQL);
        HQLHelper.setParamsOnQuery(params, q);

        if (start == -1 || end == -1) {
            // do not use these indexes.  Just retrieve everything
        }
        else { // set the index values
            q.setFirstResult(start);
            q.setMaxResults(end - start);
        }
        List l = q.list();
        Iterator triplets = l.iterator();
        while(triplets.hasNext()) {
            Object[] triplet = (Object[]) triplets.next();
            SNPAssociationFinding finding = (SNPAssociationFinding) triplet[0];
            SNPAnnotation snpAnnot = (SNPAnnotation) triplet[1];
            finding.setSnpAnnotation(snpAnnot);
            SNPAssociationAnalysis analysis = (SNPAssociationAnalysis) triplet[2];
            finding.setSnpAssociationAnalysis(analysis);
            snpAssociationFindings.add(finding);
        }

     /*   for (int i = 0; i < l.size(); i++) {
            l.set(i,null);

        }*/
        return snpAssociationFindings ;
    }

    protected StringBuffer addHQLForFindingAttributeCriteria(FindingCriteriaDTO crit, StringBuffer hql, HashMap params,
                                                        Session session) { // StringBuffer totalHQL,

        SNPAssociationFindingCriteriaDTO findingCritDTO = (SNPAssociationFindingCriteriaDTO)crit;
        /* 1. Include SNPAnalysisGroup Criteria in TargetFinding query by converting and adding to AnalysisGroupCriteria */
        handleAnalysisGroupCriteria(findingCritDTO, session);
        HashMap h = applySnpassociationAnalysisCriteria(findingCritDTO, params);       //, hql


        String analysisJoin = " JOIN "+ TARGET_FINDING_ALIAS +
                              ".snpAssociationAnalysis analysis ";
        //(String) h.get("ANALYSIS_JOIN");
        String analysisCondition = h.get("ANALYSIS_COND").toString();
        addSNPAssociationFindingAttriuteCrit((SNPAssociationFindingCriteriaDTO) crit, hql, params);
        StringBuffer temp = new StringBuffer(
                    MessageFormat.format( hql.toString(), new Object[] {"","",analysisCondition, analysisJoin}) );
                    //totalHQL.toString()
        return temp;
     }

    protected Collection<? extends Finding> getFindingsFromResults(List results) {
        Collection<SNPAssociationFinding> snpAssociationFindings = new ArrayList<SNPAssociationFinding>(results.size());
        Iterator resultsIter = results.iterator();
        while(resultsIter.hasNext()) {
            Object[] triplet = (Object[]) resultsIter.next();
            SNPAssociationFinding finding = (SNPAssociationFinding) triplet[0];
            SNPAnnotation snpAnnot = (SNPAnnotation) triplet[3];
            finding.setSnpAnnotation(snpAnnot);
            SNPAssociationAnalysis analysis = (SNPAssociationAnalysis) triplet[1];
            finding.setSnpAssociationAnalysis(analysis);
            snpAssociationFindings.add(finding);
        }
        return snpAssociationFindings;
    }

    public Collection<? extends Finding> executePanelOnlySearch(FindingCriteriaDTO findingCritDTO, Session session, int start, int end) {
         AnnotationCriteria annotCrit = findingCritDTO.getAnnotationCriteria();
         Collection<? extends Finding> findings = null;
/*
         //BEFORE PERFORMANCE RE-FACTORING
         StringBuffer hql = new StringBuffer(" FROM SNPAssociationFinding {0}, SNPAssay s JOIN s.snpAnnotation snpAnnot {3} "
                                              + " WHERE s.snpPanel.id = {1} AND {2} " );

*/

        StringBuffer hql = new StringBuffer(" FROM SNPAssociationFinding {0}, SNPAssay s JOIN " +
                                             TARGET_FINDING_ALIAS + ".snpAnnotation " +
                                             " {3} WHERE s.snpPanel.id = {1} AND " +
                                             TARGET_FINDING_ALIAS + ".snpAnnotation = s.snpAnnotation " +
                                             " AND {2} " );



         StringBuffer panelHQL = new StringBuffer (
                            MessageFormat.format( hql.toString(), new Object[] {
                                TARGET_FINDING_ALIAS, annotCrit.getPanelCriteria().getSnpPanelID()}) );

         // add  HQL for Finding criteria attributes itself
         StringBuffer findingHQL = new StringBuffer("");
         HashMap params = new HashMap();
         StringBuffer finalPanelHQL = addHQLForFindingAttributeCriteria(findingCritDTO, panelHQL, params,  session);
         StringBuffer finalHQLWithAND = new StringBuffer(HQLHelper.removeTrailingToken(new StringBuffer(finalPanelHQL), "AND"));
         finalHQLWithAND.append(" AND ");
         String finalHQL = HQLHelper.removeTrailingToken(new StringBuffer(finalHQLWithAND), "AND");

         Query q = session.createQuery(finalHQL);
         HQLHelper.setParamsOnQuery(params, q);
         q.setFirstResult(start);
         q.setMaxResults(end);
         List results = q.list();
         findings = getFindingsFromResultsForPanelSearch(results);
         return findings;
    }

    protected Collection<? extends Finding> getFindingsFromResultsForPanelSearch(List results) {
        Collection<SNPAssociationFinding> snpAssociationFindings = new ArrayList<SNPAssociationFinding>(results.size());
        Iterator resultsIter = results.iterator();
        while(resultsIter.hasNext()) {
            Object[] triplet = (Object[]) resultsIter.next();
            SNPAssociationFinding finding = (SNPAssociationFinding) triplet[0];
            SNPAnnotation snpAnnot = (SNPAnnotation) triplet[1];
            finding.setSnpAnnotation(snpAnnot);
            SNPAssociationAnalysis analysis = (SNPAssociationAnalysis) triplet[2];
            finding.setSnpAssociationAnalysis(analysis);
            snpAssociationFindings.add(finding);
        }
        return snpAssociationFindings;
    }


}
