package com.github.jinahya.bit.io;

import java.io.IOException;

class WhiteArrayByteInput extends ArrayByteInput {

    // -----------------------------------------------------------------------------------------------------------------
    private static final int LENGTH = 1;

    // -----------------------------------------------------------------------------------------------------------------
    WhiteArrayByteInput() {
        super(null, -1, -1);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int read() throws IOException {
        if (source == null) {
            source = new byte[LENGTH];
            limit = source.length;
            index = limit;
        }
        if (index == limit) {
            index(0);
        }
        return super.read();
    }
}
