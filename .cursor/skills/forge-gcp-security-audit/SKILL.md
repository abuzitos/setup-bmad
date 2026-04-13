---
name: forge-gcp-security-audit
description: "Analyze GKE logs for attack patterns, exploit attempts, and security anomalies — classifies attacks and validates exposure risk."
user-invocable: true
risk: safe
source: "Forge Kit v2"
---

# GCP Security Audit

Detect and classify attack attempts against GKE-hosted applications by analyzing HTTP access logs and application logs.

## When to Use

- Periodic security review of public-facing endpoints
- Investigating a suspected breach or spike in suspicious traffic
- Building a security posture report for stakeholders

## Workflow

### Step 1: Collect HTTP Access Logs

```bash
gcloud logging read \
  'resource.type="k8s_container" AND resource.labels.namespace_name="NAMESPACE" AND httpRequest.requestUrl!=""' \
  --project=PROJECT_ID \
  --limit=5000 \
  --freshness=7d \
  --format=json > /tmp/http_logs.json
```

Also collect application-level exception logs:

```bash
gcloud logging read \
  'resource.type="k8s_container" AND resource.labels.namespace_name="NAMESPACE" AND severity>="WARNING"' \
  --project=PROJECT_ID \
  --limit=5000 \
  --freshness=7d \
  --format=json > /tmp/error_logs.json
```

### Step 2: Classify Attack Patterns

Apply regex-based classification to HTTP request URLs and payloads. Known categories:

| Category | Pattern Examples |
|----------|----------------|
| **ThinkPHP RCE** | `/index.php?s=/module/action/param` with `invokefunction` |
| **PearCMD RCE** | `pearcmd.php`, `register-argv-argc` |
| **Path Traversal** | `../`, `..%2f`, `..\\` in URLs |
| **Git Exposure** | `/.git/config`, `/.git/HEAD`, `/.gitignore` |
| **Env File Exposure** | `/.env`, `/.env.local`, `/.env.production` |
| **PHP Info Disclosure** | `/phpinfo.php`, `/info.php` |
| **Spring Actuator** | `/actuator/env`, `/actuator/health`, `/actuator/gateway/routes` |
| **WordPress Probing** | `/wp-admin/`, `/wp-login.php`, `/wp-content/`, `/xmlrpc.php` |
| **SQL Injection** | `UNION SELECT`, `' OR 1=1`, `; DROP TABLE` |
| **Shell Injection** | `/shell`, `/eval-stdin`, `/cgi-bin/` |
| **Scanner Fingerprints** | User-Agent containing `Nuclei`, `zgrab`, `Nmap`, `sqlmap` |

Python classification example:

```python
import re

ATTACK_PATTERNS = {
    'ThinkPHP RCE': r'think[Pp]hp|invokefunction|s=/index',
    'Path Traversal': r'\.\./|\.\.%2[fF]|\.\.\\\\',
    'Git Exposure': r'/\.git/(config|HEAD|objects|refs)',
    'Env Exposure': r'/\.env(\.|$)',
    'PHP Info': r'phpinfo|info\.php',
    'Spring Actuator': r'/actuator/(env|health|gateway|beans|configprops)',
    'WordPress Probe': r'/wp-(admin|login|content|includes)|/xmlrpc\.php',
    'Shell/RCE': r'/shell|/eval-stdin|/cgi-bin/',
    'Scanner': r'[Nn]uclei|zgrab|[Nn]map|sqlmap',
}

def classify(url, user_agent=''):
    combined = f"{url} {user_agent}"
    for name, pattern in ATTACK_PATTERNS.items():
        if re.search(pattern, combined):
            return name
    return None
```

### Step 3: Validate Exposure

For routes returning HTTP 200, check if it's a real exposure or a false positive:

- **SPA catch-all**: Single Page Applications serve `index.html` for ANY route (including `/.env`, `/.git/config`). Check the `Content-Type` header or response size — if it's HTML with the same size across all routes, it's a catch-all.
- **Real exposure**: Response contains actual sensitive data (different content per path, non-HTML content-type for config files).

```bash
# Check if multiple suspicious routes return identical responses
gcloud logging read \
  'resource.type="k8s_container" AND resource.labels.container_name="CONTAINER" AND httpRequest.requestUrl=~"\.env|\.git"' \
  --project=PROJECT_ID \
  --limit=50 \
  --format=json
```

Compare `httpRequest.responseSize` across different attack URLs — identical sizes = SPA catch-all.

### Step 4: Generate Report

Structure the report as:

```
## Attack Summary (Period: DATE_START — DATE_END)

### Volume
- Total suspicious requests: N
- Unique source IPs: N
- Unique attack types: N

### By Category
| Attack Type | Count | % of Total | Risk |
|-------------|-------|-----------|------|
| type | n | % | NONE/LOW/MED/HIGH |

### Risk Assessment
- Real exposures found: list
- False positives (SPA catch-all): list
- Recommendations: list

### Top Attacker IPs (if available)
| IP | Requests | Attack Types |
```

## Tips

- **SPA false positives are common**: Always validate before reporting HTTP 200 on sensitive paths as an exposure.
- **Automated scanners**: Nuclei and zgrab generate bulk traffic — high volume doesn't mean targeted attack.
- **ClearSale/DEBUG credentials**: Some applications log third-party API credentials at DEBUG level. Flag these as a security concern even if they're not attack-related.
- **Rate of attacks**: Track week-over-week changes. A sudden spike may indicate targeted scanning.

## Permissions Required

- `roles/logging.viewer` on target project(s)
