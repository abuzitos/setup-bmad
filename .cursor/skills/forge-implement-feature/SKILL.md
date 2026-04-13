---
name: forge-implement-feature
description: "Feature implementation from acceptance criteria to PR-ready code with incremental delivery."
user-invocable: true
risk: safe
source: "Forge Kit v2"
---

# Skill: Implement Feature

How to implement a feature from acceptance criteria to PR-ready code, following a structured process.

## Purpose

Implement a feature end-to-end: from understanding acceptance criteria to delivering
tested, reviewed-ready code in a clean branch.

Use when: starting implementation of a ticket with defined acceptance criteria.
Do not use when: the task is a bug fix (use `debug-issue`) or a pure refactor (use `refactor-function`).

## Inputs

- **Ticket**: ID, title, and description of the feature
- **Acceptance criteria**: numbered list of verifiable conditions
- **Codebase context**: relevant modules, existing patterns, tech stack
- **Design constraints**: any architectural decisions already made (ADRs, tech lead guidance)

## Steps

### 1. Analyze the scope

- Read every acceptance criterion — clarify ambiguity before writing code
- Identify the files that need to change (read them all before modifying any)
- Estimate scope: is this a local change (1-3 files) or cross-cutting (5+ files)?
- If scope is larger than expected: flag it before starting implementation

**Output**: list of files to modify/create + confirmed understanding of acceptance criteria

### 2. Plan the implementation

Break the feature into small, independently testable increments:
- Each increment should be a commit that leaves the codebase in a working state
- Order increments by dependency: data model → business logic → API → UI
- Identify what can be reused from existing code vs what needs to be built

Rules:
- Implement exactly what the acceptance criteria require — no more, no less
- Follow existing patterns in the codebase — do not introduce new patterns without justification
- If a design decision is needed: document it and ask for confirmation

### 3. Implement increment by increment

For each increment:
1. Write the code following existing file patterns and naming conventions
2. Handle error cases explicitly — no silent failures
3. Write unit tests for the new behavior (see `write-unit-tests` skill)
4. Run all tests — existing and new must pass
5. Commit with a descriptive message: `feat: [what this increment adds]`

### 4. Verify against acceptance criteria

Go through each acceptance criterion one by one:
- [ ] Criterion 1: [verified how]
- [ ] Criterion 2: [verified how]
- ...

If any criterion is not met: go back and implement the missing part.
If any criterion is ambiguous: document your interpretation in the PR description.

### 5. Prepare the PR

- Review your own diff: read every changed line as if you were the reviewer
- Remove any debug code, commented-out code, or TODOs
- Ensure the branch is rebased on the latest base branch
- Write the PR description: what was done, what changed, how to test

## Output Format

```markdown
## Feature Implementation: [ticket ID] — [title]

### Acceptance Criteria Status

| # | Criterion | Status | Verified By |
|---|-----------|--------|------------|
| 1 | [criterion] | [done/partial/blocked] | [test name or manual check] |

### Changes Made

| File | Change Type | Description |
|------|------------|-------------|
| [path] | [new/modified] | [what changed] |

### Tests Added
- [N] unit tests in [test file]
- All [N] existing tests passing

### Commits
1. `feat: [description]`
2. `feat: [description]`
3. `test: [description]`

### PR Ready
- [ ] All acceptance criteria verified
- [ ] All tests passing
- [ ] Self-review completed
- [ ] No debug code or TODOs remaining
- [ ] Branch rebased on latest base
```

## Example Usage

**Input provided**:
- Ticket: ENG-1234 — "Add email notification when certificate expires in 30 days"
- Acceptance criteria:
  1. System sends email to certificate owner 30 days before expiration
  2. Email contains: certificate name, expiration date, renewal link
  3. No duplicate emails for the same certificate
  4. Notification is logged in the audit trail
- Codebase context: Python/FastAPI, existing `notification_service.py` with `send_email()`, SQLAlchemy models, existing cron job framework
- Design constraints: ADR-005 says all notifications go through the notification service

**Execution**:
1. Analyzes: needs changes in 4 files — new cron job, new email template, notification service call, audit log entry. Reads all 4 existing files.
2. Plans 3 increments: (1) add `expiring_certificates` query + cron job, (2) add email template + send via notification service, (3) add dedup check + audit logging
3. Implements:
   - Increment 1: adds `get_certificates_expiring_in(days)` query + `notify_expiring_certificates` cron job scheduled daily
   - Increment 2: adds `certificate_expiry_warning.html` template, calls `notification_service.send_email()` with template data
   - Increment 3: adds `notification_sent` table with unique constraint on `(certificate_id, notification_type)`, writes audit log entry
4. Verifies: criterion 1 (cron job test), criterion 2 (email template test), criterion 3 (dedup constraint test), criterion 4 (audit log test) — all pass
5. Self-reviews diff, removes debug prints, rebases on main

**Output**:
```markdown
## Feature Implementation: ENG-1234 — Add email notification when certificate expires in 30 days

### Acceptance Criteria Status

| # | Criterion | Status | Verified By |
|---|-----------|--------|------------|
| 1 | Email sent 30 days before expiration | Done | test_notify_expiring_certificates |
| 2 | Email contains name, date, renewal link | Done | test_expiry_email_template_content |
| 3 | No duplicate emails | Done | test_duplicate_notification_prevented |
| 4 | Notification logged in audit trail | Done | test_audit_log_created_on_notification |

### Changes Made

| File | Change Type | Description |
|------|------------|-------------|
| src/cron/expiry_notifier.py | New | Daily cron job that queries expiring certificates and sends emails |
| src/templates/certificate_expiry_warning.html | New | Email template with certificate details and renewal link |
| src/models/notification_sent.py | New | Dedup table with unique constraint on (certificate_id, type) |
| src/notification_service.py | Modified | Added `send_expiry_warning()` method |
| src/audit/logger.py | Modified | Added `NOTIFICATION_SENT` event type |

### Tests Added
- 5 unit tests in tests/cron/test_expiry_notifier.py
- 2 unit tests in tests/test_notification_service.py
- All 89 existing tests passing

### Commits
1. `feat: add query for certificates expiring within N days`
2. `feat: add daily cron job for expiry email notifications`
3. `feat: add dedup table and audit logging for expiry notifications`
4. `test: add tests for expiry notification flow`

### PR Ready
- [x] All acceptance criteria verified
- [x] All tests passing
- [x] Self-review completed
- [x] No debug code or TODOs remaining
- [x] Branch rebased on latest main
```
