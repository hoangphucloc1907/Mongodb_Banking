package com.loczxph.banking.dto;

import java.text.DecimalFormat;

public class AccountResponse {
    private String accountNumber;
    private double balance;

//    public String getFormattedBalance() {
//        DecimalFormat decimalFormat = new DecimalFormat("#.##");
//        return decimalFormat.format(balance);
//    }
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
