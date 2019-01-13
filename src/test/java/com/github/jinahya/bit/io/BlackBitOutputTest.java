package com.github.jinahya.bit.io;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import static java.util.concurrent.ThreadLocalRandom.current;

@ExtendWith(value = {BlackBitOutputParameterResolver.class})
class BlackBitOutputTest {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------

    @RepeatedTest(1024)
    void testWriteBoolean(final BitOutput bitOutput) throws IOException {
        bitOutput.writeBoolean(current().nextBoolean());
    }

    @RepeatedTest(1024)
    void testWriteByte(final BitOutput bitOutput) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Byte.SIZE + (unsigned ? 0 : 1));
        byte value = (byte) current().nextInt();
        final int shift = Integer.SIZE - size;
        value = (byte) (unsigned ? value >>> shift : value >> shift);
        bitOutput.writeByte(unsigned, size, value);
    }

    @RepeatedTest(1024)
    void testWriteShort(final BitOutput bitOutput) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Short.SIZE + (unsigned ? 0 : 1));
        short value = (short) current().nextInt();
        final int shift = Integer.SIZE - size;
        value = (short) (unsigned ? value >>> shift : value >> shift);
        bitOutput.writeShort(unsigned, size, value);
    }

    @RepeatedTest(1024)
    void testWriteInt(final BitOutput bitOutput) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Integer.SIZE + (unsigned ? 0 : 1));
        int value = current().nextInt();
        final int shift = Integer.SIZE - size;
        value = (short) (unsigned ? value >>> shift : value >> shift);
        bitOutput.writeInt(unsigned, size, value);
    }

    @RepeatedTest(1024)
    void testWriteLong(final BitOutput bitOutput) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Long.SIZE + (unsigned ? 0 : 1));
        long value = current().nextLong();
        final int shift = Long.SIZE - size;
        value = (unsigned ? value >>> shift : value >> shift);
        bitOutput.writeLong(unsigned, size, value);
    }

    @RepeatedTest(1024)
    void testWriteChar(final BitOutput bitOutput) throws IOException {
        final int size = current().nextInt(1, Character.SIZE + 1);
        final char value = (char) (current().nextInt(0, 65536) >> (Character.SIZE - size));
        bitOutput.writeChar(size, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @RepeatedTest(1024)
    void testAlign(final BitOutput bitOutput) throws IOException {
        final int bytes = current().nextInt(1, 256);
        bitOutput.align(bytes);
    }
}
