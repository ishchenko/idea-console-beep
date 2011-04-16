package net.ishchenko.consolebeep;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 16.04.11
 * Time: 5:17
 */
public class BeepSettings {

    private Map<String, String> patternSounds = new HashMap<String, String>();

    public synchronized void addPatternSound(String pattern, String soundId) {
        patternSounds.put(pattern, soundId);
    }

    public synchronized String getPatternSound(String pattern) {
        return patternSounds.get(pattern);
    }

    public Map<String, String> getPatternSounds() {
        return patternSounds;
    }

    public void setPatternSounds(Map<String, String> patternSounds) {
        this.patternSounds = patternSounds;
    }
}
