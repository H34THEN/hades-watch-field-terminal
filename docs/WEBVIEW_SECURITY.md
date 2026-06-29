# WebView Security Model

## Trusted Domains

Only these hosts load inside the in-app WebView:

- `hadeswatch.com`
- `www.hadeswatch.com`

All other HTTPS hosts open in the system browser (when enabled in Settings).

## Transport & Content

- HTTPS only for in-app navigation
- Mixed content blocked (`MIXED_CONTENT_NEVER_ALLOW`)
- Safe Browsing enabled where supported
- JavaScript enabled only for trusted in-app pages (required by the site)
- DOM storage enabled for session persistence
- File access disabled
- Universal file access disabled
- No `addJavascriptInterface`
- No injected JavaScript

## External Schemes

Safe external schemes handled outside WebView:

- `mailto:`
- `tel:`

Unknown or non-HTTPS schemes are not loaded in-app.

## User Agent

Appends `HadesWatchAndroid/0.1` to the default WebView user agent for support identification.

## Authentication

- Login at `https://hadeswatch.com/login` inside WebView
- Website handles credentials
- Cookies managed by Android `CookieManager`
- Settings provides **Clear website session** and **Clear WebView cache**
- No credential logging, interception, or manual cookie storage

## Logging Policy

Do not log:

- Cookies or session tokens
- Passwords or auth headers
- URLs with sensitive query strings

## Positioning

Hades Watch Field Terminal is **not a general browser**. It is a sandboxed mobile HUD shell for the Hades Watch website with local-first Field Terminal tools as overlays.

- No arbitrary URL entry in the trusted WebShell
- No JavaScript bridges or page scraping
- No website credential storage
- EPUB imports processed locally only

## Back Navigation

WebView back stack integrated with Android back gesture via `BackHandler`.

## Error Handling

Failed main-frame loads show a friendly offline/error screen with retry — no silent failure.

## Underworld Gateways (Separate from WebShell)

Underworld Gateways is **not** part of the Hades Watch trusted WebView policy.

- User-saved gateway URLs are **never** added to `TrustedDomainPolicy`
- Gateway launches use `Intent.ACTION_VIEW` (external browser)
- Gateway sessions **do not** share Hades Watch WebView cookies
- HTTP LAN URLs require user confirmation before launch

See [UNDERWORLD_GATEWAYS.md](UNDERWORLD_GATEWAYS.md).
