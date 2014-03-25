bit-io
======
A small library for reading or writing none octet aligned values such as `1-bit boolean` or `17-bit unsigned int`.

## versions
* 1.1.1-SNAPSHOT ([site](http://jinahya.github.io/bit-io/site/1.1.1-SNAPSHOT/index.html)) ([apidocs](http://jinahya.github.io/bit-io/site/1.1.1-SNAPSHOT/apidocs/index.html))
* 1.1 ([site](http://jinahya.github.io/bit-io/site/1.1/index.html)) ([apidocs](http://jinahya.github.io/bit-io/site/1.1/apidocs/index.html))


## Usages
### Reading
```java
final InputStream stream;
final BitInput<?> input = new BitInput<>(new StreamInput(stream));

final ByteBuffer buffer;
final BitInput<?> input = new BitInput<>(new BufferInput(buffer));

final ReadableByteChannel channel;
final BitInput<?> input = new BitInput<>(new ChannelInput(channel));

final boolean b = input.readBoolean();    // 1-bit boolean        1    1
final int ui6 = input.readUnsignedInt(6); // 6-bit unsigned int   6    7
final long sl47 = input.readLong(47);     // 47-bit signed long  47   54

final int discarded = input.aling(1);     // aligns to 8-bit      2   56
assert discarded == 2;
```
### writing
```java
final OutputStream stream;
final BitOutput<?> output = new BitOutput<>(new StreamOutput(stream));

final ByteBuffer buffer;
final BitOutput<?> output = new BitOutput<>(new BufferOutput(buffer));

final WritableByteChannel channel;
final BitOutput<?> output = new BitOutput<>(new ChannelOutput(channel));

output.writeBoolean(true);          // 1-bit boolean        1    1
output.writeInt(7, -1);             // 7-bit signed int     7    8
output.writeUnsignedLong(33, 1L);   // 49-bit signed long  33   41

final int padded = output.aling(4); // aligns to 32-bit    23   64
assert padded == 23;
```

[wanna donate some?](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=GWDFLJNSZSEGG&lc=KR&item_name=github&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)
