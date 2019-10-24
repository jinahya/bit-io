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
 * An interface for writing contents to a bit output.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public interface BitWritable {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes contents to specified bit output.
     *
     * @param output the bit output to which contents are written.
     * @throws IOException if an I/O error occurs.
     */
    void write(BitOutput output) throws IOException;
}
