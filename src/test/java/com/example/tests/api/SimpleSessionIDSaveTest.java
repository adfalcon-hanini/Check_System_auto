package com.example.tests.api;

import com.example.utils.APIConfigManager;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Simple test to save sessionID and verify it persists after test finishes
 */
public class SimpleSessionIDSaveTest {

    @Test
    public void saveSessionIDAndVerify() throws Exception {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SAVING SESSIONID TO CONFIG FILE");
        System.out.println("=".repeat(80));

        // Create a simple sessionID
        String sessionID = "TEST-SESSION-" + System.currentTimeMillis();
        System.out.println("\nSessionID to save: " + sessionID);

        // Save it
        boolean saved = APIConfigManager.updateSessionID(sessionID);
        System.out.println("Save result: " + saved);
        assertTrue(saved, "SessionID should be saved");

        // Get the file path
        String projectRoot = System.getProperty("user.dir");
        String filePath = projectRoot + "\\src\\main\\resources\\api-config.properties";

        System.out.println("\n" + "=".repeat(80));
        System.out.println("FILE LOCATION:");
        System.out.println("=".repeat(80));
        System.out.println(filePath);

        // Read and display file content
        System.out.println("\n" + "=".repeat(80));
        System.out.println("CURRENT FILE CONTENT:");
        System.out.println("=".repeat(80));

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (String line : lines) {
            if (line.contains("api.sessionID")) {
                System.out.println(">>> " + line + " <<<");
            } else {
                System.out.println(line);
            }
        }

        // Verify sessionID is in the file
        boolean found = false;
        for (String line : lines) {
            if (line.contains("api.sessionID=" + sessionID)) {
                found = true;
                break;
            }
        }

        assertTrue(found, "SessionID must be in the file");

        System.out.println("\n" + "=".repeat(80));
        System.out.println("✓✓✓ SUCCESS ✓✓✓");
        System.out.println("=".repeat(80));
        System.out.println("SessionID saved to: " + filePath);
        System.out.println("SessionID value: " + sessionID);
        System.out.println("\nPLEASE CHECK THE FILE NOW:");
        System.out.println("1. Open: " + filePath);
        System.out.println("2. Look for line: api.sessionID=" + sessionID);
        System.out.println("3. The file should contain this exact value");
        System.out.println("=".repeat(80) + "\n");

        // Wait 2 seconds so user can check
        System.out.println("Waiting 2 seconds before test ends...");
        Thread.sleep(2000);
        System.out.println("Test completed. File should still contain the sessionID.");
    }
}
