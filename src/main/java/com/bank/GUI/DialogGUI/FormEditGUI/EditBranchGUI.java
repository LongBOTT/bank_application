package com.bank.GUI.DialogGUI.FormEditGUI;

import com.bank.BLL.BranchBLL;
import com.bank.DTO.Branch;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.main.Bank_Application;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EditBranchGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeBranch;
    private List<JTextField> jTextFieldBranch;
    private JButton buttonCancel;
    private JButton buttonEdit;
    private BranchBLL branchBLL = new BranchBLL();
    private Branch branch;

    public EditBranchGUI(Branch branch) {
        super();
        super.setTitle("Cập Nhập Thông Tin Chi Nhánh");
        super.setSize(new Dimension(600, 450));
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        this.branch = branch;
        init(branch);
        setVisible(true);
    }

    private void init(Branch branch) {
        titleName = new JLabel();
        attributeBranch = new ArrayList<>();
        jTextFieldBranch = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonEdit = new JButton("Cập nhật");
        content.setLayout(new MigLayout("",
                "50[]20[]50",
                "20[]20[]20"));

        titleName.setText("Cập Nhập Thông Tin Chi Nhánh");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Mã Chi Nhánh", "Tên Chi Nhánh", "Điện Thoại", "Địa Chỉ"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeBranch.add(label);
            content.add(label);

            JTextField textField = new JTextField();

            if (string.equals("Tên Chi Nhánh")) {
                textField.setText(branch.getName());
            }
            if (string.equals("Điện Thoại")) {
                textField.setText(branch.getPhone());
            }
            if (string.equals("Địa Chỉ")) {
                textField.setText(branch.getAddress());
            }
            if (string.equals("Mã Chi Nhánh")) {
                JLabel jLabel = new JLabel();
                jLabel.setText(String.valueOf(branch.getId()));
                jLabel.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                content.add(jLabel, "wrap");
                continue;
            }

            textField.setPreferredSize(new Dimension(1000, 30));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            jTextFieldBranch.add(textField);
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

        buttonEdit.setPreferredSize(new Dimension(100, 30));
        buttonEdit.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                editBranch();
            }
        });
        containerButton.add(buttonEdit);
    }

    private void editBranch() {
        Pair<Boolean, String> result;
        int id, headquarter_id;
        String name, phone, address;

        id = branch.getId();
        name = jTextFieldBranch.get(0).getText();
        phone = jTextFieldBranch.get(1).getText();
        address = jTextFieldBranch.get(2).getText();
        headquarter_id = branch.getHeadquarter_id();

        Branch newsBranch = new Branch(id, name, phone, address, headquarter_id,false); // false là tồn tại, true là đã xoá

        result = branchBLL.updateBranch(branch, newsBranch);

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
