package net.ishchenko.consolebeep;

import com.intellij.openapi.application.ApplicationManager;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 14.04.11
 * Time: 4:14
 */
public class Beeper {

    public void beep(String pattern) {

        Runnable beepRunnable = new Runnable() {
            public void run() {
                doBeep("/ding.wav");
            }
        };
        ApplicationManager.getApplication().executeOnPooledThread(beepRunnable);

    }

    private synchronized void doBeep(String filename) {

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(Beeper.class.getResourceAsStream(filename));
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
