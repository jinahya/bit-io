package com.github.jinahya.bit.io;

import java.util.Objects;

/**
 * An abstract class for testing subclasses of {@link AbstractByteOutput}.
 *
 * @param <T> byte output type parameter.
 * @param <U> byte target type parameter.
 */
abstract class AbstractByteOutputTest<T extends AbstractByteOutput<U>, U> extends ByteOutputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     *
     * @param byteOutputClass byte output class.
     * @param byteTargetClass byte target class.
     */
    AbstractByteOutputTest(final Class<T> byteOutputClass, final Class<U> byteTargetClass) {
        super(byteOutputClass);
        this.byteTargetClass = Objects.requireNonNull(byteTargetClass, "byteTargetClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The byte target class.
     */
    final Class<U> byteTargetClass;
}
