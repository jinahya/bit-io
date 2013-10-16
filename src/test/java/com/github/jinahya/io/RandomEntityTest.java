/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
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


package com.github.jinahya.io;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.ThreadLocalRandom;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
public class RandomEntityTest {


    @Test
    public void readWriteInBits() throws IOException {


//        final RandomEntity[] expected =
//            new RandomEntity[ThreadLocalRandom.current().nextInt(128)];
        final RandomEntity[] expected = new RandomEntity[1];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = new RandomEntity();
        }

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output =
            new BitOutput(new BitOutput.StreamOutput(baos));
        for (RandomEntity entity : expected) {
            entity.write(output);
        }
        output.align(1);
        baos.flush();

        final ByteArrayInputStream bais =
            new ByteArrayInputStream(baos.toByteArray());
        final BitInput input = new BitInput(new BitInput.StreamInput(bais));

        final RandomEntity[] actual = new RandomEntity[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = new RandomEntity();
            actual[i].read(input);
        }
        input.align((short) 1);

        Assert.assertEquals(actual, expected);
    }


    @Test(enabled = false)
    public void readWriteInBytes() throws IOException {

        final RandomEntity[] expected =
            new RandomEntity[ThreadLocalRandom.current().nextInt(128)];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = new RandomEntity();
        }

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataOutput output = new DataOutputStream(baos);
        for (RandomEntity entity : expected) {
            entity.write(output);
        }
        baos.flush();

        final ByteArrayInputStream bais =
            new ByteArrayInputStream(baos.toByteArray());
        final DataInput input = new DataInputStream(bais);

        final RandomEntity[] actual = new RandomEntity[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = new RandomEntity();
            actual[i].read(input);
        }

        Assert.assertEquals(actual, expected);
    }


    @Test(enabled = false)
    public void compareSize() throws IOException {

        final RandomEntity[] entities =
            new RandomEntity[ThreadLocalRandom.current().nextInt(128)];
        for (int i = 0; i < entities.length; i++) {
            entities[i] = new RandomEntity();
        }

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        {
            baos.reset();
            final BitOutput output =
                new BitOutput(new BitOutput.StreamOutput(baos));
            for (RandomEntity entity : entities) {
                entity.write(output);
            }
            output.align(1);
            baos.flush();
            System.out.println("in bits: " + baos.size());
        }

        {
            baos.reset();
            final DataOutput output = new DataOutputStream(baos);
            for (RandomEntity entity : entities) {
                entity.write(output);
            }
            baos.flush();
            System.out.println("in bytes: " + baos.size());
        }
    }


    @Test(enabled = false)
    public void writeToStreamReadFromChannel() throws IOException {

        final RandomEntity[] expected =
            new RandomEntity[ThreadLocalRandom.current().nextInt(128)];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = new RandomEntity();
        }

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output =
            new BitOutput(new BitOutput.StreamOutput(baos));
        for (RandomEntity entity : expected) {
            entity.write(output);
        }
        output.align(1);
        baos.flush();

        final ReadableByteChannel rbc =
            Channels.newChannel(new ByteArrayInputStream(baos.toByteArray()));
        final BitInput input = new BitInput(new BitInput.ChannelInput(rbc));

        final RandomEntity[] actual = new RandomEntity[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = new RandomEntity();
            actual[i].read(input);
        }
        input.align((short) 1);

        Assert.assertEquals(actual, expected);
    }


    @Test(enabled = false)
    public void writeToChannelReadFromStream() throws IOException {

        final RandomEntity[] expected =
            new RandomEntity[ThreadLocalRandom.current().nextInt(128)];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = new RandomEntity();
        }

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableByteChannel wbc = Channels.newChannel(baos);
        final BitOutput output =
            new BitOutput(new BitOutput.ChannelOutput(wbc));
        for (RandomEntity entity : expected) {
            entity.write(output);
        }
        output.align(1);
        baos.flush();

        final ByteArrayInputStream bais =
            new ByteArrayInputStream(baos.toByteArray());
        final BitInput input = new BitInput(new BitInput.StreamInput(bais));

        final RandomEntity[] actual = new RandomEntity[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = new RandomEntity();
            actual[i].read(input);
        }
        input.align((short) 1);

        Assert.assertEquals(actual, expected);
    }


}

