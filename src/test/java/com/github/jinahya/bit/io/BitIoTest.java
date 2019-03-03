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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import static com.github.jinahya.bit.io.BitIoTests.randomSizeValueByte;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeValueInt;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeValueLong;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeValueShort;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class BitIoTest {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------
    static final int BOUND_COUNT = 1024;

    static {
        assert BOUND_COUNT > 1;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static Arguments array() {
        final byte[] array = new byte[BOUND_COUNT * Long.BYTES];
        final ByteOutput target = new ArrayByteOutput(array, 0, array.length);
        final BitOutput output = new DefaultBitOutput<>(target);
        final Supplier<BitInput> inputSupplier = () -> {
            final ByteInput source = new ArrayByteInput(array, 0, array.length);
            return new DefaultBitInput<>(source);
        };
        return Arguments.of(output, inputSupplier);
    }

    private static Arguments buffer() {
        final ByteBuffer buffer = ByteBuffer.allocate(BOUND_COUNT * Long.BYTES);
        final ByteOutput delegate = new BufferByteOutput<>(buffer);
        final BitOutput output = new DefaultBitOutput<>(delegate);
        final Supplier<BitInput> inputSupplier = () -> {
            final ByteInput source = new BufferByteInput<>((ByteBuffer) buffer.flip());
            return new DefaultBitInput<>(source);
        };
        return Arguments.of(output, inputSupplier);
    }

    private static Arguments data() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(BOUND_COUNT * Long.BYTES);
        final DataOutput target = new DataOutputStream(baos);
        final BitOutput output = new DefaultBitOutput<>(new DataByteOutput<DataOutput>(target));
        final Supplier<BitInput> inputSupplier = () -> {
            final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            final DataInput source = new DataInputStream(bais);
            return new DefaultBitInput<ByteInput>(new DataByteInput<>(source));
        };
        return Arguments.of(output, inputSupplier);
    }

    private static Arguments stream() {
        final ByteArrayOutputStream target = new ByteArrayOutputStream(BOUND_COUNT * Long.BYTES);
        final BitOutput output = new DefaultBitOutput<ByteOutput>(new StreamByteOutput<>(target));
        final Supplier<BitInput> inputSupplier = () -> {
            final ByteArrayInputStream source = new ByteArrayInputStream(target.toByteArray());
            return new DefaultBitInput<ByteInput>(new StreamByteInput<>(source));
        };
        return Arguments.of(output, inputSupplier);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Object[] source() {
        return new Object[] {
                array(),
                buffer(),
                data(),
                stream()
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"source"})
    @ParameterizedTest
    void random(final BitOutput output, final Supplier<BitInput> inputSupplier) throws IOException {
        final List<Object> list = new LinkedList<>();
        final int count = current().nextInt(BOUND_COUNT);
        for (int i = 0; i < count; i++) {
            switch (current().nextInt(4)) {
                case 0:
                    list.add(0);
                    randomSizeValueByte((pair, value) -> {
                        final boolean unsigned = pair.getKey();
                        final int size = pair.getValue();
                        list.add(unsigned);
                        list.add(size);
                        list.add(value);
                        try {
                            output.writeByte(unsigned, size, value);
                        } catch (final IOException ioe) {
                            fail(ioe);
                        }
                    });
                    break;
                case 1:
                    list.add(1);
                    randomSizeValueShort((pair, value) -> {
                        final boolean unsigned = pair.getKey();
                        final int size = pair.getValue();
                        list.add(unsigned);
                        list.add(size);
                        list.add(value);
                        try {
                            output.writeShort(unsigned, size, value);
                        } catch (final IOException ioe) {
                            fail(ioe);
                        }
                    });
                    break;
                case 2:
                    list.add(2);
                    randomSizeValueInt((pair, value) -> {
                        final boolean unsigned = pair.getKey();
                        final int size = pair.getValue();
                        list.add(unsigned);
                        list.add(size);
                        list.add(value);
                        try {
                            output.writeInt(unsigned, size, value);
                        } catch (final IOException ioe) {
                            fail(ioe);
                        }
                    });
                    break;
                default:
                    list.add(3);
                    randomSizeValueLong((pair, value) -> {
                        final boolean unsigned = pair.getKey();
                        final int size = pair.getValue();
                        list.add(unsigned);
                        list.add(size);
                        list.add(value);
                        try {
                            output.writeLong(unsigned, size, value);
                        } catch (final IOException ioe) {
                            fail(ioe);
                        }
                    });
                    break;
            }
        }
        output.align(1);
        final BitInput input = inputSupplier.get();
        for (int i = 0; i < count; i++) {
            final int type = (Integer) list.remove(0);
            final boolean unsigned = (Boolean) list.remove(0);
            final int size = (Integer) list.remove(0);
            switch (type) {
                case 0: {
                    final byte expected = (Byte) list.remove(0);
                    final byte actual = input.readByte(unsigned, size);
                    assertEquals(expected, actual);
                }
                break;
                case 1: {
                    final short expected = (Short) list.remove(0);
                    final short actual = input.readShort(unsigned, size);
                    assertEquals(expected, actual);
                }
                break;
                case 2: {
                    final int expected = (Integer) list.remove(0);
                    final int actual = input.readInt(unsigned, size);
                    assertEquals(expected, actual);
                }
                break;
                default: {
                    final long expected = (Long) list.remove(0);
                    final long actual = input.readLong(unsigned, size);
                    assertEquals(expected, actual);
                }
                break;
            }
        }
        input.align(1);
    }

    @MethodSource({"source"})
    @ParameterizedTest
    void testInt(final BitOutput output, final Supplier<BitInput> inputSupplier) throws IOException {
        output.writeInt(true, Integer.SIZE - 1, 0);
        output.writeInt(true, Integer.SIZE - 1, 1);
        output.writeInt(true, Integer.SIZE - 1, Integer.MAX_VALUE);
        output.writeInt(false, Integer.SIZE, -1);
        output.writeInt(false, Integer.SIZE, 0);
        output.writeInt(false, Integer.SIZE, Integer.MIN_VALUE);
        output.align(1);
        final BitInput input = inputSupplier.get();
        assertEquals(0, input.readInt(true, Integer.SIZE - 1));
        assertEquals(1, input.readInt(true, Integer.SIZE - 1));
        assertEquals(Integer.MAX_VALUE, input.readInt(true, Integer.SIZE - 1));
        assertEquals(-1, input.readInt(false, Integer.SIZE));
        assertEquals(0, input.readInt(false, Integer.SIZE));
        assertEquals(Integer.MIN_VALUE, input.readInt(false, Integer.SIZE));
        input.align(1);
    }

    @MethodSource({"source"})
    @ParameterizedTest
    void testIntRandom(final BitOutput output, final Supplier<BitInput> inputSupplier) throws IOException {
        final List<Object> list = new LinkedList<>();
        final int count = current().nextInt(BOUND_COUNT);
        for (int i = 0; i < count; i++) {
            randomSizeValueInt((pair, value) -> {
                final boolean unsigned = pair.getKey();
                final int size = pair.getValue();
                list.add(unsigned);
                list.add(size);
                list.add(value);
                try {
                    output.writeInt(unsigned, size, value);
                } catch (final IOException ioe) {
                    fail(ioe);
                }
            });
        }
        output.align(1);
        final BitInput input = inputSupplier.get();
        for (int i = 0; i < count; i++) {
            final boolean unsigned = (Boolean) list.remove(0);
            final int size = (Integer) list.remove(0);
            final int expected = (Integer) list.remove(0);
            final int actual = input.readInt(unsigned, size);
            assertEquals(expected, actual);
        }
        input.align(1);
    }

    @MethodSource({"source"})
    @ParameterizedTest
    void testLong(final BitOutput output, final Supplier<BitInput> inputSupplier) throws IOException {
        output.writeLong(true, Long.SIZE - 1, 0L);
        output.writeLong(true, Long.SIZE - 1, 1L);
        output.writeLong(true, Long.SIZE - 1, Long.MAX_VALUE);
        output.writeLong(false, Long.SIZE, -1L);
        output.writeLong(false, Long.SIZE, 0L);
        output.writeLong(false, Long.SIZE, Long.MIN_VALUE);
        output.align(1);
        final BitInput input = inputSupplier.get();
        assertEquals(0L, input.readLong(true, Long.SIZE - 1));
        assertEquals(1L, input.readLong(true, Long.SIZE - 1));
        assertEquals(Long.MAX_VALUE, input.readLong(true, Long.SIZE - 1));
        assertEquals(-1L, input.readLong(false, Long.SIZE));
        assertEquals(0L, input.readLong(false, Long.SIZE));
        assertEquals(Long.MIN_VALUE, input.readLong(false, Long.SIZE));
        input.align(1);
    }

    @MethodSource({"source"})
    @ParameterizedTest
    void testLongRandom(final BitOutput output, final Supplier<BitInput> inputSupplier) throws IOException {
        final List<Object> list = new LinkedList<>();
        final int count = current().nextInt(BOUND_COUNT);
        for (int i = 0; i < count; i++) {
            randomSizeValueLong((pair, value) -> {
                final boolean unsigned = pair.getKey();
                final int size = pair.getValue();
                list.add(unsigned);
                list.add(size);
                list.add(value);
                try {
                    output.writeLong(unsigned, size, value);
                } catch (final IOException ioe) {
                    fail(ioe);
                }
            });
        }
        output.align(1);
        final BitInput input = inputSupplier.get();
        for (int i = 0; i < count; i++) {
            final boolean unsigned = (Boolean) list.remove(0);
            final int size = (Integer) list.remove(0);
            final long expected = (Long) list.remove(0);
            final long actual = input.readLong(unsigned, size);
            assertEquals(expected, actual);
        }
        input.align(1);
    }
}
