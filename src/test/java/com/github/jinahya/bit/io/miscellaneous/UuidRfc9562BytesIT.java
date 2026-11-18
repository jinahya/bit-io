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
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class UuidRfc9562BytesIT
        extends OfficialDocumentITSupport {

    /**
     * The final UUIDs of <a href="https://www.rfc-editor.org/rfc/rfc9562.txt">RFC 9562</a>, Appendix A, Test Vectors.
     */
    private static final List<String> APPENDIX_A_UUIDS = Arrays.asList(
            "c232ab00-9414-11ec-b3c8-9f6bdeced846", // A.1. Example of a UUIDv1 Value
            "5df41881-3aed-3515-88a7-2f4a814cf09e", // A.2. Example of a UUIDv3 Value
            "919108f7-52d1-4320-9bac-f847db4148a8", // A.3. Example of a UUIDv4 Value
            "2ed6657d-e927-568b-95e1-2665a8aea6a2", // A.4. Example of a UUIDv5 Value
            "1ec9414c-232a-6b00-b3c8-9f6bdeced846", // A.5. Example of a UUIDv6 Value
            "017f22e2-79b0-7cc3-98c4-dc0c0c07398f"  // A.6. Example of a UUIDv7 Value
    );

    @Test
    void verifiesAppendixATestVectorsFromRfc9562() throws Exception {
        final String document = checkSource("https://www.rfc-editor.org/rfc/rfc9562.txt", 512 * 1024,
                                            d -> d.contains("Appendix A.  Test Vectors"), "downloaded RFC 9562");
        if (document != null) {
            assertEquals(APPENDIX_A_UUIDS, parseAppendixA(document), "RFC 9562 Appendix A final UUIDs");
        }

        final List<String> uuids = APPENDIX_A_UUIDS;
        for (int i = 0; i < uuids.size(); i++) {
            final byte[] bytes = hex(uuids.get(i).replace("-", ""));
            assertArrayEquals(bytes, read(UuidRfc9562Bytes.INSTANCE, bytes), "read row " + i);
            assertArrayEquals(bytes, write(UuidRfc9562Bytes.INSTANCE, bytes), "write row " + i);
        }
    }

    private static List<String> parseAppendixA(final String document) {
        final String appendixA = document.substring(document.lastIndexOf("Appendix A.  Test Vectors"),
                                                    document.lastIndexOf("Appendix B.  Illustrative Examples"));
        final Matcher matcher =
                Pattern.compile("(?im)^\\s*final:\\s*([0-9a-f]{8}(?:-[0-9a-f]{4}){3}-[0-9a-f]{12})\\s*$")
                        .matcher(appendixA);
        final Set<String> set = new LinkedHashSet<>();
        while (matcher.find()) {
            set.add(matcher.group(1).toLowerCase(Locale.ROOT));
        }
        return new ArrayList<>(set);
    }
}
