#!/bin/sh
java -Xmx128m -classpath ./lib/jnipegasos.jar:./lib/JSAP-2.1.jar:. -Djava.library.path=./lib JNIPegasosClassifyTest --modelFile src/pegasos-native/example/model.dat --testFile src/pegasos-native/example/test.dat --predictFile src/pegasos-native/example/predict.dat 
