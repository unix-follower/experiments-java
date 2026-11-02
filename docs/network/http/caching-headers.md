# HTTP caching headers

| Header | Description |
|--------|-------------|
| `Cache-Control` | Main directive header for both requests and responses; controls who may cache a response and for how long. |
| `Cache-Control: max-age=<s>` | Response stays fresh for `<s>` seconds from generation. Overrides `Expires`. |
| `Cache-Control: s-maxage=<s>` | Like `max-age` but only for shared caches (CDN, proxy); overrides `max-age` there. |
| `Cache-Control: no-cache` | Cache may store the response but must revalidate with the origin before every reuse. |
| `Cache-Control: no-store` | Nothing may be stored anywhere (sensitive data). |
| `Cache-Control: public` | Any cache, including shared ones, may store the response (even if the request had `Authorization`). |
| `Cache-Control: private` | Only the end user's cache (browser) may store it; shared caches must not. |
| `Cache-Control: must-revalidate` | Once stale, the response must not be reused without successful revalidation. |
| `Cache-Control: proxy-revalidate` | Same as `must-revalidate`, but applies to shared caches only. |
| `Cache-Control: immutable` | Content will never change while fresh; the browser skips revalidation on reload. |
| `Cache-Control: no-transform` | Intermediaries must not alter the body (e.g. image recompression). |
| `Cache-Control: stale-while-revalidate=<s>` | A stale response may be served for `<s>` seconds while it is refreshed in the background. |
| `Cache-Control: stale-if-error=<s>` | A stale response may be served for `<s>` seconds if the origin returns an error. |
| `Cache-Control: max-stale[=<s>]` | Request: client accepts a stale response (optionally no staler than `<s>` seconds). |
| `Cache-Control: min-fresh=<s>` | Request: client wants a response that stays fresh for at least `<s>` more seconds. |
| `Cache-Control: only-if-cached` | Request: return a cached response only, otherwise `504 Gateway Timeout`. |
| `Expires` | Absolute date after which the response is stale. Legacy; ignored when `Cache-Control: max-age` is present. |
| `Pragma: no-cache` | HTTP/1.0 request/response header, roughly `Cache-Control: no-cache`. Use only for backward compatibility. |
| `Age` | Seconds the response has spent in caches since it was generated or validated by the origin. |
| `ETag` | Opaque validator identifying a specific version of the resource (strong `"abc"` or weak `W/"abc"`). |
| `Last-Modified` | Date the resource was last changed; a weaker validator than `ETag`, also used for heuristic freshness. |
| `If-None-Match` | Conditional request carrying an `ETag`; server replies `304 Not Modified` if it still matches. |
| `If-Modified-Since` | Conditional request; server replies `304 Not Modified` if unchanged since the given date. |
| `If-Match` | Proceed only if the `ETag` matches (else `412`); used for optimistic concurrency and safe resumes. |
| `If-Unmodified-Since` | Proceed only if the resource has not changed since the date (else `412`). |
| `If-Range` | Send the requested byte range only if the validator matches; otherwise send the whole resource. |
| `Vary` | Lists request headers (e.g. `Accept-Encoding`, `Accept-Language`) that select the stored variant; part of the cache key. |
| `Date` | Time the response was generated; the base for freshness and `Age` calculations. |
| `Clear-Site-Data` | Tells the browser to clear cached data for the site (`"cache"`, `"cookies"`, `"storage"`). |
| `Surrogate-Control` | Caching directives aimed at reverse proxies/CDNs (e.g. Varnish, Fastly); usually stripped before reaching the client. |
| `CDN-Cache-Control` | Standard (RFC 9213) targeted header with `Cache-Control` syntax, honored by CDNs only. |
| `Warning` | Deprecated (RFC 9111) caching-related warnings such as `110 Response is Stale`; do not use. |
