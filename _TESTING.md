# bit-io Testing Strategy

Status: **draft — for review**. Phased rollout; each phase independently green.

## Goals

Keep the suite organized around behavioral contracts, not historical scaffolding. Every
public API should have deterministic unit/contract coverage, every encoding should have
byte-exact round-trip coverage, and every adapter should prove its boundary behavior against
its backing source/sink.

Baseline: `mvn test` = 238 run, 0 failures, 0 errors, 61 skipped. Primitive bit I/O is covered
by `BitRoundTripTest`; float/double reduced IEEE behavior by `BitFloatingPointRoundTripTest`.

## Test taxonomy and file map

| Layer | Test type | Files / responsibility |
|---|---|---|
| Byte layer | Interface contracts | `ByteInputContractTest`, `ByteOutputContractTest`: null/EOF/range behavior. |
| Byte layer | Adapter round-trip | `ByteIoTest`: one-byte write/read matrix over `ByteIoSource#sourceByteIo`. |
| Byte layer | Adapter boundaries | `ByteAdapterBoundaryTest`, `ChannelProgressContractTest`, `Array/Buffer/Channel/Data/Stream ByteInput/OutputTest`. |
| Bit layer | Primitive round-trip | `BitRoundTripTest`: boolean, sized byte/short/int/long/char, fixed-width, little-endian. |
| Bit layer | Floating-point encoding | `BitFloatingPointRoundTripTest`: full-width raw bits, reduced encoding, qNaN/sNaN, truncation, saturation. |
| Bit layer | Alignment / byte order | `BitAlignmentTest`, `EndiannessTest`. |
| Bit layer | Validation | `BitValidationContractTest`, `BitIoConstraintsTest`. |
| Object layer | Reader/writer contracts | `BitObjectContractTest` (`readObject`/`writeObject`, nullable filters). |
| Object layer | Codec round-trip | New `ByteArrayObjectRoundTripTest`, `StringObjectRoundTripTest`, optional `AsciiObjectRoundTripTest`. |

## Disposition of the 61 skipped tests

- **Reactivate as-is (1):** `ByteIoTest` (already uses `ByteIoSource#sourceByteIo`).
- **Rewrite to direct construction (~27 adapter tests):** the `*ByteInput/OutputTest` set. Drop
  weak Weld/CDI base classes; construct real arrays/buffers/streams/`DataInput/Output`/channels.
  Preserve: array index get/set, zero-capacity channel-buffer rejection, `BufferByteInput.from(null)`,
  `BufferByteOutput.from(null)`, read/write-through, EOF.
- **Rewrite (~17):** `DefaultBitInputTest`/`DefaultBitOutputTest` → explicit constructor/delegation
  tests over `StreamByteInput/Output`; semantics already live in the round-trip suites.
- **Delete after salvaging (~20):** `AbstractBitInputSpyTest`/`AbstractBitOutputSpyTest` (Mockito
  spies of abstract classes). Move useful `skip`/`align`/`getCount` validation into contract/validation
  tests, then delete.

## Object-layer coverage to add

- **`ByteArrayObjectRoundTripTest`:** `ofSigned`/`ofUnsigned` with element sizes (1/4/8 signed, 1/4/7
  unsigned, incl. nibbles `0x0/0x7/0xF`); empty/single/max-length arrays; length-overflow rejection;
  byte-exact layout for a compact case.
- **`StringObjectRoundTripTest`:** `StringWriter`/`StringReader` over `US-ASCII`/`UTF-8`/`UTF-16*`;
  empty/ASCII/multibyte/non-Latin; null checks; unsupported-charset path.
- **ASCII:** public factories `StringWriter.ofAscii`/`StringReader.ofAscii`; byte-exact assertion that
  ASCII uses 7-bit unsigned elements (not full 8-bit).
- **Extend `BitObjectContractTest`:** nullable writer must not call delegate for null; nullable reader
  must not call delegate when presence bit is false.

## Constraints coverage

- Table-driven boundary suite: signed sizes byte `1..8` / short `1..16` / int `1..32` / long `1..64`;
  unsigned `1..7` / `1..15` / `1..31` / `1..63`; char `1..16`; float/double exponent/fraction validators.
- **Dispatch equivalence:** `requireValidSize<T>(true, n)` == `requireValidSizeForUnsigned<T>(n)` and
  `false` == signed, including exception equivalence on invalid sizes.

## Conventions

- Behavior-named tests (`roundTripsUnsignedNibbles`, `rejectsLengthOverflow`, `alignPadsToNextByte`).
- `ByteIoSource#sourceByteIo` only for byte adapter matrix; local byte-array bit harness for bit/object
  round-trips; `BitIoTests` random generators for smoke, deterministic boundaries in contract tests.
- Encoding tests: `output.align(1)` then assert exact `byte[]`. Alignment tests: assert pad/discard
  count and bytes. Never reintroduce the removed `BitInput`/`BitOutput` byte/string convenience APIs.

## Edge / adversarial suites

- Float/double: qNaN vs sNaN, payload truncation, infinity saturation, underflow to signed zero,
  full-width raw-bit preservation.
- Alignment/skip: zero/negative args, cross-byte skips, skip-then-read/write, aligned vs unaligned.
- Length prefixes: max encodable length, one-over-max rejection, zero-length.
- Charset errors: unsupported charset name; ASCII truncation/replacement expectations.
- Adapter boundaries: EOF, closed/short channels, zero-capacity buffers, index bounds.

## Coverage and tooling

- JaCoCo: short-term 80% line / 70% branch for `com.github.jinahya.bit.io`; after cleanup 85% / 75%.
  Don't chase 100%.
- Workflow: `mvn test-compile` after structural edits; `mvn test` before each phase completion;
  `mvn site` / PMD / Checkstyle before larger reorganizations; keep animal-sniffer green (no Java 9+
  APIs in `src/main/java`).

## Phased rollout

1. **Skipped-test cleanup baseline** — reactivate `ByteIoTest`; rewrite adapter tests to direct
   construction; salvage-then-delete spy tests. Goal: skipped count drops; `mvn test` green.
2. **Constraints completeness** — split methods, dispatch equivalence, float/double validators.
3. **Object byte arrays** — `ByteArrayObjectRoundTripTest` (signed/unsigned, nibbles, boundaries, overflow).
4. **Object strings and ASCII** — `StringObjectRoundTripTest`, 7-bit ASCII factories.
5. **Adapter adversarial cases** — EOF, short read/write, channel progress, buffer position/limit, index bounds.
6. **Coverage / tooling gate** — review JaCoCo branch gaps, add high-value cases, run `mvn test` then `mvn site`.
