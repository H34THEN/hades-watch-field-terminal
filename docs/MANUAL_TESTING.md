# Manual Testing Checklist

Use this checklist after builds or milestone changes.

## Navigation (HUD 4-tab dock)

- [ ] Bottom dock shows: Web, Tools, Reader, Settings — each with icon + label
- [ ] Selected tab is visually obvious after tool sub-navigation
- [ ] Web tab is default start (dashboard URL)
- [ ] Web HUD chip collapsed by default — login page has maximum vertical space
- [ ] Expand web controls → back, forward, reload, routes, open externally
- [ ] Route selector lists Dashboard, MMO, Dead Drops, Forums, Profile, Notifications, Login
- [ ] FAB on Web tab opens Field Terminal tools drawer
- [ ] Tools drawer quick actions navigate (k0R34DER, Signal Reader, Gateways, etc.)
- [ ] Reader tab opens k0R34DER directly
- [ ] Tool sub-screens highlight Tools tab in dock
- [ ] No dead dock buttons

## Hades Watch WebShell

- [ ] MMO tab loads `https://hadeswatch.com/mmo`
- [ ] Forums tab loads `https://hadeswatch.com/community/forums`
- [ ] External links open externally (when setting enabled)
- [ ] Unknown domains do not become trusted in WebShell

## k0R34DER

- [ ] Reader tab opens k0R34DER
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
