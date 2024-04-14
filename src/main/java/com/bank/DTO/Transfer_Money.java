package com.bank.DTO;

import com.bank.utils.VNString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transfer_Money {
    private int id;
    private String sender_bank_account_number;
    private String receiver_bank_account_number;
    private LocalDateTime send_date;
    private BigDecimal money_amount;
    private String description;
    private int staff_id;

    public Transfer_Money() {
    }

    public Transfer_Money(int id, String sender_bank_account_number, String receiver_bank_account_number, LocalDateTime send_date, BigDecimal money_amount, String description, int staff_id) {
        this.id = id;
        this.sender_bank_account_number = sender_bank_account_number;
        this.receiver_bank_account_number = receiver_bank_account_number;
        this.send_date = send_date;
        this.money_amount = money_amount;
        this.description = description;
        this.staff_id = staff_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        return id + " | " +
                sender_bank_account_number + " | " +
                receiver_bank_account_number + " | " +
                VNString.currency(Double.parseDouble(money_amount.toString())) + " | " +
                send_date.format(myFormatObj)  + " | " +
                (description.isEmpty() ? " " : description);
    }
}
