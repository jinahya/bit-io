package com.github.jinahya.bit.io;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import static java.lang.String.format;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(value = {WhiteBitInputParameterResolver.class})
class BitInput2Test {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------
    @RepeatedTest(1024)
    void testReadBoolean(final WhiteBitInput bitInput) throws IOException {
        final boolean value = bitInput.readBoolean();
    }

    @RepeatedTest(1024)
    void testReadByte(final WhiteBitInput bitInput) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Byte.SIZE + (unsigned ? 0 : 1));
        final byte value = bitInput.readByte(unsigned, size);
        if (unsigned) {
            assertTrue(value >= 0, format("negative value for unsigned; size: %d, value: %d", size, value));
        }
    }

    @RepeatedTest(1024)
    void testReadShort(final WhiteBitInput bitInput) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Short.SIZE + (unsigned ? 0 : 1));
        final short value = bitInput.readShort(unsigned, size);
        if (unsigned) {
            assertTrue(value >= 0, format("negative value for unsigned; size: %d, value: %d", size, value));
        }
    }

    @RepeatedTest(1024)
    void testReadInt(final WhiteBitInput bitInput) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Integer.SIZE + (unsigned ? 0 : 1));
        final int value = bitInput.readInt(unsigned, size);
        if (unsigned) {
            assertTrue(value >= 0, format("negative value for unsigned; size: %d, value: %d", size, value));
        }
    }

    @RepeatedTest(1024)
    void testReadLong(final WhiteBitInput bitInput) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Long.SIZE + (unsigned ? 0 : 1));
        final long value = bitInput.readLong(unsigned, size);
        if (unsigned) {
            assertTrue(value >= 0L, format("negative value for unsigned; size: %d, value: %d", size, value));
        }
    }
}
