bit-io
======
a small library for reading or writing none octet aligned values such as `1-bit boolean` or `17-bit unsigned int`.

<form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="encrypted" value="-----BEGIN PKCS7-----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-----END PKCS7-----
">
<input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
<img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">
</form>


### Apache Maven
Check the [Maven Central Repository](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.jinahya%22%20AND%20a%3A%22bit-io%22) for the latest release.
### Jenkins
[jinahya.com/jenkins](https://jinahya.com/jenkins/job/com.github.jinahya%20bit-io/)
### Apidocs
* 1.0.4-SNAPSHOT ([github](http://jinahya.github.io/bit-io/site/1.0.4-SNAPSHOT/apidocs/index.html)) ([jinahya](https://jinahya.com/mvn/site/com.github.jinahya/bit-io/1.0.4-SNAPSHOT/apidocs/index.html))
* 1.0.3 ([github](http://jinahya.github.io/bit-io/site/1.0.3/apidocs/index.html)) ([jinahya](https://jinahya.com/mvn/site/com.github.jinahya/bit-io/1.0.3/apidocs/index.html))

## Reading Bits
```java
final InputStream stream;
final BitInput input = new BitInput(new StreamInput(stream));

final ByteBuffer buffer;
final BitInput input = new BitInput(new BufferInput(buffer));

final ReadableByteChannel channel;
final BitInput input = new BitInput(new ChannelInput(channel));

final boolean b = input.readBoolean();        // 1-bit boolean        1    1
final int ui6 = input.readUnsignedInt(6);     // 6-bit unsigned int   6    7
final long sl47 = input.readLong(47);         // 47-bit signed long  47   54

final int discarded = input.aling((short) 1); // aligns to 8-bit      2   56
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

output.writeBoolean(true);                  // 1-bit boolean          1    1
output.writeInt(7, -1);                     // 7-bit signed int       7    8
output.writeUnsignedLong(33, 1L);           // 49-bit signed long    33   41

final int padded = output.aling((short) 4); // aligns to 32-bit      23   64
assert padded == 23;
```
