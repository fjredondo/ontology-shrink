package tasks;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.OWLOntologyChange;

import main.Main;

/**
 * The Class ShrinkTaskResult.
 */
public class ShrinkTaskResult implements Serializable{

	private final static Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 436305010795217064L;
	
	/** The owl file. */
	private String owlFile;
	
	List<OWLOntologyChange> owlChanges;

	/**
	 * Allows to print OWLOntologyChanges.
	 * 
	 * @param owlChanges
	 * @param owlFile
	 */
	public ShrinkTaskResult(List<OWLOntologyChange> owlChanges, String owlFile) {
		super();
		this.owlChanges = owlChanges;
		this.owlFile = owlFile;
	}
	
	public String getResult() {
		
		
		return owlChanges.stream().map(Object::toString)
		        .collect(Collectors.joining("\n "));
	}
	
	public void printRemovedAxioms() {
		
		
		for (OWLOntologyChange obj : owlChanges) {
			
			LOGGER.info(obj.toString());
		}
		
	}
	

	
	/**
	 * Gets the owl file.
	 *
	 * @return the owl file
	 */
	public String getOwlFile() {
		return owlFile;
	}
	
	/**
	 * Sets the owl file.
	 *
	 * @param owlFile the new owl file
	 */
	public void setOwlFile(String owlFile) {
		this.owlFile = owlFile;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((owlFile == null) ? 0 : owlFile.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShrinkTaskResult other = (ShrinkTaskResult) obj;
		if (owlFile == null) {
			if (other.owlFile != null)
				return false;
		} else if (!owlFile.equals(other.owlFile))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShrinkTaskResult [result=0");
		builder.append(", owlFile=");
		builder.append(owlFile);
		builder.append("]");
		return builder.toString();
	}

	
}
