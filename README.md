# Hades Watch Field Terminal

Official Android companion app for [Hades Watch](https://hadeswatch.com) — a mobile Underwatch terminal for the text MMO and community site.

**Package:** `com.heathen.hadeswatch`  
**GitHub:** https://github.com/H34THEN/hades-watch-field-terminal

## MVP Status

| Area | Status |
|------|--------|
| Hardened WebView shell | Done |
| Bottom navigation (8 sections) | Done |
| Home quick-launch grid | Done |
| Tools Hub + k0R34DER | Done |
| 4R3S placeholder (no permissions) | Done |
| Field Notes (local draft) | Done |
| Settings + Privacy/Safety | Done |
| Deep link scaffold (`hadeswatch://`) | Done |
| K0R34D3R SDK adapter boundary | Done (local engine) |
| Debug APK build | Verified via `./gradlew assembleDebug` |

The website remains the **source of truth**. This app does not duplicate game data, scrape HTML, or bypass website auth.

## Setup Requirements

- JDK 17+ (JDK 26 tested)
- Android SDK with `compileSdk 36`
- `local.properties` with `sdk.dir` (not committed)

Example `local.properties`:

```properties
sdk.dir=/path/to/Android/Sdk
```

## Build Commands

```bash
./gradlew assembleDebug
```

Debug APK output:

```
app/build/outputs/apk/debug/app-debug.apk
```

## Install Commands

With a device or emulator connected:

```bash
adb devices
./gradlew installDebug
```

## Architecture Overview

```
Native shell (Compose) + WebView (hadeswatch.com) + local tools (DataStore)
```

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) for package layout and navigation.

## WebView Security Model

- Allowlisted hosts: `hadeswatch.com`, `www.hadeswatch.com`
- HTTPS only in-app; external links open in system browser
- No JavaScript bridges, no credential interception
- User agent suffix: `HadesWatchAndroid/0.1`
- Session via WebView cookies only

Details: [docs/WEBVIEW_SECURITY.md](docs/WEBVIEW_SECURITY.md)

## Privacy / Safety Policy

- Website handles login; app does not store passwords
- Native tools are opt-in and local-first in MVP
- Clear session/cache/local tool data from Settings

Details: [docs/PRIVACY_AND_SAFETY.md](docs/PRIVACY_AND_SAFETY.md)

## Native Tools

| Tool | MVP |
|------|-----|
| k0R34DER | Local RSVP reader for pasted text |
| K0R34D3R SDK Adapter | Planned — adapter interface ready |
| 4R3S | Coming soon — no scanning/permissions |
| Field Notes | Local draft only |
| Dead Drop Tracker | Web shortcut |
| Signal Reader | Placeholder |
| Accessibility View | Settings shortcut |

## k0R34DER (Local-Only)

- Paste text, set WPM/chunk/font size
- Start/pause, prev/next, rewind
- Preferences in DataStore
- **No network upload** in MVP
- **No website scraping**

## K0R34D3R SDK Integration

External repo: https://github.com/H34THEN/K0R34D3R

K0R34D3R is a **Flutter app** — not directly importable as a Kotlin module in MVP. The app defines `K0ReaderAdapter` with a local `RsvpReaderEngine` fallback.

Plan: [docs/K0R34D3R_INTEGRATION_PLAN.md](docs/K0R34D3R_INTEGRATION_PLAN.md)

## 4R3S Placeholder Safety

MVP shows coming-soon UI only. No Bluetooth, location, nearby devices, Wi-Fi scan, background services, or signal collection.

## Known Limitations

- No verified Android App Links for `https://hadeswatch.com` (requires site `assetlinks.json`)
- No push notifications (needs official API/Firebase plan)
- No admin shortcuts (no safe role API)
- K0R34D3R SDK not bundled — local engine only
- Field Notes and reader data not synced to website
- Bottom nav has 8 items (may feel crowded on small phones)
- Not published to Play Store

## Future API Integration (Recommended)

These are **not assumed to exist** yet:

- `GET /api/mobile/session`
- `GET /api/mobile/notifications`
- `GET /api/mobile/dead-drops`
- `GET /api/mobile/daily-signals`
- `GET /api/mobile/field-logs`
- `GET /api/mobile/profile/summary`
- `POST /api/mobile/field-notes`
- `POST /api/mobile/reading-progress`

See [docs/FUTURE_API_ENDPOINTS.md](docs/FUTURE_API_ENDPOINTS.md)

## Permissions

**Used in MVP:**

- `INTERNET`
- `ACCESS_NETWORK_STATE`

**Planned but not requested:**

- Bluetooth / Nearby devices / Location / Wi-Fi state (4R3S future only, explicit opt-in)

## Git Workflow

Stable milestone commits on `main`:

1. `chore: scaffold android project`
2. `feat: add themed compose navigation and home shell`
3. `feat: add hardened hades watch webview shell`
4. `feat: add tools hub and k0reader mvp`
5. `feat: add k0r34d3r sdk adapter plan`
6. `feat: add safety settings and 4r3s scaffold`
7. `docs: add architecture privacy and api notes`
8. `build: verify debug apk`

## License

See repository license (if added). Hades Watch branding and lore belong to the Hades Watch project.
