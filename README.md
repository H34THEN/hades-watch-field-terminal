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
| `:k0r34d3r-core` Kotlin module | Done |
| K0SdkReaderAdapter (default engine) | Done |
| LocalK0ReaderAdapter fallback | Done |
| Underworld Gateways (NAS/homelab launcher) | Done |
| 4R3S placeholder (no permissions) | Done |
| Field Notes (local draft) | Done |
| Settings + Privacy/Safety | Done |
| Deep link scaffold (`hadeswatch://`) | Done |
| Debug APK build | Verified via `./gradlew assembleDebug` |

The website remains the **source of truth**. This app does not duplicate game data, scrape HTML, or bypass website auth.

## Setup Requirements

- JDK 17+ (Android Studio JBR / JDK 21 recommended)
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

Run Kotlin core unit tests:

```bash
./gradlew :k0r34d3r-core:test
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
Native shell (Compose) + WebView (hadeswatch.com) + :k0r34d3r-core + local tools (DataStore)
```

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md), [docs/TOOLS_ARCHITECTURE.md](docs/TOOLS_ARCHITECTURE.md).

## Modules

| Module | Purpose |
|--------|---------|
| `:app` | Android Compose application |
| `:k0r34d3r-core` | Pure Kotlin RSVP/text-processing (ported from K0R34D3R Flutter logic) |

## k0R34DER

- **Default engine:** `K0SdkReaderAdapter` → `:k0r34d3r-core`
- **Fallback:** `LocalK0ReaderAdapter` → `RsvpReaderEngine` (toggle in Settings)
- Local-only pasted text — no upload, no website scraping

See [docs/K0R34D3R_INTEGRATION_PLAN.md](docs/K0R34D3R_INTEGRATION_PLAN.md).

## Underworld Gateways

Local NAS/homelab URL launcher. Opens **externally** — not in the Hades Watch trusted WebView. No Hades Watch cookies shared.

See [docs/UNDERWORLD_GATEWAYS.md](docs/UNDERWORLD_GATEWAYS.md).

## WebView Security Model

- Allowlisted hosts: `hadeswatch.com`, `www.hadeswatch.com`
- Gateway URLs are **not** allowlisted
- HTTPS only in-app; external links open in system browser
- No JavaScript bridges, no credential interception

Details: [docs/WEBVIEW_SECURITY.md](docs/WEBVIEW_SECURITY.md)

## Privacy / Safety Policy

Details: [docs/PRIVACY_AND_SAFETY.md](docs/PRIVACY_AND_SAFETY.md)

## Native Tools

| Tool | MVP |
|------|-----|
| k0R34DER | Local RSVP via Kotlin core |
| Underworld Gateways | Local URL launcher |
| 4R3S | Coming soon — no scanning/permissions |
| Field Notes | Local draft only |
| Dead Drop Tracker | Web shortcut |
| Signal Reader | Placeholder |
| Accessibility View | Settings shortcut |

## Permissions

**Used in MVP:** `INTERNET`, `ACCESS_NETWORK_STATE`

**Not requested:** Location, Bluetooth, Camera, Mic, Storage, etc.

## Known Limitations

- K0R34D3R Flutter app not embedded (Kotlin core port only)
- Gateway launches always external in MVP
- No custom photo icons for gateways (built-in icons only)
- Field Notes / reader data not synced to website
- No verified Android App Links yet

## Git Workflow

Commit on `main`, push to GitHub after verified builds.
