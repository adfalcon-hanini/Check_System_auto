package com.example.tests.clients;

import com.example.screensData.clients.GetClientsBalancesData;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Test class to retrieve client balances data for NIN: 12240
 */
public class GetClientsBalancesDataTest {

    private static final Logger logger = Logger.getLogger(GetClientsBalancesDataTest.class);
    private OracleDBConnection dbConnection;
    private static final String TARGET_NIN = "12240";

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for NIN " + TARGET_NIN + " client balances data");
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Fetch Client Balances Data for NIN " + TARGET_NIN)
    public void testFetchClientBalancesForNin12240() {
        printTableHeader("CLIENT BALANCES DATA - NIN: " + TARGET_NIN);

        GetClientsBalancesData clientBalancesData = new GetClientsBalancesData(dbConnection);
        boolean success = clientBalancesData.fetchClientBalancesByNin(TARGET_NIN);

        if (success) {
            logger.info("Successfully fetched client balances data for NIN: " + TARGET_NIN);

            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║       Client Balances Data - NIN: " + TARGET_NIN + "              ║");
            System.out.println("╠═══════════════════════════════════════════════════════════════╣");
            System.out.println("║  Total Records Found: " + clientBalancesData.getRecordCount());
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");

            // Print first record details using specific fields
            if (clientBalancesData.getRecordCount() > 0) {
                System.out.println("\n--- First Record Details ---");
                System.out.println("NIN: " + clientBalancesData.getNin());
                System.out.println("Current Balance: " + clientBalancesData.getCurBal());
                System.out.println("Matured Balance: " + clientBalancesData.getMaturedBal());
                System.out.println("T3 Balance: " + clientBalancesData.getT3Balance());
                System.out.println("T2 Balance: " + clientBalancesData.getT2Balance());
                System.out.println("T1 Balance: " + clientBalancesData.getT1Balance());
                System.out.println("T0 Balance: " + clientBalancesData.getT0Balance());
                System.out.println("Uncleared Chq Bal: " + clientBalancesData.getUnclearedChqBal());
                System.out.println("Postponded Balance: " + clientBalancesData.getPostpondedBal());
                System.out.println("Created By: " + clientBalancesData.getCreatedBy());
                System.out.println("Creation Date: " + clientBalancesData.getCreationDate());
                System.out.println("User ID: " + clientBalancesData.getUserId());
                System.out.println("Time Stamp: " + clientBalancesData.getTimeStamp());
            }

            // Print all records in table format
            clientBalancesData.printDataTable();
        } else {
            logger.warn("No client balances data found for NIN: " + TARGET_NIN);
            System.out.println("\n⚠ No client balances data found for NIN: " + TARGET_NIN);
        }
    }

    @Test(priority = 2, description = "Display All Client Balances Records")
    public void testDisplayAllClientBalancesRecords() {
        printTableHeader("ALL CLIENT BALANCES RECORDS - NIN: " + TARGET_NIN);

        GetClientsBalancesData clientBalancesData = new GetClientsBalancesData(dbConnection);
        boolean success = clientBalancesData.fetchClientBalancesByNin(TARGET_NIN);

        if (success) {
            logger.info("Displaying all client balances records for NIN: " + TARGET_NIN);

            // Print all records with full details
            clientBalancesData.printAllData();

            // Print summary
            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║                Summary Statistics                             ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");
            System.out.println("\n  NIN: " + TARGET_NIN);
            System.out.println("  Total Records: " + clientBalancesData.getRecordCount());

            // Calculate total balances
            List<Map<String, Object>> allRecords = clientBalancesData.getAllRecords();
            double totalCurBal = 0;
            double totalMaturedBal = 0;
            double totalT3Balance = 0;
            double totalT2Balance = 0;
            double totalT1Balance = 0;
            double totalT0Balance = 0;

            for (Map<String, Object> record : allRecords) {
                try {
                    if (record.get("CUR_BAL") != null) {
                        totalCurBal += Double.parseDouble(record.get("CUR_BAL").toString());
                    }
                    if (record.get("MATURED_BAL") != null) {
                        totalMaturedBal += Double.parseDouble(record.get("MATURED_BAL").toString());
                    }
                    if (record.get("T3_BALANCE") != null) {
                        totalT3Balance += Double.parseDouble(record.get("T3_BALANCE").toString());
                    }
                    if (record.get("T2_BALANCE") != null) {
                        totalT2Balance += Double.parseDouble(record.get("T2_BALANCE").toString());
                    }
                    if (record.get("T1_BALANCE") != null) {
                        totalT1Balance += Double.parseDouble(record.get("T1_BALANCE").toString());
                    }
                    if (record.get("T0_BALANCE") != null) {
                        totalT0Balance += Double.parseDouble(record.get("T0_BALANCE").toString());
                    }
                } catch (NumberFormatException e) {
                    // Skip if unable to parse
                }
            }

            System.out.println("  Total Current Balance: " + totalCurBal);
            System.out.println("  Total Matured Balance: " + totalMaturedBal);
            System.out.println("  Total T3 Balance: " + totalT3Balance);
            System.out.println("  Total T2 Balance: " + totalT2Balance);
            System.out.println("  Total T1 Balance: " + totalT1Balance);
            System.out.println("  Total T0 Balance: " + totalT0Balance);
            System.out.println();
        } else {
            logger.warn("No client balances data found for NIN: " + TARGET_NIN);
            System.out.println("\n⚠ No client balances data found for NIN: " + TARGET_NIN);
        }
    }

