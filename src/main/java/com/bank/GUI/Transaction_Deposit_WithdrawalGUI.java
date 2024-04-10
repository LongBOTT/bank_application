package com.bank.GUI;

import com.bank.BLL.Transaction_Deposit_WithdrawalBLL;
import com.bank.DTO.Function;
import com.bank.DTO.Transaction_Deposit_Withdrawal;
import com.bank.GUI.components.DataTable;
import com.bank.GUI.components.Layout2;
import com.bank.GUI.components.RoundedPanel;
import com.bank.GUI.components.RoundedScrollPane;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class Transaction_Deposit_WithdrawalGUI extends Layout2 {
    private RoundedPanel containerSearch;
    private JLabel iconSearch;
    private JTextField jTextFieldSearch;
    private JButton jButtonSearch;
    private JComboBox<String> jComboBoxSearch;
    private JDateChooser[] jDateChooser;
    private List<Function> functions;
    private Transaction_Deposit_WithdrawalBLL Transaction_Deposit_WithdrawalBLL = new Transaction_Deposit_WithdrawalBLL();
    private DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private int indexColumnDetail = -1;
    private boolean detail = false;
    private String[] columnNames;
    private Object[][] data = new Object[0][0];
    private int branch_id  = 0;

    public Transaction_Deposit_WithdrawalGUI(List<Function> functions) {
        super();
        this.functions = functions;
        if (functions.stream().anyMatch(f -> f.getName().equals("view")))
            detail = true;

        init(functions);
    }

    private void init(List<Function> functions) {
        containerSearch = new RoundedPanel();
        iconSearch = new JLabel();
        jTextFieldSearch = new JTextField();
        jButtonSearch = new JButton("Tìm kiếm");
        jComboBoxSearch = new JComboBox<>(new String[]{"Mã Giao Dịch", "Số Tài Khoản", "Giao Dịch Gửi Tiền", "Giao Dịch Rút Tiền"});
        jDateChooser = new JDateChooser[2];

        columnNames = new String[]{"Mã Giao Dịch", "Số Tài Khoản", "Loại Giao Dịch", "Số Tiền", "Thời Gian Giao Dịch"};
        if (detail) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnDetail = columnNames.length - 1;
            columnNames[indexColumnDetail] = "Xem";
        }

        dataTable = new DataTable(new Object[0][0], columnNames,
                e -> selectFunction(),
                detail, false, false, 5); // table hiển thị các thuộc tính "Mã NCC", "Tên NCC", "SĐT", "Email" nên điền 4
        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1165, 680));
        bottom.add(scrollPane, BorderLayout.CENTER);

        for (int i = 0; i < 2; i++) {
            jDateChooser[i] = new JDateChooser();
            jDateChooser[i].setDateFormatString("dd/MM/yyyy");
            jDateChooser[i].setBackground(new Color(191, 198, 208));
            jDateChooser[i].setPreferredSize(new Dimension(198, 30));
            jDateChooser[i].setMinSelectableDate(java.sql.Date.valueOf("1000-1-1"));

            if (i == 0) {
                JLabel jLabel = new JLabel("Từ Ngày");
                jLabel.setFont(new Font("Lexend", Font.BOLD, 14));
                FilterDatePanel.add(jLabel);
            } else {
                JLabel jLabel = new JLabel("Đến Ngày");
                jLabel.setFont(new Font("Lexend", Font.BOLD, 14));
                FilterDatePanel.add(jLabel);
            }
            FilterDatePanel.add(jDateChooser[i]);
        }

        containerSearch.setLayout(new MigLayout("", "10[]10[]10", ""));
        containerSearch.setBackground(new Color(255, 255, 255));
        containerSearch.setPreferredSize(new Dimension(280, 40));
        SearchPanel.add(containerSearch);

        iconSearch.setIcon(new FlatSVGIcon("icon/search.svg"));
        containerSearch.add(iconSearch);

        jTextFieldSearch.setFont(new Font("Public Sans", Font.PLAIN, 14));
        jTextFieldSearch.setBackground(new Color(255, 255, 255));
        jTextFieldSearch.setBorder(BorderFactory.createEmptyBorder());
        jTextFieldSearch.putClientProperty("JTextField.placeholderText", "Nhập nội dung tìm kiếm");
        jTextFieldSearch.setPreferredSize(new Dimension(300, 30));
        containerSearch.add(jTextFieldSearch);
        jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchTransaction_Deposit_Withdrawals();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchTransaction_Deposit_Withdrawals();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchTransaction_Deposit_Withdrawals();
            }
        });

        jButtonSearch.setBackground(new Color(1, 120, 220));
        jButtonSearch.setForeground(Color.white);
        jButtonSearch.setPreferredSize(new Dimension(100, 30));
        jButtonSearch.addActionListener(e -> searchTransaction_Deposit_Withdrawals());
        SearchPanel.add(jButtonSearch);

        jComboBoxSearch.setBackground(new Color(1, 120, 220));
        jComboBoxSearch.setForeground(Color.white);
        jComboBoxSearch.setPreferredSize(new Dimension(150, 30));
        jComboBoxSearch.addActionListener(e -> searchTransaction_Deposit_Withdrawals());
        SearchPanel.add(jComboBoxSearch);

        loadDataTable(Transaction_Deposit_WithdrawalBLL.getData(Transaction_Deposit_WithdrawalBLL.getTransaction_deposit_withdrawalListAll()));

        RoundedPanel refreshPanel = new RoundedPanel();
        refreshPanel.setLayout(new GridBagLayout());
        refreshPanel.setPreferredSize(new Dimension(130, 40));
        refreshPanel.setBackground(new Color(1, 120, 220));
        refreshPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                refresh();
            }
        });
        FunctionPanel.add(refreshPanel);

        JLabel refreshLabel = new JLabel("Làm mới");
        refreshLabel.setFont(new Font("Public Sans", Font.PLAIN, 13));
        refreshLabel.setForeground(Color.white);
        refreshLabel.setIcon(new FlatSVGIcon("icon/refresh.svg"));
        refreshPanel.add(refreshLabel);

        if (functions.stream().anyMatch(f -> f.getName().equals("add"))) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setLayout(new GridBagLayout());
            roundedPanel.setPreferredSize(new Dimension(130, 40));
            roundedPanel.setBackground(new Color(1, 120, 220));
            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            roundedPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                }
            });

            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Thêm mới");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/add.svg"));
            roundedPanel.add(panel);
        }
        if (functions.stream().anyMatch(f -> f.getName().equals("excel"))) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setLayout(new GridBagLayout());
            roundedPanel.setPreferredSize(new Dimension(130, 40));
            roundedPanel.setBackground(new Color(1, 120, 220));
            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Xuất Excel");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/export.svg"));
            roundedPanel.add(panel);
        }
        if (functions.stream().anyMatch(f -> f.getName().equals("pdf"))) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setLayout(new GridBagLayout());
            roundedPanel.setPreferredSize(new Dimension(130, 40));
            roundedPanel.setBackground(new Color(1, 120, 220));
            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Xuất PDF");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/export.svg"));
            roundedPanel.add(panel);
        }
    }

    public void refresh() {
        Transaction_Deposit_WithdrawalBLL = new Transaction_Deposit_WithdrawalBLL();
        jTextFieldSearch.setText("");
        jDateChooser[0].getDateEditor().setDate(null);
        jDateChooser[1].getDateEditor().setDate(null);
        jComboBoxSearch.setSelectedIndex(0);
        loadDataTable(Transaction_Deposit_WithdrawalBLL.getData(Transaction_Deposit_WithdrawalBLL.getTransaction_deposit_withdrawalListAll()));
    }

    private void searchTransaction_Deposit_Withdrawals() {
        List<Transaction_Deposit_Withdrawal> Transaction_Deposit_WithdrawalList = new ArrayList<>(Transaction_Deposit_WithdrawalBLL.getTransaction_deposit_withdrawalListAll());
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        if (jComboBoxSearch.getSelectedIndex() == 2) {
            Transaction_Deposit_WithdrawalList.removeIf(Transaction_Deposit_Withdrawal -> !Transaction_Deposit_Withdrawal.getTransaction_type());
        }
        else if (jComboBoxSearch.getSelectedIndex() == 3) {
            Transaction_Deposit_WithdrawalList.removeIf(Transaction_Deposit_Withdrawal::getTransaction_type);
        }

        if (jTextFieldSearch.getText().isEmpty() && jDateChooser[0].getDateEditor().getDate() == null && jDateChooser[1].getDateEditor().getDate() == null) {
            loadDataTable(Transaction_Deposit_WithdrawalBLL.getData(Transaction_Deposit_WithdrawalList));
        } else {
            if (!jTextFieldSearch.getText().isEmpty()) {
                if (jComboBoxSearch.getSelectedIndex() == 0) {
                    Transaction_Deposit_WithdrawalList.removeIf(Transaction_Deposit_Withdrawal -> !String.valueOf(Transaction_Deposit_Withdrawal.getId()).contains(jTextFieldSearch.getText()));
                }
                else if (jComboBoxSearch.getSelectedIndex() == 1) {
                    Transaction_Deposit_WithdrawalList.removeIf(Transaction_Deposit_Withdrawal -> !Transaction_Deposit_Withdrawal.getBank_number_account().contains(jTextFieldSearch.getText()));
                }else {
                    Transaction_Deposit_WithdrawalList.removeIf(Transaction_Deposit_Withdrawal -> !String.valueOf(Transaction_Deposit_Withdrawal.getId()).contains(jTextFieldSearch.getText()));
                }
            }
            if (jDateChooser[0].getDateEditor().getDate() != null || jDateChooser[1].getDateEditor().getDate() != null) {
                if (jDateChooser[0].getDateEditor().getDate() != null && jDateChooser[1].getDateEditor().getDate() != null) {
                    Date startDate = jDateChooser[0].getDate();
                    Date endDate = jDateChooser[1].getDate();
                    if (startDate.after(endDate)) {
                        JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc.",
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Transaction_Deposit_WithdrawalList.removeIf(Transaction_Deposit_Withdrawal -> (Transaction_Deposit_Withdrawal.getTransaction_date().isBefore(LocalDateTime.parse(startDate.toString(), myFormatObj)) || Transaction_Deposit_Withdrawal.getTransaction_date().isAfter(LocalDateTime.parse(endDate.toString(), myFormatObj))));
                } else {
                    if (jDateChooser[0].getDateEditor().getDate() == null) {
                        Date endDate = jDateChooser[1].getDate();
                        Transaction_Deposit_WithdrawalList.removeIf(Transaction_Deposit_Withdrawal -> (Transaction_Deposit_Withdrawal.getTransaction_date().isBefore(LocalDateTime.MIN)) || Transaction_Deposit_Withdrawal.getTransaction_date().isAfter(LocalDateTime.parse(endDate.toString(), myFormatObj)));
                    } else {
                        Date startDate = jDateChooser[0].getDate();
                        Transaction_Deposit_WithdrawalList.removeIf(Transaction_Deposit_Withdrawal -> (Transaction_Deposit_Withdrawal.getTransaction_date().isBefore(LocalDateTime.parse(startDate.toString(), myFormatObj))));
                    }
                }
            }
            loadDataTable(Transaction_Deposit_WithdrawalBLL.getData(Transaction_Deposit_WithdrawalList));
        }
    }

    public void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        if (objects.length == 0) {
            return;
        }

        data = new Object[objects.length][objects[0].length];

        for (int i = 0; i < objects.length; i++) {
            System.arraycopy(objects[i], 0, data[i], 0, objects[i].length);

            if (detail) {
                JLabel iconDetail = new JLabel(new FlatSVGIcon("icon/detail.svg"));
                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = iconDetail;
            }
        }

        for (Object[] object : data) {
            model.addRow(object);
        }
    }

    private void selectFunction() {
        int indexRow = dataTable.getSelectedRow();
        int indexColumn = dataTable.getSelectedColumn();

//        Transaction_Deposit_Withdrawal selectedTransaction_Deposit_Withdrawal = Transaction_Deposit_WithdrawalBLL.findAllTransaction_Deposit_Withdrawals("number",  data[indexRow][0].toString()).get(0);
//        if (detail && indexColumn == indexColumnDetail) {
//            new DetailTransaction_Deposit_WithdrawalGUI(selectedTransaction_Deposit_Withdrawal); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá
//        }

    }
}



