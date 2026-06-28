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
package com.github.jinahya.bit.io

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Verifies that the Java API is usable from Kotlin.
 */
internal class KotlinCompatibilityTest {

    @Test
    fun `write then read round-trips an unsigned int, a boolean and a long`() {
        val baos = ByteArrayOutputStream()
        val output: BitOutput = DefaultBitOutput(StreamByteOutput(baos))
        output.writeInt(true, 5, 17)
        output.writeBoolean(true)
        output.writeLong(false, 40, -123456789L)
        val paddedOnWrite = output.align(1)
        assertTrue(paddedOnWrite >= 0L)

        val input: BitInput = DefaultBitInput(StreamByteInput(ByteArrayInputStream(baos.toByteArray())))
        assertEquals(17, input.readInt(true, 5))
        assertEquals(true, input.readBoolean())
        assertEquals(-123456789L, input.readLong(false, 40))
        val paddedOnRead = input.align(1)
        assertEquals(paddedOnWrite, paddedOnRead)
    }
}
