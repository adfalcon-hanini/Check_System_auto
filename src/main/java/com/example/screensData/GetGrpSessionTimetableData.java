package com.example.screensData;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Group Session Timetable data from XDP_GRP_SESSION_TIMETABLE table
 */
public class GetGrpSessionTimetableData {

    private static final Logger logger = Logger.getLogger(GetGrpSessionTimetableData.class);
    private OracleDBConnection dbConnection;

    // XDP_GRP_SESSION_TIMETABLE column fields
    private String groupCode;  // VARCHAR2
    private String sessionType;  // VARCHAR2
    private String propenTime1;  // VARCHAR2
    private String openTime1;  // VARCHAR2
    private String closeTime1;  // VARCHAR2
    private String propenTime2;  // VARCHAR2
    private String openTime2;  // VARCHAR2
    private String closeTime2;  // VARCHAR2
    private String propenTime3;  // VARCHAR2
    private String openTime3;  // VARCHAR2
    private String closeTime3;  // VARCHAR2
    private String eodTime;  // VARCHAR2
    private String dateoflastupdate;  // DATE

    private List<Map<String, Object>> allGrpSessionTimetableData;

    public GetGrpSessionTimetableData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allGrpSessionTimetableData = new ArrayList<>();
    }

    public boolean fetchAllGrpSessionTimetable() {
        try {
            logger.info("Fetching all group session timetable data from XDP_GRP_SESSION_TIMETABLE");
            String query = "SELECT * FROM XDP_GRP_SESSION_TIMETABLE";
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allGrpSessionTimetableData = results;
                logger.info("Group session timetable data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No group session timetable data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching group session timetable data: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean fetchGrpSessionTimetableByCondition(String whereClause) {
        try {
            logger.info("Fetching group session timetable data with condition: " + whereClause);
            String query = "SELECT * FROM XDP_GRP_SESSION_TIMETABLE WHERE " + whereClause;
            List<Map<String, Object>> results = dbConnection.executeQuery(query);

            if (!results.isEmpty()) {
                allGrpSessionTimetableData = results;
                logger.info("Group session timetable data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No group session timetable data found");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error fetching group session timetable data: " + e.getMessage(), e);
            return false;
        }
    }

    public List<Map<String, Object>> getAllGrpSessionTimetableRecords() {
        return allGrpSessionTimetableData;
    }

    public Map<String, Object> getGrpSessionTimetableRecordByIndex(int index) {
        if (index >= 0 && index < allGrpSessionTimetableData.size()) {
            return allGrpSessionTimetableData.get(index);
        }
        return null;
    }

    public int getRecordCount() {
        return allGrpSessionTimetableData.size();
    }

    /**
     * Parse a single row from database and populate all fields
     */
    private void parseData(Map<String, Object> row) {
        this.groupCode = row.get("GROUP_CODE") != null ? row.get("GROUP_CODE").toString() : "";
        this.sessionType = row.get("SESSION_TYPE") != null ? row.get("SESSION_TYPE").toString() : "";
        this.propenTime1 = row.get("PROPEN_TIME1") != null ? row.get("PROPEN_TIME1").toString() : "";
        this.openTime1 = row.get("OPEN_TIME1") != null ? row.get("OPEN_TIME1").toString() : "";
        this.closeTime1 = row.get("CLOSE_TIME1") != null ? row.get("CLOSE_TIME1").toString() : "";
        this.propenTime2 = row.get("PROPEN_TIME2") != null ? row.get("PROPEN_TIME2").toString() : "";
        this.openTime2 = row.get("OPEN_TIME2") != null ? row.get("OPEN_TIME2").toString() : "";
        this.closeTime2 = row.get("CLOSE_TIME2") != null ? row.get("CLOSE_TIME2").toString() : "";
        this.propenTime3 = row.get("PROPEN_TIME3") != null ? row.get("PROPEN_TIME3").toString() : "";
        this.openTime3 = row.get("OPEN_TIME3") != null ? row.get("OPEN_TIME3").toString() : "";
        this.closeTime3 = row.get("CLOSE_TIME3") != null ? row.get("CLOSE_TIME3").toString() : "";
        this.eodTime = row.get("EOD_TIME") != null ? row.get("EOD_TIME").toString() : "";
        this.dateoflastupdate = row.get("DATEOFLASTUPDATE") != null ? row.get("DATEOFLASTUPDATE").toString() : "";
    }

    // Getters and Setters
    public String getGroupCode() {
        return groupCode;
    }
    public String getSessionType() {
        return sessionType;
    }
    public String getPropenTime1() {
        return propenTime1;
    }
    public String getOpenTime1() {
        return openTime1;
    }
    public String getCloseTime1() {
        return closeTime1;
    }
    public String getPropenTime2() {
        return propenTime2;
    }
    public String getOpenTime2() {
        return openTime2;
    }
    public String getCloseTime2() {
        return closeTime2;
    }
    public String getPropenTime3() {
        return propenTime3;
    }
    public String getOpenTime3() {
        return openTime3;
    }
    public String getCloseTime3() {
        return closeTime3;
    }
    public String getEodTime() {
        return eodTime;
    }
    public String getDateoflastupdate() {
        return dateoflastupdate;
    }
    public void printAllGrpSessionTimetableData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║     All Grp Session Timetable Records (" + allGrpSessionTimetableData.size() + " total)      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allGrpSessionTimetableData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allGrpSessionTimetableData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }
}
