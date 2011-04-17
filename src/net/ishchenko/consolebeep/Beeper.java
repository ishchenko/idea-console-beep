package net.ishchenko.consolebeep;

import com.intellij.execution.filters.Filter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
public class Beeper implements PersistentStateComponent<BeepSettings> {

    private static final String DEFAULT_BEEP_ID = "ding";

    private final Object beepLock = new Object();
    private Map<String, BeepSound> sounds;
    private BeepSettings state = new BeepSettings();

    private Filter[] filters = new Filter[]{
            new Filter() {
                public Result applyFilter(String line, int entireLength) {
                    tryLine(line);
                    return null;
                }
            }
    };

    public Beeper() throws IOException {
        sounds = new HashMap<String, BeepSound>();
        sounds.put(DEFAULT_BEEP_ID, new BeepSound("/ding.wav"));
    }

    public void addDefaultBeep(String pattern) {
        state.addPatternSound(pattern, DEFAULT_BEEP_ID);
    }

    private void tryLine(String line) {

        for (BeepSettings.PatternBeep pattern : state.getSettings()) {
            if (line.contains(pattern.getPattern())) {
                BeepSound beepSound = sounds.get(pattern.getBeep());
                if (beepSound != null && pattern.isEnabled()) {
                    doBeep(beepSound);
                    break; //beep once per line
                }
            }
        }

    }

    private synchronized void doBeep(final BeepSound beepSound) {

        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            public void run() {
                synchronized (beepLock) {

                    try {
                        AudioInputStream ais = AudioSystem.getAudioInputStream(new ByteArrayInputStream(beepSound.getBytes()));
                        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, ais.getFormat()));
                        try {
                            line.open(ais.getFormat());
                            line.start();
                            int nBytesRead = 0;
                            byte[] abData = new byte[16384];
                            while (nBytesRead != -1) {
                                nBytesRead = ais.read(abData, 0, abData.length);
                                if (nBytesRead >= 0) {
                                    line.write(abData, 0, nBytesRead);
                                }
                            }
                        } finally {
                            line.drain();
                            line.close();
                            ais.close();
                        }
                    } catch (Exception e) {
                        CheckedExceptionsTamer.<RuntimeException>tame(e);
                    }
                }

            }
        });

    }

    public Filter[] getFilters() {
        return filters;
    }

    public BeepSettings getState() {
        return this.state;
    }

    public void loadState(BeepSettings state) {
        XmlSerializerUtil.copyBean(state, this.state);
    }

    public static Beeper getInstance(Project project) {
        return ServiceManager.getService(project, Beeper.class);
    }

}
