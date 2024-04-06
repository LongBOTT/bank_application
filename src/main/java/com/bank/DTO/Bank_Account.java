package com.bank.DTO;

import java.math.BigDecimal;
import java.util.Date;

public class Bank_Account {
    private String number;
    private String customer_no;
    private BigDecimal balance;
    private int branch_id;
    private Date creation_date;
    private boolean status;

    public Bank_Account() {
    }

    public Bank_Account(String number, String customer_no, BigDecimal balance, int branch_id, Date creation_date, boolean status) {
        this.number = number;
        this.customer_no = customer_no;
        this.balance = balance;
        this.branch_id = branch_id;
        this.creation_date = creation_date;
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCustomer_no() {
        return customer_no;
    }

    public void setCustomer_no(String customer_no) {
        this.customer_no = customer_no;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String status1 = status? "Đang mở" : "Đã khoá";
        return number + " | " +
                customer_no + " | " +
                balance + " | " +
                branch_id + " | " +
                creation_date + " | " +
                status1;
    }
}
