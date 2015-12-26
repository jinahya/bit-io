bit-io
======
[![GitHub license](https://img.shields.io/github/license/jinahya/bit-io.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Dependency Status](https://www.versioneye.com/user/projects/563ccc514d415e001e00009b/badge.svg)](https://www.versioneye.com/user/projects/563ccc514d415e001e00009b)
[![Build Status](https://travis-ci.org/jinahya/bit-io.svg?branch=develop)](https://travis-ci.org/jinahya/bit-io)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/bit-io.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22bit-io%22)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/53ae4f92af8246a48cbe8ecf0c04a002)](https://www.codacy.com/app/jinahya/bit-io)
[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)

A library for reading/writing non octet aligned values such as `1-bit boolean` or `17-bit unsigned int`.

## Versions
|Version|Site|Apidocs|Notes|
|-------|----|-------|-----|
|1.3.1-SNAPSHOT|[site](http://jinahya.github.io/bit-io/sites/1.3.1-SNAPSHOT/index.html)|[apidocs](http://jinahya.github.io/bit-io/sites/1.3.1-SNAPSHOT/apidocs/index.html)||
|1.3.0|[site](http://jinahya.github.io/bit-io/sites/1.3.0/index.html)|[apidocs](http://jinahya.github.io/bit-io/sites/1.3.0/apidocs/index.html)||

## Specifications
### Primitives
### boolean
|Type     |Size(min)|Size(max)|Notes|
|---------|---------|---------|-----|
|`boolean`|1        |1        |`readBoolean()`, `writeBoolean()`|
### numeric
#### integral
|Type   |Size(min)|Size(max)|Notes|
|-------|---------|---------|-----|
|`byte` |2        |8        |`readByte(boolean, int)`, `readByte(boolean, int, byte)`|
|`short`|2        |16       |`readShort(boolean, int)`, `writeShort(boolean, int, short)`|
|`int`  |2        |32       |`readInt(boolean, int)`, `writeInt(boolean, int, int)`|
|`long` |2        |64       |`readLong(boolean, int)`, `writeLong(boolean, int, long)`|
|`char` |1        |16       |`readChar(int)`, `writeChar(int, char)`|
#### floating-point
|Type    |Size(min)|Size(max)|Notes|
|--------|---------|---------|-----|
|`float` |(32)     |(32)     |`readFloat()`, `writeFloat(float)`|
|`double`|(64)     |(64)     |`readDouble()`, `writeDouble(double)`|
### References
#### Implementing `BitReadable`/`BitWritable`
You can directly read/write values from/to `BitInput`/`BitOutput` by making your class implementing those interfaces.
```java
public class Person implements BitReadable, BitWritable {

    @Override
    public void read(final BitInput input) throws IOException {
        setAge(input.readInt(true, 7));
        setMarried(input.readBoolean());
    }

    @Override
    public void write(final BitOutput output) throws IOException {
        output.writeInt(true, 7, getAge());
        output.writeBoolean(isMarried());
    }
}
```
It's, now, too obvious you can do this.
```java
final Person person = new Person().age(31).married(true);
person.read(input);
person.write(output);
```
#### Using `BitDecoder`/`BitEncoder`
If modifying existing classes (e.g. implementing additional interfaces) is not applicable, you can make some sort of specialized classes for decoding/encoding.
```java
public class PersonDecoder extends NullableDecoder<Person> {

    public PersonDecoder(final boolean nullable) {
        super(nullable);
    }

    @Override
    public Person decode(final BitInput input) throws IOException {
        return super.decode(input);
    }

    @Override
    public Person decodeValue(final BitInput input) throws IOException {
        return new Person().age(input.readInt(true, 7)).married(input.readBoolean());
    }
}

public class PersonEncoder extends NullableEncoder<Person> {

    public PersonEncoder(final boolean nullable) {
        super(nullable);
    }

    @Override
    public void encode(final BitOutput output, final Person value) throws IOException {
        super.encode(output, value);
    }

    @Override
    public void encodeValue(final BitOutput output, final Person person) throws IOException {
        output.writeInt(true, 7, value.getAge());
        output.writeBoolean(value.isMarried());
    }
}
```
There is an abstract class for implementing these two interfaces easily (including the nullable feature).
```java
public class PersonCodec extends NullableCodec<Person> {

    public PersonCodec(final boolean nullable) {
        super(nullable);
        decoder = new PersonDecoder(false);
        encoder = new PersonEncoder(false);
    }
    
    @Override
    public Person decode(final BitInput input) throws IOException {
        return super.decode(input);
    }
    
    @Override
    public void encode(final BitOutput output, final Person value) throws IOException {
        super.encode(output, value);
    }

    @Override
    protected Person decodeValue(final BitInput input) throws IOException {
        return decoder.decode(input);
    }

    @Override
    protected void encodeValue(final BitOutput output, final Person value) throws IOException {
        encoder.encode(output, value);
    }

    private final BitDecoder<Person> decoder;
    private final BitEncoder<Person> encoder;
}
```
Again, you can use the codec like this.
```java
final PersonCodec codec = new PersonCodec(true);
final Person person = codec.decode(input));
codec.encode(output, person);
```
## Reading
### Preparing `ByteInput`
Prepare an instance of `ByteInput` from various sources.
````java
new ArrayInput(byte[], int, int);
new BufferInput(java.nio.ByteBuffer);
new DataInput(java.io.DataInput);
new FileInput(java.io.RandomAccessFile);
new StreamInput(java.io.InputStream);
````
Those constructors don't check arguments which means you can lazily instantiate and set them.
```java
final InputStream output = openFile();

final ByteInput input = new ArrayInput(null, -1, -1) {

    @Override
    public int read() throws IOException {
        if (source == null) {
            source = byte[16];
            limit = source.length;
            index = limit;
        }
        if (index == limit) {
            final int read = stream.read(source);
            if (read == -1) {
                throw new EOFException();
            }
            limit = read;
            index = 0;
        }
        return super.read();
    }
};
```
### Creating `BitInput`
#### Using `DefaultBitInput`
Construct with an already created a `ByteInput`.
```java
final ByteInput delegate = createByteInput();

final BitInput input = new DefalutBitInput(delegate);
```
Or lazliy instantiate its `delegate` field.
```java
new DefaultBitInput<InputStream>(null) {

    @Override
    public int read() throws IOException {
        if (delegate == null) {
            delegate = new StreamInput(openFile());
        }
        return super.read();
    }
};
```
#### Using `BitInputFactory`
You can create `BitInput`s using various `newInstance(...)` methods.
```java
final RedableByteChannel channel = openChannel();

final BitInput input = BitInputFactory.newInstance(
    () -> (ByteBuffer) ByteBuffer.allocate(10).position(10),
    b -> {
        if (!b.hasRemaining()) {
            b.clear();
            do {
                final int read = channel.read(b);
                if (read == -1) {
                    throw new EOFException();
                }
            } while (b.position() == 0);
            b.flip();
        }
        return b.get() & 0xFF;
    });
```
### Reading values.
```java
final BitInput input;

final boolean b = input.readBoolean();        // 1-bit boolean        1    1
final int ui6 = input.readInt(true, 6);       // 6-bit unsigned int   6    7
final long sl47 = input.readLong(false, 47);  // 47-bit signed long  47   54

final long discarded = input.align(1);        // aligns to 8-bit      2   56
assert discarded == 2L;
```
```
biiiiiil llllllll llllllll llllllll llllllll llllllll lllllldd
```
## Writing
### Preparing `ByteOutput`
### Creating `BitOutput`
#### Using `DefalutBitOutput`
#### Using `BitOutputFactory`
### Writing values.
```java
final BitOutput output;

output.writeBoolean(false);           // 1-bit boolean          1    1
output.writeInt(false, 9, -72);       // 7-bit signed int       9   10
output.writeBoolean(true);            // 1-bit boolean          1   11
output.writeLong(true, 33, 99L);      // 33-bit unsigned long  33   44

final long padded = output.align(4);  // aligns to 32-bit      20   64
assert padded == 20L;
```
```
biiiiiii iiblllll llllllll llllllll llllllll llllpppp pppppppp pppppppp
01101110 00100000 00000000 00000000 00000110 00110000 00000000 00000000
```
----
[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)
