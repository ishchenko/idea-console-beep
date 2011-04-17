package net.ishchenko.consolebeep;

import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import java.awt.*;
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
        return panel = new JPanel();
    }

    public void apply() throws ConfigurationException {

        BeepSettings settings = new BeepSettings();
        List<BeepSettings.PatternBeep> beeps = new ArrayList<BeepSettings.PatternBeep>();
        for (PatterBeepControls field : fields) {
            beeps.add(new BeepSettings.PatternBeep(field.patternField.getText(), (String) field.beepType.getModel().getSelectedItem(), field.enabledCheckbox.isSelected()));
        }
        settings.setSettings(beeps);

        Beeper.getInstance(project).loadState(settings);

    }

    public void reset() {

        panel.removeAll();
        fields = new ArrayList<PatterBeepControls>();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (BeepSettings.PatternBeep beep : Beeper.getInstance(project).getState().getSettings()) {
            PatterBeepControls controls = new PatterBeepControls(
                    new JTextField(beep.getPattern(), 15),
                    new JComboBox(new Object[]{beep.getBeep()}),
                    new JCheckBox((String) null, beep.isEnabled())
            );
            fields.add(controls);
            JPanel line = new JPanel();
            line.add(controls.patternField);
            line.add(controls.beepType);
            line.add(controls.enabledCheckbox);
            panel.add(line);
            panel.add(Box.createHorizontalGlue());
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
        JComboBox beepType;
        JCheckBox enabledCheckbox;

        private PatterBeepControls(JTextField patternField, JComboBox beepType, JCheckBox enabledCheckbox) {
            this.patternField = patternField;
            this.beepType = beepType;
            this.enabledCheckbox = enabledCheckbox;
        }
    }
}



