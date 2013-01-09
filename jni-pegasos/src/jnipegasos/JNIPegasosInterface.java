/*
 * JNI_Pegasos - A Java Native Interface for Pegasos SVM
 * 
 * Copyright (C) 2012
 * Deepak Nayak 
 * Columbia University, Computer Science MS'13
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package jnipegasos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.text.ParseException;
import java.util.TreeMap;

/**
 * The main interface class that send training data file, learning parameters, and 
 * model file to Pegasos SVM by native calls.
 * @author Deepak Nayak
 */

public class JNIPegasosInterface {
	static {
		System.loadLibrary("pegasos");
	}
	
	protected LearningParameter lp;
	
	/**
	 * Main interface to pegasos native training method. 
	 * Sends training data filename and other necessary arguments to pegasos svm library
	 * by native call.
	 * @param trainfilename training data file in svmlight format
	 * @param modelfilename modelfile name, to which weights will be written
	 * @param lp learning parameters
	 */
	private native void trainmodel(String trainfilename, String modelfilename, LearningParameter lp);
	public void trainModel(String trainfilename, String modelfilename, LearningParameter lp) {
		trainmodel(trainfilename, modelfilename, lp);
	}
	/**
	 * Performs classification on test data by using pegasos svm library.
	 * Testfile should be labeled as in pegasos svm, and is in same format as in training file.
	 * @param testfilename test data filename in svmlight format
	 * @param modelfilename model file(weight vector) to be used. it is written in training phase
	 * @param predictfilename filename to which predicted output will be written
	 */
	public native void classify(String testfilename, String modelfilename, String predictfilename);	
	/**
	 * Reads the modelfile written by pegasos into an object of class PrimalSVMWeights
	 * The modelfile written by Pegasos are in a very simple format. It contains one
	 * line in the form int:double pairs of dimension and value. There is no comment line 
	 * or anything else.
	 * @param modelfilename filename in which weights are written
	 */
	public PrimalSVMWeights getWeightsfromFile(String modelfilename) throws ParseException{
		PrimalSVMWeights weights = new PrimalSVMWeights();
		File modelfile = new File(modelfilename);
		BufferedReader bi = null;
		try {
			bi = new BufferedReader(new FileReader(modelfile));
			String line = bi.readLine();
			if(line != null) {
				// the weight written by pegasos is simple file of one line
				// each dimension separated by space only
				String[] tokens = line.trim().split("[ ]");
				String dimval, dim, val;
				int idx = 0;
				int size = tokens.length;
				if (size < 1) {
					throw new ParseException("Parse error in file '"
							+ modelfilename + ". "
							+ " No weight vector found? may be empty?", 0);
				}
				TreeMap<Integer, Double> map = new TreeMap<Integer, Double>();
				for (int tokencnt = 0; tokencnt < size; tokencnt++ ) {
					dimval = tokens[tokencnt];
					idx = dimval.indexOf(':');
					if(idx >= 0) {
						dim = dimval.substring(0, idx);
						val = dimval.substring(idx + 1, dimval.length());
						map.put(Integer.parseInt(dim), Double.parseDouble(val));
					}
					else {
						throw new ParseException("Parse error in file '"
								+ modelfilename +  ", token: "
								+ tokencnt + ". Could not estimate a \"int:double\" pair ?! "
								+ modelfilename
								+ " contains a wrongly defined weight vector!", 0);
					}
				}
				weights.setFeatures(map);	
			}
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		finally {
			if (bi != null) {
				try {
					bi.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		return weights;
	}
}
