package es.um.dis.tecnomod.ontologyShrink.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

class OntologyUtilsTest {

	@Test
	void testGO() throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {
		OWLOntology ontology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(new File("/home/fabad/legibility_embeddings_clean/ontologies/go/go.owl"));
		OntologyUtils.removeObsoloteEntities(ontology);
		OntologyUtils.removeUnclassifiedAnnotations(ontology);
		ontology.saveOntology(new FileOutputStream(new File("/home/fabad/legibility_embeddings_clean/ontologies/go/go_clean.owl")));
	}

	@Test
	void testDTO() throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {
		OWLOntology ontology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(new File("/home/fabad/legibility_embeddings_clean/ontologies/dto/dto.owl"));
		OntologyUtils.removeObsoloteEntities(ontology);
		OntologyUtils.removeUnclassifiedAnnotations(ontology);
		ontology.saveOntology(new FileOutputStream(new File("/home/fabad/legibility_embeddings_clean/ontologies/dto/dto_clean.owl")));
	}

	@Test
	void testSO() throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {
		OWLOntology ontology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(new File("/home/fabad/legibility_embeddings_clean/ontologies/so/so.owl"));
		OntologyUtils.removeObsoloteEntities(ontology);
		OntologyUtils.removeUnclassifiedAnnotations(ontology);
		ontology.saveOntology(new FileOutputStream(new File("/home/fabad/legibility_embeddings_clean/ontologies/so/so_clean.owl")));
	}
	
	@Test
	void testHelis() throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {
		OWLOntology ontology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(new File("/home/fabad/legibility_embeddings_clean/ontologies/helis/helis.owl"));
		OntologyUtils.removeObsoloteEntities(ontology);
		OntologyUtils.removeUnclassifiedAnnotations(ontology);
		ontology.saveOntology(new FileOutputStream(new File("/home/fabad/legibility_embeddings_clean/ontologies/helis/helis_clean.owl")));
	}
	
	@Test
	void testFoodon() throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {
		OWLOntology ontology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(new File("/home/fabad/legibility_embeddings_clean/ontologies/foodon/foodon.owl"));
		OntologyUtils.removeObsoloteEntities(ontology);
		OntologyUtils.removeUnclassifiedAnnotations(ontology);
		ontology.saveOntology(new FileOutputStream(new File("/home/fabad/legibility_embeddings_clean/ontologies/foodon/foodon_clean.owl")));
	}

}
