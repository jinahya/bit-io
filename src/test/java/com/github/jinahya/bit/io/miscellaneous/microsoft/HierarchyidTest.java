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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link Hierarchyid} against [MS-SSCLRT] section 2.2 (HIERARCHYID Structure) and section 3.2 (its examples).
 *
 * @see <a href="https://learn.microsoft.com/en-us/openspecs/sql_server_protocols/ms-ssclrt/77460aa9-8c2f-4449-a65e-1d649ebd77fa">
 * [MS-SSCLRT]</a>
 */
class HierarchyidTest {

    /**
     * The ranges of section 2.2: the <code>L<sub>i</sub></code> prefix, the number of
     * <code>O<sub>i</sub></code> bits with the anti-ambiguity bits, and the range.
     */
    private static final Object[][] SECTION_2_2_RANGES = {
            {"000100", 53, -281479271682120L, -4294971465L},
            {"000101", 36, -4294971464L, -4169L},
            {"000110", 15, -4168L, -73L},
            {"0010", 8, -72L, -9L},
            {"00111", 3, -8L, -1L},
            {"01", 2, 0L, 3L},
            {"100", 2, 4L, 7L},
            {"101", 3, 8L, 15L},
            {"110", 8, 16L, 79L},
            {"1110", 13, 80L, 1103L},
            {"11110", 15, 1104L, 5199L},
            {"111110", 36, 5200L, 4294972495L},
            {"111111", 53, 4294972496L, 281479271683151L}
    };

    // ------------------------------------------------------------------------------------------------ section 3.2

    @Test
    void rootIsEmptyBinaryString() {
        assertSame(Hierarchyid.ROOT, Hierarchyid.parse("/"));
        assertArrayEquals(new byte[0], Hierarchyid.ROOT.getBytes());
        assertSame(Hierarchyid.ROOT, Hierarchyid.decode(new byte[0]));
        assertEquals("/", Hierarchyid.ROOT.toString());
        assertEquals(0, Hierarchyid.ROOT.getLevel());
    }

    @Test
    void section32Example1() {
        // 01 01 1 + 000 padding
        assertBinary("/1/", 0x58);
    }

    @Test
    void section32Example2() {
        // 01 01 1 | 00111 111 0 | 110 00001010 1 | 000000 padding
        assertBinary("/1/-2.18/", 0x59, 0xFB, 0x05, 0x40);
    }

    // ------------------------------------------------------------------------------------------------ section 2.2

    @Test
    void everyRangeBoundaryUsesItsPrefixAndSize() {
        for (final Object[] row : SECTION_2_2_RANGES) {
            final String prefix = (String) row[0];
            final int size = prefix.length() + (Integer) row[1] + 1; // L + O + F
            for (final long value : new long[]{(Long) row[2], (Long) row[3]}) {
                final String string = "/" + value + "/";
                final byte[] bytes = Hierarchyid.parse(string).getBytes();
                assertEquals((size + Byte.SIZE - 1) / Byte.SIZE, bytes.length, string);
                assertEquals(prefix, bits(bytes).substring(0, prefix.length()), string);
                assertEquals('1', bits(bytes).charAt(size - 1), string + ": F bit");
                assertEquals(string, Hierarchyid.decode(bytes).toString());
            }
        }
    }

