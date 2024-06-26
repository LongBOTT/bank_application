package com.bank.GUI.DialogGUI.FormAddGUI;


import com.bank.BLL.Bank_AccountBLL;
import com.bank.BLL.CustomerBLL;
import com.bank.DTO.Bank_Account;
import com.bank.DTO.Customer;
import com.bank.GUI.Bank_AccountGUI;
import com.bank.GUI.CustomerGUI;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.DialogGUI.FormDetailGUI.DetailBank_AccountGUI;
import com.bank.GUI.HomeGUI;
import com.bank.GUI.components.Card;
import com.bank.GUI.components.Circle_ProgressBar;
import com.bank.GUI.components.DatePicker;
import com.bank.GUI.components.MyTextFieldUnderLine;

import com.bank.main.Bank_Application;
import com.bank.utils.Email;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bank.GUI.DialogGUI.FormDetailGUI.DetailCustomerGUI.loadCardList;


public class AddCustomerGUI extends DialogForm {
    private List<JLabel> attributeCustomer;
    private List<JTextField> jTextFieldsCustomer;
    private JLabel titleName;
    private JButton buttonCancel;
    private JButton buttonAdd;
    private DatePicker datePicker;
    private JFormattedTextField editor;
    private CustomerBLL customerBLL = new CustomerBLL();
    private JRadioButton radioMale = new JRadioButton();
    private JRadioButton radioFemale = new JRadioButton();
    private ButtonGroup Gender;
    private Thread currentCountDownThread;
    private ActionListener refresh;
    public AddCustomerGUI(ActionListener refresh) {
        super();
        super.setTitle("Thêm Khách Hàng");
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        this.refresh = refresh;
        init();
        setVisible(true);
    }

    private void init() {
        titleName = new JLabel();
        attributeCustomer = new ArrayList<>();
        jTextFieldsCustomer = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonAdd = new JButton("Thêm");

        titleName.setText("Thêm Khách Hàng");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);

        content.setPreferredSize(new Dimension(1000, 250));

        Gender = new ButtonGroup();

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
            textField.setBackground(new Color(245, 246, 250));

