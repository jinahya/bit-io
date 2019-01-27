package com.github.jinahya.bit.io;

import java.io.IOException;

class BlackArrayByteOutput extends ArrayByteOutput {

    // -----------------------------------------------------------------------------------------------------------------
    private static final int LENGTH = 1;

    // -----------------------------------------------------------------------------------------------------------------
    BlackArrayByteOutput() {
        super(null, -1, -1);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(int value) throws IOException {
        if (target == null) {
            target = new byte[LENGTH];
            limit = target.length;
            index = limit;
        }
        if (index == limit) {
            setIndex(0);
        }
        super.write(value);
    }
}
