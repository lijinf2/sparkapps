#!/bin/bash

#switch off sensitivity to ^C
trap '' 2

# single-machine
# /data/opt/spark-2.0.2/bin/spark-shell \

# here goes REPL invoke
/data/opt/spark-1.6.1/bin/spark-shell --master spark://master:7077 \
--driver-memory 4g \
--driver-cores 2 \
--executor-cores 6 \
--executor-memory 10g \
--jars ./inputformat/target/spark-terasort-1.0-SNAPSHOT-jar-with-dependencies.jar

#get back sensitivity to ^C
trap 2
