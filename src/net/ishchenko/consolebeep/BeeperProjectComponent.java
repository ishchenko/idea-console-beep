package net.ishchenko.consolebeep;

import com.intellij.execution.filters.ConsoleFilterProvider;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 15.04.11
 * Time: 19:24
 */

@State(
        name = "consoleBeeps",
        storages = {
                @Storage(id = "default", file = "$PROJECT_FILE$"),
                @Storage(id = "dir", file = "$PROJECT_CONFIG_DIR$/consoleBeeps.xml", scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public class BeeperProjectComponent implements PersistentStateComponent<BeepSettings> {

    private final BeeperApplicationComponent beeperApplicationComponent;

    private final BeepSettings state = new BeepSettings();

    private final Filter[] filters = new Filter[]{
            new Filter() {
                public Result applyFilter(String line, int entireLength) {
                    tryLine(line);
                    return null;
                }
            }
    };

    public BeeperProjectComponent(BeeperApplicationComponent beeperApplicationComponent) {
        this.beeperApplicationComponent = beeperApplicationComponent;
    }

    public BeepSettings getState() {
        return this.state;
    }

    public void loadState(BeepSettings state) {
        XmlSerializerUtil.copyBean(state, this.state);
    }

    public Filter[] getFilters() {
        return filters;
    }

    private void tryLine(String line) {

        for (BeepSettings.PatternBeep pattern : state.getSettings()) {
            if (line.contains(pattern.getPattern()) && pattern.isEnabled()) {
                if (beeperApplicationComponent.tryBeep(pattern.getBeep())) {
                    break; //beep no more than once per line
                }
            }
        }

    }

    public static class BeepFilterProvider implements ConsoleFilterProvider {

        @NotNull
        public Filter[] getDefaultFilters(@NotNull Project project) {
            return ServiceManager.getService(project, BeeperProjectComponent.class).getFilters();
        }

    }
}
