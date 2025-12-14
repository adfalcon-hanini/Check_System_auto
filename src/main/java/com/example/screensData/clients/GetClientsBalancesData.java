package com.example.screensData.clients;

import com.example.utils.OracleDBConnection;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to retrieve and store Clients Balances data from SEC_CLIENTS_BALANCES table
 */
public class GetClientsBalancesData {

    private static final Logger logger = Logger.getLogger(GetClientsBalancesData.class);
    private OracleDBConnection dbConnection;

    // All 73 columns from SEC_CLIENTS_BALANCES table
    private String nin;  // VARCHAR2
    private String curBal;  // NUMBER
    private String unclearedChqBal;  // NUMBER
    private String postpondedBal;  // NUMBER
    private String createdBy;  // VARCHAR2
    private String creationDate;  // DATE
    private String userId;  // VARCHAR2
    private String timeStamp;  // DATE
    private String buyOrdinmarketBal;  // NUMBER
    private String dvpUnclearedBal;  // NUMBER
    private String t3Sell;  // NUMBER
    private String t2Sell;  // NUMBER
    private String t1Sell;  // NUMBER
    private String maturedBal;  // NUMBER
    private String collectedBalPrevious;  // NUMBER
    private String t3Buy;  // NUMBER
    private String t2Buy;  // NUMBER
    private String t1Buy;  // NUMBER
    private String t3MaturedAmount;  // NUMBER
    private String t2MaturedAmount;  // NUMBER
    private String t1MaturedAmount;  // NUMBER
    private String t0Sell;  // NUMBER
    private String t0Buy;  // NUMBER
    private String t0MaturedAmount;  // NUMBER
    private String uncollectedWithdrawalFrombnk;  // NUMBER
    private String liabCurBal;  // NUMBER
    private String liabUnclearedChqBal;  // NUMBER
    private String liabPostpondedBal;  // NUMBER
    private String liabCollectedBalPrevious;  // NUMBER
    private String liabUncolWithdrawalFrombnk;  // NUMBER
    private String liabMaturedBal;  // NUMBER
    private String t3Balance;  // NUMBER
    private String t3MaturityDate;  // DATE
    private String t2Balance;  // NUMBER
    private String t2MaturityDate;  // DATE
    private String t1Balance;  // NUMBER
    private String t1MaturityDate;  // DATE
    private String t0Balance;  // NUMBER
    private String t3UrgentDate;  // DATE
    private String t3UrgentAmount;  // NUMBER
    private String t3UrgentFees;  // NUMBER
    private String t2UrgentDate;  // DATE
    private String t2UrgentAmount;  // NUMBER
    private String t2UrgentFees;  // NUMBER
    private String t1UrgentDate;  // DATE
    private String t1UrgentAmount;  // NUMBER
    private String t1UrgentFees;  // NUMBER
    private String reservedVisaBal;  // NUMBER
    private String visaAccountIncome;  // NUMBER
    private String visaMaxDailyCashbal;  // NUMBER
    private String unclearedQpay;  // NUMBER
    private String liabUnclearedQpay;  // NUMBER
    private String reservedQpayRefund;  // NUMBER
    private String atmWithdrawDay;  // NUMBER
    private String atmWithdrawMonth;  // NUMBER
    private String atmWithdrawYear;  // NUMBER
    private String t0UrgentWithdrawal;  // NUMBER
    private String t1UrgentWithdrawal;  // NUMBER
    private String t2UrgentWithdrawal;  // NUMBER
    private String t3UrgentWithdrawal;  // NUMBER
    private String t4UrgentWithdrawal;  // NUMBER
    private String t5UrgentWithdrawal;  // NUMBER
    private String t6UrgentWithdrawal;  // NUMBER
    private String t0UrgentSwift;  // NUMBER
    private String t1UrgentSwift;  // NUMBER
    private String t2UrgentSwift;  // NUMBER
    private String t3UrgentSwift;  // NUMBER
    private String t4UrgentSwift;  // NUMBER
    private String t5UrgentSwift;  // NUMBER
    private String t6UrgentSwift;  // NUMBER
    private String totalExemptFromFees;  // NUMBER
    private String t3MaxCurBal;  // NUMBER
    private String mBalance;  // NUMBER

