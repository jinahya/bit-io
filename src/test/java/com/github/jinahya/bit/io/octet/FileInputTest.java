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

package com.github.jinahya.bit.io.octet;


import java.io.IOException;
import java.io.RandomAccessFile;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class FileInputTest {


    @Test
    public void lazySource() {

        final ByteInput byteInput = new RandomAccessInput(null) {

            @Override
            public int read() throws IOException {
                if (source == null) {
                    source = new RandomAccessFile(
                        "com/github/jinahya/bit/io/FileInputTest.class", "r");
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        try {
                            source.close();
                        } catch (final IOException ioe) {
                            throw new RuntimeException(ioe);
                        }
                    }));
                }
                return super.read();
            }

        };
    }

}

