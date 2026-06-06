package com.onlinebank.common.dto.request;


import java.math.BigDecimal;

/**
 * Requête pour débiter un compte
 */
public record AccountDebitRequestDTO(
        String accountNumber,   // numéro du compte à débiter
        BigDecimal amount,      // montant à débiter
        String description      // description de l'opération
) {}
