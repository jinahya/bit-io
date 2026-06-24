package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
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

/**
 * Constraints for bit-io.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class BitIoConstraints {

    /**
     * Checks that the specified bit size is valid for a {@code byte} value.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the bit size to check; between {@code 1} and ({@value java.lang.Byte#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeByte(final boolean unsigned, final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        if (size > Byte.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") > " + Byte.SIZE);
        }
        if (unsigned && size == Byte.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for unsigned byte");
        }
        return size;
    }

    /**
     * Checks that the specified bit size is valid for a {@code short} value.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the bit size to check; between {@code 1} and ({@value java.lang.Short#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeShort(final boolean unsigned, final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        if (size > Short.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") > " + Short.SIZE);
        }
        if (unsigned && size == Short.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for unsigned short");
        }
        return size;
    }

    /**
     * Checks that the specified bit size is valid for an {@code int} value.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the bit size to check; between {@code 1} and ({@value java.lang.Integer#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeInt(final boolean unsigned, final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        if (size > Integer.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") > " + Integer.SIZE);
        }
        if (unsigned && size == Integer.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for unsigned integer");
        }
        return size;
    }

    /**
     * Checks that the specified bit size is valid for a {@code long} value.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the bit size to check; between {@code 1} and ({@value java.lang.Long#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeLong(final boolean unsigned, final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        if (size > Long.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") > " + Long.SIZE);
        }
        if (unsigned && size == Long.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") for unsigned long");
        }
        return size;
    }

    /**
     * Checks that the specified bit size is valid for a {@code char} value.
     *
     * @param size the bit size to check; between {@code 1} and {@value java.lang.Character#SIZE}, both inclusive.
     * @return given {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     */
    static int requireValidSizeChar(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size(" + size + ") <= 0");
        }
        if (size > Character.SIZE) {
            throw new IllegalArgumentException("invalid size(" + size + ") > " + Character.SIZE);
        }
        return size;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private BitIoConstraints() {
        throw new AssertionError("instantiation is not allowed");
    }
}
