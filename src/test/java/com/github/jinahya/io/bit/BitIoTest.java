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


import com.github.jinahya.io.bit.candidate.BitReadable;
import com.github.jinahya.io.bit.candidate.BitWritable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon
 */
public class BitIoTest {


    private static final Logger logger
        = LoggerFactory.getLogger(BitIoTest.class);


    private static ThreadLocalRandom random() {

        return ThreadLocalRandom.current();
    }


    private static void test(final BitWritable writable,
                             final BitReadable readable)
        throws IOException {

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new BitOutput((a) -> baos.write(a));
        writable.write(output);

        output.align(1);

        final ByteArrayInputStream bais
            = new ByteArrayInputStream(baos.toByteArray());
        final BitInput input = new BitInput(() -> bais.read());
        readable.read(input);
    }


    @Test
    public void boolean_() throws IOException {

        final int size = random().nextInt(0, 128);

        final List<Boolean> expected = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            expected.add(random().nextBoolean());
        }

        final List<Boolean> actual = new ArrayList<>(size);

        test(
            (w) -> {
                for (final boolean value : expected) {
                    w.writeBoolean(value);
                }
            },
            (r) -> {
                for (int i = 0; i < size; i++) {
                    actual.add(r.readBoolean());
                }
            }
        );

        Assert.assertEquals(actual, expected);

    }


    @Test
    public void intUnsigned_() throws IOException {

        final int size = random().nextInt(0, 128);

        final List<Integer> lengths = new ArrayList<>(size);
        BitIoTests.lengthIntUnsigned(size, lengths);

        final List<Integer> expected = new ArrayList<>(size);
        BitIoTests.valueIntUnsigned(lengths, expected);

        final List<Integer> actual = new ArrayList<>(size);

        test(
            (w) -> {
                for (int i = 0; i < size; i++) {
                    w.writeUnsignedInt(lengths.get(i), expected.get(i));
                }
            },
            (r) -> {
                for (int i = 0; i < size; i++) {
                    actual.add(r.readUnsignedInt(lengths.get(i)));
                }
            }
        );

        Assert.assertEquals(actual, expected);
    }


    @Test
    public void int_() throws IOException {

        final int size = random().nextInt(0, 128);

        final List<Integer> lengths = new ArrayList<>(size);
        BitIoTests.lengthInt(size, lengths);

        final List<Integer> expected = new ArrayList<>(size);
        BitIoTests.valueInt(lengths, expected);

        final List<Integer> actual = new ArrayList<>(size);

        test(
            (w) -> {
                for (int i = 0; i < size; i++) {
                    w.writeInt(lengths.get(i), expected.get(i));
                }
            },
            (r) -> {
                for (int i = 0; i < size; i++) {
                    actual.add(r.readInt(lengths.get(i)));
                }
            }
        );

        Assert.assertEquals(actual, expected);
    }


    @Test
    public void variableLengthIntLE_llvm_example() throws IOException {

        final int length = 3;
        final int expected = 27;

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput bo = new BitOutput(new StreamOutput(baos));
        bo.writeVariableLengthIntLE(length, 27);
        bo.align(1);

        {
            final BitInput bi = new BitInput(new StreamInput(
                new ByteArrayInputStream(baos.toByteArray())));
            assertEquals(bi.readUnsignedByte(1), 1); // next
            assertEquals(bi.readUnsignedByte(1), 0);
            assertEquals(bi.readUnsignedByte(1), 1);
            assertEquals(bi.readUnsignedByte(1), 1);
            assertEquals(bi.readUnsignedByte(1), 0); // next
            assertEquals(bi.readUnsignedByte(1), 0);
            assertEquals(bi.readUnsignedByte(1), 1);
            assertEquals(bi.readUnsignedByte(1), 1);
        }

        final BitInput bi = new BitInput(new StreamInput(
            new ByteArrayInputStream(baos.toByteArray())));
        final int actual = bi.readVariableLengthIntLE(length);

        assertEquals(actual, expected);
    }


}

