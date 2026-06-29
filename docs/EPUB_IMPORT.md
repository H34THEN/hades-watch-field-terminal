# EPUB Import (k0R34DER)

Local-only EPUB text import for RSVP reading. **No storage permissions. No upload.**

## User Flow

1. Open **Reader** tab (k0R34DER)
2. Tap **Import EPUB**
3. Android system document picker opens (`ACTION_OPEN_DOCUMENT`)
4. Select an `.epub` file
5. App parses text locally and loads into the reader
6. Title and token count shown; **Clear imported book** removes session text

## MIME Types

Preferred:

- `application/epub+zip`
- `application/octet-stream` (fallback for some file managers)

## Architecture

| Layer | Location | Role |
|-------|----------|------|
| UI picker | `K0ReaderScreen` | `ActivityResultContracts.OpenDocument` |
| URI → bytes | `EpubDocumentLoader` | `ContentResolver` on IO dispatcher |
| Parse | `:k0r34d3r-core` `EpubTextExtractor` | ZIP + container.xml + OPF spine + HTML strip |

## Parser Approach

Minimal pure-Kotlin extractor (ported from Flutter `EpubService` behavior):

1. Read EPUB as ZIP
2. Parse `META-INF/container.xml` → OPF path
3. Parse OPF manifest + spine order
4. Extract XHTML/HTML chapters in spine order
5. Strip tags → plain text via `K0R34D3RTokenizer.normalizeWhitespace`
6. Return `EpubParseResult(title, plainText, chapterCount)`

No third-party EPUB library. No DRM support.

## Privacy & Safety

- EPUB bytes never leave the device
- EPUB content is not logged
- No persistable URI permission required for one-shot read (optional take if granted)
- Full book text is **not** saved unless user enables “Save last text locally” in reader settings

## Limitations

- DRM-protected EPUBs fail with a friendly error
- Complex EPUB3 layouts, embedded fonts, and images are ignored (text only)
- Regex-based OPF parsing — sufficient for MVP, not exhaustive
- No chapter navigation UI yet (full text merged for RSVP)

## Tests

`EpubTextExtractorTest` in `:k0r34d3r-core` — minimal in-memory EPUB fixtures.

```bash
./gradlew :k0r34d3r-core:test
```
