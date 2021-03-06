/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.studyQueryService.test.germline;

import gov.nih.nci.caintegrator.domain.study.bean.StudyParticipant;
import gov.nih.nci.caintegrator.domain.study.bean.Population;
import gov.nih.nci.caintegrator.studyQueryService.germline.FindingsManager;
import gov.nih.nci.caintegrator.studyQueryService.dto.study.PopulationCriteria;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Author: Ram Bhattaru
 * Date:   Aug 16, 2006
 * Time:   8:05:49 AM
 */
public class PopulationSearchTest extends GenotypeFindingTest {
    PopulationCriteria popCrit = null;
    public void testPopulationSearch() {

        Collection names = new ArrayList<String>();
        names.add("CASE"); 
        names.add("CEPH");
        //popCrit = new PopulationCriteria(names);
        //popCrit = new PopulationCriteria("CGEMS Prostate Cancer WGAS Phase 1");
        popCrit = new PopulationCriteria(new Long(2));

     /*  Collection<String> names = new ArrayList();

       popCrit = new PopulationCriteria(names);
        //names.add("CEPH");    */
        //popCrit.addNames(names);
        executeSearch();
    }

    private void executeSearch() {
        try {
            Collection<Population> population = manager.getPopulations(popCrit);
            System.out.println("Number of Populations Retrieved: " + population.size());
            for (Iterator<Population> iterator = population.iterator(); iterator.hasNext();) {
                Population popObj =  iterator.next();
                System.out.println("Population Name: " +
                                    popObj.getName());
              /*  System.out.println("Member Count: " + popObj.getMemberCount());
                System.out.println("Description: " + popObj.getDescription());
                System.out.println("Source: " + popObj.getSource());*/
            }
        } catch (Throwable t)  {
           System.out.println("CGEMS Exception in getting Population Objects: " + t.toString());
           t.printStackTrace();
       }
    }

    public static Test suite() {
        TestSuite suit =  new TestSuite();
        suit.addTest(new TestSuite(PopulationSearchTest.class));
        return suit;
    }

}
