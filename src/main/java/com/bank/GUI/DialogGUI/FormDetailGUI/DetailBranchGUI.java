package com.bank.GUI.DialogGUI.FormDetailGUI;

import com.bank.BLL.BranchBLL;
import com.bank.DTO.Branch;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.components.MyTextFieldUnderLine;
import com.bank.main.Bank_Application;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DetailBranchGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeBranch;

    public DetailBranchGUI(Branch branch) {
        super();
        super.setTitle("Thông Tin Chi Nhánh");
        super.setSize(new Dimension(1000, 300));
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        init(branch);
        setVisible(true);
    }

    private void init(Branch branch) {
        titleName = new JLabel();
        attributeBranch = new ArrayList<>();

        titleName.setText("Thông Tin Chi Nhánh");
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

            MyTextFieldUnderLine textField = new MyTextFieldUnderLine();
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            textField.setEditable(false);
            if (string.equals("Tên Chi Nhánh")) {
                textField.setText(branch.getName());

                content.add(textField, "wrap");
                continue;
            }
            if (string.equals("Điện Thoại")) {
                textField.setText(branch.getPhone());

                content.add(textField);
                continue;
            }
            if (string.equals("Địa Chỉ")) {
                textField.setText(branch.getAddress());

                content.add(textField, "wrap");
                continue;
            }
            if (string.equals("Mã Chi Nhánh")) {
                textField.setText(String.valueOf(branch.getId()));
                content.add(textField);
            }
        }
    }
}

