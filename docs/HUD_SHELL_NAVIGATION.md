# HUD Shell Navigation

Hades Watch Field Terminal is a **cyberpunk HUD companion shell** around the mobile Hades Watch website — not a generic browser.

## Layout (current)

| Layer | Component | Purpose |
|-------|-----------|---------|
| Content | `WebHubScreen`, tool screens | Dominant full-screen area |
| Overlay | **Field Hex** (`CommandHex`) | Draggable command orb — primary navigation |
| Menu | `CommandHexMenu` | Floating panel: Web, Tools, Reader, Settings, web/tool actions |
| Sheets | `HudRouteSelectorSheet` | Allowlisted Hades Watch routes (from hex menu) |
| Bridge | `WebShellController` | Back/forward/reload/route selector for WebShell |

Package: `app/src/main/java/com/heathen/hadeswatch/core/hud/`

See [FIELD_HEX_COMMAND_HUD.md](FIELD_HEX_COMMAND_HUD.md) for full Field Hex behavior.

## Removed / Unwired

These components are **no longer part of the live shell**:

- `HudBottomDock` — 4-tab bottom banner
- `HudWebControls` — collapsible top web chrome
- `HudCommandFab` + `HudToolDrawerSheet` — redundant with Field Hex menu

## Navigation Rules

- **Single app-level `NavHostController`** in `MainActivity`
- **Start destination:** `web?url=https://hadeswatch.com/dashboard`
- Field Hex menu navigates via `handleCommandHexAction()` in `HadesHudScaffold`
- Tab-like destinations still exist in the nav graph: Web, Tools, Reader, Settings, tool sub-routes
- `resolveTabRoute()` maps nested routes for menu context (web vs tools vs reader)

## Web Tab UX

- WebView fills the screen — **no permanent browser banner**
- Optional brief safety chip on load (Settings toggle)
- Back, forward, reload, route selector, open externally — **Field Hex menu only**
- No address bar; no arbitrary URL entry
- Route selector lists allowlisted Hades Watch paths only

## Safe Insets

- Web content edge-to-edge; hex and menus respect `WindowInsets.safeDrawing`
- Tool/Settings screens use `safeDrawingPadding()` on content
- No fixed header/footer overlays colliding with status or navigation bars

## Home Screen

`home` route remains in the nav graph for deep links. Use route selector or deep link `hadeswatch://dashboard` to reach web content.

## Manual Tests

- [ ] Field Hex visible on WebShell and tool screens
- [ ] Tap hex → menu opens; tap outside or Back dismisses
- [ ] Drag hex → position persists after restart
- [ ] Long press or Settings reset restores default position
- [ ] Web login page has no bulky top banner
- [ ] Menu: Web, Tools, Reader, Settings navigate correctly
- [ ] On WebShell: back, forward, refresh, route selector work from menu
- [ ] Hex does not overlap status bar or navigation bar
- [ ] With hex disabled, Settings fallback button remains reachable

See [MANUAL_TESTING.md](MANUAL_TESTING.md).
