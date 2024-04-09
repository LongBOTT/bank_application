package com.bank.GUI.components;

import com.bank.GUI.components.swing.LiquidProgress;
import com.bank.main.Bank_Application;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatProgressBarUI;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.lang.Thread.sleep;

public class Circle_ProgressBar extends JDialog {
    private LiquidProgress liquidProgress;
    public Circle_ProgressBar() {
        super((Frame) null, "", true);
        setSize(new Dimension(200, 200));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(Bank_Application.homeGUI);
        setUndecorated(true);
        initComponents();
    }

    private void initComponents() {
        liquidProgress = new LiquidProgress();
        liquidProgress.setBackground(new java.awt.Color(153, 219, 255));
        liquidProgress.setForeground(new java.awt.Color(42, 161, 233));
        liquidProgress.setValue(0);
        liquidProgress.setAnimateColor(new java.awt.Color(255, 255, 255));
        liquidProgress.setBorderColor(new java.awt.Color(42, 161, 233));
        liquidProgress.setBorderSize(8);
        liquidProgress.setSpaceSize(10);
        add(liquidProgress);
    }

    public void progress() {
        try {
            Thread threadProgress = new Thread(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    while (i <= 100) {
                        i++;
                        liquidProgress.setValue(i);
                        try {
                            sleep(20);
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    dispose();
                }
            });
            threadProgress.start();
        } catch (Exception ignored) {

        }

    }
}
