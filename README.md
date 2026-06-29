# Hades Watch Field Terminal

Official Android companion app for [Hades Watch](https://hadeswatch.com).

**Package:** `com.heathen.hadeswatch`  
**GitHub:** https://github.com/H34THEN/hades-watch-field-terminal

## Current Status

| Feature | Status |
|---------|--------|
| Hades Watch WebShell (trusted domains) | Done |
| `:k0r34d3r-core` Kotlin module | Done |
| k0R34DER (K0SdkReaderAdapter default) | Done |
| Signal Reader (local snippets) | Done |
| Underworld Gateways + isolated viewer | Done |
| Field Notes, 4R3S placeholder | Done |

## Build

```bash
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
| k0R34DER | Local RSVP via `:k0r34d3r-core`; LocalK0ReaderAdapter fallback |
| Signal Reader | Local snippet library; Read in k0R34DER |
| Underworld Gateways | NAS/homelab URLs; external browser default; optional Gateway Viewer |
| Field Notes | Local draft |
| 4R3S | Coming soon — no permissions |

## Permissions (MVP)

- `INTERNET`
- `ACCESS_NETWORK_STATE`

No storage, location, Bluetooth, camera, or mic permissions.

## Security Summary

- **Hades Watch WebShell:** `hadeswatch.com` only
- **Underworld Gateways:** User URLs — not allowlisted; no Hades Watch cookie sharing by design
- **Gateway Viewer:** Isolated WebView, separate user agent, labeled UI
- **Signal Reader / k0R34DER:** Local-only, no upload, no scraping

## Manual Testing Checklist

1. Tools → Underworld Gateways — add HTTP + HTTPS gateways, export/import JSON
2. Launch external browser gateway; confirm no Hades Watch session bleed
3. Set gateway to in-app viewer — confirm Gateway Viewer label and HTTP warning
4. Tools → Signal Reader — add snippet, search, Read in k0R34DER
5. Settings — clear Signal Reader, gateways, Gateway Viewer cache separately
6. Hades Watch MMO tab still loads hadeswatch.com
7. `./gradlew :k0r34d3r-core:test` passes

## Docs

- [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)
- [docs/UNDERWORLD_GATEWAYS.md](docs/UNDERWORLD_GATEWAYS.md)
- [docs/SIGNAL_READER.md](docs/SIGNAL_READER.md)
- [docs/K0R34D3R_PARITY_TESTS.md](docs/K0R34D3R_PARITY_TESTS.md)
- [docs/TOOLS_ARCHITECTURE.md](docs/TOOLS_ARCHITECTURE.md)

## Known Limitations

- WebView cookie isolation is process-level (documented honestly)
- Custom gateway icons depend on photo picker URI persistence
- No website API sync for Signal Reader or gateways
- Gateway import merges without overwrite by default
