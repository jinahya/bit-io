Rice coding (FLAC) -- official test material
============================================

The FLAC standard (IETF RFC 9639, "Free Lossless Audio Codec") specifies Rice
coding of residuals in Section 9.2.7 (Residual coding) but does NOT publish a
numeric example table mapping individual (parameter, value) pairs to bit strings.
There is therefore no official primitive-level Rice vector file to transcribe here.

The only official, authoritative test material is the whole-file conformance corpus
maintained by the IETF CELLAR working group:

    https://github.com/ietf-wg-cellar/flac-test-files

Those are complete .flac streams that exercise the full decoder (headers, subframes,
partitioned Rice residuals, etc.), not the isolated Rice primitive implemented by
RiceFLAC. To use them you must decode a whole frame, which is out of scope for a
unit test of the Rice codec.

Recommendation for RiceFLAC unit tests:
  - Derive (parameter k, unsigned value n) -> bit string vectors directly from the
    RFC 9639 Section 9.2.7 algorithm (unary quotient n>>k + k-bit remainder) and keep
    them in a *.tsv next to this file with the derivation noted; or
  - Add an integration test that decodes a file from the corpus above.

Do NOT commit hand-rolled vectors without citing the exact RFC 9639 section they were
derived from, to avoid self-referential (impl-tests-impl) checks.
