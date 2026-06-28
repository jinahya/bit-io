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

import static org.junit.jupiter.api.Assertions.assertEquals;

class BitRoundTripTest {

    @Test
    void roundTripsBooleans() throws IOException {
        final byte[] bytes = write(new BitWriter() {
            @Override
            public void write(final BitOutput output) throws IOException {
                output.writeBoolean(false);
                output.writeBoolean(true);
                output.writeBoolean(true);
                output.writeBoolean(false);
            }
        });
        read(bytes, new BitReader() {
            @Override
            public void read(final BitInput input) throws IOException {
                assertEquals(false, input.readBoolean());
                assertEquals(true, input.readBoolean());
                assertEquals(true, input.readBoolean());
                assertEquals(false, input.readBoolean());
            }
        });
    }

    @Test
    void roundTripsIntegralValuesWithExplicitSizes() throws IOException {
        final byte[] bytes = write(new BitWriter() {
            @Override
            public void write(final BitOutput output) throws IOException {
                output.writeUnsignedByte(7, (byte) 0x7F);
                output.writeByte(8, (byte) -1);
                output.writeUnsignedShort(15, (short) 0x7FFF);
                output.writeShort(16, (short) -32768);
                output.writeUnsignedInt(31, 0x7FFFFFFF);
                output.writeInt(32, Integer.MIN_VALUE);
                output.writeUnsignedLong(63, Long.MAX_VALUE);
                output.writeLong(64, Long.MIN_VALUE);
                output.writeChar(16, (char) 0xFFFF);
            }
        });
        read(bytes, new BitReader() {
            @Override
            public void read(final BitInput input) throws IOException {
                assertEquals((byte) 0x7F, input.readUnsignedByte(7));
                assertEquals((byte) -1, input.readByte(8));
                assertEquals((short) 0x7FFF, input.readUnsignedShort(15));
                assertEquals((short) -32768, input.readShort(16));
                assertEquals(0x7FFFFFFF, input.readUnsignedInt(31));
                assertEquals(Integer.MIN_VALUE, input.readInt(32));
                assertEquals(Long.MAX_VALUE, input.readUnsignedLong(63));
                assertEquals(Long.MIN_VALUE, input.readLong(64));
                assertEquals((char) 0xFFFF, input.readChar(16));
            }
        });
    }

    @Test
    void roundTripsSizedBytesForEveryValidSize() throws IOException {
        for (int size = 1; size < Byte.SIZE; size++) {
            final int s = size;
            final byte expected = (byte) ((1 << s) - 1);
            read(write(new BitWriter() {
                @Override
                public void write(final BitOutput output) throws IOException {
                    output.writeUnsignedByte(s, expected);
                }
            }), new BitReader() {
                @Override
                public void read(final BitInput input) throws IOException {
                    assertEquals(expected, input.readUnsignedByte(s));
                }
            });
        }
        for (int size = 1; size <= Byte.SIZE; size++) {
            final int s = size;
            final byte expected = (byte) -(1 << (s - 1));
            read(write(new BitWriter() {
                @Override
                public void write(final BitOutput output) throws IOException {
                    output.writeByte(s, expected);
                }
            }), new BitReader() {
                @Override
                public void read(final BitInput input) throws IOException {
                    assertEquals(expected, input.readByte(s));
                }
            });
        }
    }

    @Test
    void roundTripsSizedShortsForEveryValidSize() throws IOException {
        for (int size = 1; size < Short.SIZE; size++) {
            final int s = size;
            final short expected = (short) ((1 << s) - 1);
            read(write(new BitWriter() {
                @Override
                public void write(final BitOutput output) throws IOException {
                    output.writeUnsignedShort(s, expected);
                }
            }), new BitReader() {
                @Override
                public void read(final BitInput input) throws IOException {
                    assertEquals(expected, input.readUnsignedShort(s));
                }
            });
        }
        for (int size = 1; size <= Short.SIZE; size++) {
            final int s = size;
            final short expected = (short) -(1 << (s - 1));
            read(write(new BitWriter() {
                @Override
                public void write(final BitOutput output) throws IOException {
                    output.writeShort(s, expected);
                }
            }), new BitReader() {
                @Override
                public void read(final BitInput input) throws IOException {
                    assertEquals(expected, input.readShort(s));
                }
            });
        }
    }

