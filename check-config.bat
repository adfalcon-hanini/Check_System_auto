@echo off
echo ================================================================================
echo CHECKING CONFIG FILE BEFORE AND AFTER TEST
echo ================================================================================
echo.

echo BEFORE TEST:
echo ================================================================================
type "src\main\resources\api-config.properties"
echo.
echo ================================================================================
echo.

echo Running test...
call mvn test -Dtest=SimpleSessionIDSaveTest -q

echo.
echo AFTER TEST:
echo ================================================================================
type "src\main\resources\api-config.properties"
echo.
echo ================================================================================
echo.

echo DONE! Please check if api.sessionID line changed above.
pause
