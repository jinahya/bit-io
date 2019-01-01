package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

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
    @Test
    void test() {
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The bit input class to test.
     */
    protected final Class<T> bitInputClass;
}
