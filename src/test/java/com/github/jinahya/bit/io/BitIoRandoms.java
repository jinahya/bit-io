/*
 * Copyright 2013 Jin Kwon <jinahya at gmail.com>.
 *
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
 */


package com.github.jinahya.bit.io;


import static java.util.concurrent.ThreadLocalRandom.current;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class BitIoRandoms {


    static boolean randomBooleanValue() {

        return current().nextBoolean();
    }


    static int randomUnsignedByteSize() {

        final int size = current().nextInt(
            BitIoConstants.UBYTE_SIZE_MIN, BitIoConstants.UBYTE_SIZE_MAX + 1);

        return BitIoConstraints.requireValidUnsignedByteSize(size);
    }


    static int randomUnsignedShortSize() {

        final int size = current().nextInt(
            BitIoConstants.USHORT_SIZE_MIN, BitIoConstants.USHORT_SIZE_MAX + 1);

        return BitIoConstraints.requireValidUnsignedShortSize(size);
    }


    static int randomUnsignedIntSize(final int max) {

        final int size = current().nextInt(
            BitIoConstants.UINT_SIZE_MIN, max + 1);

        return BitIoConstraints.requireValidUnsignedIntSize(size);
    }


    static int randomUnsignedIntSize() {

        return randomUnsignedIntSize(BitIoConstants.UINT_SIZE_MAX);
    }


    static int randomUnsignedIntValue(final int size) {

        BitIoConstraints.requireValidUnsignedIntSize(size);

        final int value = current().nextInt() >>> (Integer.SIZE - size);

        return BitIoConstraints.requireValidUnsignedIntValue(value, size);
    }


    static int randomIntSize() {

        final int size = current().nextInt(
            BitIoConstants.INT_SIZE_MIN, BitIoConstants.INT_SIZE_MAX + 1);

        return BitIoConstraints.requireValidIntSize(size);
    }


    static int randomIntValue(final int size) {

        BitIoConstraints.requireValidIntSize(size);

        final int value = current().nextInt() >> (Integer.SIZE - size);

        return BitIoConstraints.requireValidIntValue(value, size);
    }


    static int randomUnsignedLongSize() {

        final int size = current().nextInt(
            BitIoConstants.ULONG_SIZE_MIN, BitIoConstants.ULONG_SIZE_MAX + 1);

        return BitIoConstraints.requireValidUnsignedLongSize(size);
    }


    static long unsignedLongValue(final int size) {

        BitIoConstraints.requireValidUnsignedLongSize(size);

        final long value = current().nextLong() >>> (Long.SIZE - size);

        return BitIoConstraints.requireValidUnsignedLongValue(value, size);
    }


    static int randomLongSize() {

        final int size = current().nextInt(
            BitIoConstants.LONG_SIZE_MIN, BitIoConstants.LONG_SIZE_MAX + 1);

        return BitIoConstraints.requireValidLongSize(size);
    }


    static long randomLongValue(final int size) {

        BitIoConstraints.requireValidLongSize(size);

        final long value = current().nextLong() >> (Long.SIZE - size);

        return BitIoConstraints.requireValidLongValue(value, size);
    }


    private BitIoRandoms() {
        super();
    }

}

