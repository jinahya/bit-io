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

import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

/**
 * A byte output that writes bytes to a {@link MemorySegment}, from offset {@code 0} on.
 *
 * <p>Offsets are {@code long}s, so a segment may exceed the {@code 2 GiB} limit of a {@link java.nio.ByteBuffer};
 * the segment may be on-heap, native, or memory-mapped. Writing past the end of the segment throws an
 * {@link IOException}. Writing to a read-only segment throws its {@link IllegalArgumentException}, and accessing
 * a segment whose arena has been closed, or from a thread other than its owner, throws its
 * {@link IllegalStateException} or {@link WrongThreadException}, unchanged.</p>
 *
 * <p>This class requires Java 22, and is present only in the Java 22 part of the multi-release jar.</p>
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see SegmentByteInput
 */
public class SegmentByteOutput
        extends AbstractByteOutput<MemorySegment> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance writing to specified segment.
     *
     * @param target the segment to which bytes are written; must not be {@code null}.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public SegmentByteOutput(final MemorySegment target) {
        super(target);
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
     * {@inheritDoc} The {@code write(int)} method of {@code SegmentByteOutput} class sets the byte at the current
     * {@link #getOffset() offset} of the segment, and increases the offset by {@code 1}.
     *
     * @param value {@inheritDoc}
     * @throws IOException if the current offset is not less than the size of the segment.
     */
    @Override
    public void write(final int value) throws IOException {
        if (offset >= target.byteSize()) {
            throw new IOException("segment is full; offset: " + offset);
        }
        target.set(ValueLayout.JAVA_BYTE, offset++, (byte) value);
    }

    // ---------------------------------------------------------------------------------------------------------- offset

    /**
     * Returns the offset of the next byte to write; the number of bytes written so far.
     *
     * @return the offset of the next byte to write.
     */
    public long getOffset() {
        return offset;
    }

    private long offset;
}
