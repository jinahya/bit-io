package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
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

/**
 * An abstract class for implementing {@link ByteInput} interface.
 *
 * @param <T> byte source type parameter
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see AbstractByteOutput
 */
abstract class AbstractByteInput<T> implements ByteInput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified byte source.
     *
     * @param source the underlying byte source from which bytes are read; {@code null} if it is intended to be lazily
     *               initialized and set.
     * @see #getSource()
     * @see #setSource(Object)
     */
    AbstractByteInput(final T source) {
        super();
        this.source = source;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return super.toString() + "{"
               + "source=" + source
               + "}";
    }

    // ---------------------------------------------------------------------------------------------------------- source

    /**
     * Returns the current value of {@code source} attribute.
     *
     * @return the current value of {@code source} attribute.
     */
    protected T getSource() {
        return source;
    }

    /**
     * Replaces the current value of {@code source} attribute with given.
     *
     * @param source new value for {@code source} attribute.
     */
    protected void setSource(final T source) {
        this.source = source;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The bytes source from which byte are read.
     */
    private T source;
}
