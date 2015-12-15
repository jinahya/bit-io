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
enum BitIoType {

    BOOLEAN() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            return input.readBoolean();
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final boolean value = BitIoRandoms.randomBooleanValue();
            output.writeBoolean(value);

            return value;
        }

    },
    UINT() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            final int size = (int) params.remove(0);

            return input.readUnsignedInt(size);
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final int size = BitIoRandoms.randomUnsignedIntSize();
            final int value = BitIoRandoms.randomUnsignedIntValue(size);
            params.add(size);
            output.writeUnsignedInt(size, value);

            return value;
        }

    },
    INT() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            final int size = (int) params.remove(0);
            final int value = input.readInt(size);

            return value;
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final int size = BitIoRandoms.randomIntSize();
            final int value = BitIoRandoms.randomIntValue(size);
            params.add(size);
            output.writeInt(size, value);

            return value;
        }

    },
    ULONG() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            final int size = (int) params.remove(0);

            return input.readUnsignedLong(size);
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final int size = BitIoRandoms.randomUnsignedLongSize();
            final long value = BitIoRandoms.unsignedLongValue(size);
            params.add(size);
            output.writeUnsignedLong(size, value);

            return value;
        }

    },
    LONG() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            final int size = (int) params.remove(0);
            final long value = input.readLong(size);

            return value;
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final int size = BitIoRandoms.randomLongSize();
            final long value = BitIoRandoms.randomLongValue(size);
            params.add(size);
            output.writeLong(size, value);

            return value;
        }

    },
    FBYTES() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            final byte[] array = new byte[(int) params.remove(0)];
            final int byteSize = (int) params.remove(0);

            input.readBytes(array, 0, array.length, byteSize);

            return array;
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final byte[] array = new byte[current().nextInt(1024)];
            final int byteSize = current().nextInt(1, 9);
            params.add(array.length);
            params.add(byteSize);
            output.writeBytes(array, 0, array.length, byteSize);

            return array;
        }

    },
    VBYTES() {

        @Override
        Object read(final List<Object> params, final BitInput input)
            throws IOException {

            final byte[] array = new byte[(int) params.remove(0)];
            final int byteSize = (int) params.remove(0);

            input.readBytes(array, 0, array.length, byteSize);

            return array;
        }


        @Override
        Object write(final List<Object> params, final BitOutput output)
            throws IOException {

            final byte[] array = new byte[current().nextInt(1024)];
            final int byteSize = current().nextInt(1, 9);
            params.add(array.length);
            params.add(byteSize);
            output.writeBytes(array, 0, array.length, byteSize);

            return array;
        }

    };
//    ASCII() {
//
//        @Override
//        Object read(final int size, final BitInput input) throws IOException {
//            return input.readAscii();
//        }
//
//
//        @Override
//        void write(final List<Object> params, final BitOutput output)
//            throws IOException {
//
//            final int s = BitIoRandoms.lengthSize()
//
//            output.writeAscii((String) value);
//        }
//
//    };


    private static final Logger logger
        = LoggerFactory.getLogger(BitIoType.class);


    abstract Object read(List<Object> params, BitInput input)
        throws IOException;


    abstract Object write(List<Object> params, BitOutput output)
        throws IOException;

}

