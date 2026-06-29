# Signal Reader

## Purpose

Local-first library for manually saved text snippets — lore, field transmissions, forum copy, dead-drop notes, etc.

**Not a scraper.** Does not pull content from Hades Watch automatically.

## MVP Features

- Add / edit / delete snippets
- Fields: title, body, source label (optional), tags (optional)
- Search/filter by title, body, tags, source
- Detail view with copy
- **Read in k0R34DER** — transfers body via `ReaderTransferRepository` (in-memory, no route args)

## Persistence

DataStore JSON array (`signal_snippets_data` key).

## Privacy

- Local-only storage
- No network upload
- No WebView scraping
- Clear from Settings → Clear Signal Reader snippets

## Integration

Signal Reader → k0R34DER uses shared `ReaderTransferRepository` to avoid large navigation arguments.

## Future (Not MVP)

- Official Hades Watch API import with explicit consent
- Export/import JSON backup
- Sync with website field logs endpoint (when available)
