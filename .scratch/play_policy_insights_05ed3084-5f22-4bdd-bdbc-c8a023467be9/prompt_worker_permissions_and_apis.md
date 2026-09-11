





## Permissions and APIs Audit

### Hint: Deducing Core Functionality

Since you must determine if certain permissions are justified by the app's "core
purpose", use these fast heuristics:
1. **The "Broken" Test**: Is the feature essential to the app's primary purpose?
   If the app would still be functional and useful without the feature, it is
   NOT core functionality.
2. **Manifest Intent**: Review `AndroidManifest.xml`. The name of the `LAUNCHER`
   Activity and specialized `<intent-filter>` declarations (like default SMS
   handlers) strongly indicate the app's main purpose.
3. **Naming**: The package name (`{{PACKAGE_NAME}}`) and app label
   (`app_name`) often describe the app's purpose explicitly.
4. **Execution Context**: Usage in classes like `BackupManager` suggest core
   functionality, whereas usage in `AdHelper`, `CrashReporter`, or
   `AnalyticsManager` indicates secondary features.
5. **Mandatory Rule**: Secondary features like **advertising, analytics, or
   social sharing never justify** restricted permissions like Background
   Location, All Files Access, or Broad Media Access.

---

### Policies to Verify

#### Location Access Policy (Policy ID: location_access_policy)

- **Goal**: Verify that location access is essential, uses minimum scope, and is
  properly disclosed.
- **The Policy Spirit**: User tracking is extremely sensitive. Apps must collect
  the minimum scope of location required (approximate vs precise), provide clear
  prominent disclosures, and must never utilize background tracking unless it is
  essential for safety, navigation, or physical fitness features.
- **Evidence**:
  
  - **Precise Location usage**:
    - `androidApp/src/main/kotlin/com/iqbalwork/robithoh/widget/PrayerWidgetHelper.kt (Pattern: ACCESS_FINE_LOCATION)`
- `shared/src/androidMain/kotlin/com/iqbalwork/robithoh/core/location/LocationProvider.android.kt (Pattern: ACCESS_FINE_LOCATION)`
- `shared/src/androidMain/kotlin/com/iqbalwork/robithoh/core/notification/PrayerAlarmScheduler.android.kt (Pattern: latitude)`
  
  
  - **Approximate Location usage**:
    - `androidApp/src/main/kotlin/com/iqbalwork/robithoh/widget/PrayerWidgetHelper.kt (Pattern: ACCESS_COARSE_LOCATION)`
- `shared/src/androidMain/kotlin/com/iqbalwork/robithoh/core/location/LocationProvider.android.kt (Pattern: ACCESS_COARSE_LOCATION)`
- `shared/src/iosMain/kotlin/com/iqbalwork/robithoh/core/location/LocationProvider.ios.kt (Pattern: LocationManager)`
  
  **Relevant Permissions Requested**:
  - `android.permission.ACCESS_FINE_LOCATION`
- `android.permission.ACCESS_COARSE_LOCATION`
  
- **Common Evaluation Matrix**:
  | Scope | Target SDK | Finding / Condition Checked | Severity | Direct Actionable Recommendation |
  | :--- | :--- | :--- | :--- | :--- |
  | **Foreground** | Any | Requests `ACCESS_FINE_LOCATION` but features only require city-level or approximate weather/search features. | `IMPORTANT` | Downgrade Manifest to `ACCESS_COARSE_LOCATION` to respect the minimum scope mandate. |
  | **Foreground** | Any | App purpose (`app_name`) does not imply location, yet foreground tracking is used, and prominent disclosure alert code is missing. | `IMPORTANT` | Implement a **Prominent In-App Disclosure** dialog explaining what location data is collected *before* requesting foreground permission. |
  | **Background** | Any | Requests `ACCESS_BACKGROUND_LOCATION` solely for advertising, marketing, or general analytics. | `CRITICAL` | **High-Risk Violation**: Completely remove background location collection from the codebase. |
  | **Background** | Any | Requests background location, but features could operate with foreground location access. | `IMPORTANT` | Downgrade the feature to foreground-only location tracking and remove background permission. |
  | **Background** | Any | Background location is legitimate, but prominent disclosure does not mention "location" and "when the app is closed or not in use." | `IMPORTANT` | Update the Prominent Disclosure text to explicitly state "location" and "when closed or not in use". |
  | **Foreground** | **37 or higher** | Precise location is requested on Android 17+. | `SUGGESTION` | Migrate to the **Location Button** API as the minimum scope mechanism for precise foreground location. |

