/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.studyQueryService.dto.epi;

/**
 * Created by IntelliJ IDEA.
 * User: Ram Bhattaru
 * Date: Apr 13, 2007
 * Time: 5:26:32 PM
 */
public enum EducationLevel  implements IntegerValueEnum {
    NONE(1, "None"), 
    ELEMENTARY(2, "Elementary School"),
    LOWER_MIDDLE(3, "Lower Middle"),
    TEACHER_TRAINING(4, "Teacher Training High School"),
    TECHNICAL(5, "Technical, Industrial, Commercial H.School"),
    COLLEGE_PREP(6, "College Prep. High Schools (Classical, Science, Art)"),
    POST_HS(7, "Post H.S. Academies or Junior Colleges"),
    DEGREE(8, "Degree"),
    POSTGRAD(9, "Postgraduate"),
    OTHER(10, "Other");

    private final int value;
    private final String name;

    EducationLevel(int key, String name) {
        this.value = key;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public int getValue()   {
        return value;
    }

}
