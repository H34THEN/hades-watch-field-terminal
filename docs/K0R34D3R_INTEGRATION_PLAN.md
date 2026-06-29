# K0R34D3R SDK Integration Plan

## Repository

- **URL:** https://github.com/H34THEN/K0R34D3R
- **Observed HEAD:** Flutter/Dart multi-platform app (not Android/Kotlin native)

## Observed Structure (MVP inspection)

```
K0R34D3R/
├── lib/
│   ├── services/text_processing_service.dart   # RSVP chunking, word split
│   ├── screens/rsvp_screen.dart
│   └── ... (Flutter UI, Hive storage, PDF/EPUB import)
├── pubspec.yaml                                # Flutter SDK ^3.12
├── android/                                    # Flutter Android host
├── ios/, linux/, macos/, web/, windows/
└── test/
```

**Conclusion:** K0R34D3R is a **Flutter application**, not a standalone Kotlin/Android library. Direct Gradle module import is **not clean** for MVP.

## Recommended Integration Mode

| Option | Feasibility | Notes |
|--------|-------------|-------|
| Git submodule as Gradle module | Low | Flutter module embedding adds toolchain weight |
| Extract `text_processing_service.dart` logic | Medium | Port RSVP algorithms to Kotlin (adapter already exists) |
| Flutter module in Android app | Medium-Low | Requires Flutter SDK in CI and dual stack |
| Separate K0R34D3R Android library fork | Future | Best if SDK is split from app UI |

**MVP choice:** Keep local Kotlin `RsvpReaderEngine` behind `K0ReaderAdapter`. Document port path from Dart `TextProcessingService`.

## App Interface (Adapter Contract)

```kotlin
interface K0ReaderAdapter {
    fun loadText(text: String)
    fun setWordsPerMinute(wpm: Int)
    fun setChunkSize(chunkSize: Int)
    fun currentToken(): String
    fun nextToken(): String?
    fun previousToken(): String?
    fun reset()
    fun tokenCount(): Int
    fun currentIndex(): Int
    fun rewind(steps: Int = 10)
}
```

**Implementation in MVP:** `LocalK0ReaderAdapter` → `RsvpReaderEngine`

**Future SDK implementation:** `K0SdkReaderAdapter` (not yet created) could:

1. Wrap a ported Kotlin library extracted from K0R34D3R algorithms, or
2. Delegate to a Flutter engine channel if full SDK UI is embedded (heavier)

## Porting Checklist (Next Steps)

1. Extract `TextProcessingService` RSVP/chunk logic from K0R34D3R into a Kotlin module (`:k0r34d3r-core`).
2. Add unit tests comparing Dart vs Kotlin token output for sample texts.
3. Implement `K0SdkReaderAdapter : K0ReaderAdapter` using the shared core.
4. Optional: publish `:k0r34d3r-core` from K0R34D3R repo or submodule.
5. Keep UI in Hades Watch Field Terminal Compose screens (do not embed full Flutter UI unless required).

## Safety / Privacy Expectations

- No automatic import of website content
- No network upload of pasted text in reader MVP
- No storage permissions for reader MVP
- K0R34D3R's Reddit/article import features should **not** be enabled in Hades Watch companion without explicit product review

## Current MVP Fallback

`features/k0reader/RsvpReaderEngine.kt` provides:

- Whitespace normalization
- Word/chunk splitting (1–4 words per chunk)
- WPM-based timing via `intervalMillis()`
- Manual prev/next/rewind/reset

Algorithm inspired by K0R34D3R's `text_processing_service.dart` but implemented independently in Kotlin.

## Submodule Command (Future)

When a Kotlin core module exists:

```bash
git submodule add https://github.com/H34THEN/K0R34D3R.git external/K0R34D3R
# Then include only the extracted core module in settings.gradle.kts
```

Do **not** submodule the full Flutter app into this repo until the integration path is stable.
