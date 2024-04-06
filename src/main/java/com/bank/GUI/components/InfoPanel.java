package com.bank.GUI.components;

import net.miginfocom.swing.MigLayout;

import java.awt.*;

public class InfoPanel extends RoundedPanel {
    public RoundedPanel TitleInfoAccount;
    public RoundedPanel InfoAccountPanel;
    public RoundedPanel TitleInfoStaff;
    public RoundedPanel InfoStaffPanel;

    public InfoPanel() {
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setBackground(new Color(217, 217, 217));
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setPreferredSize(new Dimension(1165, 733));

        TitleInfoAccount = new RoundedPanel();
        InfoAccountPanel = new RoundedPanel();
        TitleInfoStaff = new RoundedPanel();
        InfoStaffPanel = new RoundedPanel();

        TitleInfoAccount.setLayout(new BorderLayout());
        TitleInfoAccount.setPreferredSize(new Dimension(1160, 50));
        TitleInfoAccount.setBackground(new Color(232, 206, 180));
        add(TitleInfoAccount);

        InfoAccountPanel.setLayout(new MigLayout("", "150[]90[]10[]150"));
        InfoAccountPanel.setPreferredSize(new Dimension(1160, 120));
        InfoAccountPanel.setBackground(new Color(217, 217, 217));
        add(InfoAccountPanel);

        TitleInfoStaff.setLayout(new BorderLayout());
        TitleInfoStaff.setPreferredSize(new Dimension(1160, 50));
        TitleInfoStaff.setBackground(new Color(232, 206, 180));
        add(TitleInfoStaff);

        InfoStaffPanel.setLayout(new MigLayout("", "150[]50[]10[]150", "20[]20"));
        InfoStaffPanel.setPreferredSize(new Dimension(1160, 493));
        InfoStaffPanel.setBackground(new Color(217, 217, 217));
        add(InfoStaffPanel);
    }
}
