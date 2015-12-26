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

package com.github.jinahya.bit.io;


import com.github.jinahya.bit.io.octet.BufferInput;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class DefaultBitInputTest {


    @Test
    public void lazyDelegate() {

        final BitInput input = new DefaultBitInput<BufferInput>(null) {

            @Override
            public int read() throws IOException {

                if (delegate == null) {
                    final ByteBuffer buffer = ByteBuffer.allocate(0);
                    delegate = new BufferInput(buffer);
                }

                return super.read();
            }

        };
    }


    @Test
    public void lazyDelegateLasySource() {

        final BitInput input = new DefaultBitInput(null) {

            @Override
            public int read() throws IOException {

                if (delegate == null) {
                    delegate = new BufferInput(null) {

                        @Override
                        public int read() throws IOException {
                            if (source == null) {
                                source = ByteBuffer.allocate(0);
                            }
                            return super.read();
                        }

                    };
                }

                return super.read();
            }

        };
    }

}

