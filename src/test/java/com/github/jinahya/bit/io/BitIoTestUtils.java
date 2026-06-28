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

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Utilities for testing classes.
 */
@Slf4j
final class BitIoTestUtils {

    private static void _a(final Consumer<? super BitOutput> outputConsumer, final ByteOutput byteOutput) {
        final BitOutput bitOutput = new DefaultBitOutput(byteOutput);
        outputConsumer.accept(bitOutput);
        try {
            bitOutput.align(1);
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private static void _b(final Consumer<? super BitInput> inputConsumer, final ByteInput byteInput) {
        final BitInput bitInput = new DefaultBitInput(byteInput);
        inputConsumer.accept(bitInput);
        try {
            bitInput.align(1);
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    static void stream(final Consumer<? super BitOutput> consumer1, final Consumer<? super BitInput> consumer2) {
        ByteIoTestUtils.stream_(
                o -> _a(consumer1, o),
                i -> _b(consumer2, i)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitIoTestUtils() {
        super();
    }
}
