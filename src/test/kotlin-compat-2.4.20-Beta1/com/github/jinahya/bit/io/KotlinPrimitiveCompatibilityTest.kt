package com.github.jinahya.bit.io

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class KotlinPrimitiveCompatibilityTest {

    @Test
    fun `primitive signed unsigned and endian APIs are callable from Kotlin`() {
        val target = ByteArrayOutputStream()
        val output = BitOutputs.from(target)
        output.writeShort(false, 12, (-321).toShort())
        output.writeChar16Le('한')
        output.writeFloat32Le(1.25f)
        output.writeDouble64(123.5)
        output.align(1)

        val input = BitInputs.from(ByteArrayInputStream(target.toByteArray()))
        assertEquals((-321).toShort(), input.readShort(false, 12))
        assertEquals('한', input.readChar16Le())
        assertEquals(1.25f, input.readFloat32Le())
        assertEquals(123.5, input.readDouble64())
    }
}
