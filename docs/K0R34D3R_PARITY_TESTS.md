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

## Test Coverage (`K0R34D3RReaderTest`)

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

Run: `./gradlew :k0r34d3r-core:test`

## Known Differences

| Area | Dart | Kotlin |
|------|------|--------|
| Chunk mode enum | `ChunkSizeMode.phrase` explicit | Mapped to chunk size ≥ 4 |
| WPM max | 1200 | 1200 (config) |
| UI timing | Flutter animation | Compose `delay(intervalMillis)` |
| Sentence/paragraph split | Implemented in Dart | Not ported (not needed for RSVP MVP) |

## Full Parity Remaining

- Golden fixture files comparing Dart vs Kotlin output for shared sample texts
- `estimatedSecondsRemaining` / `formatDuration` exposed on app adapter
- Explicit `ChunkSizeMode` enum in Kotlin core (optional)
- Property-based tests for random text inputs

## Golden Fixtures (Recommended)

Create paired fixtures:

```
k0r34d3r-core/src/test/resources/fixtures/sample_lore.txt
```

Compare token lists from Dart CLI/script vs Kotlin test — not yet automated in this milestone.
