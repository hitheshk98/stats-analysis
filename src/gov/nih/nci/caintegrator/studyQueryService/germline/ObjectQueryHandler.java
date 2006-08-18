package gov.nih.nci.caintegrator.studyQueryService.germline;

import gov.nih.nci.caintegrator.studyQueryService.dto.study.StudyCriteria;
import gov.nih.nci.caintegrator.studyQueryService.dto.germline.SNPAssociationAnalysisCriteria;
import gov.nih.nci.caintegrator.util.HQLHelper;
import gov.nih.nci.caintegrator.util.HibernateUtil;
import gov.nih.nci.caintegrator.domain.study.bean.Study;
import gov.nih.nci.caintegrator.domain.analysis.snp.bean.SNPAssociationAnalysis;
import gov.nih.nci.caintegrator.domain.annotation.snp.bean.SNPAnnotation;


import java.util.*;
import java.text.MessageFormat;

import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Projections;

/**
 * Author: Ram Bhattaru
 * Date:   Aug 16, 2006
 * Time:   7:39:39 AM
 */

public class ObjectQueryHandler {
    private static List<String> CHROMOSOME_LIST = null;

    public static Collection<Study> getStudyObjects(StudyCriteria studyCrit) {
        if (studyCrit == null) return new ArrayList<Study>();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        HashMap params = new HashMap();
        String studyCritHQL = " FROM Study s WHERE {0} {1} ";
        StringBuffer studyNameJoin = new StringBuffer("");
        String studyName = studyCrit.getName();
        String sponsorStudyIdentifier = studyCrit.getSponsorStudyIdentifier();

        if ((studyName != null) && (studyName.length() > 0)) {
           studyNameJoin.append(" s.name = :studyName  AND ");
           params.put("studyName", studyName);
        }

        StringBuffer sponsorJoin = new StringBuffer("");
        if ((sponsorStudyIdentifier != null) && (sponsorStudyIdentifier.length() > 0))  {
             sponsorJoin .append(" s.sponsorStudyIdentifier = :sponsorStudyIdentifier ");
             params.put("sponsorStudyIdentifier", sponsorStudyIdentifier);
        }

        String hql = MessageFormat.format(studyCritHQL, new Object[] {
                            studyNameJoin, sponsorJoin});

        String tempHQL = HQLHelper.removeTrailingToken(new StringBuffer(hql), "AND");
        String finalHQL = HQLHelper.removeTrailingToken(new StringBuffer(tempHQL), "WHERE");
        Query studyQuery = session.createQuery(finalHQL);
        HQLHelper.setParamsOnQuery(params, studyQuery );
        List<Study> studyObjs = studyQuery.list();
        session.close();
        return studyObjs;
    }

    public static Collection<SNPAssociationAnalysis> getSNPAssociationAnalysisObjects(SNPAssociationAnalysisCriteria crit) {
        if(crit == null) return new ArrayList<SNPAssociationAnalysis>();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        HashMap params = new HashMap();
        String analysisCritHQL = " FROM SNPAssociationAnalysis s WHERE {0} {1} ";
        String analysisName = crit.getName();
        String methods = crit.getMethods();

        StringBuffer analysisJoin = new StringBuffer("");
        if ((analysisName != null) && (analysisName.length() > 0)) {
           analysisJoin.append(" s.name = :analysisName  AND ");
           params.put("analysisName", analysisName);
        }

        StringBuffer methodsJoin = new StringBuffer("");
        if ((methods != null) && (methods.length() > 0))  {
             methodsJoin.append(" s.methods = :methods ");
             params.put("methods", methods);
        }

        String hql = MessageFormat.format(analysisCritHQL, new Object[] {
                            analysisJoin, methodsJoin});

        String tempHQL = HQLHelper.removeTrailingToken(new StringBuffer(hql), "AND");
        String finalHQL = HQLHelper.removeTrailingToken(new StringBuffer(tempHQL), "WHERE");
        Query analysisQuery = session.createQuery(finalHQL);
        HQLHelper.setParamsOnQuery(params, analysisQuery);
        List<SNPAssociationAnalysis> analysisObjs = analysisQuery.list();

        session.close();
        return analysisObjs;
    }

    public static List<String> getChromosomes() {
        if (CHROMOSOME_LIST == null) {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

          /* The below code is extremely slow.  Hence commented out.
            At the same time, we do not have a class that is mapped to
            CHR_START_END table.  So as a temp solution use direct SQL query
          */
          /*  Criteria crit = session.createCriteria(SNPAnnotation.class );
              crit.setProjection(Projections.property("chromosomeName"));
              List<String> values = crit.list();
           */
          
            SQLQuery q = session.createSQLQuery("SELECT chromosome FROM CHR_START_END");
            q.list();
            List<String> values = q.list();
            /* now sort them and place 'em in CHROMOSOME_LIST */
            CHROMOSOME_LIST = new ArrayList<String>();

            String[] sortedChrs = new String[22];
            SortedSet<String> sexChromosomes = new TreeSet<String>();
            for (int i = 0; i < values.size(); i++) {
                String chr = values.get(i);
                try {
                    int index = Integer.parseInt(chr) - 1;
                    sortedChrs[index] =  chr;
                } catch(NumberFormatException e) {
                    sexChromosomes.add(chr); // either X, Y, or MT
                }
             }

            for (int i = 0, j = 0; i < sortedChrs.length; i++) {
                String sortedChr = sortedChrs[i];
                CHROMOSOME_LIST.add(j++, sortedChr);
            }

            CHROMOSOME_LIST.addAll(sexChromosomes);
            session.close();
        }
        return CHROMOSOME_LIST;
    }
}
