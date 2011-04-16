package net.ishchenko.consolebeep;

import com.intellij.openapi.util.io.StreamUtil;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 15.04.11
 * Time: 19:10
 */
public class BeepSound {

    private final byte[] bytes;

    public BeepSound(String filename) throws IOException {
        bytes = StreamUtil.loadFromStream(getClass().getResourceAsStream(filename));
    }

    public byte[] getBytes() {
        return bytes;
    }

}
