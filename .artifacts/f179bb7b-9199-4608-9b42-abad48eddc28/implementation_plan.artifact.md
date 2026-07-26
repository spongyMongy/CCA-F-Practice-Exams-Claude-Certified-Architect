# Restore Developer Debug Options

The user wants to restore the "Developer Debug Button" that was previously removed. This feature typically allows developers to bypass the paywall (Pro unlock) and manage the local database (sync/reset) during testing without needing a real purchase or manual data manipulation.

## Proposed Changes

### Configuration

#### [MODIFY] [MainActivity.kt](file:///C:/Users/makti/StudioProjects/CCA-F-Practice-Exams-Claude-Certified-Architect/app/src/main/java/com/arslan/ccafprep/MainActivity.kt)
- Remove the hardcoded `settingsManager.setProUnlocked(true)` and other debug overrides in `onCreate`. This ensures the app starts in a realistic state and allows the debug button to have a visible effect.

### Presentation Layer

#### [MODIFY] [SettingsViewModel.kt](file:///C:/Users/makti/StudioProjects/CCA-F-Practice-Exams-Claude-Certified-Architect/app/src/main/java/com/arslan/ccafprep/presentation/settings/SettingsViewModel.kt)
- Add a `togglePro()` function to toggle the `isProUnlocked` state in `SettingsManager`.
- Ensure `forceSync()` is accessible for the UI.

#### [MODIFY] [SettingsScreen.kt](file:///C:/Users/makti/StudioProjects/CCA-F-Practice-Exams-Claude-Certified-Architect/app/src/main/java/com/arslan/ccafprep/presentation/settings/SettingsScreen.kt)
- Add a new "Developer Options" section at the bottom of the settings list.
- Add a card with the following debug actions:
    - **Toggle Pro Status**: Toggles the simulated purchase state.
    - **Force DB Re-Seed**: Triggers a database sync to ensure all questions are loaded.
    - **Reset All Data**: (Optional but helpful) A button to clear everything.

## Verification Plan

### Manual Verification
- Navigate to **Settings**.
- Scroll to the bottom to find **Developer Options**.
- Tap **Toggle Pro Status** and verify that locked themes (e.g., Midnight Pro) become available immediately.
- Tap **Force DB Re-Seed** and verify (via logs or database inspector if available) that the seeding process triggers.
