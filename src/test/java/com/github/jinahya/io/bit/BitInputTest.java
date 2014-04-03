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


import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon
 */
@Guice(modules = {BitInputModule.class, ByteInputMockModule.class})
public class BitInputTest {


    /**
     * logger.
     */
    private static final Logger logger
            = LoggerFactory.getLogger(BitInputTest.class);


    @DataProvider
    public Iterator<Object[]> provideByteInputs() {

        return Arrays.<Object[]>asList(
                new Object[]{ByteInputs.newInstance()}
        ).iterator();
    }


    @Test
    public void readBoolean_() throws IOException {

        input.readBoolean();
    }


    @Test
    public void readUnsignedInt_() throws IOException {

        try {
            input.readUnsignedInt(0x00);
            Assert.fail("passed: readUnsignedInt(0x00)");
        } catch (final IllegalArgumentException iae) {
            // epxected
        }

        try {
            input.readUnsignedInt(0x20);
            Assert.fail("passed: readUnsignedInt(0x20)");
        } catch (final IllegalArgumentException iae) {
            // epxected
        }

        for (int length = 1; length < 32; length++) {
            input.readUnsignedInt(length);
        }
    }


    @Test
    public void readInt_() throws IOException {

        try {
            input.readInt(0x01);
            Assert.fail("passed: readInt(0x01)");
        } catch (final IllegalArgumentException iae) {
            // epxected
        }

        try {
            input.readInt(0x21);
            Assert.fail("passed: readInt(0x21)");
        } catch (final IllegalArgumentException iae) {
            // epxected
        }

        for (int length = 2; length < 33; length++) {
            input.readInt(length);
        }
    }


    @Test
    public void readFloat_() throws IOException {

        input.readFloat();
    }


    @Test
    public void readUnsignedLong_() throws IOException {

        try {
            input.readUnsignedLong(0x00);
            Assert.fail("passed: readUnsignedLong(0x00)");
        } catch (final IllegalArgumentException iae) {
            // epxected
        }

        try {
            input.readUnsignedLong(0x40);
            Assert.fail("passed: readUnsignedLong(0x40)");
        } catch (final IllegalArgumentException iae) {
            // epxected
        }

        for (int length = 1; length < 64; length++) {
            input.readUnsignedLong(length);
        }
    }


    @Test
    public void readLong_() throws IOException {

        try {
            input.readLong(0x01);
            Assert.fail("passed: readLong(0x01)");
        } catch (final IllegalArgumentException iae) {
            // epxected
        }

        try {
            input.readLong(0x41);
            Assert.fail("passed: readLong(0x41)");
        } catch (final IllegalArgumentException iae) {
            // epxected
        }

        for (int length = 2; length < 65; length++) {
            input.readLong(length);
        }
    }


    @Test
    public void readDouble_() throws IOException {

        input.readDouble();
    }


    @Inject
    private BitInput input;


}