    @Test
    void roundTripsSizedIntsForEveryValidSize() throws IOException {
        for (int size = 1; size < Integer.SIZE; size++) {
            final int s = size;
            final int expected = (1 << s) - 1;
            read(write(new BitWriter() {
                @Override
                public void write(final BitOutput output) throws IOException {
                    output.writeUnsignedInt(s, expected);
                }
            }), new BitReader() {
                @Override
                public void read(final BitInput input) throws IOException {
                    assertEquals(expected, input.readUnsignedInt(s));
                }
            });
        }
        for (int size = 1; size <= Integer.SIZE; size++) {
            final int s = size;
            final int expected = -(1 << (s - 1));
            read(write(new BitWriter() {
                @Override
                public void write(final BitOutput output) throws IOException {
                    output.writeInt(s, expected);
                }
            }), new BitReader() {
                @Override
                public void read(final BitInput input) throws IOException {
                    assertEquals(expected, input.readInt(s));
                }
            });
        }
    }

    @Test
    void roundTripsSizedLongsForEveryValidSize() throws IOException {
        for (int size = 1; size < Long.SIZE; size++) {
            final int s = size;
            final long expected = (1L << s) - 1L;
            read(write(new BitWriter() {
                @Override
                public void write(final BitOutput output) throws IOException {
                    output.writeUnsignedLong(s, expected);
                }
            }), new BitReader() {
                @Override
                public void read(final BitInput input) throws IOException {
                    assertEquals(expected, input.readUnsignedLong(s));
                }
            });
        }
        for (int size = 1; size <= Long.SIZE; size++) {
            final int s = size;
            final long expected = -(1L << (s - 1));
            read(write(new BitWriter() {
                @Override
                public void write(final BitOutput output) throws IOException {
                    output.writeLong(s, expected);
                }
            }), new BitReader() {
                @Override
                public void read(final BitInput input) throws IOException {
                    assertEquals(expected, input.readLong(s));
                }
            });
        }
    }

    @Test
    void roundTripsSizedCharsForEveryValidSize() throws IOException {
        for (int size = 1; size <= Character.SIZE; size++) {
            final int s = size;
            final char expected = (char) ((1 << s) - 1);
            read(write(new BitWriter() {
                @Override
                public void write(final BitOutput output) throws IOException {
                    output.writeChar(s, expected);
                }
            }), new BitReader() {
                @Override
                public void read(final BitInput input) throws IOException {
                    assertEquals(expected, input.readChar(s));
                }
            });
        }
    }

    @Test
    void roundTripsFixedWidthValues() throws IOException {
        final byte[] bytes = write(new BitWriter() {
            @Override
            public void write(final BitOutput output) throws IOException {
                output.writeByte8((byte) 0x80);
                output.writeShort16((short) 0x1234);
                output.writeInt32(0x12345678);
                output.writeLong64(0x0123456789ABCDEFL);
                output.writeChar16((char) 0xCAFE);
            }
        });
        read(bytes, new BitReader() {
            @Override
            public void read(final BitInput input) throws IOException {
                assertEquals((byte) 0x80, input.readByte8());
                assertEquals((short) 0x1234, input.readShort16());
                assertEquals(0x12345678, input.readInt32());
                assertEquals(0x0123456789ABCDEFL, input.readLong64());
                assertEquals((char) 0xCAFE, input.readChar16());
            }
        });
    }

    @Test
    void roundTripsLittleEndianValues() throws IOException {
        final byte[] bytes = write(new BitWriter() {
            @Override
            public void write(final BitOutput output) throws IOException {
                output.writeShort16Le((short) 0x1234);
                output.writeInt32Le(0x12345678);
                output.writeLong64Le(0x0123456789ABCDEFL);
                output.writeChar16Le((char) 0xCAFE);
            }
        });
        read(bytes, new BitReader() {
            @Override
            public void read(final BitInput input) throws IOException {
                assertEquals((short) 0x1234, input.readShort16Le());
                assertEquals(0x12345678, input.readInt32Le());
                assertEquals(0x0123456789ABCDEFL, input.readLong64Le());
                assertEquals((char) 0xCAFE, input.readChar16Le());
            }
        });
    }

    private static byte[] write(final BitWriter writer) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        writer.write(output);
        output.align(1);
        return baos.toByteArray();
    }

    private static void read(final byte[] bytes, final BitReader reader) throws IOException {
        reader.read(new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes))));
    }

    private interface BitWriter {

        void write(BitOutput output) throws IOException;
    }

    private interface BitReader {

        void read(BitInput input) throws IOException;
    }
}
