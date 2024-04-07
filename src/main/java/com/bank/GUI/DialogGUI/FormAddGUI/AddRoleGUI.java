package com.bank.GUI.DialogGUI.FormAddGUI;

import com.bank.BLL.RoleBLL;
import com.bank.DTO.Role;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.components.MyTextFieldUnderLine;
import com.bank.main.Bank_Application;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class AddRoleGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeRole;
    private List<JTextField> jTextFieldRole;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private RoleBLL roleBLL = new RoleBLL();

    public AddRoleGUI() {
        super();
        super.setTitle("Thêm chức vụ");
        super.setSize(new Dimension(1000, 250));
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        init();
        setVisible(true);
    }

    private void init() {
        titleName = new JLabel();
        attributeRole = new ArrayList<>();
        jTextFieldRole = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");

        titleName.setText("Thêm chức vụ");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Tên chức vụ"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(150, 35));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.PLAIN, 16)));
            attributeRole.add(label);
            content.add(label);

            MyTextFieldUnderLine textField = new MyTextFieldUnderLine();
            textField.setPreferredSize(new Dimension(280, 35));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            jTextFieldRole.add(textField);
            content.add(textField, "wrap");

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

        buttonAdd.setPreferredSize(new Dimension(100, 30));
        buttonAdd.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addRole();
            }
        });
        containerButton.add(buttonAdd);
    }

    private void addRole() {
        Pair<Boolean, String> result;
        int id;
        String name;

        id = roleBLL.getAutoID(roleBLL.searchRoles()); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá
        name = jTextFieldRole.get(0).getText();

        Role role = new Role(id, name); // false là tồn tại, true là đã xoá

        result = roleBLL.addRole(role);

        if (result.getKey()) {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
