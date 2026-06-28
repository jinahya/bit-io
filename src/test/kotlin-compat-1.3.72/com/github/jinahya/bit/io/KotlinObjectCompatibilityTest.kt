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
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class KotlinObjectCompatibilityTest {

    @Test
    fun `byte array and string readers and writers are usable from Kotlin`() {
        val target = ByteArrayOutputStream()
        val output = BitOutputs.from(target)
        val bytes = byteArrayOf(1, 2, 15)
        output.writeObject(ByteArrayWriter.ofUnsigned(4, 4), bytes)
        output.writeObject(StringWriter.ofAscii(8), "Kotlin")
        output.writeObject(StringWriter(8, "UTF-8"), "compat")
        output.align(1)

        val input = BitInputs.from(ByteArrayInputStream(target.toByteArray()))
        assertArrayEquals(bytes, input.readObject(ByteArrayReader.ofUnsigned(4, 4)))
        assertEquals("Kotlin", input.readObject(StringReader.ofAscii(8)))
        assertEquals("compat", input.readObject(StringReader(8, "UTF-8")))
    }

    @Test
    fun `java generic bit reader and writer SAMs work with nullable wrappers`() {
        val textWriter = BitWriter<String> { output, value ->
            output.writeObject(StringWriter.ofAscii(8), value)
        }
        val textReader = BitReader<String> { input ->
            input.readObject(StringReader.ofAscii(8))
        }
        val nullableWriter: BitWriter<String> = BitWriters.nullable(textWriter)
        val nullableReader: BitReader<String> = BitReaders.nullable(textReader)

        val target = ByteArrayOutputStream()
        val output = BitOutputs.from(target)
        nullableWriter.write(output, "present")
        nullableWriter.write(output, null)
        output.align(1)

        val input = BitInputs.from(ByteArrayInputStream(target.toByteArray()))
        assertEquals("present", nullableReader.read(input))
        assertNull(nullableReader.read(input))
    }
}
