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

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link Decimal}, whose string forms follow the General Decimal Arithmetic specification.
 *
 * @see <a href="https://speleotrove.com/decimal/daconvs.html">Numeric string conversions</a>
 */
class DecimalTest {

    @Test
    void toScientificString() {
        // (coefficient, exponent) -> string; the examples of the to-scientific-string conversion
        final Object[][] rows = {
                {"123", 0, "123"}, {"-123", 0, "-123"}, {"123", 1, "1.23E+3"}, {"123", 3, "1.23E+5"},
                {"123", -1, "12.3"}, {"123", -5, "0.00123"}, {"123", -10, "1.23E-8"}, {"-123", -12, "-1.23E-10"},
                {"0", 0, "0"}, {"0", -2, "0.00"}, {"0", 2, "0E+2"}, {"-0", 0, "-0"}, {"5", -6, "0.000005"},
                {"50", -7, "0.0000050"}, {"5", -7, "5E-7"}
        };
        for (final Object[] row : rows) {
            final String coefficient = (String) row[0];
            final boolean negative = coefficient.startsWith("-");
            final BigInteger magnitude = new BigInteger(negative ? coefficient.substring(1) : coefficient);
            final Decimal value = Decimal.finite(negative, magnitude, (Integer) row[1]);
            assertEquals(row[2], value.toString());
            assertEquals(value, Decimal.parse((String) row[2]), (String) row[2]);
        }
    }

    @Test
    void specialValues() {
        assertEquals("Infinity", Decimal.infinity(false).toString());
        assertEquals("-Infinity", Decimal.infinity(true).toString());
        assertEquals("NaN", Decimal.nan(false, false, BigInteger.ZERO).toString());
        assertEquals("-sNaN", Decimal.nan(true, true, BigInteger.ZERO).toString());
        assertEquals("NaN123", Decimal.nan(false, false, BigInteger.valueOf(123L)).toString());
        for (final String string : new String[]{"Infinity", "-Infinity", "NaN", "-NaN", "sNaN", "-sNaN12", "NaN999"}) {
            assertEquals(string, Decimal.parse(string).toString());
        }
        assertEquals(Decimal.infinity(false), Decimal.parse("inf"));
        assertEquals(Decimal.infinity(true), Decimal.parse("-INFINITY"));
        assertEquals(Decimal.nan(false, true, BigInteger.ZERO), Decimal.parse("SNAN"));
        final Decimal nan = Decimal.parse("-sNaN7");
        assertTrue(nan.isNaN());
        assertTrue(nan.isSignaling());
        assertTrue(nan.isNegative());
        assertFalse(nan.isFinite());
        assertEquals(BigInteger.valueOf(7L), nan.getCoefficient());
        assertTrue(Decimal.infinity(true).isInfinite());
    }

    @Test
    void parseAcceptsNumericStrings() {
        assertEquals("12.70", Decimal.parse("12.70").toString());
        assertEquals("0.003", Decimal.parse("+0.003").toString());
        assertEquals("17", Decimal.parse("017.").toString());
        assertEquals("0.5", Decimal.parse(".5").toString());
        assertEquals("4E+9", Decimal.parse("4E+9").toString());
        assertEquals("7.3E-8", Decimal.parse("0.73e-7").toString());
    }

    @Test
    void parseRejectsInvalidStrings() {
        for (final String string : new String[]{"", "+", ".", "1..2", "1e", "1e+", "e1", "1 ", " 1", "1x", "NaNx",
                                                "Infinit",
                                                "1E99999999999999999999", "1E2147483648"}) {
            assertThrows(IllegalArgumentException.class, () -> Decimal.parse(string), string);
        }
        assertThrows(NullPointerException.class, () -> Decimal.parse(null));
    }

    @Test
    void bigDecimal() {
        assertEquals(Decimal.parse("-1.20"), Decimal.valueOf(new BigDecimal("-1.20")));
        assertEquals(new BigDecimal("1.23E+5"), Decimal.parse("1.23E+5").toBigDecimal());
        assertEquals(new BigDecimal("0.00"), Decimal.parse("-0.00").toBigDecimal());
        assertThrows(ArithmeticException.class, () -> Decimal.infinity(false).toBigDecimal());
        assertThrows(ArithmeticException.class, () -> Decimal.parse("NaN").toBigDecimal());
    }

    @Test
    void equalityIsRepresentational() {
        assertNotEquals(Decimal.parse("1.0"), Decimal.parse("1.00"));
        assertNotEquals(Decimal.parse("0"), Decimal.parse("-0"));
        assertNotEquals(Decimal.parse("NaN"), Decimal.parse("sNaN"));
        assertEquals(Decimal.parse("1.0").hashCode(), Decimal.parse("10E-1").hashCode());
        assertEquals(Decimal.parse("1.0"), Decimal.parse("10E-1"));
    }

    @Test
    void factoriesRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> Decimal.finite(false, BigInteger.valueOf(-1L), 0));
        assertThrows(NullPointerException.class, () -> Decimal.finite(false, null, 0));
        assertThrows(IllegalArgumentException.class, () -> Decimal.nan(false, false, BigInteger.valueOf(-1L)));
        assertThrows(NullPointerException.class, () -> Decimal.valueOf(null));
    }
}
