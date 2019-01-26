package com.github.jinahya.bit.io;

import java.util.Objects;

abstract class AbstractByteInputParameterResolver<T extends AbstractByteInput<U>, U>
        extends ByteInputParameterResolver<T> {

    // -----------------------------------------------------------------------------------------------------------------
    AbstractByteInputParameterResolver(final Class<T> byteInputClass, final Class<U> sourceClass) {
        super(byteInputClass);
        this.sourceClass = Objects.requireNonNull(sourceClass, "sourceClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    final Class<U> sourceClass;
}
