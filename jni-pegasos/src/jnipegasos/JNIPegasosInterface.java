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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;

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
	
}
