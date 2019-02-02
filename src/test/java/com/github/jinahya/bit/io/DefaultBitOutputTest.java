package com.github.jinahya.bit.io;

import static java.util.Objects.requireNonNull;

/**
 * An abstract class for testing subclasses of {@link DefaultBitOutput}.
 *
 * @param <T> bit output type parameter.
 * @param <U> byte output type parameter.
 */
public abstract class DefaultBitOutputTest<T extends DefaultBitOutput<U>, U extends ByteOutput>
        extends AbstractBitOutputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     *
     * @param bitOutputClass  bit output class.
     * @param byteOutputClass byte output class.
     */
    public DefaultBitOutputTest(final Class<T> bitOutputClass, final Class<U> byteOutputClass) {
        super(bitOutputClass);
        this.byteOutputClass = requireNonNull(byteOutputClass, "byteOutputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The byte output class.
     */
    protected final Class<U> byteOutputClass;
}
