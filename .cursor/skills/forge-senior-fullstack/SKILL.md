---
name: forge-senior-fullstack
description: "Code quality analysis toolkit. Structured codebase assessment with complexity, dependency, and test gap analysis."
user-invocable: true
risk: safe
source: "Forge Kit v2"
---

# Skill: Code Quality Analysis

How to assess and improve the quality of a codebase or module through structured analysis.

## Purpose

Perform a structured quality assessment of a codebase, module, or project area.
Identifies concrete issues and produces prioritized, actionable recommendations.

Use when: starting work on an unfamiliar codebase, preparing for a major refactor,
onboarding to a new project, or auditing code quality before a release.
Do not use when: reviewing a single PR (use `review-pr`) or refactoring a single function (use `refactor-function`).

## Inputs

- **Scope**: directory, module, or service to analyze
- **Focus area**: what to prioritize (maintainability, testability, performance, security, or all)
- **Language/framework**: primary tech stack of the codebase
- **Known concerns**: any existing issues the team is aware of (optional)

## Steps

### 1. Survey the codebase structure

- Map the directory layout: how is code organized?
- Identify entry points: where do requests/commands enter the system?
- Count files by type: source, tests, config, docs
- Check test-to-source ratio: are there tests at all? What percentage of modules have tests?

### 2. Analyze code complexity

For each module or key file:
- **Function length**: flag functions over 40 lines
- **File length**: flag files over 300 lines
- **Nesting depth**: flag code with more than 3 levels of nesting
- **Parameter count**: flag functions with more than 4 parameters
- **Cyclomatic complexity**: identify functions with many branching paths

### 3. Check dependency health

- List direct dependencies and their versions
- Flag dependencies with known vulnerabilities (if lockfile available)
- Identify unused dependencies (imported but never referenced)
- Check for circular dependencies between modules

### 4. Evaluate test coverage

- Run existing tests and note pass/fail ratio
- Identify untested modules (no corresponding test file)
- Check test quality: are tests testing behavior or implementation details?
- Flag brittle tests: tests that mock too many internals

### 5. Assess code patterns

- **Naming**: are names consistent and descriptive?
- **Error handling**: are errors caught and handled, or silently ignored?
- **Duplication**: are there copy-pasted blocks that should be extracted?
- **Separation of concerns**: is business logic mixed with I/O or presentation?
- **Configuration**: are secrets or environment-specific values hardcoded?

### 6. Produce prioritized recommendations

For each finding:
- State the issue with a specific file and line reference
- Explain the impact (bugs, maintenance burden, security risk)
- Suggest a concrete fix
- Estimate effort: small (< 1 hour), medium (< 1 day), large (> 1 day)

## Output Format

```markdown
## Code Quality Analysis: [scope]

### Overview
- **Language/Framework**: [tech stack]
- **Files analyzed**: [N] source / [N] test / [N] config
- **Test-to-source ratio**: [X:Y]
- **Overall assessment**: [healthy / needs attention / critical]

### Complexity Hotspots

| File | Function | Lines | Issue |
|------|----------|-------|-------|
| [path] | [name] | [N] | [long function / deep nesting / too many params] |

### Dependency Issues

| Dependency | Issue | Severity |
|-----------|-------|----------|
| [name@version] | [vulnerability / unused / outdated] | [high/medium/low] |

### Test Gaps

| Module | Has Tests? | Coverage Notes |
|--------|-----------|---------------|
| [path] | [yes/no/partial] | [what's missing] |

### Code Pattern Issues

| # | Issue | File:Line | Impact | Fix Effort |
|---|-------|-----------|--------|-----------|
| 1 | [issue] | [file:line] | [impact] | [S/M/L] |

### Recommendations (Prioritized)

| Priority | Recommendation | Effort | Impact |
|----------|---------------|--------|--------|
| 1 | [concrete action] | [S/M/L] | [what it fixes] |
```

## Example Usage

**Input provided**:
- Scope: `src/billing/` module of a SaaS platform
- Focus area: maintainability and testability
- Language/framework: Python 3.11, FastAPI, SQLAlchemy
- Known concerns: "the billing module is hard to modify — changes often break things"

**Execution**:
1. Surveys structure: 12 source files, 3 test files, 0 config files. Test ratio: 1:4
2. Complexity: `invoice_generator.py:generate_invoice()` is 120 lines with 6 levels of nesting; `payment_processor.py` has 4 functions with 5+ parameters each
3. Dependencies: `stripe` pinned to 2-year-old version with 2 known CVEs; `pydantic` v1 while rest of project uses v2
4. Tests: only happy-path tests; no error scenarios; `subscription_manager.py` has zero tests
5. Patterns: SQL queries built by string concatenation in `reports.py`; business logic mixed with HTTP handlers in `routes.py`; Stripe API key hardcoded in `payment_processor.py`
6. Produces 6 prioritized recommendations

**Output**:
```markdown
## Code Quality Analysis: src/billing/

### Overview
- **Language/Framework**: Python 3.11, FastAPI, SQLAlchemy
- **Files analyzed**: 12 source / 3 test / 0 config
- **Test-to-source ratio**: 1:4
- **Overall assessment**: needs attention

### Complexity Hotspots

| File | Function | Lines | Issue |
|------|----------|-------|-------|
| invoice_generator.py | generate_invoice | 120 | 6 nesting levels, multiple responsibilities |
| payment_processor.py | process_payment | 65 | 5 parameters, mixed I/O and logic |
| payment_processor.py | handle_refund | 58 | 5 parameters, duplicated validation |

### Dependency Issues

| Dependency | Issue | Severity |
|-----------|-------|----------|
| stripe@4.2.0 | 2 known CVEs (CVE-2023-XXXX, CVE-2024-XXXX) | High |
| pydantic@1.10 | v1 while project uses v2 elsewhere | Medium |

### Test Gaps

| Module | Has Tests? | Coverage Notes |
|--------|-----------|---------------|
| invoice_generator.py | Yes | Only happy path — no error cases |
| subscription_manager.py | No | Zero tests for subscription lifecycle |
| reports.py | No | SQL query generation untested |

### Code Pattern Issues

| # | Issue | File:Line | Impact | Fix Effort |
|---|-------|-----------|--------|-----------|
| 1 | SQL by string concatenation | reports.py:34 | SQL injection risk | S |
| 2 | Stripe API key hardcoded | payment_processor.py:12 | Secret in source code | S |
| 3 | Business logic in HTTP handler | routes.py:45-120 | Untestable without HTTP context | M |
| 4 | Duplicated validation in 3 functions | payment_processor.py | Bug risk on divergence | M |

### Recommendations (Prioritized)

| Priority | Recommendation | Effort | Impact |
|----------|---------------|--------|--------|
| 1 | Move Stripe API key to environment variable | S | Eliminates hardcoded secret |
| 2 | Replace string concatenation with parameterized queries in reports.py | S | Eliminates SQL injection risk |
| 3 | Upgrade stripe to latest (fix CVEs) | M | Resolves 2 known vulnerabilities |
| 4 | Extract business logic from routes.py into service layer | M | Enables unit testing without HTTP |
| 5 | Add tests for subscription_manager.py lifecycle | M | Covers critical untested module |
| 6 | Refactor generate_invoice into smaller functions | L | Reduces 120-line function to testable units |
```
