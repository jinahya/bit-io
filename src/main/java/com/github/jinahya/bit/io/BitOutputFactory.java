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


import java.io.DataOutput;
import java.io.IOException;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;


/**
 * A factory class for creating {@link BitOutput}s.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BitOutputFactory {


    public static BitOutput newInstance(final ByteOutput target) {

        if (target == null) {
            throw new NullPointerException("null target");
        }

        return new AbstractBitOutput() {

            @Override
            public void writeUnsignedByte(final int value) throws IOException {

                target.writeUnsignedByte(value);
            }

        };
    }


    public static BitOutput newInstance(final Supplier<ByteOutput> supplier) {

        if (supplier == null) {
            throw new NullPointerException("null supplier");
        }

        return new AbstractBitOutput() {

            @Override
            public void writeUnsignedByte(final int value) throws IOException {

                if (output == null) {
                    output = BitIoUtilities.get(supplier, IOException.class);
                }

                output.writeUnsignedByte(value);
            }


            private ByteOutput output;

        };
    }


    public static <T> BitOutput newInstance(
        final Supplier<T> targetSupplier, final ObjIntConsumer<T> valueWriter) {

        if (targetSupplier == null) {
            throw new NullPointerException("null targetSupplier");
        }

        if (valueWriter == null) {
            throw new NullPointerException("null valueWriter");
        }

        return new AbstractBitOutput() {

            @Override
            public void writeUnsignedByte(final int value) throws IOException {

                if (target == null) {
                    target = BitIoUtilities.get(
                        targetSupplier, IOException.class);
                }

                try {
                    valueWriter.accept(target, value);
                } catch (final RuntimeException re) {
                    final Throwable cause = re.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                    throw re;
                }
            }


            private T target;

        };
    }


    public static BitOutput newInstance(final DataOutput target) {

        if (target == null) {
            throw new NullPointerException("null target");
        }

        return new AbstractBitOutput() {

            @Override
            public void writeUnsignedByte(final int value) throws IOException {

                target.writeByte(value);
            }

        };
    }


    private BitOutputFactory() {

        super();
    }

}

