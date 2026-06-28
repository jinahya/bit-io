package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
 * %%
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
 * #L%
 */

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilterBitReaderWriterTest {

    @Test
    void identityReaderReturnsDelegateValue() throws IOException {
        final BitInput input = bitInput(new byte[]{(byte) 0xA0});
        final Object value = new Object();

        final Object actual = FilterBitReaders.identity(new BitReader<Object>() {
            @Override
            public Object read(final BitInput actualInput) {
                assertSame(input, actualInput);
                return value;
            }
        }).read(input);

        assertSame(value, actual);
    }

    @Test
    void identityReaderRejectsNullDelegate() {
        assertThrows(NullPointerException.class, () -> FilterBitReaders.identity(null));
    }

    @Test
    void filterReaderAppliesDelegateValue() throws IOException {
        final BitReader<Integer> delegate = new BitReader<Integer>() {
            @Override
            public Integer read(final BitInput input) throws IOException {
                return input.readInt(true, 3);
            }
        };

        final String value = new FilterBitReader<String, Integer>(delegate) {
            @Override
            protected String apply(final Integer value) {
                return "[" + value + "]";
            }
        }.read(bitInput(new byte[]{(byte) 0xA0}));

        assertEquals("[5]", value);
    }

    @Test
    void filterReaderRejectsNullDelegate() {
        assertThrows(NullPointerException.class, () -> new FilterBitReader<String, Integer>(null) {
            @Override
            protected String apply(final Integer value) {
                return value.toString();
            }
        });
    }

    @Test
    void identityWriterWritesDelegateValue() throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = bitOutput(bytes);
        final Object value = new Object();

        FilterBitWriters.identity(new BitWriter<Object>() {
            @Override
            public void write(final BitOutput actualOutput, final Object actualValue) {
                assertSame(output, actualOutput);
                assertSame(value, actualValue);
            }
        }).write(output, value);
    }

    @Test
    void identityWriterRejectsNullDelegate() {
        assertThrows(NullPointerException.class, () -> FilterBitWriters.identity(null));
    }

    @Test
    void filterWriterAppliesValueForDelegate() throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = bitOutput(bytes);

        new FilterBitWriter<String, Integer>(new BitWriter<Integer>() {
            @Override
            public void write(final BitOutput output, final Integer value) throws IOException {
                output.writeInt(true, 3, value);
            }
        }) {
            @Override
            protected Integer apply(final String value) {
                return Integer.valueOf(value);
            }
        }.write(output, "5");
        output.align(1);

        assertArrayEquals(new byte[]{(byte) 0xA0}, bytes.toByteArray());
    }

    @Test
    void filterWriterRejectsNullDelegate() {
        assertThrows(NullPointerException.class, () -> new FilterBitWriter<String, Integer>(null) {
            @Override
            protected Integer apply(final String value) {
                return Integer.valueOf(value);
            }
        });
    }

    private static BitInput bitInput(final byte[] bytes) {
        return new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
    }

    private static BitOutput bitOutput(final ByteArrayOutputStream bytes) {
        return new DefaultBitOutput(new StreamByteOutput(bytes));
    }
}
