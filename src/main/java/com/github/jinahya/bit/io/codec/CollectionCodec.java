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


import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.BitOutput;
import java.io.IOException;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> collection type parameter
 * @param <E> element type parameter
 */
public class CollectionCodec<T extends Collection<Object>, E>
    extends ScaleCodec<T, E> {


    public CollectionCodec(final boolean nullable, final int scale,
                           final BitCodec<E> codec, final Class<?> type) {

        super(nullable, scale, codec);

        this.type = type;
    }


    @Override
    @SuppressWarnings("unchecked")
    protected T decodeValue(final BitInput input) throws IOException {

        final TypeVariable<?>[] typeParameters = getClass().getTypeParameters();
        for (final TypeVariable<?> typeParameter : typeParameters) {
            System.out.println("typeParamter: " + typeParameter);
            final GenericDeclaration genericDeclaration = typeParameter.getGenericDeclaration();
            System.out.println("\tgenericDeclaration: " + genericDeclaration);
            final Type[] bounds = typeParameter.getBounds();
            for (final Type bound : bounds) {
                System.out.println("\tbound: " + bound);
                if (bound instanceof ParameterizedType) {
                    final Type[] actualTypeArguments = ((ParameterizedType) bound).getActualTypeArguments();
                    for (final Type actualTypeArgument : actualTypeArguments) {
                        System.out.println("\t\tactualTypeArgument: " + actualTypeArgument);
                    }
                }
            }
        }

//        final int length = readCount(input);
//
//        final Collection<E> value;
//        try {
//            value = (Collection<E>) type.newInstance();
//        } catch (final InstantiationException ie) {
//            throw new RuntimeException(ie);
//        } catch (final IllegalAccessException iae) {
//            throw new RuntimeException(iae);
//        }
//
//        for (int i = 0; i < length; i++) {
//            value.add(codec.decode(input));
//        }
        return null;
    }


    @Override
    protected void encodeValue(final BitOutput output, final T collection)
        throws IOException {

        writeCount(output, collection.size());

        for (final Object element : collection) {
            codec.encode(output, (E) element);
        }
    }


    private final Class<?> type;

}

