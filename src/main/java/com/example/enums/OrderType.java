package com.example.enums;

/**
 * Enum representing different types of orders
 * Eliminates magic strings and improves type safety
 */
public enum OrderType {
    BUY("B", "BUY"),
    SELL("S", "SELL");

    private final String code;
    private final String displayName;

    OrderType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Get OrderType from code
     * @param code Order type code (B or S)
     * @return OrderType enum
     */
    public static OrderType fromCode(String code) {
        for (OrderType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown order type code: " + code);
    }
}
