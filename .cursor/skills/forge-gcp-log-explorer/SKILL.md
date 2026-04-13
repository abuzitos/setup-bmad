---
name: forge-gcp-log-explorer
description: "Exploratory analysis of GKE container logs via gcloud CLI — discovers containers, log patterns, error rates, and anomalies across GCP projects."
user-invocable: true
risk: safe
source: "Forge Kit v2"
---

# GCP Log Explorer

Perform exploratory analysis of GKE container logs to understand what data is available, identify patterns, classify errors, and detect anomalies.

## When to Use

- Starting work on a new GCP project and need to understand what's being logged
- Investigating production issues across multiple GKE clusters
- Building observability baselines for containers

## Workflow

### Step 1: Discover Containers

Query a sample of logs to identify all active containers in a project/namespace:

```bash
gcloud logging read \
  'resource.type="k8s_container" AND resource.labels.namespace_name="NAMESPACE"' \
  --project=PROJECT_ID \
  --limit=500 \
  --freshness=1d \
  --format=json > /tmp/discover_logs.json
```

Extract unique containers with Python:

```python
import json
data = json.load(open('/tmp/discover_logs.json'))
containers = set()
for entry in data:
    name = entry.get('resource', {}).get('labels', {}).get('container_name', 'unknown')
    containers.add(name)
for c in sorted(containers):
    print(c)
```

### Step 2: Sample Each Container

For each discovered container, pull a focused sample:

```bash
gcloud logging read \
  'resource.type="k8s_container" AND resource.labels.container_name="CONTAINER_NAME" AND resource.labels.namespace_name="NAMESPACE"' \
  --project=PROJECT_ID \
  --limit=200 \
  --freshness=2d \
  --format=json > /tmp/sample_CONTAINER.json
```

### Step 3: Classify Log Content

Parse each sample to identify:

1. **Log format** — JSON structured? Plain text? Spring Boot with ANSI codes?
2. **Severity distribution** — How many DEBUG/INFO/WARNING/ERROR?
3. **Recurring patterns** — Regex-extract class names, method names, status codes
4. **Error signatures** — Stack traces, exception types, error messages
5. **Business events** — Authentication, transactions, API calls to external services

**ANSI color stripping** (Java Spring Boot logs):
```python
import re
clean = re.sub(r'\x1b\[[0-9;]*m|\[[\d;]*m', '', raw_text)
```

### Step 4: Cross-Container Analysis

Compare findings across containers:
- Which containers log errors vs just info?
- Which containers have HTTP access logs (check `httpRequest` field)?
- Are there correlated events across containers (same request flowing through)?

### Step 5: Report

Produce a summary per container:

| Container | Log Format | Volume (est/day) | Error Rate | Key Patterns |
|-----------|-----------|-------------------|------------|-------------|
| name | format | count | % | patterns found |

## Tips

- **Log volume**: Production containers can produce thousands of entries per hour. Always use `--limit` and narrow time windows.
- **Multi-project**: GCP doesn't support multi-project queries. Run separate calls and merge results in Python.
- **Week-by-week pagination**: For 30-day analysis, split into weekly queries of 5000 each to avoid ceiling.
- **textPayload vs jsonPayload**: Check which field contains the actual log content — varies by container configuration.
- **httpRequest field**: Present in ingress/load balancer logs, absent from application stdout logs.

## Permissions Required

- `roles/logging.viewer` (minimum) on target project(s)
