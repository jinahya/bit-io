package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2026 Jinahya, Inc.
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

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests {@link FilterBitReaders#mapping(BitReader, java.util.function.Function)} and
 * {@link FilterBitWriters#mapping(BitWriter, java.util.function.Function)}.
 */
class FilterMappingTest {

    @Test
    void readerMapsReadValues() throws IOException {
        final BitReader<Integer> delegate = input -> 3;
        assertEquals(Integer.valueOf(7), FilterBitReaders.mapping(delegate, v -> v + 4).read(null));
    }

    @Test
    void readerMappingsCompose() throws IOException {
        final BitReader<Integer> delegate = input -> 3;
        assertEquals("7", FilterBitReaders.mapping(FilterBitReaders.mapping(delegate, v -> v + 4), String::valueOf)
                .read(null));
    }

    @Test
    void readerMappingWorksWithNullableAndIdentity() throws IOException {
        final BitReader<Integer> delegate = input -> 3;
        final BitReader<Integer> mapped = FilterBitReaders.mapping(delegate, v -> v + 4);
        assertEquals(Integer.valueOf(7), FilterBitReaders.identity(mapped).read(null));
        assertEquals(Integer.valueOf(7), BitReaders.nullable(mapped).read(BitInputs.from(new byte[]{(byte) 0x80})));
    }

    @Test
    void writerMapsValuesBeforeWriting() throws IOException {
        final AtomicReference<Integer> written = new AtomicReference<>();
        final BitWriter<Integer> delegate = (output, value) -> written.set(value);
        FilterBitWriters.<String, Integer>mapping(delegate, String::length).write(null, "four");
        assertEquals(Integer.valueOf(4), written.get());
    }

    @Test
    void writerMappingsComposeInnerClosestToDelegate() throws IOException {
        final AtomicReference<Integer> written = new AtomicReference<>();
        final BitWriter<Integer> delegate = (output, value) -> written.set(value);
        final BitWriter<String> lengths = FilterBitWriters.mapping(delegate, String::length);
        FilterBitWriters.<Object, String>mapping(lengths, Object::toString).write(null, 1234);
        assertEquals(Integer.valueOf(4), written.get());
    }

    @Test
    void writerMappingWorksWithNullableAndIdentity() throws IOException {
        final AtomicReference<Integer> written = new AtomicReference<>();
        final BitWriter<Integer> delegate = (output, value) -> written.set(value);
        final BitWriter<String> mapped = FilterBitWriters.mapping(delegate, String::length);
        FilterBitWriters.identity(mapped).write(null, "four");
        assertEquals(Integer.valueOf(4), written.get());
        BitWriters.nullable(mapped).write(BitOutputs.from(new byte[1]), "abc");
        assertEquals(Integer.valueOf(3), written.get());
    }

    @Test
    void rejectsNullArguments() {
        final BitReader<Integer> reader = input -> 3;
        final BitWriter<Integer> writer = (output, value) -> { };
        assertEquals("delegate is null", assertThrows(
                NullPointerException.class, () -> FilterBitReaders.mapping(null, v -> v)).getMessage());
        assertEquals("mapper is null", assertThrows(
                NullPointerException.class, () -> FilterBitReaders.mapping(reader, null)).getMessage());
        assertEquals("delegate is null", assertThrows(
                NullPointerException.class, () -> FilterBitWriters.mapping(null, v -> v)).getMessage());
        assertEquals("mapper is null", assertThrows(
                NullPointerException.class, () -> FilterBitWriters.mapping(writer, null)).getMessage());
    }
}
