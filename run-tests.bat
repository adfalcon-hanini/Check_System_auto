@echo off
REM =============================================================================
REM Oracle Database Testing Framework - Test Execution Script
REM =============================================================================
REM
REM This script provides easy test execution with various options
REM
REM Usage:
REM   run-tests.bat               - Run all tests
REM   run-tests.bat clients       - Run Clients module tests
REM   run-tests.bat portfolio     - Run Portfolio module tests
REM   run-tests.bat orders        - Run Orders module tests
REM   run-tests.bat agm           - Run AGM module tests
REM   run-tests.bat fund          - Run Fund module tests
REM   run-tests.bat xdp           - Run XDP/FIX module tests
REM   run-tests.bat config        - Run Configuration & Utilities tests
REM   run-tests.bat clean         - Clean and run all tests
REM =============================================================================

setlocal enabledelayedexpansion

echo.
echo =========================================================================
echo   Oracle Database Testing Framework - Module-Based Test Runner
echo =========================================================================
echo.

set TEST_MODULE=%1

if "%TEST_MODULE%"=="" (
    echo [INFO] No module specified, running ALL tests...
    echo [INFO] Command: mvn test
    echo.
    mvn test
) else if "%TEST_MODULE%"=="clients" (
    echo [INFO] Running CLIENTS MODULE tests...
    echo [INFO] Tables: SEC_CLIENTS, SEC_CLIENTS_BALANCES, SEC_CLTDAILY_BALANCES
    echo [INFO] Command: mvn test -Dtest="Clients Module"
    echo.
    mvn test -Dtest="Clients Module"
) else if "%TEST_MODULE%"=="portfolio" (
    echo [INFO] Running PORTFOLIO MODULE tests...
    echo [INFO] Tables: SEC_EQ_SHARES, SEC_PORTFOLIO_AVG_PRICE, etc.
    echo [INFO] Command: mvn test -Dtest="Portfolio Module"
    echo.
    mvn test -Dtest="Portfolio Module"
) else if "%TEST_MODULE%"=="orders" (
    echo [INFO] Running ORDERS MODULE tests...
    echo [INFO] Tables: SEC_ORDERS, SEC_ORDERS_HIST
    echo [INFO] Command: mvn test -Dtest="Orders Module"
    echo.
    mvn test -Dtest="Orders Module"
) else if "%TEST_MODULE%"=="agm" (
    echo [INFO] Running AGM MODULE tests...
    echo [INFO] Tables: FUND_AGM_DATES, SEC_VIRTUAL_TRADE, MYCALCULATOR_STUDY
    echo [INFO] Command: mvn test -Dtest="AGM Module"
    echo.
    mvn test -Dtest="AGM Module"
) else if "%TEST_MODULE%"=="fund" (
    echo [INFO] Running FUND MODULE tests...
    echo [INFO] Tables: FUND_CLIENTS, FUND_CLIENTS_MIRROR
    echo [INFO] Command: mvn test -Dtest="Fund Module"
    echo.
    mvn test -Dtest="Fund Module"
) else if "%TEST_MODULE%"=="xdp" (
    echo [INFO] Running XDP/FIX MODULE tests...
    echo [INFO] Tables: XDP_TRADES, XDP_INSTRUMENTS, XDP_QUOTES
    echo [INFO] Command: mvn test -Dtest="XDP/FIX Module"
    echo.
    mvn test -Dtest="XDP/FIX Module"
) else if "%TEST_MODULE%"=="config" (
    echo [INFO] Running CONFIGURATION ^& UTILITIES tests...
    echo [INFO] Command: mvn test -Dtest="Configuration & Utilities"
    echo.
    mvn test -Dtest="Configuration & Utilities"
) else if "%TEST_MODULE%"=="clean" (
    echo [INFO] Cleaning and running all tests...
    echo [INFO] Command: mvn clean test
    echo.
    mvn clean test
) else (
    echo [ERROR] Unknown module: %TEST_MODULE%
    echo.
    echo Supported modules:
    echo   clients    - Clients module (SEC_CLIENTS, SEC_CLIENTS_BALANCES)
    echo   portfolio  - Portfolio module (SEC_EQ_SHARES, SEC_PORTFOLIO_AVG_PRICE, etc.)
    echo   orders     - Orders module (SEC_ORDERS, SEC_ORDERS_HIST)
    echo   agm        - AGM module (FUND_AGM_DATES, SEC_VIRTUAL_TRADE)
    echo   fund       - Fund module (FUND_CLIENTS, FUND_CLIENTS_MIRROR)
    echo   xdp        - XDP/FIX module (XDP_TRADES, XDP_INSTRUMENTS)
    echo   config     - Configuration ^& Utilities tests
    echo   clean      - Clean and run all tests
    echo.
    echo Examples:
    echo   run-tests.bat             - Run all modules
    echo   run-tests.bat clients     - Run only Clients module
    echo   run-tests.bat portfolio   - Run only Portfolio module
    echo.
    goto :end
)

if errorlevel 1 (
    echo.
    echo [ERROR] Tests failed! Check the output above for details.
    echo.
    goto :end
)

echo.
echo =========================================================================
echo   Tests completed successfully!
echo =========================================================================
echo.
echo Next steps:
echo   1. View Allure report:  generate-allure-report.bat
echo   2. Generate PDF report: generate-pdf-report.bat
echo.

:end
endlocal
pause
