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

package com.github.jinahya.bit.io.codec;


import java.io.IOException;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class StringCodecTest extends BitCodecTest<StringCodec, String> {


    @Test(invocationCount = 1024)
    public void utf8() throws IOException {

        final boolean nullable = current().nextBoolean();
        final int scale = current().nextInt(1, 10);

        final BitCodec<String> codec
            = new StringCodec(nullable, scale, false, 8, "UTF-8");

        final int count = current().nextInt((int) Math.pow(2, scale)) / 4;
        String value = RandomStringUtils.random(count);
        if (nullable && current().nextBoolean()) {
            value = null;
        }

        BitCodecTests.test(codec, value);
    }


    @Test(invocationCount = 1024)
    public void ascii() throws IOException {

        final boolean nullable = current().nextBoolean();
        final int scale = current().nextInt(1, 10);

        final BitCodec<String> codec
            = new StringCodec(nullable, scale, true, 7, "US-ASCII");

        final int count = current().nextInt((int) Math.pow(2, scale));
        String value = RandomStringUtils.randomAscii(count);
        if (nullable && current().nextBoolean()) {
            value = null;
        }

        BitCodecTests.test(codec, value);
    }


}

