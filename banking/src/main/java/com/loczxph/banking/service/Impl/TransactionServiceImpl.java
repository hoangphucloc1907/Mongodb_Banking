package com.loczxph.banking.service.Impl;

import com.loczxph.banking.dto.TransactionDTO;
import com.loczxph.banking.dto.TransactionMapper;
import com.loczxph.banking.model.Account;
import com.loczxph.banking.model.Transaction;
import com.loczxph.banking.repository.AccountRepository;
import com.loczxph.banking.repository.TransactionRepository;
import com.loczxph.banking.service.TransactionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<TransactionDTO> getAllTransactionsByAccountNumber(String accountNumber) {
        if (StringUtils.isBlank(accountNumber)) {
            return Collections.emptyList();
        }

        System.out.println(accountNumber);

        Account account = accountRepository.findByAccountNumber(accountNumber);

        if (account != null) {
            List<Transaction> transactions = transactionRepository.findBySourceAccountOrTargetAccount(account, account);

            List<TransactionDTO> transactionDTOs = transactions.stream()
                    .map(transactionMapper::toDto)
                    .sorted(Comparator.comparing(TransactionDTO::getTransaction_date).reversed())
                    .collect(Collectors.toList());

            return transactionDTOs;
        } else {
            // Xử lý trường hợp không tìm thấy tài khoản
            // Ví dụ: throw NotFoundException("Account not found");
            return Collections.emptyList();
        }

    }
}
