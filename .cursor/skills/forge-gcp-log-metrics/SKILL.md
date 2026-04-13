---
name: forge-gcp-log-metrics
description: "Create GCP Log-based Metrics and Cloud Monitoring dashboards from analyzed log patterns — turns log analysis into persistent observability."
user-invocable: true
risk: safe
source: "Forge Kit v2"
---

# GCP Log-based Metrics & Dashboards

Convert log analysis findings into persistent, queryable metrics and visual dashboards in GCP Cloud Monitoring.

## When to Use

- After completing a log exploration and wanting to make findings permanent
- Setting up ongoing monitoring for a GKE application
- Building operational dashboards for stakeholders

## Workflow

### Step 1: Define Metrics from Log Patterns

For each pattern you want to track, create a log-based metric. Common categories:

| Category | Example Filter |
|----------|---------------|
| **Auth success** | `textPayload=~"Authorization requested for .* was succeed"` |
| **Auth failure** | `textPayload=~"Authorization requested for .* was failed"` |
| **API requests** | `textPayload=~"ClassName"` (adapt to your log class) |
| **Errors** | `severity="ERROR"` |
| **Attack attempts** | `httpRequest.requestUrl=~"\.php\|\.env\|actuator\|\.git"` |

### Step 2: Create Metrics via gcloud

```bash
gcloud logging metrics create METRIC_NAME \
  --project=PROJECT_ID \
  --description="Human-readable description" \
  --log-filter='resource.type="k8s_container" AND resource.labels.container_name="CONTAINER" AND resource.labels.namespace_name="NAMESPACE" AND PATTERN_FILTER'
```

**Naming convention**: Use prefix `bio-` or `app-` followed by category: `bio-auth-success`, `bio-core-errors`, `bio-attack-attempts`.

**Important**: Metrics only count logs **from creation time forward** — they do not backfill historical data.

### Step 3: Verify Metrics

```bash
# List all custom metrics in a project
gcloud logging metrics list --project=PROJECT_ID --format="table(name,description,filter)"

# Delete a metric if needed
gcloud logging metrics delete METRIC_NAME --project=PROJECT_ID
```

### Step 4: Build Dashboard JSON

Cloud Monitoring dashboards are defined as JSON. Structure:

```json
{
  "displayName": "Dashboard Name",
  "mosaicLayout": {
    "columns": 12,
    "tiles": [
      {
        "xPos": 0, "yPos": 0, "width": 6, "height": 4,
        "widget": {
          "title": "Widget Title",
          "xyChart": {
            "dataSets": [{
              "timeSeriesQuery": {
                "timeSeriesFilter": {
                  "filter": "metric.type=\"logging.googleapis.com/user/METRIC_NAME\" AND resource.type=\"k8s_container\"",
                  "aggregation": {
                    "alignmentPeriod": "3600s",
                    "perSeriesAligner": "ALIGN_RATE",
                    "crossSeriesReducer": "REDUCE_SUM"
                  }
                }
              },
              "plotType": "LINE"
            }]
          }
        }
      }
    ]
  }
}
```

**Widget types**:
- `xyChart` with `plotType: "LINE"` — time series line chart
- `xyChart` with `plotType: "STACKED_BAR"` — bar chart
- `scorecard` — single-value KPI with optional thresholds

**Scorecard with thresholds** (alerts at visual level):
```json
{
  "title": "Failures (24h)",
  "scorecard": {
    "timeSeriesQuery": { ... },
    "thresholds": [
      { "label": "Warning", "value": 50, "color": "YELLOW", "direction": "ABOVE" },
      { "label": "Critical", "value": 200, "color": "RED", "direction": "ABOVE" }
    ]
  }
}
```

### Step 5: Deploy Dashboard

```bash
# Via gcloud (requires roles/monitoring.dashboardEditor)
gcloud monitoring dashboards create \
  --config-from-file=dashboard.json \
  --project=PROJECT_ID

# If no CLI permission, import manually:
# Cloud Console → Monitoring → Dashboards → Create → JSON Editor → paste JSON
```

### Step 6: Optional Alerting Policies

```bash
# Create alert when error rate exceeds threshold
gcloud alpha monitoring policies create \
  --project=PROJECT_ID \
  --notification-channels=CHANNEL_ID \
  --display-name="High error rate" \
  --condition-display-name="Errors > 100/hour" \
  --condition-filter='metric.type="logging.googleapis.com/user/bio-core-errors" AND resource.type="k8s_container"' \
  --condition-threshold-value=100 \
  --condition-threshold-duration=3600s \
  --condition-threshold-comparison=COMPARISON_GT
```

## Dashboard Layout Guide

Grid is 12 columns wide. Recommended layout:

```
Row 0 (y=0):  [Line Chart 6x4] [Line Chart 6x4]    ← trends
Row 1 (y=4):  [Bar Chart 6x4]  [Line Chart 6x4]    ← categories
Row 2 (y=8):  [Scorecard 4x3]  [Scorecard 4x3]  [Scorecard 4x3]  ← KPIs
```

## Permissions Required

| Action | Role |
|--------|------|
| Create log metrics | `roles/logging.configWriter` |
| View log metrics | `roles/logging.viewer` |
| Create dashboards | `roles/monitoring.dashboardEditor` |
| Create alert policies | `roles/monitoring.alertPolicyEditor` |
| Use monitoring API | `roles/serviceusage.serviceUsageConsumer` |

## Tips

- **Multi-project**: Create metrics in each project separately. Dashboard can reference metrics from multiple projects if cross-project monitoring is configured.
- **Metric labels**: Use `--label-extractors` to extract fields (e.g., client name) as metric labels for groupBy in charts.
- **Cost**: Log-based metrics are free for counter metrics. Distribution metrics cost based on volume.
- **Dashboard JSON export**: Export an existing dashboard with `gcloud monitoring dashboards describe DASHBOARD_ID --format=json`.
