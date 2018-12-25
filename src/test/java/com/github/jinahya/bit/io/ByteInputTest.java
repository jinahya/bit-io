package com.github.jinahya.bit.io;

import java.util.Objects;

/**
 * An abstract class for testing subclasses of {@link ByteInput}.
 *
 * @param <T> byte input subclass type parameter
 */
public abstract class ByteInputTest<T extends ByteInput> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance on top of specified byte input class.
     *
     * @param byteInputClass the byte input class to test.
     * @see #byteInputClass
     */
    public ByteInputTest(final Class<T> byteInputClass) {
        super();
        this.byteInputClass = Objects.requireNonNull(byteInputClass, "byteInputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The byte input class to test.
     */
    protected final Class<T> byteInputClass;
}
