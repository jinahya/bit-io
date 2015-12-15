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
|1.2.1-SNAPSHOT|[site](http://jinahya.github.io/bit-io/sites/1.2.1-SNAPSHOT/index.html)|[apidocs](http://jinahya.github.io/bit-io/sites/1.2.1-SNAPSHOT/apidocs/index.html)||
|1.2.0|[site](http://jinahya.github.io/bit-io/sites/1.2.0/index.html)|[apidocs](http://jinahya.github.io/bit-io/sites/1.2.0/apidocs/index.html)||
|1.1.5|[site](http://jinahya.github.io/bit-io/sites/1.1.5/index.html)|[apidocs](http://jinahya.github.io/bit-io/sites/1.1.5/apidocs/index.html)||

## Specifications
|Value type   |Minimum size|Maximum size|Notes|
|-------------|------------|------------|-----|
|boolean      |1           |1           ||
|unsigned int |1           |31          ||
|int          |2           |32          ||
|unsigned long|1           |63          ||
|long         |2           |64          ||

## Reading
### Preparing `ByteInput`
Prepare an instance of `ByteInput` from various sources.
````java
new ArrayInput(byte[], index, limit);
new BufferInput(java.nio.ByteBuffer);
new FileInput(java.io.RandomAccessFile);
new IntSupplierInput(java.util.function.IntSuppiler);
new StreamInput(java.io.InputStream);
new SupplierInput(java.util.function.Supplier<Byte>);
````
Those constructors don't check arguments which means you can lazily instantiate and set them.
```java
new ArrayInput(null, -1, -1) {
    @Override
    public int readUnsignedByte() throws IOException {
        if (source == null) {
            source = byte[16];
            index = 0;
            limit = source.length;
        }
        return super.readUnsignedByte();
    }
};
```
### Creating `BitInput`
#### Using `DelegatedBitInput`
Construct with an already created a `ByteInput`.
```java
final ByteInput input = createByteInput();
new DelegatedBitInput(input);
```
Or laziy instantiate its `delegate` field.
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
-      - ------- --------- -------- -------- -------- ------
biiiiiil llllllll llllllll llllllll llllllll llllllll lllllldd
 ------                                                     --
```
## Writing
### Preparing `ByteOutput`
### Creating `BitOutput`
#### Using `DelegatedBitOutput`
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
-          -                                     ---- -------- --------
biiiiiii iiblllll llllllll llllllll llllllll llllpppp pppppppp pppppppp
01101110 00100000 00000000 00000000 00000110 00110000 00000000 00000000
 ------- -- ----- -------- -------- -------- ----
```
----
[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)
