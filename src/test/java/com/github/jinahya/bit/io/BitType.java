/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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


import java.io.IOException;
import static java.util.concurrent.ThreadLocalRandom.current;
import java.util.function.Function;
import org.apache.commons.lang3.RandomStringUtils;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
enum BitType {

    BOOLEAN(1, 1, s -> current().nextBoolean()) {

        @Override
        Object read(final int size, final BitInput input) throws IOException {
            return input.readBoolean();
        }


        @Override
        void write(final int size, final BitOutput output, final Object value)
            throws IOException {

            output.writeBoolean((Boolean) value);
        }

    },
    UINT(BitIoConstants.UINT_SIZE_MIN, BitIoConstants.UINT_SIZE_MAX,
         s -> current().nextInt() >>> (Integer.SIZE - s)) {

        @Override
        Object read(final int size, final BitInput input) throws IOException {
            return input.readUnsignedInt(size);
        }


        @Override
        void write(final int length, final BitOutput output, final Object value)
            throws IOException {

            output.writeUnsignedInt(length, (Integer) value);
        }

    },
    INT(BitIoConstants.INT_SIZE_MIN, BitIoConstants.INT_SIZE_MAX,
        s -> current().nextInt() >> (Integer.SIZE - s)) {

        @Override
        Object read(final int size, final BitInput input) throws IOException {
            return input.readInt(size);
        }


        @Override
        void write(final int length, final BitOutput output, final Object value)
            throws IOException {

            output.writeInt(length, (Integer) value);
        }

    },
    ULONG(BitIoConstants.ULONG_SIZE_MIN, BitIoConstants.ULONG_SIZE_MAX,
          s -> current().nextLong() >>> (Long.SIZE - s)) {

        @Override
        Object read(final int size, final BitInput input) throws IOException {
            return input.readUnsignedLong(size);
        }


        @Override
        void write(final int size, final BitOutput output, final Object value)
            throws IOException {
            output.writeUnsignedLong(size, (Long) value);
        }

    },
    LONG(BitIoConstants.LONG_SIZE_MIN, BitIoConstants.LONG_SIZE_MAX,
         s -> current().nextLong() >> (Long.SIZE - s)) {

        @Override
        Object read(final int size, final BitInput input) throws IOException {
            return input.readLong(size);
        }


        @Override
        void write(final int size, final BitOutput output, final Object value)
            throws IOException {

            output.writeLong(size, (Long) value);
        }

    },
    BYTES(0, 1024, s -> {
          final byte[] value = new byte[s];
          current().nextBytes(value);
          return value;
      }) {

        @Override
        Object read(final int size, final BitInput input) throws IOException {
            return input.readBytes(BitIoConstants.SCALE_SIZE_MAX,
                                   BitIoConstants.RANGE_SIZE_MAX);
        }


        @Override
        void write(final int size, final BitOutput output, final Object value)
            throws IOException {

            output.writeBytes(BitIoConstants.SCALE_SIZE_MAX,
                              BitIoConstants.RANGE_SIZE_MAX, (byte[]) value);
        }

    },
    ASCII(0, 1024, s -> RandomStringUtils.randomAscii(s)) {

        @Override
        Object read(final int size, final BitInput input) throws IOException {
            return input.readAscii();
        }


        @Override
        void write(final int size, final BitOutput output, final Object value)
            throws IOException {

            output.writeAscii((String) value);
        }

    };


    private BitType(final int minLength, final int maxLength,
                       final Function<Integer, Object> generator) {

        assert minLength <= maxLength;

        this.minLength = minLength;
        this.maxLength = maxLength;
        this.generator = generator;
    }


    public int length() {

        if (minLength == maxLength) {
            return minLength;
        }

        return current().nextInt(minLength, maxLength);
    }


    public Object value(final int size) {

        return generator.apply(size);
    }


    abstract Object read(int size, BitInput input) throws IOException;


    abstract void write(int size, BitOutput output, Object value)
        throws IOException;


    private final int minLength;


    private final int maxLength;


    private final Function<Integer, Object> generator;

}

