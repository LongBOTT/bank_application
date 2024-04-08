package com.bank.GUI.DialogGUI.FormEditGUI;

import com.bank.BLL.BranchBLL;
import com.bank.BLL.RoleBLL;
import com.bank.BLL.Role_DetailBLL;
import com.bank.BLL.CustomerBLL;
import com.bank.DTO.Branch;
import com.bank.DTO.Role;
import com.bank.DTO.Role_Detail;
import com.bank.DTO.Customer;
import com.bank.GUI.ChangeRoleGUI;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.HomeGUI;
import com.bank.GUI.components.MyTextFieldUnderLine;
import com.bank.main.Bank_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.toedter.calendar.JDateChooser;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditCustomerGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeCustomer;
    private JButton buttonCancel;
    private JButton buttonEdit;
    private CustomerBLL customerBLL = new CustomerBLL();
    private List<JTextField> jTextFieldsCustomer;
    public static JTextField textFieldRole;
    public static boolean changeRole = false;
    private JDateChooser jDateChooser = new JDateChooser();
    private Customer customer;

    public EditCustomerGUI(Customer customer) {
        super();
        super.setTitle("Cập Nhật Thông Tin Khách Hàng");
        this.customer = customer;
        super.setSize(new Dimension(1000, 450));
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        init(customer);
        setVisible(true);
    }

    private void init(Customer customer) {
        titleName = new JLabel();
        attributeCustomer = new ArrayList<>();
        jTextFieldsCustomer = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonEdit = new JButton("Cập nhật");

        titleName.setText("Cập Nhật Thông Tin Khách Hàng");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Tên Khách Hàng", "CCCD", "Giới Tính",
                "Ngày Sinh", "Số Điện Thoại", "Địa Chỉ", "Email"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(150, 35));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 15)));
            attributeCustomer.add(label);
            content.add(label);

            MyTextFieldUnderLine textField = new MyTextFieldUnderLine();
            textField.setPreferredSize(new Dimension(280, 35));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));

            if (string.trim().equals("Ngày Sinh")) {
                Date birthDate = customer.getBirthdate();
                jDateChooser = new JDateChooser();
                jDateChooser.setDateFormatString("dd/MM/yyyy");
                jDateChooser.setDate(birthDate);
                jDateChooser.setPreferredSize(new Dimension(180, 35));
                jDateChooser.setMinSelectableDate(java.sql.Date.valueOf("1000-01-01"));
                jDateChooser.setEnabled(false);
                content.add(jDateChooser, "wrap");
            } else {
                if (string.trim().equals("Tên Khách Hàng")) {
                    textField.setText(customer.getName());
                    textField.setEditable(false);
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("CCCD")) {
                    textField.setText(customer.getCustomerNo());
                    textField.setEditable(false);
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Giới Tính")) {
                    boolean gender = customer.isGender();
                    String gender1 = gender ? "Nữ" : "Nam";
                    textField.setText(gender1);
                    textField.setEditable(false);
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("Số Điện Thoại")) {
                    textField.setText(customer.getPhone());
                    jTextFieldsCustomer.add(textField);
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("Địa Chỉ")) {
                    textField.setText(customer.getAddress());
                    jTextFieldsCustomer.add(textField);
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Email")) {
                    textField.setText(customer.getEmail());
                    jTextFieldsCustomer.add(textField);
                    content.add(textField);
                }
            }
        }

        buttonCancel.setPreferredSize(new Dimension(100, 30));
        buttonCancel.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                buttonCancel.setBackground(new Color(0xD54218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonCancel.setBackground(Color.white);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                cancel();
            }
        });
        containerButton.add(buttonCancel);

        buttonEdit.setPreferredSize(new Dimension(100, 30));
        buttonEdit.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                editCustomer();
            }
        });
        containerButton.add(buttonEdit);
    }


    private void editCustomer() {
        Pair<Boolean, String> result;
        String customerNo, name, phone, address, email;
        boolean gender;
        Date birthdate;
        customerNo = customer.getCustomerNo();
        name = customer.getName();
        gender = customer.isGender(); // Chuyển đổi giá trị boolean từ text field
        birthdate = customer.getBirthdate(); // Lấy ngày tháng từ JDateChooser

        phone = jTextFieldsCustomer.get(0).getText().trim();
        address = jTextFieldsCustomer.get(1).getText().trim();
        email = jTextFieldsCustomer.get(2).getText().trim();


        Customer newCustomer = new Customer(customerNo, name, gender, birthdate, phone, address, email, false);

        result = customerBLL.updateAllCustomer(customer, newCustomer);

        if (result.getKey()) {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    }
}
