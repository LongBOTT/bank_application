package com.bank.GUI;


import com.bank.BLL.*;
import com.bank.DTO.Branch;
import com.bank.GUI.components.*;
import com.bank.GUI.components.line_chart.ModelData;
import com.bank.GUI.components.line_chart.chart.CurveLineChart;
import com.bank.GUI.components.line_chart.chart.ModelChart;
import com.bank.GUI.components.swing.DataSearch;
import com.bank.GUI.components.swing.EventClick;
import com.bank.GUI.components.swing.MyTextField;
import com.bank.GUI.components.swing.PanelSearch;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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
    private List<Integer> branch_idListCustomer = new ArrayList<>();
    private List<Integer> branch_idListStaff = new ArrayList<>();
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
    private DefaultCategoryDataset datasetCustomer;
    private DefaultCategoryDataset datasetStaff;
    private DefaultCategoryDataset datasetBank_Account;
    private  DefaultCategoryDataset datasetLineBank_Account;
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

        int total = 0, male = 0, female = 0;
        for (List<String> strings : statisticCustomerLists) {
            male += Integer.parseInt(strings.get(1));
            female += Integer.parseInt(strings.get(2));
            total += Integer.parseInt(strings.get(3));
        }

        datasetCustomer = new DefaultCategoryDataset();
        datasetCustomer.addValue(total, "Tổng", "Tất Cả");
        datasetCustomer.addValue(male, "Nam", "Tất Cả");
        datasetCustomer.addValue(female, "Nữ", "Tất Cả");


        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống Kê Khách Hàng",
                "",
                "Số Lượng",
                datasetCustomer,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        CategoryPlot plot = barChart.getCategoryPlot();

        plot.setBackgroundPaint(new Color(191, 198, 208));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, series1Color);
        renderer.setSeriesPaint(1, series2Color);
        renderer.setSeriesPaint(2, series3Color);

        Font labelFont = new Font("Inter", Font.BOLD, 13);
        plot.getDomainAxis().setLabelFont(labelFont);
        plot.getRangeAxis().setLabelFont(labelFont);
        plot.getDomainAxis().setTickLabelFont(labelFont);
        plot.getRangeAxis().setTickLabelFont(labelFont);
        plot.getDomainAxis().setLabelPaint(Color.BLACK);
        plot.getRangeAxis().setLabelPaint(Color.BLACK);
        plot.getDomainAxis().setTickLabelPaint(Color.BLACK);
        plot.getRangeAxis().setTickLabelPaint(Color.BLACK);

        JPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(1170, 600));
        dashboardCustomerPanel.add(chartPanel);
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

        datasetLineBank_Account = new DefaultCategoryDataset();
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Tỉ Lệ Mở Tài Khoản",
                "Tháng",
                "Phần Trăm %",
                datasetLineBank_Account,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        CategoryPlot plot1 = lineChart.getCategoryPlot();
        plot1.setBackgroundPaint(new Color(191, 198, 208));
        Font labelFont1 = new Font("Inter", Font.BOLD, 13);
        plot1.getDomainAxis().setLabelFont(labelFont1);
        plot1.getRangeAxis().setLabelFont(labelFont1);
        plot1.getDomainAxis().setTickLabelFont(labelFont1);
        plot1.getRangeAxis().setTickLabelFont(labelFont1);
        plot1.getDomainAxis().setLabelPaint(Color.BLACK);
        plot1.getRangeAxis().setLabelPaint(Color.BLACK);
        plot1.getDomainAxis().setTickLabelPaint(Color.BLACK);
        plot1.getRangeAxis().setTickLabelPaint(Color.BLACK);
        lineChart.setBackgroundPaint(Color.white);
        lineChart.getTitle().setPaint(Color.black);
        lineChart.getTitle().setFont(new Font("Arial", Font.BOLD, 18));

        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        lineChartPanel.setPreferredSize(new Dimension(1170, 300));

        dashboardBank_AccountPanel.add(lineChartPanel, "wrap");


        // -----------------------------------------------------------------//

        datasetBank_Account = new DefaultCategoryDataset();
        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống Kê Tài Khoản",
                "",
                "Số Lượng",
                datasetBank_Account,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        CategoryPlot plot2 = barChart.getCategoryPlot();
        plot2.setBackgroundPaint(new Color(191, 198, 208));
        BarRenderer renderer = (BarRenderer) plot2.getRenderer();
        renderer.setSeriesPaint(0, series2Color);
        Font labelFont2 = new Font("Inter", Font.BOLD, 13);
        plot2.getDomainAxis().setLabelFont(labelFont2);
        plot2.getRangeAxis().setLabelFont(labelFont2);
        plot2.getDomainAxis().setTickLabelFont(labelFont2);
        plot2.getRangeAxis().setTickLabelFont(labelFont2);
        plot2.getDomainAxis().setLabelPaint(Color.BLACK);
        plot2.getRangeAxis().setLabelPaint(Color.BLACK);
        plot2.getDomainAxis().setTickLabelPaint(Color.BLACK);
        plot2.getRangeAxis().setTickLabelPaint(Color.BLACK);

        JPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(1170, 300));

        dashboardBank_AccountPanel.add(chartPanel);
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

        int total = 0, male = 0, female = 0;
        for (List<String> strings : statisticStaffLists) {
            male += Integer.parseInt(strings.get(1));
            female += Integer.parseInt(strings.get(2));
            total += Integer.parseInt(strings.get(3));
        }

        datasetStaff = new DefaultCategoryDataset();
        datasetStaff.addValue(total, "Tổng", "Tất Cả");
        datasetStaff.addValue(male, "Nam", "Tất Cả");
        datasetStaff.addValue(female, "Nữ", "Tất Cả");


        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống Kê Nhân Viên",
                "",
                "Số Lượng",
                datasetStaff,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        CategoryPlot plot = barChart.getCategoryPlot();

        plot.setBackgroundPaint(new Color(191, 198, 208));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, series1Color);
        renderer.setSeriesPaint(1, series2Color);
        renderer.setSeriesPaint(2, series3Color);

        Font labelFont = new Font("Inter", Font.BOLD, 13);
        plot.getDomainAxis().setLabelFont(labelFont);
        plot.getRangeAxis().setLabelFont(labelFont);
        plot.getDomainAxis().setTickLabelFont(labelFont);
        plot.getRangeAxis().setTickLabelFont(labelFont);
        plot.getDomainAxis().setLabelPaint(Color.BLACK);
        plot.getRangeAxis().setLabelPaint(Color.BLACK);
        plot.getDomainAxis().setTickLabelPaint(Color.BLACK);
        plot.getRangeAxis().setTickLabelPaint(Color.BLACK);

        JPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(1170, 600));
        dashboardStaffPanel.add(chartPanel);
    }

    private void initTransactionPanel() {
        List<List<String>> statisticTransactionList = new Transaction_Deposit_WithdrawalBLL().getStatisticTotalTransaction();
        List<List<String>> statisticTransferList = new Transfer_MoneyBLL().getStatisticTotalTransfer();

        DefaultCategoryDataset datasetLineTransaction = new DefaultCategoryDataset();
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Tỉ Lệ Giao Dịch",
                "Tháng",
                "Phần Trăm %",
                datasetLineTransaction,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        CategoryPlot plot1 = lineChart.getCategoryPlot();
        plot1.setBackgroundPaint(new Color(191, 198, 208));
        Font labelFont1 = new Font("Inter", Font.BOLD, 13);
        plot1.getDomainAxis().setLabelFont(labelFont1);
        plot1.getRangeAxis().setLabelFont(labelFont1);
        plot1.getDomainAxis().setTickLabelFont(labelFont1);
        plot1.getRangeAxis().setTickLabelFont(labelFont1);
        plot1.getDomainAxis().setLabelPaint(Color.BLACK);
        plot1.getRangeAxis().setLabelPaint(Color.BLACK);
        plot1.getDomainAxis().setTickLabelPaint(Color.BLACK);
        plot1.getRangeAxis().setTickLabelPaint(Color.BLACK);
        lineChart.setBackgroundPaint(Color.white);
        lineChart.getTitle().setPaint(Color.black);
        lineChart.getTitle().setFont(new Font("Arial", Font.BOLD, 18));

        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        lineChartPanel.setPreferredSize(new Dimension(1170, 300));

        transactionPanel.add(lineChartPanel, "wrap");


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
        double previousGrowthPercentage = 0;
        double previousMonthOpenings = Double.parseDouble(statisticTransactionList.get(0).get(1));
        for (List<String> strings : statisticTransactionList) {
            double currentMonthOpenings = Double.parseDouble(strings.get(1));
            double growthPercentage = (((double) (currentMonthOpenings - previousMonthOpenings) / previousMonthOpenings) * 100) + previousGrowthPercentage;
            previousMonthOpenings = currentMonthOpenings;
            previousGrowthPercentage = growthPercentage;
            datasetLineTransaction.addValue(growthPercentage, "Gửi Tiền %", strings.get(0));
        }

        previousGrowthPercentage = 0;
        previousMonthOpenings = Double.parseDouble(statisticTransactionList.get(0).get(2));
        for (List<String> strings : statisticTransactionList) {
            double currentMonthOpenings = Double.parseDouble(strings.get(2));
            double growthPercentage = (((double) (currentMonthOpenings - previousMonthOpenings) / previousMonthOpenings) * 100) + previousGrowthPercentage;
            previousMonthOpenings = currentMonthOpenings;
            previousGrowthPercentage = growthPercentage;
            datasetLineTransaction.addValue(growthPercentage, "Rút Tiền %", strings.get(0));
        }

        previousGrowthPercentage = 0;
        previousMonthOpenings = Double.parseDouble(statisticTransferList.get(0).get(1));
        for (List<String> strings : statisticTransferList) {
            double currentMonthOpenings = Double.parseDouble(strings.get(1));
            double growthPercentage = (((double) (currentMonthOpenings - previousMonthOpenings) / previousMonthOpenings) * 100) + previousGrowthPercentage;
            previousMonthOpenings = currentMonthOpenings;
            previousGrowthPercentage = growthPercentage;
            datasetLineTransaction.addValue(growthPercentage, "Chuyển Tiền %", strings.get(0));
        }

        //-----------------------------//
    }


    private void addBranchCustomer(Branch branch) {
        if (branch_idListCustomer.size() == 4) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn tối đa 4 chi nhánh!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        branch_idListCustomer.add(branch.getId());

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
                branch_idListCustomer.remove((Object) branch.getId());
                datasetCustomer.removeColumn(branch.getName());
            }
        });
        jPanel.add(icon);

        int total = 0, male = 0, female = 0;
        for (List<String> strings : statisticCustomerLists) {
            if (Integer.parseInt(strings.get(0)) == branch.getId()) {
                male += Integer.parseInt(strings.get(1));
                female += Integer.parseInt(strings.get(2));
                total += Integer.parseInt(strings.get(3));
            }
        }

        datasetCustomer.addValue(total, "Tổng", branch.getName());
        datasetCustomer.addValue(male, "Nam", branch.getName());
        datasetCustomer.addValue(female, "Nữ", branch.getName());
    }

    private void addBranchBank_Account(Branch branch) {
        datasetBank_Account.clear();
        datasetLineBank_Account.clear();

        List<List<String>> statisticBank_AccountList = bankAccountBLL.getStatisticBank_Account(branch.getId());
        double previousGrowthPercentage = 0;
        int previousMonthOpenings = Integer.parseInt(statisticBank_AccountList.get(0).get(1));
        for (List<String> strings : statisticBank_AccountList) {
            int currentMonthOpenings = Integer.parseInt(strings.get(1));
            double growthPercentage = (((double) (currentMonthOpenings - previousMonthOpenings) / previousMonthOpenings) * 100) + previousGrowthPercentage;
            previousMonthOpenings = Integer.parseInt(strings.get(1));
            previousGrowthPercentage = growthPercentage;
            datasetLineBank_Account.addValue(growthPercentage, "Phần Trăm %", strings.get(0));

            datasetBank_Account.addValue(Integer.parseInt(strings.get(1)), "Tổng", strings.get(0));
        }
    }

    private void addBranchStaff(Branch branch) {
        if (branch_idListStaff.size() == 4) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn tối đa 4 chi nhánh!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        branch_idListStaff.add(branch.getId());

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
                branch_idListStaff.remove((Object) branch.getId());
                datasetStaff.removeColumn(branch.getName());
            }
        });
        jPanel.add(icon);

        int total = 0, male = 0, female = 0;
        for (List<String> strings : statisticStaffLists) {
            if (Integer.parseInt(strings.get(0)) == branch.getId()) {
                male += Integer.parseInt(strings.get(1));
                female += Integer.parseInt(strings.get(2));
                total += Integer.parseInt(strings.get(3));
            }
        }

        datasetStaff.addValue(total, "Tổng", branch.getName());
        datasetStaff.addValue(male, "Nam", branch.getName());
        datasetStaff.addValue(female, "Nữ", branch.getName());
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
