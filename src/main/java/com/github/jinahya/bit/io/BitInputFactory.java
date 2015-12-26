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
import java.io.UncheckedIOException;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;


/**
 * A factory class for creating {@link BitOutput}s.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@IgnoreJRERequirement
public class BitInputFactory {


    /**
     * Creates new {@code BitInput} instance to which specified byte input
     * supplies bytes.
     *
     * @param <T> byte input type parameter
     * @param input byte input
     *
     * @return a new {@code BitInput} instance.
     */
    public static <T extends ByteInput> BitInput newInstance(final T input) {

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


    public static <T extends ByteInput> BitInput newInstance(
        final Supplier<? extends T> supplier) {

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


            private T input;

        };
    }


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


    public static <T> BitInput newInstance(
        final Supplier<? extends T> sourceSupplier,
        final ToIntFunction<? super T> octetSupplier) {

        if (sourceSupplier == null) {
            throw new NullPointerException("null sourceSupplier");
        }

        if (octetSupplier == null) {
            throw new NullPointerException("null octetSupplier");
        }

        return new AbstractBitInput() {

            @Override
            public int read() throws IOException {

                if (source == null) {
                    source = BitIoUtilities.get(sourceSupplier);
                }

                try {
                    return octetSupplier.applyAsInt(source);
                } catch (final UncheckedIOException uioe) {
                    throw uioe.getCause();
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


    public static <T> BitInput newInstance(
        final UnaryOperator<T> sourceOperator,
        final ToIntFunction<? super T> octetSupplier) {

        if (sourceOperator == null) {
            throw new NullPointerException("null sourceOperator");
        }

        if (octetSupplier == null) {
            throw new NullPointerException("null octetSupplier");
        }

        return new AbstractBitInput() {

            @Override
            public int read() throws IOException {

                source = BitIoUtilities.apply(sourceOperator, source);

                try {
                    return octetSupplier.applyAsInt(source);
                } catch (final UncheckedIOException uioe) {
                    throw uioe.getCause();
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


    private BitInputFactory() {

        super();
    }

}

