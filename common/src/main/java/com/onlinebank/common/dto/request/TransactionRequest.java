package com.onlinebank.common.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Transaction request DTO for credit/debit operations.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
public record TransactionRequest(
        @NotBlank(message = "Account ID is required")
        @JsonProperty("account_id")
        String accountId,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        @JsonProperty("amount")
        Double amount,

        @JsonProperty("description")
        String description,

        @JsonProperty("reference")
        String reference
) {
    public TransactionRequest {
        if (amount != null && amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    public static TransactionRequest of(String accountId, Double amount) {
        return new TransactionRequest(accountId, amount, null, null);
    }

    public static TransactionRequest of(String accountId, Double amount, String description) {
        return new TransactionRequest(accountId, amount, description, null);
    }
}