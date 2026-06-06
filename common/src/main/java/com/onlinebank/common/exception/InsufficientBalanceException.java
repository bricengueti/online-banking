package com.onlinebank.common.exception;

import org.springframework.http.HttpStatus;

import com.onlinebank.common.config.exception.BusinessException;

/**
 * Exception thrown when account balance is insufficient for a transaction.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
public class InsufficientBalanceException extends  BusinessException{

    private static final String ERROR_CODE = "INSUFFICIENT_BALANCE";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private final String accountId;
    private final Double currentBalance;
    private final Double requestedAmount;

    public InsufficientBalanceException(String accountId, Double currentBalance, Double requestedAmount) {
        super(
                String.format("Insufficient balance in account %s. Current balance: %.2f, Requested: %.2f",
                        accountId, currentBalance, requestedAmount),
                ERROR_CODE,
                STATUS
        );
        this.accountId = accountId;
        this.currentBalance = currentBalance;
        this.requestedAmount = requestedAmount;
    }

    public String getAccountId() {
        return accountId;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public Double getRequestedAmount() {
        return requestedAmount;
    }

    public Double getDifference() {
        return requestedAmount - currentBalance;
    }
}