package com.bank.GUI.components;

import com.bank.BLL.CustomerBLL;
import com.bank.DTO.Bank_Account;
import com.bank.DTO.Customer;
import com.bank.utils.Resource;
import com.bank.utils.VNString;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

public class Card extends RoundedPanel {
    public Bank_Account bankAccount;
    public JLabel balance;
    public boolean mouseListenerIsActive = true;
    public Card(Bank_Account bank_account) {
        this.bankAccount = bank_account;
        initComponents(bank_account);
        setVisible(true);
    }

    private void initComponents(Bank_Account bank_account) {
        setBackground(new Color(51,162,255));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 160));

        RoundedPanel top = new RoundedPanel();
        top.setLayout(new GridBagLayout());
        top.setPreferredSize(new Dimension(300, 60));
        top.setBackground(new Color(255, 255, 255));
        top.setBorder(BorderFactory.createMatteBorder(0,0,20, 0, new Color(31, 31, 31)));
        add(top, BorderLayout.NORTH);

        RoundedPanel center = new RoundedPanel();
        center.setLayout(new BorderLayout());
        center.setPreferredSize(new Dimension(300, 50));
        center.setBackground(new Color(51,162,255));
        add(center, BorderLayout.CENTER);

        RoundedPanel bottom = new RoundedPanel();
        bottom.setLayout(new BorderLayout());
        bottom.setPreferredSize(new Dimension(300, 60));
        bottom.setBackground(new Color(51,162,255));
        add(bottom, BorderLayout.SOUTH);

        RoundedPanel logoPanel = new RoundedPanel();
        logoPanel.setBackground(new Color(255, 255, 255));
        logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setPreferredSize(new Dimension(200, 40));
        top.add(logoPanel);

        RoundedPanel iconPanel = new RoundedPanel();
        iconPanel.setLayout(new BorderLayout());
        iconPanel.setBackground(new Color(255, 255, 255));
        iconPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        iconPanel.setPreferredSize(new Dimension(100, 40));
        top.add(iconPanel);

        JLabel logo = new JLabel(new FlatSVGIcon("icon/logo-acb.svg"));
        logo.setBorder(BorderFactory.createEmptyBorder(0,5, 0 ,0));
        logoPanel.add(logo);

        JLabel icon = new JLabel(new FlatSVGIcon("icon/Napas.svg"));
        icon.setVerticalAlignment(JLabel.CENTER);
        icon.setHorizontalAlignment(JLabel.CENTER);
        iconPanel.add(icon);

        balance = new JLabel(VNString.currency(Double.parseDouble(bank_account.getBalance().toString())));
        balance.setForeground(Color.white);
        balance.setHorizontalAlignment(JLabel.CENTER);
        balance.setVerticalAlignment(JLabel.CENTER);
        balance.setFont(new Font("Public Sans", Font.BOLD, 20));
        center.add(balance, BorderLayout.CENTER);

        RoundedPanel panel1 = new RoundedPanel();
        panel1.setBackground(new Color(51,162,255));
        panel1.setLayout(new BorderLayout());
        panel1.setPreferredSize(new Dimension(180, 60));
        bottom.add(panel1, BorderLayout.WEST);

        RoundedPanel panel2 = new RoundedPanel();
        panel2.setBackground(new Color(51,162,255));
        panel2.setLayout(new BorderLayout());
        panel2.setPreferredSize(new Dimension(120, 60));
        bottom.add(panel2, BorderLayout.EAST);

        String validFrom = new SimpleDateFormat("MM/yy").format(bank_account.getCreation_date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bank_account.getCreation_date());
        calendar.add(Calendar.YEAR, 5);
        Date newDate = calendar.getTime();
        String validThru = new SimpleDateFormat("MM/yy").format(newDate);
        JLabel labelValidFrom = new JLabel("<html><p style='font-size:6pt'>VALID FROM&nbsp;&nbsp;&nbsp;VALID THRU</p><b>" + validFrom + "&nbsp;&nbsp;&nbsp;&nbsp;" + validThru + "</b></html>");
//        labelValidFrom.setBorder(BorderFactory.createEmptyBorder(0,10, 5 ,0));
        labelValidFrom.setFont(loadFont2("Font/OCR-B/OCR-B.ttf"));
        labelValidFrom.setForeground(Color.white);
        panel2.add(labelValidFrom, BorderLayout.CENTER);

        JLabel number = new JLabel(bank_account.getNumber());
        number.setBorder(BorderFactory.createEmptyBorder(0,10, 0 ,0));
        number.setFont(loadFont1("Font/OCR-B/OCR-B.ttf"));
        number.setForeground(Color.white);
        panel1.add(number, BorderLayout.CENTER);

        Customer customer = new CustomerBLL().searchCustomers("[no] = '" + bank_account.getCustomer_no() + "'").get(0);

        String normalized = Normalizer.normalize(customer.getName(), Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String customerName = pattern.matcher(normalized).replaceAll("");

        JLabel name = new JLabel(customerName.toUpperCase());
        name.setBorder(BorderFactory.createEmptyBorder(0,10, 5 ,0));
        name.setFont(loadFont2("Font/OCR-B/OCR-B.ttf"));
        name.setForeground(Color.white);
        panel1.add(name, BorderLayout.SOUTH);
    }
    private static Font loadFont1(String fontPath) {
        Font customFont = null;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.File(Objects.requireNonNull(Resource.getAbsolutePath(fontPath)))).deriveFont(Font.PLAIN, 13);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customFont;
    }

    private static Font loadFont2(String fontPath) {
        Font customFont = null;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.File(Objects.requireNonNull(Resource.getAbsolutePath(fontPath)))).deriveFont(Font.PLAIN, 8);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customFont;
    }
}
