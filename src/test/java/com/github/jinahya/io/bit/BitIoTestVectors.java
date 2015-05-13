/*
 * Copyright 2014 Jin Kwon.
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


package com.github.jinahya.io.bit;


import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Jin Kwon
 */
public final class BitIoTestVectors {


    // standard MIDI file format
    // http://www.music.mcgill.ca/~ich/classes/mumt306/midiformat.pdf
    // http://www.cs.cmu.edu/~music/cmsip/readings/Standard-MIDI-file-format-updated.pdf
    static final Map<Integer, byte[]> A = new HashMap<Integer, byte[]>() {

        {

            put(0x00000000, new byte[]{(byte) 0x00});
            put(0x00000040, new byte[]{(byte) 0x40});
            put(0x0000007F, new byte[]{(byte) 0x7F});
            put(0x00000080, new byte[]{(byte) 0x81, (byte) 0x00});
            put(0x00002000, new byte[]{(byte) 0xC0, (byte) 0x00});
            put(0x00003FFF, new byte[]{(byte) 0xFF, (byte) 0x7F});
            put(0x00004000, new byte[]{(byte) 0x81, (byte) 0x80,
                                       (byte) 0x00});
            put(0x00100000, new byte[]{(byte) 0xC0, (byte) 0x80,
                                       (byte) 0x00});
            put(0x001FFFFF, new byte[]{(byte) 0xFF, (byte) 0xFF,
                                       (byte) 0x7F});
            put(0x00200000, new byte[]{(byte) 0x81, (byte) 0x80,
                                       (byte) 0x80, (byte) 0x00});
            put(0x08000000, new byte[]{(byte) 0xC0, (byte) 0x80,
                                       (byte) 0x80, (byte) 0x00});
            put(0x0FFFFFFF, new byte[]{(byte) 0xFF, (byte) 0xFF,
                                       (byte) 0xFF, (byte) 0x7F});
        }


    };


    // rosettacode.org
    static final Map<Long, byte[]> B = new HashMap<Long, byte[]>() {

        {
            // 2097152L
            put(0x0000000000200000L, new byte[]{(byte) 0x81, (byte) 0x80,
                                                (byte) 0x80, (byte) 0x00});
            // 2097151L
            put(0x00000000001FFFFFL, new byte[]{(byte) 0xFF, (byte) 0xFF,
                                                (byte) 0x7F});
            // 1L
            put(0x0000000000000001L, new byte[]{(byte) 0x01});
            // 127L
            put(0x000000000000007FL, new byte[]{(byte) 0x7F});
            // 128L
            put(0x0000000000000080L, new byte[]{(byte) 0x81, (byte) 0x00});
            // 589723405834L
            put(0x000000894E410E0AL, new byte[]{(byte) 0x91, (byte) 0x94,
                                                (byte) 0xF2, (byte) 0x84,
                                                (byte) 0x9C, (byte) 0x0A});
        }


    };


    private BitIoTestVectors() {

        super();
    }


}

