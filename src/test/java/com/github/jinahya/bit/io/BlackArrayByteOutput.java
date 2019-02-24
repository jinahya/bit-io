package com.github.jinahya.bit.io;

import java.io.IOException;

/**
 * A class of {@link ArrayByteOutput} which discards bytes.
 *
 * @see WhiteArrayByteInput
 */
class BlackArrayByteOutput extends ArrayByteOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    BlackArrayByteOutput() {
        super(null, -1, -1);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(int value) throws IOException {
        if (target == null) {
            target = new byte[1];
            index = 0;
            limit = target.length;
        }
        super.write(value);
        if (index == limit) {
            index = 0;
        }
    }
}
