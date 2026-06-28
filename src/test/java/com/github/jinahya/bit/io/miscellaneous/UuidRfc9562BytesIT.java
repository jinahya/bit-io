package com.github.jinahya.bit.io.miscellaneous;

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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class UuidRfc9562BytesIT
        extends OfficialDocumentITSupport {

    @Test
    void verifiesAppendixATestVectorsFromRfc9562() throws Exception {
        final String document = downloadText("https://www.rfc-editor.org/rfc/rfc9562.txt", 512 * 1024);
        assertTrue(document.contains("Appendix A.  Test Vectors"), "downloaded RFC 9562");

        final String appendixA = document.substring(document.lastIndexOf("Appendix A.  Test Vectors"),
                                                    document.lastIndexOf("Appendix B.  Illustrative Examples"));
        final Matcher matcher =
                Pattern.compile("(?im)^\\s*final:\\s*([0-9a-f]{8}(?:-[0-9a-f]{4}){3}-[0-9a-f]{12})\\s*$")
                        .matcher(appendixA);
        final Set<String> set = new LinkedHashSet<>();
        while (matcher.find()) {
            set.add(matcher.group(1).toLowerCase(Locale.ROOT));
        }
        final List<String> uuids = new ArrayList<>(set);
        assertEquals(6, uuids.size(), "RFC 9562 Appendix A final UUID count");
        for (int i = 0; i < uuids.size(); i++) {
            final byte[] bytes = hex(uuids.get(i).replace("-", ""));
            assertArrayEquals(bytes, read(UuidRfc9562Bytes.INSTANCE, bytes), "read row " + i);
            assertArrayEquals(bytes, write(UuidRfc9562Bytes.INSTANCE, bytes), "write row " + i);
        }
    }
}
