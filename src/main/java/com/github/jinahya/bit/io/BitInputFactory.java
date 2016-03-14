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
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

/**
 * A factory class for creating {@link BitOutput}s.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BitInputFactory {

    /**
     * Creates new instance which read bytes from specified byte input.
     *
     * @param input the byte input
     *
     * @return a new instance.
     */
    public static BitInput newInstance(final ByteInput input) {

        if (input == null) {
            throw new NullPointerException("null input");
        }

        return new AbstractBitInput() {

            @Override
            public int read() throws IOException {

                return input.read();
            }

        };
    }

    @IgnoreJRERequirement
    public static BitInput newInstance(
            final Supplier<? extends ByteInput> supplier) {

        if (supplier == null) {
            throw new NullPointerException("null supplier");
        }

        return new AbstractBitInput() {

            @Override
            public int read() throws IOException {

                if (input == null) {
                    input = BitIoUtilities.get(supplier);
                }

                return input.read();
            }

            private ByteInput input;

        };
    }

    @IgnoreJRERequirement
    public static <T extends ByteInput> BitInput newInstance(
            final UnaryOperator<T> operator) {

        if (operator == null) {
            throw new NullPointerException("null operator");
        }

        return new AbstractBitInput() {

            @Override
            public int read() throws IOException {

                input = BitIoUtilities.apply(operator, input);

                return input.read();
            }

            private T input;

        };
    }

    @IgnoreJRERequirement
    public static <T> BitInput newInstance(
            final Supplier<? extends T> supplier,
            final ToIntFunction<? super T> function) {

        if (supplier == null) {
            throw new NullPointerException("null supplier");
        }

        if (function == null) {
            throw new NullPointerException("null function");
        }

        return new AbstractBitInput() {

            @Override
            public int read() throws IOException {

                if (source == null) {
                    source = BitIoUtilities.get(supplier);
                }

                return BitIoUtilities.apply(function, source);
            }

            private T source;

        };
    }

    @IgnoreJRERequirement
    public static <T> BitInput newInstance(
            final UnaryOperator<T> operator,
            final ToIntFunction<? super T> function) {

        if (operator == null) {
            throw new NullPointerException("null operator");
        }

        if (function == null) {
            throw new NullPointerException("null function");
        }

        return new AbstractBitInput() {

            @Override
            public int read() throws IOException {

                source = BitIoUtilities.apply(operator, source);

                return BitIoUtilities.apply(function, source);
            }

            private T source;

        };
    }

    private BitInputFactory() {

        super();
    }

}
