package com.github.jinahya.bit.io;

import java.util.Objects;

abstract class AbstractByteInputTest<T extends AbstractByteInput<U>, U> extends ByteInputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------
    AbstractByteInputTest(final Class<T> byteInputClass, final Class<U> sourceClass) {
        super(byteInputClass);
        this.sourceClass = Objects.requireNonNull(sourceClass, "sourceClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    final Class<U> sourceClass;
}
