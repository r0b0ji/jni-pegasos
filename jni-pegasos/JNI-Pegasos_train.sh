#!/bin/sh
java -Xmx128m -classpath ./lib/jnisvmlight.jar:./lib/jnipegasos.jar:./lib/JSAP-2.1.jar:. -Djava.library.path=./lib JNIPegasosTrainTest --modelFile src/pegasos-native/example/model.dat --dataFile src/pegasos-native/example/train.dat 
