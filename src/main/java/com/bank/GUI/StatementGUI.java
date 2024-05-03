package com.bank.GUI;

import com.bank.BLL.*;
import com.bank.DAL.SQLServer;
import com.bank.DTO.Bank_Account;
import com.bank.DTO.Branch;
import com.bank.DTO.Customer;
import com.bank.DTO.Transfer_Money;
import com.bank.GUI.components.*;
import com.bank.GUI.components.line_chart.ModelData;
import com.bank.GUI.components.line_chart.chart.CurveLineChart;
import com.bank.GUI.components.line_chart.chart.ModelChart;
import com.bank.GUI.components.swing.DataSearch;
import com.bank.GUI.components.swing.EventClick;
import com.bank.GUI.components.swing.MyTextField;
import com.bank.GUI.components.swing.PanelSearch;
import com.bank.main.Bank_Application;
import com.bank.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class StatementGUI extends JDialog {
    private JLabel firstBalance;
    private DatePicker datePicker;
    private JFormattedTextField editor;
    private Transfer_MoneyBLL transfer_MoneyBLL = new Transfer_MoneyBLL();
    private Transaction_Deposit_WithdrawalBLL transactionDepositWithdrawalBLL = new Transaction_Deposit_WithdrawalBLL();
    private DataTable dataTable;
    private JButton buttonPrint;
    private JButton buttonSearch;
    private Bank_Account bankAccount;
    private Card card;
    private CurveLineChart chart;
    private RoundedPanel currentPanel = null;
    private SQLServer sqlServer = new SQLServer();
    private Object[][] data;
    public StatementGUI(Bank_Account bank_account, Card card) {
        super((Frame) null, "", true);
        this.bankAccount = bank_account;
        this.card = card;
        this.card.mouseListenerIsActive = false;
        getContentPane().setBackground(new Color(220, 224, 253));
        setTitle("Hệ Thống Sao Kê");
        setLayout(new MigLayout("", "5[]5", "10[]10[]10"));
        setIconImage(new FlatSVGIcon("icon/ACB.svg").getImage());
        setSize(new Dimension(1165, 800));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(Bank_Application.homeGUI);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        buttonPrint = new JButton("In Sao Kê");
        buttonSearch = new JButton("Tìm Kiếm");
        datePicker = new DatePicker();
        editor = new JFormattedTextField();
        chart = new CurveLineChart();

        RoundedPanel top = new RoundedPanel();
        top.setLayout(new MigLayout("", "0[]10[]0", "0[]0"));
        top.setBackground(new Color(220, 224, 253));
        top.setPreferredSize(new Dimension(1165, 170));
        add(top, "wrap");

        RoundedPanel center = new RoundedPanel();
        center.setLayout(new MigLayout("", "[]", "[]"));
        center.setBackground(new Color(255,255,255));
        center.setPreferredSize(new Dimension(1165, 300));
        add(center, "wrap");

        RoundedPanel bottom = new RoundedPanel();
        bottom.setLayout(new MigLayout("", "[]", "[]"));
        bottom.setBackground(new Color(255,255,255));
        bottom.setPreferredSize(new Dimension(1165, 300));
        add(bottom, "wrap");

        RoundedPanel filterPanel = new RoundedPanel();
        filterPanel.setLayout(new MigLayout("", "10[]10", "[]5[]"));
        filterPanel.setBackground(new Color(255,255,255));
        filterPanel.setPreferredSize(new Dimension(825, 170));
        top.add(filterPanel);

        RoundedPanel panelCard = new RoundedPanel();
        panelCard.setLayout(new BorderLayout());
        panelCard.setPreferredSize(new Dimension(330, 170));
        top.add(panelCard, "wrap");

        panelCard.add(card, BorderLayout.CENTER);

        JLabel jLabelTitle = new JLabel("Sao Kê Tài Khoản");
        jLabelTitle.setFont(new Font("Inter", Font.BOLD, 15));
        jLabelTitle.setForeground(new Color(0x919191));
        filterPanel.add(jLabelTitle, "wrap");

        RoundedPanel last3Month = new RoundedPanel();
        last3Month.setLayout(new BorderLayout());
        last3Month.setPreferredSize(new Dimension(200, 100));
        last3Month.setCursor(new Cursor(Cursor.HAND_CURSOR));
        last3Month.setBackground(new Color(237,242,247));
        last3Month.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentPanel != last3Month) {
                    Active(last3Month);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setData(3);
                        }
                    });
                    thread.start();
                    currentPanel = last3Month;
                }
            }
        });
        filterPanel.add(last3Month);

        RoundedPanel last6Month = new RoundedPanel();
        last6Month.setLayout(new BorderLayout());
        last6Month.setCursor(new Cursor(Cursor.HAND_CURSOR));
        last6Month.setPreferredSize(new Dimension(200, 100));
        last6Month.setBackground(new Color(237,242,247));
        last6Month.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentPanel != last6Month) {
                    Active(last6Month);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setData(6);
                        }
                    });
                    thread.start();
                    currentPanel = last6Month;
                }
            }
        });
        filterPanel.add(last6Month);

        RoundedPanel last9Month = new RoundedPanel();
        last9Month.setLayout(new BorderLayout());
        last9Month.setCursor(new Cursor(Cursor.HAND_CURSOR));
        last9Month.setPreferredSize(new Dimension(200, 100));
        last9Month.setBackground(new Color(237,242,247));
        last9Month.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentPanel != last9Month) {
                    Active(last9Month);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setData(9);
                        }
                    });
                    thread.start();
                    currentPanel = last9Month;
                }
            }
        });
        filterPanel.add(last9Month);

        RoundedPanel lastYear = new RoundedPanel();
        lastYear.setLayout(new BorderLayout());
        lastYear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lastYear.setPreferredSize(new Dimension(200, 100));
        lastYear.setBackground(new Color(237,242,247));
        lastYear.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentPanel != lastYear) {
                    Active(lastYear);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setData(12);
                        }
                    });
                    thread.start();
                    currentPanel = lastYear;
                }
            }
        });
        filterPanel.add(lastYear, "wrap");

        JLabel last3MonthLabel = new JLabel("3 tháng gần nhất");
        last3MonthLabel.setFont(new Font("Inter", Font.BOLD, 14));
        last3MonthLabel.setForeground(new Color(115,121,210));
        last3MonthLabel.setVerticalAlignment(JLabel.CENTER);
        last3MonthLabel.setHorizontalAlignment(JLabel.CENTER);
        last3Month.add(last3MonthLabel, BorderLayout.CENTER);

        JLabel last6MonthLabel = new JLabel("6 tháng gần nhất");
        last6MonthLabel.setFont(new Font("Inter", Font.BOLD, 14));
        last6MonthLabel.setForeground(new Color(115,121,210));
        last6MonthLabel.setVerticalAlignment(JLabel.CENTER);
        last6MonthLabel.setHorizontalAlignment(JLabel.CENTER);
        last6Month.add(last6MonthLabel, BorderLayout.CENTER);

        JLabel last9MonthLabel = new JLabel("9 tháng gần nhất");
        last9MonthLabel.setFont(new Font("Inter", Font.BOLD, 14));
        last9MonthLabel.setForeground(new Color(115,121,210));
        last9MonthLabel.setVerticalAlignment(JLabel.CENTER);
        last9MonthLabel.setHorizontalAlignment(JLabel.CENTER);
        last9Month.add(last9MonthLabel, BorderLayout.CENTER);

        JLabel lastYearLabel = new JLabel("1 năm gần nhất");
        lastYearLabel.setFont(new Font("Inter", Font.BOLD, 14));
        lastYearLabel.setForeground(new Color(115,121,210));
        lastYearLabel.setVerticalAlignment(JLabel.CENTER);
        lastYearLabel.setHorizontalAlignment(JLabel.CENTER);
        lastYear.add(lastYearLabel, BorderLayout.CENTER);

        RoundedPanel filterDate = new RoundedPanel();
        filterDate.setLayout(new MigLayout("", "0[]10[]10[]0", "0[]0"));
        filterDate.setPreferredSize(new Dimension(825, 70));
        filterDate.setBackground(new Color(255,255,255));
        filterPanel.add(filterDate, "span");

        datePicker.setDateSelectionMode(raven.datetime.component.date.DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED);
        datePicker.setEditor(editor);
        datePicker.setUsePanelOption(true);
        datePicker.setCloseAfterSelected(true);
        datePicker.addDateSelectionListener(new DateSelectionListener() {
            @Override
            public void dateSelected(DateEvent dateEvent) {
                search();
            }
        });
        editor.setPreferredSize(new Dimension(280, 40));
        editor.setFont(new Font("Inter", Font.BOLD, 15));
        filterDate.add(editor);

        buttonSearch.setIcon(new FlatSVGIcon("icon/search1.svg"));
        buttonSearch.setBackground(new Color(115,121,210));
        buttonSearch.setForeground(Color.white);
        buttonSearch.setPreferredSize(new Dimension(150,60));
        buttonSearch.setFont(new Font("Inter", Font.BOLD, 13));
        buttonSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        filterDate.add(buttonSearch);

        buttonPrint.setIcon(new FlatSVGIcon("icon/print.svg"));
        buttonPrint.setBackground(new Color(115,121,210));
        buttonPrint.setForeground(Color.white);
        buttonPrint.setPreferredSize(new Dimension(150,60));
        buttonPrint.setFont(new Font("Inter", Font.BOLD, 13));
        buttonPrint.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterDate.add(buttonPrint);

        chart.setTitle("Thống Kê Giao Dịch");
        chart.addLegend("Gửi Tiền", Color.decode("#7b4397"), Color.decode("#dc2430"));
        chart.addLegend("Rút Tiền", Color.decode("#e65c00"), Color.decode("#F9D423"));
        chart.addLegend("Chuyển Tiền", Color.decode("#0099F7"), Color.decode("#F11712"));
        chart.setForeground(new Color(0x919191));
        chart.setFillColor(true);

        JPanel panelShadow = new JPanel();
        panelShadow.setPreferredSize(new Dimension(1165, 300));
        panelShadow.setBackground(new Color(255, 255, 255));
        panelShadow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelShadow.setLayout(new BorderLayout());
        panelShadow.add(chart, BorderLayout.CENTER);
        center.add(panelShadow);

        firstBalance = new JLabel();
        firstBalance.setFont(new Font("Inter", Font.BOLD, 15));
        bottom.add(firstBalance, "wrap");

        String[] columnNames = new String[]{"Thời Gian", "Số Tiền Ghi Nợ", "Số Tiền Ghi Có", "Số Dư", "Nội Dung Chi Tiết"};
        dataTable = new DataTable(new Object[0][0], columnNames);
        dataTable.getTableHeader().setFont(new Font("Public Sans", Font.BOLD, 15));
        dataTable.setBackground(Color.white);
        dataTable.setSelectionBackground(new Color(245, 243, 243, 221));
        dataTable.setFont(new Font("Public Sans", Font.PLAIN, 13));
        dataTable.getColumnModel().getColumn(0).setMinWidth(130);
        dataTable.getColumnModel().getColumn(1).setMinWidth(170);
        dataTable.getColumnModel().getColumn(2).setMinWidth(170);
        dataTable.getColumnModel().getColumn(3).setMinWidth(170);
        dataTable.getColumnModel().getColumn(0).setMaxWidth(130);
        dataTable.getColumnModel().getColumn(1).setMaxWidth(170);
        dataTable.getColumnModel().getColumn(2).setMaxWidth(170);
        dataTable.getColumnModel().getColumn(3).setMaxWidth(170);
        dataTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        dataTable.setSelectionBackground(new Color(176, 180, 183));
        dataTable.setRowHeight(20);

        RoundedScrollPane scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1165, 300));
        bottom.add(scrollPane);

        List<Transfer_Money> transfer_Moneys = new ArrayList<>(transfer_MoneyBLL.getTransfer_moneyListAll());
        transfer_Moneys.removeIf(Transfer_Money -> !Transfer_Money.getSender_bank_account_number().equals(bankAccount.getNumber()));

        Active(last3Month);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                setData(3);
            }
        });
        thread.start();
        currentPanel = last3Month;

    }

    public void loadDataTable(List<List<String>> objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        if (objects.isEmpty()) {
            firstBalance.setText("");
            return;
        }

        data = new Object[objects.size()][5];
        double balance = Double.parseDouble(objects.get(0).get(3));
        double amount;
        if (objects.get(0).get(1).equals("0.0")) {
            amount = Double.parseDouble(objects.get(0).get(2));
            balance -= amount;
        }
        else {
            amount = Double.parseDouble(objects.get(0).get(1));
            balance += amount;
        }
        firstBalance.setText("Số Dư Đầu Kỳ: " + VNString.currency(balance));

        for (int i = 0; i < objects.size(); i++) {
            data[i][0] = objects.get(i).get(0);
            if (!objects.get(i).get(1).equals("0.0"))
                data[i][1] = VNString.currency(Double.parseDouble(objects.get(i).get(1)));
            else
                data[i][1] = " ";

            if (!objects.get(i).get(2).equals("0.0"))
                data[i][2] = VNString.currency(Double.parseDouble(objects.get(i).get(2)));
            else
                data[i][2] = " ";

            data[i][3] = VNString.currency(Double.parseDouble(objects.get(i).get(3)));
            data[i][4] = objects.get(i).get(4);
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

    private void setData(int numberOfMonth) {
        datePicker.clearSelectedDate();
        chart.clear();

        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        List<List<String>> objects = new ArrayList<>();
        List<List<String>> statement = sqlServer.getStatement(bankAccount.getNumber());

        for (List<String> strings : statement) {
            strings.set(0, LocalDateTime.parse(strings.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")).format(myFormatObj));
        }

        List<ModelData> lists = new ArrayList<>();

        List<List<String>> totalTransaction = transactionDepositWithdrawalBLL.getTotalTransaction_By_Month_In_Year(bankAccount.getNumber());
        List<List<String>> totalTransfer = transfer_MoneyBLL.getTotalTransfer_By_Month_In_Year(bankAccount.getNumber());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, - numberOfMonth + 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

        for (int i = 0; i < numberOfMonth; i++) {
            String formattedDate = dateFormat.format(cal.getTime());

            String month = formattedDate;
            double deposit = 0;
            double withdrawal = 0;
            double transfer = 0;
            for (List<String> strings : totalTransaction) {
                if (strings.get(0).equals(formattedDate)) {
                    deposit = Double.parseDouble(strings.get(1));
                    withdrawal = Double.parseDouble(strings.get(2));
                    break;
                }
            }

            for (List<String> strings : totalTransfer) {
                if (strings.get(0).equals(formattedDate)) {
                    transfer = Double.parseDouble(strings.get(1));
                    break;
                }
            }
            lists.add(new ModelData(month, deposit, withdrawal, transfer));

            cal.add(Calendar.MONTH, 1);

            List<List<String>> list = new ArrayList<>(statement);
            list.removeIf(strings -> !strings.get(0).contains(formattedDate));
            objects.addAll(list);
        }

        for (ModelData d : lists) {
            chart.addData(new ModelChart(d.getMonth(), new double[]{d.getDeposit(), d.getWithdrawal(), d.getTransfer()}));
        }

        chart.start();

        loadDataTable(objects);

    }

    private void Disable() {
        if (currentPanel != null) {
            currentPanel.setBackground(new Color(237,242,247));
        }
    }

    private void Active(RoundedPanel module) {
        Disable();
        currentPanel = module;
        module.setBackground(new Color(176, 180, 183));
    }

    private void search() {
        if (datePicker.getDateSQL_Between() == null) {

        }else {
            Disable();
            currentPanel = null;

            chart.clear();

            List<ModelData> lists = new ArrayList<>();
            List<List<String>> totalTransaction;
            List<List<String>> totalTransfer;

            Date startDate = datePicker.getDateSQL_Between()[0];
            Date endDate = datePicker.getDateSQL_Between()[1];
            if (startDate.after(endDate)) {
                JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<List<String>> statement = sqlServer.getStatement_By_Date(bankAccount.getNumber(), startDate.toString(), endDate.toString());
            DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            for (List<String> strings : statement) {
                strings.set(0, LocalDateTime.parse(strings.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")).format(myFormat));
            }

            if (startDate.getMonth() != endDate.getMonth()) {

                totalTransaction = transactionDepositWithdrawalBLL.getTotalTransaction_By_Date(bankAccount.getNumber(), startDate.toString(), endDate.toString());

                totalTransfer = transfer_MoneyBLL.getTotalTransfer_By_Date(bankAccount.getNumber(), startDate.toString(), endDate.toString());

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(startDate);
                String formattedDate = dateFormat.format(cal1.getTime());

                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(endDate);
                cal2.add(Calendar.MONTH, 1);
                String end = dateFormat.format(cal2.getTime());

                while (!formattedDate.equals(end)) {
                    String month = formattedDate;
                    double deposit = 0;
                    double withdrawal = 0;
                    double transfer = 0;
                    for (List<String> strings : totalTransaction) {
                        if (strings.get(0).equals(formattedDate)) {
                            deposit = Double.parseDouble(strings.get(1));
                            withdrawal = Double.parseDouble(strings.get(2));
                            break;
                        }
                    }

                    for (List<String> strings : totalTransfer) {
                        if (strings.get(0).equals(formattedDate)) {
                            transfer = Double.parseDouble(strings.get(1));
                            break;
                        }
                    }
                    lists.add(new ModelData(month, deposit, withdrawal, transfer));

                    cal1.add(Calendar.MONTH, 1);
                    formattedDate = dateFormat.format(cal1.getTime());
                }

                for (ModelData d : lists) {
                    chart.addData(new ModelChart(d.getMonth(), new double[]{d.getDeposit(), d.getWithdrawal(), d.getTransfer()}));
                }
                chart.start();
            } else {

                totalTransaction = transactionDepositWithdrawalBLL.getTotalTransaction_In_Month(bankAccount.getNumber(), startDate.toString(), endDate.toString());

                totalTransfer = transfer_MoneyBLL.getTotalTransfer_In_Month(bankAccount.getNumber(), startDate.toString(), endDate.toString());

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(startDate);
                String formattedDate = dateFormat.format(cal1.getTime());

                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(endDate);
                cal2.add(Calendar.DATE, 1);
                String end = dateFormat.format(cal2.getTime());

                while (!formattedDate.equals(end)) {
                    String month = formattedDate;
                    double deposit = 0;
                    double withdrawal = 0;
                    double transfer = 0;
                    for (List<String> strings : totalTransaction) {
                        if (strings.get(0).equals(formattedDate)) {
                            deposit = Double.parseDouble(strings.get(1));
                            withdrawal = Double.parseDouble(strings.get(2));
                            break;
                        }
                    }

                    for (List<String> strings : totalTransfer) {
                        if (strings.get(0).equals(formattedDate)) {
                            transfer = Double.parseDouble(strings.get(1));
                            break;
                        }
                    }
                    lists.add(new ModelData(month, deposit, withdrawal, transfer));

                    cal1.add(Calendar.DATE, 1);
                    formattedDate = dateFormat.format(cal1.getTime());
                }

                for (ModelData d : lists) {
                    chart.addData(new ModelChart(d.getMonth(), new double[]{d.getDeposit(), d.getWithdrawal(), d.getTransfer()}));
                }
                chart.start();
            }

            loadDataTable(statement);
        }
    }
}
