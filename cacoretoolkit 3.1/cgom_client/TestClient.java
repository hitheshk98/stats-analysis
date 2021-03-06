/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-analysis/LICENSE.txt for details.
 */

import gov.nih.nci.caintegrator.domain.analysis.snp.SNPAnalysisGroup;
import gov.nih.nci.caintegrator.domain.analysis.snp.SNPAssociationAnalysis;
import gov.nih.nci.caintegrator.domain.analysis.snp.SNPAssociationFinding;
import gov.nih.nci.caintegrator.domain.annotation.gene.GeneBiomarker;
import gov.nih.nci.caintegrator.domain.annotation.snp.SNPAnnotation;
import gov.nih.nci.caintegrator.domain.annotation.snp.SNPAssay;
import gov.nih.nci.caintegrator.domain.annotation.snp.SNPPanel;
import gov.nih.nci.caintegrator.domain.finding.variation.snpFrequency.SNPFrequencyFinding;
import gov.nih.nci.caintegrator.domain.study.Population;
import gov.nih.nci.caintegrator.domain.study.Specimen;
import gov.nih.nci.caintegrator.domain.study.Study;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;



/**
 * @author CGEMS Team
 * @version 1.0
 */

/**
 * TestClient.java demonstartes various ways to execute searches with and
 * without using Application Service Layer (convenience layer that abstracts
 * building criteria Uncomment different scenarios below to demonstrate the
 * various types of searches
 */

/**
 * @author sahnih, zhangd, guruswamis
 */
public class TestClient {

	public static void main(String[] args) {
		System.out.println("*** TestClient...");
		try {
			searchStudy();
			searchSNPPanel();
			searchSNPAssociationAnalysis();
			searchSNPAnalysisGroup();
			searchPopulation();			
			searchSNPAnnoation();
			searchSNPAssociationFinding();
			searchSNPFrequencyFinding();
			searchGeneBiomarker();
			searchSNPAssay();
			searchSNPAssayHQL();
		} catch (RuntimeException e) {
			e.printStackTrace();
			System.out.println("Test client throws Exception = " + e);
		}
	}

