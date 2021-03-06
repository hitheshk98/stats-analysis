/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.studyQueryService.dto.epi;

import java.util.Collection;

/**
  * Author: Ram Bhattaru
  * Date:   Apr 06, 2007
  * Time:   5:08:50 PM
**/

public class EnvironmentalTobaccoSmokeCriterion {

    public java.util.Collection<SmokingExposure> smokingExposureCollection;

    public EnvironmentalTobaccoSmokeCriterion(){

    }

    public Collection<SmokingExposure> getSmokingExposureCollection() {
        return smokingExposureCollection;
    }

    public void setSmokingExposureCollection(Collection<SmokingExposure> smokingExposureCollection) {
        this.smokingExposureCollection = smokingExposureCollection;
    }

}