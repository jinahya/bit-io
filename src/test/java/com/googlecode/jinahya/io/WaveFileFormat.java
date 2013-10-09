/*
 * Copyright 2013 Jin Kwon <jinahya at gmail.com>.
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


package com.googlecode.jinahya.io;


import static com.googlecode.jinahya.io.FileFormat.read;
import static com.googlecode.jinahya.io.FileFormat.toggleEndian;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
public class WaveFileFormat extends FileFormat {


    public WaveFileFormat() {
        super();
    }


    @Override
    protected void read(final BitInput input) throws IOException {

        final byte[] chunkId = read(input, new byte[4]);
        System.out.println(
            "chunkId: " + new String(chunkId, StandardCharsets.US_ASCII));

        final long chunkSize = toggleEndian(input.readUnsignedLong(32), 4);
        System.out.println("chunkSize: " + chunkSize);

        final byte[] format = read(input, new byte[4]);
        System.out.println(
            "format: " + new String(format, StandardCharsets.US_ASCII));

        final byte[] subChunk1Id = read(input, new byte[4]);
        System.out.println(
            "subChunk1Id: "
            + new String(subChunk1Id, StandardCharsets.US_ASCII));

        final long subChunk1Size = toggleEndian(input.readUnsignedLong(32), 4);
        System.out.println("subChunk1Size: " + subChunk1Size);

        final int audioFormat = toggleEndian(input.readUnsignedInt(16), 2);
        System.out.println("audioFormat: " + audioFormat);

        final int numChannels = toggleEndian(input.readUnsignedInt(16), 2);
        System.out.println("numChannels: " + numChannels);

        final long sampleRate = toggleEndian(input.readUnsignedLong(32), 4);
        System.out.println("sampleRate: " + sampleRate);

        final long byteRate = toggleEndian(input.readUnsignedLong(32), 4);
        System.out.println("byteRate: " + byteRate);

        final int blockAlign = toggleEndian(input.readUnsignedInt(16), 2);
        System.out.println("blockAlign: " + blockAlign);

        final int bitsPerSample = toggleEndian(input.readUnsignedInt(16), 2);
        System.out.println("bitsPerSample: " + bitsPerSample);

        if (audioFormat != 1) { // not PCM
            final int extraParamSize =
                toggleEndian(input.readUnsignedInt(16), 2);
            System.out.println("extraParamSize: " + extraParamSize);
            final byte[] extraParams = read(input, new byte[extraParamSize]);
        };

        final byte[] subChunk2Id = read(input, new byte[4]);
        System.out.println(
            "subChunk2Id: "
            + new String(subChunk2Id, StandardCharsets.US_ASCII));

        final long subChunk2Size = toggleEndian(input.readUnsignedLong(32), 4);
        System.out.println("subChunk2Size: " + subChunk2Size);

        for (long i = 0; i < subChunk2Size; i++) {
            System.out.println(i);
            input.readUnsignedByte(8);
        }
    }


}

