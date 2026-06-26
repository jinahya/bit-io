Golomb coding (JPEG-LS) -- official test material
==================================================

JPEG-LS is standardized as ISO/IEC 14495-1 and ITU-T Rec. T.87. The limited-length
Golomb coding procedure (the LG(k, LIMIT) / MELCODE process) is defined in the
standard, but:

  - The standard documents are paywalled (ISO) / access-controlled (ITU-T T.87:
    https://www.itu.int/rec/T-REC-T.87 ), and
  - No freely published, primitive-level Golomb example table (mapping individual
    (k, value) pairs to bit strings) is available, and
  - The official conformance material is a set of whole test IMAGES that exercise the
    full JPEG-LS codec (prediction, context modeling, run mode, Golomb coding), not the
    isolated Golomb primitive implemented by GolombJPEGLS.

There is therefore no official primitive-level vector file to transcribe here.

Recommendation for GolombJPEGLS unit tests:
  - Derive (k, value) -> bit string vectors directly from the ITU-T T.87 Golomb
    coding clause and keep them in a *.tsv next to this file, citing the exact clause; or
  - Cross-check against an independent JPEG-LS reference implementation rather than
    against this library's own encoder (to avoid self-referential checks).