- **Domain-Specific Heuristics (Strictly Bounded)**:
  Actively trace custom background workers and disclosures:
  1. **The Foreground Sufficiency Test**: Analyze background threads,
     `WorkManager` tasks, or background services triggering location updates. If
     the background process performs syncing or location calculations that could
     be deferred to when the user is actively viewing the app, flag an
     `IMPORTANT` violation.
  2. **The Reasonable Expectation Test**: If the app label (`app_name`) or
     packages suggest a utility that shouldn't logically track location,
     evaluate any indirect geo-tracking (such as geo-lookup of network IP
     addresses, or sending local Wi-Fi SSID logs off-device). If found, flag an
     `IMPORTANT` missing prominent disclosure.

#### Exact Alarm Policy (Policy ID: exact_alarm_policy)

- **Goal**: Evaluate if the app's core functionality justifies the
  `USE_EXACT_ALARM` permission.
- **The Policy Spirit**: Exact alarms degrade system performance and battery
  life. The Play Store strictly limits the high-risk `USE_EXACT_ALARM`
  permission to alarm clocks, timers, and calendar apps where precise,
  down-to-the-second timing is critical.
- **Evidence**:
  - `androidApp/src/main/kotlin/com/iqbalwork/robithoh/widget/PrayerWidgetHelper.kt (Pattern: AlarmManager)`
- `shared/src/androidMain/kotlin/com/iqbalwork/robithoh/core/notification/PrayerAlarmScheduler.android.kt (Pattern: setExact)`
  **Relevant Permissions Requested**:
  - `android.permission.USE_EXACT_ALARM`
- `android.permission.SCHEDULE_EXACT_ALARM`
- **Common Evaluation Matrix**:
  | Core App Purpose | Permission Requested | Justified? | Severity | Direct Actionable Recommendation |
  | :--- | :--- | :--- | :--- | :--- |
  | **Alarm Clock, Timer, or Calendar App** | `USE_EXACT_ALARM` | Yes (Compliant) | None | No action needed. |
  | **Standard Utility, Game, Sync, or Productivity App** | `USE_EXACT_ALARM` | No (Violation) | `IMPORTANT` | Switch to `SCHEDULE_EXACT_ALARM` which respects system battery constraints, or use standard `AlarmManager` inexact scheduling. |

- **Domain-Specific Heuristics (Strictly Bounded)**:
  Examine alarm trigger targets:
  1. **Time-Insensitive Syncing**: Verify the files scheduling alarms. If alarms
     are used to fetch network updates, clean cache files, trigger analytics
     uploads, or post local daily notifications, exact timing is not justified.
     Recommend using `WorkManager` for background tasks instead of
     `AlarmManager`.

#### Foreground Services (Policy ID: foreground_services_policy)

- **Goal**: Verify the declaration and justification of foreground services.
- **The Policy Spirit**: Foreground services keep processes alive in the
  background and must be highly visible to users. Every declared service must
  have an appropriate `foregroundServiceType` defined in the Manifest, and
  special types like `specialUse` require specific tag property justifications.
- **Evidence**:
  - `shared/src/androidMain/kotlin/com/iqbalwork/robithoh/core/notification/PrayerAdzanService.kt (Pattern: FOREGROUND_SERVICE)`
- `shared/src/androidMain/kotlin/com/iqbalwork/robithoh/core/notification/PrayerAlarmReceiver.kt (Pattern: startForeground)`
  **Relevant Permissions Requested**:
  - `android.permission.FOREGROUND_SERVICE`
  
