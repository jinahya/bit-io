package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Assertions;
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

    /**
     * Generates a random valid value for {@code exponent}.
     *
     * @return a random valid value for {@code exponent}.
     */
    private static int randomExponentValid() {
        return requireValidExponent(current().nextInt(MIN_EXPONENT, MAX_EXPONENT + 1));
    }

    /**
     * Generates a random invalid value for {@code exponent}.
     *
     * @return a random invalid value for {@code exponent}.
     */
    private static int randomExponentInvalid() {
        int exponentInvalid;
        do {
            exponentInvalid = current().nextInt();
        } while (exponentInvalid >= MIN_EXPONENT && exponentInvalid <= MAX_EXPONENT);
        return exponentInvalid;
    }

    /**
     * Generates a random valid {@code size} for given arguments.
     *
     * @param unsigned the value for {@code unsigned} parameter.
     * @param exponent the value for {@code exponent} parameter
     * @return a random valid {@code size} for given arguments.
     */
    private static int randomSizeValid(final boolean unsigned, final int exponent) {
        final int maxSize = maxSize(unsigned, requireValidExponent(exponent));
        return current().nextInt(MIN_SIZE, maxSize + 1);
    }

    /**
     * Generates a random invalid {@code size} for given arguments.
     *
     * @param unsigned the value for {@code unsigned} parameter.
     * @param exponent the value for {@code exponent} parameter
     * @return a random invalid {@code size} for given arguments.
     */
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

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testRequireValidSize() {
        // invalid exponent
        {
            final boolean unsigned = current().nextBoolean();
            final int exponentInvalid = randomExponentInvalid();
            final int size = randomSizeValid(unsigned, randomExponentValid());
            assertThrows(IllegalArgumentException.class,
                    () -> requireValidSize(current().nextBoolean(), exponentInvalid, size));
        }
        // valid exponent
        {
            final boolean unsigned = current().nextBoolean();
            final int exponentValid = randomExponentValid();
            final int size = randomSizeValid(unsigned, exponentValid);
            Assertions.assertDoesNotThrow(() -> requireValidSize(current().nextBoolean(), exponentValid, size));
        }
        // invalid size
        {
            final boolean unsigned = current().nextBoolean();
            final int exponent = randomExponentValid();
            final int sizeInvalid = randomSizeInvalid(unsigned, exponent);
            assertThrows(IllegalArgumentException.class,
                    () -> requireValidSize(current().nextBoolean(), exponent, sizeInvalid));
        }
        // valid size
        {
            final boolean unsigned = current().nextBoolean();
            final int exponent = randomExponentValid();
            final int sizeVlid = randomSizeValid(unsigned, exponent);
            Assertions.assertDoesNotThrow(() -> requireValidSize(current().nextBoolean(), exponent, sizeVlid));
        }
    }
}
