@echo off
echo ========================================
echo Git Setup and Push to GitHub
echo ========================================
echo.

cd /d C:\Check_System_Auto

echo Adding all files to git...
git add .

echo.
echo Creating initial commit...
git commit -m "Initial commit: Add Check_System_Auto test automation project - Added Maven project structure with Selenium WebDriver - Included Allure reporting configuration - Added test execution and reporting scripts"

echo.
echo Adding remote repository...
git remote add origin https://github.com/adfalcon-hanini/Check_System_auto.git

echo.
echo Pushing to GitHub master branch...
git push -u origin master

echo.
echo ========================================
echo Done! Project pushed to GitHub
echo ========================================
pause
