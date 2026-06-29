# UI/UX Rebuild

Milestone history: 5-tab nav → **HUD shell** with 4-tab dock, compact web controls, EPUB import.

## Navigation Model (current)

Bottom dock reduced to **4 primary tabs**:

| Tab | Route | Purpose |
|-----|-------|---------|
| Web | `web?url={url}` | Full-screen Hades Watch WebShell (default start) |
| Tools | `tools` | Native/local tools hub |
| Reader | `reader` | k0R34DER speed reader (top-level) |
| Settings | `settings` | App settings, privacy, local data |

`home` remains for deep links but is not on the dock.

HUD components in `core/hud/`:

- `HadesHudScaffold` — dock + content inset + tools FAB on Web
- `HudBottomDock` — 4-tab navigation with save/restore state
- `HudWebControls` — collapsible web chrome (replaces bulky browser banner)
- `HudToolDrawerSheet` — Field Terminal tools overlay
- `HudRouteSelectorSheet` — allowlisted Hades Watch routes

See [HUD_SHELL_NAVIGATION.md](HUD_SHELL_NAVIGATION.md).

## Design System

Reusable components in `core/ui/`:

- `HadesBottomNav` — legacy 5-tab bar (retained; superseded by `HudBottomDock`)
- `HadesActionCard`, `HadesToolCard`, `HadesSectionHeader`, `HadesSearchBar`
- `HadesEmptyState`, `HadesWarningBox`, `HadesStatusChip`, `HadesIcon`

## k0R34DER UX

- Dedicated **Reader** dock tab
- **Import EPUB** via system picker — see [EPUB_IMPORT.md](EPUB_IMPORT.md)
- Large ORP-style token display with optional focus highlight
- Context preview (previous/next tokens dimmed)
- Start/Pause, punctuation pause, chunk modes, focus mode
- Signal Reader handoff → Reader tab

## Known UI Limitations

- Web back/forward depends on WebView history within selected route
- ORP highlight is Kotlin-core approximation, not full Flutter UI parity
- No landscape-specific layouts yet
- EPUB: merged plain text only (no per-chapter UI)

## Manual Test Notes

See [MANUAL_TESTING.md](MANUAL_TESTING.md) — verify HUD dock, compact web controls, tools drawer, EPUB import.
