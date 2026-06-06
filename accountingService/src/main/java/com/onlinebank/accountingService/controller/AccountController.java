package com.onlinebank.accountingService.controller;

import com.onlinebank.accountingService.service.AccountService;
import com.onlinebank.common.dto.request.AccountRequestDTO;
import com.onlinebank.common.dto.request.AccountCreditRequestDTO;
import com.onlinebank.common.dto.request.AccountDebitRequestDTO;
import com.onlinebank.common.dto.response.AccountResponse;
import com.onlinebank.common.dto.response.AccountCreditDebitResponse;
import com.onlinebank.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Comptes bancaires", description = "Endpoints pour la création et la gestion des comptes bancaires")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
            summary = "Créer un compte",
            description = "Crée un nouveau compte bancaire pour un utilisateur."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@RequestBody AccountRequestDTO dto) {
        AccountResponse response = accountService.createAccount(dto);
        return ResponseEntity.ok(ApiResponse.created(response, "Compte créé avec succès"));
    }

    @Operation(
            summary = "Créditer un compte",
            description = "Ajoute un montant au solde du compte spécifié."
    )
    @PostMapping("/credit")
    public ResponseEntity<ApiResponse<AccountCreditDebitResponse>> credit(@RequestBody AccountCreditRequestDTO dto) {
        AccountCreditDebitResponse response = accountService.credit(dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Compte crédité avec succès"));
    }

    @Operation(
            summary = "Débiter un compte",
            description = "Déduit un montant du solde du compte spécifié."
    )
    @PostMapping("/debit")
    public ResponseEntity<ApiResponse<AccountCreditDebitResponse>> debit(@RequestBody AccountDebitRequestDTO dto) {
        AccountCreditDebitResponse response = accountService.debit(dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Compte débité avec succès"));
    }
}