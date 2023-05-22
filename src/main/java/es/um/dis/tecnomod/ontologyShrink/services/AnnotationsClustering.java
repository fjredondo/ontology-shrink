package es.um.dis.tecnomod.ontologyShrink.services;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;


import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import es.um.dis.tecnomod.ontologyShrink.main.Main;
import es.um.dis.tecnomod.ontologyShrink.main.Prefixes;

public class AnnotationsClustering {
	
	//private static final boolean INCLUDE_IMPORTED_ANNOTATIONS = true;
	
	/** The Constant SKOS_PREFERRED_LABEL. */
	/* Annotation properties referring names */
	private static final IRI SKOS_PREFERRED_LABEL = IRI.create(Prefixes.SKOS + "prefLabel");
	
	/** The Constant RDFS_LABEL. */
	private static final IRI RDFS_LABEL = IRI.create(Prefixes.RDFS + "label");
	
	/** The Constant SCHEMA_NAME. */
	private static final IRI SCHEMA_NAME = IRI.create(Prefixes.SCHEMA + "name");
	
	/** The Constant NCIT_PREFERRED_NAME. */
	private static final IRI NCIT_PREFERRED_NAME = IRI.create(Prefixes.NCIT + "P108");
	
	/** The Constant IAO_OBO_FOUNDRY_UNIQUE_NAME. */
	private static final IRI IAO_OBO_FOUNDRY_UNIQUE_NAME = IRI.create(Prefixes.IAO + "0000589");
	
	/** The Constant FOAF_NAME. */
	private static final IRI FOAF_NAME = IRI.create(Prefixes.FOAF + "name");

	/** The Constant IAO_OBO_FOUNDRY__PREFERRED_NAME. */
	private static final IRI IAO_OBO_FOUNDRY_PREFERRED_TERM = IRI.create(Prefixes.IAO + "0000111");


	/* Annotation properties referring synonyms */
	/** The Constant SKOS_ALT_LABEL. */
	private static final IRI SKOS_ALT_LABEL = IRI.create(Prefixes.SKOS + "altLabel");
	
	/** The Constant OBO_HAS_EXACT_SYNONYM. */
	private static final IRI OBO_HAS_EXACT_SYNONYM = IRI.create(Prefixes.OBO_IN_OWL + "hasExactSynonym");
	
	/** The Constant OBO_HAS_RELATED_SYNONYM. */
	private static final IRI OBO_HAS_RELATED_SYNONYM = IRI.create(Prefixes.OBO_IN_OWL + "hasRelatedSynonym");
	
	/** The Constant OBO_HAS_BROAD_SYNONYM. */
	private static final IRI OBO_HAS_BROAD_SYNONYM = IRI.create(Prefixes.OBO_IN_OWL + "hasBroadSynonym");
	
	/** The Constant OBO_HAS_NARROW_SYNONYM. */
	private static final IRI OBO_HAS_NARROW_SYNONYM = IRI.create(Prefixes.OBO_IN_OWL + "hasNarrowSynonym");
	
	/** The Constant NCIT_FULLY_QUALIFIED_SYNONYM. */
	private static final IRI NCIT_FULLY_QUALIFIED_SYNONYM = IRI.create(Prefixes.NCIT + "P90");
	
	/** The Constant IAO_ALTERNATIVE_NAME. */
	private static final IRI IAO_ALTERNATIVE_NAME = IRI.create(Prefixes.IAO + "0000118");
	
	/** The Constant OBI_FGED_ALT_NAME */
	private static final IRI OBI_FGED_ALT_NAME = IRI.create(Prefixes.OBI + "9991119");
	
	/** The Constant OBI_IEDB_ALT_NAME */
	private static final IRI OBI_IEDB_ALT_NAME = IRI.create(Prefixes.OBI + "9991118");
	
	/** The Constant OBI_ISA_ALT_NAME */
	private static final IRI OBI_ISA_ALT_NAME = IRI.create(Prefixes.OBI + "0001847");
	
	/** The Constant OBI_NIAID_ALT_NAME */
	private static final IRI OBI_NIAID_ALT_NAME = IRI.create(Prefixes.OBI + "0001886");

	/* Annotation properties referring definitions, comments or explanations */
	/** The Constant IAO_OFFICIAL_DEFINITION. */
	private static final IRI IAO_OFFICIAL_DEFINITION = IRI.create(Prefixes.IAO + "0000115");
	
	/** The Constant SKOS_DEFINITION. */
	private static final IRI SKOS_DEFINITION = IRI.create(Prefixes.SKOS + "definition");
	
	/** The Constant RDFS_COMMENT. */
	private static final IRI RDFS_COMMENT = IRI.create(Prefixes.RDFS + "comment");
	
	/** The Constant DC_DESCRIPTION. */
	private static final IRI DC_DESCRIPTION = IRI.create(Prefixes.DC + "description");
	
	/** The Constant NCIT_DEFINITION. */
	private static final IRI NCIT_DEFINITION = IRI.create(Prefixes.NCIT + "P97");

	/** The Constant NAME_PROPERTIES. */
	protected static final List<IRI> NAME_PROPERTIES = Arrays.asList(SKOS_PREFERRED_LABEL, RDFS_LABEL, SCHEMA_NAME,
			NCIT_PREFERRED_NAME, IAO_OBO_FOUNDRY_UNIQUE_NAME, FOAF_NAME, IAO_OBO_FOUNDRY_PREFERRED_TERM);

