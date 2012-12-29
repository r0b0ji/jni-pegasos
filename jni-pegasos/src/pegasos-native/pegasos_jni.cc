/** \file pegasos_jni.cc
 * Implementation of JNI methods
 * This file implements the native methods which is auto generated by jni
 * in jnipegasos_JNIPegasosInterface.h from JNIPegasosInterface.java. Internally
 * it calls the methods in Pegasos_optimize and other pegasos svm files.
 * 
 * @author Deepak Nayak
 * copyright GNU Public License 
 */
#include "pegasos_jni.h"
#include <string>
#include <vector>
#include <cstdlib>
#include "pegasos_optimize.h"
#include "jnipegasos_JNIPegasosInterface.h"

JNIEXPORT void JNICALL Java_jnipegasos_JNIPegasosInterface_trainmodel
(JNIEnv *env, jobject obj, jstring datafilename, jstring modelfilename, jobject lp) {
  // get filenames from arguments by converting jstring to native string
  const char* cdatafilename = env->GetStringUTFChars(datafilename, 0);
  const char* cmodelfilename = env->GetStringUTFChars(modelfilename, 0);
  std::string testFile = "noTestFile";
  std::string modelFile(cmodelfilename);
  std::string dataFile(cdatafilename);
  env->ReleaseStringUTFChars(datafilename, cdatafilename);
  env->ReleaseStringUTFChars(modelfilename, cmodelfilename);

  // read datafile and initialize vectors
  uint dimension = 0;
  std::vector<simple_sparse_vector> Dataset;
  std::vector<int> Labels;
  long readingTime;
  ReadData(dataFile,Dataset,Labels,dimension,readingTime);

  uint testDimension = 0;
  std::vector<simple_sparse_vector> testDataset;
  std::vector<int> testLabels;
  long testReadingTime;
  if (testFile != "noTestFile") {
    ReadData(testFile,testDataset,testLabels,testDimension,testReadingTime);
  } else {
    testReadingTime = 0;
  }
    
  // these variables will be filled by training method
  long trainTime;
  long calc_obj_time;
  double obj_value;
  double norm_value;
  double loss_value;
  double zero_one_error;
  double test_loss;
  double test_error;
  // get lambda, max_iter, exam_per_iter from learning parameter
  jclass lpCls = env->GetObjectClass(lp);
  jfieldID lambdaID = env->GetFieldID(lpCls, "lambda", "D");
  jfieldID max_iterID = env->GetFieldID(lpCls, "max_iter", "I");
  jfieldID exam_per_iterID = env->GetFieldID(lpCls, "exam_per_iter", "I");
  double lambda = env->GetDoubleField(lp, lambdaID);
  int max_iter = env->GetIntField(lp, max_iterID);
  int exam_per_iter = env->GetIntField(lp, exam_per_iterID);
  /**
   * @function LearnReturnLast  Native learning method called by JNI
   * //Input parameters
   * @param Dataset std::vector<simple_sparse_vector>& training dataset
   * @param Labels std::vector<int>& Labels of training dataset
   * @param dimension unit dimension of training input
   * @param testDataset std::vector<simple_sparse_vector>& test dataset
   * @param testLabels std::vector<int>& Lables of test dataset
   * @param lambda double regularization parameter
   * @param max_iter int maximum iteration
   * @param exam_per_iter int number of examples per iteration to consider \
   *        for stochastic gradient
   * @param  model_filename std::string& filename to which model will be written
   * //Output parameters
   * @param train_time long& time spend in training
   * @param calc_obj_time long& time spend in objective function calculation
   * @param obj_value double& objective function value
   * @param norm_value double& norm of weight after training
   * @param loss_value double& loss value after training
   * @param zero_one_error double& zero-one misclassification error
   * //Addtional parameters
   * @param eta_rule_type int rule to calculate eta_constant
   * @param eta_constant double learning rate
   * @param projection_rule rule to calculate projection, like cosine, etc
   * @param projection_constant double projection constant
   */
  LearnReturnLast(Dataset, Labels, dimension, testDataset, testLabels,
		  lambda, max_iter, exam_per_iter, modelFile, trainTime,
		  calc_obj_time, obj_value,norm_value, loss_value, zero_one_error, 
		  test_loss, test_error, 0, 0.0, 0, 0.0);
  // -------------------------------------------------------------
  // ---------------------- Print Results ------------------------
  // -------------------------------------------------------------
  std::cout << norm_value  << " = Norm of solution\n" 
	    << loss_value << " = avg Loss of solution\n"  
	    << zero_one_error  << " = avg zero-one error of solution\n" 
	    << obj_value << " = primal objective of solution\n" 
	    <<  std::endl;
}

