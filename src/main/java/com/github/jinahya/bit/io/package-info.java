/**
 * Defines interfaces and classes for reading and writing non-octet-aligned values.
 *
 * <p>The API is organized in two layers.</p>
 *
 * <p>The <em>byte layer</em> — {@link com.github.jinahya.bit.io.ByteInput} and
 * {@link com.github.jinahya.bit.io.ByteOutput} — reads and writes a single unsigned {@value java.lang.Byte#SIZE}-bit
 * value. Adapters wrap common byte sources and targets: {@link com.github.jinahya.bit.io.ArrayByteInput} (a
 * {@code byte[]}), {@link com.github.jinahya.bit.io.BufferByteInput} (a {@link java.nio.ByteBuffer}),
 * {@link com.github.jinahya.bit.io.StreamByteInput} (an {@link java.io.InputStream}), and
 * {@link com.github.jinahya.bit.io.DataByteInput} (a {@link java.io.DataInput}) — and their mirror {@code *ByteOutput}
 * classes.</p>
 *
 * <p>The <em>bit layer</em> — {@link com.github.jinahya.bit.io.BitInput} and
 * {@link com.github.jinahya.bit.io.BitOutput} — reads and writes values of an arbitrary number of bits (for example a
 * {@code 1}-bit {@code boolean} or a {@code 17}-bit unsigned {@code int}).
 * {@link com.github.jinahya.bit.io.DefaultBitInput} and {@link com.github.jinahya.bit.io.DefaultBitOutput} implement
 * the bit layer by delegating single bytes to a byte-layer instance.</p>
 *
 * <p>Typical usage stacks a bit-layer implementation on top of a byte-layer adapter:</p>
 * <blockquote><pre>{@code
 * final BitInput input = new DefaultBitInput(new StreamByteInput(stream));
 * final int value = input.readUnsignedInt(17); // a 17-bit unsigned int
 * }</pre></blockquote>
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
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


