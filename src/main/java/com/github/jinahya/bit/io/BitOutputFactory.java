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
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;


/**
 * A factory class for creating {@link BitOutput}s.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BitOutputFactory {


    /**
     * Creates new instance which writes bytes to specified byte output.
     *
     * @param output the byte output
     *
     * @return a new bit output
     */
    public static BitOutput newInstance(final ByteOutput output) {

        if (output == null) {
            throw new NullPointerException("null output");
        }

        return new AbstractBitOutput() {

            @Override
            public void write(final int value) throws IOException {

                output.write(value);
            }

        };
    }


    @IgnoreJRERequirement
    public static BitOutput newInstance(
        final Supplier<? extends ByteOutput> supplier) {

        if (supplier == null) {
            throw new NullPointerException("null supplier");
        }

        return new AbstractBitOutput() {

            @Override
            public void write(final int value) throws IOException {

                if (output == null) {
                    output = BitIoUtilities.get(supplier);
                }

                output.write(value);
            }


            private ByteOutput output;

        };
    }


    @IgnoreJRERequirement
    public static <T extends ByteOutput> BitOutput newInstance(
        final UnaryOperator<T> operator) {

        if (operator == null) {
            throw new NullPointerException("null operator");
        }

        return new AbstractBitOutput() {

            @Override
            public void write(final int value) throws IOException {

                output = BitIoUtilities.apply(operator, output);

                output.write(value);
            }


            private T output;

        };
    }


    @IgnoreJRERequirement
    public static <T> BitOutput newInstance(
        final Supplier<? extends T> supplier,
        final ObjIntConsumer<? super T> consumer) {

        if (supplier == null) {
            throw new NullPointerException("null supplier");
        }

        if (consumer == null) {
            throw new NullPointerException("null consumer");
        }

        return new AbstractBitOutput() {

            @Override
            public void write(final int value) throws IOException {

                if (target == null) {
                    target = BitIoUtilities.get(supplier);
                }

                BitIoUtilities.accept(consumer, target, value);
            }


            private T target;

        };
    }


    @IgnoreJRERequirement
    public static <T> BitOutput newInstance(
        final UnaryOperator<T> operator,
        final ObjIntConsumer<? super T> consumer) {

        if (operator == null) {
            throw new NullPointerException("null operator");
        }

        if (consumer == null) {
            throw new NullPointerException("null consumer");
        }

        return new AbstractBitOutput() {

            @Override
            public void write(final int value) throws IOException {

                target = BitIoUtilities.apply(operator, target);

                BitIoUtilities.accept(consumer, target, value);
            }


            private T target;

        };
    }


    private BitOutputFactory() {

        super();
    }

}

