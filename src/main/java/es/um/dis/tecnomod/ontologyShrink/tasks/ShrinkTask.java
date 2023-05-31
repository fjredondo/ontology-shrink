package es.um.dis.tecnomod.ontologyShrink.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.RemoveAxiom;

import es.um.dis.tecnomod.ontologyShrink.main.Main;
import es.um.dis.tecnomod.ontologyShrink.services.AnnotationsClustering;


/**
 * The Class ShrinkTask.
 */
public class ShrinkTask implements Callable<ShrinkTaskResult> {
	
	
	/** The Constant LOGGER. */
	private final static Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	/** The ontology file. */
	private File inputOWLFile;
	
	/** The output ontology file. */
	private File outputOWLFile;	
	
	/** The ontology. */
	private OWLOntology ontology;
	
	/** The percentage of human readable annotation reduction. */
	private int hraReduction;	

	/**
	 * Performs the task of reduction of human readable content of the ontology. 
	 * Generate a new ontology file with suffix "_output.owl".
	 *
	 * @param inputOWLFile the ontology file
	 */
	public ShrinkTask(File inputOWLFile, File outputOWLFile, int hraReduction) {
		super();
		this.inputOWLFile = inputOWLFile;
		this.hraReduction = hraReduction;
		this.outputOWLFile = outputOWLFile;		
	}

	/**
	 * Load the ontology from a file name and randomly select annotations, with human readable content. 
	 * RemoveAxioms from the ontology and, finally, generates a new shrunken ontology.
	 * Set the result into a new ShrinkTaskResult object.
	 * 
	 * @return ShrinkTaskResult
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public ShrinkTaskResult call() throws Exception {
		OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
		LOGGER.log(Level.INFO, String.format("Loading %s", inputOWLFile.getName()));
		ontology = ontologyManager.loadOntologyFromOntologyDocument(inputOWLFile);
		LOGGER.log(Level.INFO, String.format("%s loaded", inputOWLFile.getName()));
		
		Set<OWLAnnotationAssertionAxiom> annotationsSet = new AnnotationsClustering(ontology).getRandomAnnotationsSubset(hraReduction);
		
		FileOutputStream  fout = new FileOutputStream(outputOWLFile);
		
		LOGGER.log(Level.INFO, String.format("%s\t-Removing annotations.. ", inputOWLFile.getName()));

		List<OWLOntologyChange> owlChanges = this.getOWLOntologyChanges(ontology, annotationsSet);
		
		ontologyManager.applyChanges(owlChanges);
		ontology = ontologyManager.getOntology(ontology.getOntologyID());
		ontologyManager.saveOntology(ontology, fout);

		ShrinkTaskResult result = new ShrinkTaskResult(owlChanges, outputOWLFile.getName());

		LOGGER.log(Level.INFO, String.format("%s\t-Done", outputOWLFile.getName()));


		ontologyManager.removeOntology(ontology);
		return result;
	}
	
	private List<OWLOntologyChange> getOWLOntologyChanges(OWLOntology ontology,
			Set<OWLAnnotationAssertionAxiom> annotationsSet) {
		List<OWLOntologyChange> changes = new ArrayList<>();
		for (OWLAxiom axiom : annotationsSet) {
			changes.add(new RemoveAxiom(ontology, axiom));
		}
		return changes;
	}

	/**
	 * Gets the ontology file.
	 *
	 * @return the ontology file
	 */
	public File getOntologyFile(){
		return this.inputOWLFile;
	}

}