    @Test
    void integersOutsideTheRangesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> Hierarchyid.parse("/-281479271682121/"));
        assertThrows(IllegalArgumentException.class, () -> Hierarchyid.parse("/281479271683152/"));
        // a fake level holds the integer plus one
        assertThrows(IllegalArgumentException.class, () -> Hierarchyid.parse("/281479271683151.0/"));
        Hierarchyid.parse("/281479271683150.0/");
        Hierarchyid.parse("/-281479271682121.0/");
    }

    @Test
    void fakeLevelsRoundTrip() {
        for (final String string : new String[]{"/0.3.-7/", "/0.1/0.2/", "/-1.-1/", "/1/3/", "/99/-7.13/4294972496/"}) {
            final Hierarchyid value = Hierarchyid.parse(string);
            assertEquals(string, value.toString());
            assertEquals(value, Hierarchyid.decode(value.getBytes()));
            assertEquals(string, Hierarchyid.decode(value.getBytes()).toString());
        }
    }

    @Test
    void binaryFormIsLimitedToMaxBytes() {
        // a level of 4294972496 takes 6 + 53 + 1 = 60 bits
        assertEquals(885, Hierarchyid.of(repeat(new long[]{4294972496L}, 118)).getBytes().length);
        assertThrows(IllegalArgumentException.class, () -> Hierarchyid.of(repeat(new long[]{4294972496L}, 119)));
        assertThrows(IllegalArgumentException.class, () -> Hierarchyid.decode(new byte[Hierarchyid.MAX_BYTES + 1]));
    }

    // ---------------------------------------------------------------------------------------------------- ordering

    @Test
    void comparesInDepthFirstOrder() {
        final List<Hierarchyid> expected = new ArrayList<Hierarchyid>();
        for (final String string : new String[]{
                "/", "/-1/", "/0/", "/1/", "/1/1/", "/1/1/5/", "/1/2/", "/1.1/", "/1.1/3/", "/2/", "/100/"}) {
            expected.add(Hierarchyid.parse(string));
        }
        final List<Hierarchyid> actual = new ArrayList<Hierarchyid>(expected);
        Collections.reverse(actual);
        Collections.sort(actual);
        assertEquals(expected, actual);
    }

    // ------------------------------------------------------------------------------------------------- malformed

    @Test
    void parseRejectsInvalidRepresentations() {
        for (final String string : new String[]{"", "1/", "/1", "//", "/1//", "/1..2/", "/.1/", "/a/", "/1 /"}) {
            assertThrows(IllegalArgumentException.class, () -> Hierarchyid.parse(string), string);
        }
        assertThrows(NullPointerException.class, () -> Hierarchyid.parse(null));
    }

    @Test
    void decodeRejectsInvalidBinaryForms() {
        // more than 7 padding bits
        assertThrows(IllegalArgumentException.class, () -> Hierarchyid.decode(new byte[]{0x58, 0x00}));
        assertThrows(IllegalArgumentException.class, () -> Hierarchyid.decode(new byte[]{0x00}));
        // unknown prefix 000000
        assertThrows(IllegalArgumentException.class, () -> Hierarchyid.decode(new byte[]{0x01}));
        // /1/ followed by a truncated level
        assertThrows(IllegalArgumentException.class, () -> Hierarchyid.decode(new byte[]{0x59}));
        // a prefix of the 53-bit range in a single byte
        assertThrows(IllegalArgumentException.class, () -> Hierarchyid.decode(new byte[]{0x10}));
        // 0010 00 1 0 1 000 1: the first anti-ambiguity bit of 0010..0.1... must be 0
        assertThrows(IllegalArgumentException.class,
                     () -> Hierarchyid.decode(new byte[]{0x22, (byte) 0x88}));
        assertThrows(NullPointerException.class, () -> Hierarchyid.decode(null));
    }

    @Test
    void ofRejectsInvalidLabels() {
        assertThrows(IllegalArgumentException.class,
                     () -> Hierarchyid.of(Collections.singletonList(new long[0])));
        assertThrows(NullPointerException.class, () -> Hierarchyid.of(null));
        assertThrows(NullPointerException.class,
                     () -> Hierarchyid.of(Collections.<long[]>singletonList(null)));
    }

    // --------------------------------------------------------------------------------------------------- methods

    @Test
    void ofCopiesLabels() {
        final long[] label = {1L, 2L};
        final Hierarchyid value = Hierarchyid.of(Collections.singletonList(label));
        label[0] = 9L;
        assertEquals("/1.2/", value.toString());
        value.getLabels().get(0)[0] = 9L;
        assertEquals("/1.2/", value.toString());
        assertThrows(UnsupportedOperationException.class, () -> value.getLabels().clear());
        value.getBytes()[0] = 0;
        assertEquals(Hierarchyid.parse("/1.2/"), value);
    }

    @Test
    void levelsAndAncestors() {
        final Hierarchyid node = Hierarchyid.parse("/1/2.3/4/");
        assertEquals(3, node.getLevel());
        assertSame(node, node.getAncestor(0));
        assertEquals(Hierarchyid.parse("/1/2.3/"), node.getAncestor(1));
        assertEquals(Hierarchyid.parse("/1/"), node.getAncestor(2));
        assertEquals(Hierarchyid.ROOT, node.getAncestor(3));
        assertNull(node.getAncestor(4));
        assertThrows(IllegalArgumentException.class, () -> node.getAncestor(-1));
    }

    @Test
    void descendants() {
        final Hierarchyid node = Hierarchyid.parse("/1/2.3/");
        assertTrue(node.isDescendantOf(node));
        assertTrue(node.isDescendantOf(Hierarchyid.ROOT));
        assertTrue(node.isDescendantOf(Hierarchyid.parse("/1/")));
        assertFalse(node.isDescendantOf(Hierarchyid.parse("/1/2/")));
        assertFalse(node.isDescendantOf(Hierarchyid.parse("/1/2.3/4/")));
        assertFalse(Hierarchyid.parse("/1.1/").isDescendantOf(Hierarchyid.parse("/1/")));
        assertThrows(NullPointerException.class, () -> node.isDescendantOf(null));
    }

    @Test
    void equalsAndHashCodeFollowBinaryForm() {
        final Hierarchyid a = Hierarchyid.parse("/1/-2.18/");
        final Hierarchyid b = Hierarchyid.decode(a.getBytes());
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(0, a.compareTo(b));
        assertNotEquals(a, Hierarchyid.parse("/1/-2.17/"));
        assertNotEquals(a, "/1/-2.18/");
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static void assertBinary(final String string, final int... expected) {
        final byte[] bytes = new byte[expected.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) expected[i];
        }
        assertArrayEquals(bytes, Hierarchyid.parse(string).getBytes(), string);
        assertEquals(string, Hierarchyid.decode(bytes).toString());
    }

    private static String bits(final byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        for (final byte b : bytes) {
            for (int i = Byte.SIZE - 1; i >= 0; i--) {
                builder.append((b >> i) & 1);
            }
        }
        return builder.toString();
    }

    private static List<long[]> repeat(final long[] label, final int count) {
        final List<long[]> labels = new ArrayList<long[]>();
        for (int i = 0; i < count; i++) {
            labels.add(label);
        }
        return labels;
    }
}
