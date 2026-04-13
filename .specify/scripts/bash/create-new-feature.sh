#!/usr/bin/env bash

set -e

JSON_MODE=false
SHORT_NAME=""
BRANCH_PREFIX="feature"
TICKET_ID=""
BASE_BRANCH=""
DRY_RUN=false
ALLOW_EXISTING=false
ARGS=()
i=1
while [ $i -le $# ]; do
    arg="${!i}"
    case "$arg" in
        --json)
            JSON_MODE=true
            ;;
        --short-name)
            if [ $((i + 1)) -gt $# ]; then
                echo 'Error: --short-name requires a value' >&2
                exit 1
            fi
            i=$((i + 1))
            next_arg="${!i}"
            if [[ "$next_arg" == --* ]]; then
                echo 'Error: --short-name requires a value' >&2
                exit 1
            fi
            SHORT_NAME="$next_arg"
            ;;
        --prefix)
            if [ $((i + 1)) -gt $# ]; then
                echo 'Error: --prefix requires a value' >&2
                exit 1
            fi
            i=$((i + 1))
            next_arg="${!i}"
            if [[ "$next_arg" == --* ]]; then
                echo 'Error: --prefix requires a value' >&2
                exit 1
            fi
            BRANCH_PREFIX="$next_arg"
            ;;
        --ticket)
            if [ $((i + 1)) -gt $# ]; then
                echo 'Error: --ticket requires a value' >&2
                exit 1
            fi
            i=$((i + 1))
            next_arg="${!i}"
            if [[ "$next_arg" == --* ]]; then
                echo 'Error: --ticket requires a value' >&2
                exit 1
            fi
            TICKET_ID="$next_arg"
            ;;
        --base)
            if [ $((i + 1)) -gt $# ]; then
                echo 'Error: --base requires a value' >&2
                exit 1
            fi
            i=$((i + 1))
            next_arg="${!i}"
            if [[ "$next_arg" == --* ]]; then
                echo 'Error: --base requires a value' >&2
                exit 1
            fi
            BASE_BRANCH="$next_arg"
            ;;
        --dry-run)
            DRY_RUN=true
            ;;
        --allow-existing-branch)
            ALLOW_EXISTING=true
            ;;
        --help|-h)
            echo "Usage: $0 [options] <feature_description>"
            echo ""
            echo "Options:"
            echo "  --prefix <type>     Git-flow prefix (default: feature)"
            echo "                      Common prefixes: feature, bugfix, hotfix, release, chore, docs"
            echo "  --ticket <id>       Ticket ID (e.g., PROJ-123)"
            echo "  --base <branch>     Override automatic base branch selection"
            echo "  --short-name <name> Provide a custom short name (2-4 words) for the slug"
            echo "  --json              Output in JSON format"
            echo "  --dry-run           Preview branch/dir names without creating anything"
            echo "  --allow-existing-branch  Checkout existing branch instead of erroring"
            echo "  --help, -h          Show this help message"
            echo ""
            echo "Branch naming:"
            echo "  With ticket:    {prefix}/{ticket-id}-{slug}  (e.g., feature/PROJ-123-user-auth)"
            echo "  Without ticket: {prefix}/{slug}              (e.g., feature/user-auth)"
            echo ""
            echo "Base branch selection (automatic unless --base is specified):"
            echo "  feature, bugfix, chore  -> develop (fallback to main)"
            echo "  hotfix                  -> main"
            echo "  release                 -> develop"
            echo "  docs                    -> main"
            echo ""
            echo "Examples:"
            echo "  $0 'Add user authentication system'"
            echo "  $0 --ticket PROJ-123 'Add user authentication system'"
            echo "  $0 --prefix hotfix --ticket PROJ-456 'Fix login crash'"
            echo "  $0 --prefix release --short-name 'v2.1' 'Release version 2.1'"
            exit 0
            ;;
        *)
            ARGS+=("$arg")
            ;;
    esac
    i=$((i + 1))
done

FEATURE_DESCRIPTION="${ARGS[*]}"
if [ -z "$FEATURE_DESCRIPTION" ]; then
    echo "Usage: $0 [--prefix <type>] [--ticket <id>] [--base <branch>] [--short-name <name>] [--json] <feature_description>" >&2
    exit 1
fi

# Trim whitespace and validate description is not empty (e.g., user passed only whitespace)
FEATURE_DESCRIPTION=$(echo "$FEATURE_DESCRIPTION" | xargs)
if [ -z "$FEATURE_DESCRIPTION" ]; then
    echo "Error: Feature description cannot be empty or contain only whitespace" >&2
    exit 1
fi

