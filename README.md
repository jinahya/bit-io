# bit-io

[![GitHub Action](https://github.com/jinahya/bit-io/workflows/Java%20CI/badge.svg)](https://github.com/jinahya/bit-io/actions?workflow=Java+CI)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jinahya_bit-io&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jinahya_bit-io)

[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/bit-io.svg)](https://img.shields.io/maven-central/v/com.github.jinahya/bit-io)
[![javadoc](https://javadoc.io/badge2/com.github.jinahya/bit-io/javadoc.svg)](https://javadoc.io/doc/com.github.jinahya/bit-io)

A library for reading/writing non octet aligned values such as `1-bit boolean` or `17-bit unsigned int`.

See [bit-io2](https://github.com/jinahya/bit-io2) for Java8+ flavored version.

## Specifications

The library reads and writes the following value types. All sizes are in **bits**.

### `boolean`

A single bit. `readBoolean()` / `writeBoolean(boolean)`.

### `byte`

Signed `1`–`8` bits, unsigned `1`–`7`. `readByte(unsigned, size)` / `writeByte(unsigned, size, byte)`.

### `short`

Signed `1`–`16` bits, unsigned `1`–`15`. `readShort(unsigned, size)` / `writeShort(unsigned, size, short)`.

### `int`

Signed `1`–`32` bits, unsigned `1`–`31`. `readInt(unsigned, size)` / `writeInt(unsigned, size, int)`.

### `long`

Signed `1`–`64` bits, unsigned `1`–`63`. `readLong(unsigned, size)` / `writeLong(unsigned, size, long)`.

### `char`

`1`–`16` bits (always unsigned). `readChar(size)` / `writeChar(size, char)`.

### `float`

**Packed:** `readFloat(exponentSize, fractionSize)` / `writeFloat(exponentSize, fractionSize, float)`, with
`exponentSize` `2`–`8` and `fractionSize` `2`–`23`, storing `1 + exponentSize + fractionSize` bits
(sign + exponent + fraction). Magnitudes that don't fit saturate to `±Infinity`, too-small ones underflow to
`±0`, and an over-wide fraction is truncated.

**Full width (lossless):** `readFloat32()` / `writeFloat32(float)` — `Float.SIZE` = 32 bits via raw IEEE-754
bits. At the native `8`/`23` widths the packed form equals this.

### `double`

**Packed:** `readDouble(exponentSize, fractionSize)` / `writeDouble(exponentSize, fractionSize, double)`, with
`exponentSize` `2`–`11` and `fractionSize` `2`–`52`; same packing and saturation rules as `float`.

**Full width (lossless):** `readDouble64()` / `writeDouble64(double)` — `Double.SIZE` = 64 bits via raw
IEEE-754 bits. At the native `11`/`52` widths the packed form equals this.

### Object references

Arbitrary types are read/written through a `BitReader<T>` / `BitWriter<T>` pair.

```java
<T> T  readObject(BitReader<? extends T> reader);
<T> void writeObject(BitWriter<? super T> writer, T value);
```

Implement `BitReader`/`BitWriter` as named classes (no lambdas on Java 1.6) to (de)serialize your own types.
Built-in implementations:

|kind        |reader / writer                                                         |notes                                            |
|------------|------------------------------------------------------------------------|-------------------------------------------------|
|byte array  |`ByteArrayReader.ofSigned`/`ofUnsigned(lengthSize, elementSize)`, `ByteArrayWriter`|length-prefixed `byte[]`, each element `elementSize` bits|
|string      |`new StringReader(lengthSize, charsetName)`, `new StringWriter(...)`     |length-prefixed bytes decoded in a named charset |
|ASCII string|`StringReader.ofAscii(lengthSize)`, `StringWriter.ofAscii(lengthSize)`   |compressed 7-bit (`Byte.SIZE - 1`) elements      |
|nullable    |`FilterBitReader.nullable(reader)`, `FilterBitWriter.nullable(writer)`   |prefixes a 1-bit nullability flag                |

## Reading

* You need to prepare an instance of `ByteInput` for reading octets.
* You can read bits from an instance of `BitInput` which uses the `ByteInput` instance.

### Preparing `ByteInput`

Prepare an instance of `ByteInput` from various sources.

````java
new ArrayByteInput(byte[]);
new BufferByteInput(java.nio.ByteBuffer);
new DataByteInput(java.io.DataInput);
new StreamByteInput(java.io.InputStream);
BufferByteInput.from(java.nio.channels.ReadableByteChannel); // write-through; blocking channels
````

### Creating `BitInput`

#### Using `DefaultBitInput`

Construct with an already existing `ByteInput`.

```java
final BitInput bitInput = new DefaultBitInput(byteInput);
```

### Reading values.

```java
final BitInput input;

final boolean b = input.readBoolean();        // 1-bit boolean        1    1
final int ui6 = input.readInt(true, 6);       // 6-bit unsigned int   6    7
final long sl47 = input.readLong(false, 47);  // 47-bit signed long  47   54

final long discarded = input.align(1);        // aligns to (1*8)-bit  2   56
assert discarded == 2L;
```

```
b        llllllll llllllll llllllll llllllll llllllll llllll
 iiiiiil                                                    dd
```

## Writing

* You need to prepare an instance of `ByteOutput` for writing octets.
* You can write bits to an instance of `BitOutput` which uses the `ByteOutput` instance.

### Preparing `ByteOutput`

There are counterpart classes and constructors mirroring those of `ByteInput`
(e.g. `ArrayByteOutput`, `BufferByteOutput`, `DataByteOutput`, `StreamByteOutput`,
and `BufferByteOutput.from(java.nio.channels.WritableByteChannel)`).

### Creating `BitOutput`

There are also counter classes and constructors to `BitInput`.

#### Using `DefaultBitOutput`

### Writing values.

```java
final BitOutput output;

output.writeBoolean(false);           // 1-bit boolean          1    1
output.writeInt(false, 9, -72);       // 9-bit signed int       9   10
output.writeBoolean(true);            // 1-bit boolean          1   11
output.writeLong(true, 33, 99L);      // 33-bit unsigned long  33   44

final long padded = output.align(4);  // aligns to (4*8)-bit   20   64
assert padded == 20L;
```

```
b          b                                     pppp pppppppp pppppppp
 iiiiiii ii lllll llllllll llllllll llllllll llll
01101110 00100000 00000000 00000000 00000110 00110000 00000000 00000000
```

## Aligning

Both `BitInput` and `BitOutput` work bit-by-bit, so after reading or writing a
number of bits that is not a multiple of `8` the stream is left in the middle of
a byte. Use `align(int bytes)` to move to a byte boundary.

```java
long align(int bytes); // bytes must be positive; returns the number of bits moved over
```

* `BitInput.align(bytes)` **discards** bits until the number of bytes read so far is
  a multiple of `bytes`, and returns the number of bits discarded.
* `BitOutput.align(bytes)` **pads** zero bits until the number of bytes written so far
  is a multiple of `bytes`, and returns the number of bits padded.

```java
final long discarded = input.align(1);  // align to the next byte; discards 0..7 bits
final long padded = output.align(1);     // align to the next byte; pads 0..7 zero bits
```

> **Important (writing):** a `BitOutput` buffers the current, partially-filled octet
> and only emits it once the byte is complete. If your last write does not end on a
> byte boundary, call `align(1)` (or any positive `bytes`) before you finish — otherwise
> the trailing bits are never written. Reading and writing must use the **same**
> `align(bytes)` argument at the same position to stay in sync.

