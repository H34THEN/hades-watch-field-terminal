# K0R34D3R Integration Plan

## Repository

- **URL:** https://github.com/H34THEN/K0R34D3R
- **Local inspection path:** `/home/heathen/Projects/K0R34D3R-inspection`
- **Repo available locally:** Yes (cloned for inspection)

## Observed Structure

K0R34D3R is a **Flutter/Dart multi-platform application**, not a Kotlin/Android library.

```
K0R34D3R/
├── lib/services/text_processing_service.dart   # Core RSVP logic
├── lib/models/app_settings.dart                # WPM, chunk modes, UI prefs
├── lib/screens/rsvp_screen.dart                # Flutter RSVP UI
├── pubspec.yaml                                # Flutter SDK ^3.12
└── android/, ios/, web/, ...                   # Platform hosts
```

## Files Inspected

| File | Relevance |
|------|-----------|
| `lib/services/text_processing_service.dart` | **Ported** — whitespace, word split, chunking, ORP, timing |
| `lib/models/app_settings.dart` | Partially referenced — WPM bounds, chunk modes |
| `lib/screens/rsvp_screen.dart` | UI inspiration only — not ported |
| `lib/services/epub_service.dart` | **Ported (MVP)** — local EPUB text extract only |
| `lib/services/storage_service.dart` | **Not ported** — Hive storage (app uses DataStore) |

## Relevant Dart Logic Found

From `TextProcessingService`:

- `normalizeWhitespace` — CRLF, collapse spaces/tabs, trim excess newlines
- `splitIntoWords` — whitespace tokenization preserving punctuation on words
- `buildRsvpChunks` — 1/2/3 word chunks + phrase mode (punctuation boundaries, max 8 words)
- `wordIndexForChunkIndex` / `chunkIndexForWordIndex` — index mapping on chunk resize
- `optimalRecognitionPoint` — ORP at ~37% of chunk length
- `estimatedSecondsRemaining` / `formatDuration` — WPM timing helpers

From `AppSettings`:

- WPM range 100–1200
- `ChunkSizeMode`: oneWord, twoWords, threeWords, phrase

## What Was Ported (Kotlin `:k0r34d3r-core`)

| Dart | Kotlin module |
|------|---------------|
| `normalizeWhitespace` | `K0R34D3RTokenizer.normalizeWhitespace` |
| `splitIntoWords` | `K0R34D3RTokenizer.splitIntoWords` |
| `buildRsvpChunks` | `K0R34D3RChunker.buildTokens` |
| Chunk index mapping | `K0R34D3RChunker.wordIndexForChunkIndex` etc. |
| `optimalRecognitionPoint` | `K0R34D3RChunker.optimalRecognitionPoint` |
| Reader stepping | `K0R34D3RReader` implementing `K0R34D3RCore` |

**App integration:** `K0SdkReaderAdapter` wraps `K0R34D3RReader` for the k0R34DER Compose screen.

## What Was Intentionally Not Ported

- Flutter UI (themes, reticles, backgrounds, fonts)
- Document import (PDF, Reddit, article extraction) — **EPUB text import ported**
- Hive/local document library
- Network HTTP fetching
- Full `AppSettings` theme/visual configuration
- Wakelock / keep-awake behavior

## Current Kotlin Architecture

```
:k0r34d3r-core/                    # Pure Kotlin JVM library
  com.heathen.k0r34d3r.core/
    K0R34D3RCore.kt                 # Interface
    K0R34D3RReader.kt               # Default implementation
    K0R34D3RTokenizer.kt
    K0R34D3RChunker.kt
    K0R34D3RReaderConfig.kt
    K0R34D3RReadingState.kt
    epub/EpubTextExtractor.kt          # Local EPUB → plain text (MVP)

app/features/k0reader/
  epub/EpubDocumentLoader.kt          # ContentResolver → bytes → core extractor
  K0ReaderAdapter.kt                # App-facing interface
  K0SdkReaderAdapter.kt             # Wraps :k0r34d3r-core (default)
  LocalK0ReaderAdapter.kt           # Legacy fallback
  RsvpReaderEngine.kt               # Fallback engine (retained)
```

## Why Flutter Was Not Embedded

- K0R34D3R is a standalone Flutter **app**, not an embeddable SDK module
- Embedding Flutter would add dual toolchain weight to CI and APK size
- Hades Watch Field Terminal is Kotlin/Compose-first
- RSVP logic is small enough to port safely into a shared Kotlin core

## Future Path to True Shared SDK

1. **Extract** `:k0r34d3r-core` from Field Terminal into its own repo or K0R34D3R monorepo subdirectory
2. **Publish** as Maven artifact or Git submodule consumed by both projects
3. **Optional:** K0R34D3R Flutter app could call the same core via FFI/platform channel (long-term)
4. **Keep** `K0ReaderAdapter` as the app boundary — swap implementations without UI changes
5. Add parity tests comparing Dart `TextProcessingService` output vs Kotlin core for golden texts

## Safety / Privacy

- Core module has **no network**, **no storage**, **no Android APIs**
- k0R34DER UI accepts pasted text and local EPUB import — no website scraping
- EPUB bytes and extracted text are not uploaded
- No upload of reader text
- User can disable Kotlin core and use `LocalK0ReaderAdapter` fallback in Settings
