package com.bank.DAL;

import com.bank.DTO.Transaction_Deposit_Withdrawal;
import com.bank.DTO.Transaction_Deposit_Withdrawal;
import javafx.util.Pair;

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
                        "description",
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
                        new BigDecimal(row.get(4)).setScale(2), // money_amount
                        row.get(5),
                        Integer.parseInt(row.get(6)) //staff_id
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Transaction_Deposit_WithdrawalDAL.convertToTransaction_Deposit_Withdrawals(): " + e.getMessage());
            }
            return new Transaction_Deposit_Withdrawal();
        });
    }

    public int addTransaction_Deposit_Withdrawal(Transaction_Deposit_Withdrawal transaction_Deposit_Withdrawal) {
        int result = 0;
        try {
            result = Integer.parseInt(executeProcedure("sp_AddTransaction_Deposit_Withdrawal",
                    new Pair<>("id", transaction_Deposit_Withdrawal.getId()),
                    new Pair<>("bank_account_number", transaction_Deposit_Withdrawal.getBank_number_account()),
                    new Pair<>("transaction_type", transaction_Deposit_Withdrawal.getTransaction_type()),
                    new Pair<>("money_amount", transaction_Deposit_Withdrawal.getMoney_amount()),
                    new Pair<>("description", transaction_Deposit_Withdrawal.getDescription()),
                    new Pair<>("staff_id", transaction_Deposit_Withdrawal.getStaff_id()),
                    new Pair<>("datetime", transaction_Deposit_Withdrawal.getTransaction_date())
                    ).get(0).get(0));
            return result;
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Transaction_Deposit_WithdrawalDAL.addTransaction_Deposit_Withdrawal(): " + e.getMessage());
        }
        return result;
    }

    public int updateTransaction_Deposit_Withdrawal(Transaction_Deposit_Withdrawal transaction_Deposit_Withdrawal) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(transaction_Deposit_Withdrawal.getId());
            updateValues.add(transaction_Deposit_Withdrawal.getBank_number_account());
            updateValues.add(transaction_Deposit_Withdrawal.getTransaction_type());
            updateValues.add(transaction_Deposit_Withdrawal.getTransaction_date());
            updateValues.add(transaction_Deposit_Withdrawal.getMoney_amount());
            updateValues.add(transaction_Deposit_Withdrawal.getDescription());
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

    public List<List<String>> getTotalTransaction_By_Month_In_Year(String bank_account_number) {
        try {
            return executeProcedure("sp_GetTotalTransaction_By_Month_In_Year", new Pair<>("bank_account_number", bank_account_number));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Transfer_MoneyDAL.getTotalTransfer_By_Month_In_Year(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<List<String>> getTotalTransaction_By_Date(String bank_account_number, String start_date, String end_date) {
        try {
            return executeProcedure("sp_GetTotalTransaction_By_Date", new Pair<>("bank_account_number", bank_account_number), new Pair<>("start_date", start_date), new Pair<>("end_date", end_date));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Transfer_MoneyDAL.getTotalTransfer_By_Date(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<List<String>> getStatisticTotalTransaction() {
        try {
            return executeProcedure("sp_GetStatisticTotalTransaction");
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Transfer_MoneyDAL.getStatisticTotalTransaction(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
