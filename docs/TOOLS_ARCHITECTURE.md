# Tools Architecture

## Registry Pattern

Tools are declared in `ToolRegistry` as `ToolDefinition` entries consumed by `ToolsHubScreen`.

Each tool includes:

| Field | Purpose |
|-------|---------|
| `id` | Stable identifier |
| `name` | Display name |
| `description` | Short summary |
| `status` | Available, ComingSoon, WebShortcut, etc. |
| `classification` | LOCAL_ONLY, WEB_SHORTCUT, FUTURE_API, PERMISSION_GATED, COMING_SOON |
| `category` | READING, LAUNCHER, OBSERVER, NOTES, WEB, SYSTEM, FUTURE |
| `iconKey` | Icon lookup key for future UI |
| `permissionsNeeded` | Human-readable permission summary |
| `safetyNote` | Safety copy shown on tool card |
| `route` | Native navigation route |
| `webUrl` | Website shortcut URL |
| `settingsAction` | Settings deep link |
| `settingsToggleKey` | DataStore key for enable/disable |

## Current Tools

| Tool | Classification | Category | Status |
|------|----------------|----------|--------|
| k0R34DER | LOCAL_ONLY | READING | Available |
| Underworld Gateways | LOCAL_ONLY | LAUNCHER | Available |
| 4R3S | PERMISSION_GATED | OBSERVER | Coming Soon |
| Field Notes | LOCAL_ONLY | NOTES | Available |
| Dead Drop Tracker | WEB_SHORTCUT | WEB | Web shortcut |
| Signal Reader | FUTURE_API | FUTURE | Coming Soon |
| Accessibility View | LOCAL_ONLY | SYSTEM | Settings shortcut |

## Adding a Future Tool

1. Add `HadesDestination` route(s) in `HadesDestinations.kt`
2. Register composable(s) in `HadesNavGraph.kt`
3. Add `ToolDefinition` to `ToolRegistry.kt`
4. Add Settings toggle + clear-data handler if needed
5. Document in `docs/PRIVACY_AND_SAFETY.md`
6. Update `routeForBottomNav` if the tool lives under Tools tab

## Module Boundaries

- **Reading tools** may depend on `:k0r34d3r-core`
- **Launcher tools** (Gateways) must not use Hades Watch WebShell
- **WEB_SHORTCUT** tools navigate to `HadesDestination.webRoute(...)` only for hadeswatch.com paths
- **FUTURE_API** tools remain placeholders until official endpoints exist

## Room for Growth

The registry is structured to add at least two more native tools (e.g. Signal Reader, expanded 4R3S) without refactoring the Tools Hub UI.
