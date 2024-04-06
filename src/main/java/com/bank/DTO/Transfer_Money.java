package com.bank.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transfer_Money {
    private int id;
    private String sender_bank_account_number;
    private String receiver_bank_account_number;
    private BigDecimal money_amount;
    private int staff_id;
    private LocalDateTime send_date;

    public Transfer_Money() {
    }

    public Transfer_Money(int id, String sender_bank_account_number, String receiver_bank_account_number, BigDecimal money_amount, int staff_id, LocalDateTime send_date) {
        this.id = id;
        this.sender_bank_account_number = sender_bank_account_number;
        this.receiver_bank_account_number = receiver_bank_account_number;
        this.money_amount = money_amount;
        this.staff_id = staff_id;
        this.send_date = send_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender_bank_account_number() {
        return sender_bank_account_number;
    }

    public void setSender_bank_account_number(String sender_bank_account_number) {
        this.sender_bank_account_number = sender_bank_account_number;
    }

    public String getReceiver_bank_account_number() {
        return receiver_bank_account_number;
    }

    public void setReceiver_bank_account_number(String receiver_bank_account_number) {
        this.receiver_bank_account_number = receiver_bank_account_number;
    }

    public BigDecimal getMoney_amount() {
        return money_amount;
    }

    public void setMoney_amount(BigDecimal money_amount) {
        this.money_amount = money_amount;
    }

    public LocalDateTime getSend_date() {
        return send_date;
    }

    public void setSend_date(LocalDateTime send_date) {
        this.send_date = send_date;
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
                sender_bank_account_number + " | " +
                receiver_bank_account_number + " | " +
                money_amount + " | " +
                staff_id + " | " +
                send_date;
    }
}
