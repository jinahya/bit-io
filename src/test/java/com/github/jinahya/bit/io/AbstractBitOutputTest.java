package com.github.jinahya.bit.io;

public abstract class AbstractBitOutputTest<T extends AbstractBitOutput> extends BitOutputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------
    public AbstractBitOutputTest(final Class<T> bitOutputClass) {
        super(bitOutputClass);
    }
}
