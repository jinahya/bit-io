bit-io
======
[![Dependency Status](https://www.versioneye.com/user/projects/563ccc514d415e001e00009b/badge.svg)](https://www.versioneye.com/user/projects/563ccc514d415e001e00009b)
[![Build Status](https://travis-ci.org/jinahya/bit-io.svg?branch=develop)](https://travis-ci.org/jinahya/bit-io)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/bit-io.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22bit-io%22)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/53ae4f92af8246a48cbe8ecf0c04a002)](https://www.codacy.com/app/jinahya/bit-io)
[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)


A small library for reading or writing none octet aligned values such as `1-bit boolean` or `17-bit unsigned int`.

## Versions
|Version|Site|Apidocs|Notes|
|-------|----|-------|-----|
|1.3.1-SNAPSHOT|[site](http://jinahya.github.io/bit-io/sites/1.3.1-SNAPSHOT/index.html)|[apidocs](http://jinahya.github.io/bit-io/sites/1.3.1-SNAPSHOT/apidocs/index.html)||
|1.3.0-SNAPSHOT|[site](http://jinahya.github.io/bit-io/sites/1.3.0-SNAPSHOT/index.html)|[apidocs](http://jinahya.github.io/bit-io/sites/1.3.0-SNAPSHOT/apidocs/index.html)||

## Specifications
### Primitives
|Value type   |Minimum size|Maximum size|Notes|
|-------------|------------|------------|-----|
|boolean      |1           |1           |`readBoolean`, `writeBoolean`|
|unsigned int |1           |31          |`readUnsignedInt(int)`, `writeUnsignedInt(int, int)`|
|int          |2           |32          |`readInt(size)`, `writeInt(int)`|
|unsigned long|1           |63          |`readUnsignedLong(size)`, `writeUnsigendLong(int, long)`|
|long         |2           |64          |`readLong(size)`, `writeLong(size)`|
|float        |32          |32          |`readFloat()`, `writeFloat(flaot)`|
|double       |64          |64          |`readDouble()`, `writeDouble(double)`|
### Objects
You can read/write custom objects using `readObject(Function<BitInput, ? extends T>)` and `writeObject(T, BiConsumer(BitOutput, ? super T))` respectively.
```java
Person person = null;
person = input.readObject((input) -> {
    if (!input.readBoolean()) {
        return null; // optional; 1-bit null flag
    }
    final Person value = new Person();
    try {
        value.setAge(input.readUnsignedInt(7));
    } catch (final IOException ioe) {
        throw new UncheckedIOException(ioe);
    }
    return value;
});
output.writeObject(person, (output, value) -> {
    if (value == null) {
        writeBoolean(false); // optional; 1-bit null flag
        return;
    }
    try {
        output.writeUnsignedInt(7, value.getAge());
    } catch (final IOException ioe) {
        throw new UncheckedIOException(ioe);
    }
});
```
## Reading
### Preparing `ByteInput`
Prepare an instance of `ByteInput` from various sources.
````java
new ArrayInput(byte[], index, limit);
new BufferInput(java.nio.ByteBuffer);
new DataInput(java.io.DataInput);
new FileInput(java.io.RandomAccessFile);
new IntSupplierInput(java.util.function.IntSuppiler);
new StreamInput(java.io.InputStream);
new SupplierInput(java.util.function.Supplier<Byte>);
````
Those constructors don't check arguments which means you can lazily instantiate and set them.
```java
final OutputStream output = openFile();
final ByteInput input = new ArrayInput(null, -1, -1) {
    @Override
    public int readUnsignedByte() throws IOException {
        if (source == null) {
            source = byte[16];
            index = 0;
            limit = source.length;
        }
        if (index == limit) {
            output.write(source);
            index = 0;
            limit = source.length;
        }
        return super.readUnsignedByte();
    }
};
```
### Creating `BitInput`
#### Using `DefaultBitInput`
Construct with an already created a `ByteInput`.
```java
final ByteInput input = createByteInput();
new DefalutBitInput(input);
```
Or lazliy instantiate its `delegate` value.
```java
new DelegatedBitInput(null) {
    @Override
    public int readUnsignedByte() throws IOException {
        if (delegate == null) {
            delegate = new BufferInput(createBuffer());
        }
        return super.readUnsignedByte();
    }
};
```
#### Using `BitInputFactory`
You can create `BitInput`s using various `newInstance(...)` methods.
```java
final BitInput input = BitInputFactory.newInstance(
    () -> (ByteBuffer) ByteBuffer.allocate(10).position(10),
    b -> {
        if (!b.hasRemaining()) {
            b.clear();
            int read;
            try {
                while ((read = source.read(b)) == 0) {
                }
            } catch (final IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
            if (read == -1) {
                throw new UncheckedIOException(new EOFException());
            }
            b.flip();
        }
        return b.get() & 0xFF;
    });
```
### Reading values.
```java
final BitInput input;

final boolean b = input.readBoolean();      // 1-bit boolean        1    1
final int   ui6 = input.readUnsignedInt(6); // 6-bit unsigned int   6    7
final long sl47 = input.readLong(47);       // 47-bit signed long  47   54

final long discarded = input.align(1);      // aligns to 8-bit      2   56
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

output.writeBoolean(false);          // 1-bit boolean          1    1
output.writeInt(9, -72);             // 7-bit signed int       9   10
output.writeBoolean(true);           // 1-bit boolean          1   11
output.writeUnsignedLong(33, 99L);   // 33-bit unsigned long  33   44

final long padded = output.align(4); // aligns to 32-bit      20   64
assert padded == 20L;
```
```
biiiiiii iiblllll llllllll llllllll llllllll llllpppp pppppppp pppppppp
01101110 00100000 00000000 00000000 00000110 00110000 00000000 00000000
```
----
[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)
