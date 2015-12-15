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


import java.io.DataInput;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;


/**
 * A factory class for creating {@link BitOutput}s.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BitInputFactory {


    public static BitInput newInstance(final ByteInput source) {

        if (source == null) {
            throw new NullPointerException("null source");
        }

        return new AbstractBitInput() {

            @Override
            public int readUnsignedByte() throws IOException {

                return source.readUnsignedByte();
            }

        };
    }


    public static BitInput newInstance(final Supplier<ByteInput> supplier) {

        if (supplier == null) {
            throw new NullPointerException("null supplier");
        }

        return new AbstractBitInput() {

            @Override
            public int readUnsignedByte() throws IOException {

                if (source == null) {
                    source = BitIoUtilities.get(supplier, IOException.class);
                }

                return source.readUnsignedByte();
            }


            private ByteInput source;

        };
    }


    public static <T> BitInput newInstance(
        final Supplier<T> sourceSupplier, final ToIntFunction<T> byteReader) {

        if (sourceSupplier == null) {
            throw new NullPointerException("null sourceSupplier");
        }

        if (byteReader == null) {
            throw new NullPointerException("null byteReader");
        }

        return new AbstractBitInput() {

            @Override
            public int readUnsignedByte() throws IOException {

                if (source == null) {
                    source = BitIoUtilities.get(
                        sourceSupplier, IOException.class);
                }

                try {
                    return byteReader.applyAsInt(source);
                } catch (final RuntimeException re) {
                    final Throwable cause = re.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                    throw re;
                }
            }


            private T source;

        };
    }


    public static BitInput newInstance(final DataInput source) {

        if (source == null) {
            throw new NullPointerException("null source");
        }

        return new AbstractBitInput() {

            @Override
            public int readUnsignedByte() throws IOException {

                return source.readUnsignedByte();
            }

        };
    }


    private BitInputFactory() {

        super();
    }

}

