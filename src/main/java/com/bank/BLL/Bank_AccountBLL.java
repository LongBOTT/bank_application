package com.bank.BLL;

import com.bank.DAL.Bank_AccountDAL;
import com.bank.DTO.Bank_Account;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Bank_AccountBLL extends Manager<Bank_Account>{
    private Bank_AccountDAL Bank_AccountDAL;
    public List<Bank_Account> bank_accountListAll;

    public Bank_AccountBLL() {
        Bank_AccountDAL = new Bank_AccountDAL();
        bank_accountListAll = Bank_AccountDAL.getAllBank_Accounts();
    }

    public List<Bank_Account> getBank_accountListAll() {
        return bank_accountListAll;
    }

    public void setBank_accountListAll(List<Bank_Account> bank_accountListAll) {
        this.bank_accountListAll = bank_accountListAll;
    }

    public Bank_AccountDAL getBank_AccountDAL() {
        return Bank_AccountDAL;
    }

    public void setBank_AccountDAL(Bank_AccountDAL Bank_AccountDAL) {
        this.Bank_AccountDAL = Bank_AccountDAL;
    }

    public Object[][] getData() {
        return getData(Bank_AccountDAL.searchBank_Accounts());
    }

    public Pair<Boolean, String> addBank_Account(Bank_Account Bank_Account) {
        Pair<Boolean, String> result = checkBank_AccountAll(Bank_Account);

        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        if (Bank_AccountDAL.addBank_Account(Bank_Account) == 0)
            return new Pair<>(false, "Thêm tài khoản ngân hàng không thành công.");

        return new Pair<>(true, "Thêm tài khoản ngân hàng thành công.");
    }

    public Pair<Boolean, String> updateBank_Account(Bank_Account Bank_Account) {
        if (Bank_AccountDAL.updateBank_Account(Bank_Account) == 0)
            return new Pair<>(false, "Cập nhật tài khoản ngân hàng không thành công.");

        return new Pair<>(true, "Cập nhật tài khoản ngân hàng thành công.");
    }

    public List<Bank_Account> searchBank_Accounts(String... conditions) {
        return Bank_AccountDAL.searchBank_Accounts(conditions);
    }

    public List<Bank_Account> findBank_Accounts(String key, String value) {
        List<Bank_Account> list = new ArrayList<>();
        List<Bank_Account> Bank_AccountList = Bank_AccountDAL.searchBank_Accounts();
        for (Bank_Account Bank_Account : Bank_AccountList) {
            if (getValueByKey(Bank_Account, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(Bank_Account);
            }
        }
        return list;
    }

    public List<Bank_Account> findAllBank_Accounts(String key, String value) {
        List<Bank_Account> list = new ArrayList<>();
        List<Bank_Account> Bank_AccountList = bank_accountListAll;
        for (Bank_Account Bank_Account : Bank_AccountList) {
            if (getValueByKey(Bank_Account, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(Bank_Account);
            }
        }
        return list;
    }

//    public List<Bank_Account> findBank_AccountsBy(Map<String, Object> conditions) {
//        List<Bank_Account> Bank_Accounts = Bank_AccountDAL.searchBank_Accounts();
//        for (Map.Entry<String, Object> entry : conditions.entrySet())
//            Bank_Accounts = findObjectsBy(entry.getKey(), entry.getValue(), Bank_Accounts);
//        return Bank_Accounts;
//    }
    public  Pair<Boolean, String> checkBank_AccountAll(Bank_Account Bank_Account) {
        Pair<Boolean, String> result;

        result = exists(Bank_Account);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        return new Pair<>(true,"");

    }

    public Pair<Boolean, String> exists(Bank_Account Bank_Account) {
        List<Bank_Account> Bank_Accounts = searchBank_Accounts("[number] = '" + Bank_Account.getNumber() + "'");

        if(!Bank_Accounts.isEmpty()){
            return new Pair<>(true, "Tài khoản ngân hàng đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    public List<Bank_Account> getALLBank_Accounts() {
        return Bank_AccountDAL.getAllBank_Accounts();
    }

    @Override
    public Object getValueByKey(Bank_Account Bank_Account, String key) {
        return switch (key) {
            case "number" -> Bank_Account.getNumber();
            case "customer_no" -> Bank_Account.getCustomer_no();
            case "balance" -> Bank_Account.getBalance();
            case "branch_id" -> Bank_Account.getBranch_id();
            case "creation_date" -> Bank_Account.getCreation_date();
            case "status" -> Bank_Account.isStatus();
            default -> null;
        };
    }

    public int getAutoID() {
        return Bank_AccountDAL.getAutoID();
    }

    public String getAutoNumber() {
        boolean flag = true;
        String number = "";
        while (flag) {
            int min = 100000000;
            int max = 999999999;
            number =  Integer.toString((int) ((Math.random() * (max - min)) + min));
            if (findAllBank_Accounts("number", number).isEmpty())
                flag = false;
        }
        return number;
    }
}
