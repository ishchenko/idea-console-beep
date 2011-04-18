package net.ishchenko.consolebeep;

import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    private JComponent panel;
    private List<PatterBeepControls> fields;

    public BeepSettingsConfigurable(Project project) {
        this.project = project;
    }

    @Nls
    public String getDisplayName() {
        return "Console Beep Patterns";
    }

    public JComponent createComponent() {
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setLayout(new BorderLayout());
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        return panel;
    }

    public void apply() throws ConfigurationException {

        BeepSettings settings = new BeepSettings();
        List<BeepSettings.PatternBeep> beeps = new ArrayList<BeepSettings.PatternBeep>();
        for (PatterBeepControls field : fields) {
            beeps.add(new BeepSettings.PatternBeep(field.patternField.getText(), (String) field.beepTypeCombo.getModel().getSelectedItem(), field.enabledCheckbox.isSelected()));
        }
        settings.setSettings(beeps);

        Beeper.getInstance(project).loadState(settings);

    }

    public void reset() {

        panel.removeAll();

        List<BeepSettings.PatternBeep> settings = Beeper.getInstance(project).getState().getSettings();
        fields = new ArrayList<PatterBeepControls>();

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        panel.add(p, BorderLayout.NORTH);

        for (BeepSettings.PatternBeep beep : settings) {

            JPanel rowPanel = new JPanel();
            final PatterBeepControls controls = new PatterBeepControls(beep, rowPanel);
            fields.add(controls);

            rowPanel.add(controls.enabledCheckbox);
            rowPanel.add(controls.patternField);
            rowPanel.add(controls.beepTypeCombo);
            rowPanel.add(controls.removeButton);
            p.add(rowPanel);

        }


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

    private class PatterBeepControls {

        JTextField patternField;
        JComboBox beepTypeCombo;
        JCheckBox enabledCheckbox;
        JButton removeButton;
        private JPanel rowPanel;

        public PatterBeepControls(BeepSettings.PatternBeep beep, final JPanel rowPanel) {

            patternField = new JTextField(beep.getPattern(), 15);
            patternField.setEnabled(beep.isEnabled());

            beepTypeCombo = new JComboBox(new Object[]{beep.getBeep()});
            Dimension dimension = beepTypeCombo.getPreferredSize();
            dimension.width = 100;
            beepTypeCombo.setPreferredSize(dimension);
            beepTypeCombo.setEnabled(beep.isEnabled());

            enabledCheckbox = new JCheckBox((String) null, beep.isEnabled());
            enabledCheckbox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    patternField.setEnabled(enabledCheckbox.isSelected());
                    beepTypeCombo.setEnabled(enabledCheckbox.isSelected());
                }
            });

            removeButton = new JButton("Remove");
            removeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fields.remove(PatterBeepControls.this);
                    rowPanel.remove(patternField);
                    rowPanel.remove(beepTypeCombo);
                    rowPanel.remove(enabledCheckbox);
                    rowPanel.remove(removeButton);
                    rowPanel.repaint();
                }
            });

        }
    }
}



