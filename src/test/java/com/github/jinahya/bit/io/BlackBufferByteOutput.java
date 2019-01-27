package com.github.jinahya.bit.io;

import java.io.IOException;
import java.nio.ByteBuffer;

class BlackBufferByteOutput extends BufferByteOutput<ByteBuffer> {

    // -----------------------------------------------------------------------------------------------------------------
    BlackBufferByteOutput() {
        super(null);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(final int value) throws IOException {
        if (target == null) {
            target = ByteBuffer.allocate(1); // position: zero, limit: capacity
        }
        super.write(value);
        if (!target.hasRemaining()) {
            target.position(0);
        }
    }
}
