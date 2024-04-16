package com.bank.DAL;

import com.bank.DTO.Transfer_Money;
import javafx.util.Pair;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Transfer_MoneyDAL extends Manager{
    public Transfer_MoneyDAL() {
        super("transfer_money",
                List.of("id",
                        "sender_bank_account_number",
                        "receiver_bank_account_number",
                        "send_date",
                        "money_amount",
                        "description",
                        "staff_id"));
    }

    public List<Transfer_Money> convertToTransfer_Moneys(List<List<String>> data) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        return convert(data, row -> {
            try {
                return new Transfer_Money(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1), // sender_bank_account_number
                        row.get(2), // receiver_bank_account_number
                        LocalDateTime.parse(row.get(3), myFormatObj), // send_date
                        new BigDecimal(row.get(4)).setScale(2), // money_amount
                        row.get(5), // description
                        Integer.parseInt(row.get(6)) //staff_id
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Transfer_MoneyDAL.convertToTransfer_Moneys(): " + e.getMessage());
            }
            return new Transfer_Money();
        });
    }

    public int addTransfer_Money(Transfer_Money transfer_Money) {
        int result = 0;
        try {
            result = Integer.parseInt(executeProcedure("sp_AddTransfer_Money",
                    new Pair<>("id", transfer_Money.getId()),
                    new Pair<>("sender_bank_account_number", transfer_Money.getSender_bank_account_number()),
                    new Pair<>("receiver_bank_account_number", transfer_Money.getReceiver_bank_account_number()),
                    new Pair<>("money_amount", transfer_Money.getMoney_amount()),
                    new Pair<>("description", transfer_Money.getDescription()),
                    new Pair<>("staff_id", transfer_Money.getStaff_id())
            ).get(0).get(0));
            return result;
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Transfer_MoneyDAL.addTransfer_Money(): " + e.getMessage());
        }
        return result;
    }

    public int updateTransfer_Money(Transfer_Money transfer_Money) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(transfer_Money.getId());
            updateValues.add(transfer_Money.getSender_bank_account_number());
            updateValues.add(transfer_Money.getReceiver_bank_account_number());
            updateValues.add(transfer_Money.getSend_date());
            updateValues.add(transfer_Money.getMoney_amount());
            updateValues.add(transfer_Money.getDescription());
            updateValues.add(transfer_Money.getStaff_id());
            return update(updateValues, "[id] = " + transfer_Money.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Transfer_MoneyDAL.updateTransfer_Money(): " + e.getMessage());
        }
        return 0;
    }

    public List<Transfer_Money> searchTransfer_Moneys(String... conditions) {
        try {
            return convertToTransfer_Moneys(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Transfer_MoneyDAL.searchTransfer_Moneys(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Transfer_Money> getAllTransfer_Money() {
        try {
            return convertToTransfer_Moneys(
                    executeProcedure("sp_GetAllTransfer_Money"));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Transfer_MoneyDAL.getAllTransfer_Money(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<List<String>> getTotalTransfer_By_Month_In_Year(String bank_account_number) {
        try {
            return executeProcedure("sp_GetTotalTransfer_By_Month_In_Year", new Pair<>("bank_account_number", bank_account_number));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Transfer_MoneyDAL.getTotalTransfer_By_Month_In_Year(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
