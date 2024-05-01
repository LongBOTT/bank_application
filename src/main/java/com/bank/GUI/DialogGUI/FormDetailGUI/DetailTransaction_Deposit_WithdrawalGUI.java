package com.bank.GUI.DialogGUI.FormDetailGUI;

import com.bank.BLL.*;
import com.bank.DTO.*;
import com.bank.GUI.CustomerGUI;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.components.MyTextFieldUnderLine;
import com.bank.GUI.components.RoundedPanel;
import com.bank.main.Bank_Application;
import com.bank.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DetailTransaction_Deposit_WithdrawalGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeTransaction_Deposit_Withdrawal;
    private List<JTextField> jTextFieldsTransaction_Deposit_Withdrawal;

    public DetailTransaction_Deposit_WithdrawalGUI(Transaction_Deposit_Withdrawal transaction_Deposit_Withdrawal) {
        super();
        super.setTitle("Thông Tin Phiếu Giao Dịch");
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        init(transaction_Deposit_Withdrawal);
        setVisible(true);
    }

    private void init(Transaction_Deposit_Withdrawal transaction_Deposit_Withdrawal) {
        titleName = new JLabel();
        attributeTransaction_Deposit_Withdrawal = new ArrayList<>();
        jTextFieldsTransaction_Deposit_Withdrawal = new ArrayList<>();

        titleName.setText("Thông Tin Phiếu Giao Dịch");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);


        for (String string : new String[]{"Mã Giao Dịch", "Số Tài Khoản", "Loại Giao Dịch", "Thời Gian Giao Dịch",
                "Số Tiền", "Mã Nhân Viên", "Nội Dung"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(150, 35));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 15)));
            attributeTransaction_Deposit_Withdrawal.add(label);
            content.add(label);
            MyTextFieldUnderLine textField = new MyTextFieldUnderLine();
            textField.setPreferredSize(new Dimension(280, 35));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setEditable(false);
            if (string.trim().equals("Thời Gian Giao Dịch")) {
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                textField.setText(transaction_Deposit_Withdrawal.getTransaction_date().format(myFormatObj));
                content.add(textField, "wrap");
                continue;
            }
            if (string.trim().equals("Số Tài Khoản")) {
                JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                jPanel.setPreferredSize(new Dimension(280, 35));
                jPanel.setBackground(Color.white);
                content.add(jPanel, "wrap");

                textField.setPreferredSize(new Dimension(210, 35));
                textField.setText(transaction_Deposit_Withdrawal.getBank_number_account());
                jPanel.add(textField);

                JLabel iconChangeRole = new JLabel(new FlatSVGIcon("icon/detail.svg"));
                iconChangeRole.setCursor(new Cursor(Cursor.HAND_CURSOR));
                iconChangeRole.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        dispose();
                        Bank_Account bankAccount = new Bank_AccountBLL().findAllBank_Accounts("number", transaction_Deposit_Withdrawal.getBank_number_account()).get(0);
                        for (Pair pair : CustomerGUI.pairList) {
                            Customer customer = (Customer) pair.getKey();
                            if (Objects.equals(customer.getCustomerNo(), bankAccount.getCustomer_no())) {
                                new DetailCustomerGUI(pair); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá
                                break;
                            }
                        }
                    }
                });
                jPanel.add(iconChangeRole);
                continue;
            }
            if (string.trim().equals("Loại Giao Dịch")) {
                textField.setText(transaction_Deposit_Withdrawal.getTransaction_type() ? "Gửi Tiền" : "Rút Tiền");
                content.add(textField);
                continue;
            }
            if (string.trim().equals("Mã Giao Dịch")) {
                textField.setText(String.valueOf(transaction_Deposit_Withdrawal.getId()));
                content.add(textField);
                continue;
            }
            if (string.trim().equals("Số Tiền")) {
                textField.setText(VNString.currency(Double.parseDouble(transaction_Deposit_Withdrawal.getMoney_amount().toString())));
                content.add(textField);
                continue;
            }
            if (string.trim().equals("Mã Nhân Viên")) {
                textField.setText(String.valueOf(transaction_Deposit_Withdrawal.getStaff_id()));
                content.add(textField, "wrap");
                continue;
            }
            if (string.trim().equals("Nội Dung")) {
                textField.setPreferredSize(new Dimension(700, 35));
                textField.setText(transaction_Deposit_Withdrawal.getDescription());
                content.add(textField, "span, wrap");
            }
        }

        RoundedPanel roundedPanel = new RoundedPanel();
        roundedPanel.setLayout(new GridBagLayout());
        roundedPanel.setPreferredSize(new Dimension(170, 40));
        roundedPanel.setBackground(new Color(1, 120, 220));
        roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        containerButton.add(roundedPanel);

        JLabel panel = new JLabel("In Phiếu Giao Dịch");
        panel.setFont(new Font("inter", Font.BOLD, 13));
        panel.setIcon(new FlatSVGIcon("icon/print.svg"));
        panel.setForeground(Color.white);
        roundedPanel.add(panel);
    }
}

