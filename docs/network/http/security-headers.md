# HTTP security headers

| Header | Description |
|--------|-------------|
| `Strict-Transport-Security` (HSTS) | Forces browsers to use HTTPS for the host for `max-age` seconds; `includeSubDomains` and `preload` extend it. Prevents SSL stripping. |
| `Content-Security-Policy` | Whitelists sources for scripts, styles, images, frames, etc. (`default-src`, `script-src`, `frame-ancestors`, ...). Main defense against XSS and injection. |
| `Content-Security-Policy-Report-Only` | Same syntax as CSP but only reports violations; used to test a policy before enforcing it. |
| `X-Content-Type-Options: nosniff` | Stops browsers from MIME-sniffing; the declared `Content-Type` is enforced. |
| `X-Frame-Options` | `DENY` / `SAMEORIGIN`: controls framing to prevent clickjacking. Superseded by CSP `frame-ancestors`. |
| `Referrer-Policy` | Controls how much of the URL is sent in `Referer` (`no-referrer`, `strict-origin-when-cross-origin`, ...). |
| `Permissions-Policy` | Enables or disables browser features (camera, microphone, geolocation, ...) for the page and embedded frames. |
| `Cross-Origin-Opener-Policy` (COOP) | Isolates the browsing context group from cross-origin windows (`same-origin`); mitigates XS-Leaks and Spectre. |
| `Cross-Origin-Embedder-Policy` (COEP) | Requires cross-origin resources to opt in (CORP/CORS); with COOP enables cross-origin isolation. |
| `Cross-Origin-Resource-Policy` (CORP) | Declares who may load the resource (`same-origin`, `same-site`, `cross-origin`); blocks cross-origin reads. |
| `Access-Control-Allow-Origin` | CORS: origin allowed to read the response. Avoid `*` for credentialed or sensitive APIs and never reflect arbitrary origins. |
| `Access-Control-Allow-Credentials` | CORS: allows cookies/auth with cross-origin requests; cannot be combined with `Access-Control-Allow-Origin: *`. |
| `Access-Control-Allow-Methods` / `Access-Control-Allow-Headers` | CORS preflight response: allowed methods and request headers. |
| `Access-Control-Expose-Headers` | CORS: response headers that scripts may read. |
| `Access-Control-Max-Age` | CORS: how long a preflight result may be cached. |
| `Set-Cookie: Secure` | Cookie is sent over HTTPS only. |
| `Set-Cookie: HttpOnly` | Cookie is hidden from JavaScript (limits XSS token theft). |
| `Set-Cookie: SameSite=Strict\|Lax\|None` | Restricts cross-site sending of cookies (CSRF defense); `None` requires `Secure`. |
| `Set-Cookie: __Host-` / `__Secure-` prefixes | Browser-enforced rules: `__Host-` needs `Secure`, `Path=/`, no `Domain`; `__Secure-` needs `Secure`. |
| `Clear-Site-Data` | Instructs the browser to clear cache, cookies and storage (e.g. on logout). |
| `Cache-Control: no-store` | Prevents storing sensitive responses in any cache (see [caching-headers.md](caching-headers.md)). |
| `Origin` | Request header with the origin of the caller; validate it server-side for CSRF and CORS decisions. |
| `Sec-Fetch-Site` / `Sec-Fetch-Mode` / `Sec-Fetch-Dest` / `Sec-Fetch-User` | Browser-set request metadata; servers can reject unexpected cross-site requests (Fetch Metadata isolation policy). |
| `Authorization` | Request credentials (`Basic`, `Bearer`, ...); must only travel over TLS and be excluded from logs and caches. |
| `WWW-Authenticate` | `401` response header declaring the required authentication scheme. |
| `Reporting-Endpoints` | Names endpoints that receive CSP, COOP and other violation reports. |
| `Server` / `X-Powered-By` | Disclose software and version; remove or minimize to reduce fingerprinting. |
| `X-XSS-Protection` | Deprecated and removed from modern browsers; can introduce vulnerabilities. Omit it or send `0`, and use CSP. |
| `Expect-CT` | Deprecated; Certificate Transparency is now enforced by browsers. |
| `Public-Key-Pins` (HPKP) | Removed; risky pinning mechanism. Do not use. |
