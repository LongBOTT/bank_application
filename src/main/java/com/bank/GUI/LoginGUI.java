package com.bank.GUI;

import com.bank.BLL.AccountBLL;
import com.bank.DAL.SQLServer;
import com.bank.DTO.Account;
import com.bank.GUI.components.Circle_ProgressBar;
import com.bank.GUI.components.RoundedPanel;
import com.bank.main.Bank_Application;
import com.bank.utils.Password;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatProgressBarUI;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class LoginGUI extends JFrame {
    private JProgressBar progressBar;
    private JPanel contentPane;
    private JPanel jPanelLogo;
    private JPanel jPanelTitle;
    private JPanel jPanelTitleLogin;
    private JPanel formLogin;
    private JPanel formInput;
    private JLabel labelLogo;
    private JLabel labelLogin;
    private JLabel labelUsername;
    private JLabel labelPassword;
    private JLabel labelForgetPasswd;
    private JTextField jTextFieldUserName;
    private JPasswordField jTextFieldPassword;
    private JButton jButtonLogin;

    public LoginGUI() {
        initComponents();
        login();
    }

    private void initComponents() {
        setIconImage(new FlatSVGIcon("icon/ACB.svg").getImage());
        setTitle("Hệ Thống Quản Lý Ngân Hàng");
        setSize(780, 500);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(232, 206, 180));
        setContentPane(contentPane);

        formLogin = new JPanel(new FlowLayout());
        formLogin.setBackground(new Color(255, 255, 255));
        formLogin.setPreferredSize(new Dimension(400, 500));
        contentPane.add(formLogin, BorderLayout.EAST);

        jPanelLogo = new JPanel(new BorderLayout());
        jPanelLogo.setBackground(new Color(232, 26, 180));
        jPanelLogo.setPreferredSize(new Dimension(380, 500));
        contentPane.add(jPanelLogo, BorderLayout.WEST);

        labelLogo = new JLabel(new FlatSVGIcon("icon/logoBank.svg"));
        labelLogo.setHorizontalAlignment(SwingConstants.CENTER);
        jPanelLogo.add(labelLogo, BorderLayout.CENTER);

        jPanelTitle = new JPanel();
        jPanelTitle.setBackground(new Color(255, 255, 255));
        formLogin.add(jPanelTitle, BorderLayout.NORTH);

        jPanelTitleLogin = new JPanel(new GridBagLayout());
        jPanelTitleLogin.setBackground(new Color(255, 255, 255));
        jPanelTitleLogin.setPreferredSize(new Dimension(300, 100));
        jPanelTitle.add(jPanelTitleLogin);

        labelLogin = new JLabel("Login", SwingConstants.CENTER);
        labelLogin.setFont(new Font("Jost", Font.BOLD, 30));
        jPanelTitleLogin.add(labelLogin);

        formInput = new JPanel(new MigLayout("", "[]", "[]0[]"));
        formInput.setBackground(new Color(255, 255, 255));
        formInput.setPreferredSize(new Dimension(350, 400));
        formLogin.add(formInput, BorderLayout.CENTER);

        labelUsername = new JLabel("Username", JLabel.LEFT);
        labelUsername.setForeground(new Color(166, 175, 182));
        labelUsername.setPreferredSize(new Dimension(100, 50));
        labelUsername.setFont(new Font("Inter", Font.BOLD, 13));
        formInput.add(labelUsername, "span, wrap");


        RoundedPanel panelUsername = new RoundedPanel();
        panelUsername.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
        panelUsername.setPreferredSize(new Dimension(350, 50));
        panelUsername.setBackground(new Color(221, 229, 250));
        formInput.add(panelUsername, "span, wrap");

        JLabel iconUser = new JLabel(new FlatSVGIcon("icon/user.svg"));
        panelUsername.add(iconUser);

        // sửa
        jTextFieldUserName = new JTextField();
        jTextFieldUserName.setText("longbott");
        jTextFieldUserName.setBackground(new Color(221, 229, 250));
        jTextFieldUserName.setBorder(BorderFactory.createEmptyBorder());
        jTextFieldUserName.setPreferredSize(new Dimension(270, 40));
        jTextFieldUserName.setFont(new Font("Inter", Font.BOLD, 15));
        jTextFieldUserName.putClientProperty("JTextField.placeholderText", "Enter username");
        jTextFieldUserName.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
                    login();
            }
        });
        panelUsername.add(jTextFieldUserName);

        labelPassword = new JLabel("Password", JLabel.LEFT);
        labelPassword.setForeground(new Color(166, 175, 182));
        labelPassword.setPreferredSize(new Dimension(100, 50));
        labelPassword.setFont(new Font("FlatLaf.style", Font.BOLD, 13));
        formInput.add(labelPassword, "span, wrap");

        RoundedPanel panelPasswd = new RoundedPanel();
        panelPasswd.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
        panelPasswd.setPreferredSize(new Dimension(350, 50));
        panelPasswd.setBackground(new Color(221, 229, 250));
        formInput.add(panelPasswd, "span, wrap");

        JLabel iconLock = new JLabel(new FlatSVGIcon("icon/lock.svg"));
        panelPasswd.add(iconLock);

        jTextFieldPassword = new JPasswordField();
        jTextFieldPassword.setText("Long123.");
        jTextFieldPassword.setBackground(new Color(221, 229, 250));
        jTextFieldPassword.setBorder(BorderFactory.createEmptyBorder());
        jTextFieldPassword.setPreferredSize(new Dimension(270, 40));
        jTextFieldPassword.setFont(new Font("Inter", Font.BOLD, 15));
        jTextFieldPassword.putClientProperty("JTextField.placeholderText", "Enter password");
        jTextFieldPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
                    login();
            }
        });
        panelPasswd.add(jTextFieldPassword);

        labelForgetPasswd = new JLabel("Forgot password?");
        labelForgetPasswd.setForeground(new Color(37, 181, 251));
        labelForgetPasswd.setFont(new Font("Inter", Font.BOLD, 13));
        labelForgetPasswd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        labelForgetPasswd.setBorder(BorderFactory.createEmptyBorder(0,0,10, 0));
        labelForgetPasswd.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                forgotPassword();
            }
        });
        formInput.add(labelForgetPasswd, "span, right, wrap");

        jButtonLogin = new JButton("Login");
        jButtonLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButtonLogin.setBackground(new Color(1, 120, 220));
        jButtonLogin.setForeground(Color.WHITE);
        jButtonLogin.setFont(new Font("Inter", Font.BOLD, 18));
        jButtonLogin.setPreferredSize(new Dimension(100, 40));
        jButtonLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                login();
            }
        });
        jButtonLogin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    login();
            }
        });
        formInput.add(jButtonLogin, "span, wrap, center");

    }

    private void progress() {
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("FlatLaf.style", Font.BOLD, 15));
        progressBar.setForeground(new Color(0x97B4EA));
        progressBar.setUI(new FlatProgressBarUI());
        contentPane.add(progressBar, BorderLayout.SOUTH);
        contentPane.repaint();
        contentPane.revalidate();
        int i = 0;
        while (i <= 100) {
            i++;
            progressBar.setValue(i);
            try {
                sleep(10);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        setEnabled(true);
        dispose();
        System.gc();
        Bank_Application.homeGUI.setVisible(true);
        contentPane.remove(progressBar);
        contentPane.repaint();
        contentPane.revalidate();
    }

    public void login() {
        String userName, passWord;
        userName = jTextFieldUserName.getText();
        passWord = new String(jTextFieldPassword.getPassword());
        System.out.println(userName + " " + passWord + "\n");

        if (userName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (passWord.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SQLServer sqlServer = new SQLServer();
        List<List<String>> list = new ArrayList<>();
        try {
            list = sqlServer.executeProcedure("sp_CheckLogin", new Pair<>("username", userName));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        String headquarter = list.get(0).get(0);
        if (headquarter.equals("0")) {
            JOptionPane.showMessageDialog(this, "Tài khoản không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!headquarter.equals("1")) {
            headquarter = headquarter.split("\\\\")[1];
            if (headquarter.equals("HO_CHI_MINH"))
                headquarter = "Hồ Chí Minh";
            if (headquarter.equals("HA_NOI"))
                headquarter = "Hà Nội";
            if (headquarter.equals("HAI_PHONG"))
                headquarter = "Hải Phòng";
            if (headquarter.equals("DA_NANG"))
                headquarter = "Đà Nẵng";
            if (headquarter.equals("OTHERS"))
                headquarter = "Khác";
            JOptionPane.showMessageDialog(this, "Tài khoản thuộc trụ sở " + headquarter + " !", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AccountBLL accountBLL = new AccountBLL();
        List<Account> accountList = accountBLL.searchAccounts("[username] = '" + userName + "'");

        String hashedPassword = accountList.get(0).getPassword();
        if (hashedPassword.startsWith("first"))
            hashedPassword = hashedPassword.substring("first".length());
        if (!Password.verifyPassword(passWord, hashedPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        setEnabled(false);
        Account account = accountList.get(0);
        try {
            Thread thread = new Thread(() -> Bank_Application.homeGUI.setAccount(account));
            thread.start();
            thread.join();
            Thread threadProgress = new Thread(this::progress);
            threadProgress.start();
        } catch (Exception ignored) {

        }
    }

    private void forgotPassword() {
        new ChangePasswordGUI().setVisible(true);
    }

    private void cancel() {
        String[] options = new String[]{"Huỷ", "Thoát ứng dụng"};
        int choice = JOptionPane.showOptionDialog(null, "Bạn có muốn thoát ứng dụng?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[1]);
        if (choice == 1)
            Bank_Application.exit(1);
    }

}
