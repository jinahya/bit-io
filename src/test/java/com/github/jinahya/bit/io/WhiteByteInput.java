package com.github.jinahya.bit.io;

import java.io.IOException;

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * A byte input whose {@link ByteInput#read()} method returns a random value.
 */
class WhiteByteInput implements ByteInput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public WhiteByteInput() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int read() throws IOException {
        return current().nextInt(0, 256);
    }
}
