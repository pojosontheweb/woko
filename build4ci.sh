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
	rm -rf build.log.*
	mvn clean install -Pwebtests | tee build.log 
	BUILD_RESULT=`grep -l "BUILD FAILURE" build.log` 
	if [ "build.log" == "$BUILD_RESULT" ]
	then
		BUILD_RESULT="FAILED"
		MSG="pull develop and have a look at what's going on !"
		BUILD_LOG=`cat build.log`
	        echo Sending email
	        echo "Woko automatic build $BUILD_RESULT - $MSG $BUILD_LOG" | mail -s "[Woko build] $BUILD_RESULT" remi@rvkb.comdd	  
	else
		BUILD_RESULT="SUCCESSFUL"
	fi
	echo Build result : $BUILD_RESULT
fi
