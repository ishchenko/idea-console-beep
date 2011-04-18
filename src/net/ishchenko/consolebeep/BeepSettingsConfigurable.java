package net.ishchenko.consolebeep;

import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.ui.TableUtil;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.ComboBoxTableCellEditor;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 16.04.11
 * Time: 16:56
 */
public class BeepSettingsConfigurable extends BaseConfigurable {

    private Project project;
    private JBTable table;

    public BeepSettingsConfigurable(Project project) {
        this.project = project;
    }

    @Nls
    public String getDisplayName() {
        return "Console Beep Patterns";
    }

    public JComponent createComponent() {

        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonsPanel = new JPanel();
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TableUtil.stopEditing(table);
                int index = table.getSelectionModel().getMinSelectionIndex();
                if (index != -1) {
                    ((BeepSettingsTableModel) table.getModel()).removeRow(index);
                }
            }
        });
        buttonsPanel.add(removeButton);
        panel.add(buttonsPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        table = new JBTable();
        table.setBorder(BorderFactory.createEtchedBorder());
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(table, BorderLayout.CENTER);
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;

    }

    public void apply() throws ConfigurationException {

        BeepSettings settings = new BeepSettings();
        settings.setSettings(((BeepSettingsTableModel) table.getModel()).settings);
        Beeper.getInstance(project).loadState(settings);

    }

    public void reset() {

        table.setModel(new BeepSettingsTableModel(Beeper.getInstance(project).getState().getSettings()));
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox(new String[]{"ding"})));

    }

    @Override
    public boolean isModified() {
        return true;
    }

    public Icon getIcon() {
        return null;
    }

    public String getHelpTopic() {
        return null;
    }

    public void disposeUIResources() {
    }

    private class BeepSettingsTableModel extends AbstractTableModel {

        private String[] columns = {"Enabled", "Pattern", "Sound"};

        private List<BeepSettings.PatternBeep> settings;

        public BeepSettingsTableModel(List<BeepSettings.PatternBeep> settings) {
            this.settings = new ArrayList<BeepSettings.PatternBeep>(settings.size());
            for (BeepSettings.PatternBeep beep : settings) {
                this.settings.add(beep.clone());
            }
        }

        public int getRowCount() {
            return settings.size();
        }

        public int getColumnCount() {
            return 3;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            BeepSettings.PatternBeep row = settings.get(rowIndex);
            if (columnIndex == 0) {
                return row.isEnabled();
            } else if (columnIndex == 1) {
                return row.getPattern();
            } else if (columnIndex == 2) {
                return row.getBeep();
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return rowIndex < settings.size() && columnIndex != 3;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            BeepSettings.PatternBeep row = settings.get(rowIndex);
            if (columnIndex == 0) {
                row.setEnabled((Boolean) aValue);
            } else if (columnIndex == 1) {
                row.setPattern((String) aValue);
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Boolean.class;
            } else if (columnIndex == 1) {
                return String.class;
            }
            return Object.class;
        }

        public void removeRow(int index) {
            settings.remove(index);
            fireTableDataChanged();
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }
    }

}



