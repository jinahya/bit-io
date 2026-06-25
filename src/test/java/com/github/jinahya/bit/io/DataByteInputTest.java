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
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for unit-testing {@link DataByteInput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DataByteOutputTest
 */
public class DataByteInputTest {

    @Test
    void readsUnsignedBytesFromDataInput() throws IOException {
        final DataInput source = new DataInputStream(new ByteArrayInputStream(new byte[]{0x00, 0x7F, (byte) 0xFF}));
        final DataByteInput input = new DataByteInput(source);

        assertEquals(0x00, input.read());
        assertEquals(0x7F, input.read());
        assertEquals(0xFF, input.read());
        assertThrows(EOFException.class, input::read);
    }

    @Test
    void propagatesEofFromBackingDataInput() {
        final DataByteInput input = new DataByteInput(new DataInputStream(new ByteArrayInputStream(new byte[0])));

        assertThrows(EOFException.class, input::read);
    }

    @Test
    void rejectsNullDataInput() {
        assertThrows(NullPointerException.class, () -> new DataByteInput(null));
    }
}
