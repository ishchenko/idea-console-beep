package net.ishchenko.consolebeep;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 14.04.11
 * Time: 3:37
 */
public class BeepProjectComponent extends AbstractProjectComponent {

    private String pattern;
    private Beeper beeper;

    public BeepProjectComponent(Project project) {
        super(project);
        this.beeper = new Beeper();
    }

    public void setPattern(String selectedText) {
        this.pattern = selectedText;
    }

    public String getPattern() {
        return pattern;
    }

    public Beeper getBeeper() {
        return beeper;
    }
}
