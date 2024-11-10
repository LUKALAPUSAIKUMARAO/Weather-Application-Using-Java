@echo off
REM Set paths for source, library, and resources
set SRC=src
set LIB=lib\gson-2.11.0.jar
set BIN=bin

REM Create bin folder if it doesn't exist
if not exist %BIN% (
    mkdir %BIN%
)

REM Compile the Java files
echo Compiling Java files...
javac -cp "%LIB%" -d %BIN% %SRC%\*.java
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b
)

REM Run the application
echo Running WeatherApp...
java -cp "%BIN%;%LIB%" WeatherApp
pause
