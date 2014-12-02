#!/bin/bash -ex

export JAVA_HOME=`/usr/libexec/java_home -v 1.7`

pushd "`pwd`"
cd zephyr_sync
mvn compile
tool_cmdline="$@"
mvn exec:java -Dexec.mainClass="com.wearezeta.zephyr_sync.App" -Dexec.args="$tool_cmdline"
popd

