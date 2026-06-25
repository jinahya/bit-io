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
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for unit-testing {@link StreamByteOutput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see StreamByteInputTest
 */
public class StreamByteOutputTest {

    @Test
    void writesBytesToStream() throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final OutputStream target = bytes;
        final StreamByteOutput output = new StreamByteOutput(target);

        output.write(0x00);
        output.write(0x7F);
        output.write(0xFF);

        assertArrayEquals(new byte[]{0x00, 0x7F, (byte) 0xFF}, bytes.toByteArray());
    }

    @Test
    void rejectsNullStream() {
        assertThrows(NullPointerException.class, () -> new StreamByteOutput(null));
    }
}
