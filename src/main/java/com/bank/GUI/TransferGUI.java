package com.bank.GUI;

import com.bank.BLL.*;
import com.bank.DTO.*;
import com.bank.GUI.CustomerGUI;
import com.bank.GUI.HomeGUI;
import com.bank.GUI.Transfer_MoneyGUI;
import com.bank.GUI.components.*;
import com.bank.GUI.components.swing.DataSearch;
import com.bank.GUI.components.swing.EventClick;
import com.bank.GUI.components.swing.MyTextField;
import com.bank.GUI.components.swing.PanelSearch;
import com.bank.main.Bank_Application;
import com.bank.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class TransferGUI extends JDialog {
    private Transfer_MoneyBLL transfer_MoneyBLL = new Transfer_MoneyBLL();
    private DataTable dataTable;
    private JLabel jLabelTitle;
    private MyTextField txtSearch;
    private PanelSearch search;
    private JPopupMenu menu;
    private MyTextFieldUnderLine myTextFieldUnderLine;
    private MyTextFieldUnderLine myTextFieldUnderLine1;
    private MyTextFieldUnderLine myTextFieldUnderLine2;
    private MyTextFieldUnderLine myTextFieldUnderLine3;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private JTextArea jTextArea;
    private Bank_Account bankAccount;
    private Card card;
    private String receiver_bank_account_number = "";
    private Bank_AccountBLL bankAccountBLL = new Bank_AccountBLL();
    private BranchBLL branchBLL = new BranchBLL();
    private CustomerBLL customerBLL = new CustomerBLL();
    private String defaultContent;
    private JComboBox<String> jComboBox;
    private boolean selected = false;
    public TransferGUI(Bank_Account bank_account, Card card) {
        super((Frame) null, "", true);
        this.bankAccount = bank_account;
        this.card = card;
        this.card.mouseListenerIsActive = false;
        getContentPane().setBackground(new Color(228,231,235));
        setTitle("Hệ Thống Chuyển Tiền");
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
                receiver_bank_account_number = data.getText();
                loadReceiver();
            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                search.remove(com);
                menu.setPopupSize(285, (search.getItemSize() * 35) + 2);
                if (search.getItemSize() == 0) {
                    menu.setVisible(false);
                }
            }
        });
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        myTextFieldUnderLine = new MyTextFieldUnderLine();
        myTextFieldUnderLine1 = new MyTextFieldUnderLine();
        myTextFieldUnderLine2 = new MyTextFieldUnderLine();
        myTextFieldUnderLine3 = new MyTextFieldUnderLine();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Chuyển Tiền");
        txtSearch = new MyTextField();
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

        jLabelTitle = new JLabel("Lịch Sử Chuyển Tiền");
        jLabelTitle.setFont(new Font("Public Sans", Font.BOLD, 15));
        panel.add(jLabelTitle, BorderLayout.WEST);

        String[] columnNames = new String[]{"Thời Gian CT", "TK Thụ Hưởng", "Số Tiền"};
        dataTable = new DataTable(new Object[0][0], columnNames);
        dataTable.getTableHeader().setFont(new Font("Public Sans", Font.BOLD, 15));
        dataTable.setBackground(Color.white);
        dataTable.setSelectionBackground(new Color(245, 243, 243, 221));

        RoundedScrollPane scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        left.add(scrollPane, "wrap");

        List<Transfer_Money> transfer_Moneys = new ArrayList<>(transfer_MoneyBLL.getTransfer_moneyListAll());
        transfer_Moneys.removeIf(Transfer_Money -> !Transfer_Money.getSender_bank_account_number().equals(bankAccount.getNumber()));

        loadDataTable(transfer_MoneyBLL.getData(transfer_Moneys));

        RoundedPanel top = new RoundedPanel();
        top.setLayout(new BorderLayout());
        top.setBackground(new Color(112,130,236));
        top.setPreferredSize(new Dimension(630, 60));
        top.setBorder(BorderFactory.createMatteBorder(0,0,15,0, new Color(112,130,236)));
        right.add(top, "wrap");

        JPanel content = new JPanel();
        content.setLayout(new MigLayout("", "0[]10[]0", "20[]10[]20[]10[]10[]10[]10[]0"));
        content.setBackground(new Color(255,255,255));
        content.setPreferredSize(new Dimension(630, 490));
        right.add(content,"wrap");

        JPanel containerButton = new JPanel();
        containerButton.setLayout(new FlowLayout(FlowLayout.CENTER));
        containerButton.setBackground(new Color(255,255,255));
        containerButton.setPreferredSize(new Dimension(630, 75));
        right.add(containerButton, "wrap");

        JLabel jLabel = new JLabel("Chuyển Tiền Trong Ngân Hàng");
        jLabel.setFont(new Font("Public Sans", Font.BOLD, 30));
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setVerticalAlignment(JLabel.CENTER);
        jLabel.setForeground(Color.white);
        top.add(jLabel, BorderLayout.SOUTH);

        JLabel jLabel1 = new JLabel("Số Tài Khoản Gửi");
        jLabel1.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
        jLabel1.setPreferredSize(new Dimension(310, 20));
        jLabel1.setFont((new Font("Inter", Font.BOLD, 13)));
        content.add(jLabel1);

        JLabel jLabel2 = new JLabel("Khách Hàng Gửi");
        jLabel2.setPreferredSize(new Dimension(310, 20));
        jLabel2.setFont((new Font("Inter", Font.BOLD, 13)));
        content.add(jLabel2, "wrap");

        MyTextFieldUnderLine textField1 = new MyTextFieldUnderLine();
        textField1.setPreferredSize(new Dimension(285, 40));
        textField1.setFont((new Font("Inter", Font.PLAIN, 14)));
        textField1.setBackground(new Color(246, 246, 246));
        textField1.setOpaque(true);
        textField1.setEditable(false);
        textField1.setText(bankAccount.getNumber());
        content.add(textField1, "right");

        Customer customer = customerBLL.searchCustomers("[no] = '" + bankAccount.getCustomer_no() + "'").get(0);

        MyTextFieldUnderLine textField2 = new MyTextFieldUnderLine();
        textField2.setPreferredSize(new Dimension(290, 40));
        textField2.setFont((new Font("Inter", Font.PLAIN, 14)));
        textField2.setBackground(new Color(246, 246, 246));
        textField2.setOpaque(true);
        textField2.setEditable(false);
        textField2.setText(customer.getName());
        content.add(textField2, "left, wrap");

        JLabel jLabel7 = new JLabel("Chi Nhánh Gửi");
        jLabel7.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
        jLabel7.setPreferredSize(new Dimension(310, 20));
        jLabel7.setFont((new Font("Inter", Font.BOLD, 13)));
        content.add(jLabel7);

        JLabel jLabel8 = new JLabel("Trụ Sở Gửi");
        jLabel8.setPreferredSize(new Dimension(310, 20));
        jLabel8.setFont((new Font("Inter", Font.BOLD, 13)));
        content.add(jLabel8, "wrap");

        Branch branch = branchBLL.findAllBranchs("id", String.valueOf(bankAccount.getBranch_id())).get(0);

        MyTextFieldUnderLine textField3 = new MyTextFieldUnderLine();
        textField3.setPreferredSize(new Dimension(285, 40));
        textField3.setFont((new Font("Inter", Font.PLAIN, 14)));
        textField3.setBackground(new Color(246, 246, 246));
        textField3.setOpaque(true);
        textField3.setEditable(false);
        textField3.setText(branch.getName());
        content.add(textField3, "right");


        MyTextFieldUnderLine textField4 = new MyTextFieldUnderLine();
        textField4.setPreferredSize(new Dimension(290, 40));
        textField4.setFont((new Font("Inter", Font.PLAIN, 14)));
        textField4.setBackground(new Color(246, 246, 246));
        textField4.setOpaque(true);
        textField4.setEditable(false);

        if (branch.getHeadquarter_id() == 1)
            textField4.setText("Hồ Chí Minh");

        else if (branch.getHeadquarter_id() == 2)
            textField4.setText("Hà Nội");


        else if (branch.getHeadquarter_id() == 3)
            textField4.setText("Hải Phòng");


        else if (branch.getHeadquarter_id() == 4)
            textField4.setText("Đà Nẵng");

        else textField4.setText("Khác");

        content.add(textField4, "left, wrap");

        JPanel jPanel  = new JPanel(new MigLayout("", "20[]10[]20", "20[]10[]0"));
        jPanel.setBackground(new Color(237, 239, 253));
        jPanel.setPreferredSize(new Dimension(640, 160));
        content.add(jPanel, "span");

        JLabel jLabel3 = new JLabel("Số Tài Khoản Thụ Hưởng");
        jLabel3.setPreferredSize(new Dimension(310, 20));
        jLabel3.setFont((new Font("Inter", Font.BOLD, 13)));
        jPanel.add(jLabel3);

        JLabel jLabel4 = new JLabel("Khách Hàng Thụ Hưởng");
        jLabel4.setPreferredSize(new Dimension(310, 20));
        jLabel4.setFont((new Font("Inter", Font.BOLD, 13)));
        jPanel.add(jLabel4, "wrap");

        txtSearch.setBackground(new Color(255, 255, 255));
        txtSearch.putClientProperty("JTextField.placeholderText", "Nhập số tài khoản tìm kiếm");
        txtSearch.setPreferredSize(new Dimension(285, 40));
        txtSearch.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                txtSearchMouseClicked(evt);
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
            public void keyReleased(KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        jPanel.add(txtSearch);

        myTextFieldUnderLine1.setPreferredSize(new Dimension(290, 40));
        myTextFieldUnderLine1.setFont((new Font("Inter", Font.PLAIN, 14)));
        myTextFieldUnderLine1.setEditable(false);
        jPanel.add(myTextFieldUnderLine1, "left, wrap");

        JLabel jLabel9 = new JLabel("Chi Nhánh Thụ Hưởng");
        jLabel9.setPreferredSize(new Dimension(310, 20));
        jLabel9.setFont((new Font("Inter", Font.BOLD, 13)));
        jPanel.add(jLabel9);

        JLabel jLabel10 = new JLabel("Trụ Sở Thụ Hưởng");
        jLabel10.setPreferredSize(new Dimension(310, 20));
        jLabel10.setFont((new Font("Inter", Font.BOLD, 13)));
        jPanel.add(jLabel10, "wrap");

        myTextFieldUnderLine2.setPreferredSize(new Dimension(290, 40));
        myTextFieldUnderLine2.setFont((new Font("Inter", Font.PLAIN, 14)));
        myTextFieldUnderLine2.setEditable(false);
        jPanel.add(myTextFieldUnderLine2);

        jComboBox = new JComboBox<>(new String[]{" ", "Hồ Chí Minh", "Hà Nội", "Hải Phòng", "Đà Nẵng", "Khác"});
        jComboBox.setPreferredSize(new Dimension(290, 40));
        jComboBox.setFont((new Font("Inter", Font.PLAIN, 14)));
        jComboBox.setEditable(false);
        jComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!selected) {
                    txtSearch.setText("");
                    receiver_bank_account_number = "";
                    myTextFieldUnderLine1.setText("");
                    myTextFieldUnderLine2.setText("");
                    myTextFieldUnderLine.setText("");
                    jTextArea.setText(defaultContent);
                }
            }
        });
        jPanel.add(jComboBox, "left, wrap");

        JLabel jLabel5 = new JLabel("Số Tiền");
        jLabel5.setPreferredSize(new Dimension(310, 20));
        jLabel5.setFont((new Font("Inter", Font.BOLD, 13)));
        jPanel.add(jLabel5, "wrap");

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

        JLabel jLabel6 = new JLabel("Nội Dung");
        jLabel6.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
        jLabel6.setPreferredSize(new Dimension(310, 20));
        jLabel6.setFont((new Font("Inter", Font.BOLD, 13)));
        content.add(jLabel6, "wrap");

        String normalized = Normalizer.normalize(customer.getName(), Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        defaultContent = pattern.matcher(normalized).replaceAll("").toUpperCase() + " chuyen khoan";

        jTextArea.setText(defaultContent);
        jTextArea.setPreferredSize(new Dimension(590, 110));
        jTextArea.setMaximumSize(new Dimension(590, 140));
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
                addTransaction();
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
        if (choice == 1) {
            this.card.mouseListenerIsActive = true;
            dispose();
        }
    }

    private void addTransaction() {
        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận chuyển tiền?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1) {
            if (receiver_bank_account_number.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn tài khoản thụ hưởng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (myTextFieldUnderLine.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập số tiền giao dịch!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id, staff_id;
            LocalDateTime send_date;
            BigDecimal money_amount = new BigDecimal(myTextFieldUnderLine.getText());
            String description;

            id = transfer_MoneyBLL.getAutoID();
            staff_id = HomeGUI.staff.getId();
            send_date = LocalDateTime.now();

            String normalized = Normalizer.normalize(jTextArea.getText(), Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            description = pattern.matcher(normalized).replaceAll("");

            if (bankAccount.getBalance().compareTo(money_amount) < 0) {
                JOptionPane.showMessageDialog(null, "Số tiền trong tài khoản không đủ để thực hiện giao dịch!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (money_amount.compareTo(new BigDecimal(30000000)) > 0) {
                JOptionPane.showMessageDialog(null, "Hạn mức chuyển tiền tối đa/lần: 30.000.000 VNĐ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Transfer_Money> transfer_MoneysList = new ArrayList<>(transfer_MoneyBLL.getTransfer_moneyListAll());
            transfer_MoneysList.removeIf(Transfer_Money -> !Transfer_Money.getSender_bank_account_number().equals(bankAccount.getNumber()));
            transfer_MoneysList.removeIf(transferMoney -> !transferMoney.getSend_date().toLocalDate().equals(LocalDate.now()));
            BigDecimal total = new BigDecimal(0);
            for (Transfer_Money transferMoney : transfer_MoneysList)
                total = total.add(transferMoney.getMoney_amount());
            BigDecimal decimal = total.add(money_amount);
            if (decimal.compareTo(new BigDecimal(50000000)) > 0) {
                JOptionPane.showMessageDialog(null, "Hạn mức chuyển tiền tối đa/ngày tại quầy: 50.000.000 VNĐ!\nKhách hàng đã chuyển " + VNString.currency(Double.parseDouble(total.toString())) + " trong hôm nay." , "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Pair<Boolean, String> result;
            Transfer_Money transfer_Money = new Transfer_Money(id, bankAccount.getNumber(), receiver_bank_account_number, send_date, money_amount, description, staff_id);
            result = transfer_MoneyBLL.addTransfer_Money(transfer_Money);

            if (result.getKey()) {
                Circle_ProgressBar circleProgressBar = new Circle_ProgressBar();
                circleProgressBar.getRootPane ().setOpaque (false);
                circleProgressBar.getContentPane ().setBackground (new Color (0, 0, 0, 0));
                circleProgressBar.setBackground (new Color (0, 0, 0, 0));
                circleProgressBar.progress();
                circleProgressBar.setVisible(true);
                JOptionPane.showMessageDialog(null, result.getValue(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                bankAccountBLL = new Bank_AccountBLL();
                bankAccount = bankAccountBLL.findAllBank_Accounts("number", bankAccount.getNumber()).get(0);

                card.bankAccount = bankAccount;
                card.balance.setText(VNString.currency(Double.parseDouble(bankAccount.getBalance().toString())));

                txtSearch.setText("");
                receiver_bank_account_number = "";
                myTextFieldUnderLine1.setText("");
                myTextFieldUnderLine2.setText("");
                jComboBox.setSelectedIndex(0);
                myTextFieldUnderLine.setText("");
                jTextArea.setText(defaultContent);

                transfer_MoneyBLL = new Transfer_MoneyBLL();
                List<Transfer_Money> transfer_Moneys = new ArrayList<>(transfer_MoneyBLL.getTransfer_moneyListAll());
                transfer_Moneys.removeIf(Transfer_Money -> !Transfer_Money.getSender_bank_account_number().equals(bankAccount.getNumber()));

                loadDataTable(transfer_MoneyBLL.getData(transfer_Moneys));

                if (Bank_Application.homeGUI.indexModuleTransferGUI != -1) {
                    Transfer_MoneyGUI indexModuleTransfer_MoneyGUI = (Transfer_MoneyGUI) Bank_Application.homeGUI.allPanelModules[Bank_Application.homeGUI.indexModuleTransferGUI];
                    indexModuleTransfer_MoneyGUI.refresh();
                }

                if (Bank_Application.homeGUI.indexModuleBank_AccountGUI != -1) {
                    Bank_AccountGUI bankAccountGUI = (Bank_AccountGUI) Bank_Application.homeGUI.allPanelModules[Bank_Application.homeGUI.indexModuleBank_AccountGUI];
                    bankAccountGUI.refresh();
                }

                Bank_Account receiveBankAccount = bankAccountBLL.findAllBank_Accounts("number", transfer_Money.getReceiver_bank_account_number()).get(0);
                for (Pair pair : CustomerGUI.pairList) {
                    Customer customer = (Customer) pair.getKey();
                    if (Objects.equals(customer.getCustomerNo(), receiveBankAccount.getCustomer_no())) {
                        List<Card> cardList = (List<Card>) pair.getValue();
                        for (Card card : cardList) {
                            if (card.bankAccount.getNumber().equals(receiveBankAccount.getNumber())) {
                                card.bankAccount = receiveBankAccount;
                                card.balance.setText(VNString.currency(Double.parseDouble(receiveBankAccount.getBalance().toString())));
                                break;
                            }
                        }
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadReceiver() {
        List<Bank_Account> bankAccounts = bankAccountBLL.findAllBank_Accounts("number", receiver_bank_account_number);
        if (bankAccounts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy người thụ hưởng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            myTextFieldUnderLine1.setText("Không tìm thấy người thụ hưởng");
        } else {
            Bank_Account receiver_bank_account = bankAccounts.get(0);
            Customer customer = customerBLL.searchCustomers("[no] = '" + receiver_bank_account.getCustomer_no() + "'").get(0);
            Branch branch = branchBLL.findAllBranchs("id", String.valueOf(receiver_bank_account.getBranch_id())).get(0);
            myTextFieldUnderLine1.setText(customer.getName());
            myTextFieldUnderLine2.setText(branch.getName());
            if (jComboBox.getSelectedIndex() == 0) {
                if (branch.getHeadquarter_id() > 4) {
                    selected = true;
                    jComboBox.setSelectedIndex(5);
                    selected = false;
                }
                else {
                    selected = true;
                    jComboBox.setSelectedIndex(branch.getHeadquarter_id());
                    selected = false;
                }
            }

        }
    }

    private void txtSearchMouseClicked(MouseEvent evt) {
        if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
            menu.show(txtSearch, 0, txtSearch.getHeight());
            search.clearSelected();
        }
    }

    private List<DataSearch> search(String text) {
        receiver_bank_account_number = "";
        selected = false;
        List<DataSearch> list = new ArrayList<>();

        List<Bank_Account> bankAccounts = bankAccountBLL.findAllBank_Accounts("number", text);
        if (jComboBox.getSelectedIndex() == 1 || jComboBox.getSelectedIndex() == 2 || jComboBox.getSelectedIndex() == 3 || jComboBox.getSelectedIndex() == 4) {
            bankAccounts.removeIf(bank_account -> branchBLL.findAllBranchs("id", String.valueOf(bank_account.getBranch_id())).get(0).getHeadquarter_id() != jComboBox.getSelectedIndex());
        }
        if (jComboBox.getSelectedIndex() == 5) {
            bankAccounts.removeIf(bank_account -> branchBLL.findAllBranchs("id", String.valueOf(bank_account.getBranch_id())).get(0).getHeadquarter_id() <= 4);
        }
        bankAccounts.removeIf(bank_account -> Objects.equals(bank_account.getNumber(), bankAccount.getNumber()));
        bankAccounts.removeIf(bank_account -> !bank_account.isStatus());
        for (Bank_Account m : bankAccounts) {
            if (list.size() == 7)
                break;
            list.add(new DataSearch(m.getNumber()));
        }
        return list;
    }

    private void txtSearchKeyReleased(KeyEvent evt) {
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtSearch.getText().trim().toLowerCase();
            search.setData(search(text));
            if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
                menu.show(txtSearch, 0, txtSearch.getHeight());
                menu.setPopupSize(285, (search.getItemSize() * 35) + 2);
            } else {
                menu.setVisible(false);
            }
        }
    }

    private void txtSearchKeyPressed(KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
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
