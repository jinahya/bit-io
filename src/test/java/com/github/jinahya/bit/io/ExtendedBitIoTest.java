package com.github.jinahya.bit.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.jinahya.bit.io.ExtendedBitInput.readUnsignedIntVariable;
import static com.github.jinahya.bit.io.ExtendedBitInput.readUnsignedLongVariable;
import static com.github.jinahya.bit.io.ExtendedBitInput.readVariableLengthQuantityInt;
import static com.github.jinahya.bit.io.ExtendedBitInput.readVariableLengthQuantityLong;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeUnsignedIntVariable;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeUnsignedLongVariable;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeVariableLengthQuantityInt;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeVariableLengthQuantityLong;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ExtendedBitIoTest {

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"com.github.jinahya.bit.io.DefaultBitIoTests#sourceBitIo"})
    @ParameterizedTest
    public void testUnsignedIntVariable_Zero(final BitOutput output, final BitInput input) throws IOException {
        final int expected = 0;
        writeUnsignedIntVariable(output, expected);
        output.align(1);
        final int actual = readUnsignedIntVariable(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.DefaultBitIoTests#sourceBitIo"})
    @ParameterizedTest
    public void testUnsignedIntVariable_Max(final BitOutput output, final BitInput input) throws IOException {
        final int expected = Integer.MAX_VALUE;
        writeUnsignedIntVariable(output, expected);
        output.align(1);
        final int actual = readUnsignedIntVariable(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.DefaultBitIoTests#sourceBitIo"})
    @ParameterizedTest
    public void testUnsignedIntVariable(final BitOutput output, final BitInput input) throws IOException {

        final int expected = current().nextInt() >>> 1;
        writeUnsignedIntVariable(output, expected);
        output.align(1);
        final int actual = readUnsignedIntVariable(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.DefaultBitIoTests#sourceBitIo"})
    @ParameterizedTest
    public void testUnsignedLongVariable_Zero(final BitOutput output, final BitInput input) throws IOException {
        final long expected = 0L;
        writeUnsignedLongVariable(output, expected);
        output.align(1);
        final long actual = readUnsignedLongVariable(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.DefaultBitIoTests#sourceBitIo"})
    @ParameterizedTest
    public void testUnsignedLongVariable_Max(final BitOutput output, final BitInput input) throws IOException {
        final long expected = Long.MAX_VALUE;
        writeUnsignedLongVariable(output, expected);
        output.align(1);
        final long actual = readUnsignedLongVariable(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.DefaultBitIoTests#sourceBitIo"})
    @ParameterizedTest
    public void testUnsignedLongVariable(final BitOutput output, final BitInput input) throws IOException {

        final long expected = current().nextLong() >>> 1;
        writeUnsignedLongVariable(output, expected);
        output.align(1);
        final long actual = readUnsignedLongVariable(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"com.github.jinahya.bit.io.DefaultBitIoTests#sourceBitIo"})
    @ParameterizedTest
    public void testVariableLengthQuantityInt_Zero(final BitOutput output, final BitInput input) throws IOException {
        final int expected = 0;
        final int size = current().nextInt(1, Integer.SIZE);
        writeVariableLengthQuantityInt(output, size, expected);
        output.align(1);
        final int actual = readVariableLengthQuantityInt(input, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.DefaultBitIoTests#sourceBitIo"})
    @ParameterizedTest
    public void testVariableLengthQuantityInt_Max(final BitOutput output, final BitInput input) throws IOException {
        final int expected = current().nextInt() >>> 1;
        final int size = current().nextInt(1, Integer.SIZE);
        writeVariableLengthQuantityInt(output, size, expected);
        output.align(1);
        final int actual = readVariableLengthQuantityInt(input, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.DefaultBitIoTests#sourceBitIo"})
    @ParameterizedTest
    public void testVariableLengthQuantityInt(final BitOutput output, final BitInput input) throws IOException {
        final int expected = current().nextInt() >>> 1;
        final int size = current().nextInt(1, Integer.SIZE);
        writeVariableLengthQuantityInt(output, size, expected);
        output.align(1);
        final int actual = readVariableLengthQuantityInt(input, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    @Test
    public void testVariableLengthQuantityInt_7() {
        // https://en.wikipedia.org/wiki/Variable-length_quantity#Examples
        final Map<Integer, byte[]> map = new HashMap<>();
        map.put(0x00000000, new byte[] {0b00000000});
        map.put(0x0000007F, new byte[] {0b01111111});
        map.put(0x00000080, new byte[] {(byte) 0b10000001, 0b00000000});
        map.put(0x00002000, new byte[] {(byte) 0b11000000, 0b00000000});
        map.put(0x00003FFF, new byte[] {(byte) 0b11111111, 0b01111111});
        map.put(0x00004000, new byte[] {(byte) 0b10000001, (byte) 0b10000000, 0b00000000});
        map.put(0x001FFFFF, new byte[] {(byte) 0b11111111, (byte) 0b11111111, 0b01111111});
        map.put(0x00200000, new byte[] {(byte) 0b10000001, (byte) 0b10000000, (byte) 0b10000000, 0b00000000});
        map.put(0x08000000, new byte[] {(byte) 0b11000000, (byte) 0b10000000, (byte) 0b10000000, 0b00000000});
        map.put(0x0FFFFFFF, new byte[] {(byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, 0b01111111});
        map.forEach((k, v) -> {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
            final BitInput input = new DefaultBitInput(new StreamByteInput(null) {
                @Override
                public int read() throws IOException {
                    if (getSource() == null) {
                        setSource(new ByteArrayInputStream(baos.toByteArray()));
                    }
                    return super.read();
                }
            });
            try {
                writeVariableLengthQuantityInt(output, 7, k);
                output.align(1);
                final int actual = readVariableLengthQuantityInt(input, 7);
                input.align(1);
                assertEquals(k, actual);
                assertArrayEquals(v, baos.toByteArray());
            } catch (final IOException ioe) {
                throw new RuntimeException(ioe);
            }
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"com.github.jinahya.bit.io.DefaultBitIoTests#sourceBitIo"})
    @ParameterizedTest
    public void testVariableLengthQuantityLong_Zero(final BitOutput output, final BitInput input) throws IOException {
        final long expected = 0L;
        final int size = current().nextInt(1, Long.SIZE);
        writeVariableLengthQuantityLong(output, size, expected);
        output.align(1);
        final long actual = readVariableLengthQuantityLong(input, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.DefaultBitIoTests#sourceBitIo"})
    @ParameterizedTest
    public void testVariableLengthQuantityLong_Max(final BitOutput output, final BitInput input) throws IOException {
        final long expected = Long.MAX_VALUE;
        final int size = current().nextInt(1, Long.SIZE);
        writeVariableLengthQuantityLong(output, size, expected);
        output.align(1);
        final long actual = readVariableLengthQuantityLong(input, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.DefaultBitIoTests#sourceBitIo"})
    @ParameterizedTest
    public void testVariableLengthQuantityLong(final BitOutput output, final BitInput input) throws IOException {
        final long expected = current().nextLong() >>> 1;
        final int size = current().nextInt(1, Long.SIZE);
        writeVariableLengthQuantityLong(output, size, expected);
        output.align(1);
        final long actual = readVariableLengthQuantityLong(input, size);
        input.align(1);
        assertEquals(expected, actual);
    }
}
