package com.github.jinahya.bit.io;

import java.io.IOException;
import java.io.InputStream;

import static java.util.concurrent.ThreadLocalRandom.current;

class WhiteInputStream extends InputStream {

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int read() throws IOException {
        return current().nextInt(0, 256);
    }
}
