---
name: forge-write-unit-tests
description: "Write effective unit tests using the AAA pattern with behavior matrix for comprehensive coverage."
user-invocable: true
risk: safe
source: "Forge Kit v2"
---

# Skill: Write Unit Tests

How to write effective unit tests that protect against regressions and document intended behavior.

## Purpose

Write unit tests for new or existing code that verify behavior, catch regressions,
and serve as living documentation of how the code works.

Use when: implementing a new feature that needs tests, adding tests to untested code,
or reproducing a bug before fixing it.
Do not use when: writing integration or E2E tests (those require different setup and scope).

## Inputs

- **Target**: file path and function/class to test
- **Behavior to verify**: what the code should do (from ticket, acceptance criteria, or reading the code)
- **Existing tests**: are there tests already? What framework is in use?
- **Edge cases**: any known edge cases or error conditions to cover

## Steps

### 1. Read and understand the code under test

- Read the full file, not just the target function
- Identify: inputs, outputs, side effects, error paths
- Map dependencies: what does the function call? What can fail?
- Check existing tests to understand the project's test patterns and framework

### 2. List test cases using the behavior matrix

For each function, enumerate:
- **Happy path**: the normal, expected input → expected output
- **Boundary values**: empty input, zero, max value, single element, exactly-at-limit
- **Error cases**: invalid input, null/undefined, missing required fields
- **State transitions**: if the function changes state, verify before and after
- **Side effects**: if it writes to DB, calls an API, or emits events — verify those

### 3. Write tests following the AAA pattern

Each test follows **Arrange → Act → Assert**:
- **Arrange**: set up the data and dependencies (mocks, fixtures, test data)
- **Act**: call the function under test with the arranged inputs
- **Assert**: verify the output and/or side effects match expectations

Rules:
- One behavior per test — do not test multiple things in one test
- Name tests by what they verify: `test_returns_error_when_card_is_expired`
- Prefer real objects over mocks when practical
- Mock only external boundaries (APIs, databases, file system)

### 4. Handle dependencies

- **Pure functions**: no mocking needed — pass inputs, check outputs
- **Functions with I/O**: mock the I/O boundary, test the logic
- **Functions with state**: reset state before each test (setUp/beforeEach)
- **Functions calling other internal functions**: do NOT mock internal functions — test through the public API

### 5. Verify test quality

- Run all tests — they must pass
- Temporarily break the code under test — at least one test must fail
- Check that test names describe the behavior, not the implementation
- Ensure no test depends on execution order or shared mutable state

## Output Format

```markdown
## Unit Tests: [function/class name] in [file]

### Test Plan

| # | Test Case | Category | Input | Expected Output |
|---|-----------|----------|-------|----------------|
| 1 | [description] | [happy/boundary/error/side-effect] | [input] | [output] |

### Tests Written
- File: [test file path]
- Framework: [pytest/jest/junit/etc.]
- [N] tests added, all passing

### Coverage Notes
- [what is covered]
- [what is NOT covered and why]
```

## Example Usage

**Input provided**:
- Target: `src/auth/token_service.py`, function `generate_access_token(user, scopes)`
- Behavior: generates a JWT with user ID, email, and requested scopes; expires in 15 minutes; raises `InvalidScopeError` if scope is not in allowed list
- Existing tests: `tests/auth/test_token_service.py` exists with 2 tests (happy path only), using pytest
- Edge cases: empty scopes list, user with no email, expired token validation

**Execution**:
1. Reads `token_service.py` — function takes `User` dataclass and `list[str]` scopes, calls `jwt.encode()`, checks scopes against `ALLOWED_SCOPES` set
2. Lists 7 test cases: valid token, single scope, all scopes, empty scopes, invalid scope, user without email, token expiry time
3. Writes tests in AAA pattern — mocks `datetime.utcnow()` for expiry test, uses real `jwt.decode()` to verify token content
4. Does NOT mock `jwt.encode()` — it's a pure function call, testing through the real encoder catches encoding bugs
5. Runs tests — all 7 pass; breaks `generate_access_token` by removing expiry — `test_token_expires_in_15_minutes` fails as expected

**Output**:
```markdown
## Unit Tests: generate_access_token in src/auth/token_service.py

### Test Plan

| # | Test Case | Category | Input | Expected Output |
|---|-----------|----------|-------|----------------|
| 1 | Generates valid JWT with user ID and email | Happy path | valid user, ["read"] | JWT with correct claims |
| 2 | Includes all requested scopes in token | Happy path | valid user, ["read", "write"] | JWT with both scopes |
| 3 | Raises InvalidScopeError for unknown scope | Error | valid user, ["admin"] | InvalidScopeError |
| 4 | Handles empty scopes list | Boundary | valid user, [] | JWT with empty scopes claim |
| 5 | Works when user has no email | Boundary | user(email=None), ["read"] | JWT with email=null |
| 6 | Token expires in exactly 15 minutes | Side-effect | valid user, ["read"] | exp = now + 15min |
| 7 | Raises TypeError for None user | Error | None, ["read"] | TypeError |

### Tests Written
- File: tests/auth/test_token_service.py
- Framework: pytest
- 7 tests added (5 new + 2 existing), all passing

### Coverage Notes
- Covered: all input combinations, expiry, scope validation, missing fields
- NOT covered: token refresh flow (separate function, separate test file)
```
