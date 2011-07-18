package net.ishchenko.consolebeep;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 15.04.11
 * Time: 19:10
 */
public class BeepSound {

    private final byte[] bytes;

    public BeepSound(byte[] bytes) throws IOException {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

}
