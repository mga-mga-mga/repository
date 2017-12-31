package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionType {

    /**
     * Debiting funds from an account.
     */
    WRITEOFF("writeOff"),

    /**
     * Crediting funds to an account.
     */
    ACCRUAL("accrual"),

    /**
     * Transferring funds from one account to another.
     */
    TRANSFER("transfer");

    private String key;

    TransactionType(String key) {
        this.key = key;
    }

    @JsonCreator
    public static TransactionType fromString(String key) {
        return key == null ? null : TransactionType.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}