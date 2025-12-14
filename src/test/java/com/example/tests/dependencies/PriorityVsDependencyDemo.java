package com.example.tests.dependencies;

import io.qameta.allure.*;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Demonstrates the difference between priority and dependsOnMethods
 *
 * Key Learning Points:
 * 1. Priority controls execution ORDER
 * 2. dependsOnMethods creates execution DEPENDENCIES
 * 3. Combining both provides fine-grained control
 * 4. Understanding when to use each approach
 */
@Epic("TestNG Features")
@Feature("Priority vs Dependencies")
public class PriorityVsDependencyDemo {

    private static final Logger logger = Logger.getLogger(PriorityVsDependencyDemo.class);

    /*
     * ============================================
     * SCENARIO 1: PRIORITY ONLY
     * ============================================
     * Tests execute in priority order
     * If one fails, others still run
     */

    @Test(priority = 1, description = "Priority only - Test 1")
    @Story("Priority Only Scenario")
    @Description("Uses priority=1, no dependencies. Runs first but others run even if this fails.")
    public void scenario1_PriorityTest1() {
        logger.info("SCENARIO 1 - Priority Test 1 (priority=1)");
        System.out.println("\n--- SCENARIO 1: PRIORITY ONLY ---");
        System.out.println("Test 1 (priority=1): Running first");
        Assert.assertTrue(true, "Test 1 passes");
        System.out.println("✓ Test 1 passed");
    }

    @Test(priority = 2, description = "Priority only - Test 2")
    @Story("Priority Only Scenario")
    @Description("Uses priority=2, no dependencies. Runs after Test 1 but is NOT dependent on it.")
    public void scenario1_PriorityTest2() {
        logger.info("SCENARIO 1 - Priority Test 2 (priority=2)");
        System.out.println("Test 2 (priority=2): Running second");
        System.out.println("✓ Test 2 runs regardless of Test 1 result");
        Assert.assertTrue(true, "Test 2 passes independently");
    }

    @Test(priority = 3, description = "Priority only - Test 3")
    @Story("Priority Only Scenario")
    @Description("Uses priority=3. Demonstrates independent execution.")
    public void scenario1_PriorityTest3() {
        logger.info("SCENARIO 1 - Priority Test 3 (priority=3)");
        System.out.println("Test 3 (priority=3): Running third");
        System.out.println("✓ Test 3 runs independently");
        System.out.println("KEY POINT: All tests run even if earlier ones fail\n");
    }

    /*
     * ============================================
     * SCENARIO 2: DEPENDENCIES ONLY
     * ============================================
     * Tests execute based on dependencies
     * If one fails, dependent tests are skipped
     */

    @Test(description = "Dependency only - Test A")
    @Story("Dependencies Only Scenario")
    @Description("No priority specified. This is the root of dependency chain.")
    public void scenario2_DependencyTestA() {
        logger.info("SCENARIO 2 - Dependency Test A (no priority)");
        System.out.println("\n--- SCENARIO 2: DEPENDENCIES ONLY ---");
        System.out.println("Test A (no priority): Foundation test");
        Assert.assertTrue(true, "Test A passes");
        System.out.println("✓ Test A passed");
    }

    @Test(dependsOnMethods = "scenario2_DependencyTestA",
          description = "Dependency only - Test B")
    @Story("Dependencies Only Scenario")
    @Description("Depends on Test A. Will be SKIPPED if Test A fails.")
    public void scenario2_DependencyTestB() {
        logger.info("SCENARIO 2 - Dependency Test B (depends on A)");
        System.out.println("Test B (depends on A): Running after A");
        System.out.println("✓ Test B runs because Test A passed");
        Assert.assertTrue(true, "Test B passes");
    }

    @Test(dependsOnMethods = "scenario2_DependencyTestB",
          description = "Dependency only - Test C")
    @Story("Dependencies Only Scenario")
    @Description("Depends on Test B. Creates a dependency chain: A -> B -> C")
    public void scenario2_DependencyTestC() {
        logger.info("SCENARIO 2 - Dependency Test C (depends on B)");
        System.out.println("Test C (depends on B): Running after B");
        System.out.println("✓ Test C runs because both A and B passed");
        System.out.println("KEY POINT: If A or B fails, C is SKIPPED\n");
    }

    /*
     * ============================================
     * SCENARIO 3: PRIORITY + DEPENDENCIES
     * ============================================
     * Best of both worlds: controlled order + dependencies
     */

