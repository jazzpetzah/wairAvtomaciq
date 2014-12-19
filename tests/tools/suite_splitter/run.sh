#!/bin/bash -ex

export JAVA_HOME=`/usr/libexec/java_home -v 1.7`

pushd "`pwd`"
cd zephyr_sync
mvn compile
mvn exec:java -Dexec.mainClass="com.wearezeta.suite_splitter.App" -Dexec.args="$@"
popd

