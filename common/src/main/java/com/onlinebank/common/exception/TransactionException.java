package com.onlinebank.common.exception;

import org.springframework.http.HttpStatus;

import com.onlinebank.common.config.exception.BusinessException;

public class TransactionException extends com.onlinebank.common.config.exception.BusinessException {

    private static final String ERROR_CODE = "TRANSACTION_ERROR";
    private static final HttpStatus STATUS = HttpStatus.UNPROCESSABLE_ENTITY;

    private final String transactionId;

    public TransactionException(String message) {
        super(message, ERROR_CODE, STATUS);
        this.transactionId = null;
    }

    public TransactionException(String message, String transactionId) {
        super(message, ERROR_CODE, STATUS);
        this.transactionId = transactionId;
    }

    // NEW: Constructor with message and cause
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
        this.transactionId = null;
    }

    // NEW: Constructor with message, transactionId, and cause
    public TransactionException(String message, String transactionId, Throwable cause) {
        super(message, cause);
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public static TransactionException accountNotFound(String accountId) {
        return new TransactionException(
                String.format("Account %s not found", accountId)
        );
    }

    public static TransactionException journalCreationFailed(String transactionId) {
        return new TransactionException(
                String.format("Failed to create journal entry for transaction %s", transactionId),
                transactionId
        );
    }
}