package com.github.jinahya.bit.io.miscellaneous.mongodb;

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
import com.github.jinahya.bit.io.DefaultBitInput;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteOutput;
import com.github.jinahya.bit.io.miscellaneous.ieee.Decimal;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link BsonDecimal128}.
 *
 * <p>The vectors are from the BSON Corpus ({@code decimal128-*.json}), cited by their descriptions; the bytes are the
 * 16 value bytes of {@code canonical_bson}, in their little-endian order. {@code BsonDecimal128IT} runs the whole
 * corpus with the {@code live-sources} profile.</p>
 *
 * @see <a href="https://github.com/mongodb/specifications/tree/master/source/bson-corpus">BSON Corpus</a>
 */
class BsonDecimal128Test {

    @Test
    void regularAndScientific() throws IOException {
        assertBoth("01000000000000000000000000003E30", "0.1"); // Regular - 0.1
        assertBoth("00000000000000000000000000004030", "0"); // Regular - 0
        assertBoth("000000000000000000000000000040B0", "-0"); // Regular - -0
        assertBoth("F2AF967ED05C82DE3297FF6FDE3C4030", "1234567890123456789012345678901234"); // Regular - Largest
        assertBoth("01000000000000000000000000000000", "1E-6176"); // Scientific - Tiny
        assertBoth("64000000000000000000000000002CB0", "-1.00E-8"); // Scientific - Fractional
        assertBoth("01000000000000000000000000004630", "1E+3"); // Scientific - No Decimal with Signed Exponent
        assertBoth("FFFFFFFF638E8D37C087ADBE09EDFF5F",
                   "9.999999999999999999999999999999999E+6144"); // Scientific - Largest
    }

    @Test
    void specialValues() throws IOException {
        assertBoth("00000000000000000000000000000078", "Infinity"); // Special - Canonical Positive Infinity
        assertBoth("000000000000000000000000000000F8", "-Infinity"); // Special - Canonical Negative Infinity
        // the BSON string of every NaN is "NaN"; a Decimal keeps the sign, the kind, and the payload
        assertBoth("0000000000000000000000000000007C", "NaN"); // Special - Canonical NaN
        assertBoth("000000000000000000000000000000FC", "-NaN"); // Special - Negative NaN
        assertBoth("0000000000000000000000000000007E", "sNaN"); // Special - Canonical SNaN
        assertBoth("000000000000000000000000000000FE", "-sNaN"); // Special - Negative SNaN
        assertBoth("1200000000000000000000000000007E", "sNaN18"); // Special - NaN with a payload
        assertTrue(read("1200000000000000000000000000007E").isNaN());
    }

    @Test
    void invalidRepresentationsReadAsZero() throws IOException {
        // the coefficient is 100 followed by 111 bits, which always exceeds 10^34 - 1
        assertEquals("0", read("0000000000000000000000000000106C").toString()); // ... treated as 0
        assertEquals("-0", read("DCBA9876543210DEADBEEF00000010EC").toString()); // ... treated as -0
        assertEquals("0E+3", read("FFFFFFFFFFFFFFFFFFFFFFFFFFFF116C").toString()); // ... treated as 0E3
        assertEquals("00000000000000000000000000004630", write(read("FFFFFFFFFFFFFFFFFFFFFFFFFFFF116C")));
    }

    @Test
    void writeRejectsWhatTheFormatCannotHoldExactly() {
        assertThrows(IllegalArgumentException.class,
                     () -> write(Decimal.parse("12345678901234567890123456789012345"))); // 35 digits
        assertThrows(IllegalArgumentException.class, () -> write(Decimal.parse("1E+6112")));
        assertThrows(IllegalArgumentException.class, () -> write(Decimal.parse("1E-6177")));
        assertThrows(IllegalArgumentException.class,
                     () -> write(Decimal.nan(false, false, BigInteger.TEN.pow(33))));
        assertThrows(NullPointerException.class, () -> BsonDecimal128.INSTANCE.read(null));
        assertThrows(NullPointerException.class, () -> write(null));
        assertEquals(16, BsonDecimal128.BYTES);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static void assertBoth(final String hex, final String string) throws IOException {
        assertEquals(string, read(hex).toString(), hex);
        assertEquals(hex, write(Decimal.parse(string)), string);
    }

    private static Decimal read(final String hex) throws IOException {
        final byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return BsonDecimal128.INSTANCE.read(new DefaultBitInput(new ArrayByteInput(bytes)));
    }

    private static String write(final Decimal value) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(bytes));
        BsonDecimal128.INSTANCE.write(output, value);
        output.align(1);
        final StringBuilder builder = new StringBuilder();
        for (final byte b : bytes.toByteArray()) {
            builder.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1).toUpperCase());
        }
        return builder.toString();
    }
}