    // Store all rows if multiple records exist
    private List<Map<String, Object>> allData;

    /**
     * Constructor
     * @param dbConnection Database connection instance
     */
    public GetClientsBalancesData(OracleDBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.allData = new ArrayList<>();
    }

    /**
     * Fetch client balances data by NIN
     * @param nin NIN to query
     * @return true if data found, false otherwise
     */
    public boolean fetchClientBalancesByNin(String nin) {
        try {
            logger.info("Fetching client balances data for NIN: " + nin);

            String query = "SELECT * FROM SEC_CLIENTS_BALANCES WHERE NIN = ?";

            List<Map<String, Object>> results = dbConnection.executeQueryWithParams(query, nin);

            if (!results.isEmpty()) {
                allData = results;
                // Parse first row for easy access
                Map<String, Object> row = results.get(0);
                parseData(row);
                logger.info("Client balances data fetched successfully. Found " + results.size() + " record(s)");
                return true;
            } else {
                logger.warn("No client balances data found for NIN: " + nin);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error fetching client balances data: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Parse client balances data from database row - All 73 columns
     * @param row Database row containing client balances data
     */
    private void parseData(Map<String, Object> row) {
        this.nin = row.get("NIN") != null ? row.get("NIN").toString() : "";
        this.curBal = row.get("CUR_BAL") != null ? row.get("CUR_BAL").toString() : "";
        this.unclearedChqBal = row.get("UNCLEARED_CHQ_BAL") != null ? row.get("UNCLEARED_CHQ_BAL").toString() : "";
        this.postpondedBal = row.get("POSTPONDED_BAL") != null ? row.get("POSTPONDED_BAL").toString() : "";
        this.createdBy = row.get("CREATED_BY") != null ? row.get("CREATED_BY").toString() : "";
        this.creationDate = row.get("CREATION_DATE") != null ? row.get("CREATION_DATE").toString() : "";
        this.userId = row.get("USER_ID") != null ? row.get("USER_ID").toString() : "";
        this.timeStamp = row.get("TIME_STAMP") != null ? row.get("TIME_STAMP").toString() : "";
        this.buyOrdinmarketBal = row.get("BUY_ORDINMARKET_BAL") != null ? row.get("BUY_ORDINMARKET_BAL").toString() : "";
        this.dvpUnclearedBal = row.get("DVP_UNCLEARED_BAL") != null ? row.get("DVP_UNCLEARED_BAL").toString() : "";
        this.t3Sell = row.get("T3_SELL") != null ? row.get("T3_SELL").toString() : "";
        this.t2Sell = row.get("T2_SELL") != null ? row.get("T2_SELL").toString() : "";
        this.t1Sell = row.get("T1_SELL") != null ? row.get("T1_SELL").toString() : "";
        this.maturedBal = row.get("MATURED_BAL") != null ? row.get("MATURED_BAL").toString() : "";
        this.collectedBalPrevious = row.get("COLLECTED_BAL_PREVIOUS") != null ? row.get("COLLECTED_BAL_PREVIOUS").toString() : "";
        this.t3Buy = row.get("T3_BUY") != null ? row.get("T3_BUY").toString() : "";
        this.t2Buy = row.get("T2_BUY") != null ? row.get("T2_BUY").toString() : "";
        this.t1Buy = row.get("T1_BUY") != null ? row.get("T1_BUY").toString() : "";
        this.t3MaturedAmount = row.get("T3_MATURED_AMOUNT") != null ? row.get("T3_MATURED_AMOUNT").toString() : "";
        this.t2MaturedAmount = row.get("T2_MATURED_AMOUNT") != null ? row.get("T2_MATURED_AMOUNT").toString() : "";
        this.t1MaturedAmount = row.get("T1_MATURED_AMOUNT") != null ? row.get("T1_MATURED_AMOUNT").toString() : "";
        this.t0Sell = row.get("T0_SELL") != null ? row.get("T0_SELL").toString() : "";
        this.t0Buy = row.get("T0_BUY") != null ? row.get("T0_BUY").toString() : "";
        this.t0MaturedAmount = row.get("T0_MATURED_AMOUNT") != null ? row.get("T0_MATURED_AMOUNT").toString() : "";
        this.uncollectedWithdrawalFrombnk = row.get("UNCOLLECTED_WITHDRAWAL_FROMBNK") != null ? row.get("UNCOLLECTED_WITHDRAWAL_FROMBNK").toString() : "";
        this.liabCurBal = row.get("LIAB_CUR_BAL") != null ? row.get("LIAB_CUR_BAL").toString() : "";
        this.liabUnclearedChqBal = row.get("LIAB_UNCLEARED_CHQ_BAL") != null ? row.get("LIAB_UNCLEARED_CHQ_BAL").toString() : "";
        this.liabPostpondedBal = row.get("LIAB_POSTPONDED_BAL") != null ? row.get("LIAB_POSTPONDED_BAL").toString() : "";
        this.liabCollectedBalPrevious = row.get("LIAB_COLLECTED_BAL_PREVIOUS") != null ? row.get("LIAB_COLLECTED_BAL_PREVIOUS").toString() : "";
        this.liabUncolWithdrawalFrombnk = row.get("LIAB_UNCOL_WITHDRAWAL_FROMBNK") != null ? row.get("LIAB_UNCOL_WITHDRAWAL_FROMBNK").toString() : "";
        this.liabMaturedBal = row.get("LIAB_MATURED_BAL") != null ? row.get("LIAB_MATURED_BAL").toString() : "";
        this.t3Balance = row.get("T3_BALANCE") != null ? row.get("T3_BALANCE").toString() : "";
        this.t3MaturityDate = row.get("T3_MATURITY_DATE") != null ? row.get("T3_MATURITY_DATE").toString() : "";
        this.t2Balance = row.get("T2_BALANCE") != null ? row.get("T2_BALANCE").toString() : "";
        this.t2MaturityDate = row.get("T2_MATURITY_DATE") != null ? row.get("T2_MATURITY_DATE").toString() : "";
        this.t1Balance = row.get("T1_BALANCE") != null ? row.get("T1_BALANCE").toString() : "";
        this.t1MaturityDate = row.get("T1_MATURITY_DATE") != null ? row.get("T1_MATURITY_DATE").toString() : "";
        this.t0Balance = row.get("T0_BALANCE") != null ? row.get("T0_BALANCE").toString() : "";
        this.t3UrgentDate = row.get("T3_URGENT_DATE") != null ? row.get("T3_URGENT_DATE").toString() : "";
        this.t3UrgentAmount = row.get("T3_URGENT_AMOUNT") != null ? row.get("T3_URGENT_AMOUNT").toString() : "";
        this.t3UrgentFees = row.get("T3_URGENT_FEES") != null ? row.get("T3_URGENT_FEES").toString() : "";
        this.t2UrgentDate = row.get("T2_URGENT_DATE") != null ? row.get("T2_URGENT_DATE").toString() : "";
        this.t2UrgentAmount = row.get("T2_URGENT_AMOUNT") != null ? row.get("T2_URGENT_AMOUNT").toString() : "";
        this.t2UrgentFees = row.get("T2_URGENT_FEES") != null ? row.get("T2_URGENT_FEES").toString() : "";
        this.t1UrgentDate = row.get("T1_URGENT_DATE") != null ? row.get("T1_URGENT_DATE").toString() : "";
        this.t1UrgentAmount = row.get("T1_URGENT_AMOUNT") != null ? row.get("T1_URGENT_AMOUNT").toString() : "";
        this.t1UrgentFees = row.get("T1_URGENT_FEES") != null ? row.get("T1_URGENT_FEES").toString() : "";
        this.reservedVisaBal = row.get("RESERVED_VISA_BAL") != null ? row.get("RESERVED_VISA_BAL").toString() : "";
        this.visaAccountIncome = row.get("VISA_ACCOUNT_INCOME") != null ? row.get("VISA_ACCOUNT_INCOME").toString() : "";
        this.visaMaxDailyCashbal = row.get("VISA_MAX_DAILY_CASHBAL") != null ? row.get("VISA_MAX_DAILY_CASHBAL").toString() : "";
        this.unclearedQpay = row.get("UNCLEARED_QPAY") != null ? row.get("UNCLEARED_QPAY").toString() : "";
        this.liabUnclearedQpay = row.get("LIAB_UNCLEARED_QPAY") != null ? row.get("LIAB_UNCLEARED_QPAY").toString() : "";
        this.reservedQpayRefund = row.get("RESERVED_QPAY_REFUND") != null ? row.get("RESERVED_QPAY_REFUND").toString() : "";
        this.atmWithdrawDay = row.get("ATM_WITHDRAW_DAY") != null ? row.get("ATM_WITHDRAW_DAY").toString() : "";
        this.atmWithdrawMonth = row.get("ATM_WITHDRAW_MONTH") != null ? row.get("ATM_WITHDRAW_MONTH").toString() : "";
        this.atmWithdrawYear = row.get("ATM_WITHDRAW_YEAR") != null ? row.get("ATM_WITHDRAW_YEAR").toString() : "";
        this.t0UrgentWithdrawal = row.get("T0_URGENT_WITHDRAWAL") != null ? row.get("T0_URGENT_WITHDRAWAL").toString() : "";
        this.t1UrgentWithdrawal = row.get("T1_URGENT_WITHDRAWAL") != null ? row.get("T1_URGENT_WITHDRAWAL").toString() : "";
        this.t2UrgentWithdrawal = row.get("T2_URGENT_WITHDRAWAL") != null ? row.get("T2_URGENT_WITHDRAWAL").toString() : "";
        this.t3UrgentWithdrawal = row.get("T3_URGENT_WITHDRAWAL") != null ? row.get("T3_URGENT_WITHDRAWAL").toString() : "";
        this.t4UrgentWithdrawal = row.get("T4_URGENT_WITHDRAWAL") != null ? row.get("T4_URGENT_WITHDRAWAL").toString() : "";
        this.t5UrgentWithdrawal = row.get("T5_URGENT_WITHDRAWAL") != null ? row.get("T5_URGENT_WITHDRAWAL").toString() : "";
        this.t6UrgentWithdrawal = row.get("T6_URGENT_WITHDRAWAL") != null ? row.get("T6_URGENT_WITHDRAWAL").toString() : "";
        this.t0UrgentSwift = row.get("T0_URGENT_SWIFT") != null ? row.get("T0_URGENT_SWIFT").toString() : "";
        this.t1UrgentSwift = row.get("T1_URGENT_SWIFT") != null ? row.get("T1_URGENT_SWIFT").toString() : "";
        this.t2UrgentSwift = row.get("T2_URGENT_SWIFT") != null ? row.get("T2_URGENT_SWIFT").toString() : "";
        this.t3UrgentSwift = row.get("T3_URGENT_SWIFT") != null ? row.get("T3_URGENT_SWIFT").toString() : "";
        this.t4UrgentSwift = row.get("T4_URGENT_SWIFT") != null ? row.get("T4_URGENT_SWIFT").toString() : "";
        this.t5UrgentSwift = row.get("T5_URGENT_SWIFT") != null ? row.get("T5_URGENT_SWIFT").toString() : "";
        this.t6UrgentSwift = row.get("T6_URGENT_SWIFT") != null ? row.get("T6_URGENT_SWIFT").toString() : "";
        this.totalExemptFromFees = row.get("TOTAL_EXEMPT_FROM_FEES") != null ? row.get("TOTAL_EXEMPT_FROM_FEES").toString() : "";
        this.t3MaxCurBal = row.get("T3_MAX_CUR_BAL") != null ? row.get("T3_MAX_CUR_BAL").toString() : "";
        this.mBalance = row.get("M_BALANCE") != null ? row.get("M_BALANCE").toString() : "";
    }

    /**
     * Get record count
     * @return Number of records
     */
    public int getRecordCount() {
        return allData.size();
    }

    /**
     * Get all data records
     * @return List of all data records
     */
    public List<Map<String, Object>> getAllRecords() {
        return allData;
    }

    /**
     * Get record by index
     * @param index Record index (0-based)
     * @return Record at specified index, or null if index out of bounds
     */
    public Map<String, Object> getRecordByIndex(int index) {
        if (index >= 0 && index < allData.size()) {
            return allData.get(index);
        }
        return null;
    }

    /**
     * Print all client balances data
     */
    public void printAllData() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          All Client Balances Records                          ║");
        System.out.println("║          Total Records: " + allData.size());
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        for (int i = 0; i < allData.size(); i++) {
            System.out.println("─────────────────── Record " + (i + 1) + " ───────────────────");
            Map<String, Object> record = allData.get(i);
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                System.out.printf("%-35s: %s%n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    /**
     * Print client balances data in table format
     */
    public void printDataTable() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                      Client Balances Data                                                      ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-10s ║ %-15s ║ %-15s ║ %-15s ║ %-15s ║ %-15s ║%n",
                "NIN", "Current Bal", "Matured Bal", "T3 Balance", "T2 Balance", "T1 Balance");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");

        for (Map<String, Object> record : allData) {
            String ninVal = record.get("NIN") != null ? record.get("NIN").toString() : "";
            String curBalVal = record.get("CUR_BAL") != null ? record.get("CUR_BAL").toString() : "";
            String maturedBalVal = record.get("MATURED_BAL") != null ? record.get("MATURED_BAL").toString() : "";
            String t3BalVal = record.get("T3_BALANCE") != null ? record.get("T3_BALANCE").toString() : "";
            String t2BalVal = record.get("T2_BALANCE") != null ? record.get("T2_BALANCE").toString() : "";
            String t1BalVal = record.get("T1_BALANCE") != null ? record.get("T1_BALANCE").toString() : "";

            // Truncate long values
            if (ninVal.length() > 10) ninVal = ninVal.substring(0, 7) + "...";
            if (curBalVal.length() > 15) curBalVal = curBalVal.substring(0, 12) + "...";
            if (maturedBalVal.length() > 15) maturedBalVal = maturedBalVal.substring(0, 12) + "...";
            if (t3BalVal.length() > 15) t3BalVal = t3BalVal.substring(0, 12) + "...";
            if (t2BalVal.length() > 15) t2BalVal = t2BalVal.substring(0, 12) + "...";
            if (t1BalVal.length() > 15) t1BalVal = t1BalVal.substring(0, 12) + "...";

            System.out.printf("║ %-10s ║ %-15s ║ %-15s ║ %-15s ║ %-15s ║ %-15s ║%n",
                    ninVal, curBalVal, maturedBalVal, t3BalVal, t2BalVal, t1BalVal);
        }

        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total Records: " + allData.size());
    }

    /**
     * Print first record details
     */
    public void printFirstRecord() {
        if (allData.isEmpty()) {
            System.out.println("No data available to print");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║           Client Balances - First Record                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println("\n  NIN:                   " + nin);
        System.out.println("  Current Balance:       " + curBal);
        System.out.println("  Matured Balance:       " + maturedBal);
        System.out.println("  T3 Balance:            " + t3Balance);
        System.out.println("  T2 Balance:            " + t2Balance);
        System.out.println("  T1 Balance:            " + t1Balance);
        System.out.println("  T0 Balance:            " + t0Balance);
        System.out.println("  Uncleared Chq Bal:     " + unclearedChqBal);
        System.out.println("  Postponded Balance:    " + postpondedBal);
        System.out.println("  Created By:            " + createdBy);
        System.out.println("  Creation Date:         " + creationDate);
        System.out.println("  User ID:               " + userId);
        System.out.println("  Time Stamp:            " + timeStamp);
        System.out.println();
    }

    // Getters for all 73 columns
    public String getNin() { return nin; }
    public String getCurBal() { return curBal; }
    public String getUnclearedChqBal() { return unclearedChqBal; }
    public String getPostpondedBal() { return postpondedBal; }
    public String getCreatedBy() { return createdBy; }
    public String getCreationDate() { return creationDate; }
    public String getUserId() { return userId; }
    public String getTimeStamp() { return timeStamp; }
    public String getBuyOrdinmarketBal() { return buyOrdinmarketBal; }
    public String getDvpUnclearedBal() { return dvpUnclearedBal; }
    public String getT3Sell() { return t3Sell; }
    public String getT2Sell() { return t2Sell; }
    public String getT1Sell() { return t1Sell; }
    public String getMaturedBal() { return maturedBal; }
    public String getCollectedBalPrevious() { return collectedBalPrevious; }
    public String getT3Buy() { return t3Buy; }
    public String getT2Buy() { return t2Buy; }
    public String getT1Buy() { return t1Buy; }
    public String getT3MaturedAmount() { return t3MaturedAmount; }
    public String getT2MaturedAmount() { return t2MaturedAmount; }
    public String getT1MaturedAmount() { return t1MaturedAmount; }
    public String getT0Sell() { return t0Sell; }
    public String getT0Buy() { return t0Buy; }
    public String getT0MaturedAmount() { return t0MaturedAmount; }
    public String getUncollectedWithdrawalFrombnk() { return uncollectedWithdrawalFrombnk; }
    public String getLiabCurBal() { return liabCurBal; }
    public String getLiabUnclearedChqBal() { return liabUnclearedChqBal; }
    public String getLiabPostpondedBal() { return liabPostpondedBal; }
    public String getLiabCollectedBalPrevious() { return liabCollectedBalPrevious; }
    public String getLiabUncolWithdrawalFrombnk() { return liabUncolWithdrawalFrombnk; }
    public String getLiabMaturedBal() { return liabMaturedBal; }
    public String getT3Balance() { return t3Balance; }
    public String getT3MaturityDate() { return t3MaturityDate; }
    public String getT2Balance() { return t2Balance; }
    public String getT2MaturityDate() { return t2MaturityDate; }
    public String getT1Balance() { return t1Balance; }
    public String getT1MaturityDate() { return t1MaturityDate; }
    public String getT0Balance() { return t0Balance; }
    public String getT3UrgentDate() { return t3UrgentDate; }
    public String getT3UrgentAmount() { return t3UrgentAmount; }
    public String getT3UrgentFees() { return t3UrgentFees; }
    public String getT2UrgentDate() { return t2UrgentDate; }
    public String getT2UrgentAmount() { return t2UrgentAmount; }
    public String getT2UrgentFees() { return t2UrgentFees; }
    public String getT1UrgentDate() { return t1UrgentDate; }
    public String getT1UrgentAmount() { return t1UrgentAmount; }
    public String getT1UrgentFees() { return t1UrgentFees; }
    public String getReservedVisaBal() { return reservedVisaBal; }
    public String getVisaAccountIncome() { return visaAccountIncome; }
    public String getVisaMaxDailyCashbal() { return visaMaxDailyCashbal; }
    public String getUnclearedQpay() { return unclearedQpay; }
    public String getLiabUnclearedQpay() { return liabUnclearedQpay; }
    public String getReservedQpayRefund() { return reservedQpayRefund; }
    public String getAtmWithdrawDay() { return atmWithdrawDay; }
    public String getAtmWithdrawMonth() { return atmWithdrawMonth; }
    public String getAtmWithdrawYear() { return atmWithdrawYear; }
    public String getT0UrgentWithdrawal() { return t0UrgentWithdrawal; }
    public String getT1UrgentWithdrawal() { return t1UrgentWithdrawal; }
    public String getT2UrgentWithdrawal() { return t2UrgentWithdrawal; }
    public String getT3UrgentWithdrawal() { return t3UrgentWithdrawal; }
    public String getT4UrgentWithdrawal() { return t4UrgentWithdrawal; }
    public String getT5UrgentWithdrawal() { return t5UrgentWithdrawal; }
    public String getT6UrgentWithdrawal() { return t6UrgentWithdrawal; }
    public String getT0UrgentSwift() { return t0UrgentSwift; }
    public String getT1UrgentSwift() { return t1UrgentSwift; }
    public String getT2UrgentSwift() { return t2UrgentSwift; }
    public String getT3UrgentSwift() { return t3UrgentSwift; }
    public String getT4UrgentSwift() { return t4UrgentSwift; }
    public String getT5UrgentSwift() { return t5UrgentSwift; }
    public String getT6UrgentSwift() { return t6UrgentSwift; }
    public String getTotalExemptFromFees() { return totalExemptFromFees; }
    public String getT3MaxCurBal() { return t3MaxCurBal; }
    public String getMBalance() { return mBalance; }
}