# Function to find the repository root by searching for existing project markers
find_repo_root() {
    local dir="$1"
    while [ "$dir" != "/" ]; do
        if [ -d "$dir/.git" ] || [ -d "$dir/.specify" ]; then
            echo "$dir"
            return 0
        fi
        dir="$(dirname "$dir")"
    done
    return 1
}

# Function to clean and format a branch name
clean_branch_name() {
    local name="$1"
    echo "$name" | tr '[:upper:]' '[:lower:]' | sed 's/[^a-z0-9]/-/g' | sed 's/-\+/-/g' | sed 's/^-//' | sed 's/-$//'
}

# Resolve repository root. Prefer git information when available, but fall back
# to searching for repository markers so the workflow still functions in repositories that
# were initialised with --no-git.
SCRIPT_DIR="$(CDPATH="" cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/common.sh"

if git rev-parse --show-toplevel >/dev/null 2>&1; then
    REPO_ROOT=$(git rev-parse --show-toplevel)
    HAS_GIT=true
else
    REPO_ROOT="$(find_repo_root "$SCRIPT_DIR")"
    if [ -z "$REPO_ROOT" ]; then
        echo "Error: Could not determine repository root. Please run this script from within the repository." >&2
        exit 1
    fi
    HAS_GIT=false
fi

cd "$REPO_ROOT"

SPECS_DIR="$REPO_ROOT/specs"
mkdir -p "$SPECS_DIR"

