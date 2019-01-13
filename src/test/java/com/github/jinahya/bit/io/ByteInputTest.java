package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.Objects;

@ExtendWith({ArrayByteInputParameterResolver.class})
public abstract class ByteInputTest<T extends ByteInput> {

    // -----------------------------------------------------------------------------------------------------------------
    public ByteInputTest(final Class<T> inputClass) {
        super();
        this.inputClass = Objects.requireNonNull(inputClass, "inputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testRead(final T byteInput) throws IOException {
        final int value = byteInput.read();
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected final Class<T> inputClass;
}
