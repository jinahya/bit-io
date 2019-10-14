package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
 * %%
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
 * #L%
 */

import java.util.function.Consumer;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExtendedBitIoTests {

    // -----------------------------------------------------------------------------------------------------------------
    static <R> R applyRandomAscii(final int lengthSize, final Function<String, R> function) {
        BitIoConstraints.requireValidSizeInt(true, lengthSize);
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        final int length = current().nextInt(0, ((1 << lengthSize)));
        final byte[] bytes = new byte[length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) current().nextInt(0, 128);
        }
        return function.apply(new String(bytes, UTF_8));
    }

    static void acceptRandomAscii(final int lengthSize, final Consumer<String> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        assertNull(applyRandomAscii(lengthSize, v -> {
            consumer.accept(v);
            return null;
        }));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ExtendedBitIoTests() {
        super();
    }
}
