package com.bank.DAL;

import com.bank.DTO.Bank_Account;
import com.bank.DTO.Customer;
import javafx.util.Pair;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Bank_AccountDAL extends Manager{
    public Bank_AccountDAL() {
        super("bank_account",
                List.of("number",
                        "customer_no",
                        "balance",
                        "branch_id",
                        "creation_date",
                        "status"));
    }

    public List<Bank_Account> convertToBank_Accounts(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Bank_Account(
                        row.get(0), // number
                        row.get(1), // customer_no
                        new BigDecimal(row.get(2)).setScale(2), // balance
                        Integer.parseInt(row.get(3)), // branch_id
                        Date.valueOf(row.get(4)), // creation_date
                        Boolean.parseBoolean(row.get(5)) // deleted
                );
            } catch (Exception e) {
                System.out.println("Error occurred in Bank_AccountDAL.convertToBank_Accounts(): " + e.getMessage());
            }
            return new Bank_Account();
        });
    }

    public int addBank_Account(Bank_Account bank_account) {
        try {
            return create(bank_account.getNumber(),
                    bank_account.getCustomer_no(),
                    bank_account.getBalance(),
                    bank_account.getBranch_id(),
                    bank_account.getCreation_date(),
                    true
            ); // bank_account khi tạo mặc định status = true (dang mo)
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Bank_AccountDAL.addBank_Account(): " + e.getMessage());
        }
        return 0;
    }

    public int updateBank_Account(Bank_Account bank_account) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(bank_account.getNumber());
            updateValues.add(bank_account.getCustomer_no());
            updateValues.add(bank_account.getBalance());
            updateValues.add(bank_account.getBranch_id());
            updateValues.add(bank_account.getCreation_date());
            updateValues.add(bank_account.isStatus());;
            return update(updateValues, "[number] = '" + bank_account.getNumber() + "'");
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Bank_AccountDAL.updateBank_Account(): " + e.getMessage());
        }
        return 0;
    }

    public int updateAllBank_Account(Bank_Account bank_account) {
        int result = 0;
        try {
            result = Integer.parseInt(executeProcedure("sp_UpdateBank_Account",
                    new Pair<>("number", bank_account.getNumber()),
                    new Pair<>("customer_no", bank_account.getCustomer_no()),
                    new Pair<>("balance", bank_account.getBalance()),
                    new Pair<>("branch_id", bank_account.getBranch_id()),
                    new Pair<>("creation_date", bank_account.getCreation_date()),
                    new Pair<>("status", bank_account.isStatus())).get(0).get(0));
            return result;
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in CustomerDAL.getAllCustomers(): " + e.getMessage());
        }
        return result;
    }

    public void closeBank_Accounts(String customer_no) {
        try {
            executeProcedure("sp_CloseBank_Accounts", new Pair<>("no", customer_no));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Bank_AccountDAL.closeBank_Accounts(): " + e.getMessage());
        }
    }

    public List<Bank_Account> searchBank_Accounts(String... conditions) {
        try {
            return convertToBank_Accounts(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Bank_AccountDAL.searchBank_Accounts(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<Bank_Account> getAllBank_Accounts() {
        try {
            return convertToBank_Accounts(
                    executeProcedure("sp_GetAllBank_Accounts"));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Bank_AccountDAL.getAllBank_Accounts(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<List<String>> getStatisticBank_Account(int branch_id) {
        try {
            return executeProcedure("sp_GetStatisticBank_Account", new Pair<>("branch_id", branch_id));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Bank_AccountDAL.GetStatisticBank_Account(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
