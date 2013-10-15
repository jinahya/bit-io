bit-io
======
a small library for reading or writing none octet aligned values such as `1-bit boolean` or `17-bit unsigned int`.

### Apache Maven
Check the [Maven Central Repository](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.jinahya%22%20AND%20a%3A%22bit-io%22) for the latest release.
### Jenkins
[jinahya.com/jenkins](https://jinahya.com/jenkins/job/com.github.jinahya%20bit-io/)
### Apidocs
* [1.0.4-SNAPSHOT](http://jinahya.github.io/bit-io/site/1.0.4-SNAPSHOT/apidocs/index.html)
* [1.0.3](http://jinahya.github.io/bit-io/site/1.0.3/apidocs/index.html)

## Reading Bits
```java
final InputStream stream;
final BitInput input = new BitInput(new StreamInput(stream));

final ByteBuffer buffer;
final BitInput input = new BitInput(new BufferInput(buffer));

final ReadableByteChannel channel;
final BitInput input = new BitInput(new ChannelInput(channel));

final boolean b = input.readBoolean();    // 1-bit boolean            1    1
final int ui6 = input.readUnsignedInt(6); // 6-bit unsigned int       6    7
final long sl47 = input.readLong(47);     // 47-bit signed long      47   54

final int discarded = input.aling((short) 1);    // aligns to 8-bit   2   56
assert discarded == 2;
```
## Writing Bits
```java
final OutputStream stream;
final BitOutput output = new BitOutput(new StreamOutput(stream));

final ByteBuffer buffer;
final BitOutput output = new BitOutput(new BufferOutput(buffer));

final WritableByteChannel channel;
final BitOutput output = new BitOutput(new ChannelOutput(channel));

output.writeBoolean(true);          // 1-bit boolean                  1    1
output.writeInt(7, -1);             // 7-bit signed int               7    8
output.writeUnsignedLong(33, 1L);   // 49-bit signed long            33   41

final int padded = output.aling((short) 4); // aligns to 32-bit      23   64
assert padded == 23;
```
