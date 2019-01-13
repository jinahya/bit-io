package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

abstract class ByteInputTest<T extends ByteInput> {

    // -----------------------------------------------------------------------------------------------------------------
    ByteInputTest(final Class<T> inputClass) {
        super();
        this.inputClass = Objects.requireNonNull(inputClass, "inputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testRead(final T byteInput) throws IOException {
        final int value = byteInput.read();
    }

    // -----------------------------------------------------------------------------------------------------------------
    final Class<T> inputClass;
}
