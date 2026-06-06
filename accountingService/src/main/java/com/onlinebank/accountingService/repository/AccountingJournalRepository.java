package com.onlinebank.accountingService.repository;

import com.onlinebank.accountingService.entity.AccountingJournal;
import com.onlinebank.accountingService.entity.Direction;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AccountingJournalRepository extends MongoRepository<AccountingJournal, String> {

    // Récupérer tous les journaux d’un compte
    List<AccountingJournal> findByAccount_Id(String accountId);

    // Récupérer tous les journaux par direction (CREDIT ou DEBIT)
    List<AccountingJournal> findByDirection(Direction direction);
}
