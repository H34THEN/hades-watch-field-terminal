# UI/UX Rebuild

Milestone history: 5-tab nav → HUD 4-tab dock → **Field Hex command HUD** (overlay-free web).

## Navigation Model (current)

**Field Hex** replaces the bottom dock and fixed web chrome as the primary navigation control.

| Destination | Route | Access |
|-------------|-------|--------|
| Web | `web?url={url}` | Field Hex → Web (default start) |
| Tools | `tools` | Field Hex → Tools |
| Reader | `reader` | Field Hex → Reader |
| Settings | `settings` | Field Hex → Settings |

`home` remains for deep links but is not a primary tab.

HUD components in `core/hud/`:

- `HadesHudScaffold` — safe insets, Field Hex overlay, route selector sheet
- `CommandHex` / `CommandHexMenu` — draggable orb + floating command panel
- `WebShellController` — web back/forward/reload bridge
- `HudRouteSelectorSheet` — allowlisted Hades Watch routes

Legacy (unwired): `HudBottomDock`, `HudWebControls`, `HudCommandFab`, `HudToolDrawerSheet`.

See [FIELD_HEX_COMMAND_HUD.md](FIELD_HEX_COMMAND_HUD.md) and [HUD_SHELL_NAVIGATION.md](HUD_SHELL_NAVIGATION.md).

## Design System

Reusable components in `core/ui/`:

- `HadesBottomNav` — legacy 5-tab bar (retained; superseded)
- `HadesActionCard`, `HadesToolCard`, `HadesSectionHeader`, `HadesSearchBar`
- `HadesEmptyState`, `HadesWarningBox`, `HadesStatusChip`, `HadesIcon`

## k0R34DER UX

- **Reader** via Field Hex menu
- **Import EPUB** via system picker — see [EPUB_IMPORT.md](EPUB_IMPORT.md)
- Large ORP-style token display with optional focus highlight
- Context preview (previous/next tokens dimmed)
- Start/Pause, punctuation pause, chunk modes, focus mode
- Signal Reader handoff → Reader route
- Default hex position lower-right so central reading token stays clear

## Known UI Limitations

- Web back/forward depends on WebView history within selected route
- ORP highlight is Kotlin-core approximation, not full Flutter UI parity
- No landscape-specific layouts yet
- EPUB: merged plain text only (no per-chapter UI)
- Command menu uses floating panel style; radial menu not yet implemented
- Reader-specific quick actions (play/pause in menu) deferred — navigation actions only

## Manual Test Notes

See [MANUAL_TESTING.md](MANUAL_TESTING.md) — verify Field Hex, safe insets, overlay-free WebShell, EPUB import.
