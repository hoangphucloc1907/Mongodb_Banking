package com.loczxph.banking.service;

import com.loczxph.banking.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    List<TransactionDTO> getAllTransactionsByAccountNumber(String accountNumber);
}