            if (string.trim().equals("Ngày Sinh")) {
                datePicker = new DatePicker();
                editor = new JFormattedTextField();
                datePicker.setDateSelectionMode(raven.datetime.component.date.DatePicker.DateSelectionMode.SINGLE_DATE_SELECTED);
                datePicker.setEditor(editor);
                datePicker.setUsePanelOption(true);
                datePicker.setCloseAfterSelected(true);

                editor.setPreferredSize(new Dimension(280, 40));
                editor.setFont(new Font("Inter", Font.BOLD, 15));
                content.add(editor, "wrap");
            } else {
                if (string.trim().equals("Giới Tính")) {
                    JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    jPanel.setPreferredSize(new Dimension(280, 35));
                    jPanel.setBackground(Color.white);

                    radioMale = new JRadioButton("Nam");
                    radioFemale = new JRadioButton("Nữ");

                    jPanel.add(radioMale);
                    jPanel.add(radioFemale);

                    Gender.add(radioMale);
                    Gender.add(radioFemale);

                    content.add(jPanel);

                }
                else {
                    if (string.trim().equals("Tên Khách Hàng") || string.trim().equals("Số Điện Thoại") || string.trim().equals("Email")) {
                        jTextFieldsCustomer.add(textField);
                        content.add(textField);
                    } else {
                        jTextFieldsCustomer.add(textField);
                        content.add(textField, "wrap");
                    }
                }
            }
        }

        buttonCancel.setPreferredSize(new Dimension(100, 30));
        buttonCancel.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                buttonCancel.setBackground(new Color(0xD54218));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonCancel.setBackground(Color.white);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                cancel();
            }
        });
        containerButton.add(buttonCancel);

        buttonAdd.setPreferredSize(new Dimension(100, 30));
        buttonAdd.setBackground(new Color(1, 120, 220));
        buttonAdd.setForeground(Color.white);
        buttonAdd.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addCustomer();

            }
        });
        containerButton.add(buttonAdd);

    }

    private void addCustomer() {
        Pair<Boolean, String> result;
        String customerNo, name, phone, address, email;
        boolean gender;
        Date birthdate;
        name = jTextFieldsCustomer.get(0).getText().trim();
        customerNo = jTextFieldsCustomer.get(1).getText().trim();
        gender = !radioMale.isSelected();
        birthdate = datePicker.getDateSQL_Single();
        phone = jTextFieldsCustomer.get(2).getText().trim();
        address = jTextFieldsCustomer.get(3).getText().trim();
        email = jTextFieldsCustomer.get(4).getText().trim();
        if (birthdate == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập ngày sinh!",
                    "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Customer customer = new Customer(customerNo, name, gender, birthdate, phone, address, email, false);

        Bank_AccountBLL bankAccountBLL = new Bank_AccountBLL();

        result = customerBLL.addCustomer(customer);

        if (result.getKey()) {
            if (result.getValue().equals("Khôi phục khách hàng thành công.")) {
                Customer rollBackCustomer = customerBLL.searchCustomers("[no] = '" + customerNo + "'").get(0);
                List<Card> cardList = new ArrayList<>();
                bankAccountBLL = new Bank_AccountBLL();
                for (Bank_Account bank_account : bankAccountBLL.findAllBank_Accounts("customer_no", rollBackCustomer.getCustomerNo())) {
                    Card card = new Card(bank_account);
                    card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    card.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            if (card.mouseListenerIsActive) {
                                new DetailBank_AccountGUI(card.bankAccount);
                                loadCardList();
                            }
                        }
                    });
                    cardList.add(card);
                }
                Pair<Customer, List<Card>> pair = new Pair<>(customer,cardList);
                CustomerGUI.pairList.add(pair);
            }
            else if (!result.getValue().equals("Khôi phục khách hàng thành công.")) {
                String number = bankAccountBLL.getAutoNumber();
                Bank_Account bank_account = new Bank_Account();
                bank_account.setNumber(number);
                bank_account.setCustomer_no(customer.getCustomerNo());
                bank_account.setBalance(BigDecimal.valueOf(0));
                bank_account.setBranch_id(HomeGUI.staff.getBranch_id());
                bank_account.setCreation_date(java.sql.Date.valueOf(LocalDate.now()));
                bank_account.setStatus(true);

                if (!bankAccountBLL.addBank_Account(bank_account).getKey()) {
                    JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Card card = new Card(bank_account);
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (card.mouseListenerIsActive) {
                            new DetailBank_AccountGUI(card.bankAccount);
                            loadCardList();
                        }
                    }
                });
                Pair<Customer, List<Card>> pair = new Pair<>(customer, List.of(card));
                CustomerGUI.pairList.add(pair);

                try {
                    Thread thread = new Thread(() -> sendOTP(email, number));
                    thread.start();
                } catch (Exception ignored) {

                }
            }
            Circle_ProgressBar circleProgressBar = new Circle_ProgressBar();
            circleProgressBar.getRootPane ().setOpaque (false);
            circleProgressBar.getContentPane ().setBackground (new Color (0, 0, 0, 0));
            circleProgressBar.setBackground (new Color (0, 0, 0, 0));
            circleProgressBar.progress();
            circleProgressBar.setVisible(true);
            JOptionPane.showMessageDialog(null, result.getValue(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            refresh.actionPerformed(null);
            if (Bank_Application.homeGUI.indexModuleBank_AccountGUI != -1) {
                Bank_AccountGUI bankAccountGUI = (Bank_AccountGUI) Bank_Application.homeGUI.allPanelModules[Bank_Application.homeGUI.indexModuleBank_AccountGUI];
                bankAccountGUI.refresh();
            }

        } else {
            JOptionPane.showMessageDialog(null, result.getValue(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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
