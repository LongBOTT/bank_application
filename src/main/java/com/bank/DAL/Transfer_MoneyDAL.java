package com.bank.DAL;

import com.bank.DTO.Transfer_Money;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transfer_MoneyDAL extends Manager{
    public Transfer_MoneyDAL() {
        super("transaction_deposit_withdrawal",
                List.of("id",
                        "sender_bank_account_number",
                        "receiver_bank_account_number",
                        "money_amount",
                        "staff_id",
                        "send_date"));
    }

    public List<Transfer_Money> convertToTransfer_Moneys(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Transfer_Money(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1), // sender_bank_account_number
                        row.get(2), // receiver_bank_account_number
                        BigDecimal.valueOf(Double.parseDouble(row.get(3))), // money_amount
                        Integer.parseInt(row.get(4)), //staff_id
                        LocalDateTime.parse(row.get(5)) // send_date
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Transfer_MoneyDAL.convertToTransfer_Moneys(): " + e.getMessage());
            }
            return new Transfer_Money();
        });
    }

    public int addTransfer_Money(Transfer_Money transfer_Money) {
        try {
            return create(transfer_Money.getId(),
                    transfer_Money.getSender_bank_account_number(),
                    transfer_Money.getReceiver_bank_account_number(),
                    transfer_Money.getMoney_amount(),
                    transfer_Money.getStaff_id(),
                    transfer_Money.getSend_date()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Transfer_MoneyDAL.addTransfer_Money(): " + e.getMessage());
        }
        return 0;
    }

    public int updateTransfer_Money(Transfer_Money transfer_Money) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(transfer_Money.getId());
            updateValues.add(transfer_Money.getSender_bank_account_number());
            updateValues.add(transfer_Money.getReceiver_bank_account_number());
            updateValues.add(transfer_Money.getMoney_amount());
            updateValues.add(transfer_Money.getStaff_id());
            updateValues.add(transfer_Money.getSend_date());
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
}
