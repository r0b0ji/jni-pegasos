// Distributed under GNU General Public License (see license.txt for details).
//
//  Copyright (c) 2007 Shai Shalev-Shwartz.
//  All Rights Reserved.
//=============================================================================
// File Name: simple_sparse_vec_hash.cc
// implements the functions of simple_sparse_vec_hash.h
//=============================================================================

//*****************************************************************************
// Included Files
//*****************************************************************************
#include "simple_sparse_vec_hash.h"

/*---------------------------------------------------------------------------*/
simple_sparse_vector::simple_sparse_vector(std::istream& is) : my_vec() {
  
  // read the number of elements
  int n = 0;
  is >> n;

  // read the elements
  for (int i=0; i<n ; ++i) {

    // read the pair (key,val)
    uint key;
    is >> key;
    double val;
    is >> val;

    // insert to the map
    my_vec.push_back(IndexValuePair(key,val));

  }

}
/*---------------------------------------------------------------------------*/


/*---------------------------------------------------------------------------*/
simple_sparse_vector::simple_sparse_vector(std::istringstream& is,int n) : my_vec() {
  
  // read the elements
  for (int i=0; i<n ; ++i) {

    // read the pair (key,val)
    uint key;
    is >> key;
    double val;
    is >> val;

    // insert to the map
    my_vec.push_back(IndexValuePair(key,val));

  }
}
/*---------------------------------------------------------------------------*/


/*---------------------------------------------------------------------------*/
void simple_sparse_vector::scale(double s) {

  for( simple_sparse_vector_iterator it = my_vec.begin(); 
      it != my_vec.end(); it++) {
    (*it).second *= s;
  }
}
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
uint simple_sparse_vector::max_index() {

  uint d=0;
  for( simple_sparse_vector_iterator it = my_vec.begin(); 
      it != my_vec.end(); it++) {
    if ((*it).first > d) d = (*it).first;
  }
  return d;
}
/*---------------------------------------------------------------------------*/


/*---------------------------------------------------------------------------*/
double simple_sparse_vector::snorm() {

  double output = 0.0;
  for(simple_sparse_vector_iterator it = my_vec.begin(); 
      it != my_vec.end(); it++) {
    double tmp = (*it).second;
    output += tmp*tmp;
  }

  return(output);
}
/*---------------------------------------------------------------------------*/


/*---------------------------------------------------------------------------*/
void simple_sparse_vector::make_binary() {

  for(simple_sparse_vector_iterator it = my_vec.begin(); 
      it != my_vec.end(); it++) {
    (*it).second = 1.0;
  }

}
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
void simple_sparse_vector::print(std::ostream& os) {

  for(simple_sparse_vector_iterator it = my_vec.begin(); 
      it != my_vec.end(); it++) {
    os << "(" << (*it).first << "," << (*it).second << ") "; 
  }
  os << std::endl;
}
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
void simple_sparse_vector::zero() {
  my_vec.clear();
}
/*---------------------------------------------------------------------------*/



#ifdef nodef

/*---------------------------------------------------------------------------*/
double simple_hash_table::get(uint i) {
 
  uint pos = i % my_vec.size();

  for (simple_sparse_vector_iterator it = my_vec[pos].begin(); 
       it != my_vec[pos].end(); ++it) {
    if ((*it).first == i) return (*it).second;
  }
  return 0.0;

}
/*---------------------------------------------------------------------------*/


/*---------------------------------------------------------------------------*/
double& simple_hash_table::get_ref(uint i) {
 
  uint pos = i % my_vec.size();

  for (simple_sparse_vector_iterator it = my_vec[pos].begin(); 
       it != my_vec[pos].end(); ++it) {
    if ((*it).first == i) return (*it).second;
  }
  IndexValuePair tmp(i,0.0);
  my_vec[pos].push_back(tmp);
  simple_sparse_vector_iterator it = my_vec[pos].end()-1;
  return (*it).second;

}
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
void simple_hash_table::scale(double s) {

  for (simple_hash_table_iterator it = my_vec.begin();
       it != my_vec.end(); it++) {
    for (simple_sparse_vector_iterator it2 = (*it).begin();
	 it2 != (*it).end(); it2++) {
      (*it2).second *= s;
    }
  }
}
/*---------------------------------------------------------------------------*/


/*---------------------------------------------------------------------------*/
void simple_hash_table::scale_and_add(simple_sparse_vector& other, double a, double b) {
  
  scale(a); add(other,b);

}
/*---------------------------------------------------------------------------*/


/*---------------------------------------------------------------------------*/
void simple_hash_table::scale_and_add(simple_hash_table& other, double a, double b) {

  scale(a); add(other,b);

}
/*---------------------------------------------------------------------------*/

void simple_hash_table::add(simple_hash_table& other, double b) { 

  for (simple_hash_table_iterator it = other.my_vec.begin();
       it != other.my_vec.end(); it++) {
    for (simple_sparse_vector_iterator it2 = (*it).begin();
	 it2 != (*it).end(); it2++) {
      double& val = get_ref((*it2).first);
      val += (b * (*it2).second);
    }
  }
}

void simple_hash_table::add(simple_sparse_vector& other, double b) { 

  for (simple_sparse_vector_iterator it = other.my_vec.begin();
       it != other.my_vec.end(); it++) {
    double& val = get_ref((*it).first);
    val += (b * (*it).second);
  }
}


/*---------------------------------------------------------------------------*/
double simple_hash_table::snorm() {

  double output = 0.0;
  for (simple_hash_table_iterator it = my_vec.begin();
       it != my_vec.end(); it++) {
    for (simple_sparse_vector_iterator it2 = (*it).begin();
	 it2 != (*it).end(); it2++) {
      double tmp = (*it2).second;
      output +=  (tmp*tmp);
    }
  }

  return(output);
}
/*---------------------------------------------------------------------------*/


/*---------------------------------------------------------------------------*/
void simple_hash_table::print(std::ostream& os) {

  for (simple_hash_table_iterator it = my_vec.begin();
       it != my_vec.end(); it++) {
    for (simple_sparse_vector_iterator it2 = (*it).begin();
	 it2 != (*it).end(); it2++) {
      os << "(" << (*it2).first << "," << (*it2).second << ") ";       
    }
  }

  os << std::endl;
}
/*---------------------------------------------------------------------------*/


/*---------------------------------------------------------------------------*/
void simple_hash_table::zero() {

  uint m = my_vec.size();
  my_vec.clear();
  my_vec.resize(m);

}
/*---------------------------------------------------------------------------*/


/*---------------------------------------------------------------------------*/
double operator* (simple_hash_table& u, simple_sparse_vector& v) {
  
  double output = 0.0;
  
  for(simple_sparse_vector_iterator it = v.my_vec.begin(); 
      it != v.my_vec.end(); it++) {
    output += (*it).second * u.get((*it).first);
  }

  return(output);
}
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
double operator* (simple_sparse_vector& u, simple_hash_table& v) { 
  return(v*u); 
}
/*---------------------------------------------------------------------------*/

#endif
