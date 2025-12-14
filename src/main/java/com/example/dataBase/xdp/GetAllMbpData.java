package com.example.dataBase.xdp;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store All MBP data from XDP_ALL_MBP table
 */
public class GetAllMbpData {

    private static final Logger logger = Logger.getLogger(GetAllMbpData.class);
    private OracleDBConnection dbConnection;

    // XDP_ALL_MBP column fields
    private String company;  // VARCHAR2
    private String instCode;  // VARCHAR2
    private String side;  // VARCHAR2
    private String orderTime;  // TIMESTAMP(6)
    private String xdpMsgSeq;  // VARCHAR2
    private String dataXml;  // CLOB
    private String createdBy;  // VARCHAR2
    private String creationDate;  // DATE
    private String createdByIpAddress;  // VARCHAR2
    private String createdByPcName;  // VARCHAR2
    private String userId;  // VARCHAR2
    private String timeStamp;  // DATE
    private String userIdIpAddress;  // VARCHAR2
    private String userIdPcName;  // VARCHAR2

    private List<Map<String, Object>> allMbpData;

    public GetAllMbpData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allMbpData = new ArrayList<>();
    }

    public boolean fetchAllMbp() {
        try {
            logger.info("Fetching all MBP data from XDP_ALL_MBP");
            String query = "SELECT * FROM XDP_ALL_MBP";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allMbpData = results;
                logger.info("All MBP data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No MBP data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching MBP data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchAllMbpByCondition(String whereClause) {
        try {
            logger.info("Fetching MBP data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_ALL_MBP WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allMbpData = results;
                logger.info("MBP data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No MBP data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching MBP data: " + e.getMessage(), e);
            return false;
        }
    }

    public List<Map<String, Object>> getAllMbpRecords() {
        return allMbpData;
    }

    public Map<String, Object> getMbpRecordByIndex(int index) {
        if (index >= 0 && index < allMbpData.size()) {
            return allMbpData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allMbpData.size();
    }

    /**
     * Parse a single row from database and populate all fields
     */
    private void parseData(Map<String, Object> row) {
        this.company = row.get("COMPANY") != null ? row.get("COMPANY").toString() : "";
        this.instCode = row.get("INST_CODE") != null ? row.get("INST_CODE").toString() : "";
        this.side = row.get("SIDE") != null ? row.get("SIDE").toString() : "";
        this.orderTime = row.get("ORDER_TIME") != null ? row.get("ORDER_TIME").toString() : "";
        this.xdpMsgSeq = row.get("XDP_MSG_SEQ") != null ? row.get("XDP_MSG_SEQ").toString() : "";
        this.dataXml = row.get("DATA_XML") != null ? row.get("DATA_XML").toString() : "";
        this.createdBy = row.get("CREATED_BY") != null ? row.get("CREATED_BY").toString() : "";
        this.creationDate = row.get("CREATION_DATE") != null ? row.get("CREATION_DATE").toString() : "";
        this.createdByIpAddress = row.get("CREATED_BY_IP_ADDRESS") != null ? row.get("CREATED_BY_IP_ADDRESS").toString() : "";
        this.createdByPcName = row.get("CREATED_BY_PC_NAME") != null ? row.get("CREATED_BY_PC_NAME").toString() : "";
        this.userId = row.get("USER_ID") != null ? row.get("USER_ID").toString() : "";
        this.timeStamp = row.get("TIME_STAMP") != null ? row.get("TIME_STAMP").toString() : "";
        this.userIdIpAddress = row.get("USER_ID_IP_ADDRESS") != null ? row.get("USER_ID_IP_ADDRESS").toString() : "";
        this.userIdPcName = row.get("USER_ID_PC_NAME") != null ? row.get("USER_ID_PC_NAME").toString() : "";
    }

    // Getters and Setters
    public String getCompany() { return company; }
    public String getInstCode() { return instCode; }
    public String getSide() { return side; }
    public String getOrderTime() { return orderTime; }
    public String getXdpMsgSeq() { return xdpMsgSeq; }
    public String getDataXml() { return dataXml; }
    public String getCreatedBy() { return createdBy; }
    public String getCreationDate() { return creationDate; }
    public String getCreatedByIpAddress() { return createdByIpAddress; }
    public String getCreatedByPcName() { return createdByPcName; }
    public String getUserId() { return userId; }
    public String getTimeStamp() { return timeStamp; }
    public String getUserIdIpAddress() { return userIdIpAddress; }
    public String getUserIdPcName() { return userIdPcName; }

    public void printAllMbpData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              All MBP Records (" + allMbpData.size() + " total)                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allMbpData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allMbpData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }
}
