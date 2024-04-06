package com.bank.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction_Deposit_Withdrawal {
    private int id;
    private String bank_number_account;
    private String transaction_type;
    private LocalDateTime transaction_date;
    private BigDecimal money_amount;
    private int staff_id;

    public Transaction_Deposit_Withdrawal() {
    }

    public Transaction_Deposit_Withdrawal(int id, String bank_number_account, String transaction_type, LocalDateTime transaction_date, BigDecimal money_amount, int staff_id) {
        this.id = id;
        this.bank_number_account = bank_number_account;
        this.transaction_type = transaction_type;
        this.transaction_date = transaction_date;
        this.money_amount = money_amount;
        this.staff_id = staff_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBank_number_account() {
        return bank_number_account;
    }

    public void setBank_number_account(String bank_number_account) {
        this.bank_number_account = bank_number_account;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public LocalDateTime getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(LocalDateTime transaction_date) {
        this.transaction_date = transaction_date;
    }

    public BigDecimal getMoney_amount() {
        return money_amount;
    }

    public void setMoney_amount(BigDecimal money_amount) {
        this.money_amount = money_amount;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    @Override
    public String toString() {
        return id + " | " +
                bank_number_account + " | " +
                transaction_type + " | " +
                transaction_date + " | " +
                money_amount + " | " +
                staff_id;
    }
}
