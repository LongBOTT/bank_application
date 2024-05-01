package com.bank.GUI;

import com.bank.BLL.Bank_AccountBLL;
import com.bank.BLL.BranchBLL;
import com.bank.DTO.Bank_Account;
import com.bank.DTO.Branch;
import com.bank.DTO.Customer;
import com.bank.DTO.Function;

import com.bank.GUI.DialogGUI.FormDetailGUI.DetailBank_AccountGUI;
import com.bank.GUI.components.*;
import com.bank.GUI.components.swing.DataSearch;
import com.bank.GUI.components.swing.EventClick;
import com.bank.GUI.components.swing.MyTextField;
import com.bank.GUI.components.swing.PanelSearch;
import com.bank.main.Bank_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class Bank_AccountGUI extends Layout2 {
    private RoundedPanel containerSearch;
    private JLabel iconSearch;
    private MyTextField txtSearch;
    private PanelSearch search;
    private JPopupMenu menu;
    private JTextField jTextFieldSearch;
    private JButton jButtonSearch;
    private JComboBox<String> jComboBoxSearch;
    private DatePicker datePicker;
    private JFormattedTextField editor;
    private List<Function> functions;
    private Bank_AccountBLL bank_accountBLL = new Bank_AccountBLL();
    private BranchBLL branchBLL = new BranchBLL();
    private DataTable dataTable;
    private RoundedScrollPane scrollPane;
    private int indexColumnDetail = -1;
    private boolean detail = false;
    private String[] columnNames;
    private Object[][] data = new Object[0][0];
    private int branch_id  = 0;

    public Bank_AccountGUI(List<Function> functions) {
        super();
        this.functions = functions;
        if (functions.stream().anyMatch(f -> f.getName().equals("view")))
            detail = true;

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
                List<Branch> branchList = branchBLL.findAllBranchs("name", data.getText());
                if (!branchList.isEmpty())
                    branch_id = branchList.get(0).getId();
                searchBank_Accounts();
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
        init(functions);
    }

    private void init(List<Function> functions) {
        containerSearch = new RoundedPanel();
        iconSearch = new JLabel();
        jTextFieldSearch = new JTextField();
        jButtonSearch = new JButton("Tìm kiếm");
        jComboBoxSearch = new JComboBox<>(new String[]{"Số thẻ", "CCCD", "Trạng Thái Mở", "Trạng Thái Đóng"});
        datePicker = new DatePicker();
        editor = new JFormattedTextField();
        txtSearch = new MyTextField();

        columnNames = new String[]{"Số Thẻ", "CCCD", "Số Dư", "Chi Nhánh", "Ngày Mở", "Trạng Thái"};
        if (detail) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnDetail = columnNames.length - 1;
            columnNames[indexColumnDetail] = "Xem";
        }

        dataTable = new DataTable(new Object[0][0], columnNames,
                e -> selectFunction(), e -> changedQuantity(),
                detail, false, false, 6, 5); // table hiển thị các thuộc tính "Mã NCC", "Tên NCC", "SĐT", "Email" nên điền 4
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
                searchBank_Accounts();
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
                searchBank_Accounts();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchBank_Accounts();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchBank_Accounts();
            }
        });

        jButtonSearch.setBackground(new Color(1, 120, 220));
        jButtonSearch.setForeground(Color.white);
        jButtonSearch.setPreferredSize(new Dimension(100, 30));
        jButtonSearch.addActionListener(e -> searchBank_Accounts());
        SearchPanel.add(jButtonSearch);

        jComboBoxSearch.setBackground(new Color(1, 120, 220));
        jComboBoxSearch.setForeground(Color.white);
        jComboBoxSearch.setPreferredSize(new Dimension(150, 30));
        jComboBoxSearch.addActionListener(e -> searchBank_Accounts());
        SearchPanel.add(jComboBoxSearch);

        txtSearch.setOpaque(true);
        txtSearch.setBackground(new Color(255, 255, 255));
        txtSearch.putClientProperty("JTextField.placeholderText", "Nhập chi nhánh tìm kiếm");
        txtSearch.setPreferredSize(new Dimension(230, 28));
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
        FilterDatePanel.add(txtSearch);

        loadDataTable(bank_accountBLL.getData(bank_accountBLL.getBank_accountListAll()));

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
        bank_accountBLL = new Bank_AccountBLL();
        jTextFieldSearch.setText("");
        txtSearch.setText("");
        branch_id = 0;
        datePicker.clearSelectedDate();
        jComboBoxSearch.setSelectedIndex(0);
        loadDataTable(bank_accountBLL.getData(bank_accountBLL.getBank_accountListAll()));
    }

    private void searchBank_Accounts() {
        List<Bank_Account> bank_accountList = new ArrayList<>(bank_accountBLL.getBank_accountListAll());

        if (!txtSearch.getText().isEmpty()) {
            if (branch_id == 0) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy chi nhánh!.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSearch.setText("");
                return;
            }
            bank_accountList.removeIf(bank_account -> bank_account.getBranch_id() != branch_id);
        }
        if (jComboBoxSearch.getSelectedIndex() == 3) {
            bank_accountList.removeIf(Bank_Account::isStatus);
        }
        if (jComboBoxSearch.getSelectedIndex() == 2) {
            bank_accountList.removeIf(bank_account -> !bank_account.isStatus());
        }
        if (jTextFieldSearch.getText().isEmpty() && datePicker.getDateSQL_Between() == null) {
            loadDataTable(bank_accountBLL.getData(bank_accountList));
        } else {

            if (!jTextFieldSearch.getText().isEmpty()) {
                if (jComboBoxSearch.getSelectedIndex() == 0) {
                    bank_accountList.removeIf(bank_account -> !bank_account.getNumber().toLowerCase().contains(jTextFieldSearch.getText().toLowerCase()));
                }
                if (jComboBoxSearch.getSelectedIndex() == 1) {
                    bank_accountList.removeIf(bank_account -> !bank_account.getCustomer_no().contains(jTextFieldSearch.getText()));
                }
                else {
                    bank_accountList.removeIf(bank_account -> !bank_account.getNumber().toLowerCase().contains(jTextFieldSearch.getText().toLowerCase()));
                }
            }
            if (datePicker.getDateSQL_Between() != null) {
                Date startDate = datePicker.getDateSQL_Between()[0];
                Date endDate = datePicker.getDateSQL_Between()[1];
                if (startDate.after(endDate)) {
                    JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                bank_accountList.removeIf(bank_account -> (bank_account.getCreation_date().before(startDate) || bank_account.getCreation_date().after(endDate)));
            }
            loadDataTable(bank_accountBLL.getData(bank_accountList));
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

            Branch branch = branchBLL.findAllBranchs("id", data[i][3].toString()).get(0);

            data[i][3] = branch.getName();

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

        Bank_Account selectedBank_Account = bank_accountBLL.findAllBank_Accounts("number",  data[indexRow][0].toString()).get(0);
        if (detail && indexColumn == indexColumnDetail) {
            new DetailBank_AccountGUI(selectedBank_Account); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá
        }

    }

    private void changedQuantity() {
        int indexRow = dataTable.getSelectedRow();
        int indexColumn = dataTable.getSelectedColumn();

        if (indexColumn == 5) {
            DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
            Object[] rowData = model.getDataVector().elementAt(indexRow).toArray();

            Bank_Account bank_account = bank_accountBLL.findAllBank_Accounts("number", String.valueOf(rowData[0])).get(0);

            boolean status = String.valueOf(rowData[5]).equals("Đang mở");
            if (status != bank_account.isStatus()) {
                bank_account.setStatus(rowData[5].equals("Đang mở"));
                Pair<Boolean, String> result = bank_accountBLL.updateAllBank_Account(bank_account);
                if (result.getKey()) {
                    Circle_ProgressBar circleProgressBar = new Circle_ProgressBar();
                    circleProgressBar.getRootPane ().setOpaque (false);
                    circleProgressBar.getContentPane ().setBackground (new Color (0, 0, 0, 0));
                    circleProgressBar.setBackground (new Color (0, 0, 0, 0));
                    circleProgressBar.progress();
                    circleProgressBar.setVisible(true);
                    JOptionPane.showMessageDialog(null, result.getValue(),
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    refresh();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (Pair pair : CustomerGUI.pairList) {
                                Customer customer = (Customer) pair.getKey();
                                if (Objects.equals(customer.getCustomerNo(), bank_account.getCustomer_no())) {
                                    List<Card> cardList = (List<Card>) pair.getValue();
                                    for (Card card : cardList) {
                                        if (card.bankAccount.getNumber().equals(bank_account.getNumber())) {
                                            card.bankAccount = bank_account;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    });
                    thread.start();
                } else {
                    JOptionPane.showMessageDialog(null, result.getValue(),
                            "Thông báo", JOptionPane.ERROR_MESSAGE);
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
        List<DataSearch> list = new ArrayList<>();
        branch_id = 0;
        List<Branch> branches = branchBLL.findAllBranchs("name", text) ;
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



