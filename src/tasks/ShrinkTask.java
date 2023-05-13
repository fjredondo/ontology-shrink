package tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import main.Main;
import services.AnnotationsClustering;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotation;

/**
 * The Class ShrinkTask.
 */
public class ShrinkTask implements Callable<ShrinkTaskResult> {
	
	/** The Constant DETAIL_FILES_FOLDER. */
//	private static final String DETAIL_FILES_FOLDER = "detailed_files";
	
	/** The Constant LOGGER. */
	private final static Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	/** The ontology file. */
	private File ontologyFile;
	
	/** The output ontology file. */
	private File outputOWLFile;	
	
	/** The ontology. */
	private OWLOntology ontology;
	
	/** The percentage of human readable annotation reduction. */
	private int hraReduction;	

	/**
	 * Instantiates a new metric calculation task.
	 *
	 * @param ontologyFile the ontology file
	 */
	public ShrinkTask(File ontologyFile, int hraReduction) {
		super();
		this.ontologyFile = ontologyFile;
		this.hraReduction = hraReduction;
		
		String inputFullName = ontologyFile.getName();
		String inputJustName = inputFullName.substring(0, inputFullName.lastIndexOf("."));
		String inputFileExtension = inputFullName.split("\\.")[1];
		outputOWLFile = new File(ontologyFile.getParent() + File.separatorChar + inputJustName +"_output." + inputFileExtension);		
		
	}

	/**
	 * Load the ontology from a file name and set it into the metric object. 
	 * Invoke calculate for each metric.
	 * Set the result into a new MetricCalculationTaskResult object.
	 * 
	 * @return ShrinkTaskResult
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public ShrinkTaskResult call() throws Exception {
		OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
		LOGGER.log(Level.INFO, String.format("Loading %s", ontologyFile.getName()));
		ontology = ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);
		LOGGER.log(Level.INFO, String.format("%s loaded", ontologyFile.getName()));
		
		Set<OWLAnnotationAssertionAxiom> annotationsSet = new AnnotationsClustering(ontology).getRandomAnnotationsSubset(hraReduction);
		
		FileOutputStream  fout = new FileOutputStream(outputOWLFile);
		
		LOGGER.log(Level.INFO, String.format("%s\t-Removing annotations.. ", ontologyFile.getName()));

		List<OWLOntologyChange> owlChanges = ontologyManager.removeAxioms(ontology, annotationsSet);
		ontologyManager.applyChanges(owlChanges);
		ontology = ontologyManager.getOntology(ontology.getOntologyID());
		ontologyManager.saveOntology(ontology, fout);

		ShrinkTaskResult result = new ShrinkTaskResult(owlChanges, outputOWLFile.getName());

		LOGGER.log(Level.INFO, String.format("%s\t-Done", outputOWLFile.getName()));


		ontologyManager.removeOntology(ontology);
		return result;
	}
	
	/**
	 * Gets the ontology file.
	 *
	 * @return the ontology file
	 */
	public File getOntologyFile(){
		return this.ontologyFile;
	}

}
