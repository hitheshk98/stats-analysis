package gov.nih.nci.caintegrator.studyQueryService.dto.annotation;

import gov.nih.nci.caintegrator.studyQueryService.dto.germline.OperatorType;
import gov.nih.nci.caintegrator.studyQueryService.dto.germline.PanelCriteria;

import java.util.Collection;

/**
 * User: Ram Bhattaru
 * Date: Jul 3, 2006
 * Time: 5:21:49 PM
*/

public class AnnotationCriteria {

    private gov.nih.nci.caintegrator.studyQueryService.dto.annotation.CytobandCriteria cytobandCriteria;
    private String[] geneOntology;
    private String[] genePathways;
    private Collection<String> geneSymbols;
    private gov.nih.nci.caintegrator.studyQueryService.dto.germline.OperatorType operatorType;
    private gov.nih.nci.caintegrator.studyQueryService.dto.germline.PanelCriteria panelCriteria;
    private gov.nih.nci.caintegrator.studyQueryService.dto.annotation.PhysicalPositionCriteria physicalPositionCriteria;
    private Collection<String> snpIdentifiers;

    public AnnotationCriteria(){ }

    public gov.nih.nci.caintegrator.studyQueryService.dto.annotation.CytobandCriteria getCytobandCriteria() {
        return cytobandCriteria;
    }

    public void setCytobandCriteria(CytobandCriteria cytobandCriteria) {
        this.cytobandCriteria = cytobandCriteria;
    }

/*  NOT IMPLEMENTED (confirmed with customer)   
    private String[] geneLocations;

    public String[] getGeneLocations() {
        return geneLocations;
    }

    public void setGeneLocations(String[] geneLocations) {
        this.geneLocations = geneLocations;
    }
 */
    public String[] getGeneOntology() {
        return geneOntology;
    }

    public void setGeneOntology(String[] geneOntology) {
        this.geneOntology = geneOntology;
    }

    public String[] getGenePathways() {
        return genePathways;
    }

    public void setGenePathways(String[] genePathways) {
        this.genePathways = genePathways;
    }

    public Collection<String> getGeneSymbols() {
        return geneSymbols;
    }

    public void setGeneSymbols(Collection<String> geneSymbols) {
        this.geneSymbols = geneSymbols;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(OperatorType operatorType) {
        this.operatorType = operatorType;
    }

    public PanelCriteria getPanelCriteria() {
        return panelCriteria;
    }

    public void setPanelCriteria(PanelCriteria panelCriteria) {
        this.panelCriteria = panelCriteria;
    }

    public gov.nih.nci.caintegrator.studyQueryService.dto.annotation.PhysicalPositionCriteria getPhysicalPositionCriteria() {
        return physicalPositionCriteria;
    }

    public void setPhysicalPositionCriteria(PhysicalPositionCriteria physicalPositionCriteria) {
        this.physicalPositionCriteria = physicalPositionCriteria;
    }

    public Collection<String> getSnpIdentifiers() {
        return snpIdentifiers;
    }

    public void setSnpIdentifiers(Collection<String> snpIdentifiers) {
        this.snpIdentifiers = snpIdentifiers;
    }

	@Override
	public String toString()
	{
		String str = "Annotation\n";
		
		if ((geneSymbols != null) && (geneSymbols.size() > 0))
		{
			str = str + "HUGO Gene Symbols\n";
			for (String gene : geneSymbols)
			{
				str = str + gene + "\n";
			}
		}
		
		return str;
	}


}