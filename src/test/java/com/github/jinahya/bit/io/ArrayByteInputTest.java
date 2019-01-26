package com.github.jinahya.bit.io;

/**
 * A class for testing {@link ArrayByteInput}.
 */
class ArrayByteInputTest extends AbstractByteInputTest<ArrayByteInput, byte[]> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    ArrayByteInputTest() {
        super(ArrayByteInput.class, byte[].class);
    }
}
