@echo off 

echo __       __     _  __
echo \ \  _  / /___ ^| ^|/ / ___
echo  \ \/ \/ // o \^|   K /   \
echo   \__W__/ \___/^|_^|\_\\_o_/  2.2-beta4
echo              POJOs on the Web !
echo.

if exist pom.xml (
    mvn exec:java -Dexec.mainClass="woko.tooling.cli.Runner" -Dexec.classpathScope=runtime -Dexec.args="%*" -q
) else (
    if "%1" == "init" (
        shift
	) else (
		echo ERROR : No pom file found in current directory.
		:input
		set INPUT=
		set /P INPUT=Do you want to init the project ? [n]: %=%
		if not "%INPUT%" == "y" (
            echo Nothing done. Hope to see you soon !
            goto:EOF 
		)
    )

    set ARTIFACTID=
    set GROUPID=
    set VERSION=
    set GROOVY=
    set BOOTSTRAP=
    set PACKAGE=
    set EXECARGS="generate"

    :GETOPTS
        if /I "%1" == "-h" goto Help
        if /I "%1" == "-n" set ARTIFACTID=%~2& shift
        if /I "%1" == "-m" set GROUPID=%~2& shift
        if /I "%1" == "-v" set VERSION=%~2& shift
        if /I "%1" == "-p" set PACKAGE=%~2& shift
        if /I "%1" == "-g" set GROOVY=yes
        if /I "%1" == "-j" set GROOVY=no
        if /I "%1" == "-b" set BOOTSTRAP=yes
        if /I "%1" == "-l" set BOOTSTRAP=no
        shift
		
    if not "%1"=="" GOTO GETOPTS

    echo Initializing project

    if "X%ARTIFACTID%" == "X" (
        set /P ARTIFACTID=Project name ? : %=%
    )

    if "X%GROUPID%" == "X" (
        set /P GROUPID=Maven groupId ? : %=%
    )

    if "X%VERSION%" == "X" (
        set VERSION=1.0-SNAPSHOT
        set /P VERSION=Your project's version ? [1.0-SNAPSHOT] : %=%
    )

    if not "X%GROOVY%" == "X" (
        set EXECARGS="%EXECARGS% -g %GROOVY%"
    )

    if not "X%BOOTSTRAP%" == "X" (
        set EXECARGS="%EXECARGS% -b %BOOTSTRAP%"
    )


    if not "X%PACKAGE%" == "X" (
        set EXECARGS="%EXECARGS% -p %PACKAGE%"
    )

    echo ^| Generating your project, please wait, it can take a while to download everything...
    call mvn archetype:generate -DarchetypeArtifactId=woko-archetype -DarchetypeGroupId=com.pojosontheweb -DarchetypeVersion=2.2-beta4 -DgroupId="%GROUPID%" -DartifactId="%ARTIFACTID%" -Dversion="%VERSION%" -DinteractiveMode="false" -q

    cd "%ARTIFACTID%"
    call mvn exec:java -Dexec.mainClass="woko.tooling.cli.Runner" -Dexec.classpathScope=runtime -Dexec.args=%EXECARGS% -q

    goto:EOF
	
    :Help
        echo Usage: woko init
        echo  -n project name, serves as the artifact Id (i.e. myapp)
        echo  -m the maven group Id (i.e. com.myexample.myapp)
        echo  -p the default package name, defaults to maven group Id
        echo  -v the version (i.e. 1.0-SNAPSHOT)
        echo  -b use Boostrap css ^& js
        echo  -l use lithium skin
        echo  -g use Groovy
        echo  -j use pure java
        echo  -h this help

)
