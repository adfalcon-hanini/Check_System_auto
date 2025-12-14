package com.example.tests.dependencies;

import com.example.screensData.clients.GetClientsData;
import com.example.utils.OracleDBConnection;
import io.qameta.allure.*;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

import java.sql.SQLException;

/**
 * Comprehensive demonstration of TestNG test dependencies and priorities
 *
 * Key Concepts Demonstrated:
 * 1. Test Execution Order with priority
 * 2. Hard Dependencies with dependsOnMethods
 * 3. Soft Dependencies with alwaysRun
 * 4. Group Dependencies with dependsOnGroups
 * 5. Best Practices for Test Dependencies
 */
@Epic("TestNG Features")
@Feature("Test Dependencies")
public class TestMethodDependenciesDemo {

    private static final Logger logger = Logger.getLogger(TestMethodDependenciesDemo.class);
    private OracleDBConnection dbConnection;
    private GetClientsData clientsData;
    private String testClientId = "12240";
    private boolean setupCompleted = false;

    /*
     * ============================================
     * SECTION 1: PRIORITY-BASED EXECUTION ORDER
     * ============================================
     * Tests execute in priority order (lowest first)
     * Default priority = 0
     */

    @Test(priority = 1, description = "Initialize database connection")
    @Story("Setup Phase")
    @Description("This test runs first due to priority = 1. Establishes database connection.")
    @Severity(SeverityLevel.BLOCKER)
    public void test01_InitializeConnection() {
        logger.info("=== PRIORITY 1: Initialize Connection ===");
        System.out.println("\n" + "=".repeat(80));
        System.out.println("PRIORITY 1: Initializing Database Connection");
        System.out.println("=".repeat(80));

        try {
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            logger.info("✓ Database connection initialized");
            System.out.println("✓ Database connection established successfully");
            setupCompleted = true;
        } catch (SQLException e) {
            logger.error("✗ Failed to initialize connection", e);
            Assert.fail("Database initialization failed: " + e.getMessage());
        }
    }

    @Test(priority = 2, description = "Create data access object")
    @Story("Setup Phase")
    @Description("This test runs second due to priority = 2")
    @Severity(SeverityLevel.BLOCKER)
    public void test02_CreateDataObject() {
        logger.info("=== PRIORITY 2: Create Data Object ===");
        System.out.println("\n" + "=".repeat(80));
        System.out.println("PRIORITY 2: Creating GetClientsData Object");
        System.out.println("=".repeat(80));

        Assert.assertTrue(setupCompleted, "Setup must be completed first");

        clientsData = new GetClientsData(dbConnection);
        Assert.assertNotNull(clientsData, "ClientsData object should be created");

        logger.info("✓ GetClientsData object created");
        System.out.println("✓ Data access object created successfully");
    }

    /*
     * ============================================
     * SECTION 2: HARD DEPENDENCIES (dependsOnMethods)
     * ============================================
     * Test will NOT run if dependent test fails
     * Test will be SKIPPED if dependency is not met
     */

    @Test(priority = 3,
          dependsOnMethods = {"test01_InitializeConnection", "test02_CreateDataObject"},
          description = "Fetch client data - depends on setup methods")
    @Story("Data Retrieval")
    @Description("Demonstrates hard dependency: This test requires both setup methods to pass")
    @Severity(SeverityLevel.CRITICAL)
    public void test03_FetchClientData() {
        logger.info("=== PRIORITY 3: Fetch Client Data (Hard Dependency) ===");
        System.out.println("\n" + "=".repeat(80));
        System.out.println("PRIORITY 3: Fetching Client Data");
        System.out.println("Dependencies: test01_InitializeConnection, test02_CreateDataObject");
        System.out.println("=".repeat(80));

        boolean success = clientsData.fetchClientData(testClientId);

        Assert.assertTrue(success, "Should fetch client data successfully");
        Assert.assertTrue(clientsData.getRecordCount() > 0, "Should have at least one record");

        logger.info("✓ Client data fetched: " + clientsData.getRecordCount() + " records");
        System.out.println("✓ Client data fetched successfully");
        System.out.println("  - Client ID: " + clientsData.getClientId());
        System.out.println("  - Name: " + clientsData.getNameEnglish());
        System.out.println("  - Records: " + clientsData.getRecordCount());
    }

