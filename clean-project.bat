@echo off
REM =============================================================================
REM Oracle Database Testing Framework - Project Cleaner
REM =============================================================================
REM
REM This script cleans build artifacts, test results, and temporary files
REM
REM Usage:
REM   clean-project.bat        - Clean all generated files
REM   clean-project.bat deep   - Deep clean (including IDE files)
REM =============================================================================

setlocal enabledelayedexpansion

echo.
echo =========================================================================
echo   Oracle Database Testing Framework - Project Cleaner
echo =========================================================================
echo.

set CLEAN_TYPE=%1

if "%CLEAN_TYPE%"=="deep" (
    echo [INFO] Performing DEEP CLEAN...
    echo [WARN] This will remove IDE configurations and cached files
    echo.
    choice /C YN /M "Are you sure you want to continue"
    if errorlevel 2 goto :end
    if errorlevel 1 goto :deep_clean
)

:normal_clean
echo [INFO] Cleaning Maven build artifacts...
call mvn clean

if exist "target" (
    echo [INFO] Removing target directory...
    rmdir /s /q target 2>nul
)

if exist "allure-report" (
    echo [INFO] Removing allure-report directory...
    rmdir /s /q allure-report 2>nul
)

if exist "test-output" (
    echo [INFO] Removing test-output directory...
    rmdir /s /q test-output 2>nul
)

if exist "logs" (
    echo [INFO] Removing logs directory...
    rmdir /s /q logs 2>nul
)

if exist "screenshots" (
    echo [INFO] Removing screenshots directory...
    rmdir /s /q screenshots 2>nul
)

if exist "Test-Execution-Report.pdf" (
    echo [INFO] Removing Test-Execution-Report.pdf...
    del /q Test-Execution-Report.pdf 2>nul
)

if exist "test-output.log" (
    echo [INFO] Removing test-output.log...
    del /q test-output.log 2>nul
)

if exist "nul" (
    echo [INFO] Removing nul file...
    del /q nul 2>nul
)

echo.
echo [SUCCESS] Normal clean completed!
goto :end_message

:deep_clean
echo.
echo [INFO] Performing deep clean...
call :normal_clean

if exist ".idea" (
    echo [INFO] Removing .idea directory...
    rmdir /s /q .idea 2>nul
)

if exist "*.iml" (
    echo [INFO] Removing .iml files...
    del /q *.iml 2>nul
)

if exist ".vscode" (
    echo [INFO] Removing .vscode directory...
    rmdir /s /q .vscode 2>nul
)

if exist ".settings" (
    echo [INFO] Removing .settings directory...
    rmdir /s /q .settings 2>nul
)

if exist ".project" (
    echo [INFO] Removing .project file...
    del /q .project 2>nul
)

if exist ".classpath" (
    echo [INFO] Removing .classpath file...
    del /q .classpath 2>nul
)

echo.
echo [SUCCESS] Deep clean completed!

:end_message
echo.
echo =========================================================================
echo   Project cleaned successfully!
echo =========================================================================
echo.
echo Next steps:
echo   1. Rebuild:  mvn clean install -DskipTests
echo   2. Run tests: run-tests.bat
echo.

:end
endlocal
pause
