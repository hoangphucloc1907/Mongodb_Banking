package com.loczxph.banking.service;

import com.loczxph.banking.dto.AccountResponse;
import com.loczxph.banking.dto.UserResponse;

import java.util.Date;

public interface DashboardService {
    UserResponse getUserDetails(String accountNumber);
    AccountResponse getAccountDetails(String accountNumber);


}
