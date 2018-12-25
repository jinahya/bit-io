package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * An abstract class for testing subclasses of {@link BitOutput}.
 *
 * @param <T> subclass type parameter.
 */
public abstract class BitOutputTest<T extends BitOutput> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance built on top of the specified bit output class.
     *
     * @param bitOutputClass the bit output class on which the new instance is built.
     */
    public BitOutputTest(final Class<T> bitOutputClass) {
        super();
        this.bitOutputClass = Objects.requireNonNull(bitOutputClass, "bitOutputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void test() {
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The bit output class to test.
     */
    protected final Class<T> bitOutputClass;
}
