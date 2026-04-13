---
name: forge-write-runbook
description: "Operational runbook creation covering deploy, monitor, and troubleshoot procedures."
user-invocable: true
risk: safe
source: "Forge Kit v2"
---

# Skill: Write Runbook

How to write an operational runbook that enables anyone on-call to deploy, monitor, and troubleshoot a service.

## Purpose

Create a runbook that provides step-by-step operational procedures for a service or system.
A good runbook enables any engineer on-call to handle routine operations and common incidents
without needing to understand the full codebase.

Use when: deploying a new service, onboarding a team to on-call for a service, or after
an incident reveals missing operational documentation.
Do not use when: documenting architecture decisions (use `write-adr`) or code patterns (use inline docs).

## Inputs

- **Service name**: the service or system this runbook covers
- **Environment**: where the service runs (AWS, GCP, on-prem, Kubernetes, etc.)
- **Key operations**: what operations need documentation (deploy, rollback, scale, restart)
- **Common failure modes**: known issues that on-call engineers encounter
- **Monitoring**: what dashboards, alerts, and metrics exist

## Steps

### 1. Document the service overview

- What does this service do in one sentence?
- Who owns it? (team, Slack channel, escalation path)
- What are its dependencies? (databases, queues, other services, external APIs)
- Where does it run? (region, cluster, namespace)
- How is it deployed? (CI/CD pipeline, manual, GitOps)

### 2. Document standard operating procedures

For each key operation (deploy, rollback, scale, restart):
- List the exact commands or steps (copy-paste ready)
- Include pre-conditions: what to check before running
- Include verification: how to confirm the operation succeeded
- Include rollback: how to undo if something goes wrong

Rules:
- Every command must be copy-paste ready — no placeholders without a legend
- Include expected output for each command so the operator knows what "success" looks like
- Time estimates: how long each operation takes under normal conditions

### 3. Document monitoring and alerting

- List all dashboards with URLs
- List all alerts with: name, threshold, meaning, and response action
- Document what "healthy" looks like: key metrics and their normal ranges
- Document SLAs/SLOs: what are the targets? What triggers a page?

### 4. Document troubleshooting procedures

For each known failure mode:
- **Symptom**: what the on-call engineer sees (alert name, error message, user report)
- **Diagnosis**: how to confirm this is the issue (commands to run, logs to check)
- **Resolution**: step-by-step fix (copy-paste commands)
- **Escalation**: when to escalate and to whom

### 5. Document access and credentials

- What access is needed to operate this service? (AWS role, K8s namespace, VPN)
- How to get access if you don't have it (link to access request process)
- Where are secrets stored? (vault path, SSM parameter store, etc.)
- Do NOT include actual credentials in the runbook

## Output Format

```markdown
# Runbook: [service name]

**Owner**: [team name] | **Slack**: [channel] | **Escalation**: [path]
**Last updated**: [date]

## Service Overview
[One-sentence description]

### Architecture
- **Runtime**: [where it runs]
- **Dependencies**: [list]
- **Data stores**: [databases, caches]

## Standard Operations

### Deploy
**Pre-conditions**: [what to verify]
```bash
[copy-paste commands]
```
**Verification**: [how to confirm success]
**Rollback**: [how to undo]
**Duration**: [expected time]

### [Other operations...]

## Monitoring

### Dashboards
| Dashboard | URL | What to look for |
|-----------|-----|-----------------|

### Alerts
| Alert | Threshold | Meaning | Response |
|-------|-----------|---------|----------|

### Healthy State
[Key metrics and normal ranges]

## Troubleshooting

### [Symptom: alert/error name]
**Diagnosis**: [commands to check]
**Resolution**: [commands to fix]
**Escalation**: [when and to whom]

## Access
| Resource | How to Access | Request Process |
|----------|--------------|----------------|
```

## Example Usage

**Input provided**:
- Service: `certificate-issuance-service`
- Environment: AWS ECS Fargate, us-east-1, PostgreSQL RDS, Redis ElastiCache
- Key operations: deploy via GitHub Actions, rollback to previous task definition, scale ECS tasks
- Common failures: HSM timeout during peak, Redis connection pool exhaustion, RDS connection limit
- Monitoring: Grafana dashboard, CloudWatch alarms, PagerDuty integration

**Execution**:
1. Documents overview: certificate issuance service owned by Platform team, depends on HSM, PostgreSQL, Redis, notification-service
2. Documents operations: deploy (GH Actions workflow dispatch), rollback (ECS update-service with previous task def), scale (update desired count)
3. Documents monitoring: 3 Grafana dashboards, 5 CloudWatch alarms with thresholds and response actions
4. Documents troubleshooting: HSM timeout (check HSM health endpoint, circuit breaker state), Redis pool exhaustion (check connection count, restart with increased pool), RDS connection limit (check active connections, kill idle)
5. Documents access: AWS SSO role, ECS exec access, Grafana team membership

