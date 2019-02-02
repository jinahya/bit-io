package com.github.jinahya.bit.io;

import java.io.DataInput;

/**
 * An abstract class for testing subclasses of {@link DataByteInput}.
 *
 * @param <T> data byte input type parameter.
 * @param <U> data input type parameter.
 */
abstract class DataByteInputTest<T extends DataByteInput<U>, U extends DataInput> extends AbstractByteInputTest<T, U> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     *
     * @param byteInputClass data byte input class
     * @param sourceClass    data input class
     */
    DataByteInputTest(final Class<T> byteInputClass, final Class<U> sourceClass) {
        super(byteInputClass, sourceClass);
    }
}
