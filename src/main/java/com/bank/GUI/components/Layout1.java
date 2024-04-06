package com.bank.GUI.components;

import java.awt.*;

public class Layout1 extends RoundedPanel {
    public RoundedPanel top;
    public RoundedPanel bottom;
    public RoundedPanel SearchPanel;
    public RoundedPanel FunctionPanel;

    public Layout1() {
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setBackground(new Color(228,231,235));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1165, 733));

        top = new RoundedPanel();
        bottom = new RoundedPanel();
        SearchPanel = new RoundedPanel();
        FunctionPanel = new RoundedPanel();

        top.setLayout(new BorderLayout());
        top.setPreferredSize(new Dimension(1165, 50));
        top.setBackground(new Color(228,231,235));
        add(top, BorderLayout.NORTH);

        bottom.setLayout(new BorderLayout());
        bottom.setPreferredSize(new Dimension(1165, 680));
        bottom.setBackground(Color.white);
        add(bottom, BorderLayout.SOUTH);

        SearchPanel.setLayout(new GridBagLayout());
        SearchPanel.setBackground(new Color(228,231,235));
        SearchPanel.setPreferredSize(new Dimension(530, 50));
        top.add(SearchPanel, BorderLayout.WEST);

        FunctionPanel.setLayout(new FlowLayout());
        FunctionPanel.setBackground(new Color(228,231,235));
        FunctionPanel.setPreferredSize(new Dimension(620, 50));
        top.add(FunctionPanel, BorderLayout.EAST);

    }
}
