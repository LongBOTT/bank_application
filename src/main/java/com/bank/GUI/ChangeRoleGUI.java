package com.bank.GUI;

import com.bank.BLL.*;
import com.bank.DTO.*;
import com.bank.GUI.DialogGUI.FormEditGUI.EditStaffGUI;
import com.bank.GUI.components.RoundedPanel;
import com.bank.main.Bank_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChangeRoleGUI extends JDialog {
    private RoundedPanel role_detail_panel;
    private JLabel titleName;
    private List<JLabel> attributeRole_detail;
    private JComboBox<String> jComboBoxRole;
    private JTextField textFieldSalary;
    private JButton buttonCancel;
    private JButton buttonSet;
    private Role_DetailBLL role_detailBLL = new Role_DetailBLL();
    private Staff staff;
    private Role_Detail roleDetail;
    private RoundedPanel content = new RoundedPanel();

    public ChangeRoleGUI(Staff staff) {
        super((Frame) null, "", true);
        getContentPane().setBackground(new Color(228,231,235));
        setTitle("Thiết Lập Lương");
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setIconImage(new FlatSVGIcon("icon/Vietcombank.svg").getImage());
        setSize(new Dimension(500, 350));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(Bank_Application.homeGUI);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });
        this.staff = staff;
        List<Role_Detail> role_detailList = new Role_DetailBLL().searchRole_detailsByStaff(staff.getId());
        if (!role_detailList.isEmpty()) {
            roleDetail = role_detailList.get(0);
        } else {
            roleDetail = null;
        }
        init();
        setVisible(true);
    }

    private void init() {
        role_detail_panel = new RoundedPanel();
        titleName = new JLabel();
        attributeRole_detail = new ArrayList<>();
        jComboBoxRole = new JComboBox<>();
        textFieldSalary = new JTextField();
        buttonCancel = new JButton("Huỷ");
        buttonSet = new JButton("Thiết lập");


        RoundedPanel title = new RoundedPanel();
        RoundedPanel containerButton = new RoundedPanel();

        title.setLayout(new BorderLayout());
        title.setBackground(new Color(228,231,235));
        title.setPreferredSize(new Dimension(500, 40));
        add(title);

        content.setLayout(new FlowLayout());
        content.setBackground(new Color(255, 255, 255));
        content.setPreferredSize(new Dimension(500, 200));
        add(content);

        containerButton.setLayout(new FlowLayout());
        containerButton.setBackground(new Color(228,231,235));
        containerButton.setPreferredSize(new Dimension(500, 50));
        add(containerButton);

        titleName.setText("Thiết lập lương");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);


        role_detail_panel.setBackground(new Color(255, 255, 255));
        role_detail_panel.setLayout(new MigLayout("", "20[]20[]20", "20[]20[]20"));
        role_detail_panel.setPreferredSize(new Dimension(300, 200));
        content.add(role_detail_panel);


        for (String string : new String[]{"Nhân viên", "Chức vụ","Tiền lương"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.PLAIN, 16)));
            attributeRole_detail.add(label);
            role_detail_panel.add(label);


            if (string.equals("Nhân viên")) {
                JLabel jLabel = new JLabel(staff.getName());
                jLabel.setFont((new Font("Public Sans", Font.BOLD, 16)));
                role_detail_panel.add(jLabel, "wrap");
            }

            if (string.equals("Chức vụ")) {
                for (Role role : new RoleBLL().searchRoles("[id] > 1"))
                    jComboBoxRole.addItem(role.getName());

                if (roleDetail != null) {
                    Role role = new RoleBLL().searchRoles("[id] = " + roleDetail.getRole_id()).get(0);
                    jComboBoxRole.setSelectedItem(role.getName());
                    jComboBoxRole.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            checkDate();
                        }
                    });
                }

                jComboBoxRole.setPreferredSize(new Dimension(150, 30));
                jComboBoxRole.setFont((new Font("Public Sans", Font.PLAIN, 14)));


                role_detail_panel.add(jComboBoxRole, "wrap");
            }

            if (string.equals("Tiền lương")) {
                textFieldSalary.setPreferredSize(new Dimension(150, 30));
                textFieldSalary.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                if (roleDetail != null) {
                    textFieldSalary.setText(String.valueOf(roleDetail.getSalary()));
                }
                textFieldSalary.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (!Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                    }
                });
                role_detail_panel.add(textFieldSalary);
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

        buttonSet.setPreferredSize(new Dimension(100, 30));
        buttonSet.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonSet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonSet.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                updateRole_detail();
            }
        });
        containerButton.add(buttonSet);

    }

    private void checkDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        if (!firstDayOfMonth.equals(LocalDate.now())) {
            Role role = new RoleBLL().searchRoles("[id] = " + roleDetail.getRole_id()).get(0);
            jComboBoxRole.setSelectedItem(role.getName());
            JOptionPane.showMessageDialog(null, "Vui lòng thay đổi chức vụ vào ngày đầu tiên của tháng.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateRole_detail() {
        Pair<Boolean, String> result;
        int role_id, staff_id;
        java.util.Date entry_date;
        BigDecimal salary;

        role_id = jComboBoxRole.getSelectedIndex() + 2;
        staff_id = staff.getId();
        entry_date = Date.valueOf(LocalDate.now());
        salary = BigDecimal.valueOf(Double.parseDouble(textFieldSalary.getText()));
        Role_Detail role_detail = new Role_Detail(role_id, staff_id, entry_date, salary); // false là tồn tại, true là đã xoá

        result = role_detailBLL.addRole_detail(role_detail);

        if (result.getKey()) {

            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            EditStaffGUI.textFieldRole.setText(Objects.requireNonNull(jComboBoxRole.getSelectedItem()).toString());
            EditStaffGUI.changeRole = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void cancel() {
        String[] options = new String[]{"Huỷ", "Thoát"};
        int choice = JOptionPane.showOptionDialog(null, "Bạn có muốn thoát?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1)
            dispose();
    }
}