    @Test(priority = 10,
          description = "Priority + Dependency - Test X")
    @Story("Priority + Dependencies Scenario")
    @Description("Uses priority=10. Defines execution order and serves as dependency root.")
    @Severity(SeverityLevel.CRITICAL)
    public void scenario3_PriorityDependencyTestX() {
        logger.info("SCENARIO 3 - Test X (priority=10)");
        System.out.println("\n--- SCENARIO 3: PRIORITY + DEPENDENCIES ---");
        System.out.println("Test X (priority=10): Foundation with explicit order");
        Assert.assertTrue(true, "Test X passes");
        System.out.println("✓ Test X passed at priority 10");
    }

    @Test(priority = 11,
          dependsOnMethods = "scenario3_PriorityDependencyTestX",
          description = "Priority + Dependency - Test Y")
    @Story("Priority + Dependencies Scenario")
    @Description("Uses priority=11 AND depends on Test X. Guaranteed order and dependency.")
    @Severity(SeverityLevel.CRITICAL)
    public void scenario3_PriorityDependencyTestY() {
        logger.info("SCENARIO 3 - Test Y (priority=11, depends on X)");
        System.out.println("Test Y (priority=11, depends on X): Controlled execution");
        System.out.println("✓ Test Y: Runs after X (priority) AND requires X to pass (dependency)");
        Assert.assertTrue(true, "Test Y passes");
    }

    @Test(priority = 12,
          dependsOnMethods = "scenario3_PriorityDependencyTestY",
          description = "Priority + Dependency - Test Z")
    @Story("Priority + Dependencies Scenario")
    @Description("Uses priority=12 AND depends on Test Y. Final test in chain.")
    @Severity(SeverityLevel.NORMAL)
    public void scenario3_PriorityDependencyTestZ() {
        logger.info("SCENARIO 3 - Test Z (priority=12, depends on Y)");
        System.out.println("Test Z (priority=12, depends on Y): End of chain");
        System.out.println("✓ Test Z: Precise order + dependency chain");
        System.out.println("KEY POINT: Best practice for complex test suites\n");
    }

    /*
     * ============================================
     * SCENARIO 4: DEMONSTRATING SKIP BEHAVIOR
     * ============================================
     */

    @Test(priority = 20,
          enabled = false,  // Disabled to prevent actual failure
          description = "Test that would fail")
    @Story("Skip Behavior Scenario")
    @Description("Disabled test that demonstrates skip propagation")
    public void scenario4_FailingTest() {
        logger.info("SCENARIO 4 - This test would fail if enabled");
        System.out.println("\n--- SCENARIO 4: SKIP BEHAVIOR ---");
        Assert.fail("This test fails to demonstrate skip behavior");
    }

    @Test(priority = 21,
          dependsOnMethods = "scenario4_FailingTest",
          enabled = false,  // Disabled because dependency is disabled
          description = "Test that gets skipped")
    @Story("Skip Behavior Scenario")
    @Description("This test would be SKIPPED due to failed dependency")
    public void scenario4_SkippedTest() {
        logger.info("SCENARIO 4 - This test is SKIPPED");
        System.out.println("This test is SKIPPED because scenario4_FailingTest failed");
    }

    @Test(priority = 22,
          dependsOnMethods = "scenario4_FailingTest",
          alwaysRun = true,
          enabled = false,  // Disabled for demo
          description = "Test that runs despite failure")
    @Story("Skip Behavior Scenario")
    @Description("This test RUNS despite dependency failure due to alwaysRun=true")
    public void scenario4_AlwaysRunsTest() {
        logger.info("SCENARIO 4 - This test RUNS (alwaysRun=true)");
        System.out.println("✓ This test RUNS despite dependency failure");
        System.out.println("  Reason: alwaysRun=true\n");
    }

    /*
     * ============================================
     * SCENARIO 5: REAL-WORLD EXAMPLE
     * ============================================
     * Practical example mimicking real test scenario
     */

    @Test(priority = 30,
          groups = {"initialization"},
          description = "Real-world: System initialization")
    @Story("Real-World Scenario")
    @Description("Step 1: Initialize system resources")
    @Severity(SeverityLevel.BLOCKER)
    public void realWorld_01_InitializeSystem() {
        logger.info("REAL-WORLD - Initialize System");
        System.out.println("\n--- SCENARIO 5: REAL-WORLD EXAMPLE ---");
        System.out.println("Step 1: Initialize System (priority=30)");
        System.out.println("✓ System initialized successfully");
    }

