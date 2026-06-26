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

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Utilities for testing classes.
 */
@Slf4j
final class ByteIoTestUtils {

    static Supplier<byte[]> array_(final Consumer<? super ByteOutput> consumer) {
        final byte[] array = new byte[1048576];
        consumer.accept(new ArrayByteOutput(array));
        return () -> array;
    }

    static Supplier<byte[]> stream_(final Consumer<? super ByteOutput> consumer) {
        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            consumer.accept(new StreamByteOutput(baos));
            baos.flush();
            return baos::toByteArray;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Supplier<byte[]> data_(final Consumer<? super ByteOutput> consumer) {
        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(baos)) {
            consumer.accept(new DataByteOutput(dos));
            dos.flush();
            return baos::toByteArray;
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    static Supplier<byte[]> buffer_(final Consumer<? super ByteOutput> consumer) {
        final ByteBuffer buffer = ByteBuffer.allocate(1048576);
        consumer.accept(new BufferByteOutput(buffer));
        return () -> {
            buffer.flip();
            final byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            return bytes;
        };
    }

    @SuppressWarnings("unchecked")
    static Supplier<byte[]> accept(final Consumer<? super ByteOutput> consumer) {
        final List<Method> methods = new ArrayList<>();
        for (final Method method : ByteIoTestUtils.class.getDeclaredMethods()) {
            if (method.isSynthetic() || !method.getName().endsWith("_")) {
                continue; // not a '_'-suffixed builder (array_/stream_/data_/buffer_)
            }
            if (!Supplier.class.isAssignableFrom(method.getReturnType())) {
                continue; // must return a Supplier<byte[]>
            }
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1 || !parameterTypes[0].equals(Consumer.class)) {
                continue; // must take a single Consumer<? super ByteOutput>
            }
            methods.add(method);
        }
        final Method method = methods.get(ThreadLocalRandom.current().nextInt(methods.size()));
        try {
            return (Supplier<byte[]>) method.invoke(null, consumer);
        } catch (final IllegalAccessException iae) {
            throw new RuntimeException(iae);
        } catch (final InvocationTargetException ite) {
            final Throwable cause = ite.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new RuntimeException(cause);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    static void array_(final Consumer<? super ByteOutput> consumer1, final Consumer<? super ByteInput> consumer2) {
        final byte[] bytes = array_(consumer1).get();
        consumer2.accept(new ArrayByteInput(bytes));
    }

    static void stream_(final Consumer<? super ByteOutput> consumer1, final Consumer<? super ByteInput> consumer2) {
        final byte[] bytes = stream_(consumer1).get();
        try (final InputStream bais = new ByteArrayInputStream(bytes)) {
            consumer2.accept(new StreamByteInput(bais));
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    static void data_(final Consumer<? super ByteOutput> consumer1, final Consumer<? super ByteInput> consumer2) {
        final byte[] bytes = data_(consumer1).get();
        try (final InputStream bais = new ByteArrayInputStream(bytes)) {
            consumer2.accept(new DataByteInput(new DataInputStream(bais)));
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    static void buffer_(final Consumer<? super ByteOutput> consumer1, final Consumer<? super ByteInput> consumer2) {
        final byte[] bytes = buffer_(consumer1).get();
        consumer2.accept(new BufferByteInput(ByteBuffer.wrap(bytes)));
    }

    static void accept(final Consumer<? super ByteOutput> consumer1, final Consumer<? super ByteInput> consumer2) {
        final List<Method> methods = new ArrayList<>();
        for (final Method method : ByteIoTestUtils.class.getDeclaredMethods()) {
            if (method.isSynthetic() || !method.getName().endsWith("_")) {
                continue; // not a '_'-suffixed round-trip (array_/stream_/data_/buffer_)
            }
            if (!void.class.equals(method.getReturnType())) {
                continue; // must be a void round-trip
            }
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 2
                || !parameterTypes[0].equals(Consumer.class)
                || !parameterTypes[1].equals(Consumer.class)) {
                continue; // must take (Consumer<? super ByteOutput>, Consumer<? super ByteInput>)
            }
            methods.add(method);
        }
        final Method method = methods.get(ThreadLocalRandom.current().nextInt(methods.size()));
        try {
            method.invoke(null, consumer1, consumer2);
        } catch (final IllegalAccessException iae) {
            throw new RuntimeException(iae);
        } catch (final InvocationTargetException ite) {
            final Throwable cause = ite.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new RuntimeException(cause);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ByteIoTestUtils() {
        super();
    }
}
