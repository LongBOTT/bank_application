package com.bank.GUI.DialogGUI.FormDetailGUI;

import com.bank.BLL.BranchBLL;
import com.bank.BLL.Transaction_Deposit_WithdrawalBLL;
import com.bank.BLL.Transfer_MoneyBLL;
import com.bank.DTO.Branch;
import com.bank.DTO.Bank_Account;
import com.bank.DTO.Customer;
import com.bank.GUI.CustomerGUI;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.StatementGUI;
import com.bank.GUI.TransactionGUI;
import com.bank.GUI.TransferGUI;
import com.bank.GUI.components.Card;
import com.bank.GUI.components.MyTextFieldUnderLine;
import com.bank.GUI.components.RoundedPanel;
import com.bank.GUI.components.barchart.BarChart;
import com.bank.GUI.components.barchart.ModelBarChart;
import com.bank.GUI.components.line_chart.ModelData;
import com.bank.GUI.components.line_chart.chart.CurveLineChart;
import com.bank.GUI.components.line_chart.chart.ModelChart;
import com.bank.GUI.components.line_chart.panel.PanelShadow;
import com.bank.GUI.components.pie_chart.ModelPieChart;
import com.bank.GUI.components.pie_chart.PieChart;
import com.bank.main.Bank_Application;
import com.bank.utils.VNString;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class DetailBank_AccountGUI extends DialogForm {
    private JLabel titleName;
    private List<JLabel> attributeBank_Account;
    private List<JTextField> jTextFieldsBank_Account;
    private JButton buttonTransfer;
    private JButton buttonTransaction;
    private JButton buttonStatement;
    private PieChart pieChart;
    private BarChart barChart;
    private Bank_Account bankAccount;
    public DetailBank_AccountGUI(Bank_Account Bank_Account) {
        super();
        super.setName("DetailBankAccount");
        super.setTitle("Thông Tin Tài khoản Ngân Hàng");
        super.setSize(new Dimension(1000, 700));
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        this.bankAccount = Bank_Account;
        init(Bank_Account);
        setVisible(true);
    }

    private void init(Bank_Account Bank_Account) {
        titleName = new JLabel();
        attributeBank_Account = new ArrayList<>();
        jTextFieldsBank_Account = new ArrayList<>();
        buttonTransfer = new JButton("Chuyển tiền");
        buttonTransaction = new JButton("Giao dịch");
        buttonStatement = new JButton("Sao kê");
        pieChart = new PieChart();
        barChart = new BarChart();

        titleName.setText("Thông Tin Tài khoản Ngân Hàng");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        content.setLayout(new MigLayout("", "5[]5[]5", "5[]5"));
        content.setPreferredSize(new Dimension(1000, 600));

        JPanel panelShadow = new JPanel(new MigLayout("", "0[]0"));
        panelShadow.setPreferredSize(new Dimension(500, 600));
        panelShadow.setBackground(new Color(255, 255, 255));
        panelShadow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        GroupLayout panelShadowLayout = new GroupLayout(panelShadow);
//        panelShadow.setLayout(panelShadowLayout);
//        panelShadowLayout.setHorizontalGroup(
//                panelShadowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelShadowLayout.createSequentialGroup()
//                                .addContainerGap()
//                                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
//                                .addContainerGap())
//        );
//        panelShadowLayout.setVerticalGroup(
//                panelShadowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(panelShadowLayout.createSequentialGroup()
//                                .addContainerGap()
//                                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
//                                .addContainerGap())
//        );

        content.add(panelShadow);

        JLabel jLabelTile1 = new JLabel("Thống kê loại giao dịch 3 tháng gần nhất");
        jLabelTile1.setFont(new Font("Inter", Font.BOLD, 15));
        panelShadow.add(jLabelTile1, "center, span, wrap");

        JPanel jPanelNotice = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jPanelNotice.setBackground(new Color(255, 255, 255));

        JLabel jLabelAvailable = new JLabel("Gửi tiền");
        JPanel jPanelAvailable = new JPanel();
        jPanelAvailable.setPreferredSize(new Dimension(10, 10));
        jPanelAvailable.setBackground(new Color(189, 135, 245));

        jPanelNotice.add(jPanelAvailable);
        jPanelNotice.add(jLabelAvailable);

        JLabel jLabelDone = new JLabel("Rút tiền");
        JPanel jPanelDone = new JPanel();
        jPanelDone.setPreferredSize(new Dimension(10, 10));
        jPanelDone.setBackground(new Color(135, 189, 245));

        jPanelNotice.add(jPanelDone);
        jPanelNotice.add(jLabelDone);

        JLabel jLabelAbsent = new JLabel("Chuyển tiền");
        JPanel jPanelAbsent = new JPanel();
        jPanelAbsent.setPreferredSize(new Dimension(10, 10));
        jPanelAbsent.setBackground(new Color(139, 229, 184));

        jPanelNotice.add(jPanelAbsent);
        jPanelNotice.add(jLabelAbsent);

        panelShadow.add(jPanelNotice, "center, wrap, span");

        pieChart.setPreferredSize(new Dimension(1000, 350));
        pieChart.setFont(new java.awt.Font("Inter", Font.BOLD, 14));
        pieChart.setChartType(PieChart.PeiChartType.DEFAULT);
        panelShadow.add(pieChart, "wrap");

        JLabel jLabelTile2 = new JLabel("Thống kê giá trị giao dịch trong 3 tháng gần nhất ");
        jLabelTile2.setFont(new Font("Inter", Font.BOLD, 15));
        panelShadow.add(jLabelTile2, "center, span, wrap");

        barChart.setPreferredSize(new Dimension(1000, 350));
        barChart.addLegend("Tiền gửi", new Color(189, 135, 245));
        barChart.addLegend("Tiền rút", new Color(135, 189, 245));
        barChart.addLegend("Tiền chuyển", new Color(139, 229, 222));
        panelShadow.add(barChart, "wrap");

//        chart.setTitle("Thống Kê Giao Dịch");
//        chart.addLegend("Gửi Tiền", Color.decode("#7b4397"), Color.decode("#dc2430"));
//        chart.addLegend("Rút Tiền", Color.decode("#e65c00"), Color.decode("#F9D423"));
//        chart.addLegend("Chuyển Tiền", Color.decode("#0099F7"), Color.decode("#F11712"));
//        chart.setForeground(new Color(0, 0, 0));
//        chart.setFillColor(true);
//
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                setData();
            }
        });
        thread.start();


        RoundedPanel panelInfo = new RoundedPanel();
        panelInfo.setLayout(new MigLayout("", "20[]20[]20", "40[]40[]40"));
        panelInfo.setPreferredSize(new Dimension(500, 600));
        content.add(panelInfo, "wrap");

        for (String string : new String[]{"Số Thẻ", "CCCD Chủ Thẻ", "Số Dư", "Chi Nhánh",
                "Ngày Mở", "Trạng Thái"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(150, 35));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 15)));
            attributeBank_Account.add(label);
            panelInfo.add(label);
            MyTextFieldUnderLine textField = new MyTextFieldUnderLine();
            textField.setPreferredSize(new Dimension(280, 35));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setEditable(false);
            if (string.trim().equals("Ngày Mở")) {
                textField.setText(new SimpleDateFormat("dd-MM-yyyy").format(bankAccount.getCreation_date()));
                textField.setEditable(false);
                panelInfo.add(textField, "wrap");
            } else {
                if (string.trim().equals("Số Thẻ")) {
                    String Bank_AccountId = Bank_Account.getNumber();
                    textField.setText(Bank_AccountId);
                    panelInfo.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("CCCD Chủ Thẻ")) {
                    textField.setText(Bank_Account.getCustomer_no());
                    panelInfo.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Số Dư")) {
                    textField.setText(VNString.currency(Double.parseDouble(bankAccount.getBalance().toString())));
                    panelInfo.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Chi Nhánh")) {
                    Branch branch = new BranchBLL().findAllBranchs("id", String.valueOf(Bank_Account.getBranch_id())).get(0);
                    textField.setText(branch.getName());
                    panelInfo.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Trạng Thái")) {
                    String status = Bank_Account.isStatus()? "Đang mở" : "Đã đóng";
                    textField.setText(status);
                    panelInfo.add(textField, "wrap");
                }
            }

        }

        if (Bank_Account.isStatus()) {
            buttonStatement.setPreferredSize(new Dimension(100, 35));
            buttonStatement.setBackground(new Color(1, 120, 220));
            buttonStatement.setForeground(Color.white);
            buttonStatement.setFont(new Font("Public Sans", Font.BOLD, 15));
            buttonStatement.setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttonStatement.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    dispose();
                    Card selectedCard = null;
                    for (Pair pair : CustomerGUI.pairList) {
                        Customer customer = (Customer) pair.getKey();
                        if (Objects.equals(customer.getCustomerNo(), Bank_Account.getCustomer_no())) {
                            List<Card> cardList = (List<Card>) pair.getValue();
                            for (Card card : cardList) {
                                if (card.bankAccount.getNumber().equals(Bank_Account.getNumber())) {
                                    selectedCard = card;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    new StatementGUI(Bank_Account, selectedCard);
                }
            });
            containerButton.add(buttonStatement);

            buttonTransfer.setPreferredSize(new Dimension(150, 35));
            buttonTransfer.setBackground(new Color(1, 120, 220));
            buttonTransfer.setForeground(Color.white);
            buttonTransfer.setFont(new Font("Public Sans", Font.BOLD, 15));
            buttonTransfer.setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttonTransfer.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    dispose();
                    Card selectedCard = null;
                    for (Pair pair : CustomerGUI.pairList) {
                        Customer customer = (Customer) pair.getKey();
                        if (Objects.equals(customer.getCustomerNo(), Bank_Account.getCustomer_no())) {
                            List<Card> cardList = (List<Card>) pair.getValue();
                            for (Card card : cardList) {
                                if (card.bankAccount.getNumber().equals(Bank_Account.getNumber())) {
                                    selectedCard = card;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    new TransferGUI(Bank_Account, selectedCard);
                }
            });
            containerButton.add(buttonTransfer);

            buttonTransaction.setPreferredSize(new Dimension(150, 35));
            buttonTransaction.setBackground(new Color(1, 120, 220));
            buttonTransaction.setForeground(Color.white);
            buttonTransaction.setFont(new Font("Public Sans", Font.BOLD, 15));
            buttonTransaction.setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttonTransaction.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    dispose();
                    Card selectedCard = null;
                    for (Pair pair : CustomerGUI.pairList) {
                        Customer customer = (Customer) pair.getKey();
                        if (Objects.equals(customer.getCustomerNo(), Bank_Account.getCustomer_no())) {
                            List<Card> cardList = (List<Card>) pair.getValue();
                            for (Card card : cardList) {
                                if (card.bankAccount.getNumber().equals(Bank_Account.getNumber())) {
                                    selectedCard = card;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    new TransactionGUI(Bank_Account, selectedCard);
                }
            });
            containerButton.add(buttonTransaction);
        }
    }

    private void setData() {
        pieChart.clearData();
        barChart.clear();

        List<List<String>> totalTransaction = new Transaction_Deposit_WithdrawalBLL().getTotalTransaction_By_Month_In_Year(bankAccount.getNumber());
        List<List<String>> totalTransfer = new Transfer_MoneyBLL().getTotalTransfer_By_Month_In_Year(bankAccount.getNumber());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -2);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

        double totalDepositMoney = 0;
        double totalWithdrawalMoney = 0;
        double totalTransferMoney = 0;
        for (int i = 0; i < 3; i++) {
            String formattedDate = dateFormat.format(cal.getTime());

            String month = formattedDate;
            double deposit = 0;
            double withdrawal = 0;
            double transfer = 0;
            for (List<String> strings : totalTransaction) {
                if (strings.get(0).equals(formattedDate)) {
                    deposit = Double.parseDouble(strings.get(1));
                    withdrawal = Double.parseDouble(strings.get(2));

                    totalDepositMoney += deposit;
                    totalWithdrawalMoney += withdrawal;
                    break;
                }
            }

            for (List<String> strings : totalTransfer) {
                if (strings.get(0).equals(formattedDate)) {
                    transfer = Double.parseDouble(strings.get(1));
                    totalTransferMoney += transfer;
                    break;
                }
            }

            barChart.addData(new ModelBarChart(month, new double[]{deposit, withdrawal, transfer}));


            cal.add(Calendar.MONTH, 1);
        }

        pieChart.addData(new ModelPieChart("Gửi Tiền",totalDepositMoney, new Color(189, 135, 245)));
        pieChart.addData(new ModelPieChart("Rút tiền", totalWithdrawalMoney, new Color(135, 189, 245)));
        pieChart.addData(new ModelPieChart("Chuyển Tiền", totalTransferMoney, new Color(139, 229, 222)));

        barChart.start();

        content.repaint();
        content.revalidate();
    }
}

