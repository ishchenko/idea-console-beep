package net.ishchenko.consolebeep;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 16.04.11
 * Time: 5:29
 */
public class ViewBeepSettingsAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        ShowSettingsUtil.getInstance().editConfigurable(project, new BeepSettingsConfigurable(project));
    }

}

