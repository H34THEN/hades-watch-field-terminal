# Signal Reader

## Purpose

Local-first library for manually saved text snippets — lore, field transmissions, forum copy, dead-drop notes, etc.

**Not a scraper.** Does not pull content from Hades Watch automatically.

## MVP Features

- Add / edit / delete snippets
- Fields: title, body, source label (optional), tags (optional), sortOrder
- Search/filter by title, body, tags, source
- Detail view with copy
- **JSON import/export** — clipboard, share, paste with preview merge ([SIGNAL_READER_IMPORT_EXPORT.md](SIGNAL_READER_IMPORT_EXPORT.md))
- **Read in k0R34DER** — transfers body via `ReaderTransferRepository` (in-memory, no route args)

## Persistence

DataStore JSON (`signal_snippets_data` key). Export wraps snippets in `{ "version": 1, "snippets": [...] }`.

## Privacy

- Local-only storage
- No network upload
- No WebView scraping
- Clear from Settings → Local Tool Data → Signal Reader snippets

## Integration

Signal Reader → k0R34DER uses `ReaderTransferRepository`:

- Sets snippet body + source title before navigation
- k0R34DER shows “Loaded from Signal Reader: …”
- Transfer is **temporary** — cleared on consume, explicit clear, or app process death
- No reading progress sync to website

## Future (Not MVP)

- Official Hades Watch API import with explicit consent
- Sync with website field logs endpoint (when available) — see [FUTURE_API_ENDPOINTS.md](FUTURE_API_ENDPOINTS.md)
