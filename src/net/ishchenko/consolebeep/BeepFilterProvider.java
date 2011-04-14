package net.ishchenko.consolebeep;

import com.intellij.execution.filters.ConsoleFilterProvider;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 14.04.11
 * Time: 4:09
 */
public class BeepFilterProvider implements ConsoleFilterProvider {

    @NotNull
    public Filter[] getDefaultFilters(@NotNull Project project) {

        BeepProjectComponent component = project.getComponent(BeepProjectComponent.class);

        return new Filter[]{new BeepFilter(component.getPattern(), component.getBeeper())};

    }

    public static class BeepFilter implements Filter {

        private final String pattern;
        private final Beeper beeper;

        public BeepFilter(String pattern, Beeper beeper) {
            this.pattern = pattern;
            this.beeper = beeper;
        }

        public Result applyFilter(String line, int entireLength) {
            if (pattern != null && line.contains(pattern)) {
                beeper.beep(pattern);
            }
            return null;
        }

    }

}
