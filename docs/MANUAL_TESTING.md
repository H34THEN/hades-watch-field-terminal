# Manual Testing Checklist

Use this checklist after builds or milestone changes.

## Navigation (5-tab model)

- [ ] Bottom nav shows: Home, Web, Tools, Reader, Settings — each with icon + label
- [ ] Selected tab is visually obvious
- [ ] No label overlap on small phone width
- [ ] Web tab opens route selector + WebShell
- [ ] Reader tab opens k0R34DER directly
- [ ] Tool sub-screens highlight Tools tab in bottom nav

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
- [ ] Optional save last text locally (Settings toggles in reader)

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
