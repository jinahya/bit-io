/*
 * Copyright 2013 Jin Kwon <jinahya at gmail.com>.
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


package com.github.jinahya.io.bit;


import java.io.IOException;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
public class ByteInputTest {


    private static class UnsignedByteAnswer implements Answer<Integer> {


        public UnsignedByteAnswer(final long limit) {

            super();

            if (limit < -1L) {
                throw new IllegalArgumentException(
                    "limit(" + limit + ") < -1L");
            }

            this.limit = limit;

            count = 0L;
        }


        @Override
        public Integer answer(final InvocationOnMock invocation)
            throws Throwable {

            if (limit != -1L && count++ >= limit) {
                return -1;
            }

            return (int) (System.currentTimeMillis() & 0xFF);
        }


        private final long limit;


        private volatile long count;


    }


    public static ByteInput<?> mock(final long limit) throws IOException {

        if (limit < -1L) {
            throw new IllegalArgumentException("limit(" + limit + ") < -1L");
        }

        final ByteInput<?> mock = Mockito.mock(ByteInput.class);

        Mockito.when(mock.readUnsignedByte())
            .thenAnswer(new UnsignedByteAnswer(limit));

        return mock;
    }


}