# Get the next auto-number by checking remote branch refs (no fetch side-effect)
get_next_auto_number() {
    local prefix="$1"
    local highest=0

    if [ "$HAS_GIT" = true ]; then
        # Check remote branches via ls-remote (no fetch)
        local remote_refs
        remote_refs=$(git ls-remote --heads origin "${prefix}/*" 2>/dev/null || true)
        if [ -n "$remote_refs" ]; then
            while IFS= read -r line; do
                local ref_name="${line##*/}"
                if [[ "$ref_name" =~ ^([0-9]+)- ]]; then
                    local num="${BASH_REMATCH[1]}"
                    num=$((10#$num))  # Remove leading zeros
                    if [ "$num" -gt "$highest" ]; then
                        highest=$num
                    fi
                fi
            done <<< "$remote_refs"
        fi

        # Also check local branches
        local local_refs
        local_refs=$(git branch --list "${prefix}/*" 2>/dev/null | sed 's/^[* ]*//')
        if [ -n "$local_refs" ]; then
            while IFS= read -r ref_name; do
                ref_name="${ref_name##*/}"
                if [[ "$ref_name" =~ ^([0-9]+)- ]]; then
                    local num="${BASH_REMATCH[1]}"
                    num=$((10#$num))
                    if [ "$num" -gt "$highest" ]; then
                        highest=$num
                    fi
                fi
            done <<< "$local_refs"
        fi
    fi

    echo $((highest + 1))
}

# Function to generate branch name with stop word filtering and length filtering
generate_branch_name() {
    local description="$1"

    # Common stop words to filter out
    local stop_words="^(i|a|an|the|to|for|of|in|on|at|by|with|from|is|are|was|were|be|been|being|have|has|had|do|does|did|will|would|should|could|can|may|might|must|shall|this|that|these|those|my|your|our|their|want|need|add|get|set)$"

    # Convert to lowercase and split into words
    local clean_name=$(echo "$description" | tr '[:upper:]' '[:lower:]' | sed 's/[^a-z0-9]/ /g')

    # Filter words: remove stop words and words shorter than 3 chars (unless they're uppercase acronyms in original)
    local meaningful_words=()
    for word in $clean_name; do
        # Skip empty words
        [ -z "$word" ] && continue

        # Keep words that are NOT stop words AND (length >= 3 OR are potential acronyms)
        if ! echo "$word" | grep -qiE "$stop_words"; then
            if [ ${#word} -ge 3 ]; then
                meaningful_words+=("$word")
            elif echo "$description" | grep -q "\b${word^^}\b"; then
                # Keep short words if they appear as uppercase in original (likely acronyms)
                meaningful_words+=("$word")
            fi
        fi
    done

    # If we have meaningful words, use first 3-4 of them
    if [ ${#meaningful_words[@]} -gt 0 ]; then
        local max_words=3
        if [ ${#meaningful_words[@]} -eq 4 ]; then max_words=4; fi

        local result=""
        local count=0
        for word in "${meaningful_words[@]}"; do
            if [ $count -ge $max_words ]; then break; fi
            if [ -n "$result" ]; then result="$result-"; fi
            result="$result$word"
            count=$((count + 1))
        done
        echo "$result"
    else
        # Fallback to original logic if no meaningful words found
        local cleaned=$(clean_branch_name "$description")
        echo "$cleaned" | tr '-' '\n' | grep -v '^$' | head -3 | tr '\n' '-' | sed 's/-$//'
    fi
}

# Determine base branch automatically based on prefix, unless --base overrides
resolve_base_branch() {
    local prefix="$1"

    case "$prefix" in
        feature|bugfix|chore)
            # Prefer develop, fallback to main
            if git rev-parse --verify develop >/dev/null 2>&1; then
                echo "develop"
            else
                echo "main"
            fi
            ;;
        hotfix)
            echo "main"
            ;;
        release)
            echo "develop"
            ;;
        docs)
            echo "main"
            ;;
        *)
            # Unknown prefix: prefer develop, fallback to main
            if git rev-parse --verify develop >/dev/null 2>&1; then
                echo "develop"
            else
                echo "main"
            fi
            ;;
    esac
}

# Generate branch slug
if [ -n "$SHORT_NAME" ]; then
    # Use provided short name, just clean it up
    BRANCH_SLUG=$(clean_branch_name "$SHORT_NAME")
else
    # Generate from description with smart filtering
    BRANCH_SLUG=$(generate_branch_name "$FEATURE_DESCRIPTION")
fi

# Build branch name: {prefix}/{ticket-id}-{slug} or {prefix}/{slug}
if [ -n "$TICKET_ID" ]; then
    BRANCH_NAME="${BRANCH_PREFIX}/${TICKET_ID}-${BRANCH_SLUG}"
    SPEC_DIR_NAME="${TICKET_ID}-${BRANCH_SLUG}"
else
    BRANCH_NAME="${BRANCH_PREFIX}/${BRANCH_SLUG}"
    SPEC_DIR_NAME="${BRANCH_SLUG}"
fi

# GitHub enforces a 244-byte limit on branch names
# Validate and truncate if necessary
MAX_BRANCH_LENGTH=244
if [ ${#BRANCH_NAME} -gt $MAX_BRANCH_LENGTH ]; then
    # Calculate max slug length: total - prefix/ - ticket-id- (if present)
    PREFIX_LEN=$(( ${#BRANCH_PREFIX} + 1 ))  # +1 for /
    if [ -n "$TICKET_ID" ]; then
        PREFIX_LEN=$(( PREFIX_LEN + ${#TICKET_ID} + 1 ))  # +1 for -
    fi
    MAX_SLUG_LENGTH=$(( MAX_BRANCH_LENGTH - PREFIX_LEN ))

    # Truncate slug
    TRUNCATED_SLUG=$(echo "$BRANCH_SLUG" | cut -c1-$MAX_SLUG_LENGTH)
    TRUNCATED_SLUG=$(echo "$TRUNCATED_SLUG" | sed 's/-$//')

    ORIGINAL_BRANCH_NAME="$BRANCH_NAME"
    BRANCH_SLUG="$TRUNCATED_SLUG"

    if [ -n "$TICKET_ID" ]; then
        BRANCH_NAME="${BRANCH_PREFIX}/${TICKET_ID}-${BRANCH_SLUG}"
        SPEC_DIR_NAME="${TICKET_ID}-${BRANCH_SLUG}"
    else
        BRANCH_NAME="${BRANCH_PREFIX}/${BRANCH_SLUG}"
        SPEC_DIR_NAME="${BRANCH_SLUG}"
    fi

    >&2 echo "[specify] Warning: Branch name exceeded GitHub's 244-byte limit"
    >&2 echo "[specify] Original: $ORIGINAL_BRANCH_NAME (${#ORIGINAL_BRANCH_NAME} bytes)"
    >&2 echo "[specify] Truncated to: $BRANCH_NAME (${#BRANCH_NAME} bytes)"
fi

# Determine base branch
if [ -n "$BASE_BRANCH" ]; then
    BRANCH_BASE="$BASE_BRANCH"
elif [ "$HAS_GIT" = true ]; then
    BRANCH_BASE=$(resolve_base_branch "$BRANCH_PREFIX")
else
    BRANCH_BASE="main"
fi

# Dry-run: output computed values and exit without side effects
if $DRY_RUN; then
    if $JSON_MODE; then
        if command -v jq >/dev/null 2>&1; then
            jq -cn \
                --arg branch_name "$BRANCH_NAME" \
                --arg spec_dir "$SPECS_DIR/$SPEC_DIR_NAME" \
                --arg branch_prefix "$BRANCH_PREFIX" \
                --arg branch_base "$BRANCH_BASE" \
                --argjson dry_run true \
                '{BRANCH_NAME:$branch_name,SPEC_DIR:$spec_dir,BRANCH_PREFIX:$branch_prefix,BRANCH_BASE:$branch_base,DRY_RUN:$dry_run}'
        else
            printf '{"BRANCH_NAME":"%s","SPEC_DIR":"%s","BRANCH_PREFIX":"%s","BRANCH_BASE":"%s","DRY_RUN":true}\n' \
                "$(json_escape "$BRANCH_NAME")" \
                "$(json_escape "$SPECS_DIR/$SPEC_DIR_NAME")" \
                "$(json_escape "$BRANCH_PREFIX")" \
                "$(json_escape "$BRANCH_BASE")"
        fi
    else
        echo "BRANCH_NAME: $BRANCH_NAME"
        echo "SPEC_DIR: $SPECS_DIR/$SPEC_DIR_NAME"
        echo "BRANCH_PREFIX: $BRANCH_PREFIX"
        echo "BRANCH_BASE: $BRANCH_BASE"
        echo "DRY_RUN: true"
    fi
    exit 0
fi

# Create git branch
if [ "$HAS_GIT" = true ]; then
    # Checkout the base branch first
    if ! git checkout "$BRANCH_BASE" >/dev/null 2>&1; then
        >&2 echo "Error: Failed to checkout base branch '$BRANCH_BASE'. Please verify it exists."
        exit 1
    fi

    # Create the new branch
    if ! git checkout -b "$BRANCH_NAME" 2>/dev/null; then
        if git branch --list "$BRANCH_NAME" | grep -q .; then
            if $ALLOW_EXISTING; then
                >&2 echo "[specify] Branch '$BRANCH_NAME' already exists; checking out existing branch"
                if ! git checkout "$BRANCH_NAME" 2>/dev/null; then
                    >&2 echo "Error: Failed to checkout existing branch '$BRANCH_NAME'."
                    exit 1
                fi
            else
                >&2 echo "Error: Branch '$BRANCH_NAME' already exists. Use --allow-existing-branch to checkout it, or use a different feature name."
                exit 1
            fi
        else
            >&2 echo "Error: Failed to create git branch '$BRANCH_NAME'. Please check your git configuration and try again."
            exit 1
        fi
    fi
else
    >&2 echo "[specify] Warning: Git repository not detected; skipped branch creation for $BRANCH_NAME"
fi

# Create spec directory (no git-flow prefix in spec dir)
FEATURE_DIR="$SPECS_DIR/$SPEC_DIR_NAME"
mkdir -p "$FEATURE_DIR"

TEMPLATE=$(resolve_template "spec-template" "$REPO_ROOT") || true
SPEC_FILE="$FEATURE_DIR/spec.md"
if [ -f "$SPEC_FILE" ]; then
    >&2 echo "[specify] Spec file already exists at $SPEC_FILE; skipping template copy"
else
    if [ -n "$TEMPLATE" ] && [ -f "$TEMPLATE" ]; then
        cp "$TEMPLATE" "$SPEC_FILE"
    else
        echo "Warning: Spec template not found; created empty spec file" >&2
        touch "$SPEC_FILE"
    fi
fi

# Inform the user how to persist the feature variable in their own shell
printf '# To persist: export SPECIFY_FEATURE=%q\n' "$BRANCH_NAME" >&2

if $JSON_MODE; then
    if command -v jq >/dev/null 2>&1; then
        jq -cn \
            --arg branch_name "$BRANCH_NAME" \
            --arg spec_file "$SPEC_FILE" \
            --arg branch_prefix "$BRANCH_PREFIX" \
            --arg branch_base "$BRANCH_BASE" \
            '{BRANCH_NAME:$branch_name,SPEC_FILE:$spec_file,BRANCH_PREFIX:$branch_prefix,BRANCH_BASE:$branch_base}'
    else
        printf '{"BRANCH_NAME":"%s","SPEC_FILE":"%s","BRANCH_PREFIX":"%s","BRANCH_BASE":"%s"}\n' \
            "$(json_escape "$BRANCH_NAME")" \
            "$(json_escape "$SPEC_FILE")" \
            "$(json_escape "$BRANCH_PREFIX")" \
            "$(json_escape "$BRANCH_BASE")"
    fi
else
    echo "BRANCH_NAME: $BRANCH_NAME"
    echo "SPEC_FILE: $SPEC_FILE"
    echo "BRANCH_PREFIX: $BRANCH_PREFIX"
    echo "BRANCH_BASE: $BRANCH_BASE"
    printf '# To persist in your shell: export SPECIFY_FEATURE=%q\n' "$BRANCH_NAME"
fi
