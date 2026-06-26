# bit-io

[![GitHub Action](https://github.com/jinahya/bit-io/workflows/Java%20CI/badge.svg)](https://github.com/jinahya/bit-io/actions?workflow=Java+CI)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jinahya_bit-io&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jinahya_bit-io)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.jinahya/bit-io.svg)](https://central.sonatype.com/artifact/io.github.jinahya/bit-io)
[![javadoc](https://javadoc.io/badge2/io.github.jinahya/bit-io/javadoc.svg)](https://javadoc.io/doc/io.github.jinahya/bit-io)

A small zero-dependency Java library for bit-level I/O, with retro-translated artifacts for legacy JVMs.

`bit-io` reads and writes values that are not aligned to octet boundaries, such as a `1`-bit `boolean`, a `6`-bit
unsigned `int`, or a `47`-bit signed `long`.

```java
final BitOutput output = new DefaultBitOutput(new StreamByteOutput(out));
output.writeBoolean(true);
output.writeInt(true, 6, 42);
output.writeLong(false, 47, -1L);
output.align(1);
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

Retro-translated artifacts are also published for legacy JVMs:

```xml
<dependency>
  <groupId>io.github.jinahya</groupId>
  <artifactId>bit-io</artifactId>
  <version>${bit-io.version}</version>
  <classifier>retrotranslated13</classifier>
</dependency>
```

```xml
<dependency>
  <groupId>io.github.jinahya</groupId>
  <artifactId>bit-io</artifactId>
  <version>${bit-io.version}</version>
  <classifier>retrotranslated14</classifier>
</dependency>
```

## Documentation

Guides, examples, and design notes are maintained in the
[GitHub wiki](https://github.com/jinahya/bit-io/wiki).

API reference: [javadoc.io/doc/io.github.jinahya/bit-io](https://javadoc.io/doc/io.github.jinahya/bit-io)

## Notes

- Runtime target: Java 1.6.
- Retro-translated artifacts are published with `retrotranslated13` and `retrotranslated14` classifiers.
- For a Java 8+ flavored version, see [bit-io2](https://github.com/jinahya/bit-io2).
- License: [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
