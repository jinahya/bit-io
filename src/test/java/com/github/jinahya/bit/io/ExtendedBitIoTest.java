package com.github.jinahya.bit.io;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.github.jinahya.bit.io.ExtendedBitIoTests.applyRandomAscii;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A class for testing {@link ExtendedBitInput} and {@link ExtendedBitOutput}.
 */
public class ExtendedBitIoTest {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link ExtendedBitOutput#writeAscii(BitOutput, int, String)} and {@link
     * ExtendedBitInput#readAscii(BitInput, int)}.
     *
     * @param bitOutput        a bit output to test with.
     * @param bitInputSupplier a supplier for a bit input.
     * @throws IOException if an I/O error occurs.
     */
    @ArgumentsSource(BitIoArgumentsProvider.class)
    @ParameterizedTest
    public void testAscii(final BitOutput bitOutput, final Supplier<BitInput> bitInputSupplier) throws IOException {
        final int lengthSize = 4;
        final int count = 128;
        final List<String> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final String ascii = applyRandomAscii(lengthSize, v -> {
                try {
                    final int bytesWritten = ExtendedBitOutput.writeAscii(bitOutput, lengthSize, v);
                    return v;
                } catch (final IOException ioe) {
                    throw new RuntimeException(ioe);
                }
            });
            list.add(ascii);
        }
        bitOutput.align(1);
        final BitInput bitInput = bitInputSupplier.get();
        for (int i = 0; i < count; i++) {
            final String actual;
            try {
                actual = ExtendedBitInput.readAscii(bitInput, lengthSize);
            } catch (final IOException ioe) {
                throw new RuntimeException(ioe);
            }
            final String expected = list.remove(0);
            assertEquals(expected, actual);
        }
        bitInput.align(1);
    }
}
