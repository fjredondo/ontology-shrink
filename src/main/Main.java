package main;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.FileHandler;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


import tasks.ShrinkTask;
import tasks.ShrinkTaskResult;

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
	public static void main(String[] args) throws IOException, InterruptedException {
		CommandLine cmd = generateOptions(args);
		setLogLevel(cmd.getOptionValue('l', Level.INFO.getName()));
		
		File inputOWLFile = new File(cmd.getOptionValue('i'));
		
		if (!inputOWLFile.exists()) {
			LOGGER.log(Level.SEVERE, String.format("'%s' not found.", args[0]));
			return;
		}
		
		int hraReduction = 0;
		String hraReductionArg = cmd.getOptionValue('r');
		try {
			hraReduction = Integer.parseInt(hraReductionArg);
		}catch(NumberFormatException e) {
			LOGGER.log(Level.SEVERE, String.format("'%s' not percentaje.", hraReductionArg));
			return;
		}
		
		
		String inputFullName = inputOWLFile.getName();
		String inputJustName = inputFullName.substring(0, inputFullName.lastIndexOf("."));
		File outputLog = new File(inputOWLFile.getParent() + File.separatorChar + inputJustName +"_output.log");
	
		FileHandler fh = new FileHandler(outputLog.getPath()); 
        fh.setFormatter(new SimpleFormatter()); 
		LOGGER.addHandler(fh); 
		
		if (inputOWLFile.isFile()) {
			
			ShrinkTask task = new ShrinkTask(inputOWLFile, hraReduction);
			executeTask(outputLog, task);
		}

	}
	
	/**
	 * Execute without task executor.
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
        
        input = new Option("r", "HRA_reduction", true, "Percentage of human readable annotation reduction.");
        input.setRequired(true);
        options.addOption(input);
        
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
