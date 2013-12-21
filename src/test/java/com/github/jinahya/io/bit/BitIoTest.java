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


package com.github.jinahya.io.bit;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ThreadLocalRandom;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
public class BitIoTest {


    @Test
    public static void random_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();

        final int count = random.nextInt(1024);

        final L ls[] = new L[count];
        for (int i = 0; i < ls.length; i++) {
            ls[i] = new L();
        }

        final V evs[] = new V[ls.length];
        for (int i = 0; i < evs.length; i++) {
            evs[0] = new V(ls[i]);
        }

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final ByteOutput<OutputStream> byt = new StreamOutput(baos)) {
            try (final BitOutput<OutputStream> bit = new BitOutput<>(byt)) {
                for (int i = 0; i < evs.length; i++) {
                    evs[i] = new V(ls[i]);
                    evs[i].write(bit);
                }
                bit.align((short) 1);
            }
        }

        final V[] avs = new V[ls.length];

        final ByteArrayInputStream bais
            = new ByteArrayInputStream(baos.toByteArray());
        try (final ByteInput<InputStream> byt = new StreamInput(bais)) {
            try (final BitInput<InputStream> bit = new BitInput<>(byt)) {
                for (int i = 0; i < avs.length; i++) {
                    avs[i] = new V(ls[i]);
                    avs[i].read(bit);
                }
                bit.align((short) 1);
            }
        }

        Assert.assertEquals(avs, evs);
    }


}

