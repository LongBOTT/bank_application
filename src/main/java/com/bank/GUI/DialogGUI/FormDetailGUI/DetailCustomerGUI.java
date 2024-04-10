package com.bank.GUI.DialogGUI.FormDetailGUI;
;
import com.bank.BLL.Bank_AccountBLL;
import com.bank.BLL.BranchBLL;
import com.bank.DTO.Bank_Account;
import com.bank.DTO.Branch;
import com.bank.DTO.Customer;
import com.bank.GUI.Bank_AccountGUI;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.HomeGUI;
import com.bank.GUI.components.*;
import com.bank.main.Bank_Application;
import com.bank.utils.Email;
import com.toedter.calendar.JDateChooser;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailCustomerGUI extends DialogForm {
    private Bank_AccountBLL bankAccountBLL = new Bank_AccountBLL();
    private BranchBLL branchBLL = new BranchBLL();
    private JLabel titleName;
    private List<JLabel> attributeCustomer;
    private List<JTextField> jTextFieldsCustomer;
    private JDateChooser jDateChooser = new JDateChooser();
    private DataTable dataTable;
    private Object[][] data = new Object[0][0];
    private Thread currentCountDownThread;
    private Customer customer;
    public DetailCustomerGUI(Customer customer) {
        super();
        super.setTitle("Thông Tin Khách Hàng ");
        super.setSize(new Dimension(1000, 600));
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        this.customer = customer;
        init(customer);
        setVisible(true);
    }

    private void init(Customer customer) {
        titleName = new JLabel();
        attributeCustomer = new ArrayList<>();
        jTextFieldsCustomer = new ArrayList<>();

        titleName.setText("Thông Tin Khách Hàng");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        content.setPreferredSize(new Dimension(1000, 250));

        for (String string : new String[]{"Tên Khách Hàng", "CCCD", "Giới Tính",
                "Ngày Sinh", "Số Điện Thoại", "Địa Chỉ", "Email"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(150, 35));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 15)));
            attributeCustomer.add(label);
            content.add(label);
            MyTextFieldUnderLine textField = new MyTextFieldUnderLine();
            textField.setPreferredSize(new Dimension(280, 35));
            textField.setFont((new Font("Public Sans", Font.PLAIN, 14)));
            textField.setEditable(false);
            if (string.trim().equals("Ngày Sinh")) {
                Date birthDate = customer.getBirthdate();
                jDateChooser = new JDateChooser();
                jDateChooser.setDateFormatString("dd/MM/yyyy");
                jDateChooser.setDate(birthDate);
                jDateChooser.setPreferredSize(new Dimension(180, 35));
                jDateChooser.setMinSelectableDate(java.sql.Date.valueOf("1000-01-01"));
                jDateChooser.setEnabled(false);
                content.add(jDateChooser, "wrap");
            } else {
                if (string.trim().equals("Tên Khách Hàng")) {
                    textField.setText(customer.getName());
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("CCCD")) {
                    textField.setText(customer.getCustomerNo());
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Giới Tính")) {
                    boolean gender = customer.isGender();
                    String gender1 = gender ? "Nữ" : "Nam";
                    textField.setText(gender1);
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("Số Điện Thoại")) {
                    textField.setText(customer.getPhone());
                    content.add(textField);
                    continue;
                }
                if (string.trim().equals("Địa Chỉ")) {
                    textField.setText(customer.getAddress());
                    content.add(textField, "wrap");
                    continue;
                }
                if (string.trim().equals("Email")) {
                    textField.setText(customer.getEmail());
                    content.add(textField);
                }
            }
        }

        String[] columnNames = new String[]{"Số Thẻ", "CCCD", "Số Dư", "Chi Nhánh", "Ngày Mở", "Trạng Thái"};
        dataTable = new DataTable(new Object[0][0], columnNames, e -> selectFunction());

        super.remove(containerButton);
        RoundedScrollPane scrollPanel = new RoundedScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.getViewport().setBackground(new Color(191, 198, 208));
        scrollPanel.setPreferredSize(new Dimension(1000, 300));
        super.add(scrollPanel, "wrap");

        loadDataTable(bankAccountBLL.getData(bankAccountBLL.findAllBank_Accounts("customer_no", customer.getCustomerNo())));
    }

    public void loadDataTable(Object[][] objects) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);

        if (objects.length == 0) {
            return;
        }

        data = new Object[objects.length + 1][objects[0].length];

        for (int i = 0; i < objects.length; i++) {
            System.arraycopy(objects[i], 0, data[i], 0, objects[i].length);
            Branch branch = branchBLL.findAllBranchs("id", data[i][3].toString()).get(0);

            data[i][3] = branch.getName();
        }

        data[data.length - 1][0] = "+";

        for (Object[] object : data) {
            model.addRow(object);
        }
    }

    private void selectFunction() {
        int indexRow = dataTable.getSelectedRow();
        int indexColumn = dataTable.getSelectedColumn();
        Pair<Boolean, String> result = new Pair<>(true, "");
        if (indexColumn == 0 && indexRow == dataTable.getRowCount() - 1) {
            String[] options = new String[]{"Huỷ", "Xác nhận"};
            int choice = JOptionPane.showOptionDialog(null, "Xác nhận mở tài khoản ngân hàng cho khách hàng?",
                    "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (choice == 1) {
                String number = bankAccountBLL.getAutoNumber();
                Bank_Account bank_account = new Bank_Account();
                bank_account.setNumber(number);
                bank_account.setCustomer_no(customer.getCustomerNo());
                bank_account.setBalance(BigDecimal.valueOf(0));
                bank_account.setBranch_id(HomeGUI.staff.getBranch_id());
                bank_account.setCreation_date(java.sql.Date.valueOf(LocalDate.now()));
                bank_account.setStatus(true);

                result = bankAccountBLL.addBank_Account(bank_account);

                if (!result.getKey()) {
                    JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    Thread thread = new Thread(() -> sendOTP(customer.getEmail(), number));
                    thread.start();
                } catch (Exception ignored) {

                }
                Circle_ProgressBar circleProgressBar = new Circle_ProgressBar();
                circleProgressBar.getRootPane ().setOpaque (false);
                circleProgressBar.getContentPane ().setBackground (new Color (0, 0, 0, 0));
                circleProgressBar.setBackground (new Color (0, 0, 0, 0));
                circleProgressBar.progress();
                circleProgressBar.setVisible(true);
                JOptionPane.showMessageDialog(null, result.getValue(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                bankAccountBLL = new Bank_AccountBLL();
                loadDataTable(bankAccountBLL.getData(bankAccountBLL.findAllBank_Accounts("customer_no", customer.getCustomerNo())));
                Bank_AccountGUI bankAccountGUI = (Bank_AccountGUI) Bank_Application.homeGUI.allPanelModules[Bank_Application.homeGUI.indexModuleBank_AccountGUI];
                bankAccountGUI.refresh();
            }
        }
    }

    private void sendOTP(String email, String number)    {
        if (currentCountDownThread != null)
            currentCountDownThread.interrupt();
        currentCountDownThread = new Thread(() -> {
            Email.sendOTP(email, "Mở thẻ Ngân Hàng ACB",
                    "<html><p>Ngân Hàng ACB xin thông báo quý khách đã mở thẻ thành công vào ngày <strong> "+ LocalDate.now() + " </strong>.</p>" +
                            "    <p>Số thẻ của quý khách là: <strong>" + number +"</strong></p>" +
                            "    <p>Mật khẩu mặc định là: <strong>" + Email.getOTP() + "</strong></p>" +
                            "    <p>Vui lòng thực hiện thay đổi mật khẩu.</p></html>");
        });
        currentCountDownThread.start();
    }
}

