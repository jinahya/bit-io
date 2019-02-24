package com.github.jinahya.bit.io;

import java.io.OutputStream;

/**
 * An abstract class for testing subclasses of {@link StreamByteOutput}.
 *
 * @param <T> byte output type parameter
 * @param <U> output stream type parameter
 * @see StreamByteInputTest
 */
abstract class StreamByteOutputTest<T extends StreamByteOutput<U>, U extends OutputStream>
        extends AbstractByteOutputTest<T, U> {

    // -----------------------------------------------------------------------------------------------------------------
    StreamByteOutputTest(final Class<T> byteOutputClass, final Class<U> byteTargetClass) {
        super(byteOutputClass, byteTargetClass);
    }
}
