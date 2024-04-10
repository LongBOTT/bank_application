package com.bank.DAL;

import com.bank.DTO.Transaction_Deposit_Withdrawal;
import com.bank.DTO.Transaction_Deposit_Withdrawal;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Transaction_Deposit_WithdrawalDAL extends Manager{
    public Transaction_Deposit_WithdrawalDAL() {
        super("transaction_deposit_withdrawal",
                List.of("id",
                        "bank_account_number",
                        "transaction_type",
                        "transaction_date",
                        "money_amount",
                        "staff_id"));
    }

    public List<Transaction_Deposit_Withdrawal> convertToTransaction_Deposit_Withdrawals(List<List<String>> data) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        return convert(data, row -> {
            try {
                return new Transaction_Deposit_Withdrawal(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1), // bank_account_number
                        Boolean.parseBoolean(row.get(2)), // transaction_type
                        LocalDateTime.parse(row.get(3), myFormatObj), // transaction_date
                        new BigDecimal(row.get(4)), // money_amount
                        Integer.parseInt(row.get(5)) //staff_id
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Transaction_Deposit_WithdrawalDAL.convertToTransaction_Deposit_Withdrawals(): " + e.getMessage());
            }
            return new Transaction_Deposit_Withdrawal();
        });
    }

    public int addTransaction_Deposit_Withdrawal(Transaction_Deposit_Withdrawal transaction_Deposit_Withdrawal) {
        try {
            return create(transaction_Deposit_Withdrawal.getId(),
                    transaction_Deposit_Withdrawal.getBank_number_account(),
                    transaction_Deposit_Withdrawal.getTransaction_type(),
                    transaction_Deposit_Withdrawal.getTransaction_date(),
                    transaction_Deposit_Withdrawal.getMoney_amount(),
                    transaction_Deposit_Withdrawal.getStaff_id()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Transaction_Deposit_WithdrawalDAL.addTransaction_Deposit_Withdrawal(): " + e.getMessage());
        }
        return 0;
    }

    public int updateTransaction_Deposit_Withdrawal(Transaction_Deposit_Withdrawal transaction_Deposit_Withdrawal) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(transaction_Deposit_Withdrawal.getId());
            updateValues.add(transaction_Deposit_Withdrawal.getBank_number_account());
            updateValues.add(transaction_Deposit_Withdrawal.getTransaction_type());
            updateValues.add(transaction_Deposit_Withdrawal.getTransaction_date());
            updateValues.add(transaction_Deposit_Withdrawal.getMoney_amount());
            updateValues.add(transaction_Deposit_Withdrawal.getStaff_id());
            return update(updateValues, "[id] = " + transaction_Deposit_Withdrawal.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Transaction_Deposit_WithdrawalDAL.updateTransaction_Deposit_Withdrawal(): " + e.getMessage());
        }
        return 0;
    }

    public List<Transaction_Deposit_Withdrawal> searchTransaction_Deposit_Withdrawals(String... conditions) {
        try {
            return convertToTransaction_Deposit_Withdrawals(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Transaction_Deposit_WithdrawalDAL.searchTransaction_Deposit_Withdrawals(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Transaction_Deposit_Withdrawal> getAllTransaction_Deposit_Withdrawal() {
        try {
            return convertToTransaction_Deposit_Withdrawals(
                    executeProcedure("sp_GetAllTransaction_Deposit_Withdrawal"));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Transaction_Deposit_WithdrawalDAL.getAllTransaction_Deposit_Withdrawal(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
