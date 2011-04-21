#!/bin/sh

TARGET=$1
RELEASETYPE=$2
RELEASE=$3
shift
shift
shift

ANT=$ANT_HOME/bin/ant

if [ "$ANT_HOME" = "" ]; then
	echo ANT_HOME is not set, please set ANT_HOME before calling this script
	exit
fi

$ANT $@ -lib ../lib -f common/buildRequirements.xml cleanRequirements

if [ "$TARGET" = "product" ]; then
	$ANT $@ -lib ../lib -f product/productBuild.xml release -DreleaseType=$RELEASETYPE -DreleaseNumber=$RELEASE | tee build.log

	if [ "$?" = "0" ]; then
		$ANT $@ -lib ../lib -f product/buildResults.xml publish.log
	else
		echo "\t[JBossIDE-Build ERROR] There was an error running the build"
		exit -1
	fi

else
	$ANT $@ -lib ../lib -f builder-wrap.xml release -Dbuilder=$TARGET -DreleaseType=$RELEASETYPE -DreleaseNumber=$RELEASE
fi