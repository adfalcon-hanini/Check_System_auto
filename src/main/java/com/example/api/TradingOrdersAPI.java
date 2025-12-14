package com.example.api;

import io.qameta.allure.Step;

/**
 * API Client for Trading Orders (Normal and Synthetic)
 */
public class TradingOrdersAPI extends BaseAPIClient {

    private static final String ORDERS_ENDPOINT = "/api/trading/orders";
    private static final String NORMAL_ORDERS_ENDPOINT = "/api/trading/orders/normal";
    private static final String SYNTHETIC_ORDERS_ENDPOINT = "/api/trading/orders/synthetic";

    public TradingOrdersAPI(String baseUrl) {
        super(baseUrl);
    }

    // ==================== Normal Orders ====================

    @Step("Place normal BUY order")
    public String placeNormalBuyOrder(String orderJson) throws Exception {
        String response = executePost(NORMAL_ORDERS_ENDPOINT + "/buy", orderJson);
        logger.info("Normal BUY order placed");
        return response;
    }

    @Step("Place normal SELL order")
    public String placeNormalSellOrder(String orderJson) throws Exception {
        String response = executePost(NORMAL_ORDERS_ENDPOINT + "/sell", orderJson);
        logger.info("Normal SELL order placed");
        return response;
    }

    @Step("Get normal orders")
    public String getNormalOrders() throws Exception {
        String response = executeGet(NORMAL_ORDERS_ENDPOINT);
        logger.info("Normal orders retrieved");
        return response;
    }

    @Step("Get normal order by ID: {orderId}")
    public String getNormalOrderById(String orderId) throws Exception {
        String response = executeGet(NORMAL_ORDERS_ENDPOINT + "/" + orderId);
        logger.info("Normal order {} retrieved", orderId);
        return response;
    }

    @Step("Cancel normal order: {orderId}")
    public String cancelNormalOrder(String orderId) throws Exception {
        String response = executePost(NORMAL_ORDERS_ENDPOINT + "/" + orderId + "/cancel", "{}");
        logger.info("Normal order {} cancelled", orderId);
        return response;
    }

    @Step("Modify normal order: {orderId}")
    public String modifyNormalOrder(String orderId, String orderJson) throws Exception {
        String response = executePut(NORMAL_ORDERS_ENDPOINT + "/" + orderId, orderJson);
        logger.info("Normal order {} modified", orderId);
        return response;
    }

    // ==================== Synthetic Orders ====================

    @Step("Place synthetic BUY order")
    public String placeSyntheticBuyOrder(String orderJson) throws Exception {
        String response = executePost(SYNTHETIC_ORDERS_ENDPOINT + "/buy", orderJson);
        logger.info("Synthetic BUY order placed");
        return response;
    }

    @Step("Place synthetic SELL order")
    public String placeSyntheticSellOrder(String orderJson) throws Exception {
        String response = executePost(SYNTHETIC_ORDERS_ENDPOINT + "/sell", orderJson);
        logger.info("Synthetic SELL order placed");
        return response;
    }

    @Step("Get synthetic orders")
    public String getSyntheticOrders() throws Exception {
        String response = executeGet(SYNTHETIC_ORDERS_ENDPOINT);
        logger.info("Synthetic orders retrieved");
        return response;
    }

    @Step("Get synthetic order by ID: {orderId}")
    public String getSyntheticOrderById(String orderId) throws Exception {
        String response = executeGet(SYNTHETIC_ORDERS_ENDPOINT + "/" + orderId);
        logger.info("Synthetic order {} retrieved", orderId);
        return response;
    }

    @Step("Cancel synthetic order: {orderId}")
    public String cancelSyntheticOrder(String orderId) throws Exception {
        String response = executePost(SYNTHETIC_ORDERS_ENDPOINT + "/" + orderId + "/cancel", "{}");
        logger.info("Synthetic order {} cancelled", orderId);
        return response;
    }

    // ==================== Common Operations ====================

    @Step("Get order history for NIN: {nin}")
    public String getOrderHistory(String nin, String startDate, String endDate) throws Exception {
        String endpoint = String.format("%s/history?nin=%s&startDate=%s&endDate=%s",
            ORDERS_ENDPOINT, nin, startDate, endDate);
        String response = executeGet(endpoint);
        logger.info("Order history retrieved for NIN: {}", nin);
        return response;
    }

    @Step("Get active orders for NIN: {nin}")
    public String getActiveOrders(String nin) throws Exception {
        String response = executeGet(ORDERS_ENDPOINT + "/active?nin=" + nin);
        logger.info("Active orders retrieved for NIN: {}", nin);
        return response;
    }

    @Step("Get order status: {orderId}")
    public String getOrderStatus(String orderId) throws Exception {
        String response = executeGet(ORDERS_ENDPOINT + "/" + orderId + "/status");
        logger.info("Order {} status retrieved", orderId);
        return response;
    }
}
