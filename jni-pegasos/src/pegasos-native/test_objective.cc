// Distributed under GNU General Public License (see license.txt for details).
//
//  Copyright (c) 2007 Shai Shalev-Shwartz.
//  All Rights Reserved.
//
// This file gets a name of dataset file, lambda, and w and calculate
// objective

#include "cmd_line.h"
#include "simple_sparse_vec_hash.h"
#include "pegasos_optimize.h"

int main(int argc, char** argv) {


  // -------------------------------------------------------------
  // ---------------------- Parse Command Line -------------------
  // -------------------------------------------------------------

  std::string data_filename;
  std::string model_filename;
  double lambda = 1.0;

  // parse command line
  learning::cmd_line cmdline;
  cmdline.info("Test me");
  cmdline.add_master_option("<data-file>", &data_filename);
  cmdline.add_master_option("<model-file>", &model_filename);
  cmdline.add("-lambda", "regularization parameter", &lambda, 1.0);


  int rc = cmdline.parse(argc, argv);
  if (rc < 2) {
    cmdline.print_help();
    return EXIT_FAILURE;
  }


  // -------------------------------------------------------------
  // ---------------------- read the data ------------------------
  // -------------------------------------------------------------
  uint dimension = 0;
  std::vector<simple_sparse_vector> Dataset;
  std::vector<int> Labels;
  long readingTime;
  ReadData(data_filename,Dataset,Labels,dimension,readingTime);


  // read weight vector

  std::ifstream model_file(model_filename.c_str());
  if (!model_file.good()) {
    std::cerr << "error w/ " << model_filename << std::endl;
    exit(EXIT_FAILURE);
  }
  WeightVector W(dimension,model_file); 
  model_file.close();

  // and calculate objective
  double obj_value = W.snorm() * lambda / 2.0;
  double loss_value = 0.0;
  for (uint i=0; i < Dataset.size(); ++i) {
    double cur_loss = 1 - Labels[i]*(W * Dataset[i]); 
    if (cur_loss < 0.0) cur_loss = 0.0;
    loss_value += cur_loss;
  }
  obj_value += loss_value/Dataset.size();

  std::cout << "Obj value = " << obj_value << std::endl;
  
}
  
