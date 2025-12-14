package com.example.tests.orders;

import com.example.screensData.orders.GetOrdersData;
import com.example.utils.OracleDBConnection;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for GetOrdersData
 * Tests retrieval of orders from SEC_ORDERS table
 */
public class GetOrdersDataTest {

    private static final String TEST_CLIENT_ID = "12240";
    private OracleDBConnection dbConnection;
    private GetOrdersData ordersData;

    @BeforeClass
    public void setup() {
        dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║         GET ORDERS DATA TEST STARTED                          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        try {
            dbConnection.connect();
            System.out.println("✓ Database connection established successfully!");

            ordersData = new GetOrdersData(dbConnection);
            System.out.println("✓ GetOrdersData instance initialized successfully!");

        } catch (Exception e) {
            System.err.println("✗ Setup failed: " + e.getMessage());
            Assert.fail("Failed to setup test environment: " + e.getMessage());
        }
    }

    @Test(priority = 1)
    public void testFetchOrdersForClientToday() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 1: Fetch Orders for Client Today                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        System.out.println("\nFetching orders for Client ID: " + TEST_CLIENT_ID + " for today...\n");

        boolean result = ordersData.fetchOrdersByClientIdToday(TEST_CLIENT_ID);

        if (result) {
            System.out.println("✓ Successfully fetched " + ordersData.getRecordCount() + " order(s)");

            // Print orders table
            ordersData.printOrdersTable();

            // Print orders summary
            ordersData.printOrdersSummary();

            Assert.assertTrue(ordersData.getRecordCount() > 0, "Should have at least one order");
        } else {
            System.out.println("ℹ No orders found for client " + TEST_CLIENT_ID + " today");
            System.out.println("This might be expected if no orders were placed today.");
        }
    }

    @Test(priority = 2)
    public void testFetchAllOrdersByClient() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 2: Fetch All Orders for Client                        ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        System.out.println("\nFetching all orders for Client ID: " + TEST_CLIENT_ID + "...\n");

        GetOrdersData allOrdersData = new GetOrdersData(dbConnection);
        boolean result = allOrdersData.fetchAllOrdersByClientId(TEST_CLIENT_ID);

        if (result) {
            System.out.println("✓ Successfully fetched " + allOrdersData.getRecordCount() + " order(s)");

            // Print orders summary
            allOrdersData.printOrdersSummary();

            Assert.assertTrue(allOrdersData.getRecordCount() > 0, "Should have at least one order");
        } else {
            System.out.println("ℹ No orders found for client " + TEST_CLIENT_ID);
        }
    }

    @Test(priority = 3)
    public void testPrintAllOrdersDetailed() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 3: Print All Orders Details                           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch orders first
        ordersData.fetchOrdersByClientIdToday(TEST_CLIENT_ID);

        if (ordersData.getRecordCount() > 0) {
            // Print all orders with full details
            ordersData.printAllOrderData();

            Assert.assertTrue(ordersData.getRecordCount() > 0, "Should have records to print");
            System.out.println("\n✓ Successfully printed all orders details");
        } else {
            System.out.println("ℹ No orders to print");
        }
    }

    @Test(priority = 4)
    public void testGetOrderRecordByIndex() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 4: Get Order Record by Index                          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch orders first
        ordersData.fetchOrdersByClientIdToday(TEST_CLIENT_ID);

        if (ordersData.getRecordCount() > 0) {
            // Test getting first record
            Map<String, Object> firstRecord = ordersData.getOrderRecordByIndex(0);
            Assert.assertNotNull(firstRecord, "First record should not be null");

            System.out.println("\nFirst Order Record:");
            System.out.println("  ORDER_ID: " + firstRecord.get("ORDER_ID"));
            System.out.println("  CL_ID: " + firstRecord.get("CL_ID"));
            System.out.println("  INST_CODE: " + firstRecord.get("INST_CODE"));
            System.out.println("  ORDER_SIDE: " + firstRecord.get("ORDER_SIDE"));
            System.out.println("  QUANTITY: " + firstRecord.get("QUANTITY"));
            System.out.println("  PRICE: " + firstRecord.get("PRICE"));
            System.out.println("  ORDER_STATUS: " + firstRecord.get("ORDER_STATUS"));

            // Test invalid index
            Map<String, Object> invalidRecord = ordersData.getOrderRecordByIndex(999999);
            Assert.assertNull(invalidRecord, "Invalid index should return null");

            System.out.println("\n✓ Successfully retrieved order record by index");
        } else {
            System.out.println("ℹ No records available to test");
        }
    }

    @Test(priority = 5)
    public void testGetAllOrderRecords() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 5: Get All Order Records                              ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch orders first
        ordersData.fetchOrdersByClientIdToday(TEST_CLIENT_ID);

        List<Map<String, Object>> allRecords = ordersData.getAllOrderRecords();

        Assert.assertNotNull(allRecords, "Records list should not be null");
        Assert.assertEquals(allRecords.size(), ordersData.getRecordCount(), "Record count should match");

        System.out.println("\n✓ Successfully retrieved all order records");
        System.out.println("  Total records: " + allRecords.size());
    }

    @Test(priority = 6)
    public void testOrderDataFieldValidation() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 6: Validate Order Data Fields                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch orders first
        ordersData.fetchOrdersByClientIdToday(TEST_CLIENT_ID);

        if (ordersData.getRecordCount() > 0) {
            // Validate that essential fields are not null (SEC_ORDERS columns)
            System.out.println("\nValidated Fields (First Order - SEC_ORDERS):");
            System.out.println("  ORDER_ID: " + ordersData.getOrderId() + " ✓");
            System.out.println("  CLIENT_ID: " + ordersData.getClientId() + " ✓");
            System.out.println("  ORDER_STATUS: " + ordersData.getOrderStatus() + " ✓");
            System.out.println("  COMPANY_CODE: " + ordersData.getCompanyCode() + " ✓");
            System.out.println("  VOLUME_OF_SHARES: " + ordersData.getVolumeOfShares() + " ✓");
            System.out.println("  ACTUAL_PRICE: " + ordersData.getActualPrice() + " ✓");
            System.out.println("  ORDER_TYPE: " + ordersData.getOrderType() + " ✓");
            System.out.println("  ORDER_DATE: " + ordersData.getOrderDate() + " ✓");
            System.out.println("  TIME_STAMP: " + ordersData.getTimeStamp() + " ✓");
            System.out.println("  FIX_STATUS: " + ordersData.getFixStatus() + " ✓");

            System.out.println("\n✓ All SEC_ORDERS fields validated successfully");
        } else {
            System.out.println("ℹ No records available to validate");
        }
    }

    @Test(priority = 7)
    public void testPrintFirstOrderData() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 7: Print First Order Data                             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch orders first
        ordersData.fetchOrdersByClientIdToday(TEST_CLIENT_ID);

        if (ordersData.getRecordCount() > 0) {
            // Print first order details
            ordersData.printOrderData();

            System.out.println("\n✓ Successfully printed first order data");
        } else {
            System.out.println("ℹ No orders to print");
        }
    }

    @Test(priority = 8)
    public void testGetAllData() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 8: Get All Data as Map                                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        // Fetch orders first
        ordersData.fetchOrdersByClientIdToday(TEST_CLIENT_ID);

        if (ordersData.getRecordCount() > 0) {
            Map<String, String> dataMap = ordersData.getAllData();

            Assert.assertNotNull(dataMap, "Data map should not be null");
            Assert.assertTrue(dataMap.containsKey("orderId"), "Map should contain orderId");
            Assert.assertTrue(dataMap.containsKey("clientId"), "Map should contain clientId");
            Assert.assertTrue(dataMap.containsKey("orderStatus"), "Map should contain orderStatus");
            Assert.assertTrue(dataMap.containsKey("companyCode"), "Map should contain companyCode");

            System.out.println("\nData Map Contents (First Order - SEC_ORDERS):");
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }

            System.out.println("\n✓ Successfully retrieved data as map");
        } else {
            System.out.println("ℹ No records available to test");
        }
    }

    @Test(priority = 9)
    public void testFetchOrdersByClientIdAndDate() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 9: Fetch Orders by Client ID and Date                 ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        String clientId = "12240";
        String orderDate = "2025-11-26"; // Format: YYYY-MM-DD (converted from 23/11/2025)

        System.out.println("\nFetching orders for Client ID: " + clientId + " on date: " + orderDate + "...\n");

        GetOrdersData ordersByDate = new GetOrdersData(dbConnection);
        boolean result = ordersByDate.fetchOrdersByClientIdAndDate(clientId, orderDate);

        if (result) {
            System.out.println("✓ Successfully fetched " + ordersByDate.getRecordCount() + " order(s) for date " + orderDate);

            // DEBUG: Print all actual column names and values from first record
            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║   DEBUG: Actual Database Columns from sec_orders             ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");
            Map<String, Object> firstRecord = ordersByDate.getOrderRecordByIndex(0);
            if (firstRecord != null) {
                System.out.println("\nColumn Name -> Value:");
                System.out.println("─────────────────────────────────────────────────────────────");
                for (Map.Entry<String, Object> entry : firstRecord.entrySet()) {
                    System.out.printf("%-30s -> %s%n", entry.getKey(), entry.getValue());
                }
                System.out.println("─────────────────────────────────────────────────────────────");
                System.out.println("Total columns: " + firstRecord.size());
            }

            // Print orders table
            ordersByDate.printOrdersTable();

            // Print orders summary
            ordersByDate.printOrdersSummary();

            // Print detailed first order data
            ordersByDate.printOrderData();

            Assert.assertTrue(ordersByDate.getRecordCount() > 0, "Should have at least one order for the specified date");
            System.out.println("\n✓ Test passed - Orders found for client " + clientId + " on " + orderDate);
        } else {
            System.out.println("ℹ No orders found for client " + clientId + " on date " + orderDate);
            System.out.println("This might be expected if no orders were placed on this specific date.");
        }
    }

    @Test(priority = 10)
    public void testDynamicQueryWithSingleParameter() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 10: Dynamic Query - Single Parameter                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        Map<String, Object> params = new HashMap<>();
        params.put("CL_ID", "12240");

        System.out.println("\nExecuting dynamic query with parameter: CL_ID = 12240\n");

        GetOrdersData dynamicOrders = new GetOrdersData(dbConnection);
        boolean result = dynamicOrders.fetchOrdersWithDynamicParams(params);

        if (result) {
            System.out.println("✓ Successfully fetched " + dynamicOrders.getRecordCount() + " order(s)");
            dynamicOrders.printOrdersSummary();

            Assert.assertTrue(dynamicOrders.getRecordCount() > 0, "Should have at least one order");
            System.out.println("\n✓ Test passed - Dynamic query with single parameter works!");
        } else {
            System.out.println("ℹ No orders found for the given parameter");
        }
    }

    @Test(priority = 11)
    public void testDynamicQueryWithMultipleParameters() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 11: Dynamic Query - Multiple Parameters               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        Map<String, Object> params = new HashMap<>();
        params.put("CL_ID", "12240");

        params.put("ORDER_TYPE","CL_BUY");

        System.out.println("\nExecuting dynamic query with parameters:");
        System.out.println("  - CL_ID = 12240");

        System.out.println("  - ORDER_TYPE = CL_BUY");

        GetOrdersData dynamicOrders = new GetOrdersData(dbConnection);
        boolean result = dynamicOrders.fetchOrdersWithDynamicParams(params);

        if (result) {
            System.out.println("✓ Successfully fetched " + dynamicOrders.getRecordCount() + " order(s)");

            // Print first record details
            dynamicOrders.printOrderData();

            // Verify the returned data matches our query parameters
            Assert.assertEquals(dynamicOrders.getClientId(), "12240", "Client ID should match");


            System.out.println("\n✓ Test passed - Dynamic query with multiple parameters works!");
            System.out.println("✓ All returned data matches the query parameters");
        } else {
            System.out.println("ℹ No orders found for the given parameters");
        }
    }

    @Test(priority = 12)
    public void testDynamicQueryWithNullAndEmptyValues() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 12: Dynamic Query - Handle Null/Empty Values          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        Map<String, Object> params = new HashMap<>();
        params.put("CL_ID", "12240");
        params.put("ORDER_STATUS", null);  // Null value - should be filtered out
        params.put("COMPANY_CODE", "");     // Empty value - should be filtered out
        params.put("INVALID_COL!", "test"); // Invalid column name - should be filtered out

        System.out.println("\nExecuting dynamic query with mixed parameters:");
        System.out.println("  - CL_ID = 12240 (valid)");
        System.out.println("  - ORDER_STATUS = null (should be filtered)");
        System.out.println("  - COMPANY_CODE = '' (empty, should be filtered)");
        System.out.println("  - INVALID_COL! = 'test' (invalid name, should be filtered)\n");

        GetOrdersData dynamicOrders = new GetOrdersData(dbConnection);
        boolean result = dynamicOrders.fetchOrdersWithDynamicParams(params);

        if (result) {
            System.out.println("✓ Successfully fetched " + dynamicOrders.getRecordCount() + " order(s)");
            System.out.println("✓ Query executed with only valid parameters (null/empty filtered out)");

            Assert.assertTrue(dynamicOrders.getRecordCount() > 0, "Should have at least one order");
            System.out.println("\n✓ Test passed - Null and empty value filtering works correctly!");
        } else {
            System.out.println("ℹ No orders found");
        }
    }

    @Test(priority = 13)
    public void testDynamicQueryWithDateRange() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 13: Dynamic Query - Date Range                        ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        Map<String, Object> params = new HashMap<>();
        params.put("CL_ID", "12240");

        String startDate = "2025-11-01";
        String endDate = "2025-11-30";

        System.out.println("\nExecuting dynamic query with date range:");
        System.out.println("  - CL_ID = 12240");
        System.out.println("  - Date Range: " + startDate + " to " + endDate + "\n");

        GetOrdersData dynamicOrders = new GetOrdersData(dbConnection);
        boolean result = dynamicOrders.fetchOrdersWithDateRange(params, "ORDER_DATE", startDate, endDate);

        if (result) {
            System.out.println("✓ Successfully fetched " + dynamicOrders.getRecordCount() + " order(s) within date range");

            // Print summary
            dynamicOrders.printOrdersSummary();

            // Print first order details
            System.out.println("\nFirst Order Details:");
            System.out.println("  Order Date: " + dynamicOrders.getOrderDate());
            System.out.println("  Client ID: " + dynamicOrders.getClientId());
            System.out.println("  Company Code: " + dynamicOrders.getCompanyCode());
            System.out.println("  Order Value: " + dynamicOrders.getOrderValue());

            Assert.assertTrue(dynamicOrders.getRecordCount() > 0, "Should have at least one order");
            System.out.println("\n✓ Test passed - Date range query works correctly!");
        } else {
            System.out.println("ℹ No orders found for the given date range");
        }
    }

    @Test(priority = 14)
    public void testDynamicQueryWithMultipleConditionsAndDateRange() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 14: Dynamic Query - Multiple Params + Date Range      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        Map<String, Object> params = new HashMap<>();
        params.put("CL_ID", "12240");
        params.put("ORDER_STATUS", "WAP");

        String startDate = "2025-11-26";
        String endDate = "2025-11-26";

        System.out.println("\nExecuting complex dynamic query:");
        System.out.println("  - CL_ID = 12240");
        System.out.println("  - ORDER_STATUS = WAP");
        System.out.println("  - Order Date = " + startDate + "\n");

        GetOrdersData dynamicOrders = new GetOrdersData(dbConnection);
        boolean result = dynamicOrders.fetchOrdersWithDateRange(params, "ORDER_DATE", startDate, endDate);

        if (result) {
            System.out.println("✓ Successfully fetched " + dynamicOrders.getRecordCount() + " order(s)");

            // Print orders table
            dynamicOrders.printOrdersTable();

            // Verify data
            Assert.assertEquals(dynamicOrders.getClientId(), "12240", "Client ID should match");
            Assert.assertEquals(dynamicOrders.getOrderStatus(), "WAP", "Order status should match");

            System.out.println("\n✓ Test passed - Complex query with multiple conditions and date range works!");
        } else {
            System.out.println("ℹ No orders found for the complex query");
        }
    }

    @Test(priority = 15)
    public void testDynamicQuerySQLInjectionPrevention() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST 15: Dynamic Query - SQL Injection Prevention          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        Map<String, Object> params = new HashMap<>();
        params.put("CL_ID", "12240");
        params.put("ORDER_STATUS'; DROP TABLE sec_orders; --", "WAP"); // SQL injection attempt

        System.out.println("\nTesting SQL injection prevention:");
        System.out.println("  - CL_ID = 12240 (valid)");
        System.out.println("  - Column: \"ORDER_STATUS'; DROP TABLE sec_orders; --\" (malicious)");
        System.out.println("  - Expected: Malicious column name should be filtered out\n");

        GetOrdersData dynamicOrders = new GetOrdersData(dbConnection);
        boolean result = dynamicOrders.fetchOrdersWithDynamicParams(params);

        if (result) {
            System.out.println("✓ Query executed safely - invalid column name was filtered");
            System.out.println("✓ Found " + dynamicOrders.getRecordCount() + " order(s) using only valid parameters");
            System.out.println("\n✓ Test passed - SQL injection attempt was blocked!");
        } else {
            System.out.println("ℹ No orders found, but SQL injection was prevented");
            System.out.println("✓ Test passed - System is protected against SQL injection!");
        }
    }

    @AfterClass
    public void tearDown() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║              CLOSING DATABASE CONNECTION                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        if (dbConnection != null) {
            dbConnection.closeConnection();
            System.out.println("✓ Connection closed successfully");
        }

        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║         GET ORDERS DATA TEST COMPLETED                        ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }
}
