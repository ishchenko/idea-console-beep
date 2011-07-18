package net.ishchenko.consolebeep;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.Constraints;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.util.io.StreamUtil;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 18.07.11
 * Time: 2:20
 */
public class BeeperApplicationComponent implements ApplicationComponent {

    private static final String SOUND_FILE_EXTENSION = ".wav";

    private final Object beepLock = new Object();
    private final Map<String, BeepSound> sounds = new ConcurrentSkipListMap<String, BeepSound>();

    public void initComponent() {

        Map<String, BeepSound> sounds = new HashMap<String, BeepSound>();

        try {

            File root = new File(getClass().getResource("/").toURI());
            File[] soundFiles = root.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(SOUND_FILE_EXTENSION);
                }
            });

            for (File soundFile : soundFiles) {

                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(soundFile);
                    sounds.put(soundFile.getName().substring(0, soundFile.getName().lastIndexOf(SOUND_FILE_EXTENSION)), new BeepSound(StreamUtil.loadFromStream(fis)));
                } finally {
                    if (fis != null) fis.close();
                }
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.sounds.putAll(sounds);

        DefaultActionGroup actionGroup = (DefaultActionGroup) ActionManager.getInstance().getAction("ConsoleBeep.BeepActionGroup");
        for (Map.Entry<String, BeepSound> entry : sounds.entrySet()) {
            actionGroup.add(new BeepOnThatAction(entry.getKey()), Constraints.FIRST);
        }

    }

    public boolean tryBeep(String beepId) {
        BeepSound beepSound = sounds.get(beepId);
        if (beepSound != null) {
            doBeep(beepSound);
            return true;
        } else {
            return false;
        }
    }

    public void disposeComponent() {

    }

    @NotNull
    public String getComponentName() {
        return "Beeper Application Component";
    }

    private void doBeep(final BeepSound beepSound) {

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

    public String[] getSoundKeys() {
        return sounds.keySet().toArray(new String[sounds.size()]);
    }


}
