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
import static org.junit.jupiter.api.Assertions.assertTrue;

class BitAlignmentTest {

    @Test
    void outputAlignPadsCurrentByteWithZeroBits() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        output.writeBoolean(true);
        assertEquals(7L, output.align(1));
        assertArrayEquals(new byte[]{(byte) 0x80}, baos.toByteArray());
    }

    @Test
    void inputAlignDiscardsRemainingBitsInCurrentByte() throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(new byte[]{
                (byte) 0x80
        })));
        assertTrue(input.readBoolean());
        assertEquals(7L, input.align(1));
    }

    @Test
    void outputAlignPadsToRequestedByteMultiple() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        output.writeBoolean(false);
        assertEquals(31L, output.align(4));
        assertEquals(4, baos.toByteArray().length);
    }

    @Test
    void inputAlignDiscardsToRequestedByteMultiple() throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(new byte[]{
                0x00, 0x00, 0x00, 0x00
        })));
        input.readBoolean();
        assertEquals(31L, input.align(4));
    }

    @Test
    void alignedOutputReturnsZeroWhenAlreadyAligned() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        output.writeByte8((byte) 0x11);
        assertEquals(0L, output.align(1));
        assertArrayEquals(new byte[]{0x11}, baos.toByteArray());
    }
}
