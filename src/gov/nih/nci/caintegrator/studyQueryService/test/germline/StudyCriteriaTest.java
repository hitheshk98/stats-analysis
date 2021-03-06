/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.studyQueryService.test.germline;

import gov.nih.nci.caintegrator.studyQueryService.dto.study.StudyCriteria;
import gov.nih.nci.caintegrator.studyQueryService.germline.FindingsManager;
import gov.nih.nci.caintegrator.domain.study.bean.Study;
import gov.nih.nci.caintegrator.test.BaseSpringTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import java.util.Collection;
import java.util.Iterator;

/**
 * Author: Ram Bhattaru
 * Date:   Aug 16, 2006
 * Time:   8:05:49 AM
 */
public class StudyCriteriaTest extends CGEMSTest {
   /* public String[] getConfigFiles() {
            return new String[] {"classpath*:applicationContext-services.xml","test/applicationContext-junit.xml"};
    }
*/
    private StudyCriteria studyCrit ;

    public void setUp() throws Exception {
        studyCrit = new StudyCriteria();
    }

    public void testStudyCriteria() {
        //sStudyetUpStudyCriteria();
        executeSearch();
    }

    protected void setUpStudyCriteria() {
        studyCrit.setName("CGEMS Prostate Cancer WGAS Phase 1A");
        //studyCrit.setSponsorStudyIdentifier("NCI DCEG, NCI OCG");
    }

    private void executeSearch() {
        try {
            Collection<Study> studyObjs = manager.getStudies(studyCrit);
            System.out.println("Number of Study Objects Retrieved: " + studyObjs.size());
            for (Iterator<Study> iterator = studyObjs.iterator(); iterator.hasNext();) {
                Study study = iterator.next();
                System.out.println("Study Name: " + study.getName());
                System.out.println("Study Description: " + study.getDescription());
                System.out.println("Study Start Date: " + study.getStartDate());
                System.out.println("Study End Date: " + study.getEndDate());
                System.out.println("Sponsor Study Identifier: " + study.getSponsorStudyIdentifier());
            }

        } catch (Throwable t)  {
           System.out.println("CGEMS Exception in getting studies: " + t.toString());
           t.printStackTrace();
       }
    }

    public static Test suite() {
        TestSuite suit =  new TestSuite();
        suit.addTest(new TestSuite(StudyCriteriaTest.class));
        return suit;
    }

}
