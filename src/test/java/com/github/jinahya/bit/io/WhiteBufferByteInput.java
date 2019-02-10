package com.github.jinahya.bit.io;

import java.io.IOException;
import java.nio.ByteBuffer;

import static java.nio.ByteBuffer.allocate;
import static java.util.concurrent.ThreadLocalRandom.current;

class WhiteBufferByteInput extends BufferByteInput<ByteBuffer> {

    // -----------------------------------------------------------------------------------------------------------------
    WhiteBufferByteInput() {
        super(null);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int read() throws IOException {
        if (source == null) {
            source = allocate(1);
            source.position(source.limit());
        }
        if (!source.hasRemaining()) {
            source.clear(); // position -> zero, limit -> capacity
            current().nextBytes(source.array());
        }
        return super.read();
    }
}
