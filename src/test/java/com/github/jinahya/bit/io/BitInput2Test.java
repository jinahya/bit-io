package com.github.jinahya.bit.io;

import org.junit.jupiter.api.RepeatedTest;

import java.io.IOException;
import java.util.Objects;

/**
 * An abstract class for testing subclasses of {@link BitInput}.
 *
 * @param <T> subclass type parameter.
 */
public abstract class BitInputTest<T extends BitInput> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance built on top of the specified bit input class.
     *
     * @param bitInputClass the bit input class on which the new instance is built.
     */
    public BitInputTest(final Class<T> bitInputClass) {
        super();
        this.bitInputClass = Objects.requireNonNull(bitInputClass, "bitInputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    abstract T instance();

    // -----------------------------------------------------------------------------------------------------------------
    @RepeatedTest(1024)
    void testReadBoolean() throws IOException {
        final boolean value = instance().readBoolean();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The bit input class to test.
     */
    protected final Class<T> bitInputClass;
}
