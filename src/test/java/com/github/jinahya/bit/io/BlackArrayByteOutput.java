package com.github.jinahya.bit.io;

import java.io.IOException;

class BlackArrayByteOutput extends ArrayByteOutput {

    // -----------------------------------------------------------------------------------------------------------------
    BlackArrayByteOutput() {
        super(null, -1, -1);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(int value) throws IOException {
        if (target == null) {
            target(new byte[1024]).index(target.length).limit(target.length);
        }
        if (index == limit) {
            setIndex(0);
        }
        super.write(value);
    }
}
