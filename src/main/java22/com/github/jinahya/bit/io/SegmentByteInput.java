package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2026 Jinahya, Inc.
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

import java.io.EOFException;
import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

/**
 * A byte input that reads bytes from a {@link MemorySegment}, from offset {@code 0} on.
 *
 * <p>Offsets are {@code long}s, so a segment may exceed the {@code 2 GiB} limit of a {@link java.nio.ByteBuffer};
 * the segment may be on-heap, native, or memory-mapped. Reading past the end of the segment throws an
 * {@link EOFException}. Accessing a segment whose arena has been closed, or from a thread other than its owner,
 * throws the {@link IllegalStateException} or {@link WrongThreadException} of {@link MemorySegment}, unchanged.</p>
 *
 * <p>This class requires Java 22, and is present only in the Java 22 part of the multi-release jar.</p>
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see SegmentByteOutput
 */
public class SegmentByteInput
        extends AbstractByteInput<MemorySegment> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance reading from specified segment.
     *
     * @param source the segment from which bytes are read; must not be {@code null}.
     * @throws NullPointerException if {@code source} is {@code null}.
     */
    public SegmentByteInput(final MemorySegment source) {
        super(source);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "offset=" + offset
               + "}";
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read()} method of {@code SegmentByteInput} class returns the byte at the current
     * {@link #getOffset() offset} of the segment, as an unsigned 8-bit value, and increases the offset by {@code 1}.
     * The offset is left unchanged when the access fails.
     *
     * @return {@inheritDoc}
     * @throws EOFException if the current offset is not less than the size of the segment.
     * @throws IOException  {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        if (offset >= source.byteSize()) {
            throw new EOFException("end of segment reached; offset: " + offset);
        }
        final int value = source.get(ValueLayout.JAVA_BYTE, offset) & 0xFF;
        offset++; // only after a successful access
        return value;
    }

    // ---------------------------------------------------------------------------------------------------------- offset

    /**
     * Returns the offset of the next byte to read; the number of bytes read so far.
     *
     * @return the offset of the next byte to read.
     */
    public long getOffset() {
        return offset;
    }

    private long offset;
}
