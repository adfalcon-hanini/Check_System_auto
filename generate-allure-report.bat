@echo off
REM =============================================================================
REM Oracle Database Testing Framework - Allure Report Generator
REM =============================================================================
REM
REM This script generates and opens an Allure HTML report
REM
REM Prerequisites:
REM   - Allure must be installed and in PATH
REM   - Tests must have been run first
REM
REM Usage:
REM   generate-allure-report.bat        - Generate and open report
REM   generate-allure-report.bat serve  - Generate and serve report
REM =============================================================================

setlocal enabledelayedexpansion

echo.
echo =========================================================================
echo   Oracle Database Testing Framework - Allure Report Generator
echo =========================================================================
echo.

REM Check if Allure is installed
where allure >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Allure is not installed or not in PATH!
    echo.
    echo Please install Allure:
    echo   Windows (Scoop): scoop install allure
    echo   macOS (Homebrew): brew install allure
    echo.
    echo Or download from: https://github.com/allure-framework/allure2/releases
    goto :error
)

REM Check if allure-results exists
if not exist "target\allure-results" (
    echo [ERROR] Allure results not found!
    echo [ERROR] Please run tests first using: run-tests.bat
    echo.
    goto :error
)

REM Count result files
set COUNT=0
for %%f in (target\allure-results\*-result.json) do set /a COUNT+=1

if %COUNT%==0 (
    echo [ERROR] No test results found in target\allure-results
    echo [ERROR] Please run tests first using: run-tests.bat
    echo.
    goto :error
)

echo [INFO] Found %COUNT% test result files
echo.

set MODE=%1

if "%MODE%"=="serve" (
    echo [INFO] Starting Allure report server...
    echo [INFO] The report will open in your browser
    echo [INFO] Press Ctrl+C to stop the server
    echo.
    allure serve target\allure-results
) else (
    echo [INFO] Generating Allure report...

    REM Preserve history if exists
    if exist "allure-report\history" (
        echo [INFO] Preserving report history...
        xcopy /E /I /Y allure-report\history target\allure-results\history >nul 2>&1
    )

    REM Generate report
    allure generate target\allure-results --clean -o allure-report

    if errorlevel 1 (
        echo [ERROR] Failed to generate Allure report!
        goto :error
    )

    echo [INFO] Opening Allure report...
    allure open allure-report
)

echo.
echo =========================================================================
echo   Allure Report generated successfully!
echo =========================================================================
echo.

goto :end

:error
echo.
echo To generate the Allure report:
echo   1. Install Allure (if not installed)
echo   2. Run tests:     run-tests.bat
echo   3. Generate:      generate-allure-report.bat
echo.

:end
endlocal
