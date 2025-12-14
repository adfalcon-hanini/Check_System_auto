package com.example.tests.api;

import com.example.utils.APIConfigManager;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test to verify that updateSessionID can save to config file
 */
public class VerifySessionIDSaveTest {

    @Test
    public void testUpdateSessionIDCanSaveToFile() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST: VERIFY updateSessionID CAN SAVE TO CONFIG FILE");
        System.out.println("=".repeat(70));

        // STEP 1: Clear any existing sessionID
        System.out.println("\nSTEP 1: Clearing existing sessionID");
        System.out.println("-".repeat(70));
        boolean cleared = APIConfigManager.clearSessionID();
        System.out.println("Cleared: " + cleared);

        // STEP 2: Verify it's cleared
        System.out.println("\nSTEP 2: Verifying sessionID is cleared");
        System.out.println("-".repeat(70));
        String clearedValue = APIConfigManager.getSessionID();
        System.out.println("Value after clear: '" + clearedValue + "'");
        assertTrue(clearedValue.isEmpty(), "SessionID should be empty after clearing");
        System.out.println("✓ SessionID is cleared");

        // STEP 3: Update sessionID with test value
        System.out.println("\nSTEP 3: Updating sessionID with test value");
        System.out.println("-".repeat(70));
        String testSessionID = "test-session-id-" + System.currentTimeMillis();
        System.out.println("Test value: " + testSessionID);

        boolean updated = APIConfigManager.updateSessionID(testSessionID);
        System.out.println("Update returned: " + updated);

        if (!updated) {
            fail("updateSessionID returned false - check logs above for detailed error messages");
        }
        System.out.println("✓ updateSessionID returned true");

        // STEP 4: Read back from config file
        System.out.println("\nSTEP 4: Reading sessionID back from config file");
        System.out.println("-".repeat(70));

        // Force reload from file
        APIConfigManager.reloadProperties();

        String retrievedValue = APIConfigManager.getSessionID();
        System.out.println("Retrieved value: " + retrievedValue);

        // STEP 5: Verify they match
        System.out.println("\nSTEP 5: Verifying values match");
        System.out.println("-".repeat(70));
        System.out.println("Expected: " + testSessionID);
        System.out.println("Actual:   " + retrievedValue);

        assertEquals(retrievedValue, testSessionID,
            "SessionID read from config should match what was written");

        System.out.println("✓ Values match!");

        // FINAL SUMMARY
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST SUMMARY");
        System.out.println("=".repeat(70));
        System.out.println("✓ Step 1: Cleared existing sessionID");
        System.out.println("✓ Step 2: Verified clearing worked");
        System.out.println("✓ Step 3: Updated sessionID with test value");
        System.out.println("✓ Step 4: Read back from config file");
        System.out.println("✓ Step 5: Verified values match");
        System.out.println("=".repeat(70));
        System.out.println("✓✓✓ ALL STEPS PASSED - updateSessionID IS WORKING ✓✓✓");
        System.out.println("=".repeat(70) + "\n");
    }
}
