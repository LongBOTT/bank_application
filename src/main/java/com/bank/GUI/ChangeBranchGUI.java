package com.bank.GUI;

import com.bank.BLL.BranchBLL;
import com.bank.BLL.RoleBLL;
import com.bank.BLL.Role_DetailBLL;
import com.bank.BLL.StaffBLL;
import com.bank.DTO.Branch;
import com.bank.DTO.Role;
import com.bank.DTO.Role_Detail;
import com.bank.DTO.Staff;
import com.bank.GUI.DialogGUI.FormEditGUI.EditStaffGUI;
import com.bank.GUI.components.Circle_ProgressBar;
import com.bank.GUI.components.MyTextFieldUnderLine;
import com.bank.GUI.components.RoundedPanel;
import com.bank.GUI.components.swing.DataSearch;
import com.bank.GUI.components.swing.EventClick;
import com.bank.GUI.components.swing.MyTextField;
import com.bank.GUI.components.swing.PanelSearch;
import com.bank.main.Bank_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChangeBranchGUI extends JDialog {
    private RoundedPanel role_detail_panel;
    private JLabel titleName;
    private List<JLabel> attributeRole_detail;
    private MyTextField txtSearch;
    private PanelSearch search;
    private JPopupMenu menu;
    private JButton buttonCancel;
    private JButton buttonSet;
    private Staff staff;
    private ActionListener refresh;
    private RoundedPanel content = new RoundedPanel();
    private BranchBLL branchBLL = new BranchBLL();
    private int branch_id = 0;
    public ChangeBranchGUI(ActionListener refresh, Staff staff) {
        super((Frame) null, "", true);
        getContentPane().setBackground(new Color(228,231,235));
        setTitle("Chuyển Chi Nhánh");
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setIconImage(new FlatSVGIcon("icon/ACB.svg").getImage());
        setSize(new Dimension(650, 350));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(Bank_Application.homeGUI);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });
        this.refresh = refresh;
        this.staff = staff;
        init();
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
        setVisible(true);
    }

    private void init() {
        role_detail_panel = new RoundedPanel();
        titleName = new JLabel();
        attributeRole_detail = new ArrayList<>();
        buttonCancel = new JButton("Huỷ");
        buttonSet = new JButton("Chuyển");
        txtSearch = new MyTextField();

        RoundedPanel title = new RoundedPanel();
        RoundedPanel containerButton = new RoundedPanel();

        title.setLayout(new BorderLayout());
        title.setBackground(new Color(228,231,235));
        title.setPreferredSize(new Dimension(500, 40));
        add(title);

        content.setLayout(new FlowLayout());
        content.setBackground(new Color(255, 255, 255));
        content.setPreferredSize(new Dimension(500, 200));
        add(content);

        containerButton.setLayout(new FlowLayout(FlowLayout.CENTER));
        containerButton.setBackground(new Color(228,231,235));
        containerButton.setPreferredSize(new Dimension(500, 50));
        add(containerButton);

        titleName.setText("Chuyển Chi Nhánh");
        titleName.setFont(new Font("Public Sans", Font.BOLD, 18));
        titleName.setHorizontalAlignment(JLabel.CENTER);
        titleName.setVerticalAlignment(JLabel.CENTER);
        title.add(titleName, BorderLayout.CENTER);


        role_detail_panel.setBackground(new Color(255, 255, 255));
        role_detail_panel.setLayout(new MigLayout("", "0[]50[]0", "20[]20[]20"));
        role_detail_panel.setPreferredSize(new Dimension(450, 200));
        content.add(role_detail_panel);


        for (String string : new String[]{"Nhân Viên", "Chi Nhánh Hiện Tại","Chi Nhánh Mới"}) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(170, 30));
            label.setText(string);
            label.setFont((new Font("Public Sans", Font.BOLD, 16)));
            attributeRole_detail.add(label);
            role_detail_panel.add(label);


            if (string.equals("Nhân Viên")) {
                JLabel jLabel = new JLabel(staff.getName());
                jLabel.setFont((new Font("Public Sans", Font.BOLD, 16)));
                role_detail_panel.add(jLabel, "wrap");
            }

            if (string.equals("Chi Nhánh Hiện Tại")) {
                JLabel jLabel = new JLabel();
                jLabel.setFont((new Font("Public Sans", Font.PLAIN, 14)));

                Branch branch = branchBLL.findAllBranchs("id", String.valueOf(staff.getBranch_id())).get(0);
                jLabel.setText(branch.getName());
                role_detail_panel.add(jLabel, "wrap");
            }

            if (string.equals("Chi Nhánh Mới")) {
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
                role_detail_panel.add(txtSearch, "wrap");
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

        buttonSet.setPreferredSize(new Dimension(100, 30));
        buttonSet.setFont(new Font("Public Sans", Font.BOLD, 15));
        buttonSet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonSet.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ChangeStaffBranch_id();
            }
        });
        containerButton.add(buttonSet);

    }

    private void ChangeStaffBranch_id() {
        if (branch_id == 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn chi nhánh mới.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (branch_id == staff.getBranch_id()) {
            JOptionPane.showMessageDialog(null, "Chi nhánh mới không được trùng chi nhánh cũ.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] options = new String[]{"Huỷ", "Xác nhận"};
        int choice = JOptionPane.showOptionDialog(null, "Xác nhận chuyển chi nhánh cho nhân viên?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            Pair<Boolean, String> result;
            result = new StaffBLL().changeStaffBranch_id(staff, branch_id);
            Circle_ProgressBar circleProgressBar = new Circle_ProgressBar();
            circleProgressBar.getRootPane ().setOpaque (false);
            circleProgressBar.getContentPane ().setBackground (new Color (0, 0, 0, 0));
            circleProgressBar.setBackground (new Color (0, 0, 0, 0));
            circleProgressBar.progress();
            circleProgressBar.setVisible(true);
            JOptionPane.showMessageDialog(null, result.getValue(),
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            refresh.actionPerformed(null);
        }

    }

    public void cancel() {
        String[] options = new String[]{"Huỷ", "Thoát"};
        int choice = JOptionPane.showOptionDialog(null, "Bạn có muốn thoát?",
                "Thông báo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1)
            dispose();
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
