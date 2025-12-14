package com.example.factory;

import org.testng.annotations.Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * Advanced Factory class demonstrating dynamic test creation from external data sources
 * This could be extended to read from Excel, JSON, Database, etc.
 */
public class DataDrivenTestFactory {

    @Factory
    public Object[] createTestsFromDataSource() {
        // Simulate reading data from external source (Excel, JSON, DB, etc.)
        List<TestDataProvider> testDataList = loadTestData();

        // Create test instances dynamically based on loaded data
        List<Object> tests = new ArrayList<>();

        for (TestDataProvider data : testDataList) {
            // Determine which test class to instantiate based on data
            if (data.getClientId() != null) {
                tests.add(new ParameterizedClientTest(data));
            } else if (data.getNin() != null) {
                tests.add(new ParameterizedCashTest(data));
            }
        }

        return tests.toArray();
    }

    /**
     * Simulates loading test data from external source
     * In real scenario, this could read from:
     * - Excel file using Apache POI
     * - JSON file using Jackson/Gson
     * - Database using JDBC
     * - CSV file
     * - API endpoint
     */
    private List<TestDataProvider> loadTestData() {
        List<TestDataProvider> testData = new ArrayList<>();

        // Client data tests
        testData.add(new TestDataProvider("Dynamic_Client_12240", "12240"));
        testData.add(new TestDataProvider("Dynamic_Client_1218", "1218"));

        // Cash data tests
        testData.add(new TestDataProvider.Builder()
            .testName("Dynamic_Cash_NIN_12240")
            .nin("12240")
            .date("27-Nov-2025")
            .build()
        );

        testData.add(new TestDataProvider.Builder()
            .testName("Dynamic_Cash_Range")
            .nin("12240")
            .startDate("01-Nov-2025")
            .endDate("30-Nov-2025")
            .build()
        );

        return testData;
    }

    /**
     * Example: Load test data from array/collection
     */
    @Factory
    public Object[] createTestsFromArray() {
        String[] clientIds = {"12240", "1218", "312"};
        Object[] tests = new Object[clientIds.length];

        for (int i = 0; i < clientIds.length; i++) {
            TestDataProvider data = new TestDataProvider(
                "Array_Client_" + clientIds[i],
                clientIds[i]
            );
            tests[i] = new ParameterizedClientTest(data);
        }

        return tests;
    }
}
