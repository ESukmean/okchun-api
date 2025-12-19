1. Auth / Account (/v1/account)

Core auth

POST /v1/account/register
Create account (email, password, profile).

POST /v1/account/login
Login; returns access + refresh tokens.

POST /v1/account/logout
Logout current session (invalidate token/refresh).

POST /v1/account/token/refresh
Refresh access token.

Email & password

POST /v1/account/verify-email/request
Send verification mail.

POST /v1/account/verify-email/confirm
Confirm email with code/token.

POST /v1/account/password/reset/request
Send reset mail.

POST /v1/account/password/reset/confirm
Confirm reset with token + new password.

Profile / multi-org

GET /v1/account/me
Get profile, list of orgs, active_org_id.

PATCH /v1/account/me
Update profile (display name, etc.).

PATCH /v1/account/me/password
Change password (requires current password).

PATCH /v1/account/me/active-organization
Set active_org_id (for UI; backend should still require explicit org_id in paths).

Invites

GET /v1/account/invites
List invites for this account (pending or accepted).

POST /v1/account/invites/{invite_code}/accept
Accept invitation (joins organization, sets role from invite).

Account lifecycle

DELETE /v1/account/me
Close account (soft delete; ensures no org ownership issues).

2. Organization (/v1/organizations)
   2.1. Organization CRUD

POST /v1/organizations
Create organization (name, optional default region).

GET /v1/organizations
List organizations the user belongs to (with role).

GET /v1/organizations/{org_id}
Organization detail.

PATCH /v1/organizations/{org_id}
Update organization name/basic settings.

DELETE /v1/organizations/{org_id}
Soft delete; only owners, requires confirmation and no active billing issues.

2.2. Members & roles

GET /v1/organizations/{org_id}/members?search=&cursor=
List/search members.

POST /v1/organizations/{org_id}/members
Add existing user by email (direct join; for internal tools).

PATCH /v1/organizations/{org_id}/members/{member_id}
Change role (owner/admin/billing/viewer/...).

DELETE /v1/organizations/{org_id}/members/{member_id}
Remove member.

Invites by org

POST /v1/organizations/{org_id}/invites
Create invite (email, role).

GET /v1/organizations/{org_id}/invites?cursor=
List invites for this org.

DELETE /v1/organizations/{org_id}/invites/{invite_id}
Cancel invite.

2.3. API keys (for B2B automation)

GET /v1/organizations/{org_id}/api-keys
List API keys.

POST /v1/organizations/{org_id}/api-keys
Create API key (name, scopes, expiry).

DELETE /v1/organizations/{org_id}/api-keys/{key_id}
Revoke API key.

3. Billing & Credits (/v1/organizations/{org_id}/billing)

Credit-based, prepaid, metered by traffic.

3.1. Credit & balance

GET /v1/organizations/{org_id}/billing/credit
Current credit balance, currency, warning thresholds, and estimated remaining hours/GB.

PATCH /v1/organizations/{org_id}/billing/credit/settings
Update low-credit notifications, auto top-up preferences (if you support).

3.2. Credit top-up (Lemon Squeezy integration)

Your backend will create Lemon Squeezy checkout sessions and process webhooks.

POST /v1/organizations/{org_id}/billing/credit/topups
Create top-up intent.
Request: { amount_credit, return_url }
Response: { checkout_url, topup_id } (Lemon Squeezy hosted page).

GET /v1/organizations/{org_id}/billing/credit/topups?cursor=
List top-up history (status, amount, created_at).

3.3. Usage & charges log

GET /v1/organizations/{org_id}/billing/usage?from=&to=&cursor=
Detailed usage records (per channel, per pool, GB used, credit charged).

GET /v1/organizations/{org_id}/billing/summary?period=day|month
Aggregated usage and credit consumption.

3.4. Webhooks (internal)

POST /v1/webhooks/lemonsqueezy
Lemon Squeezy webhook endpoint; validates signature and updates credits/topups.
(Protected by secret; no auth by session/API key.)

4. Pools (global resource) & org–pool links

Concept:

Pool = dedicated server capacity with attributes:

owner organization (who “owns”/operates it, or OkChun itself)

max bandwidth, shared bandwidth

region/country, latency hints

pricing model (credit per GB, per Mbps, etc.)

visibility: private, shared, public_market

An org can “link” to pools it can use.

4.1. Pools (admin/owner operations)

POST /v1/pools
Create pool (owner_org_id, capacity, region, visibility, pricing).

GET /v1/pools?owner_org_id=&visibility=&region=&cursor=
Admin/owner listing.

GET /v1/pools/{pool_id}
Pool detail (capacity, visibility, pricing).

PATCH /v1/pools/{pool_id}
Update metadata (name, capacity, visibility, pricing, description).

DELETE /v1/pools/{pool_id}
Deactivate pool (no new assignments; may need to drain).

Visibility & marketplace parameters are part of the pool entity:

visibility: private, shared, public_market

public_price_per_gb, public_price_per_mbps, etc.

4.2. Org <-> Pool linking (what org can use)

GET /v1/organizations/{org_id}/pools?type=&region=&cursor=
List pools available to this org (includes:

pools owned by org

pools linked/shared

public marketplace pools that org has “subscribed” to).

POST /v1/organizations/{org_id}/pools/{pool_id}:attach
Attach a pool to org (using marketplace rules / owner approval).

DELETE /v1/organizations/{org_id}/pools/{pool_id}:detach
Detach pool (no new allocations; check for active streams).

For marketplace flows, you may later add:

POST /v1/pools/{pool_id}/offers (optional, for dynamic pricing, etc.)

4.3. Pool reporting (Rust server)

You specified:

Only /pool/report

Token in body

Rust doesn’t call any other pool endpoints

So:

POST /v1/pool/report
Body example:

