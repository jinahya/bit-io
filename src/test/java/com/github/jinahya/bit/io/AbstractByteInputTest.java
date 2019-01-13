package com.github.jinahya.bit.io;

import java.util.Objects;

public abstract class AbstractByteInputTest<T extends AbstractByteInput<U>, U> extends ByteInputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------
    public AbstractByteInputTest(final Class<T> byteInputClass, final Class<U> sourceClass) {
        super(byteInputClass);
        this.sourceClass = Objects.requireNonNull(sourceClass, "sourceClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected final Class<U> sourceClass;
}
