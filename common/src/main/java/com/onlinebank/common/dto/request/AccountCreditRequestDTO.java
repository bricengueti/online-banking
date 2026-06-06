package com.onlinebank.common.dto.request;

import java.math.BigDecimal;

/**
 * Requête pour créditer un compte
 */
public record AccountCreditRequestDTO(
        String accountNumber,   // numéro du compte à créditer
        BigDecimal amount,      // montant à créditer
        String description      // description de l'opération
) {}
