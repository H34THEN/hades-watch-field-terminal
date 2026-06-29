# Architecture — Hades Watch Field Terminal

## Overview

Hades Watch Field Terminal is a native Android companion for [hadeswatch.com](https://hadeswatch.com). The website remains the source of truth for accounts, gameplay, and community data. The app provides:

- A hardened WebView shell for major Hades Watch routes
- A native Home quick-launch dashboard
- A Tools Hub for optional local-first modules
- Settings and privacy controls

## Layers

```
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

`K0ReaderAdapter` isolates the RSVP engine from UI. MVP uses `LocalK0ReaderAdapter` + `RsvpReaderEngine`. Future K0R34D3R SDK can implement the same interface or wrap a ported engine.

See [K0R34D3R_INTEGRATION_PLAN.md](K0R34D3R_INTEGRATION_PLAN.md).

## Build Targets

- `minSdk` 26
- `compileSdk` / `targetSdk` 36
- Kotlin + Jetpack Compose + Material 3