{
"pool_id": "pool_xxx",
"server_id": "okchun-us-1",
"api_token": "secret-or-signed-token",
"timestamp": 1732612345,
"stats": {
"current_sessions": 123,
"current_bitrate_mbps": 540.3,
"packets_per_second": 3950000,
"cpu_percent": 72.5,
"mem_percent": 61.2,
"nic_queues": [
{ "queue": 0, "pps": 1000000 },
{ "queue": 1, "pps": 900000 }
],
"errors_last_10s": 0
}
}

api_token is validated against a server/pool config table.

If valid & pool_id/server_id match, update last-known status and usage counters.

Optionally, an internal endpoint to query health:

GET /v1/admin/pools/{pool_id}/health
For your ops dashboard.

5. Channels & sessions (admin)

Scoped by org. Channels are long-lived; sessions are individual broadcasts.

5.1. Channels

POST /v1/organizations/{org_id}/channels
Create channel.
Body:

{
"name": "test",
"description": "...",
"tags": ["kr", "live"],
"default_settings": {
"allowed_countries": ["KR"],
"viewer_limit": 10000,
"default_pool_selection": "auto"  // auto/manual
}
}

GET /v1/organizations/{org_id}/channels?search=&state=&cursor=
List/search channels (state active|archived).

GET /v1/organizations/{org_id}/channels/{channel_id}
Channel detail (including default settings).

PATCH /v1/organizations/{org_id}/channels/{channel_id}
Update mutable fields (even if channel name is “logically immutable” you can choose to lock it).

DELETE /v1/organizations/{org_id}/channels/{channel_id}
Archive channel (no new sessions).

5.2. Channel settings and extensions

GET /v1/channels/{channel_id}/settings
Default settings: allowed countries, viewer limit, default extensions configs, etc.

PATCH /v1/channels/{channel_id}/settings
Update default settings.

Extensions

GET /v1/channels/{channel_id}/extensions
List extensions configured (thumbnail plugin, webhook, etc.).

POST /v1/channels/{channel_id}/extensions
Create/attach extension. Body includes type and config.

GET /v1/channels/{channel_id}/extensions/{extension_id}
Extension detail.

PATCH /v1/channels/{channel_id}/extensions/{extension_id}
Update extension configuration.

DELETE /v1/channels/{channel_id}/extensions/{extension_id}
Remove extension.

Invoke extension (instead of /extension/{id}/{data})

POST /v1/channels/{channel_id}/extensions/{extension_id}/invoke
Body:

{
"event": "generate_thumbnail",
"payload": { "...": "..." }
}

5.3. Sessions (per broadcast)

POST /v1/channels/{channel_id}/sessions
Create a new session (start broadcast).
Body includes:

pool_id (or “auto” to let backend pick)

optional overrides for bitrate, allowed viewers, etc.
Response returns:

session_id

WebRTC/SDP info if needed by your signal Node.

GET /v1/channels/{channel_id}/sessions?state=&cursor=
List sessions (live, finished, all).

GET /v1/channels/{channel_id}/sessions/{session_id}
Session detail (start/end time, stats, pool used, total credit consumed).

POST /v1/channels/{channel_id}/sessions/{session_id}/stop
Stop broadcast (admin action).

GET /v1/channels/{channel_id}/sessions/{session_id}/stats
Session stats for dashboard (viewers, bitrate, etc.).

5.4. Admin dashboard view (your /channel/view/...)

You want this only for admin. You can map:

GET /v1/channels/{channel_id}/admin
Aggregate for admin dashboard:

current session (if any) + stats

recent sessions

configured extensions

quick controls (stop, viewer cap changes).

If you want to keep your original path for frontend compat:

GET /v1/channel/view/{channel_id} → internally an alias to /v1/channels/{channel_id}/admin

POST /v1/channel/view/{channel_id}/control → alias to /v1/channels/{channel_id}/sessions/{session_id}/stop etc.

But internal domain model should be “channels + sessions” as above.n

6. Public viewer API (stub for future)

You said ignore detailed signal/public API for now, but you can reserve:

GET /v1/public/channels/{channel_id}
Public metadata (title, thumbnail, status: live/offline).

GET /v1/public/channels/{channel_id}/play
Returns data for initializing viewer (signal server URL, ICE servers, etc.).
Later this goes through your Node signal service.

7. Usage, analytics, audit
   7.1. Usage / analytics

GET /v1/organizations/{org_id}/usage/summary?from=&to=
Aggregated usage per channel and per pool (for billing overview).

GET /v1/organizations/{org_id}/usage/channels?from=&to=&cursor=
Channel-level usage list (GB used, peak viewers).

GET /v1/organizations/{org_id}/usage/pools?from=&to=&cursor=
How much each pool contributed to traffic for this org.

7.2. Audit logs

GET /v1/organizations/{org_id}/audit-logs?actor=&action=&from=&to=&cursor=
Administrative actions (member changes, billing changes, starting/stopping sessions, attaching pools, etc.).

8. Admin / Ops

Internal-only endpoints, protected by separate admin auth.

GET /v1/admin/health
Overall system health.

GET /v1/admin/metrics
Export metrics for Prometheus/etc.

GET /v1/admin/pools?region=&cursor=
All pools with status.

GET /v1/admin/pools/{pool_id}/status
Combined status from Rust reports.

GET /v1/admin/organizations/{org_id}/billing/debug
Internal debugging for credit adjustments/usage.

This path map keeps:

/v1/pool/report exactly as you want (single endpoint, body token).

Pools as independent resources with org links, matching your shared marketplace idea.

Credits as the core billing primitive, with Lemon Squeezy used only for top-ups.

/channel/view semantics preserved via /v1/channels/{channel_id}/admin/alias while keeping a clean channel+session model.