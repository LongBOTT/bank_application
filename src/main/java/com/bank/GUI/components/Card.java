package com.bank.GUI.components;

import com.bank.BLL.CustomerBLL;
import com.bank.DTO.Bank_Account;
import com.bank.DTO.Customer;
import com.bank.utils.Resource;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.text.Normalizer;
import java.util.Objects;
import java.util.regex.Pattern;

public class Card extends RoundedPanel {
    public JLabel balance;
    public Card(Bank_Account bank_account) {
        initComponents(bank_account);
        setVisible(true);
    }

    private void initComponents(Bank_Account bank_account) {
        setBackground(new Color(208, 221, 250));
        setLayout(new FlowLayout(FlowLayout.CENTER, 0 ,0));
        setPreferredSize(new Dimension(300, 160));

        RoundedPanel top = new RoundedPanel();
        top.setLayout(new GridBagLayout());
        top.setPreferredSize(new Dimension(300, 40));
        top.setBackground(new Color(208, 221, 250));
        add(top);

        JPanel jPanel = new JPanel();
        jPanel.setBackground(new Color(77, 76, 78));
        jPanel.setPreferredSize(new Dimension(300, 10));
        add(jPanel);

        RoundedPanel center = new RoundedPanel();
        center.setLayout(new GridBagLayout());
        center.setPreferredSize(new Dimension(300, 50));
        center.setBackground(new Color(208, 221, 250));
        add(center);

        RoundedPanel bottom = new RoundedPanel();
        bottom.setLayout(new MigLayout("", "0[]0[]0"));
        bottom.setPreferredSize(new Dimension(300, 60));
        bottom.setBackground(new Color(208, 221, 250));
        add(bottom);

        RoundedPanel logoPanel = new RoundedPanel();
        logoPanel.setBackground(new Color(208, 221, 250));
        logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setPreferredSize(new Dimension(220, 40));
        top.add(logoPanel);

        RoundedPanel iconPanel = new RoundedPanel();
        iconPanel.setBackground(new Color(208, 221, 250));
        iconPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        iconPanel.setPreferredSize(new Dimension(80, 40));
        top.add(iconPanel);

        JLabel logo = new JLabel(new FlatSVGIcon("icon/logo-acb.svg"));
        logoPanel.add(logo);

        RoundedPanel panel1 = new RoundedPanel();
        panel1.setBackground(new Color(208, 221, 250));
        panel1.setLayout((new MigLayout("", "5[]5")));
        panel1.setPreferredSize(new Dimension(230, 60));
        bottom.add(panel1);

        RoundedPanel panel2 = new RoundedPanel();
        panel2.setBackground(new Color(208, 221, 250));
        panel2.setLayout(new BorderLayout());
        panel2.setPreferredSize(new Dimension(70, 60));
        bottom.add(panel2);

        JLabel icon = new JLabel(new FlatSVGIcon("icon/nfc.svg"));
        iconPanel.add(icon);

        balance = new JLabel("$" + bank_account.getBalance());
        balance.setFont(new Font("Public Sans", Font.BOLD, 20));
        center.add(balance);

        JLabel iconChip = new JLabel(new FlatSVGIcon("icon/chip.svg"));
        panel2.add(iconChip, BorderLayout.CENTER);

        JLabel number = new JLabel(bank_account.getNumber());
        number.setFont(loadFont1("Font/OCR-B/OCR-B.ttf"));
        panel1.add(number, "wrap");


        Customer customer = new CustomerBLL().searchCustomers("[no] = '" + bank_account.getCustomer_no() + "'").get(0);

        String normalized = Normalizer.normalize(customer.getName(), Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String customerName = pattern.matcher(normalized).replaceAll("");

        JLabel name = new JLabel(customerName.toUpperCase());
        name.setFont(loadFont2("Font/OCR-B/OCR-B.ttf"));
        panel1.add(name);
    }
    private static Font loadFont1(String fontPath) {
        Font customFont = null;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.File(Objects.requireNonNull(Resource.getAbsolutePath(fontPath)))).deriveFont(Font.BOLD, 13);
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
