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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BitObjectContractTest {

    @Test
    void readObjectRejectsNullReader() {
        assertThrows(NullPointerException.class, () -> bitInput(new byte[]{0x00}).readObject(null));
    }

    @Test
    void writeObjectRejectsNullWriter() {
        assertThrows(NullPointerException.class,
                     () -> bitOutput(new ByteArrayOutputStream()).writeObject(null, "value"));
    }

    @Test
    void readIntRejectsNullReader() {
        assertThrows(NullPointerException.class, () -> bitInput(new byte[]{0x00}).readInt(null));
    }

    @Test
    void readLongRejectsNullReader() {
        assertThrows(NullPointerException.class, () -> bitInput(new byte[]{0x00}).readLong(null));
    }

    @Test
    void writeIntRejectsNullWriter() {
        assertThrows(NullPointerException.class,
                     () -> bitOutput(new ByteArrayOutputStream()).writeInt(null, 0));
    }

    @Test
    void writeLongRejectsNullWriter() {
        assertThrows(NullPointerException.class,
                     () -> bitOutput(new ByteArrayOutputStream()).writeLong(null, 0L));
    }

    @Test
    void readFloatRejectsNullReader() {
        assertThrows(NullPointerException.class, () -> bitInput(new byte[]{0x00}).readFloat(null));
    }

    @Test
    void readDoubleRejectsNullReader() {
        assertThrows(NullPointerException.class, () -> bitInput(new byte[]{0x00}).readDouble(null));
    }

    @Test
    void writeFloatRejectsNullWriter() {
        assertThrows(NullPointerException.class,
                     () -> bitOutput(new ByteArrayOutputStream()).writeFloat(null, 0.0F));
    }

    @Test
    void writeDoubleRejectsNullWriter() {
        assertThrows(NullPointerException.class,
                     () -> bitOutput(new ByteArrayOutputStream()).writeDouble(null, 0.0D));
    }

    void readObjectDelegatesCurrentInputAndReturnsReaderValue() throws IOException {
        final BitInput input = bitInput(new byte[]{(byte) 0xA0});

        final Integer value = input.readObject(new BitReader<Integer>() {
            @Override
            public Integer read(final BitInput actual) throws IOException {
                assertSame(input, actual);
                return actual.readUnsignedInt(3);
            }
        });

        assertEquals(5, value);
    }

    @Test
    void readIntDelegatesCurrentInputAndReturnsReaderValue() throws IOException {
        final BitInput input = bitInput(new byte[]{(byte) 0xA0});

        final int value = input.readInt(new IntBitReader() {
            @Override
            public int readInt(final BitInput actual) throws IOException {
                assertSame(input, actual);
                return actual.readUnsignedInt(3);
            }
        });

        assertEquals(5, value);
    }

    @Test
    void readLongDelegatesCurrentInputAndReturnsReaderValue() throws IOException {
        final BitInput input = bitInput(new byte[]{(byte) 0xA0});

        final long value = input.readLong(new LongBitReader() {
            @Override
            public long readLong(final BitInput actual) throws IOException {
                assertSame(input, actual);
                return actual.readUnsignedLong(3);
            }
        });

        assertEquals(5L, value);
    }

    @Test
    void readFloatDelegatesCurrentInputAndReturnsReaderValue() throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = bitOutput(bytes);
        output.writeFloat(8, 23, 1.25F);
        final BitInput input = bitInput(bytes.toByteArray());

        final float value = input.readFloat(new FloatBitReader() {
            @Override
            public float readFloat(final BitInput actual) throws IOException {
                assertSame(input, actual);
                return actual.readFloat(8, 23);
            }
        });

        assertEquals(1.25F, value);
    }

    @Test
    void readDoubleDelegatesCurrentInputAndReturnsReaderValue() throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = bitOutput(bytes);
        output.writeDouble(11, 52, 123.5D);
        final BitInput input = bitInput(bytes.toByteArray());

        final double value = input.readDouble(new DoubleBitReader() {
            @Override
            public double readDouble(final BitInput actual) throws IOException {
                assertSame(input, actual);
                return actual.readDouble(11, 52);
            }
        });

        assertEquals(123.5D, value);
    }

    @Test
    void writeObjectDelegatesCurrentOutputAndValue() throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = bitOutput(bytes);
        final Integer value = 5;

        output.writeObject(new BitWriter<Integer>() {
            @Override
            public void write(final BitOutput actual, final Integer actualValue) throws IOException {
                assertSame(output, actual);
                assertSame(value, actualValue);
                actual.writeUnsignedInt(3, actualValue);
            }
        }, value);
        output.align(1);

        assertArrayEquals(new byte[]{(byte) 0xA0}, bytes.toByteArray());
    }

    @Test
    void writeIntDelegatesCurrentOutputAndValue() throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = bitOutput(bytes);

        output.writeInt(new IntBitWriter() {
            @Override
            public void writeInt(final BitOutput actual, final int actualValue) throws IOException {
                assertSame(output, actual);
                assertEquals(5, actualValue);
                actual.writeUnsignedInt(3, actualValue);
            }
        }, 5);
        output.align(1);

        assertArrayEquals(new byte[]{(byte) 0xA0}, bytes.toByteArray());
    }

    @Test
    void writeLongDelegatesCurrentOutputAndValue() throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = bitOutput(bytes);

        output.writeLong(new LongBitWriter() {
            @Override
            public void writeLong(final BitOutput actual, final long actualValue) throws IOException {
                assertSame(output, actual);
                assertEquals(5L, actualValue);
                actual.writeUnsignedLong(3, actualValue);
            }
        }, 5L);
        output.align(1);

        assertArrayEquals(new byte[]{(byte) 0xA0}, bytes.toByteArray());
    }

    @Test
    void writeFloatDelegatesCurrentOutputAndValue() throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = bitOutput(bytes);

        output.writeFloat(new FloatBitWriter() {
            @Override
            public void writeFloat(final BitOutput actual, final float actualValue) throws IOException {
                assertSame(output, actual);
                assertEquals(1.25F, actualValue);
                actual.writeFloat(8, 23, actualValue);
            }
        }, 1.25F);

        assertEquals(1.25F, bitInput(bytes.toByteArray()).readFloat(8, 23));
    }

    @Test
    void writeDoubleDelegatesCurrentOutputAndValue() throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = bitOutput(bytes);

        output.writeDouble(new DoubleBitWriter() {
            @Override
            public void writeDouble(final BitOutput actual, final double actualValue) throws IOException {
                assertSame(output, actual);
                assertEquals(123.5D, actualValue);
                actual.writeDouble(11, 52, actualValue);
            }
        }, 123.5D);

        assertEquals(123.5D, bitInput(bytes.toByteArray()).readDouble(11, 52));
    }

    @Test
    void nullableWriterAndReaderRoundTripPresentObject() throws IOException {
        final byte[] bytes = writeSample(new Sample(true, 5));

        final Sample value = bitInput(bytes).readObject(BitReaders.nullable(sampleReader()));

        assertEquals(new Sample(true, 5), value);
    }

    @Test
    void nullableWriterAndReaderRoundTripNullObject() throws IOException {
        final byte[] bytes = writeSample(null);

        final Sample value = bitInput(bytes).readObject(BitReaders.nullable(sampleReader()));

        assertNull(value);
        assertArrayEquals(new byte[]{0x00}, bytes);
    }

    @Test
    void nullableReaderDoesNotCallDelegateForNullObject() throws IOException {
        final CountingReader delegate = new CountingReader();

        final Sample value = bitInput(new byte[]{0x00}).readObject(BitReaders.nullable(delegate));

        assertNull(value);
        assertEquals(0, delegate.count);
    }

    @Test
    void nullableWriterDoesNotCallDelegateForNullObject() throws IOException {
        final CountingWriter delegate = new CountingWriter();
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = bitOutput(bytes);

        output.writeObject(BitWriters.nullable(delegate), null);
        output.align(1);

        assertEquals(0, delegate.count);
        assertArrayEquals(new byte[]{0x00}, bytes.toByteArray());
    }

    @Test
    void nullableReaderRejectsNullDelegateAndInput() {
        assertThrows(NullPointerException.class, () -> BitReaders.nullable(null));
        assertThrows(NullPointerException.class, () -> BitReaders.nullable(sampleReader()).read(null));
    }

    @Test
    void nullableWriterRejectsNullDelegateAndOutput() {
        assertThrows(NullPointerException.class, () -> BitWriters.nullable(null));
        assertThrows(NullPointerException.class,
                     () -> BitWriters.nullable(sampleWriter()).write(null, new Sample(true, 5)));
    }

    private static byte[] writeSample(final Sample value) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = bitOutput(bytes);
        output.writeObject(BitWriters.nullable(sampleWriter()), value);
        output.align(1);
        return bytes.toByteArray();
    }

    private static BitReader<Sample> sampleReader() {
        return new BitReader<Sample>() {
            @Override
            public Sample read(final BitInput input) throws IOException {
                return new Sample(input.readBoolean(), input.readUnsignedInt(3));
            }
        };
    }

    private static BitWriter<Sample> sampleWriter() {
        return new BitWriter<Sample>() {
            @Override
            public void write(final BitOutput output, final Sample value) throws IOException {
                output.writeBoolean(value.flag);
                output.writeUnsignedInt(3, value.code);
            }
        };
    }

    private static class CountingReader
            implements BitReader<Sample> {

        @Override
        public Sample read(final BitInput input) throws IOException {
            count++;
            return sampleReader().read(input);
        }

        private int count;
    }

    private static class CountingWriter
            implements BitWriter<Sample> {

        @Override
        public void write(final BitOutput output, final Sample value) throws IOException {
            count++;
            sampleWriter().write(output, value);
        }

        private int count;
    }

    private static BitInput bitInput(final byte[] bytes) {
        return new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
    }

    private static BitOutput bitOutput(final ByteArrayOutputStream bytes) {
        return new DefaultBitOutput(new StreamByteOutput(bytes));
    }

    private static class Sample {

        private Sample(final boolean flag, final int code) {
            super();
            this.flag = flag;
            this.code = code;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Sample)) {
                return false;
            }
            final Sample that = (Sample) obj;
            return flag == that.flag && code == that.code;
        }

        @Override
        public int hashCode() {
            int result = flag ? 1 : 0;
            result = 31 * result + code;
            return result;
        }

        private final boolean flag;

        private final int code;
    }
}
