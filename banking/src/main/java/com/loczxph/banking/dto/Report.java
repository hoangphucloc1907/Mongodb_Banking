package com.loczxph.banking.dto;

public class Report {
    private long totalWithdrawals;
    private long totalDeposits;

    public Report() {
    }

    public Report(long totalWithdrawals, long totalDeposits) {
        this.totalWithdrawals = totalWithdrawals;
        this.totalDeposits = totalDeposits;
    }

    public long getTotalWithdrawals() {
        return totalWithdrawals;
    }

    public void setTotalWithdrawals(long totalWithdrawals) {
        this.totalWithdrawals = totalWithdrawals;
    }

    public long getTotalDeposits() {
        return totalDeposits;
    }

    public void setTotalDeposits(long totalDeposits) {
        this.totalDeposits = totalDeposits;
    }
}
