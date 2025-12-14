package com.example.factory;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

/**
 * Advanced Factory class for Cash Data testing
 * Creates test instances with different NIN and date combinations
 */
public class CashDataTestFactory {

    @Factory(dataProvider = "cashDataProvider")
    public Object[] createInstances(TestDataProvider testData) {
        return new Object[] { new ParameterizedCashTest(testData) };
    }

    @DataProvider(name = "cashDataProvider")
    public static Object[][] cashDataProvider() {
        return new Object[][] {
            // Test with different NIDs and single dates
            { new TestDataProvider("NIN_12240_Nov27", "12240", "27-Nov-2025") },
            { new TestDataProvider("NIN_12240_Nov15", "12240", "15-Nov-2025") },

            // Test with date ranges
            { new TestDataProvider("NIN_12240_Range_Nov", "12240", "01-Nov-2025", "27-Nov-2025") },
            { new TestDataProvider("NIN_12240_Range_Week", "12240", "20-Nov-2025", "27-Nov-2025") },

            // Test with different NIDs using Builder pattern
            { new TestDataProvider.Builder()
                .testName("NIN_Builder_Test_1")
                .nin("12240")
                .date("27-Nov-2025")
                .expectedResult("success")
                .build()
            },

            { new TestDataProvider.Builder()
                .testName("NIN_Builder_Test_Range")
                .nin("12240")
                .startDate("01-Nov-2025")
                .endDate("30-Nov-2025")
                .expectedResult("success")
                .build()
            },
        };
    }
}
