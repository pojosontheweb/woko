@echo off 
if exist pom.xml (
    mvn exec:java -Dexec.mainClass="woko.tooling.cli.Runner" -Dexec.classpathScope=runtime -Dexec.args="%1 %2 %3 %4 %5" -q
) else (
    echo __       __     _  __
    echo \ \  _  / /___ ^| ^|/ / ___
    echo  \ \/ \/ // o \^|   K /   \
    echo   \__W__/ \___/^|_^|\_\\_o_/  2.0
    echo              POJOs on the Web !
    echo

    if "%1" == "init" (
        echo Initializing project
        :artifactId
        set ARTIFACTID=
        set /P ARTIFACTID=Project name ? : %=%
        :groupId
        set GROUPID=
        set /P GROUPID=Maven groupId ? : %=%
        :version
        set VERSION="1.0-SNAPSHOT"
        set /P VERSION=Your project's version ? [1.0-SNAPSHOT] : %=%

        echo Generating your project, please wait, it can take a while to download everything...
        mvn archetype:generate -DarchetypeArtifactId=woko-archetype -DarchetypeGroupId=com.rvkb -DgroupId="%GROUPID%" -DartifactId="%ARTIFACTID%" -Dversion="%VERSION%" -DinteractiveMode="false" -q

        cd "%ARTIFACTID%"
        mvn exec:java -Dexec.mainClass="woko.tooling.cli.Runner" -Dexec.classpathScope=runtime -Dexec.args="init" -q
	) else (
		echo ERROR : No pom file found in current directory.
		:input
		set INPUT=
		set /P INPUT=Do you want to init the project ? [n]: %=%
		if "%INPUT%"=="y" (
            echo Initializing project
            :artifactId
            set ARTIFACTID=
            set /P ARTIFACTID=Project name ? : %=%
            :groupId
            set GROUPID=
            set /P GROUPID=Maven groupId ? : %=%
            :version
            set VERSION="1.0-SNAPSHOT"
            set /P VERSION=Your project's version ? [1.0-SNAPSHOT] : %=%

            mvn archetype:generate -DarchetypeArtifactId=woko-archetype -DarchetypeGroupId=com.rvkb\
                            -DgroupId="%GROUPID%" -DartifactId="%ARTIFACTID%" -Dversion="%VERSION%" -DinteractiveMode="false" -q

            cd "%ARTIFACTID%"
            mvn exec:java -Dexec.mainClass="woko.tooling.cli.Runner" -Dexec.classpathScope=runtime -Dexec.args="init" -q
		) else (
            echo Nothing done. Hope to see you soon !
		)
	)	
)
