package com.onlinebank.common.dto.response;

import java.math.BigDecimal;

/**
 * Réponse après une opération de crédit ou de débit
 */
public record AccountCreditDebitResponse(
        String accountNumber,   // numéro du compte concerné
        BigDecimal amount,      // montant de l'opération
        String direction,       // "CREDIT" ou "DEBIT"
        BigDecimal balanceBefore, // solde avant l'opération
        BigDecimal balanceAfter,  // solde après l'opération
        String description      // description de l'opération
) {}