	/** The Constant SYNONYM_PROPERTIES. */
	protected static final List<IRI> SYNONYM_PROPERTIES = Arrays.asList(SKOS_ALT_LABEL, OBO_HAS_EXACT_SYNONYM,
			OBO_HAS_RELATED_SYNONYM, OBO_HAS_BROAD_SYNONYM, OBO_HAS_NARROW_SYNONYM, NCIT_FULLY_QUALIFIED_SYNONYM,
			IAO_ALTERNATIVE_NAME, OBI_FGED_ALT_NAME, OBI_IEDB_ALT_NAME, OBI_ISA_ALT_NAME, OBI_NIAID_ALT_NAME);

	/** The Constant DESCRIPTION_PROPERTIES. */
	protected static final List<IRI> DESCRIPTION_PROPERTIES = Arrays.asList(IAO_OFFICIAL_DEFINITION, SKOS_DEFINITION,
			RDFS_COMMENT, DC_DESCRIPTION, NCIT_DEFINITION);
	
	
	/** The ontology. */
	private OWLOntology ontology;
	
	private Set<OWLAnnotationAssertionAxiom> namesSet = new HashSet<OWLAnnotationAssertionAxiom>();
	private Set<OWLAnnotationAssertionAxiom> synonymsSet = new HashSet<OWLAnnotationAssertionAxiom>();;
	private Set<OWLAnnotationAssertionAxiom> descriptionsSet = new HashSet<OWLAnnotationAssertionAxiom>();;
	
	
	private int numberOfNames = 0;
	private int numberOfSynonyms = 0;
	private int numberOfDescriptions = 0;
	private int unclassifiedAnnotations = 0;
	private int annotationsNumber = 0;
	
	private final static Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	
	public AnnotationsClustering(OWLOntology ontology) {
		
		this.ontology = ontology;
		Set<OWLAnnotationAssertionAxiom> v_annotationsSet = ontology.getAxioms(AxiomType.ANNOTATION_ASSERTION);
		annotationsNumber = v_annotationsSet.size();
		LOGGER.info("Number of " + AxiomType.ANNOTATION_ASSERTION.toString() + " (TOTAL): " + annotationsNumber);
		classify(v_annotationsSet);

	}
	
	
	/**
	 * Classify annotation in three set of names, synonyms and descriptions.
	 *
	 */
	private void classify(Set<OWLAnnotationAssertionAxiom> p_annotationsSet){
		
		for(OWLAnnotationAssertionAxiom annotationAssertionAxiom : p_annotationsSet){
			if(NAME_PROPERTIES.contains(annotationAssertionAxiom.getProperty().getIRI())){
				namesSet.add(annotationAssertionAxiom);
				numberOfNames++;
			} else if(SYNONYM_PROPERTIES.contains(annotationAssertionAxiom.getProperty().getIRI())){
				synonymsSet.add(annotationAssertionAxiom);
				numberOfSynonyms++;
			} else if(DESCRIPTION_PROPERTIES.contains(annotationAssertionAxiom.getProperty().getIRI())){
				descriptionsSet.add(annotationAssertionAxiom);
				numberOfDescriptions++;
			} else {
				unclassifiedAnnotations++;
			}			
		}
		
		LOGGER.info(String.format("Number of %s(NAMES): %d", AxiomType.ANNOTATION_ASSERTION.toString(), numberOfNames));
		LOGGER.info(String.format("Number of %s(SYNONYMS): %d", AxiomType.ANNOTATION_ASSERTION.toString(), numberOfSynonyms));
		LOGGER.info(String.format("Number of %s(DESCRIPTIONS): %d", AxiomType.ANNOTATION_ASSERTION.toString(), numberOfDescriptions));
		LOGGER.info(String.format("Number of %s(UNCLASSIFIED): %d", AxiomType.ANNOTATION_ASSERTION.toString(), unclassifiedAnnotations));

	}

	/**
	 * Obtain a Set<OWLAnnotationAssertionAxiom> to remove from the ontology
	 * 
	 * @param percentage
	 */
	public Set<OWLAnnotationAssertionAxiom> getRandomAnnotationsSubset(int percentage) {
		
		Set<OWLAnnotationAssertionAxiom> annotationSet;
		
		int namNum = (numberOfNames * percentage) / 100;
		int symNum = (numberOfSynonyms * percentage)/ 100;
		int descNum = (numberOfDescriptions * percentage)/ 100;
		
		LOGGER.info(String.format("Percentage of human readable annotation reduction: %s%% ",percentage));
		LOGGER.info(String.format("Number of RemoveAxioms-%s(NAMES): %d", AxiomType.ANNOTATION_ASSERTION.toString(), namNum));
		LOGGER.info(String.format("Number of RemoveAxioms-%s(SYNONYMS): %d", AxiomType.ANNOTATION_ASSERTION.toString(), symNum));
		LOGGER.info(String.format("Number of RemoveAxioms-%s(DESCRIPTIONS): %d", AxiomType.ANNOTATION_ASSERTION.toString(), descNum));		
		
		annotationSet = getRandomSetElement(namesSet, namNum);
		annotationSet.addAll(getRandomSetElement(synonymsSet, symNum)); 
		annotationSet.addAll(getRandomSetElement(descriptionsSet, descNum)); 
		
		return annotationSet;
		
	}
	
	/**
	 * Choose randomly a p number elements from the input set.
	 * 
	 * @param <E>
	 * @param p_set
	 * @param p
	 * @return Set<E>
	 */
	private <E> Set<E> getRandomSetElement(Set<E> p_set, int p) {
		int r = 0;
		Set<E> newSet = new HashSet<E>();
		while (newSet.size() < p) {
			r = new Random().nextInt(p_set.size());
			newSet.add(p_set.stream().skip(r).findFirst().orElse(null));
		}
	    return newSet;
	}
	
	
}
