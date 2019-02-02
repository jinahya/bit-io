package com.github.jinahya.bit.io;

import java.nio.ByteBuffer;

abstract class BufferByteOutputTest<T extends BufferByteOutput<U>, U extends ByteBuffer>
        extends AbstractByteOutputTest<T, U> {

    // -----------------------------------------------------------------------------------------------------------------
    BufferByteOutputTest(final Class<T> byteOutputClass, final Class<U> byteTargetClass) {
        super(byteOutputClass, byteTargetClass);
    }
}