- **Common Evaluation Matrix**:
  | Service Configuration | Justification Check | Severity | Direct Actionable Recommendation |
  | :--- | :--- | :--- | :--- |
  | **Missing type tag** | Foreground service is declared but lacks a `foregroundServiceType` attribute. | `CRITICAL` | Add the appropriate `android:foregroundServiceType` attribute to the service declaration in the Manifest. |
  | **Lacks specialUse property** | Service type is `specialUse`, but Manifest lacks the required `<property android:name="android.app.PROPERTY_SPECIAL_USE_FGS_SUBTYPE" ...>` tag. | `CRITICAL` | Add the `<property>` tag inside the service block with a valid subtype string. |
  | **Type Misalignment** | Declared FGS type does not logically align with the app's core purpose. | `IMPORTANT` | Re-align FGS type to match app features, or migrate background operations to **WorkManager** if user-visible foreground presence is not justified. |
  | **Declaration Reminder** | Foreground service is declared (even if type is correct). | `SUGGESTION` | **Play Console Declaration Required**: For apps targeting Android 14+, you must complete a Foreground Service declaration in the Play Console (App content section) for each type used, providing a functional description, user impact video, and a specific use case selection. |

- **Domain-Specific Heuristics (Strictly Bounded)**:
  Critique specialUse justifications and service behavior:
  1. **Justification String Audit**: Read the text of the `<property>` tag for
     `specialUse`. If the text contains weak, boilerplate, or placeholder
     justifications (e.g., "requires background process for app to run"), flag
     an `IMPORTANT` violation warning the developer that Google Play reviewers
     will reject this service.
  2. **Notification Integrity**: Verify if the FGS implementation creates a
     valid user-facing notification. If no `startForeground()` or notification
     builder logic is associated with the service initiation, flag an
     `IMPORTANT` violation.
  3. **Play Console Declaration Confirmation**: If any foreground service is
     used, flag a `SUGGESTION` to remind the developer that a specialized
     declaration form in the Play Console is mandatory, requiring a video
     demonstration of the feature.

## Output schema

Save final JSON output to `/home/iqbalf/Projects/Robithoh/Robithoh App/.scratch/play_policy_insights_05ed3084-5f22-4bdd-bdbc-c8a023467be9/worker_{{GOAL_NAME}}.json`.

```json
{
  "domain": "Permissions and APIs",
  "findings": [
    {
      "policy_id": "STRING_VALUE (The exact Policy ID, e.g., photo_video_access_policy)",
      "issue_summary": "STRING_VALUE",
      "severity": "CRITICAL | IMPORTANT | SUGGESTION",
      "files_involved": ["STRING_VALUE"],
      "evidence": "STRING_VALUE",
      "recommendation": "STRING_VALUE"
    }
  ]
}
```

# Execution Mandates

### Technical Rules

1.  **Absolute Paths Only**: Always resolve and use absolute paths.
2.  **Containment**: Write all artifacts strictly within `/home/iqbalf/Projects/Robithoh/Robithoh App/.scratch/play_policy_insights_05ed3084-5f22-4bdd-bdbc-c8a023467be9`.
3.  **Fail-fast**: If any required input file is missing, stop immediately and
    report the failure.

### Surgical Input Protocol & Efficient Search (MANDATORY)

-   **Direct Evidence First**: Prioritize files listed in the **Context &
    Evidence** sections. Use the provided file/line evidence (e.g., from Data
    Sources or Sinks) to jump directly to the relevant code. Do not perform
    broad workspace searches if these surgical starting points are available.
-   **Path Filtering Over File Crawling**: Locate target files by name, path, or
    extension *first* using directory/file listing tools before performing any
    text/content-based searches. Restrict searches and file reads strictly to
    the target `/home/iqbalf/Projects/Robithoh/Robithoh App`.
-   **Strict Exclusions (The Noise Wall)**: Configure search, glob, and find
    tools to ignore build, cache, dependencies, and testing folders. You MUST
    exclude matches from: `**/build/**`, `**/.gradle/**`, `**/.scratch/**`,
    `**/androidTest/**`, `**/test/**`, `**/node_modules/**`.
-   **Targeted Extensions**: Restrict content searches and file reads strictly
    to source and configuration files: `.java`, `.kt`, `.xml`, `.gradle`, `.kts`
    (and `.js`, `.ts`, `.jsx`, `.tsx`, `.dart` if a hybrid/cross-platform
    environment is analyzed). Never search or read inside compiled `.class`
    files, binary resources, or output assets.
