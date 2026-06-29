# Gateway Viewer Cookie Isolation

Honest documentation for the optional **Gateway Viewer** (`ISOLATED_IN_APP_VIEWER`) vs **Hades Watch WebShell**.

## Why Gateway Viewer Is Separate

- User-defined NAS/homelab URLs are **not** in `TrustedDomainPolicy`
- Gateway Viewer uses its own UI, navigation policy, and user agent (`HadesWatchGatewayViewer/0.1`)
- No manual cookie copying from Hades Watch WebShell
- External browser remains the **default** launch mode (safest)

## What Is Isolated

| Layer | Isolated? |
|-------|-----------|
| Compose screen / route | Yes |
| WebView client policy | Yes — `GatewayViewerPolicy`, not `TrustedDomainPolicy` |
| User agent suffix | Yes |
| HTTP confirmation dialog | Yes |
| Hades Watch session clear action | Yes — separate Settings controls |

## What Is Not Perfectly Isolated

Android WebView cookie storage is often **process- or profile-level**, depending on OS and WebView version:

- Gateway Viewer and Hades WebShell may share the platform `CookieManager` in the same app process
- **Clear Gateway Viewer cache** clears a throwaway WebView instance cache/history only — it does **not** call `removeAllCookies()` and does **not** use the Hades Watch session clear function
- **Clear website session** removes cookies globally for the app process — may affect gateway sites visited in WebView

We document this honestly in Settings and Local Tool Data management.

## Settings Actions (Separate)

| Action | Effect |
|--------|--------|
| Clear website session | Cookies via `SessionManager` |
| Clear Hades Watch WebView cache | Hades WebShell cache helper |
| Clear Gateway Viewer cache | Throwaway WebView cache only |
| Clear local tool data | DataStore snippets/gateways/notes — not cookies |

## Future Research

- Android 14+ / WebView profile partitioning if available
- Custom Tabs vs in-app WebView tradeoffs
- Separate process WebView (complexity vs benefit)
- Per-site data clearing APIs as they mature

## Related

- [UNDERWORLD_GATEWAYS.md](UNDERWORLD_GATEWAYS.md)
- [WEBVIEW_SECURITY.md](WEBVIEW_SECURITY.md)
- [PRIVACY_AND_SAFETY.md](PRIVACY_AND_SAFETY.md)
