package com.bank.GUI.DialogGUI.FormAddGUI;

import com.bank.BLL.AccountBLL;
import com.bank.BLL.BranchBLL;
import com.bank.BLL.RoleBLL;
import com.bank.BLL.StaffBLL;
import com.bank.DTO.Account;
import com.bank.DTO.Branch;
import com.bank.DTO.Staff;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.HomeGUI;
import com.bank.GUI.components.Circle_ProgressBar;
import com.bank.GUI.components.MyTextFieldUnderLine;
import com.bank.GUI.components.swing.DataSearch;
import com.bank.GUI.components.swing.EventClick;
import com.bank.GUI.components.swing.MyTextField;
import com.bank.GUI.components.swing.PanelSearch;
import com.bank.main.Bank_Application;
import com.toedter.calendar.JDateChooser;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddStaffGUI extends DialogForm {
    private List<JLabel> attributeStaff;
    private List<JTextField> jTextFieldsStaff;
    private JLabel titleName;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private JDateChooser jDateChooser = new JDateChooser();
    private MyTextField txtSearch;
    private PanelSearch search;
    private JPopupMenu menu;
    private StaffBLL staffBLL = new StaffBLL();
    private BranchBLL branchBLL = new BranchBLL();
    private JRadioButton radioMale = new JRadioButton();
    private JRadioButton radioFemale = new JRadioButton();
    private ButtonGroup Gender;
    private int branch_id = 0;

    public AddStaffGUI() {
        super();
        super.setTitle("Thêm Nhân Viên");
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
                Branch branch = branchBLL.searchBranches("[name] = '" + data.getText() + "'").get(0);
                branch_id = branch.getId();
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
        attributeStaff = new ArrayList<>();
        jTextFieldsStaff = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");

        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        Gender = new ButtonGroup();

        for (String string : new String[]{"Tên Nhân Viên", "CCCD", "Giới Tính",
                "Ngày Sinh", "Số Điện Thoại", "Địa Chỉ", "Email", "Chi Nhánh"}) {
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
                jDateChooser = new JDateChooser();
                jDateChooser.setDateFormatString("dd/MM/yyyy");
                jDateChooser.setPreferredSize(new Dimension(280, 35));
                jDateChooser.setMinSelectableDate(java.sql.Date.valueOf("1000-01-01"));
                content.add(jDateChooser, "wrap");
            } else {
                if (string.trim().equals("Giới Tính")) {
                    JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    jPanel.setPreferredSize(new Dimension(280, 35));
                    jPanel.setBackground(Color.white);

                    radioMale = new JRadioButton("Nam");
                    radioFemale = new JRadioButton("Nữ");

                    jPanel.add(radioMale);
                    jPanel.add(radioFemale);

                    Gender.add(radioMale);
                    Gender.add(radioFemale);

                    content.add(jPanel);

                }
                else {
                    if (string.trim().equals("Chi Nhánh")) {
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
                        content.add(txtSearch, "wrap");
                    }
                    else {
                        if (string.trim().equals("Tên Nhân Viên") || string.trim().equals("Số Điện Thoại") || string.trim().equals("Email")) {
                            jTextFieldsStaff.add(textField);
                            content.add(textField);
                        } else {
                            jTextFieldsStaff.add(textField);
                            content.add(textField, "wrap");
                        }
                    }
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

        buttonAdd.setPreferredSize(new Dimension(100, 30));
        buttonAdd.setBackground(new Color(1, 120, 220));
        buttonAdd.setForeground(Color.white);
        buttonAdd.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addStaff();

            }
        });
        containerButton.add(buttonAdd);

    }

    private void addStaff() {
        Pair<Boolean, String> result;
        int id;
        String staffNo, name, phone, address, email;
        boolean gender;
        Date birthdate;
        id = staffBLL.getAutoID();
        name = jTextFieldsStaff.get(0).getText().trim();
        staffNo = jTextFieldsStaff.get(1).getText().trim();
        gender = !radioMale.isSelected();
        birthdate = jDateChooser.getDate() != null ? java.sql.Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser.getDate())) : null;
        phone = jTextFieldsStaff.get(2).getText().trim();
        address = jTextFieldsStaff.get(3).getText().trim();
        email = jTextFieldsStaff.get(4).getText().trim();

        if (branch_id == 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn chi nhánh!",
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Staff staff = new Staff(id, staffNo, name, gender, birthdate, phone, address, email, false, branch_id);

        result = staffBLL.addStaff(staff);

        if (result.getKey()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    AccountBLL accountBLL = new AccountBLL();
                    accountBLL.addAccount(new Account(accountBLL.getAutoID(), staff.getStaffNo(), staff.getId()));
                }
            });
            thread.start();
            Circle_ProgressBar circleProgressBar = new Circle_ProgressBar();
            circleProgressBar.getRootPane ().setOpaque (false);
            circleProgressBar.getContentPane ().setBackground (new Color (0, 0, 0, 0));
            circleProgressBar.setBackground (new Color (0, 0, 0, 0));
            circleProgressBar.progress();
            circleProgressBar.setVisible(true);
            JOptionPane.showMessageDialog(null, result.getValue(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {

            JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {
        if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
            menu.show(txtSearch, 0, txtSearch.getHeight());
            search.clearSelected();
        }
    }

    private List<DataSearch> search(String text) {
        branch_id = 0;
        List<DataSearch> list = new ArrayList<>();
        List<Branch> branches = branchBLL.findBranchs("name", text);
        for (Branch m : branches) {
            if (list.size() == 7)
                break;
            list.add(new DataSearch(m.getName()));
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
