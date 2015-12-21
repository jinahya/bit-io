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


import java.io.IOException;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;


/**
 * A factory class for creating {@link BitOutput}s.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BitOutputFactory {


    /**
     * Creates new {@code BitOutput} instance from which specified byte output
     * consumes bytes.
     *
     * @param <T> byte output type parameter
     * @param output byte output
     *
     * @return a new {@code BitOutput} instance.
     */
    public static <T extends ByteOutput> BitOutput newInstance(final T output) {

        if (output == null) {
            throw new NullPointerException("null output");
        }

        return new AbstractBitOutput() {

            @Override
            public void writeUnsignedByte(final int value) throws IOException {

                output.writeUnsignedByte(value);
            }


            private T output;

        };
    }


    public static <T extends ByteOutput> BitOutput newInstance(
        final Supplier<? extends T> supplier) {

        if (supplier == null) {
            throw new NullPointerException("null supplier");
        }

        return new AbstractBitOutput() {

            @Override
            public void writeUnsignedByte(final int value) throws IOException {

                if (output == null) {
                    output = BitIoUtilities.get(supplier);
                }

                output.writeUnsignedByte(value);
            }


            private T output;

        };
    }


    public static <T extends ByteOutput> BitOutput newInstance(
        final UnaryOperator<T> operator) {

        if (operator == null) {
            throw new NullPointerException("null operator");
        }

        return new AbstractBitOutput() {

            @Override
            public void writeUnsignedByte(final int value) throws IOException {

                output = BitIoUtilities.apply(operator, output);

                output.writeUnsignedByte(value);
            }


            private T output;

        };
    }


    public static <T> BitOutput newInstance(
        final Supplier<? extends T> targetSupplier,
        final ObjIntConsumer<? super T> octetConsumer) {

        if (targetSupplier == null) {
            throw new NullPointerException("null targetSupplier");
        }

        if (octetConsumer == null) {
            throw new NullPointerException("null octetConsumer");
        }

        return new AbstractBitOutput() {

            @Override
            public void writeUnsignedByte(final int value) throws IOException {

                if (target == null) {
                    target = BitIoUtilities.get(targetSupplier);
                }

                try {
                    octetConsumer.accept(target, value);
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


    public static <T> BitOutput newInstance(
        final UnaryOperator<T> targetOperator,
        final ObjIntConsumer<? super T> octetConsumer) {

        if (targetOperator == null) {
            throw new NullPointerException("null targetOperator");
        }

        if (octetConsumer == null) {
            throw new NullPointerException("null octetConsumer");
        }

        return new AbstractBitOutput() {

            @Override
            public void writeUnsignedByte(final int value) throws IOException {

                target = BitIoUtilities.apply(targetOperator, target);

                try {
                    octetConsumer.accept(target, value);
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


    private BitOutputFactory() {

        super();
    }

}

