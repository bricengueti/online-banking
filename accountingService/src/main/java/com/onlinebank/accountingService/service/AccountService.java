package com.onlinebank.accountingService.service;

import com.onlinebank.accountingService.entity.Account;
import com.onlinebank.accountingService.entity.AccountingJournal;
import com.onlinebank.accountingService.entity.Direction;
import com.onlinebank.accountingService.repository.AccountRepository;
import com.onlinebank.accountingService.repository.AccountingJournalRepository;
import com.onlinebank.common.dto.request.AccountCreditRequestDTO;
import com.onlinebank.common.dto.request.AccountDebitRequestDTO;
import com.onlinebank.common.dto.request.AccountRequestDTO;
import com.onlinebank.common.dto.response.AccountCreditDebitResponse;
import com.onlinebank.common.dto.response.AccountResponse;
import com.onlinebank.common.exception.DuplicateResourceException;
import com.onlinebank.common.exception.InsufficientBalanceException;
import com.onlinebank.common.exception.ResourceNotFoundException;
import com.onlinebank.common.exception.TransactionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountingJournalRepository journalRepository;

    public AccountService(AccountRepository accountRepository,
                          AccountingJournalRepository journalRepository) {
        this.accountRepository = accountRepository;
        this.journalRepository = journalRepository;
    }

    /**
     * Création d'un nouveau compte
     */
    public AccountResponse createAccount(AccountRequestDTO dto) {
        accountRepository.findByAccountNumber(dto.accountNumber())
                .ifPresent(acc -> {
                    throw new DuplicateResourceException("Account", "accountNumber", dto.accountNumber());
                });

        Account account = new Account()
                .setUserId(dto.userId())
                .setAccountNumber(dto.accountNumber())
                .setBalance(dto.initialBalance() != null ? dto.initialBalance() : BigDecimal.ZERO)
                .setCurrency(dto.currency() != null ? dto.currency() : "XAF");

        Account saved = accountRepository.save(account);

        return new AccountResponse(
                saved.getId(),
                saved.getAccountNumber(),
                saved.getUserId(),
                saved.getBalance(),
                saved.getCurrency()
        );
    }

    /**
     * Créditer un compte
     */
    @Transactional
    public AccountCreditDebitResponse credit(AccountCreditRequestDTO dto) {
        Account account = accountRepository.findByAccountNumber(dto.accountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber", dto.accountNumber()));

        BigDecimal balanceBefore = account.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(dto.amount());

        account.setBalance(balanceAfter);
        accountRepository.save(account);

        try {
            AccountingJournal journal = new AccountingJournal()
                    .setAccount(account)
                    .setDirection(Direction.CREDIT)
                    .setAmount(dto.amount())
                    .setBalanceBefore(balanceBefore)
                    .setBalanceAfter(balanceAfter)
                    .setDescription(dto.description());

            journalRepository.save(journal);
        } catch (Exception e) {
            throw TransactionException.journalCreationFailed(Direction.CREDIT+"-" + account.getAccountNumber());
        }

        return new AccountCreditDebitResponse(
                account.getAccountNumber(),
                dto.amount(),
                Direction.CREDIT.toString(),
                balanceBefore,
                balanceAfter,
                dto.description()
        );
    }

    /**
     * Débiter un compte
     */
    @Transactional
    public AccountCreditDebitResponse debit(AccountDebitRequestDTO dto) {
        Account account = accountRepository.findByAccountNumber(dto.accountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber", dto.accountNumber()));

        BigDecimal balanceBefore = account.getBalance();
        if (balanceBefore.compareTo(dto.amount()) < 0) {
            throw new InsufficientBalanceException(
                    account.getId(),
                    balanceBefore.doubleValue(),
                    dto.amount().doubleValue()
            );
        }

        BigDecimal balanceAfter = balanceBefore.subtract(dto.amount());

        account.setBalance(balanceAfter);
        accountRepository.save(account);

        try {
            AccountingJournal journal = new AccountingJournal()
                    .setAccount(account)
                    .setDirection(Direction.DEBIT)
                    .setAmount(dto.amount())
                    .setBalanceBefore(balanceBefore)
                    .setBalanceAfter(balanceAfter)
                    .setDescription(dto.description());

            journalRepository.save(journal);
        } catch (Exception e) {
            throw TransactionException.journalCreationFailed(Direction.DEBIT+"-" + account.getAccountNumber());
        }

        return new AccountCreditDebitResponse(
                account.getAccountNumber(),
                dto.amount(),
                Direction.DEBIT.toString(),
                balanceBefore,
                balanceAfter,
                dto.description()
        );
    }
}
