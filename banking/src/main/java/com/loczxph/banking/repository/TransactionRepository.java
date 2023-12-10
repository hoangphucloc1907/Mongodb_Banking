package com.loczxph.banking.repository;

import com.loczxph.banking.model.Account;
import com.loczxph.banking.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findBySourceAccountOrTargetAccount(Account sourceAccountNumber, Account targetAccountNumber);

    List<Transaction> findAllBySourceAccount_AccountNumberOrTargetAccount_AccountNumberAndTransactionTypeIn(
            String sourceAccountNumber, String targetAccountNumber, List<String> transactionTypes);
}
