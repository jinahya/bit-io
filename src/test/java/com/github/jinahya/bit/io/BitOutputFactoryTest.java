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
import java.io.UncheckedIOException;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BitOutputFactoryTest {


    @Test(expectedExceptions = NullPointerException.class)
    public static void newInstanceWithNullByteOutput() {

        BitOutputFactory.newInstance((ByteOutput) null);
    }


    @Test
    public static void newInstanceWithByteOutput() {

        final BitOutput output = BitOutputFactory.newInstance((v) -> {
        });

        try {
            BitOutputTest.test(output);
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }


    @Test(expectedExceptions = NullPointerException.class)
    public static void newInstanceWithNullSupplier() {

        BitOutputFactory.newInstance((Supplier<? extends ByteOutput>) null);
    }


    @Test
    public static void newInstanceWithSupplier() {

        final BitOutput output = BitOutputFactory.newInstance(() -> v -> {
        });

        try {
            BitOutputTest.test(output);
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }


    @Test(expectedExceptions = NullPointerException.class)
    public static void newInstanceWithNullOperator() {

        BitOutputFactory.newInstance((UnaryOperator<ByteOutput>) null);
    }


    @Test
    public static void newInstanceWithOperator() {

        final BitOutput output = BitOutputFactory.newInstance(
            o -> o != null ? o : v -> {
                }
        );

        try {
            BitOutputTest.test(output);
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }


    @Test(expectedExceptions = NullPointerException.class)
    public static void newInstanceWithNullSupplierConsumer() {

        BitOutputFactory.newInstance(
            (Supplier<?>) null,
            (t, i) -> {
            });
    }


    @Test(expectedExceptions = NullPointerException.class)
    public static void newInstanceWithSupplierNullConsumer() {

        BitOutputFactory.newInstance(() -> null, null);
    }


    @Test
    public static void newInstanceWithSupplierConsumer() {

        final BitOutput output = BitOutputFactory.newInstance(
            () -> new Object(),
            (b, v) -> {
            }
        );

        try {
            BitOutputTest.test(output);
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }


    @Test(expectedExceptions = NullPointerException.class)
    public static void newInstanceWithNullOperatorConsumer() {

        BitOutputFactory.newInstance(
            (UnaryOperator<?>) null,
            (t, i) -> {
            });
    }


    @Test(expectedExceptions = NullPointerException.class)
    public static void newInstanceWithOperatorNullConsumer() {

        BitOutputFactory.newInstance(
            (t) -> t,
            null);
    }


    @Test
    public static void newInstanceWithOperatorConsumer() {

        final BitOutput output = BitOutputFactory.newInstance(
            (t) -> null,
            (t, i) -> {
            });

        try {
            BitOutputTest.test(output);
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

}

