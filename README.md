bit-io
======
[![Build Status](https://travis-ci.org/jinahya/bit-io.svg?branch=develop)](https://travis-ci.org/jinahya/bit-io)
[ ![Codeship Status for jinahya/bit-io](https://app.codeship.com/projects/7e73e9f0-73f4-0135-cdd9-5adc5c5e3db7/status?branch=develop)](https://app.codeship.com/projects/243842)
[![Dependency Status](https://www.versioneye.com/user/projects/563ccc514d415e001e00009b/badge.svg)](https://www.versioneye.com/user/projects/563ccc514d415e001e00009b)
[![Known Vulnerabilities](https://snyk.io/test/github/jinahya/bit-io/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/jinahya/bit-io?targetFile=pom.xml)
[![Sputnik](https://sputnik.ci/conf/badge)](https://sputnik.ci/app#/builds/jinahya/bit-io)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/bit-io.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22bit-io%22)
[![Javadocs](http://www.javadoc.io/badge/com.github.jinahya/bit-io.svg)](http://www.javadoc.io/doc/com.github.jinahya/bit-io)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/53ae4f92af8246a48cbe8ecf0c04a002)](https://www.codacy.com/app/jinahya/bit-io)

A library for reading/writing non octet aligned values such as `1-bit boolean` or `17-bit unsigned int`.

## Specifications
#### boolean
|type     |size(min)|size(max)|notes|
|---------|---------|---------|-----|
|`boolean`|1        |1        |`readBoolean()`, `writeBoolean(boolean)`|
### numeric
#### integral
The size(min) is `1` and the size(max) is `2^e - (unsigned ? 1 : 0)`.

|type   |e  |size(min)|size(max)|notes
|-------|---|---------|---------|-----
|`byte` |3  |1        |7/8      |`readByte(unsigned, size)`, `readByte(unsigned, size, byte)`|
|`short`|4  |1        |15/16    |`readShort(unsigned, size)`, `writeShort(unsigned, size, short)`|
|`int`  |5  |1        |31/32    |`readInt(unsigned, size)`, `writeInt(unsigned, size, int)`|
|`long` |6  |1        |63/64    |`readLong(unsigned, size)`, `writeLong(unsigned, size, long)`|
|`char` |   |1        |16       |`readChar(size)`, `writeChar(size, char)`|
#### floating-point
`float`s and `double`s can be read/written as`int`s and `long`s, respectively, using `xxxToRawYYYBits` and `yyyBitsToXXX`.

## Reading
### Preparing `ByteInput`
Prepare an instance of `ByteInput` from various sources.
````java
new ArrayByteInput(byte[], int, int);
new BufferByteInput(java.nio.ByteBuffer);
new DataByteInput(java.io.DataInput);
new StreamByteInput(java.io.InputStream);
````
Constructors of these classes don't check arguments which means you can lazily instantiate and set them.
```java
final ByteInput input = new ArrayByteInput(null, -1, -1) {
    @Override
    public int read() throws IOException {
        // initialize the `source` field value
        if (source == null) {
            source = byte[16];
            index = source.length;
            limit = source.length;
        }
        // read bytes from the stream if empty
        if (index >= limit) {
            limit = stream.read(source);
            if (limit == -1) {
                throw new EOFException("unexpected end of stream");
            }
            index = 0;
        }
        return super.read();
    }
};
```
### Creating `BitInput`
#### Using `DefaultBitInput`
Construct with an already existing `ByteInput`.
```java
final ByteInput byteInput = createByteInput();
final BitInput bitInput = new DefalutBitInput<>(byteInput);
```
Or lazliy instantiate its `delegate` field.
```java
new DefaultBitInput<StreamByteInput>(null) {
    @Override
    public int read() throws IOException {
        if (delegate == null) {
            delegate = new StreamByteInput(openFile());
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
biiiiiil llllllll llllllll llllllll llllllll llllllll lllllldd
```
## Writing
### Preparing `ByteOutput`
### Creating `BitOutput`
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
biiiiiii iiblllll llllllll llllllll llllllll llllpppp pppppppp pppppppp
01101110 00100000 00000000 00000000 00000110 00110000 00000000 00000000
```
----
[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)
