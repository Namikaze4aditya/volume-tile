# 🔊 Volume Tile

[![Release](https://img.shields.io/github/v/release/Namikaze4aditya/volume-tile?style=for-the-badge&color=2196F3)](https://github.com/Namikaze4aditya/volume-tile/releases/latest)
[![Android](https://img.shields.io/badge/Android-8.0%2B%20(API%2026%2B)-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://android.com)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)
[![Zero Permissions](https://img.shields.io/badge/Permissions-Zero-success?style=for-the-badge)](app/src/main/AndroidManifest.xml)
[![Size](https://img.shields.io/badge/APK%20Size-~780_KB-informational?style=for-the-badge)](https://github.com/Namikaze4aditya/volume-tile/releases/latest)

A minimalist, single-purpose Android utility in Kotlin that adds a custom **Quick Settings tile** to invoke Android's **native system volume panel overlay** — the exact same overlay that appears when pressing physical hardware volume keys.

Great for:
- 📱 Devices with **broken, worn out, or sticky volume buttons**
- 📖 **Large phones, foldables, and tablets** where reaching volume buttons is awkward
- 🔇 Instant one-tap access to volume sliders directly from the notification shade

---

## ✨ Features

- **Native System Volume UI**: Invokes Android's built-in volume slider via `AudioManager.adjustSuggestedStreamVolume(ADJUST_SAME, USE_DEFAULT_STREAM_TYPE, FLAG_SHOW_UI)`. No clunky custom volume dialogs.
- **Dynamic Context Switching**: Uses `USE_DEFAULT_STREAM_TYPE` so it context-switches dynamically between Media, Voice Call, Ring, and Alarm just like physical hardware keys.
- **Live Tile Indicator**: Dynamic subtitle shows current volume percentage (`%`) or `Muted`, and switches icons (`ic_volume_up` / `ic_volume_off`).
- **Zero Background Footprint**: Volume broadcast receiver is registered **only** while the Quick Settings shade is open (`onStartListening()`) and unregistered the millisecond it closes (`onStopListening()`).
- **Zero Running Services**: No background service, no foreground notification, no JobScheduler, no WorkManager, no alarms, no battery drain.
- **Zero Permissions**: No `INTERNET` permission, zero network calls, fully offline, no telemetry, no ads.
- **Ultra Lightweight**: Under ~800 KB APK. Zero AndroidX and zero third-party dependencies.

---

## 📥 Download & Install

Download the latest APK directly from GitHub Releases:

👉 **[Download Latest APK (`app-debug.apk`)](https://github.com/Namikaze4aditya/volume-tile/releases/latest)**

Or install via ADB:
```bash
adb install -r app-debug.apk
```

---

## 🚀 How to Add the Quick Settings Tile

1. Swipe down twice from the top of your screen to open the full Quick Settings panel.
2. Tap the **Pencil / Edit** icon (or three dots `⋮` → **Edit tiles**).
3. Scroll down to find **Volume** under available tiles.
4. Drag **Volume** up into your active tiles.
5. Tap the tile anytime to adjust volume!

---

## ⚠️ OEM Battery Management Note (ColorOS / Realme UI / OxygenOS / MIUI)

On aggressive OEM skins (ColorOS, Realme UI, OxygenOS, HyperOS/MIUI), background cleaners may unbind Quick Settings tiles after long sleep periods.

If the tile ever stops responding, exclude the app from battery optimization:
> **Settings → Battery → App battery management → Volume Tile → Allow background activity / Don't optimize**.

*(Note: Android security policy does not allow apps to set this programmatically).*

---

## 🛠️ Build from Source

This project builds automatically on **GitHub Actions** with Java 17 and Gradle 8.7.

To build locally (requires Android SDK & Java 17+):
```bash
git clone https://github.com/Namikaze4aditya/volume-tile.git
cd volume-tile
./gradlew assembleDebug
```
The resulting APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

---

## 📄 License

Released under the [MIT License](LICENSE).

