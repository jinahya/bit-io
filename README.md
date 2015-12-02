bit-io
======
[![Build Status](https://travis-ci.org/jinahya/bit-io.svg?branch=develop)](https://travis-ci.org/jinahya/bit-io)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/bit-io.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22bit-io%22)
[![Dependency Status](https://www.versioneye.com/user/projects/563ccc514d415e001e00009b/badge.svg)](https://www.versioneye.com/user/projects/563ccc514d415e001e00009b)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/53ae4f92af8246a48cbe8ecf0c04a002)](https://www.codacy.com/app/jinahya/bit-io)
[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)


A small library for reading or writing none octet aligned values such as `1-bit boolean` or `17-bit unsigned int`.

## Versions
|Version|Site|Apidocs|Notes|
|-------|----|-------|-----|
|1.1.7-SNAPSHOT|[site](http://jinahya.github.io/bit-io/sites/1.1.7-SNAPSHOT/index.html)|[apidocs](http://jinahya.github.io/bit-io/sites/1.1.7-SNAPSHOT/apidocs/index.html)||
|1.1.6|[site](http://jinahya.github.io/bit-io/sites/1.1.6/index.html)|[apidocs](http://jinahya.github.io/bit-io/sites/1.1.6/apidocs/index.html)||
|1.1.5|[site](http://jinahya.github.io/bit-io/sites/1.1.5/index.html)|[apidocs](http://jinahya.github.io/bit-io/sites/1.1.5/apidocs/index.html)||

## Usages
### Reading
#### Preparing `ByteInput`
Prepare an instance of `ByteInput` from various sources.
````java
new ArrayInput(byte[], index, limit)
new BufferInput(java.nio.ByteBuffer);
new StreamInput(java.io.InputStream);
new SupplierInput(java.util.function.Supplier<Byte>)
new IntSupplierInput(java.util.function.IntSuppiler)
````
#### Creating `BitInput`
```java
new DelegatedBitInput(ByteInput);
```
#### Reading values.
```java
final boolean b = bitInput.readBoolean();    // 1-bit boolean        1    1
final int ui6 = bitInput.readUnsignedInt(6); // 6-bit unsigned int   6    7
final long sl47 = bitInput.readLong(47);     // 47-bit signed long  47   54

final int discarded = bitInput.align(1);     // aligns to 8-bit      2   56
assert discarded == 2;

biiiiiil llllllll llllllll llllllll llllllll llllllll lllllldd
```
### Writing
#### Preparing `ByteOutput`
Prepare an instance of `ByteOutput` from various targets.
```java
new ArrayOutput(byte[], index, limit)
new BufferOutput(java.nio.ByteBuffer)
new StreamOutput(java.io.OutputStream);
new ConsumerOutput(java.util.function.Consumer<Byte>)
new IntConsumerOutput(java.util.function.IntConsumer)
````
#### Creating `BitInput`
```java
new DelegatedBitOutput(ByteOutput)
```
#### Writing values.
```java
final BitOutput bitOutput;

bitOutput.writeBoolean(true);          // 1-bit boolean        1    1
bitOutput.writeInt(7, -1);             // 7-bit signed int     7    8
bitOutput.writeUnsignedLong(33, 1L);   // 33-bit signed long  33   41

final int padded = bitOutput.align(4); // aligns to 32-bit    23   64
assert padded == 23;

biiiiiii llllllll llllllll llllllll llllllll lppppppp pppppppp pppppppp
```
----
[![Domate via Paypal](https://img.shields.io/badge/donate-paypal-blue.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_cart&business=A954LDFBW4B9N&lc=KR&item_name=GitHub&amount=5%2e00&currency_code=USD&button_subtype=products&add=1&bn=PP%2dShopCartBF%3adonate%2dpaypal%2dblue%2epng%3aNonHosted)
