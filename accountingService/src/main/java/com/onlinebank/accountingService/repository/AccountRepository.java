package com.onlinebank.accountingService.repository;

import com.onlinebank.accountingService.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {

    // Exemple de méthode personnalisée
    Optional<Account> findByAccountNumber(String accountNumber);
}
