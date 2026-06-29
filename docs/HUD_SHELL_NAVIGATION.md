# HUD Shell Navigation

Hades Watch Field Terminal is a **cyberpunk HUD companion shell** around the mobile Hades Watch website — not a generic browser.

## Layout

| Layer | Component | Purpose |
|-------|-----------|---------|
| Content | `WebHubScreen`, tool screens | Dominant full-screen area |
| Bottom dock | `HudBottomDock` | 4 primary tabs: Web, Tools, Reader, Settings |
| Web overlay | `HudWebControls` | Collapsed chip + expandable back/forward/reload/routes |
| Command FAB | `HudCommandFab` | Opens Field Terminal tools drawer from Web tab |
| Sheets | `HudRouteSelectorSheet`, `HudToolDrawerSheet` | Allowlisted routes + local tools |

Package: `app/src/main/java/com/heathen/hadeswatch/core/hud/`

## Navigation Rules

- **Single app-level `NavHostController`** in `MainActivity`
- **Start destination:** `web?url=https://hadeswatch.com/dashboard` (Web tab)
- Tab selection uses `popUpTo(startDestination) { saveState = true }` + `launchSingleTop` + `restoreState`
- `resolveTabRoute()` maps nested routes to dock tabs:
  - `web*` → Web
  - `tools*` → Tools
  - `reader` / `tools/k0reader` → Reader
  - `settings*` → Settings
- Tool drawer and sub-routes use simple `navigate(route) { launchSingleTop = true }`

## Web Tab UX

- WebView fills the screen; bottom dock overlays with 72dp content inset
- Default **collapsed** HUD chip: “Hades Watch” + shield context
- **Expanded** controls: host label, back, forward, reload, route selector, open externally
- No address bar; no arbitrary URL entry
- Route selector lists allowlisted Hades Watch paths only

## Home Screen

`home` route remains in the nav graph for deep links but is **not** on the bottom dock. Use route selector or deep link `hadeswatch://dashboard` to reach web content.

## Manual Tests

- [ ] Web / Tools / Reader / Settings dock tabs always respond
- [ ] Selected tab matches current destination after tool sub-navigation
- [ ] Web login page has minimal top chrome (collapsed chip only)
- [ ] Expand web controls → back/forward/reload work
- [ ] Route selector opens from expanded controls
- [ ] FAB on Web tab opens tools drawer
- [ ] Tools drawer entries navigate without dead buttons
- [ ] Back from tool sub-screens returns predictably

See [MANUAL_TESTING.md](MANUAL_TESTING.md).
