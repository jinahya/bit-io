package com.github.jinahya.bit.io;

import java.io.DataOutput;

abstract class DataByteOutputTest<T extends DataByteOutput<U>, U extends DataOutput>
        extends AbstractByteOutputTest<T, U> {

    // -----------------------------------------------------------------------------------------------------------------
    DataByteOutputTest(final Class<T> byteOutputClass, final Class<U> byteTargetClass) {
        super(byteOutputClass, byteTargetClass);
    }
}
