package com.github.jinahya.bit.io;

import java.nio.ByteBuffer;

/**
 * An abstract class for testing subclasses of {@link BufferByteOutput}.
 *
 * @param <T> byte output type parameter
 * @param <U> byte buffer type parameter
 * @see BufferByteInputTest
 */
abstract class BufferByteOutputTest<T extends BufferByteOutput<U>, U extends ByteBuffer>
        extends AbstractByteOutputTest<T, U> {

    // -----------------------------------------------------------------------------------------------------------------
    BufferByteOutputTest(final Class<T> byteOutputClass, final Class<U> byteTargetClass) {
        super(byteOutputClass, byteTargetClass);
    }
}
