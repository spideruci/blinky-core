#!/bin/bash

pwd

## <ENV VARIABLES>
$MAIN_ARGS=
$TRACES_LOC=
$INPUT_ID=
$SUBJECT=
$BLINKY=
$ENTRY=

echo "[ENV VARIABLES]"
echo  "MAIN_ARGS: "$MAIN_ARGS
echo  "TRACES_LOC: "$TRACES_LOC
echo  "INPUT_ID: "$INPUT_ID
echo  "SUBJECT: "$SUBJECT
echo  "BLINKY: "$BLINKY

JAVA="/Users/vpalepu/open-source/java/jre1.7.0_60.jre/Contents/Home/bin/java -d64 -Xmx6g"

BLINKYHOME="/Users/vpalepu/phd-open-source/blinky-core"
BLINKYJAR="$BLINKYHOME/blinky-core/target/blinky-core-0.0.1-SNAPSHOT-jar-with-dependencies.jar"

CONFIG_YAML="$BLINKYHOME/$INPUT_ID-methods.yaml"



BLINKY="-Xbootclasspath/p:$BLINKYJAR -javaagent:$BLINKYJAR=0,retransform,whitelist,purewhitelist,$ENTRY"

# prepare blinky jar file
cd $BLINKYHOME
mvn package -DskipTests -Dconfig.override=$CONFIG_YAML -q
cd -

$JAVA $BLINKY $SUBJECT $MAIN_ARGS > $TRACES_LOC/$INPUT_ID-full.trc 2> $TRACES_LOC/$INPUT_ID-full.err