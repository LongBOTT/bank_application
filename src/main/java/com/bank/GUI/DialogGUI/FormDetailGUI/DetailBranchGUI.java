package com.bank.GUI.DialogGUI.FormDetailGUI;

import com.bank.BLL.BranchBLL;
import com.bank.DTO.Branch;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.main.Bank_Application;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DetailBranchGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeBranch;
    private List<JLabel> jTextFieldBranch;
    private BranchBLL branchBLL = new BranchBLL();

    public DetailBranchGUI(Branch branch) {
        super();
        super.setTitle("Thông Tin Chi Nhánh");
        super.setSize(new Dimension(600, 450));
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        init(branch);
        setVisible(true);
    }

    private void init(Branch branch) {
        titleName = new JLabel();
        attributeBranch = new ArrayList<>();
        jTextFieldBranch = new ArrayList<>();
        content.setLayout(new MigLayout("",
                "50[]20[]50",
                "20[]20[]20"));

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

            JLabel textField = new JLabel();

            if (string.equals("Tên Chi Nhánh")) {
                textField.setText(branch.getName());
            }
            if (string.equals("Điện Thoại")) {
                textField.setText(branch.getPhone());
            }
            if (string.equals("Địa Chỉ")) {
                textField.setText("<html>" + branch.getAddress() + "</html>");
            }
            if (string.equals("Mã Chi Nhánh")) {
                textField.setText(String.valueOf(branch.getId()));
            }

            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            jTextFieldBranch.add(textField);
            content.add(textField, "wrap");

        }
    }
}