	/**
	 * Scenario 1: Retrieve all SNPPanels No search criteria specified,
	 * resulting in an unrestricted search
	 */
	private static void searchSNPPanel() {
		SNPPanel snpPanel = new SNPPanel();
		try {
			System.out
					.println("______________________________________________________________________");
			System.out.println("Retrieving all SNPPanels...");
			ApplicationService appService = ApplicationServiceProvider
					.getApplicationService();

			List resultList = appService.search(SNPPanel.class, snpPanel);
			if (resultList != null) {
				System.out.println("Number of results returned: "
						+ resultList.size());
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator
						.hasNext();) {
					SNPPanel returnedObj = (SNPPanel) resultsIterator.next();
					System.out.println("Panel Name: " + returnedObj.getName()
							+ "\n" + "Description: "
							+ returnedObj.getDescription() + "\n"
							+ "Technology: " + returnedObj.getTechnology()
							+ "\n" + "Vendor: " + returnedObj.getVendor()
							+ "\n" + "Vendor PanelId: "
							+ returnedObj.getVendorPanelId() + "\n"
							+ "Version: " + returnedObj.getVersion() + "\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void searchSNPAssociationAnalysis() {
		SNPAssociationAnalysis snpAnalysis = new SNPAssociationAnalysis();
		try {
			System.out
					.println("______________________________________________________________________");
			System.out.println("Retrieving all SNPAssociationAnalysis...");
			ApplicationService appService = ApplicationServiceProvider
					.getApplicationService();

			List resultList = appService.search(SNPAssociationAnalysis.class,
					snpAnalysis);
			if (resultList != null) {
				System.out.println("Number of results returned: "
						+ resultList.size());
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator
						.hasNext();) {
					SNPAssociationAnalysis returnedObj = (SNPAssociationAnalysis) resultsIterator
							.next();
					System.out.println("Name: " + returnedObj.getName() + "\n"
							+ "Description: " + returnedObj.getDescription()
							+ "\n" + "Methods: " + returnedObj.getMethods()
							+ "\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void searchStudy() {
		Study study = new Study();
		try {
			System.out
					.println("______________________________________________________________________");
			System.out.println("Retrieving all Study objects...");
			ApplicationService appService = ApplicationServiceProvider
					.getApplicationService();

			List resultList = appService.search(Study.class, study);
			if (resultList != null) {
				System.out.println("Number of results returned: "
						+ resultList.size());
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator
						.hasNext();) {
					Study returnedObj = (Study) resultsIterator.next();
					System.out.println("Name: " + returnedObj.getName() + "\n"
							+ "Description: " + returnedObj.getDescription()
							+ "\n" + "SponsorStudyIdentifier: "
							+ returnedObj.getSponsorStudyIdentifier());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void searchSNPAnalysisGroup() {
		SNPAnalysisGroup snpAnalysisGroup = new SNPAnalysisGroup();
		try {
			System.out
					.println("______________________________________________________________________");
			System.out.println("Retrieving all SNPAnalysisGroup...");
			ApplicationService appService = ApplicationServiceProvider
					.getApplicationService();

			List resultList = appService.search(SNPAnalysisGroup.class,
					snpAnalysisGroup);
			if (resultList != null) {
				System.out.println("Number of results returned: "
						+ resultList.size());
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator
						.hasNext();) {
					SNPAnalysisGroup returnedObj = (SNPAnalysisGroup) resultsIterator
							.next();
					System.out.println("Name: " + returnedObj.getName() + "\n"
							+ "Description: " + returnedObj.getDescription()
							+ "\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	/**
	 * Scenario Three: Simple Search (Criteria Object Collection) to retrieve
	 * SNPFrequencyFinding for the Gene ?WT1? In this example, a search is
	 * performed for WT1 genes to retrieve the SNPFrequencyFinding. The code
	 * iterates through the returned objects and prints out the several
	 * properties of each of the object, as shown in the code listing.
	 */
	@SuppressWarnings( { "unused", "unchecked" })
	private static void searchSNPFrequencyFinding() {
		Collection geneBiomarkerCollection = new ArrayList();
		GeneBiomarker wt1 = new GeneBiomarker();
		wt1.setHugoGeneSymbol("WT1");
		geneBiomarkerCollection.add(wt1);

		SNPAnnotation snpAnnotation = new SNPAnnotation();
		snpAnnotation.setGeneBiomarkerCollection(geneBiomarkerCollection);

		SNPFrequencyFinding snpFrequencyFinding = new SNPFrequencyFinding();
		snpFrequencyFinding.setSnpAnnotation(snpAnnotation);
		try {
			System.out
					.println("______________________________________________________________________");
			System.out
					.println("Retrieving all SNPFrequencyFinding objects for WT1");
			ApplicationService appService = ApplicationServiceProvider
					.getApplicationService();

			List resultList = appService.search(SNPFrequencyFinding.class,
					snpAnnotation);
			if (resultList != null) {
				System.out.println("Number of results returned: "
						+ resultList.size());
				System.out.println("DbsnpId" + "\t" + "ChromosomeName" + "\t"
						+ "ChromosomeLocation" + "\t" + "MinorAlleleFrequency"
						+ "\t" + "HardyWeinbergPValue" + "\t"
						+ "ReferenceAllele" + "\t" + "OtherAllele" + "\t"
						+ "Population" + "\n");
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator
						.hasNext();) {
					SNPFrequencyFinding returnedObj = (SNPFrequencyFinding) resultsIterator
							.next();
					System.out.println(returnedObj.getSnpAnnotation()
							.getDbsnpId()
							+ "\t"
							+ returnedObj.getSnpAnnotation()
									.getChromosomeName()
							+ "\t"
							+ returnedObj.getSnpAnnotation()
									.getChromosomeLocation()
							+ "\t"
							+ returnedObj.getMinorAlleleFrequency()
							+ "\t"
							+ returnedObj.getHardyWeinbergPValue()
							+ "\t"
							+ returnedObj.getReferenceAllele()
							+ "\t"
							+ returnedObj.getOtherAllele()
							+ "\t"
							+ returnedObj.getPopulation().getName() + "\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Senario Four: Nested Search to retrieve SNPAssays based on dbSnpId A
	 * nested search is one where a traversal of more than one class-class
	 * association is required to obtain a set of result objects given the
	 * criteria object. This example demon-strates one such search in which the
	 * criteria object passed to the search method is of type SNPAnnotation, and
	 * the desired objects are of type SNPAssay.
	 */
	@SuppressWarnings( { "unused", "unchecked" })
	private static void searchSNPAssay() {
		SNPAnnotation snpAnnotation = new SNPAnnotation();
		snpAnnotation.setDbsnpId("rs5030335");
		SNPAssay snpAssay = new SNPAssay();
		snpAssay.setSnpAnnotation(snpAnnotation);
		try {
			System.out
					.println("______________________________________________________________________");
			System.out.println("Retrieving all SNPAssay objects for rs5030335");
			ApplicationService appService = ApplicationServiceProvider
					.getApplicationService();

			List resultList = appService.search(SNPAssay.class, snpAnnotation);
			if (resultList != null) {
				System.out.println("Number of results returned: "
						+ resultList.size());
				System.out.println("Vender Assay ID" + "\t" + "DbsnpId" + "\t"
						+ "ChromosomeName" + "\t" + "ChromosomeLocation" + "\t"
						+ "SNP Panel" + "\t" + "Version" + "\t"
						+ "DesignAlleles" + "\t" + "Status" + "\n");
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator
						.hasNext();) {
					SNPAssay returnedObj = (SNPAssay) resultsIterator.next();
					System.out.println(returnedObj.getVendorAssayId()
							+ "\t"
							+ returnedObj.getSnpAnnotation().getDbsnpId()
							+ "\t"
							+ returnedObj.getSnpAnnotation()
									.getChromosomeName()
							+ "\t"
							+ returnedObj.getSnpAnnotation()
									.getChromosomeLocation() + "\t"
							+ returnedObj.getSnpPanel().getName() + "\t"
							+ returnedObj.getVersion() + "\t"
							+ returnedObj.getDesignAlleles() + "\t"
							+ returnedObj.getStatus() + "\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void searchPopulation() {
		Population population = new Population();
		try {
			System.out
					.println("______________________________________________________________________");
			System.out.println("Retrieving all Population types");
			ApplicationService appService = ApplicationServiceProvider
					.getApplicationService();

			List resultList = appService.search(Population.class, population);
			if (resultList != null) {
				System.out.println("Number of results returned: "
						+ resultList.size());
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator
						.hasNext();) {
					Population returnedObj = (Population) resultsIterator
							.next();
					System.out.println("Name: " + returnedObj.getName() + "\n"
							+ "Description: " + returnedObj.getDescription()
							+ "\n" + "Source: " + returnedObj.getSource()
							+ "\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static void searchSNPAssociationFinding() {
		Collection geneBiomarkerCollection = new ArrayList();
		GeneBiomarker wt1 = new GeneBiomarker();
		wt1.setHugoGeneSymbol("WT1");
		geneBiomarkerCollection.add(wt1);

		SNPAnnotation snpAnnotation = new SNPAnnotation();
		snpAnnotation.setGeneBiomarkerCollection(geneBiomarkerCollection);
		try {
			System.out
					.println("______________________________________________________________________");
			System.out.println("Retrieving all SNPAssiciationFindings for WT1");
			ApplicationService appService = ApplicationServiceProvider
					.getApplicationService();

			List resultList = appService.search(SNPAssociationFinding.class,
					snpAnnotation);
			if (resultList != null) {
				System.out.println("Number of results returned: "
						+ resultList.size());
				System.out.println("DbsnpId" + "\t" + "ChromosomeName" + "\t"
						+ "ChromosomeLocation" + "\t" + "GenomeBuild" + "\t"
						+ "ReferenceSequence" + "\t" + "ReferenceStrand" + "\t"
						+ "GeneBiomarker(s)" + "\t" + "Analysis Name" + "\t"
						+ "p-Value" + "\t" + "rank" + "\n");
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator
						.hasNext();) {
					SNPAssociationFinding returnedObj = (SNPAssociationFinding) resultsIterator
							.next();
					System.out.println(returnedObj.getSnpAnnotation()
							.getDbsnpId()
							+ "\t"
							+ returnedObj.getSnpAnnotation()
									.getChromosomeName()
							+ "\t"
							+ returnedObj.getSnpAnnotation()
									.getChromosomeLocation()
							+ "\t"
							+ pipeGeneBiomarkers(returnedObj.getSnpAnnotation()
									.getGeneBiomarkerCollection())
							+ "\t"
							+ returnedObj.getSnpAssociationAnalysis().getName()
							+ "\t"
							+ returnedObj.getPvalue()
							+ "\t"
							+ returnedObj.getRank() + "\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This example demonstrates the use of Hibernate detached criteria objects
	 * to formulate and perform more sophisticated searches. for more
	 * information, please consult the Hibernate documentation at
	 * http://www.hibernate.org/hib_docs/v3/api/org/hibernate/criterion/DetachedCriteria.html
	 */
	@SuppressWarnings("unused")
	private static void searchSNPAnnoation() {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SNPAnnotation.class);
		criteria.add(Restrictions
				.ge("chromosomeLocation", new Integer(4000000)));
		criteria.add(Restrictions
				.le("chromosomeLocation", new Integer(4200000)));
		criteria.add(Restrictions.eq("chromosomeName", "1"));
		try {
			System.out
					.println("______________________________________________________________________");
			System.out
					.println("Retrieving all SNPAnnotations for Chr 1,4000000 - 4200000");
			ApplicationService appService = ApplicationServiceProvider
					.getApplicationService();

			List resultList = appService.query(criteria, SNPAnnotation.class
					.getName());
			if (resultList != null) {
				System.out.println("Number of results returned: "
						+ resultList.size());
				System.out.println("DbsnpId" + "\t" + "ChromosomeName" + "\t"
						+ "ChromosomeLocation" + "\t" + "GenomeBuild" + "\t"
						+ "ReferenceSequence" + "\t" + "ReferenceStrand" + "\t"
						+ "GeneBiomarker(s)" + "\n");
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator
						.hasNext();) {
					SNPAnnotation returnedObj = (SNPAnnotation) resultsIterator
							.next();
					System.out.println(returnedObj.getDbsnpId()
							+ "\t"
							+ returnedObj.getChromosomeName()
							+ "\t"
							+ returnedObj.getChromosomeLocation()
							+ "\t"
							+ returnedObj.getGenomeBuild()
							+ "\t"
							+ returnedObj.getReferenceSequence()
							+ "\t"
							+ returnedObj.getReferenceStrand()
							+ "\t"
							+ pipeGeneBiomarkers(returnedObj
									.getGeneBiomarkerCollection()) + "\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This example demostrates using HQL to retrieve all SNPAssay, with id <
	 * 100. It uses a Hibernate Query Language (HQL) search string to form the
	 * query. For more information on HQL syntax, consult the Hibernate
	 * documentation at
	 * http://www.hibernate.org/hib_docs/v3/reference/en/html/queryhql. html.
	 */
	private static void searchSNPAssayHQL() {
		String hqlString = "FROM SNPAssay a WHERE a.id < 100";
		HQLCriteria hqlC = new HQLCriteria(hqlString);
		try {
			System.out
					.println("______________________________________________________________________");
			System.out.println("Retrieving all SNPAssay objects, id < 100");
			ApplicationService appService = ApplicationServiceProvider
					.getApplicationService();
			List resultList = appService.query(hqlC, SNPAnnotation.class
					.getName());
			if (resultList != null) {
				if (resultList != null) {
					System.out.println("Number of results returned: "
							+ resultList.size());
					System.out.println("Id\t" + "Vender Assay ID" + "\t"
							+ "SNP Panel" + "\t" + "Version" + "\t"
							+ "DesignAlleles" + "\t" + "Status" + "\n");
					for (Iterator resultsIterator = resultList.iterator(); resultsIterator
							.hasNext();) {
						SNPAssay returnedObj = (SNPAssay) resultsIterator
								.next();
						System.out.println(returnedObj.getId() + "\t"
								+ returnedObj.getVendorAssayId() + "\t"
								+ returnedObj.getSnpPanel().getName() + "\t"
								+ returnedObj.getVersion() + "\t"
								+ returnedObj.getDesignAlleles() + "\t"
								+ returnedObj.getStatus() + "\n");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings( { "unused", "unchecked" })
	private static void searchGeneBiomarker() {
		/*
		 * This example demonstrates the use of Hibernate detached criteria
		 * objects to formulate and perform more sophisticated searches. A
		 * detailed description of detached criteria is beyond the scope of this
		 * example; for more information, please consult the Hibernate
		 * documentation at
		 * http://www.hibernate.org/hib_docs/v3/api/org/hibernate/criterion/DetachedCriteria.html
		 */

		DetachedCriteria criteria = DetachedCriteria
				.forClass(GeneBiomarker.class);
		criteria.add(Restrictions
				.gt("startPhyscialLocation", new Long(6000000)));
		criteria.add(Restrictions.lt("endPhysicalLocation", new Long(6300000)));
		criteria.add(Restrictions.eq("chromosome", "19"));

		try {
			System.out
					.println("______________________________________________________________________");
			System.out
					.println("Retrieving all GeneBiomarker objects for Chr 19,6000000 - 6300000");
			ApplicationService appService = ApplicationServiceProvider
					.getApplicationService();

			List resultList = appService.query(criteria, GeneBiomarker.class
					.getName());
			if (resultList != null) {
				System.out.println("Number of results returned: "
						+ resultList.size());
				System.out.println("ChromosomeName" + "\t"
						+ "StartPhyscialLocation" + "\t"
						+ "EndPhysicalLocation" + "\t" + "HugoGeneSymbol"
						+ "\n");
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator
						.hasNext();) {
					GeneBiomarker returnedObj = (GeneBiomarker) resultsIterator
							.next();
					System.out.println(returnedObj.getChromosome() + "\t"
							+ returnedObj.getStartPhyscialLocation() + "\t"
							+ returnedObj.getEndPhysicalLocation() + "\t"
							+ returnedObj.getHugoGeneSymbol() + "\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String pipeGeneBiomarkers(Collection geneBiomarkerCollection) {
		String geneList = "";
		if (geneBiomarkerCollection != null) {
			for (Object object : geneBiomarkerCollection) {
				GeneBiomarker geneBiomarker = (GeneBiomarker) object;
				geneList = geneList + geneBiomarker.getHugoGeneSymbol() + "|";
			}
			// remove Last |
			if (geneList.endsWith("|")) {
				geneList = geneList.substring(0, geneList.lastIndexOf("|"));
			}
		}
		return geneList;
	}
}
