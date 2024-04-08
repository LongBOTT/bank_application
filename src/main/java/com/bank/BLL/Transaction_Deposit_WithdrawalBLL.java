package com.bank.BLL;

import com.bank.DAL.Transaction_Deposit_WithdrawalDAL;
import com.bank.DTO.Transaction_Deposit_Withdrawal;
import com.bank.utils.VNString;
import javafx.util.Pair;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Transaction_Deposit_WithdrawalBLL extends Manager<Transaction_Deposit_Withdrawal>{
    private Transaction_Deposit_WithdrawalDAL transaction_deposit_withdrawalDAL;

    public Transaction_Deposit_WithdrawalBLL() {
        transaction_deposit_withdrawalDAL = new Transaction_Deposit_WithdrawalDAL();
    }

    public Transaction_Deposit_WithdrawalDAL getTransaction_Deposit_WithdrawalDAL() {
        return transaction_deposit_withdrawalDAL;
    }

    public void setTransaction_Deposit_WithdrawalDAL(Transaction_Deposit_WithdrawalDAL transaction_deposit_withdrawalDAL) {
        this.transaction_deposit_withdrawalDAL = transaction_deposit_withdrawalDAL;
    }

    public Object[][] getData() {
        return getData(transaction_deposit_withdrawalDAL.searchTransaction_Deposit_Withdrawals());
    }

    public Pair<Boolean, String> addTransaction_Deposit_Withdrawal(Transaction_Deposit_Withdrawal transaction_deposit_withdrawal) {
        Pair<Boolean ,String> result ;
        result = validateMoney_Amount(String.valueOf(transaction_deposit_withdrawal.getMoney_amount()));
        if(!result.getKey())
            return new Pair<>(false,result.getValue());

        if (transaction_deposit_withdrawalDAL.addTransaction_Deposit_Withdrawal(transaction_deposit_withdrawal) == 0)
            return new Pair<>(false, "Thêm giao dịch không thành công.");

        return new Pair<>(true, "Thêm giao dịch thành công.");
    }

    public List<Transaction_Deposit_Withdrawal> searchTransaction_Deposit_Withdrawals(String... conditions) {
        return transaction_deposit_withdrawalDAL.searchTransaction_Deposit_Withdrawals(conditions);
    }

    public List<Transaction_Deposit_Withdrawal> findTransaction_Deposit_Withdrawals(String key, String value) {
        List<Transaction_Deposit_Withdrawal> list = new ArrayList<>();
        List<Transaction_Deposit_Withdrawal> transaction_deposit_withdrawalList = transaction_deposit_withdrawalDAL.searchTransaction_Deposit_Withdrawals();
        for (Transaction_Deposit_Withdrawal transaction_deposit_withdrawal : transaction_deposit_withdrawalList) {
            if (getValueByKey(transaction_deposit_withdrawal, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(transaction_deposit_withdrawal);
            }
        }
        return list;
    }

//    public List<Transaction_Deposit_Withdrawal> findTransaction_Deposit_WithdrawalsBy(Map<String, Object> conditions) {
//        List<Transaction_Deposit_Withdrawal> transaction_deposit_withdrawals = transaction_deposit_withdrawalDAL.searchTransaction_Deposit_Withdrawals();
//        for (Map.Entry<String, Object> entry : conditions.entrySet())
//            transaction_deposit_withdrawals = findObjectsBy(entry.getKey(), entry.getValue(), transaction_deposit_withdrawals);
//        return transaction_deposit_withdrawals;
//    }

    public  Pair<Boolean, String> validateMoney_Amount(String money_amount){
        if (money_amount.isBlank())
            return new Pair<>(false, "Tiền giao dịch không được để trống.");
        if (!VNString.checkUnsignedNumber(money_amount))
            return new Pair<>(false, "Tiền giao dịch phải là số lơn hơn 0");
        return new Pair<>(true, money_amount);
    }

    @Override
    public Object getValueByKey(Transaction_Deposit_Withdrawal transaction_deposit_withdrawal, String key) {
        return switch (key) {
            case "id" -> transaction_deposit_withdrawal.getId();
            case "bank_account_number" -> transaction_deposit_withdrawal.getBank_number_account();
            case "transaction_type" -> transaction_deposit_withdrawal.getTransaction_type();
            case "transaction_date" -> transaction_deposit_withdrawal.getTransaction_date();
            case "money_amount" -> transaction_deposit_withdrawal.getMoney_amount();
            case "staff_id" -> transaction_deposit_withdrawal.getStaff_id();
            default -> null;
        };
    }

    public int getAutoID() {
        return transaction_deposit_withdrawalDAL.getAutoID();
    }
}
