# UI/UX Rebuild

Milestone: simplify navigation, unify design system, upgrade k0R34DER reading experience.

## Navigation Model

Bottom navigation reduced from **8 tabs to 5**:

| Tab | Route | Purpose |
|-----|-------|---------|
| Home | `home` | Dashboard, quick actions, local tools |
| Web | `web?url={url}` | Hades Watch WebShell with route selector |
| Tools | `tools` | Native/local tools grouped by type |
| Reader | `reader` | k0R34DER speed reader (top-level) |
| Settings | `settings` | App settings, privacy, local data |

Legacy routes (`mmo`, `forums`, etc.) remain as deep links → Web tab with appropriate URL.

Tool sub-routes (`tools/gateways`, `tools/signalreader`, …) map back to **Tools** tab in bottom nav.

## Design System

Reusable components in `core/ui/`:

- `HadesBottomNav` — consistent 5-tab bar with labels
- `HadesActionCard` — primary dashboard actions with icons
- `HadesToolCard` — tool list entries (icon, title, status chips, safety expand)
- `HadesSectionHeader` — section titles
- `HadesSearchBar` — consistent search field
- `HadesEmptyState` — empty lists with next-step guidance
- `HadesWarningBox` — HTTP / isolation / coming-soon warnings
- `HadesStatusChip` — readable status badges
- `HadesIcon` / `ToolIconKey` — centralized icon mapping

## k0R34DER UX Upgrades

- Dedicated **Reader** bottom nav tab
- Large ORP-style token display with optional focus highlight
- Context preview (previous/next tokens dimmed)
- Start/Pause auto-advance with WPM-based timing
- Punctuation-aware pause weighting (optional)
- Speed up/down buttons (+/- 25 WPM)
- Chunk mode chips: 1 / 2 / 3 words / Phrase
- Focus mode hides input while playing
- Optional local session text save (DataStore, clearable)
- Keep screen on while playing (no extra permission)
- Pause on leave screen
- Signal Reader handoff → Reader tab

## Known UI Limitations

- Web back/forward depends on WebView history within selected route
- ORP highlight is Kotlin-core approximation, not full Flutter UI parity
- No landscape-specific layouts yet (scroll-based fallback)
- Tag filter chips for Signal Reader not implemented (search only)

## Manual Test Notes

See [MANUAL_TESTING.md](MANUAL_TESTING.md) — verify 5-tab nav, Reader playback, Web route chips, Tools grouping.

## Future UI Ideas

- Resume reading banner on Home from saved session
- Custom Tabs option for gateways
- Tag filter chips in Signal Reader
- Landscape reader layout with side controls
- Compose previews in CI for key screens
