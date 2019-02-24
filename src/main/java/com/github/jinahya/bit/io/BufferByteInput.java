package com.github.jinahya.bit.io;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/**
 * A {@link ByteInput} uses an instance of {@link ByteBuffer} as its {@link #source}.
 *
 * @param <T> byte buffer type parameter.
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteOutput
 */
public class BufferByteInput<T extends ByteBuffer> extends AbstractByteInput<T> {

    // -----------------------------------------------------------------------------------------------------------------
    @SuppressWarnings({"Duplicates"})
    public static BufferByteInput<ByteBuffer> of(final int capacity, final ReadableByteChannel channel) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity(" + capacity + ") <= 0");
        }
        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        return new BufferByteInput<ByteBuffer>(null) {
            @Override
            public int read() throws IOException {
                if (source == null) {
                    source = ByteBuffer.allocate(capacity); // position: zero, limit: capacity
                    source.position(source.limit());
                }
                if (!source.hasRemaining()) { // no bytes to read
                    source.clear(); // position -> zero, limit -> capacity
                    do {
                        if (channel.read(source) == -1) {
                            throw new EOFException("the channel has reached end-of-stream");
                        }
                    } while (source.position() == 0);
                    source.flip(); // limit -> position, position -> zero
                }
                return super.read();
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance built on top of the specified byte buffer.
     *
     * @param source the byte buffer; {@code null} if it's supposed to be lazily initialized and set.
     */
    public BufferByteInput(final T source) {
        super(source);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read()} method of {@code BufferByteInput} invokes {@link ByteBuffer#get()}, on what
     * {@link #getSource()} gives, and returns the result as an unsigned 8-bit {@code int}. Override this method if
     * {@link #source} is supposed to be lazily initialized or adjusted.
     *
     * @return {@inheritDoc }
     * @throws IOException {@inheritDoc}
     * @see #source
     * @see ByteBuffer#get()
     */
    @Override
    public int read() throws IOException {
        return getSource().get() & 0xFF;
    }

    // ---------------------------------------------------------------------------------------------------------- source

    /**
     * {@inheritDoc}
     *
     * @param source {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BufferByteInput<T> source(final T source) {
        return (BufferByteInput<T>) super.source(source);
    }
}
