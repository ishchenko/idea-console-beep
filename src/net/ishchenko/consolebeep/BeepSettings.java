package net.ishchenko.consolebeep;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 16.04.11
 * Time: 5:17
 */
public class BeepSettings {

    private volatile List<PatternBeep> settings = new ArrayList<PatternBeep>();

    public synchronized void addPatternSound(String pattern, String soundId) {
        PatternBeep candidate = new PatternBeep(pattern, soundId, true);
        if (!settings.contains(candidate)) {
            settings.add(candidate);
        }
    }

    public List<PatternBeep> getSettings() {
        return settings;
    }

    public void setSettings(List<PatternBeep> settings) {
        this.settings = settings;
    }

    public static class PatternBeep {

        private String pattern;
        private String beep;
        private boolean enabled = true;

        public PatternBeep() {
        }

        public PatternBeep(String pattern, String beep, boolean enabled) {
            this.beep = beep;
            this.pattern = pattern;
            this.enabled = enabled;
        }

        public String getBeep() {
            return beep;
        }

        public void setBeep(String beep) {
            this.beep = beep;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PatternBeep that = (PatternBeep) o;

            if (pattern != null ? !pattern.equals(that.pattern) : that.pattern != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return pattern != null ? pattern.hashCode() : 0;
        }
    }

}
