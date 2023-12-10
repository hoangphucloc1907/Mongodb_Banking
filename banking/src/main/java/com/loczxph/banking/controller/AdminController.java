package com.loczxph.banking.controller;


import com.loczxph.banking.dto.AccountResponse;
import com.loczxph.banking.dto.UserResponse;
import com.loczxph.banking.model.User;
import com.loczxph.banking.service.DashboardService;
import com.loczxph.banking.service.Impl.ReportServiceImpl;
import com.loczxph.banking.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ReportServiceImpl reportService;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/allUser")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> userDtos = userService.getAllUsers();
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/findAccount/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccountDetails(@PathVariable String accountNumber) {
        AccountResponse accountResponse = dashboardService.getAccountDetails(accountNumber);
        return ResponseEntity.ok(accountResponse);
    }

//    @GetMapping("/total-withdrawals")
//    public ResponseEntity<Long> getTotalWithdrawals() {
//        return ResponseEntity.ok(reportService.getTotalWithdrawals());
//    }
//
//    @GetMapping("/total-deposits")
//    public ResponseEntity<Long> getTotalDeposits() {
//        return ResponseEntity.ok(reportService.getTotalDeposits());
//    }

    @GetMapping("/total-transactions")
    public ResponseEntity<Map<String, Long>> getTotalTransactions() {

        Long totalTransactions = reportService.getTotalTransactions();
        Long totalAmount = reportService.getTotalAmount();

        Map<String, Long> result = new HashMap<>();
        result.put("totalTransactions", totalTransactions);
        result.put("totalAmount", totalAmount);

        return ResponseEntity.ok(result);
    }


//    @GetMapping("/deposits-by-date")
//    public ResponseEntity<Map<String, Long>> getDepositsByDate(
//            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//        Map<String, Long> depositsByDate = reportService.getDepositsByDate(startDate, endDate);
//        return ResponseEntity.ok(depositsByDate);
//    }


    @GetMapping("/totalTransactionsAll")
    public ResponseEntity<Map<LocalDate, Map<String, Long>>> getTotalTransactionsAndAmountForAllAccountsByDay() {
        Map<LocalDate, Map<String, Long>> result = reportService.getTotalTransactionsAndAmountForAllAccountsByDay();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/transaction/byTypeAndDay")
    public ResponseEntity<Map<LocalDate, Long>> getTransactionsByTypeAndDay(
            @RequestParam("type") String transactionType) {
        Map<LocalDate, Long> result = reportService.getTransactionsByTypeAndDay(transactionType);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
