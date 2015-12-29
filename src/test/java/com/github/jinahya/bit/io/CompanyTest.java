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


import com.github.jinahya.bit.io.codec.BitCodec;
import com.github.jinahya.bit.io.codec.CompanyCodec;
import java.io.IOException;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class CompanyTest extends BitIoTest {


    private static final Logger logger = getLogger(CompanyTest.class);


    @Test
    public void decodeEncode() throws IOException {

        final boolean nullable = current().nextBoolean();
        final BitCodec<Company> codec = new CompanyCodec(nullable);
        codec.encode(getOutput(), codec.decode(getInput()));
    }

}

