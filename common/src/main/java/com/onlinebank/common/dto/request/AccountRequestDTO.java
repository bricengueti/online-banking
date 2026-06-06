package com.onlinebank.common.dto.request;

import java.math.BigDecimal;

/**
 * Requête pour créer un compte
 */
public record AccountRequestDTO(
        String userId,          // identifiant du propriétaire du compte
        String accountNumber,   // numéro du compte
        BigDecimal initialBalance, // solde initial
        String currency
) {}
