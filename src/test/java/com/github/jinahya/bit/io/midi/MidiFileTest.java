/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
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
 */

package com.github.jinahya.bit.io.midi;


import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.ByteInput;
import com.github.jinahya.bit.io.DelegatedBitInput;
import com.github.jinahya.bit.io.StreamInput;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class MidiFileTest {


    private static final Logger logger = getLogger(MidiFileTest.class);


    static void parse(final ByteInput byteInput) throws IOException {

        final BitInput input = new DelegatedBitInput(byteInput);

        assertEquals(input.readInt(Integer.SIZE), 0x4d546864);
        assertEquals(input.readInt(Integer.SIZE), 6);
        final int format = input.readUnsignedInt(16);
        logger.debug("format: {}", format);
        final int tracks = input.readUnsignedInt(16);
        logger.debug("tracks: {}", tracks);
        final int division = input.readInt(16);
        logger.debug("division: {}", division);

        for (int i = 0; i < tracks; i++) {
            assertEquals(input.readInt(Integer.SIZE), 0x4d54726b);
            final int length = input.readInt(Integer.SIZE);
            for (int j = 0; j < length; j++) {
                input.readUnsignedInt(Byte.SIZE);
            }
        }
    }


    @Test
    public void test() throws IOException {

        try (InputStream resource
            = getClass().getResourceAsStream("/midi/MIDI_sample.midi")) {
            parse(new StreamInput(resource));
        }
    }

}

