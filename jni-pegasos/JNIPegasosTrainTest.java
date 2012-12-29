
import java.io.File;
import java.net.URL;

import jnipegasos.JNIPegasosInterface;
import jnipegasos.LearningParameter;

import com.martiansoftware.jsap.*;
import com.martiansoftware.jsap.stringparsers.*;

public class JNIPegasosTrainTest {
		
	public static void main(String[] args) throws Exception {
		// Commandline parsing, get the model file name and train datafile name
		// in commandline
		JSAP jsap = new JSAP();
		// longFlag datafile, longFlag modelfile
		FlaggedOption opt1 = new FlaggedOption("data")
									.setLongFlag("dataFile")
									.setRequired(true)
									.setStringParser(JSAP.STRING_PARSER);
		jsap.registerParameter(opt1);
		FlaggedOption opt2 = new FlaggedOption("model")
									.setLongFlag("modelFile")
									.setDefault("noModelFile")
									.setRequired(true)
									.setStringParser(JSAP.STRING_PARSER);
		jsap.registerParameter(opt2);
		
		JSAPResult config = jsap.parse(args);
		String dataFile = config.getString("data");
		String modelFile = config.getString("model");
		// Instantiate a pegasos interface
		JNIPegasosInterface trainer = new JNIPegasosInterface();
		// Set learning parameter here like lambda, max_iter, exam_per_iter here
		LearningParameter lp = new LearningParameter();
		// call the native training method 
		trainer.trainModel(dataFile, modelFile, lp);		
	}
}
