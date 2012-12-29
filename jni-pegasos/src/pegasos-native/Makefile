##########################################################################
# Makefile for sparse pegasos
##########################################################################

HDR = simple_sparse_vec_hash.h WeightVector.h pegasos_optimize.h
SRC = simple_sparse_vec_hash.cc  WeightVector.cc pegasos_optimize.cc cmd_line.cc

CC = g++

JAVAHOME =/usr/lib/jvm/java-6-openjdk
CFLAGS  = -Wall -O3 -I$(JAVAHOME)/include -I$(JAVAHOME)/include/linux 
#CFLAGS  = -g 
LFLAGS  = -L. -lm 

OBJS = $(SRC:.cc=.o)

all: pegasos test_objective pegasos_shared_lib
install: all

sl_cmd_line.o: cmd_line.cc cmd_line.h cmd_option.h
	$(CC) -fPIC -c $(CFLAGS) cmd_line.cc -o sl_cmd_line.o

sl_pegasos_optimize.o: pegasos_optimize.cc
	$(CC) -fPIC -c $(CFLAGS) pegasos_optimize.cc -o sl_pegasos_optimize.o

sl_simple_sparse_vec_hash.o: simple_sparse_vec_hash.cc
	$(CC) -fPIC -c $(CFLAGS) simple_sparse_vec_hash.cc -o sl_simple_sparse_vec_hash.o

sl_WeightVector.o: WeightVector.cc
	$(CC) -fPIC -c $(CFLAGS) WeightVector.cc -o sl_WeightVector.o

sl_pegasos_jni.o: pegasos_jni.cc
	$(CC) -fPIC -c $(CFLAGS) pegasos_jni.cc -o sl_pegasos_jni.o

SHAREDLIB_OBJS = sl_cmd_line.o sl_pegasos_optimize.o sl_simple_sparse_vec_hash.o sl_WeightVector.o sl_pegasos_jni.o 
pegasos_shared_lib: $(SHAREDLIB_OBJS) sl_pegasos_jni.o
	$(CC) $(LFLAGS) -shared -Wl,-soname,libpegasos.so.0 -o libpegasos.so $(SHAREDLIB_OBJS)

test_objective: $(OBJS) test_objective.o
	$(CC) $(OBJS) test_objective.o $(LFLAGS) -o test_objective

pegasos: $(OBJS) main.o
	$(CC) $(OBJS) main.o $(LFLAGS) -o pegasos

tar: $(SRC) $(HDR) Makefile 
	tar zcvf pegasos.tgz *.cc *.h Makefile license.txt README data.tgz

%.o: %.cc $(HDR)
	$(CC) $(CFLAGS) -c $<

clean:
	rm -f *.o *.od *.oc *.so *~ \#*\# depend pegasos test_objective pegasos.exe test_objective.exe

install:
	cp -vf libpegasos.so ../../lib
