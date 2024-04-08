package com.bank.GUI.DialogGUI.FormDetailGUI;
;
import com.bank.BLL.Bank_AccountBLL;
import com.bank.BLL.BranchBLL;
import com.bank.DTO.Branch;
import com.bank.DTO.Customer;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.components.DataTable;
import com.bank.GUI.components.MyTextFieldUnderLine;
import com.bank.GUI.components.RoundedPanel;
import com.bank.GUI.components.RoundedScrollPane;
import com.bank.main.Bank_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DetailCustomerGUI extends DialogForm {
    private Bank_AccountBLL bankAccountBLL = new Bank_AccountBLL();
    private BranchBLL branchBLL = new BranchBLL();
    private JLabel titleName;
    private List<JLabel> attributeCustomer;
    private List<JTextField> jTextFieldsCustomer;
    private JDateChooser jDateChooser = new JDateChooser();
    private DataTable dataTable;
    private Object[][] data = new Object[0][0];

    public DetailCustomerGUI(Customer customer) {
        super();
        super.setTitle("Thông Tin Khách Hàng ");
        super.setSize(new Dimension(1000, 600));
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        init(customer);
        setVisible(true);
    }

    private void init(Customer customer) {
        titleName = new JLabel();
        attributeCustomer = new ArrayList<>();
        jTextFieldsCustomer = new ArrayList<>();

        titleName.setText("Thông Tin Khách Hàng");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        content.setPreferredSize(new Dimension(1000, 250));

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
            textField.setEditable(false);
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
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("CCCD")) {
                    textField.setText(customer.getCustomerNo());
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Giới Tính")) {
                    boolean gender = customer.isGender();
                    String gender1 = gender ? "Nữ" : "Nam";
                    textField.setText(gender1);
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("Số Điện Thoại")) {
                    textField.setText(customer.getPhone());
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("Địa Chỉ")) {
                    textField.setText(customer.getAddress());
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Email")) {
                    textField.setText(customer.getEmail());
                    content.add(textField);
                }
            }
        }

        String[] columnNames = new String[]{"Số Thẻ", "CCCD", "Số Dư", "Chi Nhánh", "Ngày Mở", "Trạng Thái"};
        dataTable = new DataTable(new Object[0][0], columnNames);

        super.remove(containerButton);
        RoundedScrollPane scrollPanel = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.getViewport().setBackground(new Color(191, 198, 208));
        scrollPanel.setPreferredSize(new Dimension(1000, 300));
        super.add(scrollPanel, "wrap");

        loadDataTable(bankAccountBLL.getData(bankAccountBLL.findAllBank_Accounts("customer_no", customer.getCustomerNo())));
    }

    public void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        if (objects.length == 0) {
            return;
        }

        data = new Object[objects.length][objects[0].length];

        for (int i = 0; i < objects.length; i++) {
            System.arraycopy(objects[i], 0, data[i], 0, objects[i].length);
            Branch branch = branchBLL.findAllBranchs("id", data[i][3].toString()).get(0);

            data[i][3] = branch.getName();
        }


        for (Object[] object : data) {
            model.addRow(object);
        }
    }

    private void selectFunction() {
        int indexRow = dataTable.getSelectedRow();
        int indexColumn = dataTable.getSelectedColumn();

        if (indexColumn == 6)
            new DetailBank_AccountGUI(bankAccountBLL.findAllBank_Accounts("number", data[indexRow][0].toString()).get(0)); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá
    }
}

