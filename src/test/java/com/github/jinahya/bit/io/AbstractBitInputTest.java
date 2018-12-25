package com.github.jinahya.bit.io;

/**
 * An abstract class for testing subclasses of {@link AbstractBitInput}.
 *
 * @param <T> subclass type parameter.
 */
public abstract class AbstractBitInputTest<T extends AbstractBitInput> extends BitInputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance built on top of the specified bit input class.
     *
     * @param bitInputClass the bit input class.
     * @see #bitInputClass
     */
    public AbstractBitInputTest(final Class<T> bitInputClass) {
        super(bitInputClass);
    }
}
