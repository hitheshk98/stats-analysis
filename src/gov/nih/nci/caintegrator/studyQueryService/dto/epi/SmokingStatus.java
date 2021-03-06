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
 * Time: 5:25:28 PM

 */
public enum SmokingStatus implements IntegerValueEnum {
    NEVER(0, "Never Smoker"),
    FORMER(1, "Former Smoker"),
    CURRENT(2, "Current Smoker"),
    NO_INFO(9, "No Information");

    private final int value;
    private final String name;

    SmokingStatus(int key, String name) {
        this.value = key;
    	this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue()	{
    	return value;
    }

}
