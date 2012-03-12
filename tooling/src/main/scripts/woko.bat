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
        java -cp "%WOKO_HOME%\lib\groovy-all-1.7.4.jar;%WOKO_HOME%\lib\woko-tooling-2.0-SNAPSHOT" woko.tooling.cli.Init %*
	) else (
		echo ERROR : No pom file found in current directory.
		:input
		set INPUT=
		set /P INPUT=Do you want to init the project ? [n]: %=%
		if "%INPUT%"=="y" (
            echo Initializing project
            java -cp "%WOKO_HOME%\lib\groovy-all-1.7.4.jar;%WOKO_HOME%\lib\woko-tooling-2.0-SNAPSHOT" woko.tooling.cli.Init %*
		) else (
            echo Nothing done. Hope to see you soon !
		)
	)	
)
