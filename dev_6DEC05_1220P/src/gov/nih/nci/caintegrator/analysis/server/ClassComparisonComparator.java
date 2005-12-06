package gov.nih.nci.caintegrator.analysis.server;

import java.util.Comparator;
import gov.nih.nci.caintegrator.analysis.messaging.ClassComparisonResultEntry;

/**
 * This comparator will sort a list of ClassComparisonResultEntries 
 * first by ascending p-value and then by descenging absolute fold change.
 * 
 * @author harrismic
 *
 */
public class ClassComparisonComparator implements Comparator {

	public ClassComparisonComparator() {
		
	}

	public int compare(Object o1, Object o2) {
		if (!(o1 instanceof ClassComparisonResultEntry)) {
		  return 0;
		}
		
		if (!(o2 instanceof ClassComparisonResultEntry)) {
			  return 0;
		}
		
		ClassComparisonResultEntry e1 = (ClassComparisonResultEntry) o1;
		ClassComparisonResultEntry e2 = (ClassComparisonResultEntry) o2;
		
		if (e1.getPvalue() < e2.getPvalue()) {
		  return -1;
		}
		
		if (e2.getPvalue() < e1.getPvalue()) {
		  return 1;
		}
		
		if (e1.getFoldChange() > e2.getFoldChange()) {
		  return -1;
		}
		
		if (e2.getFoldChange() > e1.getFoldChange()) {
		  return 1;
		}
		
		return 0;
	}

}
