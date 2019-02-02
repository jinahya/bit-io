package com.github.jinahya.bit.io;

import java.util.Objects;

/**
 * An abstract class for testing subclasses of {@link DefaultBitInput}.
 *
 * @param <T> bit input type parameter
 * @param <U> byte input type parameter.
 */
public abstract class DefaultBitInputTest<T extends DefaultBitInput<U>, U extends ByteInput>
        extends AbstractBitInputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------
    public DefaultBitInputTest(final Class<T> bitInputClass, final Class<U> byteInputClass) {
        super(bitInputClass);
        this.byteInputClass = Objects.requireNonNull(byteInputClass, "byteInputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected final Class<U> byteInputClass;
}
