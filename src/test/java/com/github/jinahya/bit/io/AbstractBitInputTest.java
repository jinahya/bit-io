package com.github.jinahya.bit.io;

/**
 * An abstract class for testing subclasses of {@link AbstractBitInput}.
 *
 * @param <T> abstract bit input type parameter.
 */
public abstract class AbstractBitInputTest<T extends AbstractBitInput> extends BitInputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with given bit input class.
     *
     * @param bitInputClass the bit input cladss.
     * @see #bitInputClass
     */
    public AbstractBitInputTest(final Class<T> bitInputClass) {
        super(bitInputClass);
    }
}
