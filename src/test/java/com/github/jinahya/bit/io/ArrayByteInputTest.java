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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for testing {@link ArrayByteInput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ArrayByteOutputTest
 */
class ArrayByteInputTest {

    @Test
    void readsUnsignedBytesAndAdvancesIndex() throws IOException {
        final ArrayByteInput input = new ArrayByteInput(new byte[]{0x00, 0x7F, (byte) 0xFF});

        assertEquals(0, input.getIndex());
        assertEquals(0x00, input.read());
        assertEquals(1, input.getIndex());
        assertEquals(0x7F, input.read());
        assertEquals(2, input.getIndex());
        assertEquals(0xFF, input.read());
        assertEquals(3, input.getIndex());
    }

    @Test
    void rejectsNullSource() {
        assertThrows(NullPointerException.class, () -> new ArrayByteInput(null));
    }

    @Test
    void throwsArrayIndexOutOfBoundsWhenExhausted() throws IOException {
        final ArrayByteInput input = new ArrayByteInput(new byte[]{0x01});

        assertEquals(0x01, input.read());
        assertThrows(ArrayIndexOutOfBoundsException.class, input::read);
    }

    @Test
    void emptyArrayStartsAtMinusOneAndThrowsArrayIndexOutOfBounds() {
        final ArrayByteInput input = new ArrayByteInput(new byte[0]);

        assertEquals(-1, input.getIndex());
        assertThrows(ArrayIndexOutOfBoundsException.class, input::read);
        assertEquals(0, input.getIndex());
    }

    // ----------------------------------------------------------------------------------------------------- getIndex()I
    @Test
    void testGetIndex() {
        final ArrayByteInput input = new ArrayByteInput(new byte[]{0x00});

        assertEquals(0, input.getIndex());
    }

    // ---------------------------------------------------------------------------------------------------- setIndex(I)V
    @Test
    void testSetIndex() throws IOException {
        final ArrayByteInput input = new ArrayByteInput(new byte[]{0x00, 0x01});

        input.setIndex(1);
        assertEquals(1, input.getIndex());
        assertEquals(0x01, input.read());
    }

    @Test
    void setIndexPastEndMakesNextReadThrowArrayIndexOutOfBounds() {
        final ArrayByteInput input = new ArrayByteInput(new byte[]{0x00});

        input.setIndex(1);
        assertThrows(ArrayIndexOutOfBoundsException.class, input::read);
        assertEquals(2, input.getIndex());
    }
}
