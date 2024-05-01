package com.bank.GUI;

import com.bank.BLL.Transaction_Deposit_WithdrawalBLL;
import com.bank.DTO.Function;
import com.bank.DTO.Transaction_Deposit_Withdrawal;
import com.bank.GUI.DialogGUI.FormDetailGUI.DetailTransaction_Deposit_WithdrawalGUI;
import com.bank.GUI.components.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
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
    private DatePicker datePicker;
    private JFormattedTextField editor;
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
        datePicker = new DatePicker();
        editor = new JFormattedTextField();

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

        datePicker.setDateSelectionMode(raven.datetime.component.date.DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED);
        datePicker.setEditor(editor);
        datePicker.setUsePanelOption(true);
        datePicker.setCloseAfterSelected(true);
        datePicker.addDateSelectionListener(new DateSelectionListener() {
            @Override
            public void dateSelected(DateEvent dateEvent) {
                searchTransaction_Deposit_Withdrawals();
            }
        });
        editor.setPreferredSize(new Dimension(280, 40));
        editor.setFont(new Font("Inter", Font.BOLD, 15));
        FilterDatePanel.add(editor);

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

            JLabel panel = new JLabel("Nhập Excel");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/import.svg"));
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
        datePicker.clearSelectedDate();
        jComboBoxSearch.setSelectedIndex(0);
        loadDataTable(Transaction_Deposit_WithdrawalBLL.getData(Transaction_Deposit_WithdrawalBLL.getTransaction_deposit_withdrawalListAll()));
    }

    private void searchTransaction_Deposit_Withdrawals() {
        List<Transaction_Deposit_Withdrawal> Transaction_Deposit_WithdrawalList = new ArrayList<>(Transaction_Deposit_WithdrawalBLL.getTransaction_deposit_withdrawalListAll());

        if (jComboBoxSearch.getSelectedIndex() == 2) {
            Transaction_Deposit_WithdrawalList.removeIf(Transaction_Deposit_Withdrawal -> !Transaction_Deposit_Withdrawal.getTransaction_type());
        }
        else if (jComboBoxSearch.getSelectedIndex() == 3) {
            Transaction_Deposit_WithdrawalList.removeIf(Transaction_Deposit_Withdrawal::getTransaction_type);
        }

        if (jTextFieldSearch.getText().isEmpty() && datePicker.getDateSQL_Between() == null) {
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
            if (datePicker.getDateSQL_Between() != null) {
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                Date startDate = datePicker.getDateSQL_Between()[0];
                Date endDate = datePicker.getDateSQL_Between()[1];
                if (startDate.after(endDate)) {
                    JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Transaction_Deposit_WithdrawalList.removeIf(Transaction_Deposit_Withdrawal -> (Transaction_Deposit_Withdrawal.getTransaction_date().toLocalDate().isBefore(LocalDate.parse(startDate.toString(), myFormatObj)) || Transaction_Deposit_Withdrawal.getTransaction_date().toLocalDate().isAfter(LocalDate.parse(endDate.toString(), myFormatObj))));
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

        data = new Object[objects.length][objects[0].length -1];

        for (int i = 0; i < objects.length; i++) {
            System.arraycopy(objects[i], 0, data[i], 0, objects[i].length -1);

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

        Transaction_Deposit_Withdrawal selectedTransaction_Deposit_Withdrawal = Transaction_Deposit_WithdrawalBLL.findAllTransaction_Deposit_Withdrawals("id",  data[indexRow][0].toString()).get(0);
        if (detail && indexColumn == indexColumnDetail) {
            new DetailTransaction_Deposit_WithdrawalGUI(selectedTransaction_Deposit_Withdrawal); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá
        }

    }
}



