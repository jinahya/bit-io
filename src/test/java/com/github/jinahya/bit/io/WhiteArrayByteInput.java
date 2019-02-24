package com.github.jinahya.bit.io;

import java.io.IOException;

/**
 * A class of {@link ArrayByteInput} which never reaches to an end-of-stream.
 *
 * @see BlackArrayByteOutput
 */
final class WhiteArrayByteInput extends ArrayByteInput {

    // -----------------------------------------------------------------------------------------------------------------
    WhiteArrayByteInput() {
        super(null, -1, -1);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int read() throws IOException {
        if (source == null) {
            source = new byte[1];
            index = source.length;
            limit = source.length;
        }
        if (index == limit) {
            index = 0;
        }
        return super.read();
    }
}
