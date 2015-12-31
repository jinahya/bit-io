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


import java.util.Map;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> value type parameter
 * @param <K> key type parameter
 * @param <V> value type parameter
 */
public abstract class MapCodec<T extends Map<K, V>, K, V>
    extends ScaleCodec<T, Map.Entry<K, V>> {


    public MapCodec(final boolean nullable, final int scale,
                    final BitCodec<Map.Entry<K, V>> codec) {
        super(nullable, scale, codec);
    }

}

