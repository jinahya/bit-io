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

import java.io.IOException;
import java.nio.charset.Charset;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeShort;

public class ExtendedBitOutput {

    // -----------------------------------------------------------------------------------------------------------------
    public static <T extends BitWritable> void writeObject(final BitOutput output, final T value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        value.write(output);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static int writeBytes(final BitOutput bitOutput, final int lengthSize, final boolean byteUnsigned,
                                 final int byteSize, final byte[] value)
            throws IOException {
        if (bitOutput == null) {
            throw new NullPointerException("bitOutput is null");
        }
        requireValidSizeInt(true, lengthSize);
        requireValidSizeByte(byteUnsigned, byteSize);
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        final int length = value.length & ((1 << lengthSize) - 1);
        bitOutput.writeInt(true, lengthSize, length);
        for (int i = 0; i < length; i++) {
            bitOutput.writeByte(byteUnsigned, byteSize, value[i]);
        }
        return length;
    }

    public static int writeBytes(final BitOutput bitOutput, final int lengthSize, final int byteSize,
                                 final byte[] value)
            throws IOException {
        return writeBytes(bitOutput, lengthSize, false, byteSize, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static int writeShorts(final BitOutput bitOutput, final int lengthSize, final int shortSize,
                                  final short[] value)
            throws IOException {
        if (bitOutput == null) {
            throw new NullPointerException("bitOutput is null");
        }
        requireValidSizeInt(true, lengthSize);
        requireValidSizeShort(false, shortSize);
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        final int length = value.length & ((1 << lengthSize) - 1);
        bitOutput.writeInt(true, lengthSize, length);
        for (int i = 0; i < length; i++) {
            bitOutput.writeShort(false, shortSize, value[i]);
        }
        return length;
    }

    public static int writeInts(final BitOutput bitOutput, final int lengthSize, final int intSize, final int[] value)
            throws IOException {
        if (bitOutput == null) {
            throw new NullPointerException("bitOutput is null");
        }
        requireValidSizeInt(true, lengthSize);
        requireValidSizeInt(false, intSize);
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        final int length = value.length & ((1 << lengthSize) - 1);
        bitOutput.writeInt(true, lengthSize, length);
        for (int i = 0; i < length; i++) {
            bitOutput.writeInt(false, intSize, value[i]);
        }
        return length;
    }

    public static int writeLongs(final BitOutput bitOutput, final int lengthSize, final int longSize,
                                 final long[] value)
            throws IOException {
        if (bitOutput == null) {
            throw new NullPointerException("bitOutput is null");
        }
        requireValidSizeInt(true, lengthSize);
        requireValidSizeLong(false, longSize);
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        final int length = value.length & ((1 << lengthSize) - 1);
        bitOutput.writeInt(true, lengthSize, length);
        for (int i = 0; i < length; i++) {
            bitOutput.writeLong(false, longSize, value[i]);
        }
        return length;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static int writeString(final BitOutput bitOutput, final int lengthSize, final String value,
                                  final String charsetName)
            throws IOException {
        return writeBytes(bitOutput, lengthSize, Byte.SIZE, value.getBytes(charsetName));
    }

    public static int writeString(final BitOutput bitOutput, final int lengthSize, final String value,
                                  final Charset charset)
            throws IOException {
        return writeBytes(bitOutput, lengthSize, Byte.SIZE, value.getBytes(charset));
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static int writeAscii(final BitOutput bitOutput, final int lengthSize, final String value)
            throws IOException {
        return writeBytes(bitOutput, lengthSize, true, 7, value.getBytes("ASCII"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected ExtendedBitOutput() {
        super();
    }
}
