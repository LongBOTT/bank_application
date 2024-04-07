/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.GUI.components.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author RAVEN
 */
public class Search_Item extends JPanel {
    private JLabel lbIcon;
    private JLabel lblPrice;
    private JLabel lbText;
    public Search_Item(DataSearch data) {
        initComponents();
        setData(data);
    }
    public Search_Item(DataSearch data,String a) {
        initComponents1();
        setData1(data);
    }

    private void setData(DataSearch data) {
        addEventMouse(this);
        addEventMouse(lbText);
        lbText.setText(data.getText());
    }
    private void setData1(DataSearch data) {
        addEventMouse(this);
        addEventMouse(lbText);
        lbText.setText(data.getText() + " (" + data.getText1() + ") ");
        lblPrice.setText("Giá bán: " + data.getPrice());
    }

    private void addEventMouse(Component com) {
        com.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                setBackground(new Color(215, 216, 216));
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setBackground(Color.WHITE);
            }

        });
    }

    private ActionListener eventClick;

    public void addEvent(ActionListener eventClick) {
        this.eventClick = eventClick;
    }

    private void initComponents() {
        setLayout(new FlowLayout());
        lbIcon = new JLabel();
        lbText = new JLabel();

        setBackground(new Color(255, 255, 255));

        lbIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lbIcon.setIcon(new ImageIcon(getClass().getResource("/icon/search_small.png")));
        lbText.setFont(new Font("sansserif", 0, 14));
        lbText.setForeground(new Color(38, 38, 38));

        lbText.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                lbTextMouseClicked(evt);
            }
        });

        add(lbIcon);
        add(lbText);

    }
    private void initComponents1() {
        lbIcon = new JLabel();
        lbText = new JLabel();

        setBackground(new Color(255, 255, 255));

        lbIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lbIcon.setIcon(new ImageIcon(getClass().getResource("/icon/search_small.png")));
        lbText.setFont(new Font("sansserif", 0, 14));
        lbText.setForeground(new Color(38, 38, 38));

        lbText.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                lbTextMouseClicked(evt);
            }
        });
        lblPrice = new JLabel();
        lblPrice.setPreferredSize(new Dimension(100,20));
        lblPrice.setFont(new Font("sansserif", 0, 14));
        lblPrice.setForeground(new Color(38, 38, 38));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(lbIcon, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbText, GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPrice, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lbIcon, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbText, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addComponent(lblPrice, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }
    private void lbTextMouseClicked(MouseEvent evt) {//GEN-FIRST:event_lbTextMouseClicked
        eventClick.actionPerformed(null);
    }

    public String getText() {
        return lbText.getText();
    }

    public void setSelected(boolean act) {
        if (act) {
            setBackground(new Color(215, 216, 216));
        } else {
            setBackground(Color.WHITE);
        }
    }
}
