package com.example.factory;

import org.testng.annotations.Factory;

/**
 * Factory class that creates multiple test instances with different client IDs
 * Each instance will run all test methods with its specific client ID
 */
public class ClientDataTestFactory {

    @Factory
    public Object[] createInstances() {
        // Create test instances with different client IDs
        return new Object[] {
            new ParameterizedClientTest(new TestDataProvider("Client_12240_Test", "12240")),
            new ParameterizedClientTest(new TestDataProvider("Client_1218_Test", "1218")),
            new ParameterizedClientTest(new TestDataProvider("Client_312_Test", "312")),
            new ParameterizedClientTest(new TestDataProvider("Client_Invalid_Test", "99999999")),
        };
    }
}
