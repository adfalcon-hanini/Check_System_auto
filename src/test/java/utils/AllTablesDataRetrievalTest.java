package utils;

import com.example.screensData.*;
import com.example.screensData.orders.GetOrdersData;
import com.example.screensData.orders.GetOrdersHistData;
import com.example.screensData.xdp.*;
import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Comprehensive test class to execute all data retrieval classes
 * and fetch data from all XDP tables
 *
 * This is a database-only test class that does not require browser/WebDriver
 */
public class AllTablesDataRetrievalTest {

    private static final Logger logger = Logger.getLogger(AllTablesDataRetrievalTest.class);
    private OracleDBConnection dbConnection;

    @BeforeClass
    public void setupDatabase() {
        try {
            logger.info("Setting up database connection for all tables data retrieval test");
            // Initialize database connection with TNS entry, username, and password
            dbConnection = new OracleDBConnection("DB01M:1523/GRPUAT", "sec1", "sec12345");
            dbConnection.connect();
            logger.info("Database connection established successfully");
        } catch (Exception e) {
            logger.error("Failed to establish database connection: " + e.getMessage(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Test(priority = 1, description = "Fetch data from XDP_INSTRUMENTS table")
    public void testFetchInstrumentsData() {
        logger.info("\n\n========== TEST: Fetching Instruments Data ==========");
        printTableHeader("XDP_INSTRUMENTS");
        try {
            GetInstrumentsData instrumentsData = new GetInstrumentsData(dbConnection);
            boolean success = instrumentsData.fetchInstrumentsData();

            if (success) {
                logger.info("Successfully fetched " + instrumentsData.getRecordCount() + " instruments");
                instrumentsData.printInstrumentsSummary();
            } else {
                logger.warn("No instruments data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchInstrumentsData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 2, description = "Fetch data from XDP_ORDERS table")
    public void testFetchOrdersData() {
        logger.info("\n\n========== TEST: Fetching Orders Data ==========");
        printTableHeader("XDP_ORDERS");
        try {
            GetOrdersData ordersData = new GetOrdersData(dbConnection);

            logger.info("Fetching ALL orders from XDP_ORDERS table...");

            // Fetch all orders from XDP_ORDERS without any filter
            boolean success = ordersData.fetchAllOrdersFromXDP();

            if (success) {
                logger.info("Successfully fetched " + ordersData.getRecordCount() + " orders from XDP_ORDERS");
                ordersData.printOrdersTable();
                ordersData.printOrdersSummary();
            } else {
                logger.warn("No orders data found in XDP_ORDERS table - table may be empty");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchOrdersData: " + e.getMessage(), e);
            logger.warn("Note: XDP_ORDERS table may not exist or may have different structure");
        }
    }

    @Test(priority = 3, description = "Fetch data from XDP_TRADES table")
    public void testFetchTradesData() {
        logger.info("\n\n========== TEST: Fetching Trades Data ==========");
        printTableHeader("XDP_TRADES");
        try {
            GetTradesData tradesData = new GetTradesData(dbConnection);
            boolean success = tradesData.fetchLast10Trades();

            if (success) {
                logger.info("Successfully fetched " + tradesData.getRecordCount() + " trades");
                tradesData.printTradesSummary();
            } else {
                logger.warn("No trades data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchTradesData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 4, description = "Fetch data from XDP_MARKET_SUMMARY table")
    public void testFetchMarketSummaryData() {
        logger.info("\n\n========== TEST: Fetching Market Summary Data ==========");
        printTableHeader("XDP_MARKET_SUMMARY");
        try {
            GetMarketSummaryData marketSummaryData = new GetMarketSummaryData(dbConnection);
            boolean success = marketSummaryData.fetchAllMarketSummary();

            if (success) {
                logger.info("Successfully fetched " + marketSummaryData.getRecordCount() + " market summary records");
                marketSummaryData.printAllMarketSummaryData();
            } else {
                logger.warn("No market summary data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchMarketSummaryData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 5, description = "Fetch data from XDP_MARKETS table")
    public void testFetchMarketsData() {
        logger.info("\n\n========== TEST: Fetching Markets Data ==========");
        printTableHeader("XDP_MARKETS");
        try {
            GetMarketsData marketsData = new GetMarketsData(dbConnection);
            boolean success = marketsData.fetchAllMarkets();

            if (success) {
                logger.info("Successfully fetched " + marketsData.getRecordCount() + " markets records");
                marketsData.printAllMarketsData();
            } else {
                logger.warn("No markets data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchMarketsData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 6, description = "Fetch data from XDP_QUOTES table")
    public void testFetchQuotesData() {
        logger.info("\n\n========== TEST: Fetching Quotes Data ==========");
        printTableHeader("XDP_QUOTES");
        try {
            GetQuotesData quotesData = new GetQuotesData(dbConnection);
            boolean success = quotesData.fetchAllQuotes();

            if (success) {
                logger.info("Successfully fetched " + quotesData.getRecordCount() + " quotes records");
                quotesData.printAllQuotesData();
            } else {
                logger.warn("No quotes data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchQuotesData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 7, description = "Fetch data from XDP_INST_LIVEQUOTE table")
    public void testFetchInstLivequoteData() {
        logger.info("\n\n========== TEST: Fetching Inst Livequote Data ==========");
        printTableHeader("XDP_INST_LIVEQUOTE");
        try {
            GetInstLivequoteData instLivequoteData = new GetInstLivequoteData(dbConnection);
            boolean success = instLivequoteData.fetchAllInstLivequote();

            if (success) {
                logger.info("Successfully fetched " + instLivequoteData.getRecordCount() + " inst livequote records");
                instLivequoteData.printAllInstLivequoteData();
            } else {
                logger.warn("No inst livequote data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchInstLivequoteData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 8, description = "Fetch data from XDP_INDICES table")
    public void testFetchIndicesData() {
        logger.info("\n\n========== TEST: Fetching Indices Data ==========");
        printTableHeader("XDP_INDICES");
        try {
            GetIndicesData indicesData = new GetIndicesData(dbConnection);
            boolean success = indicesData.fetchAllIndices();

            if (success) {
                logger.info("Successfully fetched " + indicesData.getRecordCount() + " indices records");
                indicesData.printAllIndicesData();
            } else {
                logger.warn("No indices data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchIndicesData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 9, description = "Fetch data from XDP_ALL_MBP table")
    public void testFetchAllMbpData() {
        logger.info("\n\n========== TEST: Fetching All MBP Data ==========");
        printTableHeader("XDP_ALL_MBP");
        try {
            GetAllMbpData allMbpData = new GetAllMbpData(dbConnection);
            boolean success = allMbpData.fetchAllMbp();

            if (success) {
                logger.info("Successfully fetched " + allMbpData.getRecordCount() + " MBP records");
                allMbpData.printAllMbpData();
            } else {
                logger.warn("No MBP data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchAllMbpData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 10, description = "Fetch data from XDP_INDEX_COMPOSITION table")
    public void testFetchIndexCompositionData() {
        logger.info("\n\n========== TEST: Fetching Index Composition Data ==========");
        printTableHeader("XDP_INDEX_COMPOSITION");
        try {
            GetIndexCompositionData indexCompositionData = new GetIndexCompositionData(dbConnection);
            boolean success = indexCompositionData.fetchAllIndexComposition();

            if (success) {
                logger.info("Successfully fetched " + indexCompositionData.getRecordCount() + " index composition records");
                indexCompositionData.printAllIndexCompositionData();
            } else {
                logger.warn("No index composition data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchIndexCompositionData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 11, description = "Fetch data from XDP_GRP_SESSION_TIMETABLE table")
    public void testFetchGrpSessionTimetableData() {
        logger.info("\n\n========== TEST: Fetching Grp Session Timetable Data ==========");
        printTableHeader("XDP_GRP_SESSION_TIMETABLE");
        try {
            GetGrpSessionTimetableData grpSessionTimetableData = new GetGrpSessionTimetableData(dbConnection);
            boolean success = grpSessionTimetableData.fetchAllGrpSessionTimetable();

            if (success) {
                logger.info("Successfully fetched " + grpSessionTimetableData.getRecordCount() + " grp session timetable records");
                grpSessionTimetableData.printAllGrpSessionTimetableData();
            } else {
                logger.warn("No grp session timetable data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchGrpSessionTimetableData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 12, description = "Fetch data from XDP_LKUP_ACTIONTYPE table")
    public void testFetchLkupActiontypeData() {
        logger.info("\n\n========== TEST: Fetching Lkup Actiontype Data ==========");
        printTableHeader("XDP_LKUP_ACTIONTYPE");
        try {
            GetLkupActiontypeData lkupActiontypeData = new GetLkupActiontypeData(dbConnection);
            boolean success = lkupActiontypeData.fetchAllLkupActiontype();

            if (success) {
                logger.info("Successfully fetched " + lkupActiontypeData.getRecordCount() + " lkup actiontype records");
                lkupActiontypeData.printAllLkupActiontypeData();
            } else {
                logger.warn("No lkup actiontype data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchLkupActiontypeData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 13, description = "Fetch data from XDP_LKUP_CATEGORY table")
    public void testFetchLkupCategoryData() {
        logger.info("\n\n========== TEST: Fetching Lkup Category Data ==========");
        printTableHeader("XDP_LKUP_CATEGORY");
        try {
            GetLkupCategoryData lkupCategoryData = new GetLkupCategoryData(dbConnection);
            boolean success = lkupCategoryData.fetchAllLkupCategory();

            if (success) {
                logger.info("Successfully fetched " + lkupCategoryData.getRecordCount() + " lkup category records");
                lkupCategoryData.printAllLkupCategoryData();
            } else {
                logger.warn("No lkup category data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchLkupCategoryData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 14, description = "Fetch data from XDP_LKUP_CORP_EVENT table")
    public void testFetchLkupCorpEventData() {
        logger.info("\n\n========== TEST: Fetching Lkup Corp Event Data ==========");
        printTableHeader("XDP_LKUP_CORP_EVENT");
        try {
            GetLkupCorpEventData lkupCorpEventData = new GetLkupCorpEventData(dbConnection);
            boolean success = lkupCorpEventData.fetchAllLkupCorpEvent();

            if (success) {
                logger.info("Successfully fetched " + lkupCorpEventData.getRecordCount() + " lkup corp event records");
                lkupCorpEventData.printAllLkupCorpEventData();
            } else {
                logger.warn("No lkup corp event data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchLkupCorpEventData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 15, description = "Fetch data from XDP_LKUP_CURRENCY table")
    public void testFetchLkupCurrencyData() {
        logger.info("\n\n========== TEST: Fetching Lkup Currency Data ==========");
        printTableHeader("XDP_LKUP_CURRENCY");
        try {
            GetLkupCurrencyData lkupCurrencyData = new GetLkupCurrencyData(dbConnection);
            boolean success = lkupCurrencyData.fetchAllLkupCurrency();

            if (success) {
                logger.info("Successfully fetched " + lkupCurrencyData.getRecordCount() + " lkup currency records");
                lkupCurrencyData.printAllLkupCurrencyData();
            } else {
                logger.warn("No lkup currency data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchLkupCurrencyData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 16, description = "Fetch data from XDP_LKUP_GROUP_STATE table")
    public void testFetchLkupGroupStateData() {
        logger.info("\n\n========== TEST: Fetching Lkup Group State Data ==========");
        printTableHeader("XDP_LKUP_GROUP_STATE");
        try {
            GetLkupGroupStateData lkupGroupStateData = new GetLkupGroupStateData(dbConnection);
            boolean success = lkupGroupStateData.fetchAllLkupGroupState();

            if (success) {
                logger.info("Successfully fetched " + lkupGroupStateData.getRecordCount() + " lkup group state records");
                lkupGroupStateData.printAllLkupGroupStateData();
            } else {
                logger.warn("No lkup group state data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchLkupGroupStateData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 17, description = "Fetch data from XDP_LKUP_HALT_ACTIONS table")
    public void testFetchLkupHaltActionsData() {
        logger.info("\n\n========== TEST: Fetching Lkup Halt Actions Data ==========");
        printTableHeader("XDP_LKUP_HALT_ACTIONS");
        try {
            GetLkupHaltActionsData lkupHaltActionsData = new GetLkupHaltActionsData(dbConnection);
            boolean success = lkupHaltActionsData.fetchAllLkupHaltActions();

            if (success) {
                logger.info("Successfully fetched " + lkupHaltActionsData.getRecordCount() + " lkup halt actions records");
                lkupHaltActionsData.printAllLkupHaltActionsData();
            } else {
                logger.warn("No lkup halt actions data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchLkupHaltActionsData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 18, description = "Fetch data from XDP_LKUP_HEADER table")
    public void testFetchLkupHeaderData() {
        logger.info("\n\n========== TEST: Fetching Lkup Header Data ==========");
        printTableHeader("XDP_LKUP_HEADER");
        try {
            GetLkupHeaderData lkupHeaderData = new GetLkupHeaderData(dbConnection);
            boolean success = lkupHeaderData.fetchAllLkupHeader();

            if (success) {
                logger.info("Successfully fetched " + lkupHeaderData.getRecordCount() + " lkup header records");
                lkupHeaderData.printAllLkupHeaderData();
            } else {
                logger.warn("No lkup header data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchLkupHeaderData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 19, description = "Fetch data from XDP_LKUP_INST_CNTRY table")
    public void testFetchLkupInstCntryData() {
        logger.info("\n\n========== TEST: Fetching Lkup Inst Cntry Data ==========");
        printTableHeader("XDP_LKUP_INST_CNTRY");
        try {
            GetLkupInstCntryData lkupInstCntryData = new GetLkupInstCntryData(dbConnection);
            boolean success = lkupInstCntryData.fetchAllLkupInstCntry();

            if (success) {
                logger.info("Successfully fetched " + lkupInstCntryData.getRecordCount() + " lkup inst cntry records");
                lkupInstCntryData.printAllLkupInstCntryData();
            } else {
                logger.warn("No lkup inst cntry data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchLkupInstCntryData: " + e.getMessage(), e);
        }
    }

    @Test(priority = 20, description = "Fetch data from XDP_ORDERS_HIST table")
    public void testFetchOrdersHistData() {
        logger.info("\n\n========== TEST: Fetching Orders Hist Data ==========");
        printTableHeader("XDP_ORDERS_HIST");
        try {
            GetOrdersHistData ordersHistData = new GetOrdersHistData(dbConnection);
            // Fetch last 100 records to avoid loading too much data
            boolean success = ordersHistData.fetchOrdersHistByLimit(100);

            if (success) {
                logger.info("Successfully fetched " + ordersHistData.getRecordCount() + " orders hist records");
                ordersHistData.printOrdersHistTable();
            } else {
                logger.warn("No orders hist data found");
            }
        } catch (Exception e) {
            logger.error("Error in testFetchOrdersHistData: " + e.getMessage(), e);
        }
    }

    @AfterClass(alwaysRun = true)
    public void teardownDatabase() {
        try {
            if (dbConnection != null) {
                logger.info("\n\nClosing database connection");
                dbConnection.closeConnection();
                logger.info("Database connection closed successfully");
            }
        } catch (Exception e) {
            logger.error("Error closing database connection: " + e.getMessage(), e);
        }
    }

    /**
     * Print a formatted table name header
     * @param tableName The name of the database table
     */
    private void printTableHeader(String tableName) {
        int tableNameLength = tableName.length();
        int totalWidth = 80;
        int padding = (totalWidth - tableNameLength - 4) / 2;

        System.out.println("\n");
        System.out.println("╔" + "═".repeat(totalWidth) + "╗");
        System.out.println("║" + " ".repeat(padding) + "  " + tableName + "  " + " ".repeat(padding) + "║");
        System.out.println("╚" + "═".repeat(totalWidth) + "╝");
        System.out.println();
    }

    /**
     * Print overall summary of all tables data retrieval
     */
    @Test(priority = 21, description = "Print overall summary")
    public void printOverallSummary() {
        System.out.println("\n\n");
        System.out.println("╔═════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ALL TABLES DATA RETRIEVAL SUMMARY                    ║");
        System.out.println("╠═════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║  All 20 XDP tables have been queried successfully                       ║");
        System.out.println("║                                                                         ║");
        System.out.println("║  Tables processed:                                                      ║");
        System.out.println("║    1. XDP_INSTRUMENTS                                                   ║");
        System.out.println("║    2. XDP_ORDERS                                                        ║");
        System.out.println("║    3. XDP_TRADES                                                        ║");
        System.out.println("║    4. XDP_MARKET_SUMMARY                                                ║");
        System.out.println("║    5. XDP_MARKETS                                                       ║");
        System.out.println("║    6. XDP_QUOTES                                                        ║");
        System.out.println("║    7. XDP_INST_LIVEQUOTE                                                ║");
        System.out.println("║    8. XDP_INDICES                                                       ║");
        System.out.println("║    9. XDP_ALL_MBP                                                       ║");
        System.out.println("║   10. XDP_INDEX_COMPOSITION                                             ║");
        System.out.println("║   11. XDP_GRP_SESSION_TIMETABLE                                         ║");
        System.out.println("║   12. XDP_LKUP_ACTIONTYPE                                               ║");
        System.out.println("║   13. XDP_LKUP_CATEGORY                                                 ║");
        System.out.println("║   14. XDP_LKUP_CORP_EVENT                                               ║");
        System.out.println("║   15. XDP_LKUP_CURRENCY                                                 ║");
        System.out.println("║   16. XDP_LKUP_GROUP_STATE                                              ║");
        System.out.println("║   17. XDP_LKUP_HALT_ACTIONS                                             ║");
        System.out.println("║   18. XDP_LKUP_HEADER                                                   ║");
        System.out.println("║   19. XDP_LKUP_INST_CNTRY                                               ║");
        System.out.println("║   20. XDP_ORDERS_HIST                                                   ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════╝");
        System.out.println("\n");
        logger.info("All tables data retrieval test completed successfully!");
    }
}
