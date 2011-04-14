package net.ishchenko.consolebeep;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 14.04.11
 * Time: 2:13
 */
public class BeepOnThatAction extends AnAction {

    @Override
    public void update(AnActionEvent e) {

        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        boolean enabled = editor != null &&
                editor.getSelectionModel().hasSelection() &&
                editor.getSelectionModel().getSelectionStartPosition().line == editor.getSelectionModel().getSelectionEndPosition().line;

        e.getPresentation().setEnabled(enabled);

    }

    @Override
    public void actionPerformed(AnActionEvent e) {

        BeepProjectComponent component = e.getData(PlatformDataKeys.PROJECT).getComponent(BeepProjectComponent.class);
        SelectionModel selectionModel = e.getData(PlatformDataKeys.EDITOR).getSelectionModel();

        if (selectionModel.hasSelection()) {
            component.setPattern(selectionModel.getSelectedText());
        }

    }

}
