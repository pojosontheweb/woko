@echo off

if exist pom.xml (
    mvn exec:java -Dexec.mainClass="woko.tooling.cli.Runner" -Dexec.classpathScope=runtime -Dexec.args="%*" -q
) else (
    echo __       __     _  __
    echo \ \  _  / /___ ^| ^|/ / ___
    echo  \ \/ \/ // o \^|   K /   \
    echo   \__W__/ \___/^|_^|\_\\_o_/  2.0
    echo              POJOs on the Web !
    echo

    if "%1" == "init" (
        shift
	) else (
		echo ERROR : No pom file found in current directory.
		:input
		set INPUT=
		set /P INPUT=Do you want to init the project ? [n]: %=%
		if "%INPUT%"!="y" (
            echo Nothing done. Hope to see you soon !
            exit 0
		)
    )

    set ARTIFACTID=
    set GROUPID=
    set VERSION=
    set GROOVY="no"
    set BOOTSTRAP="no"
    set PACKAGE=

    :GETOPTS
        if /I %1 == -h goto Help
        if /I %1 == -n set ARTIFACTID=%2& shift
        if /I %1 == -m set GROUPID=%2& shift
        if /I %1 == -v set VERSION=%2& shift
        if /I %1 == -p set PACKAGE=%2& shift
        if /I %1 == -g set GROOVY="yes"
        if /I %1 == -b set BOOTSTRAP="yes"
        shift
    if not (%1)==() goto GETOPTS

    echo Initializing project

    if "X%ARTIFACTID%" == "X" (
        set /P ARTIFACTID=Project name ? : %=%
    )

    if "X%GROUPID%" == "X" (
        set /P GROUPID=Maven groupId ?: %=%
    )

    if "X%VERSION%" == "X" (
        set VERSION="1.0-SNAPSHOT"
        set /P VERSION=Your project's version ? [1.0-SNAPSHOT] : %=%
    )

    if "X%PACKAGE%" == "X" (
        set PACKAGE=%GROUPID%
    )

    echo "| Generating your project, please wait, it can take a while to download everything..."
    mvn archetype:generate -DarchetypeArtifactId=woko-archetype -DarchetypeGroupId=com.rvkb\
                    -DgroupId="%GROUPID%" -DartifactId="%ARTIFACTID%" -Dversion="%VERSION%" -DinteractiveMode="false" -q

    cd "%ARTIFACTID%"
    mvn exec:java -Dexec.mainClass="woko.tooling.cli.Runner" -Dexec.classpathScope=runtime -Dexec.args="generate -b %BOOTSTRAP% -g %GROOVY% -p %PACKAGE%" -q

    exit 0

    :Help
        echo Usage: woko init"
        echo " -n project name, serves as the artifact Id (i.e. myapp)"
        echo " -m the maven group Id (i.e. com.myexample.myapp)"
        echo " -p the default package name, defaults to maven group Id"
        echo " -v the version (i.e. 1.0-SNAPSHOT)"
        echo " -b use Boostrap css & js, defaults to no"
        echo " -g use Groovy, defaults to no"

)
