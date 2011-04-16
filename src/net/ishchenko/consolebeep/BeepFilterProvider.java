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
        return Beeper.getInstance(project).getFilters();
    }

}
