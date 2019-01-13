package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * A class for testing {@link ArrayByteInput}.
 */
@ExtendWith({ArrayByteInputParameterResolver.class, ArrayByteSourceParameterResolver.class})
class ArrayByteInputTest extends AbstractByteInputTest<ArrayByteInput, byte[]> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    ArrayByteInputTest() {
        super(ArrayByteInput.class, byte[].class);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testGetIndex(final ArrayByteInput byteInput) {
        final int index = byteInput.getIndex();
    }

    @Test
    void testSetIndex(final ArrayByteInput byteInput) {
        byteInput.setIndex(current().nextInt());
    }
}
