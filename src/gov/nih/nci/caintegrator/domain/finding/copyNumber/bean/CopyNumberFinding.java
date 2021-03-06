/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.domain.finding.copyNumber.bean;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */
 
  /**
   * value obtained for each reporter (SNP reporter or BAC reporter) in a hybridization that estimates 
   * the DNA copy number for a genomic locus represented by the reporter 
   * 
   */

public  class CopyNumberFinding 
    extends gov.nih.nci.caintegrator.domain.finding.variation.bean.VariationFinding


	implements java.io.Serializable 
{

	private static final long serialVersionUID = 1234567890L;


 	
	   
    /**
   * Indication of abnomality for copy number data, values include amplified, deleted, noChange. 
   * 
   */

    private java.lang.String abnomalityStatus;
    /**
   * Indication of abnomality for copy number data, values include amplified, deleted, noChange. 
   * 
   */

	public  java.lang.String getAbnomalityStatus(){
        return abnomalityStatus;
    }
    public void setAbnomalityStatus( java.lang.String abnomalityStatus){
        this.abnomalityStatus = abnomalityStatus;
    }
	
	   
    /**
   * array design identifier, given by the manufactures of the array: eg: U133plus2.0 by Affy manufactures, 
   * values include CDNAARRAY, HGU133P2, HM100KSNPArray. 
   * 
   */

    private java.lang.String arrayType;
    /**
   * array design identifier, given by the manufactures of the array: eg: U133plus2.0 by Affy manufactures, 
   * values include CDNAARRAY, HGU133P2, HM100KSNPArray. 
   * 
   */

	public  java.lang.String getArrayType(){
        return arrayType;
    }
    public void setArrayType( java.lang.String arrayType){
        this.arrayType = arrayType;
    }
	
	   
    /**
   * Four possible states or genotype models are assigned to each probe (A, B, AB, or NoCall) 
   * 
   */

    private java.lang.String call;
    /**
   * Four possible states or genotype models are assigned to each probe (A, B, AB, or NoCall) 
   * 
   */

	public  java.lang.String getCall(){
        return call;
    }
    public void setCall( java.lang.String call){
        this.call = call;
    }
	
	   
    /**
   * value obtained for each reporter (SNP reporter or BAC reporter) in a hybridization that estimates 
   * the DNA copy number for a genomic locus represented by the reporter 
   * 
   */

    private java.lang.Double copyNumber;
    /**
   * value obtained for each reporter (SNP reporter or BAC reporter) in a hybridization that estimates 
   * the DNA copy number for a genomic locus represented by the reporter 
   * 
   */

	public  java.lang.Double getCopyNumber(){
        return copyNumber;
    }
    public void setCopyNumber( java.lang.Double copyNumber){
        this.copyNumber = copyNumber;
    }
	
	   
    /**
   * confidence value for the copy number estimation
   */

    private java.lang.Double copyNumberPvalue;
    /**
   * confidence value for the copy number estimation
   */

	public  java.lang.Double getCopyNumberPvalue(){
        return copyNumberPvalue;
    }
    public void setCopyNumberPvalue( java.lang.Double copyNumberPvalue){
        this.copyNumberPvalue = copyNumberPvalue;
    }
	


		public boolean equals(Object obj){
			boolean eq = false;
			if(obj instanceof CopyNumberFinding) {
				CopyNumberFinding c =(CopyNumberFinding)obj; 			 
				Long thisId = getId();		
				
					if(thisId != null && thisId.equals(c.getId())) {
					   eq = true;
				    }		
				
			}
			return eq;
		}
		
		public int hashCode(){
			int h = 0;
			
			if(getId() != null) {
				h += getId().hashCode();
			}
			
			return h;
        }

        
}