    @Test(priority = 3, description = "Test Get Record by Index")
    public void testGetRecordByIndex() {
        printTableHeader("GET CLIENT BALANCES RECORD BY INDEX");

        GetClientsBalancesData clientBalancesData = new GetClientsBalancesData(dbConnection);
        boolean success = clientBalancesData.fetchClientBalancesByNin(TARGET_NIN);

        if (success && clientBalancesData.getRecordCount() > 0) {
            logger.info("Testing getRecordByIndex functionality");

            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║           Testing Record Access by Index                     ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");

            // Get first record (index 0)
            Map<String, Object> firstRecord = clientBalancesData.getRecordByIndex(0);
            if (firstRecord != null) {
                System.out.println("\n--- Record at Index 0 ---");
                System.out.println("  NIN: " + firstRecord.get("NIN"));
                System.out.println("  Current Balance: " + firstRecord.get("CUR_BAL"));
                System.out.println("  Matured Balance: " + firstRecord.get("MATURED_BAL"));
                System.out.println("  T3 Balance: " + firstRecord.get("T3_BALANCE"));
                System.out.println("  T2 Balance: " + firstRecord.get("T2_BALANCE"));
                System.out.println("  T1 Balance: " + firstRecord.get("T1_BALANCE"));
            }

            // Get second record if exists (index 1)
            if (clientBalancesData.getRecordCount() > 1) {
                Map<String, Object> secondRecord = clientBalancesData.getRecordByIndex(1);
                if (secondRecord != null) {
                    System.out.println("\n--- Record at Index 1 ---");
                    System.out.println("  NIN: " + secondRecord.get("NIN"));
                    System.out.println("  Current Balance: " + secondRecord.get("CUR_BAL"));
                    System.out.println("  Matured Balance: " + secondRecord.get("MATURED_BAL"));
                    System.out.println("  T3 Balance: " + secondRecord.get("T3_BALANCE"));
                    System.out.println("  T2 Balance: " + secondRecord.get("T2_BALANCE"));
                    System.out.println("  T1 Balance: " + secondRecord.get("T1_BALANCE"));
                }
            }

            // Test invalid index
            Map<String, Object> invalidRecord = clientBalancesData.getRecordByIndex(999999);
            if (invalidRecord == null) {
                System.out.println("\n✓ Invalid index correctly returns null");
            }
        } else {
            logger.warn("No client balances data found for NIN: " + TARGET_NIN);
            System.out.println("\n⚠ No client balances data found for NIN: " + TARGET_NIN);
        }
    }

