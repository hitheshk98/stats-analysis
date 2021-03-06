/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.studyQueryService.dto.study;

/**
 * User: Ram Bhattaru
 * Date: Jul 3, 2006
 * Time: 5:21:49 PM
*/
/**
 * A type of research activity that tests how well new medical treatments or other
 * interventions work in subjects. Such plans test new methods of screening,
 * prevention, diagnosis or treatment of a disease. The specific plans are fully
 * defined in the protocol and may be carried out in a clinic or other medical
 * facility.
 */

public class StudyCriteria {
	private Long id;
	private String version;

	private String name;
	/**
	 * The unique identifier for the study assigned by the study sponsoring
	 * organization.
	 */
	private String sponsorStudyIdentifier;

	public StudyCriteria(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSponsorStudyIdentifier() {
        return sponsorStudyIdentifier;
    }

    public void setSponsorStudyIdentifier(String sponsorStudyIdentifier) {
        this.sponsorStudyIdentifier = sponsorStudyIdentifier;
    }
    
    @Override
	public String toString()
    {
		String str = "Study Criteria\n";
		
		if (name != null)
			str = str + "Study Name: " + name + "\n";
		
		return str;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}

}