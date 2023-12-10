package com.loczxph.banking.service;

import com.loczxph.banking.model.Account;
import com.loczxph.banking.model.User;

public interface AccountService {
    public Account createAccount(User user);
    public boolean isPinCreated(String accountNumber);
    public void createPIN(String accountNumber, String password, String pin) ;
    public void updatePIN(String accountNumber, String oldPIN, String password, String newPIN);
    public void cashDeposit(String accountNumber, String pin, long amount);
    public void cashWithdrawal(String accountNumber, String pin, long amount);
    public void fundTransfer(String sourceAccountNumber, String targetAccountNumber, String pin, long amount);
}
