# Field Hex Command HUD

The **Field Hex** is the primary native navigation control in Hades Watch Field Terminal. It replaces the fixed bottom dock, bulky web header, and harsh footer banner with a single draggable cyberpunk hexagon that floats above app content.

## Why Fixed Chrome Was Removed

Earlier HUD milestones used:

- A **4-tab bottom dock** that consumed vertical space and could collide with gesture/3-button navigation
- A **collapsible web header** (`HudWebControls`) that still overlapped the Android status bar on some devices
- A **command FAB + tools drawer** duplicating navigation already available elsewhere

User feedback: login and web pages felt cramped; menu controls sat under the system time; the footer felt harsh.

The Field Hex keeps native controls available without permanently blocking the Hades Watch mobile website.

## Behavior

| Action | Result |
|--------|--------|
| **Tap** | Opens floating command menu anchored near the hex |
| **Drag** | Repositions hex within safe screen bounds |
| **Long press** | Resets hex to default position (lower-right, above nav area) |
| **Tap outside menu** | Dismisses menu |
| **Back** | Closes menu before navigating away |

### Position persistence

- X/Y stored as screen fractions in DataStore (`CommandHexPositionStore`)
- Clamped on every layout change using `WindowInsets.safeDrawing`
- Invalid positions after rotation are corrected automatically
- **Settings → Field Hex → Reset position** clears stored coordinates

### Default position

Lower-right (`0.88`, `0.82` of safe area), above the system navigation bar.

## Command Menu

Floating panel style (radial reserved for future work). Actions include:

**Primary navigation**

- Web, Tools, Reader (k0R34DER), Settings

**Web controls** (when WebShell is active)

- Back, Forward, Refresh, Route selector, Open externally

**Tool shortcuts**

- k0R34DER, Signal Reader, Underworld Gateways, Field Notes, Local Tool Data, Tools Hub

**Other**

- Hide HUD 10s (temporary), Reset hex position, Privacy & Safety

Menu flips/anchors based on hex location so items stay on-screen and clear of status/nav bars.

## WebShell Integration

- WebView is **full screen** — no permanent browser banner or `hadeswatch.com` strip
- Optional brief **safety chip** on load (“Hades Watch secure shell” + host) — fades when disabled in Settings
- Route selector opens from Field Hex menu; allowlisted paths only
- `WebShellController` bridges menu actions to WebView back/forward/reload without JavaScript injection

Trusted-domain policy is unchanged: `hadeswatch.com` / `www.hadeswatch.com` only.

## Safe Insets

- **WebShell:** content may draw edge-to-edge; interactive controls use `safeDrawing` insets
- **Tool/Settings screens:** `safeDrawingPadding()` on scroll content
- **Field Hex:** clamped inside safe bounds; never placed under status bar, cutout, or navigation bar
- **Fallback when hex disabled:** small Settings button bottom-end with safe padding — user cannot lose navigation

## Settings (Field Hex)

| Setting | Options |
|---------|---------|
| Enable Field Hex | On / Off |
| Size | Small / Medium / Large |
| Opacity | Low / Medium / High |
| Show safety chip on load | On / Off |
| Reset position | Clears stored X/Y |

Disabling the hex hides it on most screens but keeps a Settings fallback affordance.

## Accessibility

- Content description: **“Open Field Terminal menu”**
- Menu items use icon + label (not icon-only)
- Minimum 48dp touch targets where practical
- Non-drag alternative: Settings → Reset position
- Reduced motion respected for chip fade animations
- Back dismisses menu

## Touch Reliability

- Drag threshold prevents accidental taps after repositioning
- Hex sits above WebView in the overlay layer; WebView receives touches when menu is closed
- No full-screen transparent interceptors when menu is dismissed

## Package Layout

`app/src/main/java/com/heathen/hadeswatch/core/hud/`

| File | Role |
|------|------|
| `CommandHex.kt` | Draggable hex UI |
| `CommandHexState.kt` | Menu/drag/hide state |
| `CommandHexPositionStore.kt` | DataStore persistence |
| `CommandHexMenu.kt` | Floating command panel |
| `CommandHexAction.kt` | Menu action model |
| `CommandHexBounds.kt` | Clamping, size/opacity enums |
| `HexagonShape.kt` | Flat-top hex `Shape` |
| `WebShellController.kt` | Web back/forward/reload/route bridge |
| `HadesHudScaffold.kt` | Root overlay host |

Legacy components (`HudBottomDock`, `HudWebControls`, `HudCommandFab`) remain in the repo but are no longer wired into the live shell.

## Security Notes

- Field Hex is **app navigation only** — not a WebView bridge
- No scraping, JS injection, or credential interception
- Gateway URLs remain separate from Hades Watch cookie/session isolation

See [WEBVIEW_SECURITY.md](WEBVIEW_SECURITY.md) and [PRIVACY_AND_SAFETY.md](PRIVACY_AND_SAFETY.md).

## Manual Tests

See [MANUAL_TESTING.md](MANUAL_TESTING.md) — Field Hex and overlay-free WebShell sections.
