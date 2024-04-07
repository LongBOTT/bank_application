/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.GUI.components.swing;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author RAVEN
 */
public class PanelSearch extends JPanel {

    private EventClick event;
    private int selectedIndex = -1;

    public void addEventClick(EventClick event) {
        this.event = event;
    }

    public PanelSearch() {
        initComponents();
        setAutoscrolls(true);
        setLayout(new MigLayout("", "0[]0", "0[]0"));
    }

    public void setData(List<DataSearch> data) {
        selectedIndex = -1; //  -1 is not selected
        this.removeAll();
        for (DataSearch d : data) {
            Search_Item item = new Search_Item(d);
            //  add event
            item.addEvent(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    //  when click
                    event.itemClick(d);

                }
            });
            this.add(item, "wrap");
        }
        repaint();
        revalidate();
    }
    public void setData1(List<DataSearch> data) {
        selectedIndex = -1; //  -1 is not selected
        this.removeAll();
        for (DataSearch d : data) {
            Search_Item item = new Search_Item(d,"");
            item.addEvent(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    //  when click
                    event.itemClick(d);

                }
            });
            this.add(item, "wrap");
        }
        repaint();
        revalidate();
    }
    public int getItemSize() {
        return getComponentCount();
    }

    public void keyUp() {
        int size = getComponentCount();
        if (size > 0) {
            if (selectedIndex <= 0) {
                selectedIndex = size - 1;
            } else {
                selectedIndex--;
            }
            showSelected();
        }
    }

    public void keyDown() {
        int size = getComponentCount();
        if (size > 0) {
            if (selectedIndex >= size - 1) {
                selectedIndex = 0;
            } else {
                selectedIndex++;
            }
            showSelected();
        }
    }

    public String getSelectedText() {
        if (selectedIndex != -1 && selectedIndex < getComponentCount()) {
            return ((Search_Item) getComponent(selectedIndex)).getText();
        }
        return "";
    }

    public void clearSelected() {
        selectedIndex = -1;
        showSelected();
    }

    private void showSelected() {
        Component com[] = getComponents();
        for (int i = 0; i < com.length; i++) {
            ((Search_Item) com[i]).setSelected(i == selectedIndex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new Color(255, 255, 255));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 438, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