    @Test(priority = 4, description = "Display Balance Details by Type")
    public void testDisplayBalanceDetailsByType() {
        printTableHeader("BALANCE DETAILS BY TYPE - NIN: " + TARGET_NIN);

        GetClientsBalancesData clientBalancesData = new GetClientsBalancesData(dbConnection);
        boolean success = clientBalancesData.fetchClientBalancesByNin(TARGET_NIN);

        if (success) {
            logger.info("Displaying balance details by type for NIN: " + TARGET_NIN);

            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║             Balance Details - NIN: " + TARGET_NIN + "                    ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");

            System.out.println("\n--- Current & Matured Balances ---");
            System.out.println("  Current Balance:          " + clientBalancesData.getCurBal());
            System.out.println("  Matured Balance:          " + clientBalancesData.getMaturedBal());
            System.out.println("  Uncleared Chq Balance:    " + clientBalancesData.getUnclearedChqBal());
            System.out.println("  Postponded Balance:       " + clientBalancesData.getPostpondedBal());
            System.out.println("  Collected Bal Previous:   " + clientBalancesData.getCollectedBalPrevious());

            System.out.println("\n--- T+N Balances ---");
            System.out.println("  T3 Balance:               " + clientBalancesData.getT3Balance());
            System.out.println("  T3 Maturity Date:         " + clientBalancesData.getT3MaturityDate());
            System.out.println("  T2 Balance:               " + clientBalancesData.getT2Balance());
            System.out.println("  T2 Maturity Date:         " + clientBalancesData.getT2MaturityDate());
            System.out.println("  T1 Balance:               " + clientBalancesData.getT1Balance());
            System.out.println("  T1 Maturity Date:         " + clientBalancesData.getT1MaturityDate());
            System.out.println("  T0 Balance:               " + clientBalancesData.getT0Balance());

            System.out.println("\n--- Buy/Sell Balances ---");
            System.out.println("  T3 Buy:                   " + clientBalancesData.getT3Buy());
            System.out.println("  T3 Sell:                  " + clientBalancesData.getT3Sell());
            System.out.println("  T2 Buy:                   " + clientBalancesData.getT2Buy());
            System.out.println("  T2 Sell:                  " + clientBalancesData.getT2Sell());
            System.out.println("  T1 Buy:                   " + clientBalancesData.getT1Buy());
            System.out.println("  T1 Sell:                  " + clientBalancesData.getT1Sell());
            System.out.println("  T0 Buy:                   " + clientBalancesData.getT0Buy());
            System.out.println("  T0 Sell:                  " + clientBalancesData.getT0Sell());

            System.out.println("\n--- Liability Balances ---");
            System.out.println("  Liab Current Balance:     " + clientBalancesData.getLiabCurBal());
            System.out.println("  Liab Matured Balance:     " + clientBalancesData.getLiabMaturedBal());
            System.out.println("  Liab Uncleared Chq Bal:   " + clientBalancesData.getLiabUnclearedChqBal());
            System.out.println("  Liab Postponded Bal:      " + clientBalancesData.getLiabPostpondedBal());

            System.out.println("\n--- VISA & QPAY Balances ---");
            System.out.println("  Reserved VISA Balance:    " + clientBalancesData.getReservedVisaBal());
            System.out.println("  VISA Account Income:      " + clientBalancesData.getVisaAccountIncome());
            System.out.println("  VISA Max Daily Cash Bal:  " + clientBalancesData.getVisaMaxDailyCashbal());
            System.out.println("  Uncleared QPAY:           " + clientBalancesData.getUnclearedQpay());
            System.out.println("  Reserved QPAY Refund:     " + clientBalancesData.getReservedQpayRefund());

            System.out.println("\n--- ATM Withdrawals ---");
            System.out.println("  ATM Withdraw Day:         " + clientBalancesData.getAtmWithdrawDay());
            System.out.println("  ATM Withdraw Month:       " + clientBalancesData.getAtmWithdrawMonth());
            System.out.println("  ATM Withdraw Year:        " + clientBalancesData.getAtmWithdrawYear());

            System.out.println("\n--- Other Information ---");
            System.out.println("  Created By:               " + clientBalancesData.getCreatedBy());
            System.out.println("  Creation Date:            " + clientBalancesData.getCreationDate());
            System.out.println("  User ID:                  " + clientBalancesData.getUserId());
            System.out.println("  Time Stamp:               " + clientBalancesData.getTimeStamp());
            System.out.println();
        } else {
            logger.warn("No client balances data found for NIN: " + TARGET_NIN);
            System.out.println("\n⚠ No client balances data found for NIN: " + TARGET_NIN);
        }
    }

    @AfterClass(alwaysRun = true)
    public void teardownDatabase() {
        try {
            if (dbConnection != null) {
                logger.info("Closing database connection");
                dbConnection.closeConnection();
                logger.info("Database connection closed successfully");
            }
        } catch (Exception e) {
            logger.error("Error closing database connection: " + e.getMessage(), e);
        }
    }

    private void printTableHeader(String title) {
        int totalWidth = 80;
        int padding = (totalWidth - title.length() - 4) / 2;
        System.out.println("\n╔" + "═".repeat(totalWidth) + "╗");
        System.out.println("║" + " ".repeat(padding) + "  " + title + "  " + " ".repeat(padding) + "║");
        System.out.println("╚" + "═".repeat(totalWidth) + "╝");
    }
}
