# Check_System_Auto

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![TestNG](https://img.shields.io/badge/TestNG-7.11.0-red.svg)](https://testng.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.27.0-green.svg)](https://www.selenium.dev/)
[![Allure](https://img.shields.io/badge/Allure-2.25.0-yellow.svg)](https://docs.qameta.io/allure/)

Check_System_Auto is a comprehensive automated system checking and testing framework with integrated Selenium WebDriver capabilities, TestNG-based test execution, and Allure reporting. The framework supports database testing, web automation, API testing, and comprehensive reporting.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Test Modules](#test-modules)
- [API Testing](#api-testing)
- [Data-Driven Testing with Factory Pattern](#data-driven-testing-with-factory-pattern)
- [Test Dependencies and Priorities](#test-dependencies-and-priorities)
- [Generating Reports](#generating-reports)
- [Browser Configuration](#browser-configuration)
- [Contributing Guidelines](#contributing-guidelines)
- [Troubleshooting](#troubleshooting)

## Features

### Core Capabilities
- **Database Testing**: Direct Oracle 19c database query execution and validation
- **Web Automation**: Selenium WebDriver integration for UI testing with headless support
- **API Testing**: REST API testing with automatic session management
- **Parallel Execution**: Configurable parallel test execution with TestNG
- **Rich Reporting**: Allure HTML reports with detailed test execution metrics
- **PDF Reports**: Automatic PDF report generation from test results
- **Multi-Environment Support**: Separate configurations for dev, QA, stage, and production
- **Modular Architecture**: Well-organized test modules (Clients, Portfolio, Orders, AGM, Fund, XDP/FIX)
- **Retry Mechanism**: Automatic retry for flaky tests
- **Factory Pattern**: Advanced parameterized testing support
- **Page Object Model**: Clean separation of test logic and page interactions

### Advanced Features
- **Dynamic Test Configuration**: Runtime test selection using TestNG groups
- **SessionID Management**: Automatic extraction and persistence across test runs
- **Builder Pattern**: Flexible test data construction
- **Test Dependencies**: Complex test execution flow control
- **Headless Browser**: Chrome runs hidden for faster execution

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 17 or higher**
  ```bash
  java -version
  ```

- **Apache Maven 3.8 or higher**
  ```bash
  mvn -version
  ```

- **Allure Command Line** (for report generation)
  ```bash
  allure --version
  ```
  Install via:
  - Windows (Scoop): `scoop install allure`
  - macOS (Homebrew): `brew install allure`
  - Or download from: https://github.com/allure-framework/allure2/releases

- **Oracle Database Access**
  - Host: DB01M:1523/GRPUAT
  - Username: sec1
  - Password: sec12345

## Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd POC_OracleDB_Testing_19c
   ```

2. **Install dependencies**
   ```bash
   mvn clean install -DskipTests
   ```

3. **Run tests**
   ```bash
   mvn test
   ```

4. **Generate and view Allure report**
   ```bash
   allure serve target/allure-results
   ```

5. **Generate PDF report**
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.utils.AllurePDFReportGenerator"
   ```

## Project Structure

```
POC_OracleDB_Testing_19c/
├── src/
│   ├── main/java/com/example/
│   │   ├── api/                    # API classes (LoginAPI, CustomerAssetsAPI)
│   │   ├── base/                   # Base classes for pages and tests
│   │   ├── dataBase/               # Database query classes by module
│   │   │   ├── agm/                # AGM module data classes
│   │   │   ├── alerts/             # Client alerts
│   │   │   ├── clients/            # Client data
│   │   │   ├── fund/               # Fund data
│   │   │   ├── mcalc/              # Calculator study data
│   │   │   ├── orders/             # Orders data
│   │   │   ├── portfolio/          # Portfolio data
│   │   │   └── xdp/                # XDP market data
│   │   ├── enums/                  # Enumerations (OrderType, etc.)
│   │   ├── listeners/              # TestNG listeners (TestListener, RetryListener, PDFReportListener)
│   │   ├── models/                 # Data models
│   │   ├── pages/                  # Page Object Model classes
│   │   ├── screensData/            # Screen-specific data extraction classes
│   │   └── utils/                  # Utility classes
│   │       ├── APIConfigManager.java          # API configuration management
│   │       ├── AllurePDFReportGenerator.java  # PDF report generation
│   │       ├── BrowserManager.java            # Browser initialization
│   │       ├── NavigationHelper.java          # Navigation utilities
│   │       └── OracleDBConnection.java        # Database connectivity
│   ├── main/resources/
│   │   ├── api-config.properties              # API configuration with sessionID persistence
│   │   ├── config.properties                  # Main configuration
│   │   ├── config-{env}.properties            # Environment-specific configs
│   │   └── log4j.properties                   # Logging configuration
│   └── test/java/com/example/
│       ├── base/                   # Base test classes
│       ├── dataproviders/          # TestNG data providers
│       ├── factory/                # TestNG Factory classes for parameterized testing
│       │   ├── TestDataProvider.java          # Test data holder with Builder pattern
│       │   ├── ClientDataTestFactory.java     # Client data factory
│       │   ├── CashDataTestFactory.java       # Cash data factory
│       │   └── README.md (to be removed)
│       ├── listeners/              # Test listeners
│       └── tests/                  # Test classes by module
│           ├── agm/                # AGM tests
│           ├── api/                # API tests (LoginAPI, Config)
│           ├── clients/            # Client tests
│           ├── config/             # Configuration tests
│           ├── dependencies/       # Test dependency examples
│           │   └── README.md (to be removed)
│           ├── fix/                # FIX protocol tests
│           ├── fund/               # Fund tests
│           ├── navigation/         # Navigation tests
│           ├── orders/             # Order tests
│           └── portfolio/          # Portfolio tests
├── pom.xml                         # Maven configuration
├── testng.xml                      # Dynamic TestNG suite configuration
└── README.md                       # This file
```

## Configuration

### Database Configuration

The framework uses Oracle Database 19c. Connection details are in test classes:
```java
OracleDBConnection dbConnection = new OracleDBConnection(
    "DB01M:1523/GRPUAT",
    "sec1",
    "sec12345"
);
```

### Environment Configuration

Create environment-specific property files:
- `config-dev.properties`
- `config-qa.properties`
- `config-stage.properties`
- `config-prod.properties`

Example configuration:
```properties
db.host=DB01M:1523/GRPUAT
db.username=sec1
db.password=sec12345
browser=chrome
headless=true
```

### API Configuration

API settings are managed in `api-config.properties`:

```properties
# Base URL for API
api.baseUrl=https://devuat.thegroup.com.qa

# API Endpoints
api.endpoint.url=https://devuat.thegroup.com.qa/jetrade/process
api.endpoint.login=/api/auth/login
api.endpoint.logout=/api/auth/logout

# Session Information (automatically updated after login)
api.sessionID=

# Request Configuration
api.timeout=30000
api.retryAttempts=3
```

**Key Features:**
- Automatic sessionID extraction from login response
- Persistent storage across test runs
- Supports multiple sessionID field names (sessionID, token, accessToken, jwt, etc.)
- 3-level search strategy (root level, nested objects, recursive deep search)

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Module
```bash
# Run by TestNG suite name
mvn test -Dgroups=clients
mvn test -Dgroups=portfolio
mvn test -Dgroups=orders
mvn test -Dgroups=agm
mvn test -Dgroups=fund
mvn test -Dgroups=xdp

# Run specific test class
mvn test -Dtest=GetClientsDataTest
```

### Run with Specific Profile
```bash
# Development environment
mvn test -Pdev

# QA environment
mvn test -Pqa

# Smoke tests only
mvn test -Psmoke

# Regression tests only
mvn test -Pregression
```

### Run Tests in Parallel
Tests are configured to run in parallel by default. Adjust thread count in `testng.xml`:
```xml
<suite name="Test Suite" parallel="tests" thread-count="3">
```

## Test Modules

### 1. Clients Module
Tests for client data management:
- `GetClientsDataTest` - Client information retrieval
- `GetClientsBalancesDataTest` - Client balance operations
- `GetCashDataTest` - Cash balance queries

**Tables**: SEC_CLIENTS, SEC_CLIENTS_BALANCES, SEC_CLTDAILY_BALANCES

### 2. Portfolio Module
Tests for portfolio operations:
- `GetEqSharesDataTest` - Equity shares data
- `GetPortfolioAvgPriceDataTest` - Average price calculations
- `GetEquDailyPortfolioDataTest` - Daily portfolio snapshots
- `GetPortfolioVolumDataTest` - Volume data
- `GetTotalBuyInBetweenDataTest` - Buy transactions
- `GetTotalSellInBetweenDataTest` - Sell transactions

**Tables**: SEC_EQ_SHARES, SEC_EQU_DAILY_PORTFOLIO, SEC_PORTFOLIO_AVG_PRICE

### 3. Orders Module
Tests for order management:
- `GetOrdersDataTest` - Active orders
- `PlacedBuyOrderTest` - Buy order placement
- `PlacedSellOrderTest` - Sell order placement
- `OrdersPageTest` - UI tests for orders page

**Tables**: SEC_ORDERS, SEC_ORDERS_HIST

### 4. AGM Module
Tests for Annual General Meeting operations:
- `GetAGMDataTest` - AGM dates and schedules
- `GetVirtualTradeDataTest` - Virtual trading
- `GetMyCalculatorStudyDataTest` - Calculator studies

**Tables**: FUND_AGM_DATES, SEC_VIRTUAL_TRADE, MYCALCULATOR_STUDY

### 5. Fund Module
Tests for fund operations:
- `GetFundClientsDataTest` - Fund client data
- `GetFundClientsMirrorDataTest` - Mirror fund data

**Tables**: FUND_CLIENTS, FUND_CLIENTS_MIRROR

### 6. XDP/FIX Module
Tests for market data:
- `GetTradesDataTest` - Market trades (9 query methods)
- `GetInstrumentsDataTest` - Trading instruments
- `GetQuotesDataTest` - Market quotes
- `DataTestIdExtractorTest` - UI data extraction

**Tables**: XDP_TRADES, XDP_INSTRUMENTS, XDP_QUOTES, XDP_INDICES

## API Testing

### LoginAPI Usage

The LoginAPI class provides automatic sessionID extraction and persistence:

```java
// Initialize LoginAPI
LoginAPI loginAPI = new LoginAPI();

// Prepare JSON request
String jsonRequest = "{\"username\":\"myuser\",\"password\":\"mypassword\"}";

// Execute login request (sessionID automatically extracted and persisted)
String response = loginAPI.executeLoginRequest(
    LoginAPI.getConfigBaseUrl(),
    jsonRequest
);

// Get sessionID
String sessionID = LoginAPI.getSessionID();

// Use sessionID in other API calls
CustomerAssetsAPI assetsAPI = new CustomerAssetsAPI(baseUrl);
assetsAPI.setAuthToken(sessionID);
```

### SessionID Extraction Process

The system uses a comprehensive 3-level search strategy:

**Level 1: Root Level Search**
- Checks 17+ field name variations: sessionID, token, accessToken, jwt, sid, etc.

**Level 2: Nested Object Search**
- Checks inside 18+ common nested objects: data, result, response, Message, payload, etc.

**Level 3: Recursive Deep Search**
- Recursively searches all nested structures for session-related fields

**Persistence:**
- SessionID automatically saved to `api-config.properties`
- Two-step verification ensures successful persistence
- Reusable across test runs

### API Configuration Methods

```java
// Get configuration values
String baseUrl = APIConfigManager.getBaseUrl();
String endpointURL = APIConfigManager.getEndpointURL();
String sessionID = APIConfigManager.getSessionID();

// Update configuration
APIConfigManager.updateSessionID("new-session-id");
APIConfigManager.clearSessionID();
APIConfigManager.reloadProperties();
```

## Data-Driven Testing with Factory Pattern

The framework includes comprehensive TestNG Factory pattern implementation for data-driven testing.

### Factory Pattern Components

**1. TestDataProvider** - Data holder class with Builder pattern:
```java
// Using Builder pattern
TestDataProvider data = new TestDataProvider.Builder()
    .testName("Advanced_Test")
    .nin("12240")
    .date("27-Nov-2025")
    .expectedResult("success")
    .build();
```

**2. ClientDataTestFactory** - Creates test instances for client data:
```java
@Factory
public Object[] createInstances() {
    return new Object[] {
        new ParameterizedClientTest(new TestDataProvider("Client_1", "12240")),
        new ParameterizedClientTest(new TestDataProvider("Client_2", "1218")),
        new ParameterizedClientTest(new TestDataProvider("Client_3", "312"))
    };
}
```

**3. CashDataTestFactory** - Advanced factory using @DataProvider:
```java
@Factory(dataProvider = "cashTestData")
public CashDataTestFactory(TestDataProvider data) {
    this.testData = data;
}
```

### Running Factory Tests

```bash
# Run all factory tests
mvn clean test -DsuiteXmlFile=testng-factory.xml

# Run specific factory test
mvn clean test -Dtest=ClientDataTestFactory

# Run with parallel execution
mvn clean test -DsuiteXmlFile=testng-factory.xml -Dparallel=classes -DthreadCount=4
```

### Factory Pattern Benefits

1. **Code Reusability**: Write test logic once, run with multiple data sets
2. **Maintainability**: Centralized test data management
3. **Scalability**: Easy to add new test data without changing test code
4. **Separation of Concerns**: Test logic separated from test data
5. **Flexible**: Can load data from any source (Excel, DB, JSON, API)

## Test Dependencies and Priorities

The framework supports comprehensive test execution control using TestNG dependencies and priorities.

### Priority-Based Execution

Controls the ORDER of test execution:
```java
@Test(priority = 1)  // Runs first
public void testA() { }

@Test(priority = 2)  // Runs second
public void testB() { }
```

### Hard Dependencies

Creates execution REQUIREMENTS - test is SKIPPED if dependency fails:
```java
@Test
public void testSetup() { }

@Test(dependsOnMethods = "testSetup")
public void testMain() {
    // Runs only if testSetup passes
}
```

### Soft Dependencies

Test runs EVEN IF dependencies fail (perfect for cleanup):
```java
@Test(dependsOnMethods = "testMain", alwaysRun = true)
public void testCleanup() {
    // This ALWAYS runs, even if testMain fails
}
```

### Group Dependencies

Depend on entire test groups:
```java
@Test(groups = {"setup"})
public void testSetup1() { }

@Test(dependsOnGroups = {"setup"})
public void testValidation() {
    // Runs only if ALL setup tests pass
}
```

### Recommended Pattern: Priority + Dependencies

```java
@Test(priority = 1)
public void setup() { }

@Test(priority = 2, dependsOnMethods = "setup")
public void testMain() { }

@Test(priority = 3, dependsOnMethods = "testMain")
public void validate() { }

@Test(priority = 4, dependsOnMethods = "setup", alwaysRun = true)
public void cleanup() { }
```

## Generating Reports

### Automatic PDF Report Generation

**PDF reports are automatically generated after test execution!**

When you run tests using `mvn test`, a timestamped PDF report will be automatically created in the project root directory.

Example output: `Oracle-DB-Test-Report-20251207-143025.pdf`

### Allure HTML Report
```bash
# Using helper script (Windows)
generate-allure-report.bat

# Or manually
allure serve target/allure-results

# Or generate to specific directory
allure generate target/allure-results --clean -o allure-report
allure open allure-report
```

### Manual PDF Report Generation
```bash
# Using helper script (Windows)
generate-pdf-report.bat

# Or manually with Maven
mvn exec:java -Dexec.mainClass="com.example.utils.AllurePDFReportGenerator"

# Or with custom output path
mvn exec:java -Dexec.mainClass="com.example.utils.AllurePDFReportGenerator" \
  -Dexec.args="target/allure-results Custom-Report-Name.pdf"
```

## Browser Configuration

The framework supports multiple browsers with headless mode enabled by default.

### Supported Browsers
- Chrome (default, headless)
- Firefox
- Edge
- Safari

### Chrome Configuration

Chrome is configured to run in headless mode for faster execution:

```java
// In BrowserManager.java
ChromeOptions options = new ChromeOptions();
options.addArguments("--headless=new");           // Hidden browser
options.addArguments("--window-size=1920,1080");  // Consistent window size
options.addArguments("--disable-notifications");
options.addArguments("--remote-allow-origins=*");
```

**To temporarily show the browser during debugging:**
Comment out the `--headless=new` line in `BrowserManager.java`

## Contributing Guidelines

### Development Workflow

1. Create a new branch from `master`:
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. Make your changes following the coding standards
3. Write tests for your changes
4. Run all tests to ensure nothing is broken
5. Commit your changes with clear messages
6. Push to your branch and create a pull request

### Branch Naming Conventions

- `feature/` - New features
- `bugfix/` - Bug fixes
- `refactor/` - Code refactoring
- `test/` - Test-related changes
- `docs/` - Documentation updates

### Coding Standards

**Java Coding Style:**
1. Indentation: Use 4 spaces (no tabs)
2. Line Length: Maximum 120 characters
3. Naming Conventions:
   - Classes: PascalCase (`GetClientsData`)
   - Methods: camelCase (`fetchClientByNIN`)
   - Constants: UPPER_SNAKE_CASE (`MAX_RETRY_COUNT`)
   - Variables: camelCase (`clientData`)

**Test Structure:**
```java
@Test(priority = 1, description = "Clear description of what this tests")
public void testMethodName() {
    // Arrange - Set up test data

    // Act - Execute the method being tested

    // Assert - Verify the results
}
```

### Commit Message Format

```
<type>: <subject>

<body>

<footer>
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

**Example:**
```
feat: Add GetVirtualTradeData class for SEC_VIRTUAL_TRADE table

- Implement fetchByNIN, fetchByCompany, fetchByReason methods
- Add comprehensive test coverage
- Update documentation with new class information

Closes #123
```

### Pull Request Process

1. Ensure all tests pass: `mvn clean test`
2. Run code formatting (if configured): `mvn spotless:apply`
3. Update documentation if needed
4. At least one reviewer must approve
5. No merge conflicts
6. All tests must pass

## Troubleshooting

### Common Issues

**1. Connection Timeout**
```
Solution: Check database connectivity, verify firewall rules, or increase timeout
```

**2. Tests Fail to Run**
```bash
# Clean and recompile
mvn clean compile

# Verify Java version
java -version  # Should be 17+
```

**3. Allure Report Not Generated**
```bash
# Ensure Allure is installed
allure --version

# Regenerate results
mvn clean test
allure serve target/allure-results
```

**4. PDF Report Generation Fails**
```bash
# Ensure tests have run first to generate allure-results
mvn test
mvn exec:java -Dexec.mainClass="com.example.utils.AllurePDFReportGenerator"
```

**5. Compilation Errors**
```bash
# Clean Maven cache and rebuild
mvn clean install -DskipTests
```

**6. SessionID Not Persisting**

Check file permissions on `api-config.properties`
Check logs for error messages
Verify the response contains a sessionID field

**7. Browser Not Running in Headless Mode**

Verify `--headless=new` is present in `BrowserManager.java:64`
Check Chrome version (should be 109+)

### Logs

- Application logs: `logs/application.log`
- Test execution logs: Console output
- Allure results: `target/allure-results/`
- Test output: `target/surefire-reports/`

## Best Practices

### Database Testing
1. Always clean up test data
2. Use transactions where possible
3. Don't rely on specific data existing
4. Handle connection failures gracefully
5. Close database connections in `@AfterClass`

### API Testing
1. Use configuration constructor for LoginAPI
2. Check sessionID before login
3. Always logout when done
4. Use endpoint URLs from config (don't hardcode)

### Test Dependencies
1. Use Priority for execution order
2. Use Dependencies for true prerequisites
3. Use alwaysRun for cleanup
4. Combine Priority + Dependencies for complex flows
5. Document complex dependencies

### Factory Pattern
1. Use Builder Pattern for complex test data
2. Use flags to enable/disable tests
3. Give each test instance a unique name
4. Each instance should manage its own resources
5. Add detailed logging

## Architecture

### Technology Stack
- **Java 17+** - Programming language
- **Maven** - Build and dependency management
- **TestNG** - Testing framework
- **Selenium WebDriver 4.27.0** - Web automation
- **Oracle JDBC 19c** - Database connectivity
- **Allure 2.25.0** - Test reporting
- **Log4j** - Logging framework

### Design Patterns
- **Page Object Model (POM)** - For UI automation
- **Data Access Layer** - Separate classes for database queries
- **Factory Pattern** - For parameterized testing
- **Builder Pattern** - For test data construction
- **Singleton Pattern** - For configuration management

## Summary

This framework provides a comprehensive solution for:
- Database testing with Oracle 19c
- Web automation with Selenium WebDriver
- API testing with automatic session management
- Data-driven testing with Factory pattern
- Advanced test execution control with dependencies
- Rich reporting with Allure and PDF generation
- Headless browser execution for faster tests
- Multi-environment configuration support

All configured through a single dynamic `testng.xml` file and centralized configuration properties.

## License

Internal project for automated system checking and testing.

## Contact

For questions or support, contact the development team.

---

**Last Updated**: December 2025
**Version**: 2.0.0
