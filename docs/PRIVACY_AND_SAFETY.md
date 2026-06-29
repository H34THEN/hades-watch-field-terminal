# Privacy and Safety

## Principles

1. **Website is source of truth** — accounts, gameplay, and community data live on hadeswatch.com.
2. **WebView-first for gameplay** — native code does not replace site auth or permissions.
3. **Local-first native tools** — optional modules process data on-device unless explicit future APIs exist.
4. **Minimal permissions in MVP** — only network access required for the WebView companion.

## What the App Does

- Loads allowlisted Hades Watch URLs in a hardened WebView
- Preserves login sessions via standard WebView cookies
- Stores UI preferences and local tool drafts in DataStore
- Opens non-allowlisted links in the system browser (configurable)

## What the App Does Not Do (MVP)

- Store passwords or raw credentials
- Scrape or duplicate website HTML/data
- Bypass website auth or role checks
- Run background location, Bluetooth, or Wi-Fi scanning
- Upload k0R34DER pasted text or Field Notes without future explicit consent + API
- Expose admin shortcuts (no safe role API yet)

## Native Tools

### k0R34DER

- Paste-in text only
- Local RSVP processing via `:k0r34d3r-core` Kotlin module (default)
- Legacy `LocalK0ReaderAdapter` fallback available in Settings
- Preferences saved on device
- No network upload

### Field Notes

- Local draft textarea
- Manual copy to website
- Future sync only via official API with opt-in

### Signal Reader

- Local snippet library — manual entry only
- JSON import/export on device (no upload)
- Read in k0R34DER via in-memory transfer (temporary)
- No scraping or website sync

### Underworld Gateways

- User-defined NAS/homelab URLs stored locally
- External browser default; optional isolated Gateway Viewer
- No Hades Watch cookie sharing by design (see [GATEWAY_VIEWER_COOKIE_ISOLATION.md](GATEWAY_VIEWER_COOKIE_ISOLATION.md))
- No storage permissions in MVP (photo picker for custom icons)

### 4R3S

- Placeholder only in MVP
- No permissions requested
- No scanning or background services

## User Controls (Settings)

- Clear website session (cookies) — **separate from local tools**
- Clear Hades Watch WebView cache
- **Local Tool Data** screen — granular clears with confirmations
- Future API Status (planned endpoints, not live)
- Reduced motion / high contrast / large text
- Tool enable/disable toggles

See [MANUAL_TESTING.md](MANUAL_TESTING.md) for a full checklist.

## K0R34D3R Kotlin Core

The `:k0r34d3r-core` module ports RSVP logic from the K0R34D3R Flutter app. It is pure Kotlin with no network or Android APIs. See [K0R34D3R_INTEGRATION_PLAN.md](K0R34D3R_INTEGRATION_PLAN.md).

## Permissions

**MVP declared:**

- `INTERNET`
- `ACCESS_NETWORK_STATE`

**Not requested in MVP:**

- Location, Bluetooth, Nearby devices, Camera, Microphone, Storage, Contacts, SMS, Wi-Fi scanning

**Future (4R3S only when implemented):**

- Bluetooth scan/connect, Nearby devices, Location (if required by Android for scans), Wi-Fi state

Permissions will be requested only when features are implemented and explicitly enabled.
