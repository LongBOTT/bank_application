package com.bank.main;

import com.bank.BLL.Bank_AccountBLL;
import com.bank.BLL.CustomerBLL;
import com.bank.BLL.Transaction_Deposit_WithdrawalBLL;
import com.bank.BLL.Transfer_MoneyBLL;
import com.bank.DTO.Bank_Account;
import com.bank.DTO.Customer;
import com.bank.DTO.Transaction_Deposit_Withdrawal;
import com.bank.DTO.Transfer_Money;
import com.bank.GUI.DialogGUI.FormDetailGUI.DetailBank_AccountGUI;
import com.bank.GUI.HomeGUI;
import com.bank.GUI.LoginGUI;
import com.bank.GUI.StatementGUI;
import com.bank.GUI.components.Card;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class Bank_Application {
    public static LoginGUI loginGUI;
    public static HomeGUI homeGUI;

    public static void main(String[] args) {
//        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("themes");
//        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatIntelliJLaf.setup();

        UIManager.put("ProgressBar.selectionForeground", Color.black);
        UIManager.put("ProgressBar.selectionBackground", Color.black);
        UIManager.put("ScrollBar.trackArc", 999);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.trackInsets", new Insets(2, 4, 2, 4));
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        UIManager.put("ScrollBar.track", new Color(220, 221, 225, 255));
        UIManager.put("PasswordField.showRevealButton", true);
        UIManager.put("PasswordField.capsLockIcon", new FlatSVGIcon("icon/capslock.svg"));
        UIManager.put("TitlePane.iconSize", new Dimension(25, 25));
        UIManager.put("TitlePane.iconMargins", new Insets(3, 5, 0, 20));
        UIManager.put("TabbedPane.selectedBackground", Color.white);
        UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0));
//        UIManager.put("TabbedPane.tabInsets", new Insets(20, 20, 20, 20));
        UIManager.put("TabbedPane.selected", Color.RED);
        UIManager.put("TabbedPane.contentAreaColor", Color.GRAY);

        Thread thread = new Thread(() -> homeGUI = new HomeGUI());
        thread.start();
        loginGUI = new LoginGUI();
        loginGUI.setVisible(true);
//        new DetailBank_AccountGUI(new Bank_AccountBLL().bank_accountListAll.get(0));
//        new StatementGUI(new Bank_AccountBLL().getBank_accountListAll().get(2), new Card(new Bank_AccountBLL().getBank_accountListAll().get(2)));

//        taoDuLieuMau();
    }

    private static void taoDuLieuMau() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 5, 7, 0, 0);

        List<Bank_Account> bankAccountList = new Bank_AccountBLL().getBank_accountListAll();
        Transfer_MoneyBLL transferMoneyBLL = new Transfer_MoneyBLL();
        Transaction_Deposit_WithdrawalBLL transactionDepositWithdrawalBLL = new Transaction_Deposit_WithdrawalBLL();
        int id1 = 1;
        int id2 = 1;
        while (!start.equals(end)) {
            Random random = new Random();
            for (Bank_Account bankAccount : bankAccountList) {
                start = start.plusHours(1);
                int type =  random.nextInt(2);
                int amount_money = random.nextInt(100) * 1000 + 1000000;
                Transaction_Deposit_Withdrawal transaction_deposit_withdrawal = new Transaction_Deposit_Withdrawal(id1, bankAccount.getNumber(), type == 1, start, new BigDecimal(amount_money), type == 1 ? "Gui Tien" : "Rut Tien", 24);
                transactionDepositWithdrawalBLL.addTransaction_Deposit_Withdrawal(transaction_deposit_withdrawal);
                id1 += 1;
            }
            int i = 0;
            for (Bank_Account bankAccount : bankAccountList) {
                start = start.plusHours(1);
                int index;
                do {
                    index = random.nextInt(bankAccountList.size());
                } while (index == i);
                Bank_Account bank_accountReceipt = bankAccountList.get(index);
                int amount_money = random.nextInt(100) * 1000 + 1000000;
                Customer customer = new CustomerBLL().searchCustomers("[no] = '" + bankAccount.getCustomer_no() + "'").get(0);
                String normalized = Normalizer.normalize(customer.getName(), Normalizer.Form.NFD);
                Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
                String customerName = pattern.matcher(normalized).replaceAll("");
                Transfer_Money transferMoney = new Transfer_Money(id2, bankAccount.getNumber(), bank_accountReceipt.getNumber(), start, new BigDecimal(amount_money), customerName.toUpperCase() + " chuyen khoan ngan hang", 24);
                transferMoneyBLL.addTransfer_Money(transferMoney);
                i++;
                id2 += 1;
            }
            start = start.plusDays(1);
            start = start.withHour(0);
            start = start.withMinute(0);
            start = start.withSecond(0);
        }
    }

    public static void exit(int status) {
        System.exit(status);
    }
}
