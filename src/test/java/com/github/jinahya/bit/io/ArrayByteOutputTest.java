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

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for testing {@link ArrayByteOutput}.
 *
 * @see ArrayByteInputTest
 */
class ArrayByteOutputTest {

    @Test
    void writesBytesAndAdvancesIndex() throws IOException {
        final byte[] bytes = new byte[3];
        final ArrayByteOutput output = new ArrayByteOutput(bytes);

        assertEquals(0, output.getIndex());
        output.write(0x00);
        assertEquals(1, output.getIndex());
        output.write(0x7F);
        assertEquals(2, output.getIndex());
        output.write(0xFF);
        assertEquals(3, output.getIndex());
        assertArrayEquals(new byte[]{0x00, 0x7F, (byte) 0xFF}, bytes);
    }

    @Test
    void rejectsNullTarget() {
        assertThrows(NullPointerException.class, () -> new ArrayByteOutput(null));
    }

    @Test
    void throwsArrayIndexOutOfBoundsWhenFull() throws IOException {
        final ArrayByteOutput output = new ArrayByteOutput(new byte[1]);

        output.write(0x01);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> output.write(0x02));
    }

    @Test
    void emptyArrayStartsAtMinusOneAndThrowsArrayIndexOutOfBoundsWhenWritten() {
        final ArrayByteOutput output = new ArrayByteOutput(new byte[0]);

        assertEquals(-1, output.getIndex());
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> output.write(0x00));
        assertEquals(0, output.getIndex());
    }

    // ----------------------------------------------------------------------------------------------------- getIndex()I
    @Test
    void testGetIndex() {
        final ArrayByteOutput output = new ArrayByteOutput(new byte[]{0x00});

        assertEquals(0, output.getIndex());
    }

    // ---------------------------------------------------------------------------------------------------- setIndex(I)V
    @Test
    void testSetIndex() throws IOException {
        final byte[] bytes = new byte[2];
        final ArrayByteOutput output = new ArrayByteOutput(bytes);

        output.setIndex(1);
        assertEquals(1, output.getIndex());
        output.write(0x7F);
        assertArrayEquals(new byte[]{0x00, 0x7F}, bytes);
    }

    @Test
    void setIndexPastEndMakesNextWriteThrowArrayIndexOutOfBounds() {
        final ArrayByteOutput output = new ArrayByteOutput(new byte[1]);

        output.setIndex(1);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> output.write(0x7F));
        assertEquals(2, output.getIndex());
    }
}
