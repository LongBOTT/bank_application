package com.bank.GUI.DialogGUI;

import com.bank.BLL.CustomerBLL;
import com.bank.BLL.Transaction_Deposit_WithdrawalBLL;
import com.bank.DTO.Bank_Account;
import com.bank.DTO.Customer;
import com.bank.DTO.Transaction_Deposit_Withdrawal;
import com.bank.GUI.HomeGUI;
import com.bank.GUI.components.*;
import com.bank.GUI.components.swing.MyTextField;
import com.bank.main.Bank_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransactionGUI extends JDialog {
    private Transaction_Deposit_WithdrawalBLL transactionDepositWithdrawalBLL = new Transaction_Deposit_WithdrawalBLL();
    private SwitchButton switchButton;
    private DataTable dataTable;
    private JLabel jLabelTitle;
    private JComboBox<String> jComboBox;
    private List<MyTextFieldUnderLine> myTextFieldUnderLineList;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private Card card;
    public TransactionGUI(Bank_Account bank_account) {
        super((Frame) null, "", true);
        getContentPane().setBackground(new Color(228,231,235));
        setTitle("Hệ Thống Giao Dịch");
        setLayout(new BorderLayout());
        setIconImage(new FlatSVGIcon("icon/ACB.svg").getImage());
        setSize(new Dimension(1165, 680));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(Bank_Application.homeGUI);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });
        initComponents(bank_account);
        setVisible(true);
    }

    private void initComponents(Bank_Account bank_account) {
        switchButton =  new SwitchButton();
        myTextFieldUnderLineList = new ArrayList<>();
        jComboBox = new JComboBox<>(new String[]{"Gửi Tiền", "Rút Tiền"});
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Giao Dịch");

        RoundedPanel left = new RoundedPanel();
        left.setLayout(new MigLayout("", "[]", "[]5[]5[]"));
        left.setBackground(new Color(228,231,235));
        left.setPreferredSize(new Dimension(500, 680));
        add(left, BorderLayout.WEST);

        RoundedPanel right = new RoundedPanel();
        right.setLayout(new BorderLayout());
        right.setBackground(new Color(228,231,235));
        right.setPreferredSize(new Dimension(640, 680));
        add(right, BorderLayout.EAST);

        RoundedPanel panelCard = new RoundedPanel();
        panelCard.setLayout(new BorderLayout());
        panelCard.setPreferredSize(new Dimension(300, 170));
        left.add(panelCard, "center, wrap");

        card = new Card(bank_account);
        panelCard.add(card, BorderLayout.CENTER);

        RoundedPanel panel = new RoundedPanel();
        panel.setBackground(new Color(228,231,235));
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 40));
        left.add(panel, "wrap");

        jLabelTitle = new JLabel("<html>Lịch Sử Giao Dịch <b>Gửi Tiền</b></html>");
        jLabelTitle.setFont(new Font("Inter", Font.PLAIN, 15));
        panel.add(jLabelTitle, BorderLayout.WEST);

        RoundedPanel panel1 = new RoundedPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel1.setBackground(new Color(228,231,235));
        panel1.setPreferredSize(new Dimension(100, 40));
        panel.add(panel1, BorderLayout.EAST);
        switchButton.addEventSelected(new EventSwitchSelected() {
            @Override
            public void onSelected(boolean selected) {
                if (selected) {
                    jLabelTitle.setText("<html>Lịch Sử Giao Dịch <b>Rút Tiền</b></html>");
                    List<Transaction_Deposit_Withdrawal> transactionDepositWithdrawals = new ArrayList<>(transactionDepositWithdrawalBLL.getTransaction_deposit_withdrawalListAll());
                    transactionDepositWithdrawals.removeIf(Transaction_Deposit_Withdrawal -> !Transaction_Deposit_Withdrawal.getBank_number_account().equals(bank_account.getNumber()));
                    transactionDepositWithdrawals.removeIf(Transaction_Deposit_Withdrawal::getTransaction_type);

                    loadDataTable(transactionDepositWithdrawalBLL.getData(transactionDepositWithdrawals));
                }
                else {
                    jLabelTitle.setText("<html>Lịch Sử Giao Dịch <b>Gửi Tiền</b></html>");
                    List<Transaction_Deposit_Withdrawal> transactionDepositWithdrawals = new ArrayList<>(transactionDepositWithdrawalBLL.getTransaction_deposit_withdrawalListAll());
                    transactionDepositWithdrawals.removeIf(Transaction_Deposit_Withdrawal -> !Transaction_Deposit_Withdrawal.getBank_number_account().equals(bank_account.getNumber()));
                    transactionDepositWithdrawals.removeIf(Transaction_Deposit_Withdrawal -> !Transaction_Deposit_Withdrawal.getTransaction_type());

                    loadDataTable(transactionDepositWithdrawalBLL.getData(transactionDepositWithdrawals));
                }
            }
        });
        panel1.add(switchButton);

        String[] columnNames = new String[]{"Thời Gian Giao Dịch", "Loại Giao Dịch", "Số Tiền"};
        dataTable = new DataTable(new Object[0][0], columnNames);

        RoundedScrollPane scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        left.add(scrollPane, "wrap");

        List<Transaction_Deposit_Withdrawal> transactionDepositWithdrawals = new ArrayList<>(transactionDepositWithdrawalBLL.getTransaction_deposit_withdrawalListAll());
        transactionDepositWithdrawals.removeIf(Transaction_Deposit_Withdrawal -> !Transaction_Deposit_Withdrawal.getBank_number_account().equals(bank_account.getNumber()));
        transactionDepositWithdrawals.removeIf(Transaction_Deposit_Withdrawal -> !Transaction_Deposit_Withdrawal.getTransaction_type());

        loadDataTable(transactionDepositWithdrawalBLL.getData(transactionDepositWithdrawals));

        RoundedPanel top = new RoundedPanel();
        top.setBackground(new Color(228,231,235));
        top.setPreferredSize(new Dimension(640, 40));
        right.add(top, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new MigLayout("", "20[]20[]20", "40[]40[]40"));
        content.setBackground(new Color(228,231,235));
        content.setPreferredSize(new Dimension(640, 590));
        right.add(content, BorderLayout.CENTER);

        RoundedPanel containerButton = new RoundedPanel();
        containerButton.setLayout(new FlowLayout());
        containerButton.setBackground(new Color(228,231,235));
        containerButton.setPreferredSize(new Dimension(640, 50));
        right.add(containerButton, BorderLayout.SOUTH);

        JLabel jLabel = new JLabel("Tạo Giao Dịch");
        jLabel.setFont(new Font("Public Sans", Font.BOLD, 18));
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setVerticalAlignment(JLabel.BOTTOM);
        top.add(jLabel);

        for (String string : new String[]{"Mã Giao Dịch", "Nhân Viên", "Khách Hàng", "Số Tài Khoản", "Loại Giao Dịch", "Số Tiền"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(150, 35));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            content.add(label);

            MyTextFieldUnderLine textField = new MyTextFieldUnderLine();
            textField.setPreferredSize(new Dimension(280, 40));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setBackground(new Color(245, 246, 250));
            if (string.equals("Mã Giao Dịch")) {
                textField.setText(String.valueOf(transactionDepositWithdrawalBLL.getAutoID()));
                textField.setEditable(false);
            }
            if (string.equals("Nhân Viên")) {
                textField.setText(HomeGUI.staff.getName());
                textField.setEditable(false);
            }
            if (string.equals("Khách Hàng")) {
                Customer customer = new CustomerBLL().searchCustomers("[no] = '" + bank_account.getCustomer_no() + "'").get(0);
                textField.setText(customer.getName());
                textField.setEditable(false);
            }
            if (string.equals("Số Tài Khoản")) {
                textField.setText(bank_account.getNumber());
                textField.setEditable(false);
            }
            if (string.equals("Loại Giao Dịch")) {
                jComboBox.setPreferredSize(new Dimension(280, 35));
                jComboBox.setFont((new Font("Public Sans", Font.PLAIN, 14)));
                content.add(jComboBox, "wrap");
                continue;
            }
            if (string.equals("Số Tiền")) {
                textField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (!Character.isDigit(e.getKeyChar())) {
                            e.consume();
                        }
                    }
                });
            }
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

        buttonAdd.setPreferredSize(new Dimension(150, 30));
        buttonAdd.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
//                addAccount();
            }
        });
        containerButton.add(buttonAdd);
    }

    public void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        if (objects.length == 0) {
            return;
        }

        Object[][] data = new Object[objects.length][3];

        for (int i = 0; i < objects.length; i++) {
            data[i][0] = objects[i][4];
            data[i][1] = objects[i][2];
            data[i][2] = objects[i][3];
        }

        for (Object[] object : data) {
            model.addRow(object);
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
