package com.bank.GUI;


import com.bank.BLL.*;
import com.bank.DTO.Branch;
import com.bank.GUI.components.*;
import com.bank.GUI.components.barchart.BarChart;
import com.bank.GUI.components.barchart.ModelBarChart;
import com.bank.GUI.components.line_chart.ModelData;
import com.bank.GUI.components.line_chart.chart.CurveLineChart;
import com.bank.GUI.components.line_chart.chart.ModelChart;
import com.bank.GUI.components.swing.DataSearch;
import com.bank.GUI.components.swing.EventClick;
import com.bank.GUI.components.swing.MyTextField;
import com.bank.GUI.components.swing.PanelSearch;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Background;
import net.miginfocom.swing.MigLayout;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class StatisticGUI extends RoundedPanel {
    private RoundedPanel customerPanel;
    private RoundedPanel bank_accountPanel;
    private RoundedPanel transactionPanel;
    private RoundedPanel staffPanel;
    private PanelSearch searchCustomer;
    private JPopupMenu menuCustomer;
    private MyTextField txtSearchCustomer;
    private PanelSearch searchBank_Account;
    private JPopupMenu menuBank_Account;
    private MyTextField txtSearchBank_Account;
    private PanelSearch searchStaff;
    private JPopupMenu menuStaff;
    private MyTextField txtSearchStaff;
    private BranchBLL branchBLL = new BranchBLL();
    private Bank_AccountBLL bankAccountBLL = new Bank_AccountBLL();
    private List<Branch> branchListCustomer = new ArrayList<>();
    private List<Branch> branchListStaff = new ArrayList<>();
    private RoundedPanel filterBranchCustomerPanel;
    private RoundedPanel dashboardCustomerPanel;
    private RoundedPanel filterBranchBank_AccountPanel;
    private RoundedPanel dashboardBank_AccountPanel;
    private RoundedPanel filterBranchStaffPanel;
    private RoundedPanel dashboardStaffPanel;
    private final Color series1Color = new Color(0x752333);
    private final Color series2Color = new Color(0x586C98);
    private final Color series3Color = new Color(0xC98A8A);
    private List<List<String>> statisticCustomerLists = new CustomerBLL().getStatisticCustomer();
    private List<List<String>> statisticStaffLists = new StaffBLL().getStatisticStaff();
    private BarChart barChartCustomer;
    private BarChart barChartStaff;
    private BarChart barChartBank_Account;
    private JFXPanel fxPanelBank_Account;
    private JFXPanel fxPanelTransaction;
    public StatisticGUI() {
        setBackground(new Color(255, 255, 255));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1165, 733));
        menuCustomer = new JPopupMenu();
        searchCustomer = new PanelSearch();
        menuCustomer.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menuCustomer.add(searchCustomer);
        menuCustomer.setFocusable(false);
        searchCustomer.addEventClick(new EventClick() {
            @Override
            public void itemClick(DataSearch data) {
                menuCustomer.setVisible(false);
                txtSearchCustomer.setText("");
                List<Branch> branchList = branchBLL.findAllBranchs("name", data.getText());
                if (!branchList.isEmpty())
                    addBranchCustomer(branchList.get(0));
            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                searchCustomer.remove(com);
                menuCustomer.setPopupSize(230, (searchCustomer.getItemSize() * 35) + 2);
                if (searchCustomer.getItemSize() == 0) {
                    menuCustomer.setVisible(false);
                }
            }
        });

        menuBank_Account = new JPopupMenu();
        searchBank_Account = new PanelSearch();
        menuBank_Account.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menuBank_Account.add(searchBank_Account);
        menuBank_Account.setFocusable(false);
        searchBank_Account.addEventClick(new EventClick() {
            @Override
            public void itemClick(DataSearch data) {
                menuBank_Account.setVisible(false);
                if (!Objects.equals(txtSearchBank_Account.getText(), data.getText())) {
                    txtSearchBank_Account.setText(data.getText());
                    List<Branch> branchList = branchBLL.findAllBranchs("name", data.getText());
                    if (!branchList.isEmpty())
                        addBranchBank_Account(branchList.get(0));
                }
            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                searchBank_Account.remove(com);
                menuBank_Account.setPopupSize(230, (searchBank_Account.getItemSize() * 35) + 2);
                if (searchBank_Account.getItemSize() == 0) {
                    menuBank_Account.setVisible(false);
                }
            }
        });

        menuStaff = new JPopupMenu();
        searchStaff = new PanelSearch();
        menuStaff.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menuStaff.add(searchStaff);
        menuStaff.setFocusable(false);
        searchStaff.addEventClick(new EventClick() {
            @Override
            public void itemClick(DataSearch data) {
                menuStaff.setVisible(false);
                txtSearchStaff.setText("");
                List<Branch> branchList = branchBLL.findAllBranchs("name", data.getText());
                if (!branchList.isEmpty())
                    addBranchStaff(branchList.get(0));
            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                searchStaff.remove(com);
                menuStaff.setPopupSize(230, (searchStaff.getItemSize() * 35) + 2);
                if (searchStaff.getItemSize() == 0) {
                    menuStaff.setVisible(false);
                }
            }
        });
        init();
        setVisible(true);
    }

    private void init() {
        customerPanel = new RoundedPanel();
        bank_accountPanel = new RoundedPanel();
        transactionPanel = new RoundedPanel();
        staffPanel = new RoundedPanel();

        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.setBackground(new Color(255,255,255));
        jTabbedPane.setPreferredSize(new Dimension(1165, 733));
        add(jTabbedPane, BorderLayout.CENTER);

        jTabbedPane.addTab("Khách Hàng", customerPanel);
        jTabbedPane.addTab("Tài Khoản", bank_accountPanel);
        jTabbedPane.addTab("Giao Dịch", transactionPanel);
        jTabbedPane.addTab("Nhân Viên", staffPanel);

        customerPanel.setBackground(new Color(191, 198, 208));
        customerPanel.setLayout(new MigLayout("", "0[]0", "0[]10[]0"));

        bank_accountPanel.setBackground(new Color(191, 198, 208));
        bank_accountPanel.setLayout(new MigLayout("", "0[]0", "0[]10[]0"));

        transactionPanel.setBackground(Color.white);
        transactionPanel.setLayout(new MigLayout());

        staffPanel.setBackground(new Color(191, 198, 208));
        staffPanel.setLayout(new MigLayout("", "0[]0", "0[]10[]0"));

        initCustomerPanel();
        initBank_AccountPanel();
        initTransactionPanel();
        initStaffPanel();
    }

    private void initCustomerPanel() {
        RoundedPanel filterPanel = new RoundedPanel();
        filterPanel.setLayout(new MigLayout("", "15[]10[]10[]10[]10[]", "15[]15"));
        filterPanel.setPreferredSize(new Dimension(1170, 100));
        filterPanel.setBackground(new Color(0xFFFFFF));
        customerPanel.add(filterPanel, "wrap");

        dashboardCustomerPanel = new RoundedPanel();
        dashboardCustomerPanel.setLayout(new MigLayout());
        dashboardCustomerPanel.setPreferredSize(new Dimension(1170, 600));
        dashboardCustomerPanel.setBackground(new Color(255,255,255));
        customerPanel.add(dashboardCustomerPanel);

        txtSearchCustomer = new MyTextField();
        txtSearchCustomer.setOpaque(true);
        txtSearchCustomer.setBackground(new Color(255, 255, 255));
        txtSearchCustomer.putClientProperty("JTextField.placeholderText", "Nhập chi nhánh tìm kiếm");
        txtSearchCustomer.setPreferredSize(new Dimension(230, 40));
        txtSearchCustomer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                txtSearchMouseClicked(evt);
            }
        });
        txtSearchCustomer.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
            public void keyReleased(KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        filterPanel.add(txtSearchCustomer);

        filterBranchCustomerPanel = new RoundedPanel();
        filterBranchCustomerPanel.setLayout(new MigLayout("", "0[]10[]10[]10[]"));
        filterBranchCustomerPanel.setPreferredSize(new Dimension(750, 30));
        filterBranchCustomerPanel.setBackground(new Color(0xFFFFFF));
        filterPanel.add(filterBranchCustomerPanel, "span, wrap");

        barChartCustomer = new BarChart();
        barChartCustomer.setPreferredSize(new Dimension(1170, 600));
        barChartCustomer.setBackground(new Color(191, 198, 208));

        barChartCustomer.addLegend("Tổng số lượng khách hàng", series1Color);
        barChartCustomer.addLegend("Khách hàng nam", series2Color);
        barChartCustomer.addLegend("Khách hàng nữ", series3Color);

        dashboardCustomerPanel.add(barChartCustomer);
        loadBarChartCustomer();
    }

    private void initBank_AccountPanel() {
        RoundedPanel filterPanel = new RoundedPanel();
        filterPanel.setLayout(new MigLayout("", "15[]10[]10[]10[]10[]", "15[]15"));
        filterPanel.setPreferredSize(new Dimension(1170, 100));
        filterPanel.setBackground(new Color(0xFFFFFF));
        bank_accountPanel.add(filterPanel, "wrap");

        dashboardBank_AccountPanel = new RoundedPanel();
        dashboardBank_AccountPanel.setLayout(new MigLayout());
        dashboardBank_AccountPanel.setPreferredSize(new Dimension(1170, 600));
        dashboardBank_AccountPanel.setBackground(new Color(255,255,255));
        bank_accountPanel.add(dashboardBank_AccountPanel);

        txtSearchBank_Account = new MyTextField();
        txtSearchBank_Account.setOpaque(true);
        txtSearchBank_Account.setBackground(new Color(255, 255, 255));
        txtSearchBank_Account.putClientProperty("JTextField.placeholderText", "Nhập chi nhánh tìm kiếm");
        txtSearchBank_Account.setPreferredSize(new Dimension(230, 40));
        txtSearchBank_Account.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                txtSearchMouseClicked1(evt);
            }
        });
        txtSearchBank_Account.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                txtSearchKeyPressed1(evt);
            }
            public void keyReleased(KeyEvent evt) {
                txtSearchKeyReleased1(evt);
            }
        });
        filterPanel.add(txtSearchBank_Account);

        filterBranchBank_AccountPanel = new RoundedPanel();
        filterBranchBank_AccountPanel.setLayout(new MigLayout("", "0[]10[]10[]10[]"));
        filterBranchBank_AccountPanel.setPreferredSize(new Dimension(750, 30));
        filterBranchBank_AccountPanel.setBackground(new Color(0xFFFFFF));
        filterPanel.add(filterBranchBank_AccountPanel, "span, wrap");

        fxPanelBank_Account = new JFXPanel();
        fxPanelBank_Account.setPreferredSize(new Dimension(1170, 300));

        dashboardBank_AccountPanel.add(fxPanelBank_Account, "wrap");


        // -----------------------------------------------------------------//

        barChartBank_Account = new BarChart();
        barChartBank_Account.setPreferredSize(new Dimension(1170, 300));
        barChartBank_Account.setBackground(new Color(191, 198, 208));

        barChartBank_Account.addLegend("Tổng số tài khoản mở", series2Color);

        dashboardBank_AccountPanel.add(barChartBank_Account);
    }

    private void initFX(JFXPanel fxPanelBank_Account, Branch branch) {
        fxPanelBank_Account.removeAll();
        // Tạo một Scene JavaFX
        Scene scene = createScene(branch);
        fxPanelBank_Account.setScene(scene);
        fxPanelBank_Account.repaint();
        fxPanelBank_Account.revalidate();
    }

    private Scene createScene(Branch branch) {
        // Tạo một Line Chart
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Tháng");
        yAxis.setLabel("Phần trăm %");
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        List<List<String>> statisticBank_AccountList = bankAccountBLL.getStatisticBank_Account(branch.getId());

        lineChart.setTitle("Tỉ lệ mở tài khoản");
        String backgroundColor = String.format(
                "-fx-background-color: rgba(%d, %d, %d, %f);",
                191, 198, 208, 1.0); // 1.0 là độ mờ (opacity)
        lineChart.setStyle(backgroundColor);
        xAxis.setAutoRanging(false); // Tắt tự động định vị trục x
        xAxis.setLowerBound(1); // Giá trị bắt đầu của trục x
        xAxis.setUpperBound(Integer.parseInt(statisticBank_AccountList.get(statisticBank_AccountList.size() - 1).get(0).split("/")[0])); // Giá trị kết thúc của trục x
        xAxis.setTickUnit(1); // Đơn vị của các nhãn trục x

        // Thêm dữ liệu vào Line Chart
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(branch.getName());
        int previousMonthOpenings = Integer.parseInt(statisticBank_AccountList.get(0).get(1));
        for (List<String> strings : statisticBank_AccountList) {
            int currentMonthOpenings = Integer.parseInt(strings.get(1));
            double growthPercentage = (((double) (currentMonthOpenings - previousMonthOpenings) / previousMonthOpenings) * 100);
            previousMonthOpenings = Integer.parseInt(strings.get(1));
            series.getData().add(new XYChart.Data<>(Integer.parseInt(strings.get(0).split("/")[0]), growthPercentage));

        }
        lineChart.getData().add(series);

        // Tạo một Scene chứa Line Chart
        return new Scene(lineChart, 600, 400);
    }

    private void initStaffPanel() {
        RoundedPanel filterPanel = new RoundedPanel();
        filterPanel.setLayout(new MigLayout("", "15[]10[]10[]10[]10[]", "15[]15"));
        filterPanel.setPreferredSize(new Dimension(1170, 100));
        filterPanel.setBackground(new Color(0xFFFFFF));
        staffPanel.add(filterPanel, "wrap");

        dashboardStaffPanel = new RoundedPanel();
        dashboardStaffPanel.setLayout(new MigLayout());
        dashboardStaffPanel.setPreferredSize(new Dimension(1170, 600));
        dashboardStaffPanel.setBackground(new Color(255,255,255));
        staffPanel.add(dashboardStaffPanel);

        txtSearchStaff = new MyTextField();
        txtSearchStaff.setOpaque(true);
        txtSearchStaff.setBackground(new Color(255, 255, 255));
        txtSearchStaff.putClientProperty("JTextField.placeholderText", "Nhập chi nhánh tìm kiếm");
        txtSearchStaff.setPreferredSize(new Dimension(230, 40));
        txtSearchStaff.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                txtSearchMouseClicked2(evt);
            }
        });
        txtSearchStaff.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                txtSearchKeyPressed2(evt);
            }
            public void keyReleased(KeyEvent evt) {
                txtSearchKeyReleased2(evt);
            }
        });
        filterPanel.add(txtSearchStaff);

        filterBranchStaffPanel = new RoundedPanel();
        filterBranchStaffPanel.setLayout(new MigLayout("", "0[]10[]10[]10[]"));
        filterBranchStaffPanel.setPreferredSize(new Dimension(750, 30));
        filterBranchStaffPanel.setBackground(new Color(0xFFFFFF));
        filterPanel.add(filterBranchStaffPanel, "span, wrap");

        barChartStaff = new BarChart();
        barChartStaff.setPreferredSize(new Dimension(1170, 600));
        barChartStaff.setBackground(new Color(191, 198, 208));

        barChartStaff.addLegend("Tổng số lượng nhân viên", series1Color);
        barChartStaff.addLegend("Nhân viên nam", series2Color);
        barChartStaff.addLegend("Nhân viên nữ", series3Color);

        dashboardStaffPanel.add(barChartStaff);
        loadBarChartStaff();
    }

    private void initTransactionPanel() {
        List<List<String>> statisticTransactionList = new Transaction_Deposit_WithdrawalBLL().getStatisticTotalTransaction();
        List<List<String>> statisticTransferList = new Transfer_MoneyBLL().getStatisticTotalTransfer();

        fxPanelTransaction = new JFXPanel();
        fxPanelTransaction.setPreferredSize(new Dimension(1170, 300));

        transactionPanel.add(fxPanelTransaction, "wrap");

        CurveLineChart chart = new CurveLineChart();
        JPanel panelShadow = new JPanel();
        panelShadow.setPreferredSize(new Dimension(1170, 400));
        panelShadow.setBackground(new Color(191, 198, 208));
        panelShadow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GroupLayout panelShadowLayout = new GroupLayout(panelShadow);
        panelShadow.setLayout(panelShadowLayout);
        panelShadowLayout.setHorizontalGroup(
                panelShadowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelShadowLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                                .addContainerGap())
        );
        panelShadowLayout.setVerticalGroup(
                panelShadowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelShadowLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                                .addContainerGap())
        );

        transactionPanel.add(panelShadow);

        chart.setTitle("Thống Kê Giao Dịch");
        chart.addLegend("Gửi Tiền", Color.decode("#7b4397"), Color.decode("#dc2430"));
        chart.addLegend("Rút Tiền", Color.decode("#e65c00"), Color.decode("#F9D423"));
        chart.addLegend("Chuyển Tiền", Color.decode("#0099F7"), Color.decode("#F11712"));
        chart.setForeground(new Color(0, 0, 0));
        chart.setFillColor(true);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.MONTH, Calendar.JANUARY);
        String formattedDate = dateFormat.format(cal1.getTime());

        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.MONTH, 1);
        String end = dateFormat.format(cal2.getTime());

        List<ModelData> lists = new ArrayList<>();

        while (!formattedDate.equals(end)) {
            String month = formattedDate;
            double deposit = 0;
            double withdrawal = 0;
            double transfer = 0;
            for (List<String> strings : statisticTransactionList) {
                if (strings.get(0).equals(formattedDate)) {
                    deposit = Double.parseDouble(strings.get(1));
                    withdrawal = Double.parseDouble(strings.get(2));
                    break;
                }
            }

            for (List<String> strings : statisticTransferList) {
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

        //-----------------------------//
        Platform.runLater(() -> {
            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Tháng");
            yAxis.setLabel("Phần trăm %");
            final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

            lineChart.setTitle("Tỉ lệ giao dịch");
            String backgroundColor = String.format(
                    "-fx-background-color: rgba(%d, %d, %d, %f);",
                    191, 198, 208, 1.0); // 1.0 là độ mờ (opacity)
            lineChart.setStyle(backgroundColor);
            xAxis.setAutoRanging(false); // Tắt tự động định vị trục x
            xAxis.setLowerBound(1); // Giá trị bắt đầu của trục x
            xAxis.setUpperBound(Integer.parseInt(statisticTransactionList.get(statisticTransactionList.size() - 1).get(0).split("/")[0])); // Giá trị kết thúc của trục x
            xAxis.setTickUnit(1); // Đơn vị của các nhãn trục x

            // Thêm dữ liệu vào Line Chart
            XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
            series1.setName("Tỉ lệ gửi tiền");
            double previousMonthOpenings = Double.parseDouble(statisticTransactionList.get(0).get(1));
            for (List<String> strings : statisticTransactionList) {
                double currentMonthOpenings = Double.parseDouble(strings.get(1));
                double growthPercentage = (((double) (currentMonthOpenings - previousMonthOpenings) / previousMonthOpenings) * 100);
                previousMonthOpenings = currentMonthOpenings;
                series1.getData().add(new XYChart.Data<>(Integer.parseInt(strings.get(0).split("/")[0]), growthPercentage));
            }

            XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
            series2.setName("Tỉ lệ rút tiền");
            previousMonthOpenings = Double.parseDouble(statisticTransactionList.get(0).get(2));
            for (List<String> strings : statisticTransactionList) {
                double currentMonthOpenings = Double.parseDouble(strings.get(2));
                double growthPercentage = (((double) (currentMonthOpenings - previousMonthOpenings) / previousMonthOpenings) * 100);
                previousMonthOpenings = currentMonthOpenings;
                series2.getData().add(new XYChart.Data<>(Integer.parseInt(strings.get(0).split("/")[0]), growthPercentage));
            }

            XYChart.Series<Number, Number> series3 = new XYChart.Series<>();
            series3.setName("Tỉ lệ chuyển tiền");
            previousMonthOpenings = Double.parseDouble(statisticTransferList.get(0).get(1));
            for (List<String> strings : statisticTransferList) {
                double currentMonthOpenings = Double.parseDouble(strings.get(1));
                double growthPercentage = (((double) (currentMonthOpenings - previousMonthOpenings) / previousMonthOpenings) * 100);
                previousMonthOpenings = currentMonthOpenings;
                series3.getData().add(new XYChart.Data<>(Integer.parseInt(strings.get(0).split("/")[0]), growthPercentage));
            }

            lineChart.getData().addAll(series1, series2, series3);
            fxPanelTransaction.setScene(new Scene(lineChart, 600, 400));
        });

        //-----------------------------//
    }


    private void addBranchCustomer(Branch branch) {
        if (branchListCustomer.size() == 6) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn tối đa 6 chi nhánh!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        branchListCustomer.add(branch);

        RoundedPanel jPanel = new RoundedPanel();
        jPanel.setBackground(new Color(228,231,235));
        filterBranchCustomerPanel.add(jPanel);

        JLabel jLabel = new JLabel(branch.getName());
        jLabel.setFont(new Font("Inter", Font.BOLD, 13));
        jLabel.setIconTextGap(15);
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setVerticalAlignment(JLabel.CENTER);
        jPanel.add(jLabel);

        JLabel icon = new JLabel();
        icon.setIcon(new FlatSVGIcon("icon/cancel-svgrepo-com.svg"));
        icon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        icon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                filterBranchCustomerPanel.remove(jPanel);
                filterBranchCustomerPanel.repaint();
                filterBranchCustomerPanel.revalidate();
                branchListCustomer.removeIf(branch1 -> branch1.getId() == branch.getId());
                loadBarChartCustomer();
            }
        });
        jPanel.add(icon);

        loadBarChartCustomer();
    }

    private void loadBarChartCustomer() {
        barChartCustomer.clear();
        double total = 0, male = 0, female = 0;
        for (List<String> strings : statisticCustomerLists) {
            male += Integer.parseInt(strings.get(1));
            female += Integer.parseInt(strings.get(2));
            total += Integer.parseInt(strings.get(3));
        }
        barChartCustomer.addData(new ModelBarChart("Tất cả chi nhánh", new double[]{total, male, female}));

        for (Branch branch : branchListCustomer) {
            total = 0;
            male = 0;
            female = 0;
            for (List<String> strings : statisticCustomerLists) {
                if (Integer.parseInt(strings.get(0)) == branch.getId()) {
                    male += Integer.parseInt(strings.get(1));
                    female += Integer.parseInt(strings.get(2));
                    total += Integer.parseInt(strings.get(3));
                }
            }
            barChartCustomer.addData(new ModelBarChart(branch.getName(), new double[] {total, male, female}));
        }
        barChartCustomer.start();
    }

    private void addBranchBank_Account(Branch branch) {
        barChartBank_Account.clear();

        List<List<String>> statisticBank_AccountList = bankAccountBLL.getStatisticBank_Account(branch.getId());
        for (List<String> strings : statisticBank_AccountList) {
            barChartBank_Account.addData(new ModelBarChart(strings.get(0), new double[] {Double.parseDouble(strings.get(1))}));
        }
        Platform.runLater(() -> initFX(fxPanelBank_Account, branch));
        barChartBank_Account.start();
    }

    private void addBranchStaff(Branch branch) {
        if (branchListStaff.size() == 6) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn tối đa 6 chi nhánh!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        branchListStaff.add(branch);

        RoundedPanel jPanel = new RoundedPanel();
        jPanel.setBackground(new Color(228,231,235));
        filterBranchStaffPanel.add(jPanel);

        JLabel jLabel = new JLabel(branch.getName());
        jLabel.setFont(new Font("Inter", Font.BOLD, 13));
        jLabel.setIconTextGap(15);
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setVerticalAlignment(JLabel.CENTER);
        jPanel.add(jLabel);

        JLabel icon = new JLabel();
        icon.setIcon(new FlatSVGIcon("icon/cancel-svgrepo-com.svg"));
        icon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        icon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                filterBranchStaffPanel.remove(jPanel);
                filterBranchStaffPanel.repaint();
                filterBranchStaffPanel.revalidate();
                branchListStaff.removeIf(branch1 -> branch1.getId() == branch.getId());
                loadBarChartStaff();
            }
        });
        jPanel.add(icon);

        loadBarChartStaff();
    }

    private void loadBarChartStaff() {
        barChartStaff.clear();
        double total = 0, male = 0, female = 0;
        for (List<String> strings : statisticStaffLists) {
            male += Integer.parseInt(strings.get(1));
            female += Integer.parseInt(strings.get(2));
            total += Integer.parseInt(strings.get(3));
        }
        barChartStaff.addData(new ModelBarChart("Tất cả chi nhánh", new double[]{total, male, female}));

        for (Branch branch : branchListStaff) {
            total = 0;
            male = 0;
            female = 0;
            for (List<String> strings : statisticStaffLists) {
                if (Integer.parseInt(strings.get(0)) == branch.getId()) {
                    male += Integer.parseInt(strings.get(1));
                    female += Integer.parseInt(strings.get(2));
                    total += Integer.parseInt(strings.get(3));
                }
            }
            barChartStaff.addData(new ModelBarChart(branch.getName(), new double[] {total, male, female}));
        }
        barChartStaff.start();
    }

    private void txtSearchMouseClicked(MouseEvent evt) {
        if (searchCustomer.getItemSize() > 0 && !txtSearchCustomer.getText().isEmpty()) {
            menuCustomer.show(txtSearchCustomer, 0, txtSearchCustomer.getHeight());
            searchCustomer.clearSelected();
        }
    }

    private List<DataSearch> search(String text) {
        List<DataSearch> list = new ArrayList<>();
        List<Branch> branches = branchBLL.findAllBranchs("name", text) ;
        List<Integer> branch_idListCustomer = new ArrayList<>();
        for (Branch branch : branchListCustomer)
            branch_idListCustomer.add(branch.getId());
        branches.removeIf(branch -> branch_idListCustomer.contains(branch.getId()));

        for (Branch m : branches) {
            if (list.size() == 7)
                break;
            list.add(new DataSearch(m.getName()));
        }
        return list;
    }

    private void txtSearchKeyReleased(KeyEvent evt) {
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtSearchCustomer.getText().trim().toLowerCase();
            searchCustomer.setData(search(text));
            if (searchCustomer.getItemSize() > 0 && !txtSearchCustomer.getText().isEmpty()) {
                menuCustomer.show(txtSearchCustomer, 0, txtSearchCustomer.getHeight());
                menuCustomer.setPopupSize(230, (searchCustomer.getItemSize() * 35) + 2);
            } else {
                menuCustomer.setVisible(false);
            }
        }
    }

    private void txtSearchKeyPressed(KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            searchCustomer.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            searchCustomer.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = searchCustomer.getSelectedText();
            txtSearchCustomer.setText(text);
        }
        menuCustomer.setVisible(false);

    }

    private void txtSearchMouseClicked1(MouseEvent evt) {
        if (searchBank_Account.getItemSize() > 0 && !txtSearchBank_Account.getText().isEmpty()) {
            menuBank_Account.show(txtSearchBank_Account, 0, txtSearchBank_Account.getHeight());
            searchBank_Account.clearSelected();
        }
    }

    private List<DataSearch> search1(String text) {
        List<DataSearch> list = new ArrayList<>();
        List<Branch> branches = branchBLL.findAllBranchs("name", text) ;
        for (Branch m : branches) {
            if (list.size() == 7)
                break;
            list.add(new DataSearch(m.getName()));
        }
        return list;
    }

    private void txtSearchKeyReleased1(KeyEvent evt) {
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtSearchBank_Account.getText().trim().toLowerCase();
            searchBank_Account.setData(search1(text));
            if (searchBank_Account.getItemSize() > 0 && !txtSearchBank_Account.getText().isEmpty()) {
                menuBank_Account.show(txtSearchBank_Account, 0, txtSearchBank_Account.getHeight());
                menuBank_Account.setPopupSize(230, (searchBank_Account.getItemSize() * 35) + 2);
            } else {
                menuBank_Account.setVisible(false);
            }
        }
    }

    private void txtSearchKeyPressed1(KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            searchBank_Account.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            searchBank_Account.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = searchBank_Account.getSelectedText();
            txtSearchBank_Account.setText(text);
        }
        menuBank_Account.setVisible(false);

    }

    private void txtSearchMouseClicked2(MouseEvent evt) {
        if (searchStaff.getItemSize() > 0 && !txtSearchStaff.getText().isEmpty()) {
            menuStaff.show(txtSearchStaff, 0, txtSearchStaff.getHeight());
            searchStaff.clearSelected();
        }
    }

    private List<DataSearch> search2(String text) {
        List<DataSearch> list = new ArrayList<>();
        List<Branch> branches = branchBLL.findAllBranchs("name", text) ;
        List<Integer> branch_idListStaff = new ArrayList<>();
        for (Branch branch : branchListStaff)
            branch_idListStaff.add(branch.getId());
        branches.removeIf(branch -> branch_idListStaff.contains(branch.getId()));

        for (Branch m : branches) {
            if (list.size() == 7)
                break;
            list.add(new DataSearch(m.getName()));
        }
        return list;
    }

    private void txtSearchKeyReleased2(KeyEvent evt) {
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtSearchStaff.getText().trim().toLowerCase();
            searchStaff.setData(search2(text));
            if (searchStaff.getItemSize() > 0 && !txtSearchStaff.getText().isEmpty()) {
                menuStaff.show(txtSearchStaff, 0, txtSearchStaff.getHeight());
                menuStaff.setPopupSize(230, (searchStaff.getItemSize() * 35) + 2);
            } else {
                menuStaff.setVisible(false);
            }
        }
    }

    private void txtSearchKeyPressed2(KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            searchStaff.keyUp();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            searchStaff.keyDown();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = searchStaff.getSelectedText();
            txtSearchStaff.setText(text);
        }
        menuStaff.setVisible(false);

    }
}
