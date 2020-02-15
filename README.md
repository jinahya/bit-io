# bit-io

[![GitHub Action](https://github.com/jinahya/bit-io/workflows/Java%20CI/badge.svg)](https://github.com/jinahya/bit-io/actions?workflow=Java+CI)
[![CircleCI](https://circleci.com/gh/jinahya/bit-io/tree/develop.svg?style=svg)](https://circleci.com/gh/jinahya/bit-io/tree/develop)
[![Build Status](https://travis-ci.org/jinahya/bit-io.svg?branch=develop)](https://travis-ci.org/jinahya/bit-io)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.github.jinahya%3Abit-io%3Adevelop&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.jinahya%3Abit-io%3Adevelop)
[![Known Vulnerabilities](https://snyk.io//test/github/jinahya/bit-io/badge.svg?targetFile=pom.xml)](https://snyk.io//test/github/jinahya/bit-io?targetFile=pom.xml)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/bit-io.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22bit-io%22)
[![javadoc](https://javadoc.io/badge2/com.github.jinahya/bit-io/javadoc.svg)](https://javadoc.io/doc/com.github.jinahya/bit-io)

A library for reading/writing non octet aligned values such as `1-bit boolean` or `17-bit unsigned int`.

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
new ArrayByteInput(byte[], int);
new BufferByteInput(java.nio.ByteBuffer);
new DataByteInput(java.io.DataInput);
new StreamByteInput(java.io.InputStream);
````

Constructors of these classes don't check arguments which means you can lazily instantiate and set them.

```java
final ByteInput byteInput = new ArrayByteInput(null) {
    @Override
    public int read() throws IOException {
        // initialize the `source` field value
        if (getSource() == null) {
            setSource(byte[16]);
            setIndex(getSource().length); // set as already drained
        }
        // read bytes from the original stream if empty
        if (getIndex() == getSource().length) {
            if (stream.readFully(getSource()) == -1) {
                throw new EOFException("unexpected end of stream");
            }
            setIndex(0);
        }
        return super.read();
    }
};
```

### Creating `BitInput`

#### Using `DefaultBitInput`

Construct with an already existing `ByteInput`.

```java
final BitInput bitInput = new DefalutBitInput(byteInput);
```

Or lazily instantiate its `delegate` field.

```java
final BitInput bitInput = new DefaultBitInput(null) {
    @Override
    public int read() throws IOException {
        if (getDelegate() == null) {
            setDelegate(newByteInput());
        }
        return super.read();
    }
};

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
* You can write bits to an instance of `BitInput` which uses the `ByteOutput` instance.

### Preparing `ByteOutput`

There are counter classes and contructors to `ByteInput`.

### Creating `BitOutput`

There are also counter classes and constructors to `BitInput`.

#### Using `DefalutBitOutput`

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

----
[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)