**Output**:
```markdown
# Runbook: certificate-issuance-service

**Owner**: Platform Team | **Slack**: #platform-oncall | **Escalation**: Platform TL → CTO
**Last updated**: 2025-01-15

## Service Overview
Issues and manages digital certificates for Certisign customers.

### Architecture
- **Runtime**: AWS ECS Fargate, us-east-1, cluster `prod-platform`
- **Dependencies**: HSM (signing), notification-service (email)
- **Data stores**: PostgreSQL RDS (audit + metadata), Redis ElastiCache (certificate status cache)

## Standard Operations

### Deploy
**Pre-conditions**: all tests green on `main`, no active incidents
```bash
# Trigger deploy via GitHub Actions
gh workflow run deploy.yml --ref main -f environment=production

# Monitor deploy progress
gh run list --workflow=deploy.yml --limit 1 --json status,conclusion
```
**Verification**: check ECS service events for `has reached a steady state`
```bash
aws ecs describe-services --cluster prod-platform --services cert-issuance \
  --query 'services[0].events[:3]'
```
**Rollback**: see Rollback section below
**Duration**: ~5 minutes

### Rollback
```bash
# Get previous task definition
PREV_TASK=$(aws ecs describe-services --cluster prod-platform \
  --services cert-issuance \
  --query 'services[0].taskDefinition' --output text | \
  sed 's/:[0-9]*$/:/' | \
  awk -F: '{print $0 ($NF-1)}')

# Roll back
aws ecs update-service --cluster prod-platform --service cert-issuance \
  --task-definition $PREV_TASK
```
**Duration**: ~3 minutes

### Scale
```bash
# Scale to N tasks
aws ecs update-service --cluster prod-platform --service cert-issuance \
  --desired-count <N>
```
**Normal count**: 3 tasks. **Peak hours (9-18 BRT)**: 6 tasks.

## Monitoring

### Dashboards
| Dashboard | URL | What to look for |
|-----------|-----|-----------------|
| Service Health | grafana.internal/d/cert-issuance | P99 latency < 2s, error rate < 0.1% |
| HSM Operations | grafana.internal/d/hsm-ops | Signing latency < 1s, queue depth < 100 |
| Database | grafana.internal/d/rds-platform | Active connections < 80%, replication lag < 100ms |

### Alerts
| Alert | Threshold | Meaning | Response |
|-------|-----------|---------|----------|
| HighP99Latency | P99 > 2s for 5min | SLA breach risk | Check HSM latency, scale tasks |
| HighErrorRate | >1% 5xx for 5min | Service degradation | Check logs, recent deploy |
| HSMTimeout | >5 timeouts/min | HSM overloaded | Check HSM dashboard, enable circuit breaker |
| RDSConnectionHigh | >80% pool used | Connection exhaustion risk | Kill idle connections, check for leaks |
| RedisConnectionHigh | >90% pool used | Cache failure risk | Restart service with increased pool size |

### Healthy State
- P99 latency: 800ms - 1.5s
- Error rate: < 0.05%
- ECS task count: 3-6 (auto-scales)
- RDS active connections: 20-40
- Redis memory usage: < 60%

## Troubleshooting

### HSM Timeout Alert
**Diagnosis**:
```bash
# Check HSM health
curl -s https://hsm.internal/health | jq .

# Check circuit breaker state
aws ecs exec --cluster prod-platform --task <task-id> \
  --command "curl -s localhost:8080/internal/circuit-breakers"
```
**Resolution**: if HSM is healthy but slow, enable circuit breaker to queue requests:
```bash
# Temporarily increase timeout
aws ssm put-parameter --name /cert-issuance/hsm-timeout-ms --value 3000 --overwrite
# Restart tasks to pick up new config
aws ecs update-service --cluster prod-platform --service cert-issuance --force-new-deployment
```
**Escalation**: if HSM health returns unhealthy → escalate to HSM vendor (see PagerDuty runbook)

### Redis Connection Pool Exhaustion
**Diagnosis**:
```bash
# Check Redis connections
redis-cli -h cert-cache.xxxxx.cache.amazonaws.com info clients | grep connected
```
**Resolution**:
```bash
# Kill idle connections older than 5 minutes
redis-cli -h cert-cache.xxxxx.cache.amazonaws.com client kill idle 300
# If persistent: redeploy with increased pool size
aws ssm put-parameter --name /cert-issuance/redis-pool-size --value 50 --overwrite
aws ecs update-service --cluster prod-platform --service cert-issuance --force-new-deployment
```
**Escalation**: if connections keep growing after fix → likely connection leak, escalate to Platform team

## Access
| Resource | How to Access | Request Process |
|----------|--------------|----------------|
| AWS Console (prod) | SSO → Platform-Engineer role | IT ticket → manager approval |
| ECS Exec | Requires `ecs:ExecuteCommand` permission | Included in Platform-Engineer role |
| Grafana | Team membership: platform-team | Self-service via Grafana UI |
| PagerDuty | Platform on-call rotation | Platform TL adds you to rotation |
| Redis CLI | Via bastion host (ssm-bastion) | SSH access via SSO |
```
