package com.github.jinahya.bit.io;

import org.junit.jupiter.api.extension.ExtendWith;

/**
 * A class for testing {@link ArrayByteInput}.
 */
//@ExtendWith({ArrayByteInputParameterResolver.class})
public class ArrayByteInputTest extends AbstractByteInputTest<ArrayByteInput, byte[]> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public ArrayByteInputTest() {
        super(ArrayByteInput.class, byte[].class);
    }
}
