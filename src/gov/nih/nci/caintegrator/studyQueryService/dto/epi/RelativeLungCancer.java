/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.studyQueryService.dto.epi;

public enum RelativeLungCancer implements IntegerValueEnum {

    NO(0, "No"),
    YES(1, "Yes"),
    UNKNOWN(9, "Unknown");


    final private String name;
    final private int value;

    RelativeLungCancer(int value, String name ) {
       this.name = name;
       this.value = value;
   }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
