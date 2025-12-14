package com.example.factory;

import com.example.utils.ExcelDataReader;
import org.testng.annotations.Factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Advanced Factory class that loads test data from Excel file
 * Demonstrates integration with ExcelDataReader utility
 */
public class ExcelDataTestFactory {

    /**
     * Creates test instances from Excel data
     * This is a real-world example of data-driven testing
     */
    @Factory
    public Object[] createTestsFromExcel() {
        List<Object> tests = new ArrayList<>();

        try {
            // Example: Load test data from Excel
            // Uncomment and modify the path when you have test data Excel file
            /*
            ExcelDataReader excelReader = new ExcelDataReader("src/test/resources/testdata/ClientTestData.xlsx");
            List<Map<String, String>> testDataList = excelReader.readExcelData("ClientTests");

            for (Map<String, String> row : testDataList) {
                if (row.get("Enabled").equalsIgnoreCase("Yes")) {
                    TestDataProvider data = new TestDataProvider.Builder()
                        .testName(row.get("TestName"))
                        .clientId(row.get("ClientID"))
                        .expectedResult(row.get("ExpectedResult"))
                        .build();

                    tests.add(new ParameterizedClientTest(data));
                }
            }
            */

            // For demonstration, using hardcoded data
            // Replace this with actual Excel reading code above
            tests.add(new ParameterizedClientTest(
                new TestDataProvider.Builder()
                    .testName("Excel_Client_1")
                    .clientId("12240")
                    .expectedResult("success")
                    .build()
            ));

            tests.add(new ParameterizedClientTest(
                new TestDataProvider.Builder()
                    .testName("Excel_Client_2")
                    .clientId("1218")
                    .expectedResult("success")
                    .build()
            ));

        } catch (Exception e) {
            System.err.println("Error loading test data from Excel: " + e.getMessage());
            e.printStackTrace();
        }

        return tests.toArray();
    }

    /**
     * Example structure for Excel test data file:
     *
     * Sheet: ClientTests
     * Columns:
     * - TestName (String): Unique name for the test
     * - ClientID (String): Client ID to test
     * - ExpectedResult (String): success/failure
     * - Enabled (String): Yes/No
     *
     * Sample Data:
     * TestName          | ClientID | ExpectedResult | Enabled
     * ------------------|----------|----------------|--------
     * Client_Valid_1    | 12240    | success        | Yes
     * Client_Valid_2    | 1218     | success        | Yes
     * Client_Invalid_1  | 99999999 | failure        | Yes
     * Client_Disabled   | 312      | success        | No
     */

    /**
     * Creates test instances for Cash Data from Excel
     */
    @Factory
    public Object[] createCashTestsFromExcel() {
        List<Object> tests = new ArrayList<>();

        try {
            // Example: Load cash test data from Excel
            /*
            ExcelDataReader excelReader = new ExcelDataReader("src/test/resources/testdata/CashTestData.xlsx");
            List<Map<String, String>> testDataList = excelReader.readExcelData("CashTests");

            for (Map<String, String> row : testDataList) {
                if (row.get("Enabled").equalsIgnoreCase("Yes")) {
                    TestDataProvider data = new TestDataProvider.Builder()
                        .testName(row.get("TestName"))
                        .nin(row.get("NIN"))
                        .date(row.get("Date"))
                        .startDate(row.get("StartDate"))
                        .endDate(row.get("EndDate"))
                        .expectedResult(row.get("ExpectedResult"))
                        .build();

                    tests.add(new ParameterizedCashTest(data));
                }
            }
            */

            // For demonstration
            tests.add(new ParameterizedCashTest(
                new TestDataProvider.Builder()
                    .testName("Excel_Cash_1")
                    .nin("12240")
                    .date("27-Nov-2025")
                    .expectedResult("success")
                    .build()
            ));

        } catch (Exception e) {
            System.err.println("Error loading cash test data from Excel: " + e.getMessage());
            e.printStackTrace();
        }

        return tests.toArray();
    }
}
