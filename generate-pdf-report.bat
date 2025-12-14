@echo off
REM =============================================================================
REM Oracle Database Testing Framework - PDF Report Generator
REM =============================================================================
REM
REM This script generates a PDF report from Allure test results
REM
REM Prerequisites:
REM   - Tests must have been run first (allure-results must exist)
REM
REM Usage:
REM   generate-pdf-report.bat
REM =============================================================================

setlocal enabledelayedexpansion

echo.
echo =========================================================================
echo   Oracle Database Testing Framework - PDF Report Generator
echo =========================================================================
echo.

REM Check if allure-results directory exists
if not exist "target\allure-results" (
    echo [ERROR] Allure results not found!
    echo [ERROR] Please run tests first using: mvn test
    echo.
    goto :error
)

REM Count result files
set COUNT=0
for %%f in (target\allure-results\*-result.json) do set /a COUNT+=1

if %COUNT%==0 (
    echo [ERROR] No test results found in target\allure-results
    echo [ERROR] Please run tests first using: mvn test
    echo.
    goto :error
)

echo [INFO] Found %COUNT% test result files
echo [INFO] Generating PDF report...
echo.

REM Generate PDF report
mvn exec:java -Dexec.mainClass="com.example.utils.AllurePDFReportGenerator" -Dexec.args="target/allure-results Test-Execution-Report.pdf" -q

if errorlevel 1 (
    echo [ERROR] Failed to generate PDF report!
    echo.
    goto :error
)

echo.
echo =========================================================================
echo   PDF Report generated successfully!
echo =========================================================================
echo.
echo Report location: Test-Execution-Report.pdf
echo.
echo You can now:
echo   1. Open the PDF report
echo   2. Generate HTML Allure report: allure serve target/allure-results
echo.

goto :end

:error
echo.
echo To generate the PDF report:
echo   1. Run tests first:  run-tests.bat
echo   2. Then generate:    generate-pdf-report.bat
echo.

:end
endlocal
pause
