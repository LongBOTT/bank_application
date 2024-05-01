package com.bank.GUI.DialogGUI.FormDetailGUI;

import com.bank.BLL.Bank_AccountBLL;
import com.bank.BLL.BranchBLL;
import com.bank.DTO.Bank_Account;
import com.bank.DTO.Customer;
import com.bank.GUI.Bank_AccountGUI;
import com.bank.GUI.CustomerGUI;
import com.bank.GUI.DialogGUI.DialogForm;
import com.bank.GUI.HomeGUI;
import com.bank.GUI.components.*;
import com.bank.main.Bank_Application;
import com.bank.utils.Email;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DetailCustomerGUI extends DialogForm {
    private Bank_AccountBLL bankAccountBLL = new Bank_AccountBLL();
    private BranchBLL branchBLL = new BranchBLL();
    private JLabel titleName;
    private List<JLabel> attributeCustomer;
    private List<JTextField> jTextFieldsCustomer;
    private Thread currentCountDownThread;
    private static Customer customer;
    private static List<Card> cardList;
    private RoundedPanel addPanel;
    public static RoundedPanel roundedPanel1 = new RoundedPanel();
    public DetailCustomerGUI(Pair<Customer, List<Card>> pair) {
        super();
        super.setName("DetailCustomer");
        super.setTitle("Thông Tin Khách Hàng ");
        super.setSize(new Dimension(1000, 600));
        super.setLocationRelativeTo(Bank_Application.homeGUI);
        customer = pair.getKey();
        cardList = pair.getValue();
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
                textField.setText(new SimpleDateFormat("dd-MM-yyyy").format(customer.getBirthdate()));
                textField.setEditable(false);
                content.add(textField, "wrap");
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

        super.remove(containerButton);
        RoundedPanel roundedPanel = new RoundedPanel();
        roundedPanel.setBackground(new Color(76,116,203));
        roundedPanel.setLayout(new GridBagLayout());
        roundedPanel.setPreferredSize(new Dimension(1000, 220));
        super.add(roundedPanel, "wrap");

        roundedPanel1.setBackground(new Color(76,116,203));
        roundedPanel1.setLayout(new FlowLayout(FlowLayout.CENTER));

        RoundedScrollPane scrollPanel = new RoundedScrollPane(roundedPanel1, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.getViewport().setBackground(new Color(76,116,203));
        scrollPanel.setPreferredSize(new Dimension(850, 180));
        roundedPanel.add(scrollPanel);

        roundedPanel1.removeAll();
        for (Card card: cardList) {
            roundedPanel1.add(card);
        }

        addPanel = new RoundedPanel();
        addPanel.setBackground(new Color(137,170,243));
        addPanel.setLayout(new GridBagLayout());
        addPanel.setPreferredSize(new Dimension(300, 160));
        addPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addBank_Account();
            }
        });
        roundedPanel1.add(addPanel);


        JLabel iconAdd = new JLabel(new FlatSVGIcon("icon/add.svg"));
        iconAdd.setText("Mở Tài Khoản");
        iconAdd.setForeground(Color.white);
        iconAdd.setFont((new Font("Public Sans", Font.BOLD, 15)));
        iconAdd.setIconTextGap(10);
        addPanel.add(iconAdd);

        roundedPanel1.repaint();
        roundedPanel1.revalidate();
    }

    private static void addBank_Account() {
        Pair<Boolean, String> result;
        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận mở tài khoản ngân hàng cho khách hàng?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if (choice == 1) {
            Bank_AccountBLL bankAccountBLL = new Bank_AccountBLL();
            if (!bankAccountBLL.findAllBank_AccountsActiveByStaffAndBranch(customer.getCustomerNo(), HomeGUI.staff.getBranch_id()).isEmpty()) {
                JOptionPane.showMessageDialog(null, "Mỗi khách hàng chỉ sở sữu 1 tài khoản ngân hàng tại mỗi chi nhánh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

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

            Card newCard = new Card(bank_account);
            newCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
            newCard.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (newCard.mouseListenerIsActive) {
                        new DetailBank_AccountGUI(newCard.bankAccount);
                        loadCardList();
                    }
                }
            });
            cardList.add(newCard);

            try {
                Thread threadRepaint = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadCardList();
                    }
                });
                threadRepaint.start();
                threadRepaint.join();
                Thread thread = new Thread(() -> {
                    Email.sendOTP(customer.getEmail(), "Mở thẻ Ngân Hàng ACB",
                            "<html><p>Ngân Hàng ACB xin thông báo quý khách đã mở thẻ thành công vào ngày <strong> "+ LocalDate.now() + " </strong>.</p>" +
                                    "    <p>Số thẻ của quý khách là: <strong>" + number +"</strong></p>" +
                                    "    <p>Mật khẩu mặc định là: <strong>" + Email.getOTP() + "</strong></p>" +
                                    "    <p>Vui lòng thực hiện thay đổi mật khẩu.</p></html>");
                });
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

            if (Bank_Application.homeGUI.indexModuleBank_AccountGUI != -1) {
                Bank_AccountGUI bankAccountGUI = (Bank_AccountGUI) Bank_Application.homeGUI.allPanelModules[Bank_Application.homeGUI.indexModuleBank_AccountGUI];
                bankAccountGUI.refresh();
            }
        }
    }

    public static void loadCardList() {
        DetailCustomerGUI.roundedPanel1.removeAll();
        for (Pair pair : CustomerGUI.pairList) {
            Customer customer = (Customer) pair.getKey();
            if (Objects.equals(customer.getCustomerNo(), DetailCustomerGUI.customer.getCustomerNo())) {
                List<Card> cardList = (List<Card>) pair.getValue();
                for (Card card : cardList) {
                    DetailCustomerGUI.roundedPanel1.add(card);
                }
                break;
            }
        }

        RoundedPanel addPanel = new RoundedPanel();
        addPanel.setBackground(new Color(137,170,243));
        addPanel.setLayout(new GridBagLayout());
        addPanel.setPreferredSize(new Dimension(300, 160));
        addPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addBank_Account();
            }
        });
        JLabel iconAdd = new JLabel(new FlatSVGIcon("icon/add.svg"));
        iconAdd.setText("Mở Tài Khoản");
        iconAdd.setForeground(Color.white);
        iconAdd.setFont((new Font("Public Sans", Font.BOLD, 15)));
        iconAdd.setIconTextGap(10);
        addPanel.add(iconAdd);

        DetailCustomerGUI.roundedPanel1.add(addPanel);
        DetailCustomerGUI.roundedPanel1.repaint();
        DetailCustomerGUI.roundedPanel1.revalidate();
    }

}

