bit-io
======

## Abstract
You can only read/write in octets(bytes) with Java.<br/>
How can you read a `17-bit-long` unsigned integer or write an `1-bit-long` boolean value?

### Apache Maven
Check the [Maven Central Repository](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.jinahya%22%20AND%20a%3A%22bit-io%22) for the latest release.
### Apidocs
* [1.0.4-SNAPSHOT](http://jinahya.github.io/bit-io/site/1.0.4-SNAPSHOT/apidocs/index.html)
* [1.0.3](http://jinahya.github.io/bit-io/site/1.0.3/apidocs/index.html)

## For Reading Bits
You can read bits from an instance of `BitInput`([src](src/main/java/com/github/jinahya/io/BitInput.java)).

### Preparing a `ByteInput`
The `BitInput` class takes one argument which must be an instance of `ByteInput`.
There are already implementations for this interface such as `StreamInput` or `BufferInput`, or `ChannelInput`.

### Creating a `BitInput`
```java
final BitInput bitInput = new BitInput(byteInput);
```

## For Writing Bits
You can read bits from an instance of `BitOutput`([src](src/main/java/com/github/jinahya/io/BitOutput.java)).

### Preparing a `ByteOutput`
The `BitOutput` class takes one argument which must be an instance of `ByteOutput`.
There are already implementations for this interface such as `StreamOutput` or `BufferOutput`, or `ChannelOutput`.

### Creating a `BitOutput`
```java
final BitOutput bitOutput = new BitOutput(byteOutput);
```

## Reading/Writing Bits

| type          | read                       | write                              | notes               |
| ------------- | -------------------------- | ---------------------------------- | ------------------- |
| boolean       | `readBoolean()`            | `writeBoolean()`                   | consumes only 1 bit |
| unsigned int  | `readUnsignedInt(length)`  | `writeUnsignedInt(length, value)`  | 1 <= length < 32    |
| signed int    | `readInt(length)`          | `writeInt(length, value`           | 1 < length <= 32    |
| unsigned long | `readUnsignedLong(length)` | `writeUnsignedLong(length, value)` | 1 <= length < 64    |
| signed long   | `readLong(length)`         | `writeLong(length, value)`         | 1 < length <= 64    |

