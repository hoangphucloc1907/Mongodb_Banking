package com.loczxph.banking.service.Impl;

import com.loczxph.banking.dto.TotalAmountResult;
import com.loczxph.banking.exception.NotFoundException;
import com.loczxph.banking.model.Account;
import com.loczxph.banking.model.Transaction;
import com.loczxph.banking.repository.AccountRepository;
import com.loczxph.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Long getTotalWithdrawals() {
        Query query = new Query(Criteria.where("transaction_type").is("Withdrawal"));
        return mongoTemplate.count(query, Transaction.class);
    }

    public Long getTotalDeposits() {
        Query query = new Query(Criteria.where("transaction_type").is("Deposit"));
        return mongoTemplate.count(query, Transaction.class);
    }

    public Long getTotalTransactions() {
        return mongoTemplate.count(new Query(), Transaction.class);
    }


    public Map<String, Long> getDepositsByDate(LocalDate startDate, LocalDate endDate) {
        Query query = new Query(Criteria.where("transaction_type").is("Deposit")
                .and("date").gte(startDate).lte(endDate));

        List<Transaction> deposits = mongoTemplate.find(query, Transaction.class);
        return groupByDate(deposits);
    }

    public Long getTotalAmount() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group().sum("amount").as("totalAmount")
        );

        AggregationResults<TotalAmountResult> result = mongoTemplate.aggregate(aggregation, "transaction", TotalAmountResult .class);
        TotalAmountResult totalAmountResult = result.getUniqueMappedResult();

        return totalAmountResult != null ? totalAmountResult.getTotalAmount() : 0;
    }

    public Map<LocalDate, Map<String, Long>> getTotalTransactionsAndAmountByDay(String accountNumber) {
        // Lấy tất cả các giao dịch liên quan đến tài khoản và loại Withdrawal, Deposit, Transfer
        List<Transaction> transactions = transactionRepository
                .findAllBySourceAccount_AccountNumberOrTargetAccount_AccountNumberAndTransactionTypeIn(accountNumber,
                        accountNumber, Arrays.asList("Withdrawal", "Deposit", "Transfer"));

        // Sử dụng Stream API để nhóm các giao dịch theo ngày và loại giao dịch, sau đó tính tổng số lượng và tổng số tiền
        Map<LocalDate, Map<String, Long>> result = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getDate,
                        Collectors.groupingBy(Transaction::getTransactionType, Collectors.summingLong(Transaction::getAmount))));

        return result;
    }

    public Map<LocalDate, Map<String, Long>> getTotalTransactionsAndAmountForAllAccountsByDay() {
        // Lấy tất cả các giao dịch từ MongoDB
        List<Transaction> allTransactions = transactionRepository.findAll();

        // Sử dụng Java Streams để nhóm giao dịch theo ngày và loại giao dịch
        Map<LocalDate, Map<String, Long>> result = allTransactions.stream()
                .filter(transaction -> transaction.getDate() != null) // Loại bỏ các giao dịch có ngày là null
                .collect(Collectors.groupingBy(
                        Transaction::getDate,
                        Collectors.groupingBy(
                                transaction -> {
                                    // Handle null transaction types, assuming a default type if it's null
                                    return transaction.getTransactionType() != null ? transaction.getTransactionType() : "Transfer";
                                },
                                Collectors.summingLong(Transaction::getAmount)
                        )
                ));

        return result;
    }

    public Map<LocalDate, Long> getTransactionsByTypeAndDay(String transactionType) {
        // Lấy tất cả các giao dịch từ MongoDB
        List<Transaction> allTransactions = transactionRepository.findAll();

        // Sử dụng Java Streams để lọc và nhóm giao dịch theo loại và ngày
        Map<LocalDate, Long> result = allTransactions.stream()
                .filter(transaction ->
                        transaction.getDate() != null && // Loại bỏ các giao dịch có ngày là null
                                transaction.getTransactionType() != null && // Loại bỏ các giao dịch có loại là null
                                transaction.getTransactionType().equals(transactionType)) // Lọc theo loại giao dịch mong muốn
                .collect(Collectors.groupingBy(
                        Transaction::getDate,
                        Collectors.summingLong(Transaction::getAmount)
                ));

        return result;
    }


    private Map<String, Long> groupByDate(List<Transaction> transactions) {
        Map<String, Long> result = new HashMap<>();
        for (Transaction transaction : transactions) {
            String dateKey = transaction.getDate().toString();
            result.merge(dateKey, 1L, Long::sum);
        }
        return result;
    }


}
