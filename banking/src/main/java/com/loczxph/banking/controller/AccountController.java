package com.loczxph.banking.controller;

import com.loczxph.banking.dto.*;
import com.loczxph.banking.exception.InsufficientBalanceException;
import com.loczxph.banking.exception.NotFoundException;
import com.loczxph.banking.exception.UnauthorizedException;
import com.loczxph.banking.service.AccountService;
import com.loczxph.banking.service.TransactionService;
import com.loczxph.banking.util.LoggedinUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/pin/check")
    public ResponseEntity<?> checkAccountPIN() {
        boolean isPINValid = accountService.isPinCreated(LoggedinUser.getAccountNumber());


        Map<String, Object> result =  new HashMap<>();
        result.put("hasPIN",isPINValid );

        if (isPINValid) {
            result.put("msg", "PIN Created");

        } else {
            result.put("msg", "Pin Not Created");
        }

        return new ResponseEntity<>( result, HttpStatus.OK);
    }

    @PostMapping("/pin/create")
    public ResponseEntity<?> createPIN(@RequestBody PinRequest pinRequest) {
        accountService.createPIN(LoggedinUser.getAccountNumber(), pinRequest.getPassword(), pinRequest.getPin());

        Map<String, String> response =  new HashMap<>();
        response.put("msg", "PIN created successfully");

        return new ResponseEntity<>( response, HttpStatus.OK);

    }

    @PostMapping("/pin/update")
    public ResponseEntity<?> updatePIN(@RequestBody PinUpdateRequest pinUpdateRequest) {
        accountService.updatePIN(LoggedinUser.getAccountNumber(), pinUpdateRequest.getOldPin(), pinUpdateRequest.getPassword(), pinUpdateRequest.getNewPin());

        Map<String, String> response =  new HashMap<>();
        response.put("msg", "PIN updated successfully");

        return new ResponseEntity<>( response, HttpStatus.OK);

    }

    @PostMapping("/deposit")
    public ResponseEntity<?> cashDeposit(@RequestBody AmountRequest amountRequest) {
        long amount = amountRequest.getAmount();

        if (amount <= 0) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Amount must > 0");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        accountService.cashDeposit(LoggedinUser.getAccountNumber(), amountRequest.getPin(), amountRequest.getAmount());

        Map<String, String> response =  new HashMap<>();
        response.put("msg", "Cash deposited successfully");

        return new ResponseEntity<>( response, HttpStatus.OK);

    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> cashWithdrawal(@RequestBody AmountRequest amountRequest) {
        try {
            if (amountRequest.getAmount() <= 0) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid withdrawal amount");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            accountService.cashWithdrawal(LoggedinUser.getAccountNumber(), amountRequest.getPin(), amountRequest.getAmount());

            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("msg", "Cash withdrawn successfully");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> fundTransfer(@RequestBody FundTransferRequest fundTransferRequest) {
        if (fundTransferRequest.getAmount() <= 0) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid transfer amount");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            accountService.fundTransfer(
                    LoggedinUser.getAccountNumber(),
                    fundTransferRequest.getTargetAccountNumber(),
                    fundTransferRequest.getPin(),
                    fundTransferRequest.getAmount()
            );

            Map<String, String> response =  new HashMap<>();
            response.put("msg", "Fund transferred successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        } catch (InsufficientBalanceException e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsByAccountNumber() {
//        List<TransactionDTO> transactions = transactionService.getAllTransactionsByAccountNumber(LoggedinUser.getAccountNumber());
        return ResponseEntity.ok(transactionService.getAllTransactionsByAccountNumber(LoggedinUser.getAccountNumber()));
    }

}
