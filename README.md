# Volume Tile

A minimal, single-purpose Android app written in Kotlin that adds a custom Quick Settings (QS) tile. When tapped, it invokes Android's own native system volume panel overlay — the exact same overlay that appears when pressing physical hardware volume buttons.

## Key Features

- **Native System Volume Panel**: Uses `AudioManager.adjustSuggestedStreamVolume(AudioManager.ADJUST_SAME, AudioManager.USE_DEFAULT_STREAM_TYPE, AudioManager.FLAG_SHOW_UI)`.
- **Dynamic Context Switching**: By passing `USE_DEFAULT_STREAM_TYPE` instead of a hardcoded stream (e.g. `STREAM_MUSIC`), Android automatically controls the appropriate stream based on active state (call, media, ring, alarm).
- **Dynamic Tile State & Subtitle**: Queries volume level and mute state in `onStartListening()` to display the current volume percentage (or "Muted") and toggles active/inactive tile states.
- **Zero Background Footprint**: A broadcast receiver for volume changes is registered only when the tile is visible (`onStartListening()`) and unregistered immediately when closed (`onStopListening()`). No background services, no polling, no alarms.
- **Zero Third-Party Dependencies**: No AndroidX, no Jetpack Compose, no Material Components. Uses purely native Android framework APIs (`android.jar`).
- **Zero Permissions**: No `INTERNET` permission, completely offline, privacy-friendly.

## ColorOS / Realme UI / Aggressive Battery Optimization Note

> **Important**: On ColorOS, Realme UI, OxygenOS, and certain other OEM Android skins, aggressive background killing may prevent the Android system from properly binding to third-party Quick Settings tiles.
>
> If the tile does not appear or becomes unresponsive, manually exclude this app from battery optimization:
> **Settings → Battery → App battery management (or More settings) → Volume Tile → Enable "Allow background activity" / Set to "Don't optimize"**.
>
> Android does not allow apps to set this programmatically.

## Headless Mode (No Launcher Activity)

If you prefer a completely headless app without any launcher icon in your app drawer:
1. Open `app/src/main/AndroidManifest.xml`.
2. Remove the `<activity android:name=".MainActivity" ...>...</activity>` element.
3. Rebuild the app.

## Building on GitHub Actions

This repository includes a ready-to-run GitHub Actions workflow (`.github/workflows/build.yml`).

1. Push this repository to GitHub.
2. Go to the **Actions** tab in your repository.
3. Select the **Build Debug APK** workflow.
4. Click **Run workflow** (`workflow_dispatch`).
5. Once the build finishes, download the generated debug APK from the workflow summary under **Artifacts** (`app-debug`).
