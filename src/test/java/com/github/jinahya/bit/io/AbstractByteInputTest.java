package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.util.Objects;

abstract class AbstractByteInputTest<T extends AbstractByteInput<U>, U> extends ByteInputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------
    AbstractByteInputTest(final Class<T> byteInputClass, final Class<U> sourceClass) {
        super(byteInputClass);
        this.sourceClass = Objects.requireNonNull(sourceClass, "sourceClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testGetSource(final T byteInput) {
        byteInput.getSource();
    }

    @Test
    void testSetSource(final U byteSource) {
    }

    // -----------------------------------------------------------------------------------------------------------------
    final Class<U> sourceClass;
}
