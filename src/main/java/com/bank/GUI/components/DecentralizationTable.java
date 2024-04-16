package com.bank.GUI.components;

import com.bank.BLL.DecentralizationBLL;
import com.bank.BLL.FunctionBLL;
import com.bank.BLL.ModuleBLL;
import com.bank.BLL.RoleBLL;
import com.bank.DTO.Decentralization;
import com.bank.DTO.Function;
import com.bank.DTO.Module;
import com.bank.DTO.Role;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecentralizationTable extends JScrollPane {
    protected final JPanel panel;
    protected final RoleBLL roleBLL;
    protected final ModuleBLL moduleBLL;
    protected final FunctionBLL functionBLL;
    protected final DecentralizationBLL decentralizationBLL;
    protected final List<Module> modules;
    protected final List<Function> functions;
    protected final Map<Pair<Integer, Integer>, Pair<Module, Function>> table;
    public final JCheckBox[][] checkboxes;
    protected Role role;

    public DecentralizationTable() {
        setBackground(Color.white);
        setBorder(BorderFactory.createEmptyBorder());
        panel = new JPanel();
        roleBLL = new RoleBLL();
        moduleBLL = new ModuleBLL();
        functionBLL = new FunctionBLL();
        decentralizationBLL = new DecentralizationBLL();
        modules = moduleBLL.searchModules();
        functions = functionBLL.searchFunctions();
        table = new HashMap<>();
        checkboxes = new JCheckBox[modules.size()][functions.size()];

        panel.setLayout(new GridLayout(modules.size() + 1, functions.size() + 1));
        panel.setBackground(Color.white);

        int i, j;
        panel.add(new JLabel());
        for (i = 0; i < functions.size(); i++) {
            String functionName = functions.get(i).getName();
            panel.add(new JLabel(functionName));
        }
        for (i = 0; i < modules.size(); i++) {
            String moduleName = modules.get(i).getName();
            panel.add(new JLabel(moduleName));
            for (j = 0; j < functions.size(); j++) {
                checkboxes[i][j] = new JCheckBox();
                checkboxes[i][j].setVisible(false);
                checkboxes[i][j].setEnabled(false);
                panel.add(checkboxes[i][j]);
                table.put(new Pair<>(i, j), new Pair<>(modules.get(i), functions.get(j)));
            }
        }
        Role role = roleBLL.searchRoles("id = 0").get(0);
        List<Decentralization> decentralizations = decentralizationBLL.searchDecentralizations("[role_id] = " + role.getId());
        for (Decentralization decentralization : decentralizations) {
            int moduleId = decentralization.getModule_id();
            int functionId = decentralization.getFunction_id();
            checkboxes[moduleId - 1][functionId - 1].setVisible(true);
        }
        handleCheckBoxes();

        this.setViewportView(panel);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
        List<Decentralization> decentralizations = decentralizationBLL.searchDecentralizations("[role_id] = " + role.getId());
        for (Decentralization decentralization : decentralizations) {
            int moduleId = decentralization.getModule_id();
            int functionId = decentralization.getFunction_id();
            checkboxes[moduleId - 1][functionId - 1].setSelected(true);
        }
        for (int i = 0; i < modules.size(); i++) {
            boolean isSelected = checkboxes[i][0].isSelected();
            checkboxes[i][0].setEnabled(true);
            for (int j = 1; j < functions.size(); j++) {
                if (isSelected)
                    checkboxes[i][j].setEnabled(true);
            }
        }
        if (role.getId() == 1) {
            checkboxes[6][0].setEnabled(false);
            checkboxes[6][1].setEnabled(false);
            checkboxes[9][0].setEnabled(false);
            checkboxes[9][1].setEnabled(false);
            checkboxes[9][2].setEnabled(false);
            checkboxes[9][3].setEnabled(false);
        }

    }

    public void refreshTable() {
        for (JCheckBox[] row : checkboxes) {
            for (JCheckBox checkBox : row) {
                checkBox.setSelected(false);
                checkBox.setEnabled(false);
            }
        }
    }

    public void handleCheckBoxes() {
        for (int i = 0; i < modules.size(); i++) {
            int rowIndex = i;
            checkboxes[i][0].addActionListener(e -> {
                boolean isSelected = checkboxes[rowIndex][0].isSelected();
                for (int j = 1; j < functions.size(); j++)
                    checkboxes[rowIndex][j].setEnabled(isSelected);
            });
            for (int j = 0; j < functions.size(); j++) {
                int colIndex = j;
                checkboxes[i][j].addActionListener(e -> {
                    Pair<Module, Function> ids = table.get(new Pair<>(rowIndex, colIndex));
                    Decentralization decentralization = new Decentralization(role.getId(), ids.getKey().getId(), ids.getValue().getId());
                    if (checkboxes[rowIndex][colIndex].isSelected())
                        decentralizationBLL.addDecentralization(decentralization);
                    else
                        decentralizationBLL.deleteDecentralization(decentralization);
                });
            }
        }
    }
}