-   **Surgical Queries & Limiters**: Use highly specific search patterns (e.g.,
    search for `getLastKnownLocation` or `deleteAccount` instead of general
    words like `location` or `delete`). If search tools support limits or
    pagination, cap results at a maximum of 50 matches. Do not load unlimited
    search results into your context window.
-   **Parallel Reading Required (Turn Efficiency)**: You are operating under a
    strict maximum turn limit. To prevent timeouts, you MUST request to read
    multiple target files concurrently in a single response. Do not read the
    evidence files sequentially one-by-one. Issue all of your file-reading tool
    calls simultaneously whenever possible.

### Evidentiary Standard & Guardrails (CRITICAL)

To prevent over-auditing, false positives, and speculative "prosecution" of
compliant code during extrapolation:

1.  **Presumption of Compliance**: Treat code as compliant unless there is
    *definitive, visible evidence* in the provided files of a policy violation.
    If code is ambiguous, or if network/database logic is hidden behind
    abstractions (e.g., calling an interface or repository method like
    `clearSession()`), you must assume standard compliant behavior. Do NOT guess
    or speculate about what happens behind interfaces.
2.  **Benefit of the Doubt**: When compliance cannot be strictly verified due to
    code abstractions or missing source file contexts, you must downgrade your
    finding:
    -   Never flag a `🔴 Critical` or `🟡 Important` finding based on suspicion or
        lack of context.
    -   Instead, output a `🔵 Suggestion` (informational) to advise the developer
        on what to double-check in their backend or configuration.
3.  **Exclusion of Local State**: Local-only processing (e.g., caching theme
    settings, user-selected visual configurations, or on-device-only database
    operations) is explicitly exempt from Data Safety collection or Account
    Deletion mandates.
4.  **Concrete Attributions**: Every `🔴 Critical` or `🟡 Important` finding must
    cite the exact file, line number, or configuration block containing the
    direct violation. If you cannot cite the exact line of code containing the
    violation, you cannot flag it as a violation.
5.  **Empty-List Discipline**: If no policy violations, discrepancies, or review
    items are identified during your audit, you MUST represent this as an empty
    array `[]` for that field (e.g., `"findings": []`, `"verified_findings":
    []`, or `"manual_verification_required": []`). **DO NOT** populate arrays
    with "dummy" objects, placeholder strings, or `"N/A"` / `"None"` values.
6.  **Heuristics & Extrapolation Boundaries**: Whenever applying specific
    heuristics defined in your goal (e.g., searching for implicit logger leaks
    or
    SDK siphoning), you must strictly bound them to the provided evidence and
    their immediate callers. You are strictly forbidden from initiating broad,
    unbounded searches for custom paths or variables across the wider codebase.
    Base your extrapolation only within the specific files already provided to
    you in the prompt.

### Finalization & Output Mandates (CRITICAL)

-   **Iterative Saving**: If your investigation requires multiple steps, save
    partial or intermediate JSON states to disk as you progress. Do not hold all
    data in memory until the very end to prevent data loss upon interruption.
-   **Strict File Output (NO TRIPLE BACKTICKS)**: You MUST save your final JSON
    output to disk at the exact path specified in the goal schema using your
    file-writing capabilities.
    -   **CRITICAL: The content written to the file MUST be pure, raw JSON. DO
        NOT wrap the contents inside the JSON file with Markdown code blocks
        (such as triple backticks `json ...`). Writing markdown blocks into the
        file makes the JSON unparseable by the compiler.**
-   **NO Chat Summaries**: **MANDATORY: DO NOT summarize your findings, explain
    your reasoning, or output JSON in your final chat response.** Your chat
    output wastes context and is ignored by the orchestrator.
-   **Verification Before Termination**: You MUST only terminate and return the
    "SUCCESS" string *after* you have explicitly verified that your JSON file
    successfully wrote to disk and contains valid JSON (e.g., by reading the
    file back or checking the directory contents).
-   **Final response**: Your final response MUST be exactly the word: "SUCCESS"
    and nothing else.
