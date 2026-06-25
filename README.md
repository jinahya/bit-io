# bit-io

[![GitHub Action](https://github.com/jinahya/bit-io/workflows/Java%20CI/badge.svg)](https://github.com/jinahya/bit-io/actions?workflow=Java+CI)
[![CircleCI](https://circleci.com/gh/jinahya/bit-io/tree/develop.svg?style=svg)](https://circleci.com/gh/jinahya/bit-io/tree/develop)
[![Build Status](https://travis-ci.org/jinahya/bit-io.svg?branch=develop)](https://travis-ci.org/jinahya/bit-io)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.github.jinahya%3Abit-io%3Adevelop&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.jinahya%3Abit-io%3Adevelop)
[![Known Vulnerabilities](https://snyk.io//test/github/jinahya/bit-io/badge.svg?targetFile=pom.xml)](https://snyk.io//test/github/jinahya/bit-io?targetFile=pom.xml)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/bit-io.svg)](https://img.shields.io/maven-central/v/com.github.jinahya/bit-io)
[![javadoc](https://javadoc.io/badge2/com.github.jinahya/bit-io/javadoc.svg)](https://javadoc.io/doc/com.github.jinahya/bit-io)

A library for reading/writing non octet aligned values such as `1-bit boolean` or `17-bit unsigned int`.

See [bit-io2](https://github.com/jinahya/bit-io2) for Java8+ flavored version.

## Specifications

#### boolean

|type     |size(min)|size(max)|notes                                   |
|---------|---------|---------|----------------------------------------|
|`boolean`|1        |1        |`readBoolean()`, `writeBoolean(boolean)`|

### numeric

#### integral

The size(min) is `1` and the size(max) is `2^e - (unsigned ? 1 : 0)`.

|type   |e  |size(min)|size(max)|notes                                                           |
|-------|---|---------|---------|----------------------------------------------------------------|
|`byte` |3  |1        |7/8      |`readByte(unsigned, size)`, `writeByte(unsigned, size, byte)`   |
|`short`|4  |1        |15/16    |`readShort(unsigned, size)`, `writeShort(unsigned, size, short)`|
|`int`  |5  |1        |31/32    |`readInt(unsigned, size)`, `writeInt(unsigned, size, int)`      |
|`long` |6  |1        |63/64    |`readLong(unsigned, size)`, `writeLong(unsigned, size, long)`   |
|`char` |   |1        |16       |`readChar(size)`, `writeChar(size, char)`                       |

#### floating-point

No methods supplied for floating-point types.

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

----
[![Donate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)
