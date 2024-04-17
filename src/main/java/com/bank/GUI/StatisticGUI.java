package com.bank.GUI;


import com.bank.BLL.BranchBLL;
import com.bank.DTO.Branch;
import com.bank.GUI.components.*;
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class StatisticGUI extends RoundedPanel {
    private RoundedPanel customerPanel;
    private RoundedPanel bank_accountPanel;
    private RoundedPanel transactionPanel;
    private RoundedPanel staffPanel;
    private PanelSearch search;
    private JPopupMenu menu;
    private MyTextField txtSearch;
    private BranchBLL branchBLL = new BranchBLL();
    private List<Integer> branch_idList = new ArrayList<>();
    private RoundedPanel filterBranchPanel;
    private RoundedPanel dashboardPanel;
    public StatisticGUI() {
        setBackground(new Color(255, 255, 255));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1165, 733));
        menu = new JPopupMenu();
        search = new PanelSearch();
        menu.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menu.add(search);
        menu.setFocusable(false);
        search.addEventClick(new EventClick() {
            @Override
            public void itemClick(DataSearch data) {
                menu.setVisible(false);
                txtSearch.setText("");
                List<Branch> branchList = branchBLL.findAllBranchs("name", data.getText());
                if (!branchList.isEmpty())
                    addBranch(branchList.get(0));
            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                search.remove(com);
                menu.setPopupSize(230, (search.getItemSize() * 35) + 2);
                if (search.getItemSize() == 0) {
                    menu.setVisible(false);
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

        bank_accountPanel.setBackground(Color.pink);
        transactionPanel.setBackground(Color.pink);
        staffPanel.setBackground(Color.pink);

        initCustomerPanel();

    }

    private void initCustomerPanel() {
        RoundedPanel filterPanel = new RoundedPanel();
        filterPanel.setLayout(new MigLayout("", "15[]10[]10[]10[]10[]", "15[]15"));
        filterPanel.setPreferredSize(new Dimension(1170, 100));
        filterPanel.setBackground(new Color(0xFFFFFF));
        customerPanel.add(filterPanel, "wrap");

        dashboardPanel = new RoundedPanel();
        dashboardPanel.setLayout(new MigLayout());
        dashboardPanel.setPreferredSize(new Dimension(1170, 600));
        dashboardPanel.setBackground(new Color(0xDDE5FA));
        customerPanel.add(dashboardPanel);

        txtSearch = new MyTextField();
        txtSearch.setOpaque(true);
        txtSearch.setBackground(new Color(255, 255, 255));
        txtSearch.putClientProperty("JTextField.placeholderText", "Nhập chi nhánh tìm kiếm");
        txtSearch.setPreferredSize(new Dimension(230, 40));
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
        filterPanel.add(txtSearch);

        JButton button = new JButton("Tạo thống kê");
        button.setIcon(new FlatSVGIcon("icon/stats-graph-svgrepo-com.svg"));
        button.setBackground(new Color(115,121,210));
        button.setForeground(Color.white);
        button.setPreferredSize(new Dimension(100,30));
        button.setFont(new Font("Inter", Font.BOLD, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statistic();
            }
        });
        filterPanel.add(button);

        filterBranchPanel = new RoundedPanel();
        filterBranchPanel.setLayout(new MigLayout("", "0[]10[]10[]10[]"));
        filterBranchPanel.setPreferredSize(new Dimension(750, 30));
        filterBranchPanel.setBackground(new Color(0xFFFFFF));
        filterPanel.add(filterBranchPanel, "span, wrap");
    }

    private void statistic() {
        dashboardPanel.removeAll();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(100, "Revenue", "January");
        dataset.addValue(150, "Revenue", "February");
        dataset.addValue(200, "Revenue", "March");
        dataset.addValue(120, "Revenue", "April");
        dataset.addValue(180, "Revenue", "May");

        // Thêm dữ liệu cho series mới
        dataset.addValue(90, "Expenses", "January");
        dataset.addValue(120, "Expenses", "February");
        dataset.addValue(150, "Expenses", "March");
        dataset.addValue(100, "Expenses", "April");
        dataset.addValue(130, "Expenses", "May");

        // Tạo biểu đồ cột từ dữ liệu
        JFreeChart barChart = ChartFactory.createBarChart(
                "Monthly Revenue",
                "Month",
                "Revenue (in $)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        CategoryPlot plot = barChart.getCategoryPlot();

        plot.setBackgroundPaint(Color.white);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0xD0DDFA));
        renderer.setSeriesPaint(0, new Color(0xC98A8A));

        JPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(1170, 600));
        dashboardPanel.add(chartPanel);

        Font labelFont = new Font("Inter", Font.BOLD, 13);
        plot.getDomainAxis().setLabelFont(labelFont);
        plot.getRangeAxis().setLabelFont(labelFont);
        plot.getDomainAxis().setTickLabelFont(labelFont);
        plot.getRangeAxis().setTickLabelFont(labelFont);
        plot.getDomainAxis().setLabelPaint(Color.BLACK);
        plot.getRangeAxis().setLabelPaint(Color.BLACK);
        plot.getDomainAxis().setTickLabelPaint(Color.BLACK);
        plot.getRangeAxis().setTickLabelPaint(Color.BLACK);

        dashboardPanel.repaint();
        dashboardPanel.revalidate();
    }

    private void addBranch(Branch branch) {
        if (branch_idList.size() == 4) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn tối đa 4 chi nhánh!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        branch_idList.add(branch.getId());

        RoundedPanel jPanel = new RoundedPanel();
        jPanel.setBackground(new Color(228,231,235));
        filterBranchPanel.add(jPanel);

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
                filterBranchPanel.remove(jPanel);
                branch_idList.remove((Object) branch.getId());
            }
        });
        jPanel.add(icon);


    }
    private void txtSearchMouseClicked(MouseEvent evt) {
        if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
            menu.show(txtSearch, 0, txtSearch.getHeight());
            search.clearSelected();
        }
    }

    private List<DataSearch> search(String text) {
        List<DataSearch> list = new ArrayList<>();
        List<Branch> branches = branchBLL.findAllBranchs("name", text) ;
        branches.removeIf(branch -> branch_idList.contains(branch.getId()));

        for (Branch m : branches) {
            if (list.size() == 7)
                break;
            list.add(new DataSearch(m.getName()));
        }
        return list;
    }

    private void txtSearchKeyReleased(KeyEvent evt) {
        if (evt.getKeyCode() != KeyEvent.VK_UP && evt.getKeyCode() != KeyEvent.VK_DOWN && evt.getKeyCode() != KeyEvent.VK_ENTER) {
            String text = txtSearch.getText().trim().toLowerCase();
            search.setData(search(text));
            if (search.getItemSize() > 0 && !txtSearch.getText().isEmpty()) {
                menu.show(txtSearch, 0, txtSearch.getHeight());
                menu.setPopupSize(230, (search.getItemSize() * 35) + 2);
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
