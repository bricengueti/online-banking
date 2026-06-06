package com.onlinebank.accountingService.entity;

import com.onlinebank.common.util.Audit;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.math.BigDecimal;

@Document(collection = "accounting_journals")
public class AccountingJournal extends Audit {

    @Id
    private String id;

    @DBRef
    private Account account;

    private Direction direction; // CREDIT / DEBIT

    private BigDecimal amount;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    private String description;




    public AccountingJournal() {
        super();
    }

    public AccountingJournal(String id, Account account, Direction direction, BigDecimal amount, BigDecimal balanceBefore, BigDecimal balanceAfter, String description) {
        this.id = id;
        this.account = account;
        this.direction = direction;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public AccountingJournal setId(String id) {
        this.id = id;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public AccountingJournal setAccount(Account account) {
        this.account = account;
        return this;
    }

    public Direction getDirection() {
        return direction;
    }

    public AccountingJournal setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public AccountingJournal setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public AccountingJournal setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
        return this;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public AccountingJournal setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AccountingJournal setDescription(String description) {
        this.description = description;
        return this;
    }
}