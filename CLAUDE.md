# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**bit-io** is a Java library for reading/writing non-octet-aligned values (e.g., 1-bit boolean, 17-bit unsigned int). It targets Java 1.6 source compatibility (tests use Java 8). The library is also verified against Java 1.5 and Android API level 9 signatures via animal-sniffer.

A newer Java 8+ version exists at [bit-io2](https://github.com/jinahya/bit-io2).

## Build Commands

```bash
# Build and run all tests
mvn clean verify

# Run tests only
mvn test

# Run a single test class
mvn test -Dtest=DefaultBitInputTest

# Run a single test method
mvn test -Dtest=DefaultBitInputTest#testMethodName

# Skip tests
mvn clean install -DskipTests

# Generate site reports (includes JaCoCo coverage, PMD, Checkstyle)
mvn site
```

## Architecture

The library uses a two-layer design in package `com.github.jinahya.bit.io`:

### Byte Layer (octet I/O)
- `ByteInput` / `ByteOutput` — functional interfaces for reading/writing single unsigned bytes
- `AbstractByteInput<T>` / `AbstractByteOutput<T>` — abstract base with a generic source/target
- Concrete adapters wrapping standard Java I/O:
  - `StreamByteInput` (InputStream), `DataByteInput` (DataInput), `ArrayByteInput` (byte[]), `BufferByteInput` (ByteBuffer), `ChannelByteInput` / `ChannelByteInput2` (ReadableByteChannel)
  - Mirror `*ByteOutput` classes for writing

### Bit Layer (sub-byte I/O)
- `BitInput` / `BitOutput` — interfaces for reading/writing arbitrary bit-width values (boolean, byte, short, int, long, char) with signed/unsigned and size parameters
- `AbstractBitInput` / `AbstractBitOutput` — core bit manipulation logic; tracks bit position within the current byte; delegates single-byte reads/writes to an abstract `read()`/`write()` method
- `DefaultBitInput` / `DefaultBitOutput` — standard implementations that delegate to a `ByteInput`/`ByteOutput` instance

### Support Classes
- `BitIoConstants` — bit mask utilities
- `BitIoConstraints` — validation for size parameters (e.g., valid bit sizes for each type)

### Usage Pattern
```
ByteInput/ByteOutput  -->  DefaultBitInput/DefaultBitOutput  -->  read/write arbitrary bit widths
```

## Key Constraints

- **Source compatibility is Java 1.6** — do not use diamond operator, try-with-resources, lambdas, or any Java 7+ features in `src/main/java`
- **Test source is Java 8** — tests may use Java 8 features
- The build retrotranslates compiled classes to Java 1.3 and 1.4 bytecode (via retrotranslator) and packages them as separate classified JARs
- The library has **zero runtime dependencies** — all dependencies are test-scoped
- Tests use JUnit 5, Mockito 4.x, Weld (CDI) for injection testing, and Lombok (test-only)
- OSGi bundle manifest is generated via maven-bundle-plugin
