package com.onlinebank.common.dto.response;


import java.math.BigDecimal;

/**
 * Réponse représentant l'état d'un compte
 */
public record AccountResponse(
        String id,              // identifiant du compte
        String accountNumber,   // numéro unique du compte
        String userId,          // propriétaire du compte
        BigDecimal balance,     // solde actuel
        String currency
) {}

