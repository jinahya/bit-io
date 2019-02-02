package com.github.jinahya.bit.io;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static com.github.jinahya.bit.io.BitIoConstraints.MAX_EXPONENT;
import static com.github.jinahya.bit.io.BitIoConstraints.MIN_EXPONENT;
import static com.github.jinahya.bit.io.BitIoConstraints.MIN_SIZE;
import static com.github.jinahya.bit.io.BitIoConstraints.maxSize;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidExponent;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSize;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeUnsigned16;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeUnsigned8;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for testing {@link BitIoConstraints}.
 */
class BitIoConstraintsTest {

    // -----------------------------------------------------------------------------------------------------------------
    private static int randomExponentValid() {
        return requireValidExponent(current().nextInt(MIN_EXPONENT, MAX_EXPONENT + 1));
    }

    private static int randomExponentInvalid() {
        int exponentInvalid;
        do {
            exponentInvalid = current().nextInt();
        } while (exponentInvalid >= MIN_EXPONENT && exponentInvalid <= MAX_EXPONENT);
        return exponentInvalid;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static int randomSizeValid(final boolean unsigned, final int exponent) {
        final int maxSize = maxSize(unsigned, requireValidExponent(exponent));
        return current().nextInt(MIN_SIZE, maxSize + 1);
    }

    private static int randomSizeInvalid(final boolean unsigned, final int exponent) {
        final int maxSize = maxSize(unsigned, requireValidExponent(exponent));
        int sizeInvalid;
        do {
            sizeInvalid = current().nextInt();
        } while (sizeInvalid >= MIN_SIZE && sizeInvalid <= maxSize);
        return sizeInvalid;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testRequireValidSizeUnsigned8() {
        // negative
        assertThrows(IllegalArgumentException.class,
                () -> requireValidSizeUnsigned8(current().nextInt() | Integer.MIN_VALUE));
        // zero
        assertThrows(IllegalArgumentException.class, () -> requireValidSizeUnsigned8(0));
        // size > Byte.SIZE
        assertThrows(IllegalArgumentException.class,
                () -> requireValidSizeUnsigned8((current().nextInt() << (Byte.SIZE + 1) >>> 1)));
        // valid
        final int size = current().nextInt(1, Byte.SIZE + 1);
        assertEquals(size, requireValidSizeUnsigned8(size));
    }

    @Test
    void testRequireValidSizeUnsigned16() {
        // negative
        assertThrows(IllegalArgumentException.class,
                () -> requireValidSizeUnsigned16(current().nextInt() | Integer.MIN_VALUE));
        // zero
        assertThrows(IllegalArgumentException.class, () -> requireValidSizeUnsigned16(0));
        // size > Short.SIZE
        assertThrows(IllegalArgumentException.class,
                () -> requireValidSizeUnsigned16((current().nextInt() << (Short.SIZE + 1) >>> 1)));
        // valid
        final int size = current().nextInt(1, Short.SIZE + 1);
        assertEquals(size, requireValidSizeUnsigned16(size));
    }

    // -------------------------------------------------------------------------------------------- requireValidExponent
    @RepeatedTest(128)
    void testRequireValidExponentAssertThrowsIllegalArgumentExceptionWhenExponentIsInvalid() {
        final int exponent = randomExponentInvalid();
        assertThrows(IllegalArgumentException.class, () -> requireValidExponent(exponent));
    }

    @RepeatedTest(128)
    void testRequireValidExponent() {
        final int exponent = randomExponentValid();
        requireValidExponent(exponent);
    }

    // ------------------------------------------------------------------------------------------------ requireValidSize
    @RepeatedTest(128)
    void testRequireValidSizeAssertThrowsIllegalArgumentExceptionWhenSizeIsInvalid() {
        final boolean unsigned = current().nextBoolean();
        final int exponent = randomExponentValid();
        final int size = randomSizeInvalid(unsigned, exponent);
        assertThrows(IllegalArgumentException.class, () -> requireValidSize(unsigned, exponent, size));
    }

    @RepeatedTest(128)
    void testRequireValidSize() {
        final boolean unsigned = current().nextBoolean();
        final int exponent = randomExponentValid();
        final int size = randomSizeValid(unsigned, exponent);
        requireValidSize(unsigned, exponent, size);
    }
}
