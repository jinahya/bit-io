package com.github.jinahya.bit.io;

import java.nio.ByteBuffer;

class BlackBufferByteOutputTest extends BufferByteOutputTest<BlackBufferByteOutput, ByteBuffer> {

    // -----------------------------------------------------------------------------------------------------------------
    BlackBufferByteOutputTest() {
        super(BlackBufferByteOutput.class, ByteBuffer.class);
    }
}
