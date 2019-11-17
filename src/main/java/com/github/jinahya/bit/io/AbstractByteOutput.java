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
 * An abstract class for implementing {@link ByteOutput} interface.
 *
 * @param <T> byte target type parameter
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see AbstractByteInput
 */
abstract class AbstractByteOutput<T> implements ByteOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified byte target.
     *
     * @param target the underlying byte target to which bytes are written; {@code null} if it is supposed to be lazily
     *               initialized and set.
     * @see #getTarget()
     * @see #setTarget(Object)
     */
    AbstractByteOutput(final T target) {
        super();
        this.target = target;
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
               + "target=" + target
               + "}";
    }

    // ---------------------------------------------------------------------------------------------------------- target

    /**
     * Returns the current value of {@code target} attribute.
     *
     * @return the current value of {@code target} attribute.
     */
    protected T getTarget() {
        return target;
    }

    /**
     * Replaces the value of {@code target} attribute with given.
     *
     * @param target new value for {@code target} attribute.
     */
    protected void setTarget(final T target) {
        this.target = target;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The byte target to which bytes are written.
     */
    private T target;
}