    @Test(priority = 4,
          dependsOnMethods = "test03_FetchClientData",
          description = "Verify client details - single dependency")
    @Story("Data Validation")
    @Description("Demonstrates single method dependency chain")
    @Severity(SeverityLevel.NORMAL)
    public void test04_VerifyClientDetails() {
        logger.info("=== PRIORITY 4: Verify Client Details (Depends on Fetch) ===");
        System.out.println("\n" + "=".repeat(80));
        System.out.println("PRIORITY 4: Verifying Client Details");
        System.out.println("Dependencies: test03_FetchClientData");
        System.out.println("=".repeat(80));

        String clientId = clientsData.getClientId();
        String nameEnglish = clientsData.getNameEnglish();
        String nationality = clientsData.getNationality();

        Assert.assertNotNull(clientId, "Client ID should not be null");
        Assert.assertFalse(clientId.isEmpty(), "Client ID should not be empty");
        Assert.assertEquals(clientId, testClientId, "Client ID should match");

        logger.info("✓ Client details verified");
        System.out.println("✓ Verification Results:");
        System.out.println("  - Client ID: " + clientId + " ✓");
        System.out.println("  - Name: " + nameEnglish + " ✓");
        System.out.println("  - Nationality: " + nationality + " ✓");
    }

    @Test(priority = 5,
          dependsOnMethods = {"test03_FetchClientData", "test04_VerifyClientDetails"},
          description = "Access data map - multiple dependencies")
    @Story("Data Validation")
    @Description("Demonstrates multiple method dependencies")
    @Severity(SeverityLevel.NORMAL)
    public void test05_AccessDataMap() {
        logger.info("=== PRIORITY 5: Access Data Map (Multiple Dependencies) ===");
        System.out.println("\n" + "=".repeat(80));
        System.out.println("PRIORITY 5: Accessing Data Map");
        System.out.println("Dependencies: test03_FetchClientData, test04_VerifyClientDetails");
        System.out.println("=".repeat(80));

        var allData = clientsData.getAllData();

        Assert.assertNotNull(allData, "Data map should not be null");
        Assert.assertFalse(allData.isEmpty(), "Data map should not be empty");
        // Data map contains client information
        Assert.assertTrue(allData.size() > 0, "Should have data fields");

        logger.info("✓ Data map accessed: " + allData.size() + " fields");
        System.out.println("✓ Data map accessed successfully");
        System.out.println("  - Total fields: " + allData.size());
        System.out.println("  - Sample fields: CL_ID, NAME_ENG, NATIONALITY");
    }

    /*
     * ============================================
     * SECTION 3: SOFT DEPENDENCIES (alwaysRun = true)
     * ============================================
     * Test WILL run even if dependent test fails
     * Useful for cleanup or logging methods
     */

    @Test(priority = 6,
          dependsOnMethods = "test03_FetchClientData",
          alwaysRun = true,
          description = "Print summary - always runs for cleanup")
    @Story("Reporting")
    @Description("Demonstrates soft dependency with alwaysRun=true. Runs even if dependencies fail.")
    @Severity(SeverityLevel.MINOR)
    public void test06_PrintSummary() {
        logger.info("=== PRIORITY 6: Print Summary (Soft Dependency - Always Runs) ===");
        System.out.println("\n" + "=".repeat(80));
        System.out.println("PRIORITY 6: Printing Test Summary");
        System.out.println("Dependencies: test03_FetchClientData (SOFT - alwaysRun=true)");
        System.out.println("=".repeat(80));

        // This will run even if test03_FetchClientData fails
        if (clientsData != null && clientsData.getRecordCount() > 0) {
            System.out.println("✓ Test completed successfully");
            System.out.println("  - Records processed: " + clientsData.getRecordCount());
            logger.info("✓ Summary printed successfully");
        } else {
            System.out.println("⚠ Test completed with warnings");
            System.out.println("  - Some dependent tests may have failed");
            logger.warn("⚠ Summary printed with warnings");
        }
    }

    /*
     * ============================================
     * SECTION 4: DEMONSTRATING FAILURE SCENARIOS
     * ============================================
     */

    @Test(priority = 7,
          enabled = false,  // Disabled to prevent actual failure
          description = "Intentional failure example")
    @Story("Failure Scenarios")
    @Description("This test is disabled but shows what happens when a test fails")
    public void test07_IntentionalFailure() {
        logger.info("=== This test would fail if enabled ===");
        Assert.fail("This test intentionally fails to demonstrate dependency behavior");
    }

