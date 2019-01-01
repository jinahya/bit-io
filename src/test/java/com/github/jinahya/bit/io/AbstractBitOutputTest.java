package com.github.jinahya.bit.io;

/**
 * An abstract class for testing subclasses of {@link AbstractBitOutput}.
 *
 * @param <T> subclass type parameter.
 */
public abstract class AbstractBitOutputTest<T extends AbstractBitOutput> extends BitOutputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance built on top of the specified bit output class.
     *
     * @param bitOutputClass the bit output class.
     * @see #bitOutputClass
     */
    public AbstractBitOutputTest(final Class<T> bitOutputClass) {
        super(bitOutputClass);
    }
}
