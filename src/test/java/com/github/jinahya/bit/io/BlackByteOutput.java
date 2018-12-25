package com.github.jinahya.bit.io;

import java.io.IOException;

class BlackByteOutput implements ByteOutput {

    // -----------------------------------------------------------------------------------------------------------------
    private static class InstanceHolder {

        private static final ByteOutput INSTANCE = new BlackByteOutput();

        private InstanceHolder() {
            super();
        }
    }

    static ByteOutput getInstance() {
        return InstanceHolder.INSTANCE;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BlackByteOutput() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(final int value) throws IOException {
        // empty;
    }
}
