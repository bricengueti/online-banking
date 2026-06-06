package com.onlinebank.accountingService.entity;

import com.onlinebank.common.util.Audit;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "accounts")
public class Account  extends Audit {

    @Id
    private String id;

    @Indexed(unique = true)
    private String accountNumber;

    private String userId;

    private BigDecimal balance = BigDecimal.ZERO;

    private String currency = "XAF";

    public Account(){
    }

    public Account(String createdBy, String id, String accountNumber, String userId, BigDecimal balance, String currency) {
        super(createdBy);
        this.id = id;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.balance = balance;
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public Account setId(String id) {
        this.id = id;
        return this;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Account setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Account setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Account setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public Account setCurrency(String currency) {
        this.currency = currency;
        return this;
    }
}