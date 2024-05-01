package com.bank.GUI.DialogGUI.FormDetailGUI;

import com.bank.BLL.BranchBLL;
import com.bank.BLL.RoleBLL;
import com.bank.BLL.Role_DetailBLL;
import com.bank.BLL.StaffBLL;
import com.bank.DTO.Branch;
import com.bank.DTO.Role;
import com.bank.DTO.Role_Detail;
import com.bank.DTO.Staff;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.components.MyTextFieldUnderLine;
import com.bank.main.Bank_Application;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailStaffGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeStaff;
    private List<JTextField> jTextFieldsStaff;
    private StaffBLL staffBLL = new StaffBLL();
    private Staff staff = new Staff();

    public DetailStaffGUI(Staff staff) {
        super();
        super.setTitle("Thông Tin Nhân Viên ");
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        init(staff);
        setVisible(true);
    }

    private void init(Staff staff) {
        titleName = new JLabel();
        attributeStaff = new ArrayList<>();
        jTextFieldsStaff = new ArrayList<>();

        titleName.setText("Thông Tin Nhân Viên");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);


        for (String string : new String[]{"Mã Nhân Viên", "Tên Nhân Viên", "CCCD", "Giới Tính",
                "Ngày Sinh", "Số Điện Thoại", "Địa Chỉ", "Email", "Chức Vụ", "Chi Nhánh"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(150, 35));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 15)));
            attributeStaff.add(label);
            content.add(label);
            MyTextFieldUnderLine textField = new MyTextFieldUnderLine();
            textField.setPreferredSize(new Dimension(280, 35));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setEditable(false);
            if (string.trim().equals("Ngày Sinh")) {
                textField.setText(new SimpleDateFormat("dd-MM-yyyy").format(staff.getBirthdate()));
                textField.setEditable(false);
                content.add(textField);
            } else {
                if (string.trim().equals("Mã Nhân Viên")) {
                    String staffId = Integer.toString(staff.getId());
                    textField.setText(staffId);
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("Tên Nhân Viên")) {
                    textField.setText(staff.getName());
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("CCCD")) {
                    textField.setText(staff.getStaffNo());
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("Giới Tính")) {
                    boolean gender = staff.isGender();
                    String gender1 = gender ? "Nữ" : "Nam";
                    textField.setText(gender1);
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Số Điện Thoại")) {
                    textField.setText(staff.getPhone());
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Địa Chỉ")) {
                    textField.setText(staff.getAddress());
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("Email")) {
                    textField.setText(staff.getEmail());
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Chức Vụ")) {
                    List<Role_Detail> roleDetails = new Role_DetailBLL().searchRole_detailsByStaff(staff.getId());
                    if (roleDetails.isEmpty()) {
                        textField.setText("Chưa có chức vụ");
                    } else {
                        Role role = new RoleBLL().searchRoles("id = " + roleDetails.get(0).getRole_id()).get(0);
                        textField.setText(role.getName());
                    }
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("Chi Nhánh")) {
                    Branch branch = new BranchBLL().searchBranches("[id] = " + staff.getBranch_id()).get(0);
                    textField.setText(branch.getName());
                    content.add(textField, "wrap");
                }
            }
        }

    }
}

