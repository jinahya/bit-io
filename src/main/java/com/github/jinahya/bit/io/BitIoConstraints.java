/*
 *  Copyright 2010 Jin Kwon.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package com.github.jinahya.bit.io;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A class for constraints.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class BitIoConstraints {


    static int requireValidUnsigned8Size(final int size) {

        if (size < BitIoConstants.U8_SIZE_MIN) {
            throw new IllegalArgumentException(
                "size(" + size + ") < " + BitIoConstants.U8_SIZE_MIN);
        }

        if (size > BitIoConstants.U8_SIZE_MAX) {
            throw new IllegalArgumentException(
                "size(" + size + ") > " + BitIoConstants.U8_SIZE_MAX);
        }

        return size;
    }


//    static int requireValidUnsigned8Value(final int size, final int value) {
//
//        requireValidUnsigned8Size(size);
//
//        if (value < 0) {
//            throw new IllegalArgumentException("value(" + value + ") < 0");
//        }
//
//        final int shifted = value >> size;
//        if (shifted > 0) {
//            throw new IllegalArgumentException(
//                shifted + "(" + value + " >> " + size + ") > 0");
//        }
//
//        return value;
//    }
    static int requireValidUnsigned16Size(final int size) {

        if (size < BitIoConstants.U16_SIZE_MIN) {
            throw new IllegalArgumentException(
                "size(" + size + ") < " + BitIoConstants.U8_SIZE_MIN);
        }

        if (size > BitIoConstants.U16_SIZE_MAX) {
            throw new IllegalArgumentException(
                "size(" + size + ") > " + BitIoConstants.U8_SIZE_MAX);
        }

        return size;
    }


//    static int requireValidUnsigned16Value(final int size, final int value) {
//
//        requireValidUnsigned16Size(size);
//
//        if (value < 0) {
//            throw new IllegalArgumentException("value(" + value + ") < 0");
//        }
//
//        final int shifted = value >> size;
//        if (shifted > 0) {
//            throw new IllegalArgumentException(
//                shifted + "(" + value + " >> " + size + ") > 0");
//        }
//
//        return value;
//    }
//    // ----------------------------------------------------------- unsigned byte
//    @Deprecated
//    public static int requireValidUnsignedByteSize(final int size) {
//
//        if (size < BitIoConstants.UBYTE_SIZE_MIN) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") < " + BitIoConstants.UBYTE_SIZE_MIN);
//        }
//
//        if (size > BitIoConstants.UBYTE_SIZE_MAX) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") > " + BitIoConstants.UBYTE_SIZE_MAX);
//        }
//
//        return size;
//    }
//
//
//    @Deprecated
//    public static int requireValidUnsignedByteValue(final int size,
//                                                    final byte value) {
//
//        requireValidUnsignedByteSize(size);
//
//        if (value < 0) {
//            throw new IllegalArgumentException("value(" + value + ") < 0");
//        }
//
//        final int shifted = value >> size;
//        if (shifted > 0) {
//            throw new IllegalArgumentException(
//                shifted + "(" + value + " >> " + size + ") > 0");
//        }
//
//        return value;
//    }
//
//
//    // -------------------------------------------------------------------- byte
//    @Deprecated
//    public static int requireValidByteSize(final int size) {
//
//        if (size < BitIoConstants.BYTE_SIZE_MIN) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") < " + BitIoConstants.BYTE_SIZE_MIN);
//        }
//
//        if (size > BitIoConstants.BYTE_SIZE_MIN) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") > " + BitIoConstants.BYTE_SIZE_MIN);
//        }
//
//        return size;
//    }
//
//
//    @Deprecated
//    public static int requireValidByteValue(final int size, final byte value) {
//
//        requireValidUnsignedByteSize(size);
//
////        if (size == BitIoConstants.BYTE_SIZE_MAX) {
////            return value;
////        }
//        final int shifted = value >> size;
//        if (value < 0) {
//            if (shifted != -1) {
//                throw new IllegalArgumentException(
//                    shifted + "(" + value + " >> " + size + ") != -1");
//            }
//        } else if (shifted != 0) {
//            throw new IllegalArgumentException(
//                shifted + "(" + value + " >> " + size + ") != 0");
//        }
//
//        return value;
//    }
//
//
//    // ---------------------------------------------------------- unsigned short
//    @Deprecated
//    public static int requireValidUnsignedShortSize(final int size) {
//
//        if (size < BitIoConstants.USHORT_SIZE_MIN) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") < " + BitIoConstants.USHORT_SIZE_MIN);
//        }
//
//        if (size > BitIoConstants.USHORT_SIZE_MAX) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") > " + BitIoConstants.USHORT_SIZE_MAX);
//        }
//
//        return size;
//    }
//
//
//    @Deprecated
//    public static int requireValidUnsignedShortValue(final int size,
//                                                     final short value) {
//
//        requireValidUnsignedShortSize(size);
//
//        if (value < 0) {
//            throw new IllegalArgumentException("value(" + value + ") < 0");
//        }
//
//        final int shifted = value >> size;
//        if (shifted > 0) {
//            throw new IllegalArgumentException(
//                shifted + "(" + value + " >> " + size + ") < 0");
//        }
//
//        return value;
//    }
//
//
//    // ------------------------------------------------------------------- short
//    @Deprecated
//    public static int requireValidShortSize(final int size) {
//
//        if (size < BitIoConstants.SHORT_SIZE_MIN) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") < " + BitIoConstants.SHORT_SIZE_MIN);
//        }
//
//        if (size > BitIoConstants.SHORT_SIZE_MAX) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") > " + BitIoConstants.SHORT_SIZE_MAX);
//        }
//
//        return size;
//    }
//
//
//    @Deprecated
//    public static int requireValidShortValue(final int size,
//                                             final short value) {
//
//        requireValidShortSize(size);
//
////        if (size == BitIoConstants.SHORT_SIZE_MAX) {
////            return value;
////        }
//        final int shifted = value >> size;
//        if (value < 0) {
//            if (shifted != -1) {
//                throw new IllegalArgumentException(
//                    shifted + "(" + value + " >> " + size + ") != -1");
//            }
//        } else if (shifted != 0) {
//            throw new IllegalArgumentException(
//                shifted + "(" + value + " >> " + size + ") != 0");
//        }
//
//        return value;
//    }
//    // -------------------------------------------------------------------- char
//    public static int requireValidCharSize(final int size) {
//
//        if (size < BitIoConstants.CHAR_SIZE_MIN) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") < " + BitIoConstants.CHAR_SIZE_MIN);
//        }
//
//        if (size > BitIoConstants.CHAR_SIZE_MAX) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") > " + BitIoConstants.CHAR_SIZE_MAX);
//        }
//
//        return size;
//    }
//    // ------------------------------------------------------------ unsigned int
//    @Deprecated
//    public static int requireValidUnsignedIntSize(final int size) {
//
//        if (size < BitIoConstants.UINT_SIZE_MIN) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") < " + BitIoConstants.UINT_SIZE_MIN);
//        }
//
//        if (size > BitIoConstants.UINT_SIZE_MAX) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") > " + BitIoConstants.UINT_SIZE_MAX);
//        }
//
//        return size;
//    }
//    @Deprecated
//    public static int requireValidUnsignedIntValue(final int size,
//                                                   final int value) {
//
//        if (value < 0) {
//            throw new IllegalArgumentException("value(" + value + ") < 0");
//        }
//
//        requireValidUnsignedIntSize(size);
//
//        final int shifted = value >> size;
//        if (shifted > 0) {
//            throw new IllegalArgumentException(
//                shifted + "(" + value + " >> " + size + ") > 0");
//        }
//
//        return value;
//    }
//
//
//    // --------------------------------------------------------------------- int
//    @Deprecated
//    public static int requireValidIntSize(final int size) {
//
//        if (size < BitIoConstants.INT_SIZE_MIN) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") < " + BitIoConstants.INT_SIZE_MIN);
//        }
//
//        if (size > BitIoConstants.INT_SIZE_MAX) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") > " + BitIoConstants.INT_SIZE_MAX);
//        }
//
//        return size;
//    }
//
//
//    @Deprecated
//    static int requireValidIntValue(final int size, final int value) {
//
//        requireValidIntSize(size);
//
//        if (size == BitIoConstants.INT_SIZE_MAX) {
//            return value;
//        }
//
//        final int shifted = value >> size;
//        if (value < 0) {
//            if (shifted != -1) {
//                throw new IllegalArgumentException(
//                    shifted + "(" + value + " >> " + size + ") != -1");
//            }
//        } else if (shifted != 0) {
//            throw new IllegalArgumentException(
//                shifted + "(" + value + " >> " + size + ") != 0");
//        }
//
//        return value;
//    }
//
//
//    // ----------------------------------------------------------- unsigned long
//    @Deprecated
//    public static int requireValidUnsignedLongSize(final int size) {
//
//        if (size < BitIoConstants.ULONG_SIZE_MIN) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") < " + BitIoConstants.ULONG_SIZE_MIN);
//        }
//
//        if (size > BitIoConstants.ULONG_SIZE_MAX) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") > " + BitIoConstants.ULONG_SIZE_MAX);
//        }
//
//        return size;
//    }
//
//
//    @Deprecated
//    public static long requireValidUnsignedLongValue(final int size,
//                                                     final long value) {
//
//        requireValidUnsignedLongSize(size);
//
//        if (value < 0L) {
//            throw new IllegalArgumentException("value(" + value + ") < 0L");
//        }
//
//        final long shifted = value >> size;
//        if (shifted > 0L) {
//            throw new IllegalArgumentException(
//                shifted + "(" + value + " >> " + size + ") > 0");
//        }
//
//        return value;
//    }
//
//
//    // -------------------------------------------------------------------- long
//    @Deprecated
//    static int requireValidLongSize(final int size) {
//
//        if (size < BitIoConstants.LONG_SIZE_MIN) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") < " + BitIoConstants.LONG_SIZE_MIN);
//        }
//
//        if (size > BitIoConstants.LONG_SIZE_MAX) {
//            throw new IllegalArgumentException(
//                "size(" + size + ") > " + BitIoConstants.LONG_SIZE_MAX);
//        }
//
//        return size;
//    }
//
//
//    @Deprecated
//    static long requireValidLongValue(final int size, final long value) {
//
//        requireValidLongSize(size);
//
//        if (size == BitIoConstants.LONG_SIZE_MAX) {
//            return value;
//        }
//
//        final long shifted = value >> size;
//        if (value < 0) {
//            if (shifted != -1L) {
//                throw new IllegalArgumentException(
//                    shifted + "(" + value + " >> " + size + ") != -1");
//            }
//        } else if (shifted != 0L) {
//            throw new IllegalArgumentException(
//                shifted + "(" + value + " >> " + size + ") != 0");
//        }
//
//        return value;
//    }
//
//
//    // ----------------------------------------------------- array/offset/length
//    @Deprecated
//    static void requireValidArrayOffsetLength(
//        final Object array, final int offset, final int length) {
//
//        if (array == null) {
//            throw new NullPointerException("null array");
//        }
//
//        if (!array.getClass().isArray()) {
//            throw new IllegalArgumentException(
//                "array(" + array + ") is not an array");
//        }
//
//        if (offset < 0) {
//            throw new IndexOutOfBoundsException("offset(" + offset + ") < 0");
//        }
//
//        if (length < 0) {
//            throw new IndexOutOfBoundsException("length(" + length + ") < 0");
//        }
//
//        final int arrayLength = Array.getLength(array);
//
//        final int limit = offset + length;
//        if (limit > arrayLength) {
//            throw new IndexOutOfBoundsException(
//                limit + "(" + offset + " + " + length + ") > array.length("
//                + arrayLength + ")");
//        }
//    }
    // -------------------------------------------------------------------- size
    private static final int MIN_EXPONENT = 3;


    private static final int MAX_EXPONENT = 6;


    private static final List<Integer> MAXES;


    static {
        final List<Integer> maxes = new ArrayList<Integer>(4);
        for (int i = MIN_EXPONENT; i <= MAX_EXPONENT; i++) {
            maxes.add((int) Math.pow(2, i));
        }
        MAXES = Collections.unmodifiableList(maxes);
    }


    public static int requireValidSize(final boolean unsigned,
                                       final int exponent, final int size) {

        if (exponent < MIN_EXPONENT) {
            throw new IllegalArgumentException(
                "exponent(" + exponent + ") < " + MIN_EXPONENT);
        }

        if (exponent > MAX_EXPONENT) {
            throw new IllegalArgumentException(
                "exponent(" + exponent + ") > " + MAX_EXPONENT);
        }

        final int min = unsigned ? 1 : 2;
        final int max = MAXES.get(exponent - MIN_EXPONENT) - (unsigned ? 1 : 0);

        if (size < min) {
            throw new IllegalArgumentException(
                "size(" + size + ") < min(" + min + ")");
        }

        if (size > max) {
            throw new IllegalArgumentException(
                "size(" + size + ") > max(" + max + ")");
        }

        return size;
    }


//    public static int requireValidByteSize(final boolean unsigned,
//                                           final int size) {
//
//        return requireValidSize(unsigned, 3, size);
//    }
//
//
//    public static int requireValidShortSize(final boolean unsigned,
//                                            final int size) {
//
//        return requireValidSize(unsigned, 4, size);
//    }
//
//
//    public static int requireValidIntSize(final boolean unsigned,
//                                          final int size) {
//
//        return requireValidSize(unsigned, 5, size);
//    }
//
//
//    public static int requireValidLongSize(final boolean unsigned,
//                                           final int size) {
//
//        return requireValidSize(unsigned, 6, size);
//    }
//
//
//    public static int requireValidCharSize(final int size) {
//
//        return requireValidSize(true, 4, size);
//    }
//    public static int requireValidValue(final boolean unsigned,
//                                        final int exponent, final int size,
//                                        final int value) {
//
//        requireValidSize(unsigned, exponent, size);
//
//        if (unsigned && value < 0) {
//            throw new IllegalArgumentException("value(" + value + ") < 0");
//        }
//
//        if (size == 32) {
//            return value;
//        }
//
//        final long shifted = value >> size;
//        if (unsigned) {
//            if (shifted > 0) {
//                throw new IllegalArgumentException(
//                    shifted + "(" + value + " >> " + size + ") != 0L");
//            }
//        } else if (value < 0) {
//            if (shifted != -1) {
//                throw new IllegalArgumentException(
//                    shifted + "(" + value + " >> " + size + ") != -1L");
//            }
//        } else if (shifted != 0) {
//            throw new IllegalArgumentException(
//                shifted + "(" + value + " >> " + size + ") != 0L");
//        }
//
//        return value;
//    }
//
//
//    public static long requireValidValue(final boolean unsigned,
//                                         final int exponent, final int size,
//                                         final long value) {
//
//        requireValidSize(unsigned, exponent, size);
//
//        if (unsigned && value < 0L) {
//            throw new IllegalArgumentException("value(" + value + ") < 0L");
//        }
//
//        if (size == 64) {
//            return value;
//        }
//
//        final long shifted = value >> size;
//        if (unsigned) {
//            if (shifted > 0L) {
//                throw new IllegalArgumentException(
//                    shifted + "(" + value + " >> " + size + ") != 0L");
//            }
//        } else if (value < 0L) {
//            if (shifted != -1L) {
//                throw new IllegalArgumentException(
//                    shifted + "(" + value + " >> " + size + ") != -1L");
//            }
//        } else if (shifted != 0L) {
//            throw new IllegalArgumentException(
//                shifted + "(" + value + " >> " + size + ") != 0L");
//        }
//
//        return value;
//    }
    // ------------------------------------------------------------- scale/count
    public static int requireValidScale(final int scale) {

        return requireValidSize(true, 5, scale);
    }


//    public static int requireValidCount(final int scale, final int count) {
//
//        return requireValidUnsignedIntValue(requireValidScale(scale), count);
//    }
    public BitIoConstraints() {

        super();
    }

}