    @Test(priority = 8,
          dependsOnMethods = "test07_IntentionalFailure",
          enabled = false,  // Disabled because dependency is disabled
          description = "Skipped due to failed dependency")
    @Story("Failure Scenarios")
    @Description("This test would be SKIPPED because its dependency failed")
    public void test08_WillBeSkipped() {
        logger.info("=== This test will be SKIPPED ===");
        System.out.println("This test never runs because test07_IntentionalFailure failed");
    }

    @Test(priority = 9,
          dependsOnMethods = "test07_IntentionalFailure",
          alwaysRun = true,
          enabled = false,  // Disabled for demo
          description = "Runs despite failed dependency")
    @Story("Failure Scenarios")
    @Description("This test WILL run despite dependency failure due to alwaysRun=true")
    public void test09_RunsDespiteFailure() {
        logger.info("=== This test RUNS even though dependency failed ===");
        System.out.println("✓ This test runs because alwaysRun=true");
        System.out.println("  - Useful for cleanup or reporting");
    }

    /*
     * ============================================
     * SECTION 5: GROUP DEPENDENCIES
     * ============================================
     */

    @Test(priority = 10,
          groups = {"setup", "database"},
          description = "Setup group test")
    @Story("Group Dependencies")
    @Description("Part of 'setup' and 'database' groups")
    public void test10_SetupGroup() {
        logger.info("=== PRIORITY 10: Setup Group Test ===");
        System.out.println("\n" + "=".repeat(80));
        System.out.println("PRIORITY 10: Setup Group Test");
        System.out.println("Groups: setup, database");
        System.out.println("=".repeat(80));
        System.out.println("✓ This test is part of setup group");
    }

    @Test(priority = 11,
          groups = {"validation"},
          dependsOnGroups = {"setup", "database"},
          description = "Validation depends on groups")
    @Story("Group Dependencies")
    @Description("Demonstrates dependsOnGroups - requires all tests in 'setup' and 'database' groups to pass")
    public void test11_ValidationDependsOnGroups() {
        logger.info("=== PRIORITY 11: Validation (Depends on Groups) ===");
        System.out.println("\n" + "=".repeat(80));
        System.out.println("PRIORITY 11: Validation Test");
        System.out.println("Depends on Groups: setup, database");
        System.out.println("=".repeat(80));
        System.out.println("✓ This test runs only if all tests in setup and database groups pass");
    }

    /*
     * ============================================
     * SECTION 6: COMPLEX DEPENDENCY CHAINS
     * ============================================
     */

    @Test(priority = 12,
          dependsOnMethods = {"test03_FetchClientData", "test04_VerifyClientDetails", "test05_AccessDataMap"},
          description = "Complex dependency chain")
    @Story("Complex Dependencies")
    @Description("Demonstrates complex dependency chain - depends on multiple methods in sequence")
    @Severity(SeverityLevel.NORMAL)
    public void test12_ComplexDependencyChain() {
        logger.info("=== PRIORITY 12: Complex Dependency Chain ===");
        System.out.println("\n" + "=".repeat(80));
        System.out.println("PRIORITY 12: Complex Dependency Chain");
        System.out.println("Dependencies: Multiple methods in sequence");
        System.out.println("=".repeat(80));

        // All previous tests must pass for this to run
        Assert.assertNotNull(clientsData, "ClientsData should be initialized");
        Assert.assertTrue(clientsData.getRecordCount() > 0, "Should have data");

        System.out.println("✓ All dependencies satisfied");
        System.out.println("  - Connection: ✓");
        System.out.println("  - Data Object: ✓");
        System.out.println("  - Data Fetch: ✓");
        System.out.println("  - Verification: ✓");
        System.out.println("  - Data Map: ✓");

        logger.info("✓ Complex dependency chain completed successfully");
    }

    /*
     * ============================================
     * SECTION 7: CLEANUP (Always Runs)
     * ============================================
     */

    @AfterClass(alwaysRun = true)
    @Step("Cleanup: Close database connection")
    public void cleanup() {
        logger.info("=== CLEANUP: Closing Database Connection ===");
        System.out.println("\n" + "=".repeat(80));
        System.out.println("CLEANUP PHASE: Closing Resources");
        System.out.println("=".repeat(80));

        if (dbConnection != null) {
            dbConnection.closeConnection();
            logger.info("✓ Database connection closed");
            System.out.println("✓ Database connection closed successfully");
        }

        System.out.println("=".repeat(80));
        System.out.println("TEST DEPENDENCIES DEMONSTRATION COMPLETED");
        System.out.println("=".repeat(80) + "\n");
    }
}
