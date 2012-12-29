
import java.io.File;
import java.net.URL;

import jnipegasos.JNIPegasosInterface;
import jnipegasos.LearningParameter;

import com.martiansoftware.jsap.*;
import com.martiansoftware.jsap.stringparsers.*;

public class JNIPegasosClassifyTest {
	
	public static void main(String[] args) throws Exception {
		// Commandline parsing, get the model file name and train datafile name
		// in commandline
		JSAP jsap = new JSAP();
		// longFlag datafile, longFlag modelfile
		FlaggedOption opt1 = new FlaggedOption("test")
									.setLongFlag("testFile")
									.setRequired(true)
									.setStringParser(JSAP.STRING_PARSER);
		jsap.registerParameter(opt1);
		FlaggedOption opt2 = new FlaggedOption("model")
									.setLongFlag("modelFile")
									.setRequired(true)
									.setStringParser(JSAP.STRING_PARSER);
		jsap.registerParameter(opt2);
		FlaggedOption opt3 = new FlaggedOption("predict")
									.setLongFlag("predictFile")
									.setRequired(true)
									.setStringParser(JSAP.STRING_PARSER);
		jsap.registerParameter(opt3);

		JSAPResult config = jsap.parse(args);
		
		String testFile = config.getString("test");
		String modelFile = config.getString("model");
		String predictFile = config.getString("predict");
		
		//
		JNIPegasosInterface trainer = new JNIPegasosInterface();
		
		trainer.classify(testFile, modelFile, predictFile);
	
	}
	
}
