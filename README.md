bit-io
======
A small library for reading or writing none octet aligned values such as `1-bit boolean` or `17-bit unsigned int`. Available at [Maven Central Repository](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.jinahya%22%20AND%20a%3A%22bit-io%22).

## Versions
|Version|Site|Apidocs|
|-------|----|-------|
|1.1.1-SNAPSHOT|[site](http://jinahya.github.io/bit-io/site/1.1.1-SNAPSHOT/index.html)|[apidocs](http://jinahya.github.io/bit-io/site/1.1.1-SNAPSHOT/apidocs/index.html)|
|1.1|[site](http://jinahya.github.io/bit-io/site/1.1/index.html)|[apidocs](http://jinahya.github.io/bit-io/site/1.1/apidocs/index.html)|

## Usages
### Reading
#### Creating instances
```java
final InputStream source;
final ByteInput byteInput = new StreamInput(source);
final BitInput bitInput = new BitInput(byteInput);
final BitInput bitInput = BitInput.newInsatnce(source); // direct

final ByteBuffer source;
final ByteInput byteInput = new BufferInput(source);
final BitInput bitInput = new BitInput(byteInput);
final BitInput bitInput = BitInput.newInstance(source); // direct
```
#### Reading values.
```java
final BitInput bitInput;

final boolean b = bitInput.readBoolean();    // 1-bit boolean        1    1
final int ui6 = bitInput.readUnsignedInt(6); // 6-bit unsigned int   6    7
final long sl47 = bitInput.readLong(47);     // 47-bit signed long  47   54

final int discarded = bitInput.aling(1);     // aligns to 8-bit      2   56
assert discarded == 2;

biiiiiil llllllll llllllll llllllll llllllll llllllll lllllldd
```
### Writing
#### Creating instances
```java
final OutputStream target;
final ByteOutput byteOutput = new StreamOutput(target);
final BitOutput bitInput = new BitOutput(byteOutput);
final BitOutput bitInput = BitOutput.newInstance(target); // direct

final ByteBuffer target;
final ByteOutput byteOutput = new BufferOutput(target);
final BitOutput bitOutput = new BitOutput(byteOutput);
final BitOutput bitOutput = BitOutput.newInstance(target); // direct
```
#### Writing values.
```java
final BitOutput bitOutput;

bitOutput.writeBoolean(true);          // 1-bit boolean        1    1
bitOutput.writeInt(7, -1);             // 7-bit signed int     7    8
bitOutput.writeUnsignedLong(33, 1L);   // 33-bit signed long  33   41

final int padded = bitOutput.aling(4); // aligns to 32-bit    23   64
assert padded == 23;

biiiiiii llllllll llllllll llllllll llllllll lppppppp pppppppp pppppppp pppppppp
```

#### [Wanna donate some?](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=GWDFLJNSZSEGG&lc=KR&item_name=github&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)
