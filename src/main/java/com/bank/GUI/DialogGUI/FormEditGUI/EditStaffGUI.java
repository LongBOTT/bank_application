package com.bank.GUI.DialogGUI.FormEditGUI;

import com.bank.BLL.BranchBLL;
import com.bank.BLL.RoleBLL;
import com.bank.BLL.Role_DetailBLL;
import com.bank.BLL.StaffBLL;
import com.bank.DTO.Branch;
import com.bank.DTO.Role;
import com.bank.DTO.Role_Detail;
import com.bank.DTO.Staff;
//import com.bank.GUI.ChangeRoleGUI;
//import com.bank.GUI.CreateWorkScheduleGUI;
import com.bank.GUI.ChangeBranchGUI;
import com.bank.GUI.ChangeRoleGUI;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.HomeGUI;
import com.bank.GUI.components.Circle_ProgressBar;
import com.bank.GUI.components.MyTextFieldUnderLine;
import com.bank.GUI.components.swing.DataSearch;
import com.bank.GUI.components.swing.EventClick;
import com.bank.GUI.components.swing.MyTextField;
import com.bank.GUI.components.swing.PanelSearch;
import com.bank.main.Bank_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.toedter.calendar.JDateChooser;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditStaffGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeStaff;
    private JButton buttonCancel;
    private JButton buttonEdit;
    private JButton buttonChangeBranch;
    private StaffBLL staffBLL = new StaffBLL();
    private List<JTextField> jTextFieldsStaff;
    public JTextField textFieldRole;
    public static boolean changeRole = false;
    private JDateChooser jDateChooser = new JDateChooser();
    private Staff staff;
    private ActionListener refresh;
    public EditStaffGUI(ActionListener refresh, Staff staff) {
        super();
        super.setTitle("Cập Nhật Thông Tin Nhân Viên");
        this.refresh = refresh;
        this.staff = staff;
        super.setSize(new Dimension(1000, 450));
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        init(staff);
        setVisible(true);
    }

    private void init(Staff staff) {
        titleName = new JLabel();
        attributeStaff = new ArrayList<>();
        jTextFieldsStaff = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonEdit = new JButton("Cập nhật");
        buttonChangeBranch = new JButton("Chuyển chi nhánh");

        titleName.setText("Cập Nhật Thông Tin Nhân Viên");
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
            textField.setBackground(new Color(245, 246, 250));

            if (string.trim().equals("Ngày Sinh")) {
                Date birthDate = staff.getBirthdate();
                jDateChooser = new JDateChooser();
                jDateChooser.setDateFormatString("dd/MM/yyyy");
                jDateChooser.setDate(birthDate);
                jDateChooser.setPreferredSize(new Dimension(180, 35));
                jDateChooser.setMinSelectableDate(java.sql.Date.valueOf("1000-01-01"));
                jDateChooser.setEnabled(false);
                content.add(jDateChooser);
            } else {
                if (string.trim().equals("Mã Nhân Viên")) {
                    String staffId = Integer.toString(staff.getId());
                    textField.setText(staffId);
                    textField.setEditable(false);
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("Tên Nhân Viên")) {
                    textField.setText(staff.getName());
                    textField.setEditable(false);
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("CCCD")) {
                    textField.setText(staff.getStaffNo());
                    textField.setEditable(false);
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("Giới Tính")) {
                    boolean gender = staff.isGender();
                    String gender1 = gender ? "Nữ" : "Nam";
                    textField.setText(gender1);
                    textField.setEditable(false);
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Số Điện Thoại")) {
                    textField.setText(staff.getPhone());
                    jTextFieldsStaff.add(textField);
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Địa Chỉ")) {
                    textField.setText(staff.getAddress());
                    jTextFieldsStaff.add(textField);
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("Email")) {
                    textField.setText(staff.getEmail());
                    jTextFieldsStaff.add(textField);
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Chức Vụ")) {
                    JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    jPanel.setPreferredSize(new Dimension(280, 35));
                    jPanel.setBackground(Color.white);
                    content.add(jPanel);

                    textFieldRole = new MyTextFieldUnderLine();
                    textFieldRole.setPreferredSize(new Dimension(215, 35));
                    textFieldRole.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                    textFieldRole.setBackground(new Color(245, 246, 250));
                    List<Role_Detail> role_detailList = new Role_DetailBLL().searchRole_detailsByStaff(staff.getId());

                    if (!role_detailList.isEmpty()) {
                        Role_Detail roleDetail = role_detailList.get(0);
                        Role role = new RoleBLL().searchRoles("id = " + roleDetail.getRole_id()).get(0);
                        textFieldRole.setText(role.getName());
                    } else {
                        textFieldRole.setText("Chưa có");
                    }
                    textFieldRole.setEditable(false);
                    jPanel.add(textFieldRole);

                    JLabel iconChangeRole = new JLabel(new FlatSVGIcon("icon/edit.svg"));
                    iconChangeRole.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    iconChangeRole.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            changeRole = false;
                            dispose();
                            new ChangeRoleGUI(staff, textFieldRole);
                            if (changeRole && staff.getId() == HomeGUI.staff.getId()) {
                                JOptionPane.showMessageDialog(null, "Vui lòng đăng nhập lại.",
                                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                Bank_Application.homeGUI.dispose();
                                System.gc();
                                Bank_Application.loginGUI.setVisible(true);
                                return;
                            }
                            if (changeRole)
                                refresh.actionPerformed(null);
                            setVisible(true);
                        }
                    });
                    jPanel.add(iconChangeRole);
                    continue;
                }
                if (string.trim().equals("Chi Nhánh")) {
                    Branch branch = new BranchBLL().searchBranches("[id] = " + staff.getBranch_id()).get(0);
                    textField.setText(branch.getName());
                    textField.setEditable(false);
                    content.add(textField, "wrap");
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
        buttonEdit.setBackground(new Color(1, 120, 220));
        buttonEdit.setForeground(Color.white);
        buttonEdit.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                editStaff();
            }
        });
        containerButton.add(buttonEdit);

        buttonChangeBranch.setPreferredSize(new Dimension(250, 30));
        buttonChangeBranch.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonChangeBranch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonChangeBranch.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent m) {
                dispose();
                new ChangeBranchGUI(refresh, staff);
            }
        });
        containerButton.add(buttonChangeBranch);
    }


    private void editStaff() {
        Pair<Boolean, String> result;
        int id, branch_id;
        String staffNo, name, phone, address, email;
        boolean gender;
        Date birthdate;
        id = staff.getId();
        staffNo = staff.getStaffNo();
        name = staff.getName();
        gender = staff.isGender(); // Chuyển đổi giá trị boolean từ text field
        birthdate = staff.getBirthdate(); // Lấy ngày tháng từ JDateChooser

        phone = jTextFieldsStaff.get(0).getText().trim();
        address = jTextFieldsStaff.get(1).getText().trim();
        email = jTextFieldsStaff.get(2).getText().trim();

        branch_id = staff.getBranch_id();

        Staff newStaff = new Staff(id, staffNo, name, gender, birthdate, phone, address, email, false, branch_id);

        result = staffBLL.updateStaff(staff, newStaff);

        if (result.getKey()) {
            Circle_ProgressBar circleProgressBar = new Circle_ProgressBar();
            circleProgressBar.getRootPane ().setOpaque (false);
            circleProgressBar.getContentPane ().setBackground (new Color (0, 0, 0, 0));
            circleProgressBar.setBackground (new Color (0, 0, 0, 0));
            circleProgressBar.progress();
            circleProgressBar.setVisible(true);
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            refresh.actionPerformed(null);
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    }
}
