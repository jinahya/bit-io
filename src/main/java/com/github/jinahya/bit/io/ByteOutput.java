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

import java.io.IOException;

/**
 * An interface for writing bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ByteInput
 */
@FunctionalInterface
public interface ByteOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes given unsigned {@value java.lang.Byte#SIZE}-bit value.
     *
     * @param value the unsigned {@value java.lang.Byte#SIZE}-bit value to write; between {@code 0} and {@code 255},
     *              both inclusive.
     * @throws IOException if an I/O error occurs.
     */
    void write(int value) throws IOException;
}
