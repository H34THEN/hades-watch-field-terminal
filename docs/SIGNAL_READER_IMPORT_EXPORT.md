# Signal Reader Import / Export

JSON backup and restore for Signal Reader snippets — **local only**, mirroring the safety model used for Underworld Gateways.

## Export

From **Tools → Signal Reader**:

- **Copy export** — JSON to clipboard
- **Share JSON** — system share sheet (`ACTION_SEND`, `application/json`)

Export format:

```json
{
  "version": 1,
  "snippets": [
    {
      "id": "uuid",
      "title": "Transmission title",
      "body": "Plain text body",
      "sourceLabel": "optional source",
      "tags": "tag1, tag2",
      "createdAt": 1710000000000,
      "updatedAt": 1710000000000,
      "sortOrder": 0
    }
  ]
}
```

A bare JSON array of snippet objects is also accepted on import.

## Import

1. Tap **Import**
2. Paste JSON
3. Tap **Preview** — validates before writing
4. Confirm merge

### Validation

- `title` cannot be blank
- `body` cannot be blank
- `tags` are trimmed and sanitized (comma-separated)
- Imported content is **plain text only** — not rendered as HTML
- Invalid entries are counted but not imported

### Merge behavior (default)

- **Does not overwrite** existing snippet IDs
- Duplicate IDs → skipped (counted in summary)
- Same title + source label → flagged as duplicate in summary; new IDs may still add if ID is unique
- Import result summary: added, skipped, duplicate, invalid counts

## Safety

- No upload, no scraping, no website sync
- Export/import stays on device unless you share the JSON yourself
- Clearing Signal Reader data is separate from Hades Watch website session (Settings → Local Tool Data)

## Related

- [SIGNAL_READER.md](SIGNAL_READER.md)
- [PRIVACY_AND_SAFETY.md](PRIVACY_AND_SAFETY.md)
