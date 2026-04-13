---
name: forge-security-review
description: "Security-focused code review using OWASP guidelines with severity-classified findings."
user-invocable: true
risk: safe
source: "Forge Kit v2"
---

# Skill: Security Review

How to perform a security-focused code review using OWASP guidelines and produce actionable findings.

## Purpose

Review code for security vulnerabilities, focusing on the OWASP Top 10 and common
application security pitfalls. Produces findings classified by severity with concrete remediation steps.

Use when: reviewing code that handles user input, authentication, authorization, sensitive data,
or external API calls. Also use as a complement to `review-pr` for security-critical changes.
Do not use when: the code has no interaction with users, external systems, or sensitive data.

## Inputs

- **Scope**: files or PR to review
- **Application type**: web API, mobile backend, internal service, CLI tool
- **Data sensitivity**: what sensitive data flows through this code (PII, credentials, financial, health)
- **Auth model**: how authentication and authorization work in this system

## Steps

### 1. Map the attack surface

- Identify all entry points: HTTP endpoints, message consumers, CLI arguments, file uploads
- For each entry point: what user-controlled data enters the system?
- Trace user input through the code: where is it used? Is it ever used in SQL, HTML, shell commands, file paths, or log statements?

### 2. Check injection vulnerabilities (OWASP A03)

- **SQL Injection**: is user input concatenated into SQL queries? Look for string formatting in queries.
  - Fix: use parameterized queries or ORM methods exclusively
- **Command Injection**: is user input passed to shell commands (`os.system`, `subprocess`, `exec`)?
  - Fix: use subprocess with list arguments, never shell=True with user input
- **XSS**: is user input rendered in HTML without escaping?
  - Fix: use framework auto-escaping, explicitly escape dynamic content
- **Path Traversal**: is user input used in file paths without validation?
  - Fix: validate against a whitelist, use `os.path.realpath` and check prefix

### 3. Check authentication and authorization (OWASP A01, A07)

- Are all endpoints that should require auth actually protected?
- Is authorization checked at the resource level (not just the endpoint)?
- Are passwords hashed with a strong algorithm (bcrypt, argon2)?
- Are JWT tokens validated: signature, expiration, issuer, audience?
- Are session tokens generated with cryptographic randomness?
- Is there rate limiting on login and password reset endpoints?

### 4. Check sensitive data handling (OWASP A02)

- Are secrets (API keys, passwords, tokens) stored in environment variables, not in code?
- Is sensitive data encrypted at rest and in transit?
- Is PII being logged? Check log statements for user data.
- Are error messages leaking internal details (stack traces, DB schema, file paths)?
- Is sensitive data returned in API responses that don't need it?

### 5. Check dependency security (OWASP A06)

- Are dependencies pinned to specific versions?
- Are there known CVEs in current dependencies? (check lockfile)
- Are dependencies from trusted sources?
- Is there a process to update dependencies when CVEs are published?

### 6. Check misconfiguration (OWASP A05)

- Are CORS headers restrictive (not `*` in production)?
- Are HTTP security headers set (HSTS, Content-Security-Policy, X-Frame-Options)?
- Are debug endpoints or verbose error pages disabled in production?
- Are default credentials changed?
- Is TLS enforced for all external communication?

## Output Format

```markdown
## Security Review: [scope]

### Attack Surface Summary
- **Entry points**: [N] endpoints / consumers / CLI commands
- **User-controlled inputs**: [list of input sources]
- **Sensitive data in scope**: [PII types, credentials, etc.]

### Findings

#### Critical

**[S-001] [Title]** — [OWASP category]
- **Location**: [file:line]
- **Risk**: [what an attacker could do]
- **Evidence**: [code snippet or pattern found]
- **Remediation**: [concrete fix with code example]

#### High

**[S-002] [Title]** — [OWASP category]
- **Location**: [file:line]
- **Risk**: [impact description]
- **Remediation**: [concrete fix]

#### Medium / Low
[Same format]

### Summary

| Severity | Count |
|----------|-------|
| Critical | [N] |
| High | [N] |
| Medium | [N] |
| Low | [N] |

### Checklist

- [ ] No SQL/command/XSS injection paths
- [ ] Auth required on all sensitive endpoints
- [ ] Authorization checked at resource level
- [ ] No secrets in source code
- [ ] No PII in logs
- [ ] Dependencies free of known CVEs
- [ ] Security headers configured
```

## Example Usage

**Input provided**:
- Scope: PR #78 — "Add public certificate verification endpoint"
- Application type: REST API (Python/FastAPI)
- Data sensitivity: certificate serial numbers (not PII), but endpoint is public (no auth)
- Auth model: JWT-based auth for internal endpoints; this new endpoint is intentionally public

**Execution**:
1. Maps attack surface: 1 new GET endpoint `/api/v1/certificates/verify?serial={serial}`, user-controlled input is `serial` query parameter
2. Injection check: `serial` is used in SQL query via f-string (`f"SELECT * FROM certs WHERE serial = '{serial}'"`) — SQL injection!
3. Auth check: endpoint is public by design, but the query returns internal fields (issuer_private_key_id, internal_notes) — over-exposure
4. Sensitive data: error handler returns full stack trace including DB connection string in dev mode — but dev mode flag is checked by environment variable (acceptable if env is correct)
5. Dependencies: no new dependencies added
6. Misconfiguration: no rate limiting on public endpoint — a scraper could enumerate all certificate serials

**Output**:
```markdown
## Security Review: PR #78 — Add public certificate verification endpoint

### Attack Surface Summary
- **Entry points**: 1 new public GET endpoint `/api/v1/certificates/verify`
- **User-controlled inputs**: `serial` query parameter (string, no validation)
- **Sensitive data in scope**: certificate metadata, internal key IDs

### Findings

#### Critical

**[S-001] SQL Injection via serial parameter** — OWASP A03: Injection
- **Location**: src/api/verify.py:23
- **Risk**: attacker can read/modify/delete any data in the database
- **Evidence**: `f"SELECT * FROM certs WHERE serial = '{serial}'"`
- **Remediation**: use parameterized query:
  ```python
  db.execute(text("SELECT * FROM certs WHERE serial = :serial"), {"serial": serial})
  ```

#### High

**[S-002] Internal fields exposed in public response** — OWASP A01: Broken Access Control
- **Location**: src/api/verify.py:31
- **Risk**: exposes `issuer_private_key_id` and `internal_notes` to unauthenticated users
- **Remediation**: create a `PublicCertificateResponse` schema that only includes: serial, subject, issuer_name, valid_from, valid_to, status

#### Medium

**[S-003] No rate limiting on public endpoint** — OWASP A05: Security Misconfiguration
- **Location**: src/api/verify.py:15
- **Risk**: enables serial number enumeration and potential DoS
- **Remediation**: add rate limiting (e.g., 60 requests/minute per IP) using FastAPI middleware

### Summary

| Severity | Count |
|----------|-------|
| Critical | 1 |
| High | 1 |
| Medium | 1 |
| Low | 0 |

### Checklist

- [x] No command injection or XSS paths
- [ ] **FAIL**: SQL injection via serial parameter
- [x] Auth model appropriate (public endpoint by design)
- [ ] **FAIL**: Internal fields exposed in public response
- [x] No secrets in source code
- [x] No PII in logs
- [x] Dependencies free of known CVEs
- [ ] **FAIL**: No rate limiting on public endpoint
```
