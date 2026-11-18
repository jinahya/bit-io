package com.github.jinahya.bit.io.miscellaneous.ieee;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2026 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.github.jinahya.bit.io.ArrayByteInput;
import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.BitReader;
import com.github.jinahya.bit.io.BitWriter;
import com.github.jinahya.bit.io.DefaultBitInput;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteOutput;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link Decimal32Dpd}, {@link Decimal64Dpd}, and {@link Decimal128Dpd}.
 *
 * <p>The vectors are from the General Decimal Arithmetic testcases ({@code dsEncode.decTest}, {@code ddEncode.decTest},
 * {@code dqEncode.decTest}), cited by their testcase ids; {@code DecimalDpdIT} runs all of them with the
 * {@code live-sources} profile.</p>
 *
 * @see <a href="https://speleotrove.com/decimal/dectest.html">General Decimal Arithmetic Testcases</a>
 */
class DecimalDpdTest {

    // ------------------------------------------------------------------------------------------------------- declets

    @Test
    void decletExamplesOfTheDpdSummary() {
        // BCD -> Densely Packed; "A Summary of Densely Packed Decimal encoding"
        assertEquals(0x005, DecimalDpdFormat.declet(5)); // 000 000 0101
        assertEquals(0x009, DecimalDpdFormat.declet(9)); // 000 000 1001
        assertEquals(0x055, DecimalDpdFormat.declet(55)); // 000 101 0101
        assertEquals(0x05F, DecimalDpdFormat.declet(99)); // 000 101 1111
        assertEquals(0x2D5, DecimalDpdFormat.declet(555)); // 101 101 0101
        assertEquals(0x0FF, DecimalDpdFormat.declet(999)); // 001 111 1111
    }

    @Test
    void everyValueRoundTripsAndEveryDecletDecodes() {
        for (int value = 0; value < 1000; value++) {
            final int declet = DecimalDpdFormat.declet(value);
            assertTrue(declet >= 0 && declet < 1024);
            assertEquals(value, DecimalDpdFormat.digits(declet));
            if (value < 80) { // 0 through 79 encode as in BCD, right-aligned
                assertEquals(((value / 10) << 4) | (value % 10), declet, "value " + value);
            }
        }
        int nonCanonical = 0;
        for (int declet = 0; declet < 1024; declet++) {
            final int value = DecimalDpdFormat.digits(declet);
            assertTrue(value >= 0 && value < 1000);
            if (DecimalDpdFormat.declet(value) != declet) {
                nonCanonical++;
                // the redundant declets are those of three 8s and 9s, with the first two bits not 0
                assertTrue(value / 100 >= 8 && value / 10 % 10 >= 8 && value % 10 >= 8, "declet " + declet);
                assertTrue((declet >> 8) != 0, "declet " + declet);
            }
        }
        assertEquals(24, nonCanonical);
    }

    // ---------------------------------------------------------------------------------------------------- decimal32

    @Test
    void decimal32() throws IOException {
        final Decimal32Dpd codec = Decimal32Dpd.INSTANCE;
        assertBoth(codec, "A23003D0", "-7.50"); // decs001
        assertBoth(codec, "A26003D0", "-7.50E+3"); // decs003
        assertBoth(codec, "A1c003D0", "-7.50E-7"); // decs017
        assertBoth(codec, "2654d2e7", "1234567"); // decs020
        assertBoth(codec, "a654d2e7", "-1234567"); // decs021
        assertBoth(codec, "77f3fcff", "9.999999E+96"); // decs031
        assertBoth(codec, "47f4d2e7", "1.234567E+96"); // decs034
        assertBoth(codec, "00600001", "1E-95"); // decs072
        assertBoth(codec, "04000000", "1.000000E-95"); // decs074
        assertBoth(codec, "7c000000", "NaN"); // decs511
        assertBoth(codec, "7e000000", "sNaN"); // decs515
        assertBoth(codec, "fc000000", "-NaN"); // decs531
        assertBoth(codec, "fe000000", "-sNaN"); // decs535
        assertBoth(codec, "78000000", "Infinity");
        assertBoth(codec, "f8000000", "-Infinity");
        // non-canonical encodings read as their values, and write canonically
        assertEquals("sNaN999999", read(codec, "7fffffff").toString()); // decs518
        assertEquals("7e03fcff", write(codec, read(codec, "7fffffff")));
        assertEquals("78000000", write(codec, read(codec, "78787878"))); // decs501
        assertEquals("NaN", read(codec, "7d000000").toString()); // decs513
    }

