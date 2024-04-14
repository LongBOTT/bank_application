package com.bank.GUI.DialogGUI.FormDetailGUI;

import com.bank.BLL.Bank_AccountBLL;
import com.bank.DTO.Bank_Account;
import com.bank.DTO.Customer;
import com.bank.DTO.Transfer_Money;
import com.bank.GUI.CustomerGUI;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.components.MyTextFieldUnderLine;
import com.bank.GUI.components.RoundedPanel;
import com.bank.main.Bank_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.toedter.calendar.JDateChooser;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailTransfer_MoneyGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeTransfer_Money;
    private List<JTextField> jTextFieldsTransfer_Money;
    public DetailTransfer_MoneyGUI(Transfer_Money transfer_Money) {
        super();
        super.setTitle("Thông Tin Phiếu Chuyển Tiền");
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        init(transfer_Money);
        setVisible(true);
    }

    private void init(Transfer_Money transfer_Money) {
        titleName = new JLabel();
        attributeTransfer_Money = new ArrayList<>();
        jTextFieldsTransfer_Money = new ArrayList<>();

        titleName.setText("Thông Tin Phiếu Chuyển Tiền");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);


        for (String string : new String[]{"Mã Chuyển Tiền", "Thời Gian Chuyển Tiền", "Số Tài Khoản Gửi", "Số Tài Khoản Nhận",
                "Số Tiền", "Mã Nhân Viên", "Nội Dung"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(150, 35));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 15)));
            attributeTransfer_Money.add(label);
            content.add(label);
            MyTextFieldUnderLine textField = new MyTextFieldUnderLine();
            textField.setPreferredSize(new Dimension(280, 35));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setEditable(false);
            if (string.trim().equals("Thời Gian Chuyển Tiền")) {
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                textField.setText(transfer_Money.getSend_date().format(myFormatObj));
                content.add(textField, "wrap");
                continue;
            }
            if (string.trim().equals("Số Tài Khoản Gửi")) {
                JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                jPanel.setPreferredSize(new Dimension(280, 35));
                jPanel.setBackground(Color.white);
                content.add(jPanel);

                textField.setPreferredSize(new Dimension(195, 35));
                textField.setText(transfer_Money.getSender_bank_account_number());
                jPanel.add(textField);

                JLabel iconChangeRole = new JLabel(new FlatSVGIcon("icon/detail.svg"));
                iconChangeRole.setCursor(new Cursor(Cursor.HAND_CURSOR));
                iconChangeRole.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        dispose();
                        Bank_Account bankAccount = new Bank_AccountBLL().findAllBank_Accounts("number", transfer_Money.getSender_bank_account_number()).get(0);
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
            if (string.trim().equals("Số Tài Khoản Nhận")) {
                JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                jPanel.setPreferredSize(new Dimension(280, 35));
                jPanel.setBackground(Color.white);
                content.add(jPanel, "wrap");

                textField.setPreferredSize(new Dimension(195, 35));
                textField.setText(transfer_Money.getReceiver_bank_account_number());
                jPanel.add(textField);

                JLabel iconChangeRole = new JLabel(new FlatSVGIcon("icon/detail.svg"));
                iconChangeRole.setCursor(new Cursor(Cursor.HAND_CURSOR));
                iconChangeRole.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        dispose();
                        Bank_Account bankAccount = new Bank_AccountBLL().findAllBank_Accounts("number", transfer_Money.getReceiver_bank_account_number()).get(0);
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
            if (string.trim().equals("Mã Chuyển Tiền")) {
                textField.setText(String.valueOf(transfer_Money.getId()));
                content.add(textField);
                continue;
            }
            if (string.trim().equals("Số Tiền")) {
                textField.setText(transfer_Money.getMoney_amount().toString());
                content.add(textField);
                continue;
            }
            if (string.trim().equals("Mã Nhân Viên")) {
                textField.setText(String.valueOf(transfer_Money.getStaff_id()));
                content.add(textField, "wrap");
                continue;
            }
            if (string.trim().equals("Nội Dung")) {
                textField.setPreferredSize(new Dimension(700, 35));
                textField.setText(transfer_Money.getDescription());
                content.add(textField, "span, wrap");
            }
        }

        RoundedPanel roundedPanel = new RoundedPanel();
        roundedPanel.setLayout(new GridBagLayout());
        roundedPanel.setPreferredSize(new Dimension(185, 40));
        roundedPanel.setBackground(new Color(1, 120, 220));
        roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        containerButton.add(roundedPanel);

        JLabel panel = new JLabel("In Phiếu Chuyển Tiền");
        panel.setFont(new Font("inter", Font.BOLD, 13));
        panel.setIcon(new FlatSVGIcon("icon/print.svg"));
        panel.setForeground(Color.white);
        roundedPanel.add(panel);
    }
}

