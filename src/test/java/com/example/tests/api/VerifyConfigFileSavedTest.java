package com.example.tests.api;

import com.example.utils.APIConfigManager;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.*;

/**
 * Test to verify that the config file is actually SAVED to disk after updateSessionID
 * This test reads the file directly from disk to confirm persistence
 */
public class VerifyConfigFileSavedTest {

    @Test
    public void testConfigFileIsSavedToDisk() throws Exception {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("TEST: VERIFY CONFIG FILE IS ACTUALLY SAVED TO DISK");
        System.out.println("=".repeat(80));

        // STEP 1: Get the config file path
        System.out.println("\nSTEP 1: Locating config file");
        System.out.println("-".repeat(80));

        // We need to figure out where the file is
        String projectRoot = System.getProperty("user.dir");
        String configFilePath = projectRoot + "\\src\\main\\resources\\api-config.properties";
        File configFile = new File(configFilePath);

        System.out.println("Project root: " + projectRoot);
        System.out.println("Config file path: " + configFilePath);
        System.out.println("File exists: " + configFile.exists());

        assertTrue(configFile.exists(), "Config file must exist at: " + configFilePath);
        System.out.println("✓ Config file found");

        // STEP 2: Get file's last modified timestamp BEFORE update
        System.out.println("\nSTEP 2: Getting file timestamp BEFORE update");
        System.out.println("-".repeat(80));

        long lastModifiedBefore = configFile.lastModified();
        System.out.println("Last modified (before): " + lastModifiedBefore);
        System.out.println("Last modified (readable): " + new java.util.Date(lastModifiedBefore));

        // Wait a moment to ensure timestamp will be different
        Thread.sleep(1000);

        // STEP 3: Update sessionID
        System.out.println("\nSTEP 3: Updating sessionID");
        System.out.println("-".repeat(80));

        String newSessionID = "verified-session-" + System.currentTimeMillis();
        System.out.println("New sessionID: " + newSessionID);

        boolean updated = APIConfigManager.updateSessionID(newSessionID);
        assertTrue(updated, "updateSessionID should return true");
        System.out.println("✓ updateSessionID returned true");

        // STEP 4: Check file timestamp AFTER update
        System.out.println("\nSTEP 4: Checking if file was actually modified");
        System.out.println("-".repeat(80));

        long lastModifiedAfter = configFile.lastModified();
        System.out.println("Last modified (after):  " + lastModifiedAfter);
        System.out.println("Last modified (readable): " + new java.util.Date(lastModifiedAfter));

        assertTrue(lastModifiedAfter > lastModifiedBefore,
            "File timestamp should change after update! Before: " + lastModifiedBefore + ", After: " + lastModifiedAfter);
        System.out.println("✓ File timestamp changed - FILE WAS MODIFIED!");

        // STEP 5: Read file content directly from disk (not through API)
        System.out.println("\nSTEP 5: Reading file DIRECTLY from disk");
        System.out.println("-".repeat(80));

        Properties directProps = new Properties();
        FileInputStream fileInput = new FileInputStream(configFile);
        directProps.load(fileInput);
        fileInput.close();

        String sessionIDFromFile = directProps.getProperty("api.sessionID");
        System.out.println("SessionID read directly from file: " + sessionIDFromFile);

        // STEP 6: Verify the value matches
        System.out.println("\nSTEP 6: Verifying sessionID matches");
        System.out.println("-".repeat(80));
        System.out.println("Expected: " + newSessionID);
        System.out.println("Actual:   " + sessionIDFromFile);

        assertEquals(sessionIDFromFile, newSessionID,
            "SessionID in file should match what we set");
        System.out.println("✓ Values match!");

        // STEP 7: Read file as text to show actual content
        System.out.println("\nSTEP 7: Displaying actual file content");
        System.out.println("-".repeat(80));

        List<String> lines = Files.readAllLines(Paths.get(configFilePath));
        System.out.println("File content:");
        System.out.println("-------------");
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            // Highlight the sessionID line
            if (line.contains("api.sessionID")) {
                System.out.println(">>> " + line + " <<<");
            } else {
                System.out.println("    " + line);
            }
        }
        System.out.println("-------------");

        // Verify sessionID appears in file content
        boolean foundInFile = false;
        for (String line : lines) {
            if (line.contains("api.sessionID=" + newSessionID)) {
                foundInFile = true;
                System.out.println("✓ Found sessionID in file content: " + line);
                break;
            }
        }

        assertTrue(foundInFile, "SessionID should appear in file content");

        // FINAL SUMMARY
        System.out.println("\n" + "=".repeat(80));
        System.out.println("TEST SUMMARY");
        System.out.println("=".repeat(80));
        System.out.println("✓ Step 1: Config file located at: " + configFilePath);
        System.out.println("✓ Step 2: Captured file timestamp before update");
        System.out.println("✓ Step 3: Called updateSessionID()");
        System.out.println("✓ Step 4: File timestamp changed - PROOF FILE WAS SAVED");
        System.out.println("✓ Step 5: Read file directly from disk");
        System.out.println("✓ Step 6: SessionID value matches");
        System.out.println("✓ Step 7: SessionID found in actual file content");
        System.out.println("=".repeat(80));
        System.out.println("✓✓✓ CONFIRMED: CONFIG FILE IS SAVED TO DISK ✓✓✓");
        System.out.println("=".repeat(80));
        System.out.println("File location: " + configFile.getAbsolutePath());
        System.out.println("Last saved: " + new java.util.Date(lastModifiedAfter));
        System.out.println("SessionID: " + newSessionID);
        System.out.println("=".repeat(80) + "\n");
    }
}
