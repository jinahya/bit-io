package com.github.jinahya.bit.io;

import java.io.IOException;
import java.nio.ByteBuffer;

import static java.util.concurrent.ThreadLocalRandom.current;

class WhiteBufferByteInput extends BufferByteInput<ByteBuffer> {

    // -----------------------------------------------------------------------------------------------------------------
    private static final int CAPACITY = 1;

    // -----------------------------------------------------------------------------------------------------------------
    WhiteBufferByteInput() {
        super(null);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int read() throws IOException {
        if (source == null) {
            source = ByteBuffer.allocate(CAPACITY).position(CAPACITY);
            assert source.hasArray();
        }
        if (!source.hasRemaining()) {
            source.clear(); // position -> zero, limit -> capacity
            current().nextBytes(source.array());
        }
        return super.read();
    }
}
