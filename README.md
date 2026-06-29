# Hades Watch Field Terminal

Official Android companion app for [Hades Watch](https://hadeswatch.com).

**Package:** `com.heathen.hadeswatch`  
**GitHub:** https://github.com/H34THEN/hades-watch-field-terminal

## Current Status

| Feature | Status |
|---------|--------|
| Hades Watch WebShell (trusted domains) | Done |
| `:k0r34d3r-core` Kotlin module + golden fixtures | Done |
| k0R34DER (K0SdkReaderAdapter default) | Done |
| Signal Reader + JSON import/export | Done |
| Underworld Gateways + isolated viewer | Done |
| Tools Hub polish (grouped, search, chips) | Done |
| UI/UX rebuild (5-tab nav, design system) | Superseded by HUD shell (4-tab dock) |
| HUD shell navigation + compact web controls | Done |
| k0R34DER EPUB import (system picker) | Done |
| k0R34DER reader experience upgrade | Done |
| Local Tool Data management screen | Done |
| Future API scaffolds (no live calls) | Done |
| Field Notes, 4R3S placeholder | Done |

## Build

```bash
export JAVA_HOME=/opt/android-studio/jbr   # JDK 21 recommended
./gradlew assembleDebug
```

## Test Kotlin core

```bash
./gradlew :k0r34d3r-core:test
```

## Install

```bash
./gradlew installDebug
```

Or:

```bash
adb install --streaming -r app/build/outputs/apk/debug/app-debug.apk
```

Fallback:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

## Modules

- `:app` — Compose Android application
- `:k0r34d3r-core` — Pure Kotlin RSVP/text processing

## Tools

| Tool | Notes |
|------|-------|
| k0R34DER | Local RSVP via `:k0r34d3r-core`; EPUB import; Signal Reader handoff |
| Signal Reader | Local snippets; JSON import/export; Read in k0R34DER |
| Underworld Gateways | NAS/homelab URLs; external browser default; optional Gateway Viewer |
| Field Notes | Local draft |
| 4R3S | Coming soon — no permissions |

## Permissions (MVP)

- `INTERNET`
- `ACCESS_NETWORK_STATE`

No storage, location, Bluetooth, camera, or mic permissions.

## Security Summary

This app is **not a general browser**. It is a safer sandboxed companion shell for [hadeswatch.com](https://hadeswatch.com).

- **Hades Watch WebShell:** `hadeswatch.com` only — external/unknown links open outside the trusted shell
- **Underworld Gateways:** User URLs — not allowlisted; no Hades Watch cookie sharing by design
- **Gateway Viewer:** Isolated WebView, separate user agent — see cookie isolation doc
- **Signal Reader / k0R34DER:** Local-only, no upload, no scraping; EPUB imports stay on device
- **Future API:** Scaffold only — Settings → Future API Status

## Manual Testing

See [docs/MANUAL_TESTING.md](docs/MANUAL_TESTING.md) for the full checklist.

### HUD navigation (4-tab dock)

- Web, Tools, Reader, Settings — each with icon + label
- Compact web HUD controls (collapsed chip; expand for back/forward/reload/routes)
- Tools FAB + drawer from Web tab
- k0R34DER Import EPUB via system picker

## Docs

- [docs/HUD_SHELL_NAVIGATION.md](docs/HUD_SHELL_NAVIGATION.md)
- [docs/EPUB_IMPORT.md](docs/EPUB_IMPORT.md)
- [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)
- [docs/UNDERWORLD_GATEWAYS.md](docs/UNDERWORLD_GATEWAYS.md)
- [docs/SIGNAL_READER.md](docs/SIGNAL_READER.md)
- [docs/SIGNAL_READER_IMPORT_EXPORT.md](docs/SIGNAL_READER_IMPORT_EXPORT.md)
- [docs/GATEWAY_VIEWER_COOKIE_ISOLATION.md](docs/GATEWAY_VIEWER_COOKIE_ISOLATION.md)
- [docs/K0R34D3R_PARITY_TESTS.md](docs/K0R34D3R_PARITY_TESTS.md)
- [docs/FUTURE_API_ENDPOINTS.md](docs/FUTURE_API_ENDPOINTS.md)
- [docs/MANUAL_TESTING.md](docs/MANUAL_TESTING.md)
- [docs/UI_UX_REBUILD.md](docs/UI_UX_REBUILD.md)
- [docs/TOOLS_ARCHITECTURE.md](docs/TOOLS_ARCHITECTURE.md)

## Known Limitations

- WebView cookie isolation is process-level (documented honestly)
- Custom gateway icons depend on photo picker URI persistence
- No website API sync for Signal Reader or gateways until official endpoints + consent
- Gateway / Signal Reader import merges without overwrite by default
- k0R34DER EPUB import: text-only MVP; no DRM; no chapter picker UI yet
- k0R34DER transfer buffer is in-memory only (not persisted across process death)

## Recommended Next Milestone

1. Automated Dart↔Kotlin golden comparison script (optional CI)
2. Gateway Viewer cookie partition research (Android 14+)
3. Wire Future API when Hades Watch mobile endpoints ship
4. Signal Reader tag filtering UI polish
