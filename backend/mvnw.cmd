@echo off
REM Maven Wrapper Startup Script for Windows
REM This script downloads and runs Maven without having it pre-installed.

setlocal enabledelayedexpansion

set "MAVEN_PROJECT_BASEDIR=%~dp0"
set "WRAPPER_DIR=%MAVEN_PROJECT_BASEDIR%.mvn\wrapper"
set "WRAPPER_JAR=%WRAPPER_DIR%maven-wrapper.jar"
set "WRAPPER_PROPERTIES=%WRAPPER_DIR%maven-wrapper.properties"
set "WRAPPER_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
set "DISTRIBUTION_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip"

REM Download wrapper jar if not present
if not exist "%WRAPPER_JAR%" (
    echo Downloading Maven Wrapper JAR...
    if not exist "%WRAPPER_DIR%" mkdir "%WRAPPER_DIR%"
    curl -f -L -o "%WRAPPER_JAR%" "%WRAPPER_URL%"
    if errorlevel 1 (
        echo Error: Failed to download Maven Wrapper JAR
        exit /b 1
    )
)

REM Read distribution URL from properties file if it exists
if exist "%WRAPPER_PROPERTIES%" (
    for /f "tokens=2 delims==" %%a in ('findstr "^distributionUrl=" "%WRAPPER_PROPERTIES%"') do set "DISTRIBUTION_URL=%%a"
)

set "MAVEN_HOME=%WRAPPER_DIR%maven"

REM Extract Maven if not already present
if not exist "%MAVEN_HOME%" (
    echo Downloading and extracting Maven...
    if not exist "%WRAPPER_DIR%" mkdir "%WRAPPER_DIR%"
    set "TMP_ZIP=%WRAPPER_DIR%maven.zip"
    curl -f -L -o "%TMP_ZIP%" "%DISTRIBUTION_URL%"
    if errorlevel 1 (
        echo Error: Failed to download Maven distribution
        exit /b 1
    )
    tar -xf "%TMP_ZIP%" -C "%WRAPPER_DIR%"
    for /d %%a in ("%WRAPPER_DIR%apache-maven-*") do move "%%a" "%MAVEN_HOME%"
    del "%TMP_ZIP%"
)

REM Run Maven
"%MAVEN_HOME%\bin\mvn.cmd" %*