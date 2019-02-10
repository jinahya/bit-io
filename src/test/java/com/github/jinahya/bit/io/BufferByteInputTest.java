package com.github.jinahya.bit.io;

import java.nio.ByteBuffer;

/**
 * An abstract class for testing subclasses of {@link BufferByteInput}.
 *
 * @param <T> byte input type parameter
 * @param <U> byte buffer type paramter
 * @see BufferByteOutputTest
 */
abstract class BufferByteInputTest<T extends BufferByteInput<U>, U extends ByteBuffer>
        extends AbstractByteInputTest<T, U> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     *
     * @param byteInputClass a byte input class.
     * @param byteSourceClass    a source class.
     */
    BufferByteInputTest(final Class<T> byteInputClass, final Class<U> byteSourceClass) {
        super(byteInputClass, byteSourceClass);
    }
}
