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
import java.util.List;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
enum BitType {

    BOOLEAN() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            final boolean value = input.readBoolean();

            return value;
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final boolean value = current().nextBoolean();
            output.writeBoolean(value);

            return value;
        }

    },
    BYTE() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            final boolean unsigned = (boolean) params.remove(0);
            final int size = (int) params.remove(0);
            final byte value = input.readByte(unsigned, size);

            return value;
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final boolean unsigned = current().nextBoolean();
            final int size = BitIoRandoms.size(unsigned, 3);
            final byte value = (byte) BitIoRandoms.value(unsigned, 3, size);
            params.add(unsigned);
            params.add(size);
            output.writeByte(unsigned, size, value);

            return value;
        }

    },
    SHORT() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            final boolean unsigned = (boolean) params.remove(0);
            final int size = (int) params.remove(0);
            final short value = input.readShort(unsigned, size);

            return value;
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final boolean u = current().nextBoolean();
            final int e = 4;
            final int size = BitIoRandoms.size(u, e);
            final short value = (short) BitIoRandoms.value(u, e, size);
            params.add(u);
            params.add(size);
            output.writeShort(u, size, value);

            return value;
        }

    },
    INT() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            final boolean unsigned = (boolean) params.remove(0);
            final int size = (int) params.remove(0);
            final int value = input.readInt(unsigned, size);

            return value;
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final boolean unsigned = current().nextBoolean();
            final int size = BitIoRandoms.size(unsigned, 5);
            final int value = (int) BitIoRandoms.value(unsigned, 5, size);
            params.add(unsigned);
            params.add(size);
            output.writeInt(unsigned, size, value);

            return value;
        }

    },
    LONG() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            final boolean unsigned = (boolean) params.remove(0);
            final int size = (int) params.remove(0);
            final long value = input.readLong(unsigned, size);

            return value;
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final boolean unsigned = current().nextBoolean();
            final int size = BitIoRandoms.size(unsigned, 6);
            final long value = BitIoRandoms.value(unsigned, 6, size);
            params.add(unsigned);
            params.add(size);
            output.writeLong(unsigned, size, value);

            return value;
        }

    },
    CHAR() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            final int size = (int) params.remove(0);
            final char value = input.readChar(size);

            return value;
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final boolean u = true;
            final int e = 4;
            final int size = BitIoRandoms.size(u, e);
            final char value = (char) BitIoRandoms.value(u, e, size);
            params.add(size);
            output.writeChar(size, value);

            return value;
        }

    },
    FLOAT() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            final float value = input.readFloat();

            return value;
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final float value = current().doubles(
                1L, -Float.MAX_VALUE, Math.nextUp((double) Float.MAX_VALUE))
                .iterator().next().floatValue();
            output.writeFloat(value);

            return value;
        }

    },
    DOUBLE() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            final double value = input.readDouble();

            return value;
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final double value = current().doubles(
                1L, -Float.MAX_VALUE, Math.nextUp((double) Float.MAX_VALUE))
                .iterator().next();
            output.writeDouble(value);

            return value;
        }

    };


    private static final Logger logger
        = LoggerFactory.getLogger(BitType.class);


    abstract Object read(List<Object> params, BitInput input)
        throws IOException;


    abstract Object write(List<Object> params, BitOutput output)
        throws IOException;

}

