package com.github.jinahya.bit.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class AbstractBitInputSpyTest {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeEach
    void stubRead() throws IOException {
        when(bitInput.read()).thenReturn(current().nextInt(0, 256));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link BitInput#readByte(boolean, int)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testReadByte() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Byte.SIZE + (unsigned ? 0 : 1));
        final byte value = bitInput.readByte(unsigned, size);
        if (unsigned) {
            assertTrue(value >= 0);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Spy
    private AbstractBitInput bitInput;
}
