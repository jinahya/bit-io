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

package com.github.jinahya.bit.io.flac;


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
public class FlacFileTest {


    private static final Logger logger = getLogger(FlacFileTest.class);


    static void parse(final BitInput input) throws IOException {

        assertEquals(input.readInt(Integer.SIZE), 0x664c6143); // "fLaC"

        // ------------------------------------------------------ METADATA_BLOCK
        while (true) {
            // ------------------------------------------- METADATA_BLOCK_HEADER
            final boolean last = input.readBoolean();
            final int type = input.readUnsignedInt(7);
            int length = input.readUnsignedInt(24);
            // --------------------------------------------- METADATA_BLOCK_DATA
            switch (type) {
                case 0: // METADATA_BLOCK_STREAMINFO
                    input.readUnsignedInt(16);
                    input.readUnsignedInt(16);
                    input.readUnsignedInt(24);
                    input.readUnsignedInt(24);
                    input.readUnsignedInt(20);
                    input.readUnsignedInt(3);
                    input.readUnsignedInt(5);
                    input.readUnsignedLong(36);
                    input.readBytes(new byte[16], 0, 16, 8);
                    break;
                case 1: // METADATA_BLOCK_PADDING
                    input.readBytes(new byte[length], 0, length, 8);
                    break;
                case 2: // METADATA_BLOCK_APPLICATION
                    input.readInt(Integer.SIZE);
                    input.readBytes(new byte[length - 4], 0, length - 4, 8);
                    break;
                case 3: // METADATA_BLOCK_SEEKTABLE
                    for (; length > 0; length -= 18) {
                        input.readLong(Long.SIZE);
                        input.readLong(Long.SIZE);
                        input.readUnsignedInt(16);
                    }
                    break;
                case 4: // METADATA_BLOCK_VORBIS_COMMENT
                    input.readBytes(new byte[length], 0, length, 8);
                    break;
                case 5: // METADATA_BLOCK_CUESHEET
                    input.readBytes(new byte[128], 0, 128, 8);
                    input.readLong(Long.SIZE);
                    input.readBoolean();
                    input.readUnsignedInt(7);
                    input.readBytes(new byte[258], 0, 258, 8);
                    input.readUnsignedInt(Byte.SIZE);
                    for (length -= 410; length > 0;) {
                        input.readLong(Long.SIZE);
                        input.readUnsignedInt(Byte.SIZE);
                        input.readBytes(new byte[12], 0, 12, 8);
                        input.readBoolean();
                        input.readBoolean();
                        input.readUnsignedInt(6);
                        input.readBytes(new byte[13], 0, 13, 8);
                        input.readUnsignedInt(Byte.SIZE);
                        for (length -= 36; length > 0; length -= 12) {
                            input.readLong(Long.SIZE);
                            input.readUnsignedInt(Byte.SIZE);
                            input.readBytes(new byte[3], 0, 3, 8);
                        }
                    }
                    break;
                case 6: // METADATA_BLOCK_PICTURE
                    input.readUnsignedInt(32);
                     {
                        final int l1 = input.readInt(32);
                        input.readBytes(new byte[l1], 0, l1, 8);
                    }
                     {
                        final int l2 = input.readInt(32);
                        input.readBytes(new byte[length], 0, length, 8);
                    }
                    input.readInt(Integer.SIZE);
                    input.readInt(Integer.SIZE);
                    input.readInt(Integer.SIZE);
                    input.readInt(Integer.SIZE);
                     {
                        final int l3 = input.readInt(32);
                        input.readBytes(new byte[length], 0, length, 8);
                    }
                    break;
                default:
                    logger.warn("unknown block type: {}", type);
                    return;
            }
            if (last) {
                break;
            }
        }
        assert input.align(1) == 0;
    }


    static void parse(final ByteInput byteInput) throws IOException {

        parse((BitInput) new DelegatedBitInput(byteInput));
    }


    @Test(enabled = true)
    public void test() throws IOException {

        try (InputStream resource
            = getClass().getResourceAsStream("/flac/recit24bit.flac")) {
            parse(new StreamInput(resource));
        }
    }

}

