# Underworld Gateways

## Purpose

Local-first launcher for user-defined NAS, homelab, and self-hosted URLs.

**Not part of the Hades Watch trusted WebShell.**

## Launch Modes

| Mode | Default | Behavior |
|------|---------|----------|
| `EXTERNAL_BROWSER` | **Yes** | `Intent.ACTION_VIEW` — safest for arbitrary URLs |
| `ISOLATED_IN_APP_VIEWER` | Opt-in | Separate Gateway Viewer WebView |

## Gateway Viewer Security

The Gateway Viewer (`GenericGatewayViewerScreen`) is **not** the Hades Watch WebShell:

- Does **not** use `TrustedDomainPolicy`
- Separate user agent suffix: `HadesWatchAndroidGatewayViewer/0.1`
- Labeled UI: "Gateway Viewer" / "Not Hades Watch"
- HTTP requires confirmation before load
- Cross-host navigation opens externally
- No `addJavascriptInterface`, no injected JS
- No credential/cookie logging

### Cookie Isolation Limitation

Android WebView shares process-level `CookieManager`. The app:

- Does not copy Hades Watch cookies to gateways
- Does not use Hades WebShell instances for gateways
- Provides **Clear Gateway Viewer cache** in Settings

Full cookie isolation between WebViews is **not guaranteed** by Android — documented honestly.

## Custom Icons

- Built-in icon set (NAS, Media, Music, etc.)
- Optional custom image via **system Photo Picker** (no storage permission)
- URI stored in `customIconUri`; persistence depends on Android URI grants

## Import / Export

- **Export:** Copy JSON to clipboard or share via `ACTION_SEND`
- **Import:** Paste JSON, preview validation, merge without overwriting existing IDs
- All imported URLs validated (`http://` or `https://`)

## Data Model

`GatewayDefinition` includes: id, displayName, url, iconKey, customIconUri, category, note, launchMode, timestamps, sortOrder.

## Permissions

None beyond app-level INTERNET (WebView/ browser launch).
