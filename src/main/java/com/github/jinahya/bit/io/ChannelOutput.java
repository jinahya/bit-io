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


import java.io.IOException;
import java.nio.channels.WritableByteChannel;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @deprecated
 */
@Deprecated
public class ChannelOutput extends AbstractByteOutput<WritableByteChannel> {


    public ChannelOutput(final WritableByteChannel target) {

        super(target);
    }


    @Override
    public void write(final int value) throws IOException {

        if (output == null) {
            output = BufferByteOutput.newInstance(target, 1, false);
        }

        output.write(value);
    }


    private ByteOutput output;

}

