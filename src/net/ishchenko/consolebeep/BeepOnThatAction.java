package net.ishchenko.consolebeep;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 14.04.11
 * Time: 2:13
 */
public class BeepOnThatAction extends AnAction {

    public BeepOnThatAction(String name) {
        super(name);
    }

    @Override
    public void update(AnActionEvent e) {

        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        boolean enabled = editor != null &&
                editor.getSelectionModel().hasSelection() &&
                editor.getSelectionModel().getSelectedText().length() > 1 &&
                editor.getSelectionModel().getSelectionStartPosition().line == editor.getSelectionModel().getSelectionEndPosition().line;

        e.getPresentation().setEnabled(enabled);

    }

    @Override
    public void actionPerformed(AnActionEvent e) {

        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor != null) {

            SelectionModel selectionModel = editor.getSelectionModel();

            if (selectionModel.hasSelection()) {
                Project project = e.getData(PlatformDataKeys.PROJECT);
                String soundId = getTemplatePresentation().getText();
                ServiceManager.getService(project, BeeperProjectComponent.class).getState().addPatternSound(selectionModel.getSelectedText(), soundId);
            }

        }

    }

}
