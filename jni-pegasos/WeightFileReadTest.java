
import java.io.File;
import java.net.URL;

import jnipegasos.JNIPegasosInterface;
import jnipegasos.LearningParameter;
import jnipegasos.PrimalSVMWeights;

import com.martiansoftware.jsap.*;
import com.martiansoftware.jsap.stringparsers.*;

public class WeightFileReadTest {
		
	public static void main(String[] args) throws Exception {
		// Commandline parsing, get the model file to read from
		JSAP jsap = new JSAP();
		// longFlag modelfile
		FlaggedOption opt1 = new FlaggedOption("model")
									.setLongFlag("modelFile")
									.setRequired(true)
									.setStringParser(JSAP.STRING_PARSER);
		jsap.registerParameter(opt1);
		
		JSAPResult config = jsap.parse(args);
		String modelFile = config.getString("model");
		// Instantiate a pegasos interface
		JNIPegasosInterface trainer = new JNIPegasosInterface();
		// get weights from model file
		PrimalSVMWeights weights = trainer.getWeightsfromFile(modelFile);	
		// print all weights
		System.out.println(weights.getWeights());
	}
}