    @Test
    void decimal64() throws IOException {
        final Decimal64Dpd codec = Decimal64Dpd.INSTANCE;
        assertBoth(codec, "A23c0000000003D0", "-7.50E+3"); // dece003
        assertBoth(codec, "A2140000000003D0", "-7.50E-7"); // dece017
        assertBoth(codec, "77fcff3fcff3fcff", "9.999999999999999E+384"); // dece033
        assertBoth(codec, "47fd34b9c1e28e56", "1.234567890123456E+384"); // dece035
        assertBoth(codec, "003c000000000001", "1E-383"); // decd072
        assertBoth(codec, "0400000000000000", "1.000000000000000E-383"); // decd074
        assertBoth(codec, "7c00000000000000", "NaN");
        assertBoth(codec, "f800000000000000", "-Infinity");
        assertEquals("7800000000000000", write(codec, read(codec, "7878787878787878"))); // decd501
    }

    @Test
    void decimal128() throws IOException {
        final Decimal128Dpd codec = Decimal128Dpd.INSTANCE;
        assertBoth(codec, "A20840000000000000000000000003D0", "-7.50E+3"); // decq003
        assertBoth(codec, "A205c0000000000000000000000003D0", "-7.50E-7"); // decq017
        assertBoth(codec, "77ffcff3fcff3fcff3fcff3fcff3fcff",
                   "9.999999999999999999999999999999999E+6144"); // decq032
        assertBoth(codec, "47ffd34b9c1e28e56f3c127177823534",
                   "1.234567890123456789012345678901234E+6144"); // decq034
        assertBoth(codec, "00084000000000000000000000000001", "1E-6143"); // decq072
        assertBoth(codec, "04000000000000000000000000000000",
                   "1.000000000000000000000000000000000E-6143"); // decq074
        assertBoth(codec, "fe000000000000000000000000000000", "-sNaN");
        assertEquals("78000000000000000000000000000000",
                     write(codec, read(codec, "79797979797979797979797979797979"))); // decq503
    }

    // -------------------------------------------------------------------------------------------------------- ranges

    @Test
    void writeRejectsWhatTheFormatCannotHoldExactly() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> write(Decimal32Dpd.INSTANCE, Decimal.parse("12345678")));
        assertThrows(IllegalArgumentException.class, () -> write(Decimal32Dpd.INSTANCE, Decimal.parse("1E+91")));
        assertThrows(IllegalArgumentException.class, () -> write(Decimal32Dpd.INSTANCE, Decimal.parse("1E-102")));
        assertThrows(IllegalArgumentException.class, () -> write(Decimal32Dpd.INSTANCE, Decimal.parse("NaN1234567")));
        assertThrows(IllegalArgumentException.class,
                     () -> write(Decimal64Dpd.INSTANCE, Decimal.parse("12345678901234567")));
        assertThrows(IllegalArgumentException.class, () -> write(Decimal128Dpd.INSTANCE, Decimal.parse("1E+6112")));
        // the limits themselves
        assertEquals("1E+90", read(Decimal32Dpd.INSTANCE, write(Decimal32Dpd.INSTANCE, Decimal.parse("1E+90")))
                .toString());
        assertEquals("1E-101", read(Decimal32Dpd.INSTANCE, write(Decimal32Dpd.INSTANCE, Decimal.parse("1E-101")))
                .toString());
        assertEquals("NaN999999", read(Decimal32Dpd.INSTANCE, write(Decimal32Dpd.INSTANCE, Decimal.parse("NaN999999")))
                .toString());
    }

    @Test
    void nullArguments() {
        assertThrows(NullPointerException.class, () -> Decimal32Dpd.INSTANCE.read(null));
        assertThrows(NullPointerException.class, () -> Decimal32Dpd.INSTANCE.write(null, Decimal.parse("1")));
        assertThrows(NullPointerException.class, () -> write(Decimal32Dpd.INSTANCE, null));
    }

    @Test
    void sizes() {
        assertEquals(32, Decimal32Dpd.SIZE);
        assertEquals(64, Decimal64Dpd.SIZE);
        assertEquals(128, Decimal128Dpd.SIZE);
        assertEquals(BigInteger.ZERO, Decimal.infinity(false).getCoefficient());
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static <C extends BitReader<Decimal> & BitWriter<Decimal>> void assertBoth(final C codec, final String hex,
                                                                                       final String string)
            throws IOException {
        assertEquals(string, read(codec, hex).toString(), hex);
        assertEquals(hex.toLowerCase(), write(codec, Decimal.parse(string)), string);
    }

    static Decimal read(final BitReader<Decimal> codec, final String hex) throws IOException {
        final byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return codec.read(new DefaultBitInput(new ArrayByteInput(bytes)));
    }

    static String write(final BitWriter<Decimal> codec, final Decimal value) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(bytes));
        codec.write(output, value);
        output.align(1);
        final StringBuilder builder = new StringBuilder();
        for (final byte b : bytes.toByteArray()) {
            builder.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1));
        }
        return builder.toString();
    }
}
