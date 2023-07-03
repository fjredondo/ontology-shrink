package es.um.dis.tecnomod.ontologyShrink.main;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.FileHandler;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import es.um.dis.tecnomod.ontologyShrink.tasks.ShrinkTask;
import es.um.dis.tecnomod.ontologyShrink.tasks.ShrinkTaskResult;

/**
 * The Class Main.
 */
public class Main {
	
    // Create static logger object with Null
    private static Logger LOGGER = null;
 
    // Static block
    static
    {
        System.setProperty(
            "java.util.logging.SimpleFormatter.format",
            "[%1$tF %1$tT] [%4$.4s] %5$s %n");
        // Logger to get logs
        LOGGER = Logger.getLogger(Main.class.getName());
    }
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public static void main(String[] args) throws IOException {
		CommandLine cmd = generateOptions(args);
		setLogLevel(cmd.getOptionValue('l', Level.INFO.getName()));
		
		File inputOWLFile = new File(cmd.getOptionValue('i'));
		Optional prefix = Optional.ofNullable(cmd.getOptionValue('o'));
		
		if (!inputOWLFile.exists()) {
			LOGGER.log(Level.SEVERE, String.format("'%s' not found.", args[0]));
			return;
		}
		
		List<Integer> hraReductionList = Collections.emptyList();
		String hraReductionArgList = cmd.getOptionValue('r');
		
		try {
			hraReductionList = Stream.of(hraReductionArgList.split(","))
			  .map(String::trim)
			  .map(Integer::parseInt).sorted().distinct()
			  .collect(Collectors.toList());
			if (hraReductionList.get(0) < 0 || hraReductionList.get(hraReductionList.size() - 1) > 100) {
				LOGGER.log(Level.INFO,"Percentaje not in range [0,100]");
				throw new Exception("Percentaje not in range [0,100]");
			}

			LOGGER.log(Level.INFO,Arrays.toString(hraReductionList.toArray()));
		}catch(Exception e) {
			LOGGER.log(Level.SEVERE, String.format("'%s' not a percentaje(int) list separated by ','.", hraReductionArgList));
			return;
		}
		
		String inputFullName = inputOWLFile.getName();
		String inputJustName = inputFullName.substring(0, inputFullName.lastIndexOf("."));
		Optional<String> inputExtension = Optional.ofNullable(inputFullName)
			      .filter(f -> f.contains("."))
			      .map(f -> f.substring(inputFullName.lastIndexOf(".") + 1));
		try {
			
			File outputLog = new File(inputOWLFile.getParent() + File.separatorChar + inputJustName +"_shrink" +".log");

			FileHandler fh = new FileHandler(outputLog.getPath()); 
	        fh.setFormatter(new SimpleFormatter()); 
			LOGGER.addHandler(fh); 
			
			Integer previousReduction = 0;
			File previousInputFile = inputOWLFile;
			
			for (Integer hraReduction : hraReductionList) {
				
				Integer increaseReduction = hraReduction - previousReduction;
				
				LOGGER.log(Level.INFO, String.format("'%s' percent annotation additional reduction begins...", increaseReduction.toString()));
				
				if (inputOWLFile.isFile()) {
					String ouputFullName;
					if (prefix.isPresent()) {
						ouputFullName = prefix.get() +"_"+ String.format("%d", hraReduction) +"." + inputExtension.get();
					} else {
						ouputFullName = inputOWLFile.getParent() + File.separatorChar + inputJustName +"_"+ String.format("%d", hraReduction) +"." + inputExtension.get();
					}
					
					File outputOWLFile = new File(ouputFullName);
					
					LOGGER.log(Level.INFO, String.format("'%s' -> '%s' percent reduction -> '%s'", previousInputFile.getName(), increaseReduction.toString(), outputOWLFile.getName()));
					
					ShrinkTask task = new ShrinkTask(previousInputFile, outputOWLFile, increaseReduction);
					executeTask(outputLog, task);
					
					previousInputFile = outputOWLFile;
					
				}
				
				LOGGER.log(Level.INFO, String.format("'%s' percent annotation cumulative reduction has ended.", hraReduction.toString()));
				
				previousReduction = hraReduction;
				
				
	        }


		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString());
		}

	}
	
	/**
	 * Execute task .
	 *
	 * @param outputFile the output file
	 * @param tasks the tasks
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void executeTask(File outputFile, ShrinkTask task) throws IOException{
		
		
		try {
			ShrinkTaskResult result = task.call();
			result.printRemovedAxioms();
				
		} catch (Exception e) {
			String msg = String.format("Error processing %s:\n%s", task.getOntologyFile().getAbsolutePath(), e.getMessage());
			LOGGER.log(Level.SEVERE, msg, e);
		}
		
	}


	/**
	 * Generate options.
	 *
	 * @param args the args
	 * @return the command line
	 */
	private static CommandLine generateOptions(String[] args){
		Options options = new Options();
		
		Option input = new Option("i", "input", true, "input owl file path, or folder containing owl files.");
        input.setRequired(true);
        options.addOption(input);
        
        Option reduction = new Option("r", "HRA_reduction", true, "Percentage (int) list of human readable annotation reduction separated by ','");
        reduction.setRequired(true);
        options.addOption(reduction);
        
        Option prefix = new Option("o", "output_prefix", true, "Prefix used for saving the output owl files. For example, if prefix is GO and reductions are '20,40,60', output files will be GO_20.owl, GO_40.owl and GO_60.owl");
        prefix.setRequired(false);
        options.addOption(prefix);
        
        Option logLevel = new Option("l", "log-level", true, "log level (SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL )");
        logLevel.setRequired(false);
        options.addOption(logLevel);
        
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;;
        try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("ontology-shrink", options);
			System.exit(1);
		}
        
        return cmd;
	}
	
	/**
	 * Sets the log level.
	 *
	 * @param level the new log level
	 */
	private static void setLogLevel(String level){
		Logger root = Logger.getLogger("");
		root.setLevel(Level.parse(level));
	}


}
