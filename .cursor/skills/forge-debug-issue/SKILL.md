---
name: forge-debug-issue
description: "Systematic debugging using reproduce-hypothesize-isolate-fix-verify cycle."
user-invocable: true
risk: safe
source: "Forge Kit v2"
---

# Skill: Debug Issue

How to systematically diagnose and fix a bug using a structured reproduce-hypothesize-isolate-fix-verify cycle.

## Purpose

Diagnose and fix a bug through a disciplined, evidence-based process that avoids
random changes and ensures the fix addresses the root cause, not just the symptom.

Use when: a bug is reported, a test is failing unexpectedly, or production behavior
diverges from expected behavior.
Do not use when: the issue is a feature request or a known design limitation.

## Inputs

- **Symptom**: what is happening vs what should happen
- **Reproduction steps**: how to trigger the bug (if known)
- **Environment**: where the bug occurs (local, staging, production, specific OS/browser)
- **Recent changes**: any deployments, config changes, or dependency updates near the time the bug appeared

## Steps

### 1. Reproduce the bug

- Follow the reported reproduction steps exactly
- If no steps are provided: read logs, error messages, and stack traces to identify the trigger
- Confirm the bug is real (not a false positive, stale cache, or environment issue)
- If you cannot reproduce: document what you tried and escalate

**Output**: a reliable way to trigger the bug on demand

### 2. Formulate hypotheses

Based on the symptom and reproduction, list 2-5 possible root causes:
- Read the code in the area where the bug manifests
- Check recent commits that touched the relevant files (`git log --oneline -20 -- <file>`)
- Consider: wrong logic, missing edge case, race condition, data corruption, dependency bug, config error

Rank hypotheses by probability (most likely first).

### 3. Isolate the root cause

For each hypothesis, starting with the most likely:
- Design a test that would confirm or refute the hypothesis
- Run the test — observe the result
- If confirmed: you found the root cause. Move to Step 4.
- If refuted: cross it off and move to the next hypothesis

Techniques:
- **Binary search**: if the bug is in a large file, comment out half the code to narrow down
- **Git bisect**: if the bug was recently introduced, use `git bisect` to find the exact commit
- **Logging**: add temporary debug logs at key decision points to trace execution flow
- **Minimal reproduction**: strip away unrelated code until you have the smallest case that triggers the bug

### 4. Write a failing test

Before fixing anything:
- Write a test that reproduces the bug (it must fail on the current code)
- This test is your proof that the bug exists and your guard against regression
- If the bug is in production but not caught by tests, this is the gap to fill

### 5. Implement the fix

- Make the minimal change that fixes the root cause
- Do NOT fix unrelated issues in the same change — one fix per bug
- Do NOT add defensive code that masks the symptom without addressing the cause
- Run the failing test — it must now pass
- Run all existing tests — none must break

### 6. Verify the fix

- Reproduce the original bug using Step 1 — it must no longer occur
- Check for related edge cases: does the fix cover similar inputs?
- If the bug was in production: verify the fix in a staging environment before deploying
- Document what caused the bug and how it was fixed (in the commit message or ticket)

## Output Format

```markdown
## Bug Fix: [short description]

### Symptom
[What was observed vs what was expected]

### Root Cause
[The actual cause — not the symptom]

### Investigation Path
1. [Hypothesis 1] — [confirmed/refuted] — [evidence]
2. [Hypothesis 2] — [confirmed/refuted] — [evidence]

### Fix Applied
- File: [path]
- Change: [what was changed and why]

### Tests
- [N] regression test(s) added
- All existing tests passing

### Verification
- [How the fix was verified]
```

## Example Usage

**Input provided**:
- Symptom: users report "certificate not found" error after successful issuance — but only intermittently
- Reproduction steps: unknown — happens ~10% of the time
- Environment: production only, not reproducible locally
- Recent changes: deployed v2.3.1 two days ago with database connection pool changes

**Execution**:
1. Cannot reproduce locally → checks production logs → finds the error happens when two requests for the same user arrive within 100ms
2. Hypotheses: (1) race condition on DB write — two requests create duplicate, second fails unique constraint and rolls back; (2) read-after-write hitting a read replica with replication lag; (3) connection pool exhaustion causing timeout
3. Isolates: checks DB query routing → confirms reads go to replica, writes to primary. Measures replication lag: 50-200ms. Hypothesis 2 confirmed — read-after-write reads stale data from replica
4. Writes test: `test_read_after_write_uses_primary_connection` — forces read immediately after write, asserts it hits primary
5. Fix: routes read-after-write queries to primary for 500ms after write using session-level routing hint
6. Verifies: test passes; deploys to staging; runs 1000 concurrent issuance requests; zero "not found" errors

**Output**:
```markdown
## Bug Fix: Intermittent "certificate not found" after issuance

### Symptom
~10% of users see "certificate not found" immediately after successful issuance.
Expected: certificate is always available immediately after issuance confirms.

### Root Cause
Read-after-write queries were routed to a database read replica with 50-200ms
replication lag. When a user's subsequent read arrived before replication
completed, the replica returned no results.

### Investigation Path
1. Race condition on DB write (duplicate constraint) — refuted — no constraint violation errors in logs
2. Read replica replication lag — confirmed — replica lag measured at 50-200ms, matching the failure window
3. Connection pool exhaustion — refuted — pool metrics show 30% utilization at peak

### Fix Applied
- File: src/db/routing.py
- Change: added session-level routing hint that directs read queries to the primary
  connection for 500ms after any write operation in the same session

### Tests
- 1 regression test added: `test_read_after_write_uses_primary_connection`
- All 47 existing tests passing

### Verification
- Deployed to staging, ran 1000 concurrent issuance+read sequences
- Zero "certificate not found" errors (previously ~100 out of 1000)
```
