# Future API Endpoints (Recommended)

These endpoints are **recommended for future Hades Watch mobile integration**. They are **not assumed to exist** in the current website MVP.

All future API use must:

- Use official Hades Watch authentication (session/token from website)
- Require explicit user consent for uploads
- Never replace WebView as primary gameplay surface unless documented

## Recommended Endpoints

| Method | Path | Purpose |
|--------|------|---------|
| GET | `/api/mobile/session` | Validate mobile session / profile stub |
| GET | `/api/mobile/notifications` | Native notification summary (future) |
| GET | `/api/mobile/dead-drops` | Dead drop highlights for widgets |
| GET | `/api/mobile/daily-signals` | Daily signals digest |
| GET | `/api/mobile/field-logs` | Field log entries |
| GET | `/api/mobile/profile/summary` | Lightweight profile card |
| POST | `/api/mobile/field-notes` | Opt-in Field Notes sync |
| POST | `/api/mobile/reading-progress` | Opt-in k0R34DER reading progress |

## Integration Notes

- Prefer cookie/session continuity from WebView before standalone API tokens
- Rate-limit and scope mobile endpoints separately from admin APIs
- Do not expose admin or moderation actions through mobile API
- Document privacy policy updates before enabling upload endpoints

## App Readiness

The Field Terminal MVP keeps native tools local-only. Scaffolding lives in `app/.../core/api/`:

- `MobileApiRoutes` — planned path constants
- `MobileApiCapability` / `MobileApiStatus` — capability metadata
- `MobileApiNotes` — in-app copy
- **Settings → Future API Status** — no network calls

When endpoints ship:

1. Add opt-in toggles in Settings
2. Use authenticated HTTPS only to `hadeswatch.com`
3. Never store raw passwords — session via website mechanisms only
