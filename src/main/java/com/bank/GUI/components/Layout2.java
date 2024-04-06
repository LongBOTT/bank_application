package com.bank.GUI.components;

import java.awt.*;

public class Layout2 extends RoundedPanel {
    public RoundedPanel top;
    public RoundedPanel bottom;
    public RoundedPanel containerSearchPanel;
    public RoundedPanel FunctionPanel;
    public RoundedPanel FilterDatePanel;
    public RoundedPanel SearchPanel;

    public Layout2() {
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setBackground(new Color(228,231,235));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1165, 733));

        top = new RoundedPanel();
        bottom = new RoundedPanel();
        containerSearchPanel = new RoundedPanel();
        SearchPanel = new RoundedPanel();
        FilterDatePanel = new RoundedPanel();
        FunctionPanel = new RoundedPanel();

        top.setLayout(new BorderLayout());
        top.setPreferredSize(new Dimension(1165, 100));
        top.setBackground(new Color(228,231,235));
        add(top, BorderLayout.NORTH);

        bottom.setLayout(new BorderLayout());
        bottom.setPreferredSize(new Dimension(1165, 630));
        bottom.setBackground(Color.white);
        add(bottom, BorderLayout.SOUTH);

        containerSearchPanel.setLayout(new FlowLayout());
        containerSearchPanel.setBackground(Color.white);
        containerSearchPanel.setPreferredSize(new Dimension(800, 100));
        top.add(containerSearchPanel, BorderLayout.WEST);

        FunctionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        FunctionPanel.setBackground(Color.white);
        FunctionPanel.setPreferredSize(new Dimension(300, 50));
        top.add(FunctionPanel, BorderLayout.EAST);

        FilterDatePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        FilterDatePanel.setBackground(new Color(228,231,235));
        FilterDatePanel.setPreferredSize(new Dimension(800, 40));
        containerSearchPanel.add(FilterDatePanel);

        SearchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        SearchPanel.setBackground(new Color(228,231,235));
        SearchPanel.setPreferredSize(new Dimension(800, 50));
        containerSearchPanel.add(SearchPanel);
    }
}
