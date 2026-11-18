
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

/**
 * Reads and writes non-octet-aligned values.
 *
 * <p>The module has no dependencies other than {@code java.base}. On Java 22 and later, it also contains the
 * {@code MemorySegment} adapters ({@code SegmentByteInput} and {@code SegmentByteOutput}) from
 * {@code META-INF/versions/22}.</p>
 */
module io.github.jinahya.bit.io {
    exports com.github.jinahya.bit.io;
    exports com.github.jinahya.bit.io.miscellaneous.ieee;
    exports com.github.jinahya.bit.io.miscellaneous.ietf;
    exports com.github.jinahya.bit.io.miscellaneous.itu;
    exports com.github.jinahya.bit.io.miscellaneous.microsoft;
    exports com.github.jinahya.bit.io.miscellaneous.mongodb;
    exports com.github.jinahya.bit.io.miscellaneous.sqlite;
    exports com.github.jinahya.bit.io.miscellaneous.w3c;
}
