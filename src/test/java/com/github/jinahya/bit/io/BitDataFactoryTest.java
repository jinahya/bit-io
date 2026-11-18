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
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A class for testing {@link BitInputs#fromDataInput(DataInput)} and {@link BitOutputs#fromDataOutput(DataOutput)}
 * with concrete data stream types, which are ambiguous for the {@code from} overloads.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see <a href="https://github.com/jinahya/bit-io/issues/23">#23</a>
 */
class BitDataFactoryTest {

    @Test
    void roundTripsWithConcreteDataStreams() throws IOException {
        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(target);
        final BitOutput output = BitOutputs.fromDataOutput(dataOutputStream);
        output.writeBoolean(true);
        output.writeInt(true, 5, 31);
        output.align(1);
        dataOutputStream.flush();

        final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(target.toByteArray()));
        final BitInput input = BitInputs.fromDataInput(dataInputStream);
        assertTrue(input.readBoolean());
        assertEquals(31, input.readInt(true, 5));
        input.align(1);
    }

    @Test
    void producesSameBytesAsExistingOverloads() throws IOException {
        final ByteArrayOutputStream viaFactory = new ByteArrayOutputStream();
        final BitOutput output1 = BitOutputs.fromDataOutput(new DataOutputStream(viaFactory));
        output1.writeInt(false, 17, -12345);
        output1.align(1);

        final ByteArrayOutputStream viaDataOutput = new ByteArrayOutputStream();
        final BitOutput output2 = BitOutputs.from((DataOutput) new DataOutputStream(viaDataOutput));
        output2.writeInt(false, 17, -12345);
        output2.align(1);

        final ByteArrayOutputStream viaOutputStream = new ByteArrayOutputStream();
        final BitOutput output3 = BitOutputs.from((OutputStream) new DataOutputStream(viaOutputStream));
        output3.writeInt(false, 17, -12345);
        output3.align(1);

        assertArrayEquals(viaDataOutput.toByteArray(), viaFactory.toByteArray());
        assertArrayEquals(viaOutputStream.toByteArray(), viaFactory.toByteArray());

        final byte[] bytes = viaFactory.toByteArray();
        assertEquals(-12345, BitInputs.fromDataInput(new DataInputStream(new ByteArrayInputStream(bytes)))
                .readInt(false, 17));
        assertEquals(-12345, BitInputs.from((DataInput) new DataInputStream(new ByteArrayInputStream(bytes)))
                .readInt(false, 17));
        assertEquals(-12345, BitInputs.from((InputStream) new DataInputStream(new ByteArrayInputStream(bytes)))
                .readInt(false, 17));
    }

    @Test
    void rejectsNull() {
        assertThrows(NullPointerException.class, () -> BitInputs.fromDataInput(null));
        assertThrows(NullPointerException.class, () -> BitOutputs.fromDataOutput(null));
    }
}
