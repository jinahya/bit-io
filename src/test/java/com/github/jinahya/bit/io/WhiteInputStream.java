package com.github.jinahya.bit.io;

import java.io.IOException;
import java.io.InputStream;

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * A class of {@link InputStream} whose {@link InputStream#read()} returns a random value.
 */
class WhiteInputStream extends InputStream {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a random value between {@code 0} and {@code 255}, both inclusive.
     *
     * @return a random byte value.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public int read() throws IOException {
        return current().nextInt(0, 256);
    }
}
