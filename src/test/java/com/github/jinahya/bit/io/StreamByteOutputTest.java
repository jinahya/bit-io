package com.github.jinahya.bit.io;

import java.io.OutputStream;

/**
 * An abstract class for testing subclasses of {@link StreamByteOutput}.
 *
 * @param <T> stream byte output type parameter
 * @param <U> target type parameter
 */
abstract class StreamByteOutputTest<T extends StreamByteOutput<U>, U extends OutputStream>
        extends AbstractByteOutputTest<T, U> {

    // -----------------------------------------------------------------------------------------------------------------
    StreamByteOutputTest(final Class<T> byteOutputClass, final Class<U> targetClass) {
        super(byteOutputClass, targetClass);
    }
}