    @Test(priority = 31,
          dependsOnMethods = "realWorld_01_InitializeSystem",
          groups = {"initialization"},
          description = "Real-world: Load configuration")
    @Story("Real-World Scenario")
    @Description("Step 2: Load configuration - depends on system initialization")
    @Severity(SeverityLevel.CRITICAL)
    public void realWorld_02_LoadConfiguration() {
        logger.info("REAL-WORLD - Load Configuration");
        System.out.println("Step 2: Load Configuration (priority=31, depends on Step 1)");
        System.out.println("✓ Configuration loaded successfully");
    }

    @Test(priority = 32,
          dependsOnMethods = {"realWorld_01_InitializeSystem", "realWorld_02_LoadConfiguration"},
          groups = {"functional"},
          description = "Real-world: Execute business logic")
    @Story("Real-World Scenario")
    @Description("Step 3: Execute business logic - depends on initialization steps")
    @Severity(SeverityLevel.CRITICAL)
    public void realWorld_03_ExecuteBusinessLogic() {
        logger.info("REAL-WORLD - Execute Business Logic");
        System.out.println("Step 3: Execute Business Logic (priority=32, depends on Steps 1 & 2)");
        System.out.println("✓ Business logic executed successfully");
    }

    @Test(priority = 33,
          dependsOnMethods = "realWorld_03_ExecuteBusinessLogic",
          groups = {"validation"},
          description = "Real-world: Validate results")
    @Story("Real-World Scenario")
    @Description("Step 4: Validate results - depends on business logic execution")
    @Severity(SeverityLevel.NORMAL)
    public void realWorld_04_ValidateResults() {
        logger.info("REAL-WORLD - Validate Results");
        System.out.println("Step 4: Validate Results (priority=33, depends on Step 3)");
        System.out.println("✓ Results validated successfully");
    }

    @Test(priority = 34,
          dependsOnMethods = "realWorld_03_ExecuteBusinessLogic",
          alwaysRun = true,
          groups = {"cleanup"},
          description = "Real-world: Cleanup resources")
    @Story("Real-World Scenario")
    @Description("Step 5: Cleanup - always runs for resource cleanup")
    @Severity(SeverityLevel.MINOR)
    public void realWorld_05_Cleanup() {
        logger.info("REAL-WORLD - Cleanup");
        System.out.println("Step 5: Cleanup (priority=34, alwaysRun=true)");
        System.out.println("✓ Resources cleaned up");
        System.out.println("\nKEY POINT: Cleanup runs even if validation fails");
        System.out.println("=".repeat(80));
    }

    /*
     * ============================================
     * SUMMARY TEST
     * ============================================
     */

    @Test(priority = 100,
          alwaysRun = true,
          description = "Summary of concepts")
    @Story("Summary")
    @Description("Summarizes all demonstrated concepts")
    public void printSummary() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SUMMARY: PRIORITY VS DEPENDENCIES");
        System.out.println("=".repeat(80));
        System.out.println();
        System.out.println("PRIORITY:");
        System.out.println("  ✓ Controls execution ORDER");
        System.out.println("  ✓ Lower numbers run first (priority=1 before priority=2)");
        System.out.println("  ✓ Default priority = 0");
        System.out.println("  ✓ Tests run independently (failure doesn't affect others)");
        System.out.println();
        System.out.println("DEPENDENCIES (dependsOnMethods):");
        System.out.println("  ✓ Creates execution REQUIREMENTS");
        System.out.println("  ✓ Dependent test SKIPPED if dependency fails");
        System.out.println("  ✓ Can depend on multiple methods");
        System.out.println("  ✓ Creates execution chains");
        System.out.println();
        System.out.println("SOFT DEPENDENCIES (alwaysRun=true):");
        System.out.println("  ✓ Test runs EVEN IF dependencies fail");
        System.out.println("  ✓ Useful for cleanup and reporting");
        System.out.println("  ✓ Best practice for @AfterMethod, @AfterClass");
        System.out.println();
        System.out.println("BEST PRACTICES:");
        System.out.println("  ✓ Use priority for execution order");
        System.out.println("  ✓ Use dependsOnMethods for true dependencies");
        System.out.println("  ✓ Combine both for complex scenarios");
        System.out.println("  ✓ Use alwaysRun=true for cleanup");
        System.out.println("  ✓ Keep dependency chains short and simple");
        System.out.println("  ✓ Document complex dependencies clearly");
        System.out.println();
        System.out.println("WHEN TO USE WHAT:");
        System.out.println("  Priority: When order matters but tests are independent");
        System.out.println("  Dependencies: When tests require previous tests to pass");
        System.out.println("  Both: When you need both order AND dependencies");
        System.out.println("  alwaysRun: For cleanup, reporting, logging");
        System.out.println("=".repeat(80) + "\n");
    }
}
