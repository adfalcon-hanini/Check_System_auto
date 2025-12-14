package com.example.screensData.clients;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Client data from SEC_CLIENTS table
 */
public class GetClientsData {

    private static final Logger logger = Logger.getLogger(GetClientsData.class);
    private OracleDBConnection dbConnection;

    // Client data fields
    private String clientId;
    private String nameArabic;
    private String nameEnglish;
    private String nationality;
    private String personalId;
    private String passportId;
    private String poBox;
    private String mobileNo;
    private String homeTelNo;
    private String workTelNo;
    private String bleepNo;
    private String address;
    private String title;
    private String email;
    private String fax;
    private String clientType;
    private String clientStatus;
    private String createdDate;
    private String modifiedDate;

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allClientsData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetClientsData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allClientsData = new ArrayList<>();
    }

    /**
     * Fetch client data by client ID
     * @param clientId Client ID to query
     * @return true if data found, false otherwise
     */
    public boolean fetchClientData(String clientId) {
        try {
            logger.info("Fetching client data for ID: " + clientId);

            String query = "SELECT * FROM SEC_CLIENTS C WHERE C.cl_id='" + clientId + "'";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allClientsData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseClientData(row);
                logger.info("Client data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No client data found for ID: " + clientId);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching client data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch client data for multiple client IDs
     * @param clientIds List of client IDs
     * @return true if data found, false otherwise
     */
    public boolean fetchClientDataForMultipleIds(List<String> clientIds) {
        try {
            logger.info("Fetching client data for multiple IDs");

            // Build IN clause
            StringBuilder idClause = new StringBuilder();
            for (int i = 0; i < clientIds.size(); i++) {
                idClause.append("'").append(clientIds.get(i)).append("'");
                if (i < clientIds.size() - 1) {
                    idClause.append(", ");
                }
            }

            String query = "SELECT * FROM SEC_CLIENTS C WHERE C.cl_id IN (" + idClause.toString() + ")";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allClientsData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseClientData(row);
                logger.info("Client data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No client data found");
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching client data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Fetch all client IDs from SEC_CLIENTS table
     * @return List of all client IDs
     */
    public List<String> fetchAllClientIds() {
        List<String> clientIds = new ArrayList<>();
        try {
            logger.info("Fetching all client IDs from SEC_CLIENTS");

            String query = "select * from sec_clients where PERSONAL_ID_EXPIRY_DATE >= TRUNC(SYSDATE) ORDER BY cl_id";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            for (Map<String, Object> row : results) {
                String clId = row.get("CL_ID") != null ? row.get("CL_ID").toString() : "";
                if (!clId.isEmpty()) {
                    clientIds.add(clId);
                }
            }

            logger.info("Successfully fetched " + clientIds.size() + " client IDs");

        } catch (SQLException e) {
            logger.error("Error fetching all client IDs: " + e.getMessage(), e);
        }

        return clientIds;
    }

    /**
     * Fetch all client IDs with optional limit
     * @param limit Maximum number of client IDs to retrieve
     * @return List of client IDs
     */
    public List<String> fetchAllClientIds(int limit) {
        List<String> clientIds = new ArrayList<>();
        try {
            logger.info("Fetching client IDs with limit: " + limit);

            String query = "select * from sec_clients where PERSONAL_ID_EXPIRY_DATE >= TRUNC(SYSDATE) ORDER BY cl_id";
            if (limit > 0) {
                query += " FETCH FIRST " + limit + " ROWS ONLY";
            }

            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            for (Map<String, Object> row : results) {
                String clId = row.get("CL_ID") != null ? row.get("CL_ID").toString() : "";
                if (!clId.isEmpty()) {
                    clientIds.add(clId);
                }
            }

            logger.info("Successfully fetched " + clientIds.size() + " client IDs");

        } catch (SQLException e) {
            logger.error("Error fetching client IDs with limit: " + e.getMessage(), e);
        }

        return clientIds;
    }

    /**
     * Get count of all clients in SEC_CLIENTS table
     * @return Total number of clients
     */
    public int getAllClientsCount() {
        try {
            logger.info("Fetching total client count");

            String query = "SELECT COUNT(*) as TOTAL_COUNT FROM SEC_CLIENTS";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                Object count = results.get(0).get("TOTAL_COUNT");
                int totalCount = count != null ? Integer.parseInt(count.toString()) : 0;
                logger.info("Total clients count: " + totalCount);
                return totalCount;
            }

        } catch (SQLException e) {
            logger.error("Error fetching total client count: " + e.getMessage(), e);
        }

        return 0;
    }

    /**
     * Parse client data from database row
     * @param row Database row containing client data
     */
    private void parseClientData(Map<String, Object> row) {
        this.clientId = row.get("CL_ID") != null ? row.get("CL_ID").toString() : "";
        this.nameArabic = row.get("NAME_ARB") != null ? row.get("NAME_ARB").toString() : "";
        this.nameEnglish = row.get("NAME_ENG") != null ? row.get("NAME_ENG").toString() : "";
        this.nationality = row.get("NATIONALITY") != null ? row.get("NATIONALITY").toString() : "";
        this.personalId = row.get("PERSONAL_ID") != null ? row.get("PERSONAL_ID").toString() : "";
        this.passportId = row.get("PASPORT_ID") != null ? row.get("PASPORT_ID").toString() : "";
        this.poBox = row.get("P_O_BOX") != null ? row.get("P_O_BOX").toString() : "";
        this.mobileNo = row.get("MOBILE_NO") != null ? row.get("MOBILE_NO").toString() : "";
        this.homeTelNo = row.get("HOME_TEL_NO") != null ? row.get("HOME_TEL_NO").toString() : "";
        this.workTelNo = row.get("WORK_TEL_NO") != null ? row.get("WORK_TEL_NO").toString() : "";
        this.bleepNo = row.get("BLEEP_NO") != null ? row.get("BLEEP_NO").toString() : "";
        this.address = row.get("ADDRESS") != null ? row.get("ADDRESS").toString() : "";
        this.title = row.get("TITLE") != null ? row.get("TITLE").toString() : "";
        this.email = row.get("EMAIL") != null ? row.get("EMAIL").toString() : "";
        this.fax = row.get("FAX") != null ? row.get("FAX").toString() : "";
        this.clientType = row.get("CLIENT_TYPE") != null ? row.get("CLIENT_TYPE").toString() : "";
        this.clientStatus = row.get("CLIENT_STATUS") != null ? row.get("CLIENT_STATUS").toString() : "";
        this.createdDate = row.get("CREATED_DATE") != null ? row.get("CREATED_DATE").toString() : "";
        this.modifiedDate = row.get("MODIFIED_DATE") != null ? row.get("MODIFIED_DATE").toString() : "";
    }

    /**
     * Get all client data as a map (first row)
     * @return Map containing all client fields
     */
    public Map<String, String> getAllData() {
        Map<String, String> data = new HashMap<>();
        data.put("clientId", this.clientId);
        data.put("nameArabic", this.nameArabic);
        data.put("nameEnglish", this.nameEnglish);
        data.put("nationality", this.nationality);
        data.put("personalId", this.personalId);
        data.put("passportId", this.passportId);
        data.put("poBox", this.poBox);
        data.put("mobileNo", this.mobileNo);
        data.put("homeTelNo", this.homeTelNo);
        data.put("workTelNo", this.workTelNo);
        data.put("bleepNo", this.bleepNo);
        data.put("address", this.address);
        data.put("title", this.title);
        data.put("email", this.email);
        data.put("fax", this.fax);
        data.put("clientType", this.clientType);
        data.put("clientStatus", this.clientStatus);
        data.put("createdDate", this.createdDate);
        data.put("modifiedDate", this.modifiedDate);
        return data;
    }

    /**
     * Get all client records
     * @return List of all client data rows
     */
    public List<Map<String, Object>> getAllClientRecords() {
        return allClientsData;
    }

    /**
     * Get client record by index
     * @param index Index of the record
     * @return Map containing client data for that index
     */
    public Map<String, Object> getClientRecordByIndex(int index) {
        if (index >= 0 && index < allClientsData.size()) {
            return allClientsData.get(index);
        }
        return null;
    }

    /**
     * Get total number of client records
     * @return Number of records
     */
    public int getRecordCount() {
        return allClientsData.size();
    }

    /**
     * Print client data to console (first record)
     */
    public void printClientData() {
        System.out.println("\n========== Client Data ==========");
        System.out.println("Client ID: " + clientId);
        System.out.println("Arabic Name: " + nameArabic);
        System.out.println("English Name: " + nameEnglish);
        System.out.println("Nationality: " + nationality);
        System.out.println("Personal ID: " + personalId);
        System.out.println("Passport ID: " + passportId);
        System.out.println("P.O. Box: " + poBox);
        System.out.println("Mobile Number: " + mobileNo);
        System.out.println("Home Telephone: " + homeTelNo);
        System.out.println("Work Telephone: " + workTelNo);
        System.out.println("Bleep Number: " + bleepNo);
        System.out.println("Address: " + address);
        System.out.println("Title: " + title);
        System.out.println("Email: " + email);
        System.out.println("Fax: " + fax);
        System.out.println("Client Type: " + clientType);
        System.out.println("Client Status: " + clientStatus);
        System.out.println("Created Date: " + createdDate);
        System.out.println("Modified Date: " + modifiedDate);
        System.out.println("==================================");
    }

    /**
     * Print all client records
     */
    public void printAllClientData() {
        System.out.println("\n========== All Client Records (" + allClientsData.size() + ") ==========");
        for (int i = 0; i < allClientsData.size(); i++) {
            System.out.println("\n--- Record " + (i + 1) + " ---");
            Map<String, Object> record = allClientsData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("=========================================================");
    }

    // Getters
    public String getClientId() {
        return clientId;
    }

    public String getNameArabic() {
        return nameArabic;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPersonalId() {
        return personalId;
    }

    public String getPassportId() {
        return passportId;
    }

    public String getPoBox() {
        return poBox;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getHomeTelNo() {
        return homeTelNo;
    }

    public String getWorkTelNo() {
        return workTelNo;
    }

    public String getBleepNo() {
        return bleepNo;
    }

    public String getAddress() {
        return address;
    }

    public String getTitle() {
        return title;
    }

    public String getEmail() {
        return email;
    }

    public String getFax() {
        return fax;
    }

    public String getClientType() {
        return clientType;
    }

    public String getClientStatus() {
        return clientStatus;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

}
