# Manual Testing Checklist

Use this checklist after builds or milestone changes.

## Field Hex Command HUD

- [ ] Field Hex appears on WebShell (default lower-right, above nav area)
- [ ] Field Hex appears on Tools, Reader, Settings, and tool sub-screens
- [ ] Tap hex → command menu opens with labeled actions
- [ ] Tap outside menu or press Back → menu closes
- [ ] Drag hex smoothly within screen; no status bar / nav bar overlap after drag
- [ ] Release drag → position saved; restart app → same position restored
- [ ] Long press hex → resets to default position
- [ ] Settings → Field Hex → Reset position works
- [ ] Size (Small/Medium/Large) and opacity settings apply
- [ ] Disable Field Hex → small Settings fallback button visible; user not trapped
- [ ] Hex contentDescription present (TalkBack: “Open Field Terminal menu”)
- [ ] Drag does not accidentally open menu; tap does not accidentally drag

## Field Hex Menu Actions

- [ ] Web → navigates to WebShell
- [ ] Tools → Tools Hub
- [ ] Reader → k0R34DER
- [ ] Settings → Settings screen
- [ ] k0R34DER, Signal Reader, Underworld Gateways, Field Notes, Local Tool Data shortcuts work
- [ ] Hide HUD 10s temporarily hides hex
- [ ] Reset hex position from menu works

## WebShell (overlay-free)

- [ ] Login page has **no** bulky top browser banner or permanent footer dock
- [ ] Web content uses maximum vertical space
- [ ] Optional safety chip appears briefly on load (if enabled) then fades
- [ ] From WebShell menu: Back, Forward, Refresh work
- [ ] Route selector opens from menu — Dashboard, MMO, Dead Drops, Forums, Profile, Notifications, Login
- [ ] Open externally works for allowlisted pages when setting enabled
- [ ] External links open externally (when setting enabled)
- [ ] Unknown domains do not become trusted in WebShell
- [ ] Website touch input works when hex menu is closed

## Safe Insets

- [ ] No app chrome overlaps Android status bar / system time
- [ ] Field Hex and menu controls are tappable (not under cutout)
- [ ] No fixed footer overlaps gesture or 3-button navigation bar
- [ ] Landscape: hex and menu remain usable (clamped on-screen)

## k0R34DER

- [ ] Reader opens from Field Hex
- [ ] Start/Pause auto-advances tokens
- [ ] WPM slider and faster/slower buttons work
- [ ] Chunk mode chips (1/2/3/Phrase) work
- [ ] ORP highlight toggle works
- [ ] Punctuation pause toggle works
- [ ] Focus mode hides input while playing
- [ ] Signal Reader handoff loads text with source label
- [ ] Clear transfer / clear text works
- [ ] Import EPUB — system picker, parse progress, title + token count
- [ ] EPUB RSVP playback works
- [ ] Clear imported book works
- [ ] Bad/corrupt EPUB shows friendly error (no crash)
- [ ] Field Hex default position does not block central reading token

## Signal Reader

- [ ] Add / edit / delete snippet
- [ ] Search filters list
- [ ] Copy export JSON to clipboard
- [ ] Share JSON via share sheet
- [ ] Import JSON — preview, merge, result summary
- [ ] Import does not overwrite existing IDs silently
- [ ] Read in k0R34DER — local transfer only
- [ ] Clear Signal Reader data (Local Tool Data screen)

## Underworld Gateways

- [ ] Add HTTP LAN URL — HTTP warning shown
- [ ] External browser launch
- [ ] Isolated in-app viewer launch — labeled UI, separate UA
- [ ] Custom icon via photo picker
- [ ] Export / import JSON
- [ ] Clear gateway list (Local Tool Data)
- [ ] Clear Gateway Viewer cache — separate from website session

## Settings

- [ ] Field Hex section: enable, size, opacity, safety chip, reset position
- [ ] Website session clear separate from local tool data
- [ ] Local Tool Data management screen — all categories + confirmations
- [ ] Future API Status screen — planned endpoints, no live calls
- [ ] Privacy & Safety reflects current tools

## Tools Hub

- [ ] Sections: Local, Web Shortcuts, Gateways, Permission-Gated, Coming Soon
- [ ] Search filters tools by name
- [ ] Status / classification chips visible
- [ ] Compact vs spacious cards (Large text setting)

## Build & Install

```bash
./gradlew :k0r34d3r-core:test
./gradlew assembleDebug
```

- [ ] `:k0r34d3r-core:test` passes (including golden fixtures)
- [ ] `assembleDebug` succeeds
- [ ] APK exists at `app/build/outputs/apk/debug/app-debug.apk`

```bash
adb install --streaming -r app/build/outputs/apk/debug/app-debug.apk
```

Fallback:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

- [ ] Install succeeds on connected device

## Automated Tests

```bash
./gradlew :k0r34d3r-core:test
```

Expected: all parity + golden fixture tests pass.
