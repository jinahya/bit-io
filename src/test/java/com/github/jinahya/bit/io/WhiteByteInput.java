package com.github.jinahya.bit.io;

import java.io.IOException;

import static java.util.concurrent.ThreadLocalRandom.current;

class WhiteByteInput implements ByteInput {

    // -----------------------------------------------------------------------------------------------------------------
    private static class InstanceHolder {

        private static final ByteInput INSTANCE = new WhiteByteInput();

        private InstanceHolder() {
            super();
        }
    }

    static ByteInput getInstance() {
        return InstanceHolder.INSTANCE;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private WhiteByteInput() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int read() throws IOException {
        return current().nextInt(256);
    }
}
