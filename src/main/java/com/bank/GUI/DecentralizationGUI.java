package com.bank.GUI;

import com.bank.BLL.RoleBLL;
import com.bank.DTO.Function;
import com.bank.DTO.Role;
import com.bank.GUI.DialogGUI.FormAddGUI.AddRoleGUI;
import com.bank.GUI.components.*;
import com.bank.utils.Database;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

public class DecentralizationGUI extends Layout1 {
    private RoundedPanel containerSearch;
    private JLabel iconSearch;
    private JTextField jTextFieldSearch;
    private RoleBLL roleBLL = new RoleBLL();
    private int indexColumnRemove = -1;
    private DecentralizationTable detailTable;
    private RoundedScrollPane scrollPane;
    private DataTable dataTable;
    private JButton jButtonSearch;
    private boolean remove = false;
    private String[] columnNames;
    private Object[][] data = new Object[0][0];
    public DecentralizationGUI(List<Function> functions) {
        super();
        if (functions.stream().anyMatch(f -> f.getName().equals("remove"))) remove = true;
        init(functions);
    }

    private void init(List<Function> functions) {
        containerSearch = new RoundedPanel();
        iconSearch = new JLabel();
        jTextFieldSearch = new JTextField();
        columnNames = new String[] {"Tên Chức Vụ"};

        if (remove && Database.headquarter_id == 0) {
            columnNames = Arrays.copyOf(columnNames, columnNames.length + 1);
            indexColumnRemove = columnNames.length - 1;
            columnNames[indexColumnRemove] = "Xoá";
        }

        dataTable = new DataTable(new Object[0][0], columnNames,
                e -> selectFunction(),
                false, false, remove, 1);

        scrollPane = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(300, 680));
        bottom.add(scrollPane, BorderLayout.WEST);
        detailTable = new DecentralizationTable();
        detailTable.setPreferredSize(new Dimension(860, 680));
        bottom.add(detailTable, BorderLayout.EAST);
        jButtonSearch = new JButton("Tìm kiếm");

        containerSearch.setLayout(new MigLayout("", "10[]10[]10", ""));
        containerSearch.setBackground(new Color(245, 246, 250));
        containerSearch.setPreferredSize(new Dimension(280, 40));
        SearchPanel.add(containerSearch);

        iconSearch.setIcon(new FlatSVGIcon("icon/search.svg"));
        containerSearch.add(iconSearch);

        jTextFieldSearch.setFont(new Font("Public Sans", Font.PLAIN, 14));
        jTextFieldSearch.setBackground(new Color(245, 246, 250));
        jTextFieldSearch.setBorder(BorderFactory.createEmptyBorder());
        jTextFieldSearch.putClientProperty("JTextField.placeholderText", "Nhập nội dung tìm kiếm");
        jTextFieldSearch.setPreferredSize(new Dimension(250, 30));
        jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchRoles();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchRoles();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchRoles();
            }
        });
        containerSearch.add(jTextFieldSearch);

        jButtonSearch.setBackground(new Color(1, 120, 220));
        jButtonSearch.setForeground(Color.white);
        jButtonSearch.setPreferredSize(new Dimension(100, 30));
        jButtonSearch.addActionListener(e -> searchRoles());
        SearchPanel.add(jButtonSearch);

        loadDataTable(roleBLL.getData(roleBLL.searchRoles("id != 0")));

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

        if (functions.stream().anyMatch(f -> f.getName().equals("add")) && Database.headquarter_id == 0) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setLayout(new GridBagLayout());
            roundedPanel.setPreferredSize(new Dimension(130, 40));
            roundedPanel.setBackground(new Color(1, 120, 220));
            roundedPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            roundedPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    new AddRoleGUI();
                    refresh();
                }
            });
            FunctionPanel.add(roundedPanel);

            JLabel panel = new JLabel("Thêm mới");
            panel.setFont(new Font("Public Sans", Font.PLAIN, 13));
            panel.setForeground(Color.white);
            panel.setIcon(new FlatSVGIcon("icon/add.svg"));
            roundedPanel.add(panel);
        }
    }

    private void searchRoles() {
        if (jTextFieldSearch.getText().isEmpty()) {
            loadDataTable(roleBLL.getData(roleBLL.searchRoles("id != 0")));
        } else {
            loadDataTable(roleBLL.getData(roleBLL.findRoles("name", jTextFieldSearch.getText())));
        }
    }

    private void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        if (objects.length == 0) {
            return;
        }

        data = new Object[objects.length][objects[0].length];

        for (int i = 0; i < objects.length; i++) {
            System.arraycopy(objects[i], 0, data[i], 0, objects[i].length);
            if (remove && Database.headquarter_id == 0) {
                JLabel iconRemove = new JLabel(new FlatSVGIcon("icon/remove.svg"));
                data[i] = Arrays.copyOf(data[i], data[i].length + 1);
                data[i][data[i].length - 1] = iconRemove;
            }
        }

        for (Object[] object : data) {
            object = Arrays.copyOfRange(object, 1, 3);
            model.addRow(object);
        }
    }

    private void selectFunction() {
        int indexRow = dataTable.getSelectedRow();
        int indexColumn = dataTable.getSelectedColumn();
        if (indexColumn == 1)
            deleteRole(roleBLL.searchRoles("[id] = " + data[indexRow][0]).get(0)); // Đối tượng nào có thuộc tính deleted thì thêm "deleted = 0" để lấy các đối tượng còn tồn tại, chưa xoá
        else
            showDetailRole(roleBLL.searchRoles("[id] = " + data[indexRow][0]).get(0));
    }

    private void deleteRole(Role role) {
        if (dataTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn chức vụ cần xoá.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận xoá chức vụ?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1) {
            Pair<Boolean, String> result = roleBLL.deleteRole(role);
            if (result.getKey()) {
                JOptionPane.showMessageDialog(null, result.getValue(),
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                refresh();
            } else {
                JOptionPane.showMessageDialog(null, result.getValue(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refresh() {
        jTextFieldSearch.setText("");
        detailTable.refreshTable();
        loadDataTable(roleBLL.getData(roleBLL.searchRoles("[id] != 0")));
    }

    private void showDetailRole(Role role) {
        detailTable.refreshTable();
        detailTable.setRole(role);
        if (Database.headquarter_id != 0) {
            for (JCheckBox[] checkBoxes : detailTable.checkboxes) {
                for (JCheckBox checkBox : checkBoxes) {
                    checkBox.setEnabled(false);
                }
            }
        }
    }

}