JNIEXPORT void JNICALL Java_jnipegasos_JNIPegasosInterface_classify
(JNIEnv * env, jobject obj, jstring testfilename, jstring modelfilename, jstring predictfilename) {
  // get filenames from arguments by converting jstring to native string
  const char* ctestfilename = env->GetStringUTFChars(testfilename, 0);
  const char* cmodelfilename = env->GetStringUTFChars(modelfilename, 0);
  const char* cpredictfilename = env->GetStringUTFChars(predictfilename, 0);
  std::string testFile(ctestfilename);
  std::string s_modelFile(cmodelfilename);
  std::string s_predictFile(cpredictfilename);
  env->ReleaseStringUTFChars(testfilename, ctestfilename);
  env->ReleaseStringUTFChars(modelfilename, cmodelfilename);
  env->ReleaseStringUTFChars(predictfilename, cpredictfilename);

  // read the testfile and initialize dataset and label vector
  uint dimension = 0; // dimension of test and train data should be same???
  std::vector<simple_sparse_vector> TestDataset;
  std::vector<int> TestLabels;
  long readingTime;
  ReadData(testFile, TestDataset, TestLabels, dimension, readingTime);

  uint num_examples = TestLabels.size();
  
  // Initialization of classification vector
  // read modelfile and initialize weightvector
  std::ifstream modelFile(s_modelFile.c_str(), std::ifstream::in);
  if (!modelFile.good()) {
    std::cerr << "error w/ " << modelFile << std::endl;
    exit(EXIT_FAILURE);
  }
  std::vector<int> PredictedLabels(num_examples);
  WeightVector W(dimension, modelFile);
  double loss_value = 0.0;
  int misclassified_count = 0;
  // Now, in a loop predict, fill the predict vector and also write in predictfile,
  // also do misclassified count, loss, zero-one-loss, etc..
  for (uint i=0; i < num_examples; ++i) {
    double predict = W * TestDataset[i];
    double cur_loss = 1 - TestLabels[i] * predict; 
    if (cur_loss < 0.0) cur_loss = 0.0;
    loss_value += cur_loss/num_examples;
    if (cur_loss >= 1.0) {
      misclassified_count++;
      PredictedLabels[i] = -1 * TestLabels[i];
    }
    else
      PredictedLabels[i] = TestLabels[i];
  }
  
  // write predicted labels to predict file
  std::ofstream predictFile(s_predictFile.c_str());
  if (!predictFile.good()) {
    std::cerr << "error w/ " << predictFile << std::endl;
    exit(EXIT_FAILURE);
  }
  for (uint i=0; i < num_examples; ++i) {
    predictFile << PredictedLabels[i] << std::endl;
  }
  std::cout << s_predictFile << " = predicted labels file" << std::endl;
  predictFile.close();
  
  // -------------------------------------------------------------
  // ---------------------- Print Results ------------------------
  // -------------------------------------------------------------
  std::cout << loss_value << " = avg Loss of solution\n"  
	    << "(" << misclassified_count <<"/" << num_examples << ")" << " = number of misclassified examples\n" 
	    <<  std::endl;
}
