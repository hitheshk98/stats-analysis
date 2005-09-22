package gov.nih.nci.caintegrator.analysis.messaging;

import java.util.*;

/**
 * PrincipalComponentAnalysisResults are both in tabular and image form
 * @author Michael A. Harris
 *
 */
public class PrincipalComponentAnalysisResult extends AnalysisResult implements java.io.Serializable {

	private List<PCAresultEntry> pcaResultEntries;
	
	private byte[] image1Bytes = null;
	private byte[] image2Bytes = null;
	private byte[] image3Bytes = null;
	
	public PrincipalComponentAnalysisResult(String sessionId, String taskId) {
		super(sessionId, taskId);
	}
	
	public String toString() {
	  return "PrincipalComponentAnalysisResult: sessionId=" + getSessionId() + " taskId=" + getTaskId();
	}
	
	public List<PCAresultEntry> getPCAresultEntries() {
		return pcaResultEntries;
	}

	public void setPCAresultEntries(List<PCAresultEntry> resultEntries) {
		this.pcaResultEntries = resultEntries;
	}
	
	public int getNumPCAresultEntries() { 
	  if (pcaResultEntries == null) {
	    return 0;
	  }
	  return pcaResultEntries.size();
	}

	public byte[] getImage1Bytes() {
		return image1Bytes;
	}

	public void setImage1Bytes(byte[] imageBytes) {
		this.image1Bytes = imageBytes;
	}

	public byte[] getImage2Bytes() {
		return image2Bytes;
	}

	public void setImage2Bytes(byte[] image2Bytes) {
		this.image2Bytes = image2Bytes;
	}

	public byte[] getImage3Bytes() {
		return image3Bytes;
	}

	public void setImage3Bytes(byte[] image3Bytes) {
		this.image3Bytes = image3Bytes;
	}

	
	
}
