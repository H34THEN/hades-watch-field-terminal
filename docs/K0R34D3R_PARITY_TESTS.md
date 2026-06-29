# K0R34D3R Parity Tests

## Dart Source Referenced

Primary reference: `/home/heathen/Projects/K0R34D3R-inspection/lib/services/text_processing_service.dart`

Also referenced: `lib/models/app_settings.dart` (WPM bounds, chunk modes)

## Kotlin Implementation

| Dart method | Kotlin equivalent |
|-------------|-------------------|
| `normalizeWhitespace` | `K0R34D3RTokenizer.normalizeWhitespace` |
| `splitIntoWords` | `K0R34D3RTokenizer.splitIntoWords` |
| `buildRsvpChunks` | `K0R34D3RChunker.buildTokens` |
| `wordIndexForChunkIndex` | `K0R34D3RChunker.wordIndexForChunkIndex` |
| `chunkIndexForWordIndex` | `K0R34D3RChunker.chunkIndexForWordIndex` |
| `optimalRecognitionPoint` | `K0R34D3RChunker.optimalRecognitionPoint` |
| Reader stepping / WPM | `K0R34D3RReader` |
| EPUB HTML strip | `EpubTextExtractor.htmlToPlainText` |
| EPUB spine extract | `EpubTextExtractor.extract` |

### EPUB tests (`EpubTextExtractorTest`)

- HTML tag stripping
- Minimal valid EPUB fixture
- Empty spine throws `EpubImportException`

Run: `./gradlew :k0r34d3r-core:test`

## Test Coverage

### Unit tests (`K0R34D3RReaderTest`)

- Empty and whitespace-only input
- Punctuation preservation and punctuation-only input
- Whitespace/tabs/newlines normalization
- Unicode word tokenization
- Chunk sizes 1, 2, 3, and phrase mode (size 4+)
- `nextToken` / `previousToken` boundaries
- `reset` after progress
- `progressPercent` at start, middle, end
- `currentToken` before load
- ORP (`optimalRecognitionPoint`) parity
- Chunk/word index mapping round-trip

### Golden fixture tests (`K0R34D3RGoldenFixtureTest`)

Fixtures live under `k0r34d3r-core/src/test/resources/golden/`:

| Fixture | Covers |
|---------|--------|
| `simple_sentence.txt` | Basic word split, chunk sizes 1–3, phrase |
| `punctuation_sentence.txt` | Period/exclamation attachment, phrase boundaries |
| `whitespace_tabs_newlines.txt` | Tab/newline normalization |
| `unicode_sentence.txt` | Accented characters |
| `phrase_mode_sample.txt` | Phrase chunking at punctuation |

Expected token lists: `golden/expected/*.expected` (one token per line).

Run: `./gradlew :k0r34d3r-core:test`

## Adding New Fixtures

1. Add `golden/my_fixture.txt` with input text
2. Run chunker locally or add a temporary test to print tokens:
   ```kotlin
   K0R34D3RChunker.buildTokens(text, chunkSize).forEach { println(it) }
   ```
3. Save output to `golden/expected/my_fixture.chunk1.expected` (etc.)
4. Add test methods in `K0R34D3RGoldenFixtureTest.kt`
5. Run `./gradlew :k0r34d3r-core:test`

## Manual Dart ↔ Kotlin Comparison

Flutter tooling is **not** required for Kotlin CI. To compare manually later:

1. Run the same input through Dart `TextProcessingService.buildRsvpChunks`
2. Compare line-by-line with Kotlin `K0R34D3RChunker.buildTokens`
3. Document differences in this file

## Known Differences

| Area | Dart | Kotlin |
|------|------|--------|
| Chunk mode enum | `ChunkSizeMode.phrase` explicit | Mapped to chunk size ≥ 4 |
| WPM max | 1200 | 1200 (config) |
| UI timing | Flutter animation | Compose `delay(intervalMillis)` |
| Sentence/paragraph split | Implemented in Dart | Not ported (not needed for RSVP MVP) |
| EPUB import | `epubx` full book model | Minimal ZIP/OPF text extractor (no DRM, no images) |
| Document library | Hive persistence | Not ported — optional session text in DataStore |

## Full Parity Remaining

- Automated Dart↔Kotlin golden runner (optional CI job)
- `estimatedSecondsRemaining` / `formatDuration` exposed on app adapter
- Explicit `ChunkSizeMode` enum in Kotlin core (optional)
- Property-based tests for random text inputs
- EPUB chapter navigation UI (spine merged to plain text today)
- Flutter visual themes, reticles, backgrounds
