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
import java.io.DataInput
import java.io.DataInputStream
import java.io.DataOutput
import java.io.DataOutputStream
import java.nio.ByteBuffer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class KotlinFactoryCompatibilityTest {

    @Test
    fun `static factories are callable with Kotlin overload resolution`() {
        val streamTarget = ByteArrayOutputStream()
        val streamOutput: BitOutput = BitOutputs.from(streamTarget)
        streamOutput.writeInt(true, 5, 31)
        streamOutput.align(1)
        val streamInput: BitInput = BitInputs.from(ByteArrayInputStream(streamTarget.toByteArray()))
        assertEquals(31, streamInput.readInt(true, 5))

        val arrayTarget = ByteArray(1)
        val arrayOutput: BitOutput = BitOutputs.from(arrayTarget)
        arrayOutput.writeByte(true, 7, 0x5A.toByte())
        arrayOutput.align(1)
        val arrayInput: BitInput = BitInputs.from(arrayTarget)
        assertEquals(0x5A.toByte(), arrayInput.readByte(true, 7))

        val buffer = ByteBuffer.allocate(2)
        val bufferOutput: BitOutput = BitOutputs.from(buffer)
        bufferOutput.writeChar(7, 'K')
        bufferOutput.align(1)
        buffer.flip()
        val bufferInput: BitInput = BitInputs.from(buffer)
        assertEquals('K', bufferInput.readChar(7))

        val dataTarget = ByteArrayOutputStream()
        val dataOutput: BitOutput = BitOutputs.from(DataOutputStream(dataTarget) as DataOutput)
        dataOutput.writeBoolean(true)
        dataOutput.align(1)
        val dataInput: BitInput = BitInputs.from(
            DataInputStream(ByteArrayInputStream(dataTarget.toByteArray())) as DataInput
        )
        assertTrue(dataInput.readBoolean())
    }
}
