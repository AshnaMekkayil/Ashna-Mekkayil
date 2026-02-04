# Monefy Mobile E2E Automation (Android)

This repository contains an end-to-end (E2E) test automation framework for the Monefy Android app:
- https://play.google.com/store/apps/details?id=com.monefy.app.lite

## ✅ Covered E2E User Flows (3 most important)
Based on exploratory testing, the chosen critical user flows are:

1) **Add Income**
- Tap INCOME → enter amount → choose category → return to home
- Validate income gets updated

2) **Add Expense**
- Tap EXPENSE → enter amount → choose category → return to home
- Validate expense total increases (or at least does not decrease)

3) **Income should increase upon adding**
- Tap INCOME → enter amount → choose category → return to home
- Validate income total increases (or at least does not decrease)

These 3 flows represent the most frequent and business-critical actions a user performs in Monefy.

---

## Tech Stack & Why
- **Java 17**: stable, widely used for automation; strong tooling and ecosystem
- **Appium (UiAutomator2)**: standard for Android UI automation; works across devices/emulators
- **TestNG**: simple test structure, annotations, and reporting integration
- **Gradle**: repeatable build and execution, easy CI integration
- **Page Object Model (POM)**: keeps selectors/actions inside page classes, reduces duplication, improves maintainability

---

## Project Structure
- `src/test/java/base` → driver + base test setup
- `src/test/java/pages` → page objects (HomePage contains actions/selectors)
- `src/test/java/tests` → test classes (3 flows)
- `build/reports/tests/test/` → generated HTML execution report after running tests

---

## Setup

### 1) Prerequisites
Install:
- Java 17 (JDK)
- Android Studio + Android SDK
- Appium Server (Desktop or npm)
- Android emulator or real device

Verify:
- `adb devices` shows your emulator/device
- Appium server is running (default: `http://127.0.0.1:4723`)

### 2) App Under Test
This framework assumes the Monefy app is installed on the device/emulator:
- package: `com.monefy.app.lite`

If not installed, install it from Play Store on the emulator/device.

### 3) Configuration (Capabilities)
Capabilities are defined in the Driver/BaseTest (example values):
- platformName: ANDROID
- automationName: UiAutomator2
- deviceName: emulator-5554
- appPackage: com.monefy.app.lite
- appActivity: com.monefy.activities.main.MainActivity (or wildcard wait activity)
- noReset: true
- autoGrantPermissions: true

---

## Run Tests

From repo root:

### Run all tests
```bash
./gradlew cleanTest test --rerun-tasks
