package com.github.jinahya.bit.io;

import java.io.InputStream;

/**
 * An abstract class for testing subclasses of {@link StreamByteInput}.
 *
 * @param <T> byte input type parameter
 * @param <U> input stream type parameter.
 * @see StreamByteOutputTest
 */
abstract class StreamByteInputTest<T extends StreamByteInput<U>, U extends InputStream>
        extends AbstractByteInputTest<T, U> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     *
     * @param byteInputClass  byte input class.
     * @param byteSourceClass the byte source class of the byte input class.
     * @see #byteInputClass
     * @see #byteSourceClass
     */
    StreamByteInputTest(final Class<T> byteInputClass, final Class<U> byteSourceClass) {
        super(byteInputClass, byteSourceClass);
    }
}
