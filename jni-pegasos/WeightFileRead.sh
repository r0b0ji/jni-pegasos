#!/bin/sh
java -Xmx128m -classpath ./lib/jnipegasos.jar:./lib/JSAP-2.1.jar:. -Djava.library.path=./lib WeightFileReadTest --modelFile src/pegasos-native/example/model.dat
