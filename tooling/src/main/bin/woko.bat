@echo off
IF NOT DEFINED WOKO_HOME GOTO NO_VAR_DEF
java -cp "%WOKO_HOME%\lib\commons-collections-3.2.1.jar;%WOKO_HOME%\lib\commons-lang-2.4.jar;%WOKO_HOME%\lib\jline-0.9.94.jar;%WOKO_HOME%\lib\velocity-1.7.jar;%WOKO_HOME%\lib\groovy-all-1.7.0.jar;%WOKO_HOME%\lib\woko-builder-0.1.jar" com.rvkb.woko.tools.Launcher %*
GOTO END
:NO_VAR_DEF
ECHO the WOKO_HOME environment variable must be set
:END