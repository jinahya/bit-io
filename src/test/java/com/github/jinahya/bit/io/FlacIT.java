package com.github.jinahya.bit.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class FlacIT {

    // -----------------------------------------------------------------------------------------------------------------
    public static long utf8(final BitInput input) throws IOException {
        long result = input.readInt(true, Byte.SIZE);
        if (result <= 0b01111111L) {
            return result;
        }
        final int bytes = Integer.numberOfLeadingZeros(~((byte) result)) - 24;
        result = result & (0b01111111 >> bytes);
        for (int i = 1; i < bytes; i++) {
            result <<= 6;
            result |= (input.readInt(true, Byte.SIZE) & 0b00111111);
        }
        return result;
    }

    private static int int32(final BitInput input) throws IOException {
        return input.readInt(false, Integer.SIZE);
    }

    private static long long64(final BitInput input) throws IOException {
        return input.readLong(false, Long.SIZE);
    }

    private static byte[] read(final BitInput input, final byte[] bytes) throws IOException {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) input.readInt(false, Byte.SIZE);
        }
        return bytes;
    }

    private static ByteBuffer read(final BitInput input, final ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            buffer.put((byte) input.readInt(false, Byte.SIZE));
        }
        buffer.flip();
        return buffer;
    }

    // ----------------------------------------------------------------------------------- METADATA_BLOCK_STREAMINFO (0)
    static void printMetadataBlockStreaminfo(final BitInput input) throws IOException {
        final int minimumBlockSize = input.readInt(true, 16);
        log.debug("minimum block size: {} (in samples)", minimumBlockSize);
        final int maximumBlockSize = input.readInt(true, 16);
        log.debug("maximum block size: {} (in samples)", maximumBlockSize);
        final int minimumFrameSize = input.readInt(true, 24);
        log.debug("minimum frame size: {} (in bytes)", minimumFrameSize);
        final int maximumFrameSize = input.readInt(true, 24);
        log.debug("maximum frame size: {} (in bytes)", maximumFrameSize);
        final int sampleRate = input.readInt(true, 20);
        log.debug("sample rate: {} (in Hz)", sampleRate);
        final int numberOfChannels = input.readInt(true, 3) + 1;
        log.debug("number of channels: {}", numberOfChannels);
        final int bitsPerSample = input.readInt(true, 5) + 1;
        log.debug("bits per sample: {}", bitsPerSample);
        final long totalSamples = input.readLong(true, 36);
        log.debug("total samples: {}", totalSamples);
        final byte[] signature = new byte[16];
        for (int i = 0; i < signature.length; i++) {
            signature[i] = (byte) input.readInt(false, Byte.SIZE);
        }
        log.debug("signature: {}", signature);
    }

    // -------------------------------------------------------------------------------------- METADATA_BLOCK_PADDING (1)
    static void printMetadataBlockPadding(final BitInput input, final int length) throws IOException {
        log.debug("--------------------------------------------------------------------------- METADATA_BLOCK_PADDING");
        final byte[] data = read(input, new byte[length]);
        log.debug("data: {}", data);
    }

    // ---------------------------------------------------------------------------------- METADATA_BLOCK_APPLICATION (2)
    static void printMetadataBlockApplication(final BitInput input, final int length) throws IOException {
        log.debug("----------------------------------------------------------------------- METADATA_BLOCK_APPLICATION");
        final int id = int32(input);
        log.debug("id: {}", id);
        final byte[] data = read(input, new byte[length - Integer.BYTES]);
        log.debug("data: {}", data);
    }

    // ------------------------------------------------------------------------------------ METADATA_BLOCK_SEEKTABLE (3)
    static void printMetadataBlockSeektable(final BitInput input, int length) throws IOException {
        log.debug("------------------------------------------------------------------------- METADATA_BLOCK_SEEKTABLE");
        for (; length > 0; length -= 18) {
            final long sampleNumber = long64(input);
            log.debug("sample number: {}", sampleNumber);
            final long offset = long64(input);
            log.debug("offset: {}", offset);
            final int numberOfSamples = input.readInt(true, 16);
            log.debug("number of samples: {}", numberOfSamples);
        }
    }

    // --------------------------------------------------------------------------------------------------------------- 4
    static void printMetadataBlockVorbisComment(final BitInput input) throws IOException {
        log.debug("-------------------------------------------------------------------- METADATA_BLOCK_VORBIS_COMMENT");
        final int vendorLength = read(input, allocate(4)).order(LITTLE_ENDIAN).asIntBuffer().get();
        log.debug("vendor_length: {}", vendorLength);
        final byte[] vendorString = read(input, new byte[vendorLength]);
        log.debug("vendor_string: {}", new String(vendorString, UTF_8));
        final int userCommentListLength = read(input, allocate(4)).order(LITTLE_ENDIAN).asIntBuffer().get();
        assertTrue(userCommentListLength >= 0);
        for (int i = 0; i < userCommentListLength; i++) {
            final int length = read(input, allocate(4)).order(LITTLE_ENDIAN).asIntBuffer().get();
            assertTrue(length >= 0);
            final byte[] userComment = read(input, new byte[length]);
            log.debug("user command: {}", new String(userComment, UTF_8));
        }
        //final int framingBit = input.readInt(true, 1);
        //log.debug("framing_bit: {}", framingBit);
    }

    // ----------------------------------------------------------------------------------------- META_BLOCK_QUESHEET (5)
    static void printMetadataBlockCuesheet(final BitInput input) throws IOException {
        log.debug("-------------------------------------------------------------------------- METADATA_BLOCK_CUESHEET");
        final byte[] mediaCatalogNumber = read(input, new byte[16]);
        log.debug("m: {}", mediaCatalogNumber);
        log.debug("media catalog number: {}", new String(mediaCatalogNumber, US_ASCII));
    }

    // -------------------------------------------------------------------------------------- METADATA_BLOCK_PICTURE (6)
    static void printMetadataBlockPicture(final BitInput input) throws IOException {
        log.debug("--------------------------------------------------------------------------- METADATA_BLOCK_PICTURE");
        final int type = input.readInt(false, Integer.SIZE);
        log.debug("type: {}", type);
        final String mimeType = new String(read(input, new byte[int32(input)]), US_ASCII);
        log.debug("mime: {}", mimeType);
        final String description = new String(read(input, new byte[int32(input)]), UTF_8);
        log.debug("description: {}", description);
        final int width = int32(input);
        log.debug("width: {}", width);
        final int height = int32(input);
        log.debug("height: {}", height);
        final int depth = int32(input);
        log.debug("depth: {}", depth);
        final int colors = int32(input);
        log.debug("colors: {}", colors);
        final byte[] data = read(input, new byte[int32(input)]);
        log.debug("data: {}", data);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static void print(final BitInput input) throws IOException {
        // ---------------------------------------------------------------------------  0x66, followed by 0x4C 0x61 0x43
        assertEquals('f', input.readChar(Byte.SIZE));
        assertEquals('L', input.readChar(Byte.SIZE));
        assertEquals('a', input.readChar(Byte.SIZE));
        assertEquals('C', input.readChar(Byte.SIZE));
        // --------------------------------------------------------------------------------------------- METADATA_BLOCK+
        boolean last;
        do {
            last = input.readBoolean();
            log.debug("----------------------------------------------------------------------------------------------");
            log.debug("last: {}", last);
            final int type = input.readInt(true, 7);
            log.debug("type: {}", type);
            final int length = input.readInt(true, 24);
            log.debug("length: {}", length);
            switch (type) {
                case 0: // STREAMINGINFO
                    printMetadataBlockStreaminfo(input);
                    break;
                case 1: // PADDING
                    printMetadataBlockPadding(input, length);
                    break;
                case 2: // APPLICATION
                    printMetadataBlockApplication(input, length);
                    break;
                case 3: // SEEKTABLE
                    break;
                case 4: // VORBIS_COMMENT
                    printMetadataBlockVorbisComment(input);
                    break;
                case 5: // CUESHEET
                    printMetadataBlockCuesheet(input);
                    break;
                case 6: // PICTURE
                    printMetadataBlockPicture(input);
                    break;
                case 127: // INVALID
                    break;
                default: // 7-126: reserved
                    break;
            }
        } while (!last);
        // ------------------------------------------------------------------------------------------------ FRAME_HEADER
        log.debug("------------------------------------------------------------------------------------- FRAME_HEADER");
        final int syncCode = input.readInt(true, 14);
        assertEquals(0b11111111111110, syncCode);
        input.readInt(true, 1); // reserved
        final int blockingStrategy = input.readInt(true, 1);
        log.debug("blocking strategy: {}", blockingStrategy);
        int blockSize = input.readInt(true, 4);
        log.debug("block size: {}", blockSize);
        int sampleRate = input.readInt(true, 4);
        log.debug("sample rate: {}", sampleRate);
        final int channelAssignment = input.readInt(true, 4);
        log.debug("channel assignment: {}", channelAssignment);
        final int sampleSize = input.readInt(true, 4);
        log.debug("sample size: {}", sampleSize);
        input.readInt(true, 1); // reserved
        final long sampleNumber = utf8(input);
        log.debug("sample number: {}", sampleNumber);
        if (blockSize == 0b0110) {
            blockSize = input.readInt(true, Byte.SIZE);
        } else if (blockSize == 0b0111) {
            blockSize = input.readInt(true, Short.SIZE);
        }
        log.debug("block size: {}", blockSize);
        if (sampleRate == 0b1100) {
            sampleRate = input.readInt(true, Byte.SIZE);
        } else if (sampleRate == 0b1101) {
            sampleRate = input.readInt(true, Short.SIZE);
        }
        log.debug("sample rate: {}", sampleRate);
        final int crc = input.readInt(true, Byte.SIZE);
        log.debug("crc: {}", crc);

        // --------------------------------------------------------------------------------------------------- SUBFRAME+
        // --------------------------------------------------------------------------------------------- SUBFRAME_HEADER
        assertEquals(0, input.readInt(true, 1)); // padding
        final int subframeType = input.readInt(true, 6);
        if (subframeType == 0b000000) {
            log.debug("---------------------------------------------------------------------------- SUBFRAME_CONSTANT");
            final int constant = input.readInt(true, sampleRate);
            log.debug("constant: {}", constant);
        } else if (subframeType == 0b000001) {
            log.debug("--------------------------------------------------------------------------- SUBFRAME_VERBATIME");

        } else if (subframeType >> 1 == 1) {
            // reserved
        } else if (subframeType >> 2 == 1) {
            // reserved
        } else if (subframeType >> 3 == 1) {
            final int order = subframeType & 0b111;
            if (order <= 4) {
                log.debug("--------------------------------------------------------------------------- SUBFRAME_FIXED");
                final int warmUpSamples = input.readInt(true, sampleRate * order);
                log.debug("unencoded warm up samples: {}", warmUpSamples);
                final int residualCodingMethod = input.readInt(true, 2);
            } else {
                // reserved;
            }
        } else if (subframeType >> 4 == 1) {
            // reserved
        } else {
            assert subframeType >> 5 == 1;
        }

        // -------------------------------------------------------------------------------------------------------------
        input.align(1);

        // ------------------------------------------------------------------------------------------------ FRAME_FOOTER
        log.debug("------------------------------------------------------------------------------------- FRAME_FOOTER");
        final int crc16 = input.readInt(true, Short.SIZE);
        log.debug("crc16: {}", crc16);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void Sample_BeeMoved_96kHz24bit() throws IOException {
        final String spec = "https://helpguide.sony.net/high-res/sample1/v1/data/Sample_BeeMoved_96kHz24bit.flac.zip";
        final HttpURLConnection connection = (HttpURLConnection) new URL(spec).openConnection();
        connection.setDoInput(true); // default
        connection.setDoOutput(false); // default
        connection.connect();
        try {
            try (ZipInputStream zis = new ZipInputStream(connection.getInputStream())) {
                final ZipEntry entry = zis.getNextEntry();
                assertNotNull(entry);
                final BitInput input = new DefaultBitInput(new StreamByteInput(zis));
                print(input);
            }
        } finally {
            connection.disconnect();
        }
    }
}
