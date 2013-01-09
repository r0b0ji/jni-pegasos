package jnipegasos;
import java.util.Map;
import java.util.TreeMap;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;

public class WeightFileReadTest {
	public static void main(String[] args) throws Exception {
		// Commandline parsing, get the model filename to read from
		JSAP jsap = new JSAP();
		// longFlag datafile, longFlag modelfile
		FlaggedOption opt1 = new FlaggedOption("model")
									.setLongFlag("modelFile")
									.setRequired(true)
									.setStringParser(JSAP.STRING_PARSER);
		jsap.registerParameter(opt1);

		JSAPResult config = jsap.parse(args);
		String modelFile = config.getString("model");
		// Instantiate a pegasos interface 
		JNIPegasosInterface trainer = new JNIPegasosInterface();
		// Get weight vector from model file
		PrimalSVMWeights weight = trainer.getWeightsfromFile(modelFile);
		TreeMap<Integer, Double> map = weight.getWeights();
		System.out.println(map);
	}
}