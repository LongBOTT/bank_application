package com.bank.GUI.DialogGUI.FormAddGUI;

import com.bank.BLL.BranchBLL;
import com.bank.DTO.Branch;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.HomeGUI;
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

public class AddBranchGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeBranch;
    private List<JTextField> jTextFieldBranch;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private BranchBLL branchBLL = new BranchBLL();

    public AddBranchGUI() {
        super();
        super.setTitle("Thêm Chi Nhánh");
        super.setSize(new Dimension(1000, 250));
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        init();
        setVisible(true);
    }

    private void init() {
        titleName = new JLabel();
        attributeBranch = new ArrayList<>();
        jTextFieldBranch = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");

        titleName.setText("Thêm Chi Nhánh");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);


        for (String string : new String[]{"Tên Chi Nhánh", "Điện Thoại", "Địa Chỉ"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(150, 35));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeBranch.add(label);
            content.add(label);

            MyTextFieldUnderLine textField = new MyTextFieldUnderLine();
            textField.setPreferredSize(new Dimension(280, 35));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            jTextFieldBranch.add(textField);
            if (string.equals("Tên Chi Nhánh")) {
                content.add(textField);
                continue;
            }
            if (string.equals("Điện Thoại")) {
                content.add(textField, "wrap");
            } else {
                content.add(textField);
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

        buttonAdd.setPreferredSize(new Dimension(100, 30));
        buttonAdd.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addBranch();
            }
        });
        containerButton.add(buttonAdd);
    }

    private void addBranch() {
        Pair<Boolean, String> result;
        int id;
        String name, phone, address;

        id = branchBLL.getAutoID(branchBLL.searchBranches("[deleted] = 0")); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá
        name = jTextFieldBranch.get(0).getText();
        phone = jTextFieldBranch.get(1).getText();
        address = jTextFieldBranch.get(2).getText();

        Branch currentBranch = branchBLL.searchBranches("[id] = " + HomeGUI.staff.getBranch_id()).get(0);

        Branch branch = new Branch(id, name, phone, address, currentBranch.getHeadquarter_id(),false); // false là tồn tại, true là đã xoá

        result = branchBLL.addBranch(branch);

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
