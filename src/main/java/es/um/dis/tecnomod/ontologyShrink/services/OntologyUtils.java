package es.um.dis.tecnomod.ontologyShrink.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplBoolean;

/**
 * The Class OntologyUtils.
 */
public class OntologyUtils {

	/**
	 * Checks if a class is obsolete or not.
	 *
	 * @param owlEntity the owl class
	 * @param ontology  the ontology
	 * @return true, if is obsolete
	 */
	public static boolean isObsolete(OWLEntity owlEntity, OWLOntology ontology) {
		Set<OWLAnnotationAssertionAxiom> axioms = ontology.annotationAssertionAxioms(owlEntity.getIRI())
				.collect(Collectors.toSet());
		for (OWLAnnotationAssertionAxiom axiom : axioms) {
			IRI propertyIRI = axiom.getProperty().asOWLAnnotationProperty().getIRI();
			if (propertyIRI.equals(OWLRDFVocabulary.OWL_DEPRECATED.getIRI())
					|| propertyIRI.equals(OWLRDFVocabulary.OWL_DEPRECATED_CLASS.getIRI())
					|| propertyIRI.equals(OWLRDFVocabulary.OWL_DEPRECATED_PROPERTY.getIRI())) {
				if (axiom.getValue() instanceof OWLLiteralImplBoolean) {
					OWLLiteralImplBoolean value = (OWLLiteralImplBoolean) axiom.getValue();
					return value.parseBoolean();
				}
			}
		}
		return false;
	}

	/**
	 * Gets all the annotations assertion axioms of one entity type from an
	 * ontology.
	 * 
	 * @param entity
	 * @param ontology
	 * @param includeImports
	 * @return a set of OWLAnnotationAssertionAxiom
	 */
	public static Set<OWLAnnotationAssertionAxiom> getOWLAnnotationAssertionAxiom(OWLEntity entity,
			OWLOntology ontology, boolean includeImports) {
		Set<OWLAnnotationAssertionAxiom> annotationAssertionAxioms = ontology.annotationAssertionAxioms(entity.getIRI())
				.collect(Collectors.toSet());
		if (includeImports) {
			for (OWLOntology importedOntology : ontology.imports().collect(Collectors.toList())) {
				Set<OWLAnnotationAssertionAxiom> importedAnnotationAssertionAxioms = importedOntology
						.annotationAssertionAxioms(entity.getIRI()).collect(Collectors.toSet());
				annotationAssertionAxioms.addAll(importedAnnotationAssertionAxioms);
			}
		}
		return annotationAssertionAxioms;
	}

	public static void removeUnclassifiedAnnotations(OWLOntology ontology) {
		List<OWLAxiom> axiomsToRemove = ontology.axioms().filter(x -> (x.isOfType(AxiomType.ANNOTATION_ASSERTION)))
				.filter(annotationAssertion -> (!AnnotationsClustering.NAME_PROPERTIES
						.contains(((OWLAnnotationAssertionAxiom) annotationAssertion).getProperty().getIRI())
						&& !AnnotationsClustering.DESCRIPTION_PROPERTIES
								.contains(((OWLAnnotationAssertionAxiom) annotationAssertion).getProperty().getIRI())
						&& !AnnotationsClustering.SYNONYM_PROPERTIES
								.contains(((OWLAnnotationAssertionAxiom) annotationAssertion).getProperty().getIRI()))).collect(Collectors.toList());
		ontology.removeAxioms(axiomsToRemove);
	}

	public static void removeObsoloteEntities(OWLOntology ontology) {
		OWLEntityRemover entityRemover = new OWLEntityRemover(ontology);
		visitObsoleteClasses(ontology, entityRemover);
		visitObsoleteObjectProperties(ontology, entityRemover);
		visitObsoleteDataProperties(ontology, entityRemover);
		visitObsoleteAnnotationProperties(ontology, entityRemover);
		ontology.applyChanges(entityRemover.getChanges());
	}

	public static void visitObsoleteClasses(OWLOntology ontology, OWLEntityRemover entityRemover) {
		ontology.classesInSignature().filter(x -> isObsolete(x, ontology)).forEach(x -> {
			entityRemover.visit(x);
		});
	}

	public static void visitObsoleteObjectProperties(OWLOntology ontology, OWLEntityRemover entityRemover) {
		ontology.objectPropertiesInSignature().filter(x -> isObsolete(x, ontology)).forEach(x -> {
			entityRemover.visit(x);
		});
	}

	public static void visitObsoleteDataProperties(OWLOntology ontology, OWLEntityRemover entityRemover) {
		ontology.dataPropertiesInSignature().filter(x -> isObsolete(x, ontology)).forEach(x -> {
			entityRemover.visit(x);
		});
	}

	public static void visitObsoleteAnnotationProperties(OWLOntology ontology, OWLEntityRemover entityRemover) {
		ontology.annotationPropertiesInSignature().filter(x -> isObsolete(x, ontology)).forEach(x -> {
			entityRemover.visit(x);
		});
	}
}
