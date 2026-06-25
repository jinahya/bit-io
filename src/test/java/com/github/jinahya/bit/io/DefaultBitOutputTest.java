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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A class for unit-testing {@link DefaultBitOutput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DefaultBitInputTest
 */
public class DefaultBitOutputTest {

    @Test
    void rejectsNullDelegate() {
        assertThrows(NullPointerException.class, () -> new DefaultBitOutput(null));
    }

    @Test
    void writeDelegatesToByteOutput() throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final DefaultBitOutput output = new DefaultBitOutput(new StreamByteOutput(bytes));

        output.write(0x00);
        output.write(0x7F);
        output.write(0xFF);

        assertArrayEquals(new byte[]{0x00, 0x7F, (byte) 0xFF}, bytes.toByteArray());
    }

    @Test
    void toStringIncludesDelegate() {
        final ByteOutput delegate = new StreamByteOutput(new ByteArrayOutputStream());

        assertTrue(new DefaultBitOutput(delegate).toString().contains("delegate=" + delegate));
    }
}
