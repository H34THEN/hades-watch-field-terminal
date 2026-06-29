# Architecture — Hades Watch Field Terminal

## Overview

Hades Watch Field Terminal is a native Android companion for [hadeswatch.com](https://hadeswatch.com). The website remains the source of truth for accounts, gameplay, and community data. The app provides:

- A hardened WebView shell for major Hades Watch routes
- A native Home quick-launch dashboard
- A Tools Hub for optional local-first modules
- Settings and privacy controls

## Layers

```
k0r34d3r-core/               # Pure Kotlin JVM RSVP/text-processing library
app/
├── MainActivity.kt          # Compose host, deep links, bottom nav
├── HadesWatchApp.kt         # Application-level repositories
├── core/
│   ├── navigation/          # Routes, NavHost, WebRoutes
│   ├── theme/               # Material 3 terminal theme
│   ├── web/                 # Hardened WebView wrapper
│   ├── settings/            # DataStore preferences
│   ├── security/            # Session/cache clearing
│   └── ui/                  # Shared Compose components
└── features/
    ├── home/                # Quick-launch grid
    ├── webshell/            # WebView screen wrapper
    ├── tools/               # Tool registry + hub
    ├── k0reader/            # RSVP reader + adapter boundary
    ├── gateways/            # Underworld Gateways launcher
    ├── ares/                # 4R3S placeholder
    ├── fieldnotes/          # Local drafts
    ├── notifications/       # Web shortcut to site notifications
    └── settings/            # Settings + privacy
```

## Navigation

Bottom navigation maps to native or WebShell destinations:

| Tab | Destination |
|-----|-------------|
| Home | Native quick-launch |
| MMO | `https://hadeswatch.com/mmo` |
| Dead Drops | `/dead-drops` |
| Forums | `/community/forums` |
| Profile | `/profile/dossier` |
| Tools | Native Tools Hub |
| Notifications | `/notifications` |
| Settings | Native settings |

Deep links (`hadeswatch://…`) map to the same routes where supported.

## Data Flow

- **Website auth**: CookieManager only; no password storage
- **Settings/tools**: DataStore Preferences on device
- **No local website DB**: All live game data comes from the site

## K0R34D3R Adapter Boundary

`K0ReaderAdapter` isolates the RSVP engine from UI.

- **Default:** `K0SdkReaderAdapter` → `:k0r34d3r-core` (`K0R34D3RReader`)
- **Fallback:** `LocalK0ReaderAdapter` → `RsvpReaderEngine` (legacy MVP engine)

Toggle in Settings: **Use K0R34D3R Kotlin core**.

See [K0R34D3R_INTEGRATION_PLAN.md](K0R34D3R_INTEGRATION_PLAN.md) and [TOOLS_ARCHITECTURE.md](TOOLS_ARCHITECTURE.md).

## Underworld Gateways

User-defined NAS/homelab URL launcher — **not** part of the Hades Watch WebShell. See [UNDERWORLD_GATEWAYS.md](UNDERWORLD_GATEWAYS.md).

## Build Targets

- `minSdk` 26
- `compileSdk` / `targetSdk` 36
- Kotlin + Jetpack Compose + Material 3
