package com.github.jinahya.bit.io;

import java.util.Objects;

/**
 * An abstract class for testing subclasses of {@link ByteOutput}.
 *
 * @param <T> byte output subclass type parameter
 */
public abstract class ByteOutputTest<T extends ByteOutput> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance on top of specified byte output class.
     *
     * @param byteOutputClass the byte output class to test.
     * @see #byteOutputClass
     */
    public ByteOutputTest(final Class<T> byteOutputClass) {
        super();
        this.byteOutputClass = Objects.requireNonNull(byteOutputClass, "byteoutputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The byte output class to test.
     */
    protected final Class<T> byteOutputClass;
}
