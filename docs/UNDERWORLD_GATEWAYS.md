# Underworld Gateways

## Purpose

Underworld Gateways is a **local-first launcher** for user-defined NAS, homelab, and self-hosted service URLs (Jellyfin, Home Assistant, MeTube, slskd, dashboards, etc.).

It is themed for Hades Watch Field Terminal but is **not** part of the trusted Hades Watch website WebShell.

## MVP Behavior

- Add / edit / delete gateway cards
- Fields: display name, URL, built-in icon, optional category, optional note
- Built-in icons: NAS, Media, Music, Downloads, Home Assistant, Terminal, Dashboard, Archive, Custom
- Local persistence via DataStore (JSON-encoded list)
- URL validation: must start with `http://` or `https://`
- HTTP URLs show a confirmation dialog (common for LAN services)
- Launch opens **externally** via `Intent.ACTION_VIEW` (system browser)

## Security Model

| Hades Watch WebShell | Underworld Gateways |
|---------------------|---------------------|
| Allowlist: `hadeswatch.com` | User-defined URLs |
| Shares WebView cookies | **No** Hades Watch cookies |
| Trusted domain policy | **Not** in `TrustedDomainPolicy` |
| In-app WebView | External browser launch |

Gateway URLs are **intentionally user-controlled**. They must never inherit Hades Watch session state or be mixed into the Hades Watch allowlist without an explicit product/security review.

## Permissions

**None** in MVP. Icon selection uses built-in Material icons — no storage or photo picker permissions.

## Data Control

Settings → **Clear Underworld Gateways** or **Clear all local tool data**.

## Future Enhancements (Not in MVP)

- Optional isolated in-app generic viewer (clearly labeled, no cookie sharing)
- Official API sync of gateway lists (with explicit consent)
- Custom image icons via system photo picker (no broad storage permission)
