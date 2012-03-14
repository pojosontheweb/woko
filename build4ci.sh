#!/bin/bash

if [ ! -e ~/wokobuilds ]
then
    echo Creating wokobuilds folder in ~/
    mkdir ~/wokobuilds
fi

cd ~/wokobuilds

echo Switched to directory `pwd`

if [ ! -e .git ]
then 
	echo Repo does not exist, cloning
	git clone https://vankeisb@github.com/vankeisb/woko2.git
fi

cd woko2

echo Switching to branch develop

git checkout develop

echo Checking for changes

PULL_RESULT=`git pull`

if [ "Already up-to-date." == "$PULL_RESULT" ]
then
	echo Up to date, nothing done.
else
	echo Code changed, building...
	rm -rf build.log
	mvn clean install | tee build.log 
	BUILD_RESULT=`grep -l "BUILD FAILURE" build.log` 
	if [ "build.log" == "$BUILD_RESULT" ]
	then
		BUILD_RESULT="FAILED"
	else
		BUILD_RESULT="SUCCESSFUL"
	fi
	echo Build result : $BUILD_RESULT
	echo Woko build $BUILD_RESULT | mail -s "Woko build $BUILD_RESULT" remi@rvkb.com
fi
