package com.bank.GUI.DialogGUI.FormAddGUI;

import com.bank.BLL.AccountBLL;
import com.bank.BLL.StaffBLL;
import com.bank.DTO.Account;
import com.bank.DTO.Staff;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.components.MyTextFieldUnderLine;
import com.bank.GUI.components.swing.DataSearch;
import com.bank.GUI.components.swing.EventClick;
import com.bank.GUI.components.swing.MyTextField;
import com.bank.GUI.components.swing.PanelSearch;
import com.bank.main.Bank_Application;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class AddAccountGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeAccount;
    private List<MyTextFieldUnderLine> jTextFieldAccount;
    private MyTextField txtSearch;
    private PanelSearch search;
    private JPopupMenu menu;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private StaffBLL staffBLL = new StaffBLL();
    private AccountBLL accountBLL = new AccountBLL();
    private List<String> staffList;
    private int staff_id = 0;

    public AddAccountGUI() {
        super();
        super.setTitle("Thêm tài khoản");
        super.setSize(new Dimension(1000, 250));
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        init();
        menu = new JPopupMenu();
        search = new PanelSearch();
        menu.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menu.add(search);
        menu.setFocusable(false);
        search.addEventClick(new EventClick() {
            @Override
            public void itemClick(DataSearch data) {
                menu.setVisible(false);
                txtSearch.setText(data.getText());
                Staff staff = new StaffBLL().searchStaffs("[id] = " + data.getText().split(" - ")[1]).get(0);
                staff_id = staff.getId();
            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                search.remove(com);
                menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                if (search.getItemSize() == 0) {
                    menu.setVisible(false);
                }
            }
        });
        setVisible(true);

    }

    private void init() {
        titleName = new JLabel();
        attributeAccount = new ArrayList<>();
        jTextFieldAccount = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");
        staffList = new ArrayList<String>();

        titleName.setText("Thêm tài khoản");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        for (String string : new String[]{"Tên Đăng Nhập", "Nhân Viên"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(150, 35));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeAccount.add(label);
            content.add(label);


            if (string.equals("Nhân Viên")) {
                txtSearch = new MyTextField();
                txtSearch.setPreferredSize(new Dimension(280, 35));
                txtSearch.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                txtSearch.setBackground(new Color(245, 246, 250));
                txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        txtSearchMouseClicked(evt);
                    }
                });
                txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        txtSearchKeyPressed(evt);
                    }

                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        txtSearchKeyReleased(evt);
                    }
                });
                content.add(txtSearch);
            } else {
                MyTextFieldUnderLine textField = new MyTextFieldUnderLine();
                textField.setPreferredSize(new Dimension(280, 35));
                textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                textField.setBackground(new Color(245, 246, 250));
                jTextFieldAccount.add(textField);
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
                addAccount();
            }
        });
        containerButton.add(buttonAdd);
    }

    private void addAccount() {
        Pair<Boolean, String> result;

        int id;
        String username;

        if (staff_id == 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên!",
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }
//
//        if (!staffList.contains(combo.getSelectedItem())) {
//            JOptionPane.showMessageDialog(null, "Nhân viên không hợp lệ!",
//                    "Thông báo", JOptionPane.ERROR_MESSAGE);
//            return;
//        }

        id = accountBLL.getAutoID();
        username = jTextFieldAccount.get(0).getText();

        Account account = new Account(id, username, staff_id);

        result = accountBLL.addAccount(account);

        if (result.getKey()) {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {
        if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
            menu.show(txtSearch, 0, txtSearch.getHeight());
            search.clearSelected();
        }
    }

    private List<DataSearch> search(String text) {
        List<DataSearch> list = new ArrayList<>();
        staffList = new ArrayList<>();
        staff_id =  0;
        for (Staff staff : staffBLL.findStaffs("name",  text)) {
            staffList.add(String.valueOf(staff.getId()));
        }

        for (Account account : accountBLL.searchAccounts())
            staffList.remove(String.valueOf(account.getStaff_id()));

        staffList.replaceAll(s -> staffBLL.searchStaffs("[id] = " + Integer.valueOf(s)).get(0).getName() + " - " + s);

        for (String m : staffList) {
            if (list.size() == 7)
                break;
            list.add(new DataSearch(m));
        }
        return list;
    }

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtSearch.getText().trim().toLowerCase();
            search.setData(search(text));
            if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
                menu.show(txtSearch, 0, txtSearch.getHeight());
                menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
            } else {
                menu.setVisible(false);
            }
        }
    }

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            search.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            search.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = search.getSelectedText();
            txtSearch.setText(text);

        }
        menu.setVisible(false);

    }
}
