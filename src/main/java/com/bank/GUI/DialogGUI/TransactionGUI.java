package com.bank.GUI.DialogGUI;

import com.bank.BLL.CustomerBLL;
import com.bank.BLL.Transaction_Deposit_WithdrawalBLL;
import com.bank.DTO.Bank_Account;
import com.bank.DTO.Customer;
import com.bank.DTO.Transaction_Deposit_Withdrawal;
import com.bank.GUI.CustomerGUI;
import com.bank.GUI.DialogGUI.FormDetailGUI.DetailCustomerGUI;
import com.bank.GUI.HomeGUI;
import com.bank.GUI.components.*;
import com.bank.main.Bank_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionGUI extends JDialog {
    private Transaction_Deposit_WithdrawalBLL transactionDepositWithdrawalBLL = new Transaction_Deposit_WithdrawalBLL();
    private SwitchButton switchButton;
    private DataTable dataTable;
    private JLabel jLabelTitle;
    private JComboBox<String> jComboBox;
    private MyTextFieldUnderLine myTextFieldUnderLine;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private JTextArea jTextArea;
    public TransactionGUI(Bank_Account bank_account, Card card) {
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
        initComponents(bank_account, card);
        setVisible(true);
    }

    private void initComponents(Bank_Account bank_account, Card card) {
        switchButton =  new SwitchButton();
        myTextFieldUnderLine = new MyTextFieldUnderLine();
        jComboBox = new JComboBox<>(new String[]{"Gửi Tiền", "Rút Tiền"});
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Giao Dịch");
        jTextArea = new JTextArea();

        RoundedPanel left = new RoundedPanel();
        left.setLayout(new MigLayout("", "[]", "[]5[]5[]"));
        left.setBackground(new Color(228,231,235));
        left.setPreferredSize(new Dimension(500, 680));
        add(left, BorderLayout.WEST);

        RoundedPanel right = new RoundedPanel();
        right.setLayout(new MigLayout("", "[]", "[]0[]0[]"));
        right.setBackground(new Color(228,231,235));
        right.setPreferredSize(new Dimension(640, 680));
        add(right, BorderLayout.EAST);

        RoundedPanel panelCard = new RoundedPanel();
        panelCard.setLayout(new BorderLayout());
        panelCard.setPreferredSize(new Dimension(300, 170));
        left.add(panelCard, "center, wrap");

        panelCard.add(card, BorderLayout.CENTER);

        RoundedPanel panel = new RoundedPanel();
        panel.setBackground(new Color(228,231,235));
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 40));
        left.add(panel, "wrap");

        jLabelTitle = new JLabel("Lịch Sử Giao Dịch Gửi Tiền");
        jLabelTitle.setFont(new Font("Public Sans", Font.BOLD, 15));
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
                    jLabelTitle.setText("Lịch Sử Giao Dịch Rút Tiền");
                    List<Transaction_Deposit_Withdrawal> transactionDepositWithdrawals = new ArrayList<>(transactionDepositWithdrawalBLL.getTransaction_deposit_withdrawalListAll());
                    transactionDepositWithdrawals.removeIf(Transaction_Deposit_Withdrawal -> !Transaction_Deposit_Withdrawal.getBank_number_account().equals(bank_account.getNumber()));
                    transactionDepositWithdrawals.removeIf(Transaction_Deposit_Withdrawal::getTransaction_type);

                    loadDataTable(transactionDepositWithdrawalBLL.getData(transactionDepositWithdrawals));
                }
                else {
                    jLabelTitle.setText("Lịch Sử Giao Dịch Gửi Tiền");
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
        dataTable.getTableHeader().setFont(new Font("Public Sans", Font.BOLD, 15));
        dataTable.setBackground(Color.white);
        dataTable.setSelectionBackground(new Color(245, 243, 243, 221));

        RoundedScrollPane scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        left.add(scrollPane, "wrap");

        List<Transaction_Deposit_Withdrawal> transactionDepositWithdrawals = new ArrayList<>(transactionDepositWithdrawalBLL.getTransaction_deposit_withdrawalListAll());
        transactionDepositWithdrawals.removeIf(Transaction_Deposit_Withdrawal -> !Transaction_Deposit_Withdrawal.getBank_number_account().equals(bank_account.getNumber()));
        transactionDepositWithdrawals.removeIf(Transaction_Deposit_Withdrawal -> !Transaction_Deposit_Withdrawal.getTransaction_type());

        loadDataTable(transactionDepositWithdrawalBLL.getData(transactionDepositWithdrawals));

        RoundedPanel top = new RoundedPanel();
        top.setLayout(new BorderLayout());
        top.setBackground(new Color(112,130,236));
        top.setPreferredSize(new Dimension(630, 150));
        top.setBorder(BorderFactory.createMatteBorder(0,0,15,0, new Color(112,130,236)));
        right.add(top, "wrap");

        JPanel content = new JPanel();
        content.setLayout(new MigLayout("", "0[]10[]0", "20[]10[]20[]10[]10[]0"));
        content.setBackground(new Color(255,255,255));
        content.setPreferredSize(new Dimension(630, 400));
        right.add(content,"wrap");

        JPanel containerButton = new JPanel();
        containerButton.setLayout(new FlowLayout(FlowLayout.CENTER));
        containerButton.setBackground(new Color(255,255,255));
        containerButton.setPreferredSize(new Dimension(630, 75));
        right.add(containerButton, "wrap");

        JLabel jLabel = new JLabel("Giao Dịch Gửi/Rút Tiền");
        jLabel.setFont(new Font("Public Sans", Font.BOLD, 30));
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setVerticalAlignment(JLabel.CENTER);
        jLabel.setForeground(Color.white);
        top.add(jLabel, BorderLayout.CENTER);

        JLabel jLabel1 = new JLabel("Số Tài Khoản");
        jLabel1.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
        jLabel1.setPreferredSize(new Dimension(310, 20));
        jLabel1.setFont((new Font("Inter", Font.BOLD, 13)));
        content.add(jLabel1);

        JLabel jLabel2 = new JLabel("Khách Hàng");
        jLabel2.setPreferredSize(new Dimension(310, 20));
        jLabel2.setFont((new Font("Inter", Font.BOLD, 13)));
        content.add(jLabel2, "wrap");

        MyTextFieldUnderLine textField1 = new MyTextFieldUnderLine();
        textField1.setPreferredSize(new Dimension(285, 40));
        textField1.setFont((new Font("Inter", Font.PLAIN, 14)));
        textField1.setBackground(new Color(246, 246, 246));
        textField1.setOpaque(true);
        textField1.setEditable(false);
        textField1.setText(bank_account.getNumber());
        content.add(textField1, "right");

        Customer customer = new CustomerBLL().searchCustomers("[no] = '" + bank_account.getCustomer_no() + "'").get(0);

        MyTextFieldUnderLine textField2 = new MyTextFieldUnderLine();
        textField2.setPreferredSize(new Dimension(290, 40));
        textField2.setFont((new Font("Inter", Font.PLAIN, 14)));
        textField2.setBackground(new Color(246, 246, 246));
        textField2.setOpaque(true);
        textField2.setEditable(false);
        textField2.setText(customer.getName());
        content.add(textField2, "left, wrap");

        JPanel jPanel  = new JPanel(new MigLayout("", "20[]10[]20", "20[]10[]0"));
        jPanel.setBackground(new Color(237, 239, 253));
        jPanel.setPreferredSize(new Dimension(640, 120));
        content.add(jPanel, "span");

        JLabel jLabel3 = new JLabel("Loại Giao Dịch");
        jLabel3.setPreferredSize(new Dimension(310, 20));
        jLabel3.setFont((new Font("Inter", Font.BOLD, 13)));
        jPanel.add(jLabel3);

        JLabel jLabel4 = new JLabel("Số Tiền");
        jLabel4.setPreferredSize(new Dimension(310, 20));
        jLabel4.setFont((new Font("Inter", Font.BOLD, 13)));
        jPanel.add(jLabel4, "wrap");

        jComboBox.setPreferredSize(new Dimension(310, 40));
        jComboBox.setFont((new Font("Inter", Font.PLAIN, 14)));
        jPanel.add(jComboBox);

        myTextFieldUnderLine.setPreferredSize(new Dimension(310, 40));
        myTextFieldUnderLine.setFont((new Font("Inter", Font.PLAIN, 14)));
        myTextFieldUnderLine.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });
        jPanel.add(myTextFieldUnderLine, "wrap");

        JLabel jLabel5 = new JLabel("Nội Dung");
        jLabel5.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
        jLabel5.setPreferredSize(new Dimension(310, 20));
        jLabel5.setFont((new Font("Inter", Font.BOLD, 13)));
        content.add(jLabel5, "wrap");

        jTextArea.setPreferredSize(new Dimension(590, 150));
        jTextArea.setMaximumSize(new Dimension(590, 150));
        jTextArea.setFont((new Font("Inter", Font.PLAIN, 14)));
        jTextArea.setBackground(new Color(246, 246, 246));
        content.add(jTextArea, "span, center");

        buttonCancel.setPreferredSize(new Dimension(100, 50));
        buttonCancel.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.setBackground(new Color(237, 239, 253));
        buttonCancel.setForeground(new Color(112,130,236));
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        containerButton.add(buttonCancel);

        buttonAdd.setPreferredSize(new Dimension(150, 50));
        buttonAdd.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.setBackground(new Color(112,130,236));
        buttonAdd.setForeground(Color.white);
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTransaction(bank_account, card);
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
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1)
            dispose();
    }

    private void addTransaction(Bank_Account bank_account, Card card) {
        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận thực hiện giao dịch?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1) {
            if (myTextFieldUnderLine.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập số tiền giao dịch!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id, staff_id;
            boolean transaction_type;
            LocalDateTime transaction_date;
            BigDecimal money_amount = new BigDecimal(myTextFieldUnderLine.getText());
            String description;

            id = transactionDepositWithdrawalBLL.getAutoID();
            staff_id = HomeGUI.staff.getId();
            transaction_type = jComboBox.getSelectedIndex() == 0;
            transaction_date = LocalDateTime.now();
            description = jTextArea.getText();

            if (!transaction_type && bank_account.getBalance().compareTo(money_amount) < 0) {
                JOptionPane.showMessageDialog(null, "Số tiền trong tài khoản không đủ để thực hiện giao dịch!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Pair<Boolean, String> result;
            Transaction_Deposit_Withdrawal transaction_deposit_withdrawal = new Transaction_Deposit_Withdrawal(id, bank_account.getNumber(), transaction_type, transaction_date, money_amount, description, staff_id);
            result = transactionDepositWithdrawalBLL.addTransaction_Deposit_Withdrawal(transaction_deposit_withdrawal);

            if (result.getKey()) {
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
    }

}
