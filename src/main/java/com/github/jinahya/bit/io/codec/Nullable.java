/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
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
 */
package com.github.jinahya.bit.io.codec;

/**
 * An abstract class for implementing {@code BitCodec}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
abstract class Nullable {

    /**
     * Creates a new instance.
     *
     * @param nullable a flag for nullability of the value.
     */
    public Nullable(final boolean nullable) {

        super();

        this.nullable = nullable;
    }

    /**
     * Returns the value of {@link #nullable}.
     *
     * @return value of {@link #nullable}.
     */
    public boolean isNullable() {

        return nullable;
    }

    /**
     * a flag for nullability.
     */
    protected final boolean nullable;

}
