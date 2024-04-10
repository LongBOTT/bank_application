package com.bank.GUI.DialogGUI.FormDetailGUI;

import com.bank.BLL.BranchBLL;
import com.bank.BLL.CustomerBLL;
import com.bank.DTO.Branch;
import com.bank.DTO.Bank_Account;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.DialogGUI.TransactionGUI;
import com.bank.GUI.HomeGUI;
import com.bank.GUI.components.MyTextFieldUnderLine;
import com.bank.main.Bank_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailBank_AccountGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeBank_Account;
    private List<JTextField> jTextFieldsBank_Account;
    private JDateChooser jDateChooser = new JDateChooser();
    private JButton buttonTransfer;
    private JButton buttonTransaction;

    public DetailBank_AccountGUI(Bank_Account Bank_Account) {
        super();
        super.setTitle("Thông Tin Tài khoản Ngân Hàng ");
        super.setSize(new Dimension(1000, 350));
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        init(Bank_Account);
        setVisible(true);
    }

    private void init(Bank_Account Bank_Account) {
        titleName = new JLabel();
        attributeBank_Account = new ArrayList<>();
        jTextFieldsBank_Account = new ArrayList<>();
        buttonTransfer = new JButton("Chuyển tiền");
        buttonTransaction = new JButton("Giao dịch");

        titleName.setText("Thông Tin Tài khoản Ngân Hàng");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);


        for (String string : new String[]{"Số Thẻ", "CCCD Chủ Thẻ", "Số Dư", "Chi Nhánh",
                "Ngày Mở", "Trạng Thái"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(150, 35));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 15)));
            attributeBank_Account.add(label);
            content.add(label);
            MyTextFieldUnderLine textField = new MyTextFieldUnderLine();
            textField.setPreferredSize(new Dimension(280, 35));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setEditable(false);
            if (string.trim().equals("Ngày Mở")) {
                Date birthDate = Bank_Account.getCreation_date();
                jDateChooser = new JDateChooser();
                jDateChooser.setDateFormatString("dd/MM/yyyy");
                jDateChooser.setDate(birthDate);
                jDateChooser.setPreferredSize(new Dimension(180, 35));
                jDateChooser.setMinSelectableDate(java.sql.Date.valueOf("1000-01-01"));
                jDateChooser.setEnabled(false);
                content.add(jDateChooser);
            } else {
                if (string.trim().equals("Số Thẻ")) {
                    String Bank_AccountId = Bank_Account.getNumber();
                    textField.setText(Bank_AccountId);
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("CCCD Chủ Thẻ")) {
                    JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    jPanel.setPreferredSize(new Dimension(280, 35));
                    jPanel.setBackground(Color.white);
                    content.add(jPanel, "wrap");

                    textField.setPreferredSize(new Dimension(215, 35));
                    textField.setText(Bank_Account.getCustomer_no());
                    jPanel.add(textField);

                    JLabel iconChangeRole = new JLabel(new FlatSVGIcon("icon/detail.svg"));
                    iconChangeRole.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    iconChangeRole.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            dispose();
                            new DetailCustomerGUI(new CustomerBLL().searchCustomers("[no] = '" + Bank_Account.getCustomer_no() + "'").get(0));
                        }
                    });
                    jPanel.add(iconChangeRole);
                    continue;
                }
                if (string.trim().equals("Số Dư")) {
                    textField.setText(String.valueOf(Bank_Account.getBalance()));
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("Chi Nhánh")) {
                    Branch branch = new BranchBLL().findAllBranchs("id", String.valueOf(Bank_Account.getBranch_id())).get(0);
                    textField.setText(branch.getName());
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Trạng Thái")) {
                    String status = Bank_Account.isStatus()? "Đang mở" : "Đã đóng";
                    textField.setText(status);
                    content.add(textField, "wrap");
                }
            }

        }
        buttonTransfer.setPreferredSize(new Dimension(150, 30));
        buttonTransfer.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonTransfer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonTransfer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dispose();
                new TransactionGUI(Bank_Account);
            }
        });
        containerButton.add(buttonTransfer);

        buttonTransaction.setPreferredSize(new Dimension(150, 30));
        buttonTransaction.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonTransaction.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        buttonTransaction.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                editStaff();
//            }
//        });
        containerButton.add(buttonTransaction);

    }
}

