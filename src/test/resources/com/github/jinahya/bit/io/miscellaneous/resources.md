# Miscellaneous codec test resources — sources

This directory holds official (or spec-transcribed) test vectors for the codecs in
`com.github.jinahya.bit.io.miscellaneous`. Files mirror the Java package path so tests can load
them with `getResourceAsStream`.

Each vector file carries its own provenance header; this document collects the detailed sources,
their access status, and how each set was obtained and verified.

## Provenance legend

| Status           | Meaning                                                                                                                                                                      |
|------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Official**     | Values are published in the standard as test/example vectors and were copied verbatim.                                                                                       |
| **Transcribed**  | Values are example tables inside the standard's text, copied by hand and re-verified independently (the source document is not distributed as a machine-readable data file). |
| **Pointer only** | No primitive-level official vectors exist; a README points to the official whole-format conformance corpus instead.                                                          |

## Files

### `uuid/rfc9562-appendix-a.tsv` — UUID — **Official**

- **Standard:** IETF RFC 9562, *Universally Unique IDentifiers (UUIDs)* (May 2024; obsoletes RFC 4122).
  Also aligns with ITU-T X.667 / ISO/IEC 9834-8.
- **Location in source:** Appendix A *Test Vectors* — Figure 16 (v1), Figure 18 (v3), Figure 20 (v4),
  Figure 23 (v5), Figure 25 (v6), Figure 26 (v7); and Appendix B.1 Figure 27 (v8, illustrative).
- **URLs:**
    - https://www.rfc-editor.org/rfc/rfc9562.txt
    - https://www.rfc-editor.org/info/rfc9562/
    - https://datatracker.ietf.org/doc/rfc9562/
- **Codec:** `UuidRfc9562Bytes` (128-bit network byte order, RFC 9562 §4).
- **Notes:** The `bytes_hex_be` column equals the canonical hex with dashes removed, so the file is
  both the canonical-string vector and the big-endian byte-layout vector.

### `leb128/dwarf5-unsigned.tsv` — Unsigned LEB128 (ULEB128) — **Transcribed**

- **Standard:** DWARF Debugging Information Format, Version 5 (2017).
- **Location in source:** Section 7.6, Table 7.8 *"Examples of unsigned LEB128 encodings"*.
- **URL:** https://dwarfstd.org/doc/DWARF5.pdf
- **Additional vector:** `624485 → e5 8e 26` is the widely-cited WebAssembly / Wikipedia canonical
  example (https://en.wikipedia.org/wiki/LEB128, https://webassembly.github.io/spec/core/binary/values.html),
  not from the DWARF table.
- **Codec:** `Leb128Reader.UNSIGNED` / `Leb128Writer.UNSIGNED`.

### `leb128/dwarf5-signed.tsv` — Signed LEB128 (SLEB128) — **Transcribed**

- **Standard:** DWARF Debugging Information Format, Version 5 (2017).
- **Location in source:** Section 7.6, Table 7.9 *"Examples of signed LEB128 encodings"*.
- **URL:** https://dwarfstd.org/doc/DWARF5.pdf
- **Additional vector:** `-123456 → c0 bb 78` is the Wikipedia canonical example, not from the DWARF table.
- **Codec:** `Leb128Reader.SIGNED` / `Leb128Writer.SIGNED`.

### `exp-golomb/h264-table-9-2-ue.tsv` — Exp-Golomb `ue(v)` — **Transcribed**

- **Standard:** ITU-T Rec. H.264 = ISO/IEC 14496-10 (Advanced Video Coding). Identical construction in
  ITU-T H.265 = ISO/IEC 23008-2 (HEVC).
- **Location in source:** Section 9.1 *"Parsing process for Exp-Golomb codes"*, Table 9-2.
- **URL:** https://www.itu.int/rec/T-REC-H.264
- **Codec:** `ExpGolombUE`.

### `exp-golomb/h264-table-9-3-se.tsv` — Exp-Golomb `se(v)` — **Transcribed**

- **Standard:** ITU-T Rec. H.264 = ISO/IEC 14496-10.
- **Location in source:** Section 9.1.1, Table 9-3 *"Assignment of syntax element to codeNum for signed
  Exp-Golomb coded syntax elements se(v)"*. Mapping: `value(k) = (-1)^(k+1) * ceil(k/2)`, encoded as the
  `ue(v)` codeword for codeNum `k`.
- **URL:** https://www.itu.int/rec/T-REC-H.264
- **Codec:** `ExpGolombSE`.

### `asn1-length/x690-8.1.3.tsv` — ASN.1 BER/DER length octets — **Transcribed**

- **Standard:** ITU-T Rec. X.690 = ISO/IEC 8825-1 (BER/CER/DER).
- **Location in source:** Section 8.1.3 *"Length octets"* — 8.1.3.4 (short form), 8.1.3.5 (long form;
  the `435 → 82 01 b3` row is the worked example in 8.1.3.5).
- **URL:** https://www.itu.int/rec/T-REC-X.690
- **Codecs:** `Asn1BerLength`, `Asn1DerLength`.
- **Notes:** Vectors are the definite, minimal-length encodings (valid for BER, CER and DER). The BER-only
  indefinite form (single octet `0x80`) is not a determinate length value and is omitted.

### `rice-flac/README.txt` — Rice coding (FLAC) — **Pointer only**

- **Standard:** IETF RFC 9639, *Free Lossless Audio Codec (FLAC)*; Rice residual coding in §9.2.7.
- **Why no vectors:** RFC 9639 specifies the algorithm but publishes no primitive `(k, value) → bits`
  example table. The only official test material is the whole-file conformance corpus.
- **Official corpus:** https://github.com/ietf-wg-cellar/flac-test-files
- **Spec URL:** https://datatracker.ietf.org/doc/rfc9639/ (see also draft-ietf-cellar-flac)
- **Codec:** `RiceFLAC`.

### `golomb-jpegls/README.txt` — Golomb coding (JPEG-LS) — **Pointer only**

- **Standard:** ISO/IEC 14495-1 = ITU-T Rec. T.87 (JPEG-LS); limited-length Golomb / MELCODE procedure.
- **Why no vectors:** The standard text is paywalled/access-controlled and ships no freely published
  primitive `(k, value) → bits` example table; official conformance material is whole test images that
  exercise the full codec.
- **Spec URL:** https://www.itu.int/rec/T-REC-T.87
- **Codec:** `GolombJPEGLS`.

## Verification

Every numeric / bit-string vector in the `*.tsv` files was checked with an **independent** decoder
(a standalone Python script, *not* this library) to avoid self-referential "implementation tests
implementation" checks:

- ULEB128 / SLEB128: decode each `encoding_hex` back to its `value` — all match.
- UUID: each `bytes_hex_be` is 16 bytes and equals the canonical string with dashes removed.
- ASN.1 length: decode each `encoding_hex` (short + long form) back to its `length` — all match.
- Exp-Golomb: decode each `ue(v)` codeword to its `codeNum`; apply the `se(v)` mapping — all match.

## Format conventions

- All vector files are tab-separated (`*.tsv`) with `#` comment / provenance header lines.
- `*_hex` columns are space-separated bytes, most significant byte first unless noted.
- Exp-Golomb `bits` columns are MSB-first bit strings exactly as written to the stream.
