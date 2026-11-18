package com.github.jinahya.bit.io.miscellaneous.microsoft;

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

import com.github.jinahya.bit.io.ArrayByteInput;
import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.DefaultBitInput;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteOutput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A value of SQL Server's {@code hierarchyid} type: a path of node labels from the root, each label a non-empty
 * sequence of integers; e.g., {@code /}, {@code /1/}, {@code /0.3.-7/}, {@code /1/3/}.
 *
 * <p>The binary form is the one [MS-SSCLRT] section 2.2 describes. Each integer of a label is one level,
 * <code>L<sub>i</sub> O<sub>i</sub> F<sub>i</sub></code>: a prefix selecting a range, the offset from the range's lower
 * limit with its anti-ambiguity bits, and a bit which is {@code 1} when the integer ends its label (a <em>real</em>
 * level) and {@code 0} when a dot follows it (a <em>fake</em> level, which holds the integer plus one). Zero bits pad
 * the levels to a whole byte. The root is the empty binary string.</p>
 *
 * <p>The binary form carries no length of its own; its end is the end of the binary string, as stored in a
 * {@code varbinary}. Hence this class converts whole binary strings ({@link #decode(byte[])}, {@link #getBytes()})
 * rather than reading from or writing to a bit stream.</p>
 *
 * <p>Instances compare as SQL Server compares the values, in depth-first order, by their binary forms.</p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/openspecs/sql_server_protocols/ms-ssclrt/77460aa9-8c2f-4449-a65e-1d649ebd77fa">
 * [MS-SSCLRT]: Microsoft SQL Server CLR Types Serialization Formats</a>
 * @see <a href="https://learn.microsoft.com/en-us/sql/t-sql/data-types/hierarchyid-data-type-method-reference">
 * hierarchyid data type method reference</a>
 */
public final class Hierarchyid
        implements Comparable<Hierarchyid> {

    /**
     * The maximum length of the binary form, in bytes. The value is {@value}.
     */
    public static final int MAX_BYTES = 892;

    /**
     * The root, {@code /}.
     */
    public static final Hierarchyid ROOT = new Hierarchyid(new ArrayList<long[]>(0), new byte[0]);

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A range of section 2.2, with its <code>L<sub>i</sub></code> prefix and the pattern of its
     * <code>O<sub>i</sub></code> field; in the pattern, a {@code .} is an offset bit, from the most significant, and a
     * {@code 0} or a {@code 1} is a fixed anti-ambiguity bit.
     */
    private static final class Range {

        Range(final String prefix, final String pattern, final long min) {
            super();
            this.prefix = Integer.parseInt(prefix, 2);
            this.prefixSize = prefix.length();
            this.pattern = pattern;
            int offsetSize = 0;
            for (int i = 0; i < pattern.length(); i++) {
                if (pattern.charAt(i) == '.') {
                    offsetSize++;
                }
            }
            this.offsetSize = offsetSize;
            this.min = min;
            this.max = min + (1L << offsetSize) - 1L;
        }

        void write(final BitOutput output, final long value) throws IOException {
            output.writeUnsignedInt(prefixSize, prefix);
            final long offset = value - min;
            int remaining = offsetSize;
            for (int i = 0; i < pattern.length(); ) {
                final char c = pattern.charAt(i);
                if (c != '.') {
                    output.writeBoolean(c == '1');
                    i++;
                    continue;
                }
                int run = 0;
                for (; i < pattern.length() && pattern.charAt(i) == '.'; i++) {
                    run++;
                }
                remaining -= run;
                output.writeUnsignedLong(run, (offset >>> remaining) & ((1L << run) - 1L));
            }
        }

        long read(final BitInput input) throws IOException {
            long offset = 0L;
            for (int i = 0; i < pattern.length(); ) {
                final char c = pattern.charAt(i);
                if (c != '.') {
                    if (input.readBoolean() != (c == '1')) {
                        throw new IllegalArgumentException("wrong anti-ambiguity bit in range starting at " + min);
                    }
                    i++;
                    continue;
                }
                int run = 0;
                for (; i < pattern.length() && pattern.charAt(i) == '.'; i++) {
                    run++;
                }
                offset = (offset << run) | input.readUnsignedLong(run);
            }
            return min + offset;
        }

        /**
         * Returns the number of bits following the prefix; the <code>O<sub>i</sub></code> field and the
         * <code>F<sub>i</sub></code> bit.
         *
         * @return the number of bits following the prefix.
         */
        int sizeAfterPrefix() {
            return pattern.length() + 1;
        }

        final int prefix;

        final int prefixSize;

        final String pattern;

        final int offsetSize;

        final long min;

        final long max;
    }

    /**
     * The ranges of section 2.2, in ascending order.
     */
    private static final Range[] RANGES = {
            new Range("000100", "..............0.....................0......0...0.1...", -281479271682120L),
            new Range("000101", "...................0......0...0.1...", -4294971464L),
            new Range("000110", ".....0...0.1...", -4168L),
            new Range("0010", "..0.1...", -72L),
            new Range("00111", "...", -8L),
            new Range("01", "..", 0L),
            new Range("100", "..", 4L),
            new Range("101", "...", 8L),
            new Range("110", "..0.1...", 16L),
            new Range("1110", "...0...0.1...", 80L),
            new Range("11110", ".....0...0.1...", 1104L),
            new Range("111110", "...................0......0...0.1...", 5200L),
            new Range("111111", "..............0.....................0......0...0.1...", 4294972496L)
    };

    /**
     * The maximum size of a prefix, in bits.
     */
    private static final int MAX_PREFIX_SIZE = 6;

    private static Range range(final long value) {
        for (final Range range : RANGES) {
            if (value >= range.min && value <= range.max) {
                return range;
            }
        }
        throw new IllegalArgumentException("out of range: " + value);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the value of specified node labels.
     *
     * @param labels the node labels, from the root; each a non-empty array of integers.
     * @return the value of {@code labels}.
     * @throws NullPointerException     if {@code labels} or any of its elements is {@code null}.
     * @throws IllegalArgumentException if a label is empty, an integer is out of range, or the binary form is longer
     *                                  than {@value #MAX_BYTES} bytes.
     */
    public static Hierarchyid of(final List<long[]> labels) {
        if (labels == null) {
            throw new NullPointerException("labels is null");
        }
        final List<long[]> copy = new ArrayList<long[]>(labels.size());
        for (final long[] label : labels) {
            if (label == null) {
                throw new NullPointerException("labels contains null");
            }
            copy.add(label.clone());
        }
        return new Hierarchyid(copy, encode(copy));
    }

    /**
     * Parses specified string representation, as {@code hierarchyid::Parse} does; e.g., {@code /}, {@code /1/},
     * {@code /0.3.-7/}, {@code /1/3/}.
     *
     * @param string the string representation to parse.
     * @return the value of {@code string}.
     * @throws NullPointerException     if {@code string} is {@code null}.
     * @throws IllegalArgumentException if {@code string} is not a valid representation.
     */
    public static Hierarchyid parse(final String string) {
        if (string == null) {
            throw new NullPointerException("string is null");
        }
        if (!string.startsWith("/") || !string.endsWith("/")) {
            throw new IllegalArgumentException("not a hierarchyid: " + string);
        }
        if (string.length() == 1) {
            return ROOT;
        }
        final List<long[]> labels = new ArrayList<long[]>();
        for (final String label : string.substring(1, string.length() - 1).split("/", -1)) {
            final String[] integers = label.split("\\.", -1);
            final long[] values = new long[integers.length];
            for (int i = 0; i < integers.length; i++) {
                try {
                    values[i] = Long.parseLong(integers[i]);
                } catch (final NumberFormatException nfe) {
                    final IllegalArgumentException iae = new IllegalArgumentException("not a hierarchyid: " + string);
                    iae.initCause(nfe);
                    throw iae;
                }
            }
            labels.add(values);
        }
        return new Hierarchyid(labels, encode(labels));
    }

    /**
     * Decodes specified binary form, as SQL Server stores and sends it.
     *
     * @param bytes the binary form to decode.
     * @return the value of {@code bytes}.
     * @throws NullPointerException     if {@code bytes} is {@code null}.
     * @throws IllegalArgumentException if {@code bytes} is not a valid binary form.
     */
    public static Hierarchyid decode(final byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        if (bytes.length > MAX_BYTES) {
            throw new IllegalArgumentException("longer than " + MAX_BYTES + " bytes: " + bytes.length);
        }
        // the last level is a real one, so the levels end with the last one bit; the rest is the padding
        int end = bytes.length * Byte.SIZE;
        while (end > 0 && (bytes[(end - 1) / Byte.SIZE] & (0x80 >>> ((end - 1) % Byte.SIZE))) == 0) {
            end--;
        }
        if (bytes.length != (end + Byte.SIZE - 1) / Byte.SIZE) {
            throw new IllegalArgumentException("padded with more than " + (Byte.SIZE - 1) + " bits");
        }
        if (end == 0) {
            return ROOT;
        }
        final BitInput input = new DefaultBitInput(new ArrayByteInput(bytes));
        final List<long[]> labels = new ArrayList<long[]>();
        final List<Long> label = new ArrayList<Long>();
        try {
            for (int position = 0; position < end; ) {
                int prefix = 0;
                int prefixSize = 0;
                Range range = null;
                while (range == null) {
                    if (prefixSize == MAX_PREFIX_SIZE) {
                        throw new IllegalArgumentException("unknown prefix at bit " + position);
                    }
                    if (position + prefixSize == end) {
                        throw new IllegalArgumentException("truncated level at bit " + position);
                    }
                    prefix = (prefix << 1) | (input.readBoolean() ? 1 : 0);
                    prefixSize++;
                    for (final Range candidate : RANGES) {
                        if (candidate.prefixSize == prefixSize && candidate.prefix == prefix) {
                            range = candidate;
                            break;
                        }
                    }
                }
                if (position + prefixSize + range.sizeAfterPrefix() > end) {
                    throw new IllegalArgumentException("truncated level at bit " + position);
                }
                final long value = range.read(input);
                final boolean real = input.readBoolean();
                position += prefixSize + range.sizeAfterPrefix();
                if (real) {
                    final long[] values = new long[label.size() + 1];
                    for (int i = 0; i < label.size(); i++) {
                        values[i] = label.get(i);
                    }
                    values[values.length - 1] = value;
                    labels.add(values);
                    label.clear();
                } else {
                    label.add(value - 1L);
                }
            }
        } catch (final IOException ioe) {
            throw unexpected(ioe);
        }
        return new Hierarchyid(labels, bytes.clone());
    }

    private static byte[] encode(final List<long[]> labels) {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(stream));
        try {
            for (final long[] label : labels) {
                if (label.length == 0) {
                    throw new IllegalArgumentException("empty label");
                }
                for (int i = 0; i < label.length; i++) {
                    final boolean real = i == label.length - 1;
                    if (!real && label[i] == Long.MAX_VALUE) {
                        throw new IllegalArgumentException("out of range: " + label[i]);
                    }
                    final long value = real ? label[i] : label[i] + 1L;
                    range(value).write(output, value);
                    output.writeBoolean(real);
                }
            }
            output.align(1);
        } catch (final IOException ioe) {
            throw unexpected(ioe);
        }
        final byte[] bytes = stream.toByteArray();
        if (bytes.length > MAX_BYTES) {
            throw new IllegalArgumentException("longer than " + MAX_BYTES + " bytes: " + bytes.length);
        }
        return bytes;
    }

    private static IllegalStateException unexpected(final IOException ioe) {
        final IllegalStateException ise = new IllegalStateException("unexpected I/O error on an in-memory array");
        ise.initCause(ioe);
        return ise;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private Hierarchyid(final List<long[]> labels, final byte[] bytes) {
        super();
        this.labels = labels;
        this.bytes = bytes;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the string representation of this value, as {@code hierarchyid::ToString} does.
     *
     * @return the string representation of this value.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("/");
        for (final long[] label : labels) {
            for (int i = 0; i < label.length; i++) {
                if (i > 0) {
                    builder.append('.');
                }
                builder.append(label[i]);
            }
            builder.append('/');
        }
        return builder.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Hierarchyid)) {
            return false;
        }
        return Arrays.equals(bytes, ((Hierarchyid) obj).bytes);
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (final byte b : bytes) {
            result = 31 * result + b;
        }
        return result;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Compares this value with specified value, by their binary forms compared as unsigned bytes, which is the
     * depth-first order SQL Server uses.
     *
     * @param o the value to be compared.
     * @return a negative integer, zero, or a positive integer as this value is less than, equal to, or greater than
     *         {@code o}.
     */
    @Override
    public int compareTo(final Hierarchyid o) {
        final int length = Math.min(bytes.length, o.bytes.length);
        for (int i = 0; i < length; i++) {
            final int c = (bytes[i] & 0xFF) - (o.bytes[i] & 0xFF);
            if (c != 0) {
                return c;
            }
        }
        return bytes.length - o.bytes.length;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the binary form of this value.
     *
     * @return the binary form of this value.
     */
    public byte[] getBytes() {
        return bytes.clone();
    }

    /**
     * Returns the node labels of this value, from the root.
     *
     * @return an unmodifiable list of the node labels of this value.
     */
    public List<long[]> getLabels() {
        final List<long[]> copy = new ArrayList<long[]>(labels.size());
        for (final long[] label : labels) {
            copy.add(label.clone());
        }
        return Collections.unmodifiableList(copy);
    }

    /**
     * Returns the level of this value, as {@code hierarchyid::GetLevel} does; {@code 0} for the root.
     *
     * @return the level of this value.
     */
    public int getLevel() {
        return labels.size();
    }

    /**
     * Returns the ancestor of this value specified levels up, as {@code hierarchyid::GetAncestor} does.
     *
     * @param n the number of levels to go up; must not be negative.
     * @return the ancestor {@code n} levels up; {@code null} when {@code n} is greater than the level of this value.
     * @throws IllegalArgumentException if {@code n} is negative.
     */
    public Hierarchyid getAncestor(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n(" + n + ") < 0");
        }
        if (n > labels.size()) {
            return null;
        }
        return n == 0 ? this : of(labels.subList(0, labels.size() - n));
    }

    /**
     * Checks whether this value is a descendant of specified value, as {@code hierarchyid::IsDescendantOf} does; a
     * value is a descendant of itself.
     *
     * @param parent the value to check against.
     * @return {@code true} if this value is a descendant of {@code parent}; {@code false} otherwise.
     * @throws NullPointerException if {@code parent} is {@code null}.
     */
    public boolean isDescendantOf(final Hierarchyid parent) {
        if (parent == null) {
            throw new NullPointerException("parent is null");
        }
        if (parent.labels.size() > labels.size()) {
            return false;
        }
        for (int i = 0; i < parent.labels.size(); i++) {
            if (!Arrays.equals(parent.labels.get(i), labels.get(i))) {
                return false;
            }
        }
        return true;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final List<long[]> labels;

    private final byte[] bytes;
}
