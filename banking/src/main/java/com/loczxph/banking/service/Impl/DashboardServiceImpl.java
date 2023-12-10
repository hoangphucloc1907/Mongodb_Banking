package com.loczxph.banking.service.Impl;

import com.loczxph.banking.dto.AccountResponse;
import com.loczxph.banking.dto.UserResponse;
import com.loczxph.banking.exception.NotFoundException;
import com.loczxph.banking.model.Account;
import com.loczxph.banking.model.User;
import com.loczxph.banking.repository.AccountRepository;
import com.loczxph.banking.repository.UserRepository;
import com.loczxph.banking.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;



    @Override
    public UserResponse getUserDetails(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);

        if (account == null) {
            throw new NotFoundException("Account not found for the provided account number.");
        }

        User user = userRepository.findByAccount_Id(account.getId());

        if (user == null) {
            throw new NotFoundException("User not found for the provided account number.");
        }

        // Map thông tin từ user và account sang UserResponse
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setAccountNumber(account.getAccountNumber());
        userResponse.setRole(user.getRole());
        return userResponse;
    }

    @Override
    public AccountResponse getAccountDetails(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        // Check if the account exists with the provided account number
        if (account == null) {
            throw new NotFoundException("Account not found for the provided account number.");
        }

        // Map the account entity to AccountResponse DTO
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountNumber(account.getAccountNumber());
        accountResponse.setBalance(account.getBalance());

        return accountResponse;
    }

}
