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


import com.googlecode.jinahya.io.BitInput.StreamInput;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
public abstract class FileFormat {


    protected static int toggleEndian(int value, final int size) {

        int result = 0x00;

        for (int i = 0; i < size; i++) {
            result <<= 8;
            result |= value & 0xFF;
            value >>= 8;
        }

        return result;
    }


    protected static long toggleEndian(long value, final int size) {

        long result = 0x00L;

        for (int i = 0; i < size; i++) {
            result <<= 8;
            result |= value & 0xFF;
            value >>= 8;
        }

        return result;
    }


    protected static void toggleEndian(final byte[] value)
        throws IOException {

        final int half = value.length / 2;
        byte tmp;
        for (int i = 0; i < half; i++) {
            tmp = value[value.length - i + 1];
            value[value.length - i + 1] = value[0];
            value[0] = tmp;
        }
    }


    protected static byte[] read(final BitInput input, final byte[] bytes)
        throws IOException {

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) input.readUnsignedByte(8);
        }

        return bytes;
    }


    public static <F extends FileFormat> F scanInstance(
        final Class<F> formatClass, final URL url)
        throws InstantiationException, IllegalAccessException, IOException {

        final InputStream input = url.openStream();
        try {
            return readInstance(formatClass, input);
        } finally {
            input.close();
        }
    }


    public static <F extends FileFormat> F readInstance(
        final Class<F> formatClass, final InputStream input)
        throws InstantiationException, IllegalAccessException, IOException {

        return readInstance(formatClass,
                            new BitInput(new StreamInput(input)));
    }


    public static <F extends FileFormat> F readInstance(
        final Class<F> formatClass, final BitInput input)
        throws InstantiationException, IllegalAccessException, IOException {

        final F instance = formatClass.newInstance();

        instance.read(input);

        return instance;
    }


    public FileFormat() {
        super();
    }


    protected abstract void read(BitInput input) throws IOException;


    public void read(final InputStream input) throws IOException {

        read(new BitInput(new BitInput.StreamInput(input)));
    }


    public void read(final URL url) throws IOException {

        final InputStream input = url.openStream();
        try {
            read(input);
        } finally {
            input.close();
        }

    }


}

