# Tetherloom: Orbit Run — Phase 1 (FloppyTap)

Package / Application ID: com.floppytap.tetherloom
Language: Java
Minimum SDK: 24

## What Phase 1 Contains

A compile-ready Android Studio project with:
- Splash screen showing the FloppyTap logo
- Main menu with all navigation destinations
- A playable minimal vertical slice: one Lumen, standard anchors, orbit-and-launch,
  capture, camera follow, fall/game-over, score, combo, pause/resume, restart
- Settings, How to Play, Credits, Progress screens
- Full English and Polish localization
- SharedPreferences-based settings and progress persistence
- Unit tests for core pure-Java logic

Daily Pattern and Practice modes are visible in the main menu but show a
"coming soon" message — their full systems (seeded generation, hazard-free tuning)
are scheduled for Phase 2, as specified.

## Build and Run Instructions

1. Open Android Studio (Giraffe or newer recommended).
2. Choose "Open" and select the `TetherloomOrbitRun` project root folder
   (the folder containing `settings.gradle`).
3. Wait for Gradle sync to finish. No manual dependency changes are required;
   all dependencies are already declared in `app/build.gradle`.
4. Confirm the application ID reads `com.floppytap.tetherloom` in
   Project Structure or `app/build.gradle`.
5. Select an emulator (API 24+) or a physical Android device connected via USB
   with developer mode and USB debugging enabled.
6. Click Run ▶ or use Shift+F10.

The app requests no permissions and works fully offline from first launch.

## Adding Original Sound Effects Later

Phase 1 ships without audio files, by design (no copyrighted or unlicensed audio
is included). To add original, properly licensed `.ogg` files in a later phase:
1. Create `app/src/main/res/raw/` if it does not exist.
2. Place `.ogg` files there (e.g., `sfx_launch.ogg`, `sfx_capture.ogg`).
3. A `SoundManager` class wiring these into `MediaPlayer`/`SoundPool` will be
   introduced in Phase 2 along with the audio architecture described in the
   original specification.

## Where Local Save Data Is Stored

All settings and progress (high score, best combo, language, toggle states) are
stored via Android `SharedPreferences` under the file name `tetherloom_prefs`,
accessed exclusively through `PreferencesManager`. This data lives in the app's
private storage sandbox and is not accessible to other apps. Uninstalling the
app removes this data. No cloud sync or account is used.

## Running Unit Tests

From Android Studio:
1. Open the `app/src/test/java/com/floppytap/tetherloom/` folder in the Project view.
2. Right-click the package and choose "Run Tests".

From the command line, in the project root:
    ./gradlew test

Test reports are written to `app/build/reports/tests/testDebugUnitTest/index.html`.

## Phase 1 QA Checklist

- [ ] First launch shows Splash → Main Menu without crashing.
- [ ] Orbit Run starts a playable session immediately.
- [ ] Tapping while orbiting detaches and launches the Lumen tangentially.
- [ ] Tapping while already flying has no duplicate launch effect.
- [ ] Flying into a reachable anchor captures the Lumen and increases score/combo.
- [ ] Camera follows the Lumen's vertical progress smoothly.
- [ ] Falling below the visible play area ends the run and shows the results overlay.
- [ ] Pause button freezes the loop and shows the pause overlay.
- [ ] Resume continues the same run without any state loss.
- [ ] Restart (from pause or game over) creates a fresh run with score/combo reset.
- [ ] Android Back during gameplay opens the pause overlay instead of exiting.
- [ ] Android Back while paused with the overlay visible (via Main Menu button) returns safely.
- [ ] Main Menu button always returns to MainActivity cleanly.
- [ ] Settings toggles (music, sfx, haptics, screen shake, reduced effects) persist after
      closing and reopening the app.
- [ ] Switching language to Polish updates all visible text and persists after restart.
- [ ] High score and best combo update correctly on the Progress screen after a run.
- [ ] How to Play screen shows the orbit diagram and localized explanation text.
- [ ] Credits screen shows the FloppyTap logo and localized credit text.
- [ ] App works with no internet connection (no network permission is requested).
- [ ] Rapid repeated taps during gameplay do not crash the app or cause double launches.

## Final Consistency Audit

- Package name `com.floppytap.tetherloom` is consistent across `settings.gradle`
  (root project name only, unrelated to app ID), `app/build.gradle` (`namespace`
  and `applicationId`), `AndroidManifest.xml` (implicit via manifest merger with
  matching namespace), and every Java file's `package` declaration.
- All seven manifest-declared activities (`SplashActivity`, `MainActivity`,
  `GameActivity`, `ui.SettingsActivity`, `ui.HowToPlayActivity`,
  `ui.CreditsActivity`, `ui.ProgressActivity`) exist as concrete Java classes.
- Every `R.id`, `R.layout`, `R.string`, `R.drawable`, and `R.color` reference used
  in Java code corresponds to a resource defined in the provided XML files.
- No player-visible text is hard-coded in Java; all strings route through
  `values/strings.xml` and `values-pl/strings.xml`.
- Game loop uses delta time (`GameLoop` computes `deltaSeconds` from
  `System.nanoTime()` each frame and clamps it to avoid spiral-of-death behavior).
- Pause/resume is safe: `GameLoop.setPaused()` stops calling `onUpdate` while still
  rendering, and `GameView` surface lifecycle methods stop/restart the thread
  correctly across Activity pause/resume/destroy.
- Back behavior is correct: pauses during gameplay, exits safely from menus by
  default Activity behavior (no custom override needed there).
- No unnecessary permissions are requested (manifest declares none).
- The project contains no pseudo-code, TODOs, or placeholder methods for anything
  required to compile and run Phase 1.
