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

Requires **Java 8** or later. For Java 1.3–7, use **4.2.1**, the last release line for those runtimes (including the
`retrotranslated13` and `retrotranslated14` classifier artifacts).

### Upgrading from 4.x

- The minimum runtime is Java 8.
- The deprecated aliases in `com.github.jinahya.bit.io.miscellaneous` are removed; import the same class names from
  its subpackages: `itu` (ASN.1, Exp-Golomb, JPEG-LS Golomb), `ietf` (CBOR, HPACK, FLAC Rice, UUID bytes), and `w3c`
  (LEB128). Code compiled against the old names must be recompiled.
- Wire formats are unchanged; data written by 4.x reads identically.

## Documentation

Guides, examples, and design notes are maintained in the
[GitHub wiki](https://github.com/jinahya/bit-io/wiki).

API reference: [javadoc.io/doc/io.github.jinahya/bit-io](https://javadoc.io/doc/io.github.jinahya/bit-io)

## Building

Build with JDK 8 or later (CI builds with 8 and 21; on JDK 9+ the compiler targets Java 8 with `--release 8`) and
Maven 3.9+ (or `./mvnw`):

```sh
mvn verify                    # unit + integration tests; no access to external sites needed
mvn -Pjapicmp package         # compares the public API with the previous release
mvn -Plive-sources verify     # also checks test vectors against the live standards documents
mvn -Pkotlin-compat-1.3.72 test         # Kotlin interop tests
mvn -Pkotlin-compat-2.4.20-Beta1 test
```

See [Building](https://github.com/jinahya/bit-io/wiki/Building) in the wiki for details.

## Notes

- Runtime target: Java 8.
- License: [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
