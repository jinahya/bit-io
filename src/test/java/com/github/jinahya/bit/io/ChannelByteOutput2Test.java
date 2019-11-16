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
import java.nio.channels.WritableByteChannel;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * A class for unit-testing {@link ChannelByteOutput2} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteInput2Test
 */
public class ChannelByteOutput2Test extends AbstractByteOutputTest<ChannelByteOutput2, WritableByteChannel> {

    // -----------------------------------------------------------------------------------------------------------------
    ChannelByteOutput2Test() {
        super(ChannelByteOutput2.class, WritableByteChannel.class);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link ChannelByteOutput2#of(WritableByteChannel)} method.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testOf() throws IOException {
        final ChannelByteOutput2 byteOutput = ChannelByteOutput2.of(new BlackByteChannel());
        byteOutput.write(0);
        assertNotNull(byteOutput.getBuffer());
    }
}
