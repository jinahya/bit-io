package com.github.jinahya.bit.io;

abstract class AbstractByteInputParameterResolver<T extends AbstractByteInput<U>, U>
        extends ByteInputParameterResolver<T> {

    // -----------------------------------------------------------------------------------------------------------------
    AbstractByteInputParameterResolver(final Class<T> byteInputClass) {
        super(byteInputClass);
    }
}
