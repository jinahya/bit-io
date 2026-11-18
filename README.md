# bit-io

[![GitHub Action](https://github.com/jinahya/bit-io/workflows/Java%20CI/badge.svg)](https://github.com/jinahya/bit-io/actions?workflow=Java+CI)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jinahya_bit-io&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jinahya_bit-io)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.jinahya/bit-io.svg)](https://central.sonatype.com/artifact/io.github.jinahya/bit-io)
[![javadoc](https://javadoc.io/badge2/io.github.jinahya/bit-io/javadoc.svg)](https://javadoc.io/doc/io.github.jinahya/bit-io)

A small zero-dependency Java library for bit-level I/O, with retro-translated artifacts for legacy JVMs.

`bit-io` reads and writes values that are not aligned to octet boundaries, such as a `1`-bit `boolean`, a `6`-bit
unsigned `int`, or a `47`-bit signed `long`.

```java
final BitOutput output = BitOutputs.from(out);
output.writeBoolean(true);          // 1 bit
output.writeUnsignedInt(6, 42);     // 6-bit unsigned int
output.writeLong(47, -1L);          // 47-bit signed long
output.align(1);                    // flush the trailing partial byte
```

## Install

```xml
<dependency>
  <groupId>io.github.jinahya</groupId>
  <artifactId>bit-io</artifactId>
  <version>${bit-io.version}</version>
</dependency>
```

Use the [Maven Central page](https://central.sonatype.com/artifact/io.github.jinahya/bit-io) for the latest released
version.

Requires **Java 17** or later. For Java 1.3–16, use **4.2.1**, the last release line for those runtimes (including the
`retrotranslated13` and `retrotranslated14` classifier artifacts).

The jar is a named module, `io.github.jinahya.bit.io`, with no dependencies other than `java.base`. It is also a
multi-release jar: `SegmentByteInput` and `SegmentByteOutput`, which read and write a `java.lang.foreign.MemorySegment`,
are in its Java 22 part, so they require Java 22 or later; every other class runs on Java 17.

`jar --validate` reports those two classes ("contains a new public class not found in base entries") and exits with 1.
This is intentional: the multi-release format asks every version of a jar to expose the same public API, but these
classes take a `MemorySegment`, which Java 17 doesn't have. The JVM, build tools, and OSGi don't apply that check; on
Java 17–21 the classes are simply absent.

### Upgrading from 4.x

- The minimum runtime is Java 17.
- The deprecated aliases in `com.github.jinahya.bit.io.miscellaneous` are removed; import the same class names from
  its subpackages: `itu` (ASN.1, Exp-Golomb, JPEG-LS Golomb), `ietf` (CBOR, HPACK, FLAC Rice, UUID bytes), and `w3c`
  (LEB128). Code compiled against the old names must be recompiled.
- Wire formats are unchanged; data written by 4.x reads identically.
- The `java.util.UUID` codecs and mapping filters of [bit-io-ext](https://github.com/jinahya/bit-io-ext) are now part of
  bit-io: `miscellaneous.ietf.UuidRfc9562`, `miscellaneous.microsoft.MicrosoftGuid` (with `MicrosoftGuidBytes`), and
  `FilterBitReaders.mapping` / `FilterBitWriters.mapping`.

## Documentation

Guides, examples, and design notes are maintained in the
[GitHub wiki](https://github.com/jinahya/bit-io/wiki).

API reference: [javadoc.io/doc/io.github.jinahya/bit-io](https://javadoc.io/doc/io.github.jinahya/bit-io)

## Building

Build with JDK 25 and Maven 3.9+ (or `./mvnw`). The jar targets Java 17 (`--release 17`), except for the
`MemorySegment` adapters in `src/main/java22` (`--release 22`); the tests compile with `--release 25`:

```sh
mvn verify                    # unit + integration tests; no access to external sites needed
mvn -Pjapicmp package         # compares the public API with the previous release
mvn -Plive-sources verify     # also checks test vectors against the live standards documents
mvn -Pkotlin-compat-2.4.20-Beta1 test   # Kotlin interop tests
```

See [Building](https://github.com/jinahya/bit-io/wiki/Building) in the wiki for details.

## Notes

- Runtime target: Java 17; Java 22 for the `MemorySegment` adapters.
- License: [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
