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

            return input.readUnsignedInt((int) params.remove(0));
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

            return input.readInt((int) params.remove(0));
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

    };
    //    BYTES(0, 1024, s -> {
    //          final byte[] value = new byte[s];
    //          current().nextBytes(value);
    //          return value;
    //      }) {
    //
    //        @Override
    //        Object read(final int size, final BitInput input) throws IOException {
    //            return input.readBytes(BitIoConstants.SCALE_SIZE_MAX,
    //                                   BitIoConstants.RANGE_SIZE_MAX);
    //        }
    //
    //
    //        @Override
    //        void write(final int size, final BitOutput output, final Object value)
    //            throws IOException {
    //
    //            output.writeBytes(BitIoConstants.SCALE_SIZE_MAX,
    //                              BitIoConstants.RANGE_SIZE_MAX, (byte[]) value);
    //        }
    //
    //    },
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


    abstract Object read(List<Object> params, BitInput input)
        throws IOException;


    abstract Object write(List<Object> params, BitOutput output)
        throws IOException;

}

