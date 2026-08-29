# Fix Android SDK Compile Target Error

The project is currently configured to use Android SDK 37 for compilation, but this target is not being correctly resolved, leading to the error: `Could not find compile target android-37 for modules :shared`.

Based on the installed SDKs, API 35 is available and stable. This plan will downgrade the `compileSdk` and `targetSdk` to API 35 to resolve the build error.

## Proposed Changes

### [Build Configuration]

#### [MODIFY] [libs.versions.toml](file:///Users/iqbalfauzi/Personal/App/Robithoh/Robithoh-App/gradle/libs.versions.toml)
- Change `android-compileSdk` from `37` to `35`.
- Change `android-targetSdk` from `36` to `35`.

### [Documentation]

#### [MODIFY] [README.md](file:///Users/iqbalfauzi/Personal/App/Robithoh/Robithoh-App/README.md)
- Update the required Android SDK version in the setup instructions to match the new configuration.

## Verification Plan

### Automated Tests
- Run `./gradlew :shared:assembleDebug` to verify the `:shared` module builds successfully.
- Run `./gradlew :androidApp:assembleDebug` to verify the `:androidApp` module builds successfully.

### Manual Verification
- Perform a Gradle Sync in Android Studio to ensure all modules are correctly synchronized with the new SDK versions.
