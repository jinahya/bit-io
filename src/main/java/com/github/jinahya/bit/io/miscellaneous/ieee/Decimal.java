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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * A decimal floating-point datum, as the decimal interchange formats encode it: a finite number
 * (<code>(-1)<sup>sign</sup> &times; coefficient &times; 10<sup>exponent</sup></code>, including a negative zero), an
 * infinity, or a quiet or signaling NaN with an optional payload.
 *
 * <p>Unlike {@link BigDecimal}, instances keep the sign of a zero, the exponent (the <em>quantum</em>) of every finite
 * value, and the special values. No arithmetic is defined; this class only carries what an encoding holds. Two
 * instances are equal when they hold the same representation, so {@code 1.0} and {@code 1.00} are not equal.</p>
 *
 * <p>{@link #toString()} and {@link #parse(String)} follow the <em>to-scientific-string</em> and <em>to-number</em>
 * conversions of the General Decimal Arithmetic specification, without any rounding.</p>
 *
 * @see <a href="https://speleotrove.com/decimal/daconvs.html">Numeric string conversions</a>
 */
public final class Decimal {

    private static final int FINITE = 0;

    private static final int INFINITE = 1;

    private static final int QUIET_NAN = 2;

    private static final int SIGNALING_NAN = 3;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a finite value of specified sign, coefficient, and exponent.
     *
     * @param negative    {@code true} for a negative value, including a negative zero.
     * @param coefficient the coefficient; must not be negative.
     * @param exponent    the exponent.
     * @return a finite value.
     * @throws NullPointerException     if {@code coefficient} is {@code null}.
     * @throws IllegalArgumentException if {@code coefficient} is negative.
     */
    public static Decimal finite(final boolean negative, final BigInteger coefficient, final int exponent) {
        return new Decimal(FINITE, negative, requireNonNegative(coefficient, "coefficient"), exponent);
    }

    /**
     * Returns an infinity of specified sign.
     *
     * @param negative {@code true} for the negative infinity; {@code false} for the positive infinity.
     * @return an infinity.
     */
    public static Decimal infinity(final boolean negative) {
        return new Decimal(INFINITE, negative, BigInteger.ZERO, 0);
    }

    /**
     * Returns a NaN of specified sign, kind, and payload.
     *
     * @param negative  the sign of the NaN.
     * @param signaling {@code true} for a signaling NaN; {@code false} for a quiet NaN.
     * @param payload   the payload (diagnostic information); must not be negative.
     * @return a NaN.
     * @throws NullPointerException     if {@code payload} is {@code null}.
     * @throws IllegalArgumentException if {@code payload} is negative.
     */
    public static Decimal nan(final boolean negative, final boolean signaling, final BigInteger payload) {
        return new Decimal(signaling ? SIGNALING_NAN : QUIET_NAN, negative, requireNonNegative(payload, "payload"), 0);
    }

    /**
     * Returns the finite value of specified big decimal, with its exponent being the negated scale of it.
     *
     * @param value the big decimal.
     * @return the finite value of {@code value}.
     * @throws NullPointerException     if {@code value} is {@code null}.
     * @throws IllegalArgumentException if the scale of {@code value} is {@link Integer#MIN_VALUE}.
     */
    public static Decimal valueOf(final BigDecimal value) {
        Objects.requireNonNull(value, "value is null");
        if (value.scale() == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("scale(" + value.scale() + ") can't be negated");
        }
        final BigInteger unscaled = value.unscaledValue();
        return finite(unscaled.signum() < 0, unscaled.abs(), -value.scale());
    }

    /**
     * Parses specified numeric string, as the <em>to-number</em> conversion does, but without rounding; e.g.,
     * {@code "1.20"}, {@code "-0"}, {@code "1E+3"}, {@code "Infinity"}, {@code "-NaN"}, {@code "sNaN12"}.
     *
     * @param string the numeric string.
     * @return the value of {@code string}.
     * @throws NullPointerException     if {@code string} is {@code null}.
     * @throws IllegalArgumentException if {@code string} is not a numeric string, or its exponent is out of the
     *                                  {@code int} range.
     */
    public static Decimal parse(final String string) {
        Objects.requireNonNull(string, "string is null");
        int i = 0;
        boolean negative = false;
        if (string.length() > 0 && (string.charAt(0) == '+' || string.charAt(0) == '-')) {
            negative = string.charAt(0) == '-';
            i++;
        }
        final String rest = string.substring(i);
        if (rest.equalsIgnoreCase("Infinity") || rest.equalsIgnoreCase("Inf")) {
            return infinity(negative);
        }
        final String lower = rest.toLowerCase();
        if (lower.startsWith("nan") || lower.startsWith("snan")) {
            final boolean signaling = lower.charAt(0) == 's';
            final String digits = rest.substring(signaling ? 4 : 3);
            if (digits.length() > 0 && !isDigits(digits)) {
                throw new IllegalArgumentException("not a numeric string: " + string);
            }
            return nan(negative, signaling, digits.length() == 0 ? BigInteger.ZERO : new BigInteger(digits));
        }
        int e = rest.length();
        for (int j = 0; j < rest.length(); j++) {
            if (rest.charAt(j) == 'e' || rest.charAt(j) == 'E') {
                e = j;
                break;
            }
        }
        final String mantissa = rest.substring(0, e);
        final int point = mantissa.indexOf('.');
        final String integral = point < 0 ? mantissa : mantissa.substring(0, point);
        final String fraction = point < 0 ? "" : mantissa.substring(point + 1);
        if ((integral.length() == 0 && fraction.length() == 0)
            || (integral.length() > 0 && !isDigits(integral))
            || (fraction.length() > 0 && !isDigits(fraction))) {
            throw new IllegalArgumentException("not a numeric string: " + string);
        }
        long exponent = 0L;
        if (e < rest.length()) {
            String digits = rest.substring(e + 1);
            boolean negativeExponent = false;
            if (digits.length() > 0 && (digits.charAt(0) == '+' || digits.charAt(0) == '-')) {
                negativeExponent = digits.charAt(0) == '-';
                digits = digits.substring(1);
            }
            if (digits.length() == 0 || !isDigits(digits)) {
                throw new IllegalArgumentException("not a numeric string: " + string);
            }
            final BigInteger value = new BigInteger(digits);
            if (value.bitLength() > Long.SIZE - 2) {
                throw new IllegalArgumentException("exponent out of range: " + string);
            }
            exponent = negativeExponent ? -value.longValue() : value.longValue();
        }
        exponent -= fraction.length();
        if (exponent < Integer.MIN_VALUE || exponent > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("exponent out of range: " + string);
        }
        return finite(negative, new BigInteger(integral + fraction), (int) exponent);
    }

    private static boolean isDigits(final String string) {
        for (int i = 0; i < string.length(); i++) {
            final char c = string.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private static BigInteger requireNonNegative(final BigInteger value, final String name) {
        Objects.requireNonNull(value, name + " is null");
        if (value.signum() < 0) {
            throw new IllegalArgumentException(name + "(" + value + ") < 0");
        }
        return value;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private Decimal(final int kind, final boolean negative, final BigInteger coefficient, final int exponent) {
        super();
        this.kind = kind;
        this.negative = negative;
        this.coefficient = coefficient;
        this.exponent = exponent;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the string representation of this value, as the <em>to-scientific-string</em> conversion does; e.g.,
     * {@code "1.20"}, {@code "-0"}, {@code "1E+3"}, {@code "-Infinity"}, {@code "NaN"}, {@code "-sNaN12"}.
     *
     * @return the string representation of this value.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        if (negative) {
            builder.append('-');
        }
        if (kind == INFINITE) {
            return builder.append("Infinity").toString();
        }
        if (kind != FINITE) {
            builder.append(kind == SIGNALING_NAN ? "sNaN" : "NaN");
            if (coefficient.signum() != 0) {
                builder.append(coefficient);
            }
            return builder.toString();
        }
        final String digits = coefficient.toString();
        final long adjusted = (long) exponent + (digits.length() - 1);
        if (exponent <= 0 && adjusted >= -6) {
            if (exponent == 0) {
                return builder.append(digits).toString();
            }
            final int point = digits.length() + exponent; // the number of integral digits
            if (point > 0) {
                return builder.append(digits, 0, point).append('.').append(digits, point, digits.length()).toString();
            }
            builder.append("0.");
            for (int i = point; i < 0; i++) {
                builder.append('0');
            }
            return builder.append(digits).toString();
        }
        builder.append(digits.charAt(0));
        if (digits.length() > 1) {
            builder.append('.').append(digits, 1, digits.length());
        }
        return builder.append('E').append(adjusted < 0L ? '-' : '+').append(Math.abs(adjusted)).toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Decimal)) {
            return false;
        }
        final Decimal that = (Decimal) obj;
        return kind == that.kind && negative == that.negative && exponent == that.exponent
               && coefficient.equals(that.coefficient);
    }

    @Override
    public int hashCode() {
        int result = kind;
        result = 31 * result + (negative ? 1 : 0);
        result = 31 * result + coefficient.hashCode();
        result = 31 * result + exponent;
        return result;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the value of this finite value as a big decimal. Note that a negative zero results in a (non-negative)
     * zero.
     *
     * @return the value of this finite value as a big decimal.
     * @throws ArithmeticException if this value is not finite, or its exponent is {@link Integer#MIN_VALUE}.
     */
    public BigDecimal toBigDecimal() {
        if (kind != FINITE) {
            throw new ArithmeticException("not finite: " + this);
        }
        if (exponent == Integer.MIN_VALUE) {
            throw new ArithmeticException("exponent(" + exponent + ") can't be negated");
        }
        return new BigDecimal(negative ? coefficient.negate() : coefficient, -exponent);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Indicates whether this value is negative; for a zero, an infinity, or a NaN, whether its sign is set.
     *
     * @return {@code true} if the sign of this value is set; {@code false} otherwise.
     */
    public boolean isNegative() {
        return negative;
    }

    /**
     * Indicates whether this value is finite.
     *
     * @return {@code true} if this value is finite; {@code false} otherwise.
     */
    public boolean isFinite() {
        return kind == FINITE;
    }

    /**
     * Indicates whether this value is an infinity.
     *
     * @return {@code true} if this value is an infinity; {@code false} otherwise.
     */
    public boolean isInfinite() {
        return kind == INFINITE;
    }

    /**
     * Indicates whether this value is a NaN, either quiet or signaling.
     *
     * @return {@code true} if this value is a NaN; {@code false} otherwise.
     */
    public boolean isNaN() {
        return kind == QUIET_NAN || kind == SIGNALING_NAN;
    }

    /**
     * Indicates whether this value is a signaling NaN.
     *
     * @return {@code true} if this value is a signaling NaN; {@code false} otherwise.
     */
    public boolean isSignaling() {
        return kind == SIGNALING_NAN;
    }

    /**
     * Returns the coefficient of this finite value, or the payload of this NaN; {@code 0} for an infinity.
     *
     * @return the coefficient, or the payload, of this value.
     */
    public BigInteger getCoefficient() {
        return coefficient;
    }

    /**
     * Returns the exponent of this finite value; {@code 0} for an infinity or a NaN.
     *
     * @return the exponent of this value.
     */
    public int getExponent() {
        return exponent;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final int kind;

    private final boolean negative;

    private final BigInteger coefficient;

    private final int exponent;
}
