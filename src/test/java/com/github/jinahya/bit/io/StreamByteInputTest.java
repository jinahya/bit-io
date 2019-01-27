package com.github.jinahya.bit.io;

import java.io.InputStream;

/**
 * An abstract class for testing subclasses of {@link StreamByteInput}.
 *
 * @param <T> stream byte input type parameter
 * @param <U> byte input type parameter.
 */
abstract class StreamByteInputTest<T extends StreamByteInput<U>, U extends InputStream>
        extends AbstractByteInputTest<T, U> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     *
     * @param byteInputClass byte input class.
     * @param sourceClass    input stream class.
     */
    StreamByteInputTest(final Class<T> byteInputClass, final Class<U> sourceClass) {
        super(byteInputClass, sourceClass);
    }
}
