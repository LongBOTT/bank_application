package com.bank.BLL;

import com.bank.DAL.Transfer_MoneyDAL;
import com.bank.DTO.Transfer_Money;
import com.bank.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Transfer_MoneyBLL extends Manager<Transfer_Money>{
    private Transfer_MoneyDAL transfer_MoneyDAL;
    private List<Transfer_Money> transfer_moneyListAll;
    public Transfer_MoneyBLL() {
        transfer_MoneyDAL = new Transfer_MoneyDAL();
        transfer_moneyListAll = transfer_MoneyDAL.getAllTransfer_Money();
    }

    public List<Transfer_Money> getTransfer_moneyListAll() {
        return transfer_moneyListAll;
    }

    public void setTransfer_moneyListAll(List<Transfer_Money> transfer_moneyListAll) {
        this.transfer_moneyListAll = transfer_moneyListAll;
    }

    public Transfer_MoneyDAL getTransfer_MoneyDAL() {
        return transfer_MoneyDAL;
    }

    public void setTransfer_MoneyDAL(Transfer_MoneyDAL transfer_MoneyDAL) {
        this.transfer_MoneyDAL = transfer_MoneyDAL;
    }

    public Object[][] getData() {
        return getData(transfer_MoneyDAL.searchTransfer_Moneys());
    }

    public Pair<Boolean, String> addTransfer_Money(Transfer_Money transfer_Money) {
        Pair<Boolean ,String> result ;
        result = validateMoney_Amount(String.valueOf(transfer_Money.getMoney_amount()));
        if(!result.getKey())
            return new Pair<>(false,result.getValue());

        if (transfer_MoneyDAL.addTransfer_Money(transfer_Money) == 0)
            return new Pair<>(false, "Chuyển tiền không thành công.");

        return new Pair<>(true, "Chuyển tiền thành công.");
    }

    public List<Transfer_Money> searchTransfer_Moneys(String... conditions) {
        return transfer_MoneyDAL.searchTransfer_Moneys(conditions);
    }

    public List<Transfer_Money> findTransfer_Moneys(String key, String value) {
        List<Transfer_Money> list = new ArrayList<>();
        List<Transfer_Money> transfer_MoneyList = transfer_MoneyDAL.searchTransfer_Moneys();
        for (Transfer_Money transfer_Money : transfer_MoneyList) {
            if (getValueByKey(transfer_Money, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(transfer_Money);
            }
        }
        return list;
    }

    public List<Transfer_Money> findAllTransfer_Moneys(String key, String value) {
        List<Transfer_Money> list = new ArrayList<>();
        List<Transfer_Money> Transfer_MoneyList = transfer_moneyListAll;
        for (Transfer_Money Transfer_Money : Transfer_MoneyList) {
            if (getValueByKey(Transfer_Money, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(Transfer_Money);
            }
        }
        return list;
    }

//    public List<Transfer_Money> findTransfer_MoneysBy(Map<String, Object> conditions) {
//        List<Transfer_Money> transfer_Moneys = transfer_MoneyDAL.searchTransfer_Moneys();
//        for (Map.Entry<String, Object> entry : conditions.entrySet())
//            transfer_Moneys = findObjectsBy(entry.getKey(), entry.getValue(), transfer_Moneys);
//        return transfer_Moneys;
//    }

    public  Pair<Boolean, String> validateMoney_Amount(String money_amount){
        if (money_amount.isBlank())
            return new Pair<>(false, "Tiền giao dịch không được để trống.");
        if (!VNString.checkUnsignedNumber(money_amount))
            return new Pair<>(false, "Tiền giao dịch phải là số lơn hơn 0");
        return new Pair<>(true, money_amount);
    }

    public List<Transfer_Money> getALLTransfer_Moneys() {
        return transfer_MoneyDAL.getAllTransfer_Money();
    }

    public List<List<String>> getTotalTransfer_By_Month_In_Year(String bank_account_number) {
        return transfer_MoneyDAL.getTotalTransfer_By_Month_In_Year(bank_account_number);
    }

    public List<List<String>> getTotalTransfer_By_Date(String bank_account_number, String start_date, String end_date) {
        return transfer_MoneyDAL.getTotalTransfer_By_Date(bank_account_number, start_date, end_date);
    }

    public List<List<String>> getTotalTransfer_In_Month(String bank_account_number, String start_date, String end_date) {
        return transfer_MoneyDAL.getTotalTransfer_In_Month(bank_account_number, start_date, end_date);
    }


    public List<List<String>> getStatisticTotalTransfer(){
        return transfer_MoneyDAL.getStatisticTotalTransfer();
    }

    @Override
    public Object getValueByKey(Transfer_Money transfer_Money, String key) {
        return switch (key) {
            case "id" -> transfer_Money.getId();
            case "sender_bank_account_number" -> transfer_Money.getSender_bank_account_number();
            case "receiver_bank_account_number" -> transfer_Money.getReceiver_bank_account_number();
            case "send_date" -> transfer_Money.getSend_date();
            case "money_amount" -> transfer_Money.getMoney_amount();
            case "description" -> transfer_Money.getDescription();
            case "staff_id" -> transfer_Money.getStaff_id();
            default -> null;
        };
    }

    public int getAutoID() {
        return transfer_MoneyDAL.getAutoID();
    }
}
