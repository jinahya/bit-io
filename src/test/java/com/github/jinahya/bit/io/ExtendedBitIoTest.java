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

import static com.github.jinahya.bit.io.ExtendedBitInput.readVariableLengthQuantityInt;
import static com.github.jinahya.bit.io.ExtendedBitInput.readVariableLengthQuantityInt7;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeVariableLengthQuantityInt;
import static com.github.jinahya.bit.io.ExtendedBitOutput.writeVariableLengthQuantityInt7;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ExtendedBitIoTest {

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"com.github.jinahya.bit.io.DefaultBitIoTests#sourceBitIo"})
    @ParameterizedTest
    public void testVariableLengthQuantityInt(final BitOutput output, final BitInput input) throws IOException {
        final int expected = current().nextInt() >>> 1;
        final int size = current().nextInt(1, Byte.SIZE);
        writeVariableLengthQuantityInt(output, size, expected);
        output.align(1);
        final int actual = readVariableLengthQuantityInt(input, size);
        input.align(1);
        assertEquals(expected, actual);
    }

    @MethodSource({"com.github.jinahya.bit.io.DefaultBitIoTests#sourceBitIo"})
    @ParameterizedTest
    public void testVariableLengthQuantityInt7(final BitOutput output, final BitInput input) throws IOException {
        final int expected = current().nextInt() >>> 1;
        writeVariableLengthQuantityInt7(output, expected);
        output.align(1);
        final int actual = readVariableLengthQuantityInt7(input);
        input.align(1);
        assertEquals(expected, actual);
    }

    @Test
    public void testVariableLengthQuantityInt7Tv() {
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
                writeVariableLengthQuantityInt7(output, k);
                output.align(1);
                final int actual = readVariableLengthQuantityInt7(input);
                input.align(1);
                assertEquals(k, actual);
                assertArrayEquals(v, baos.toByteArray());
            } catch (final IOException ioe) {
                throw new RuntimeException(ioe);
            }
        });
    }
}
