# Testowanie gry Tetherloom: Orbit Run

Są cztery sposoby testowania, od najprostszego do najbliższego prawdziwemu wydaniu.

## 1. APK z GitHub Actions na własnym telefonie (bez Android Studio)

Każdy push do `main` i każdy pull request uruchamia workflow `.github/workflows/android.yml`.
Workflow wykonuje testy jednostkowe, lint i buduje wersję debug.

1. Wejdź w repozytorium na GitHubie → zakładka **Actions** → ostatnie uruchomienie workflow **Android**.
2. Na dole strony, w sekcji **Artifacts**, pobierz `tetherloom-debug-apk` (plik ZIP z `app-debug.apk`).
3. Przenieś `app-debug.apk` na telefon (np. przez Dysk Google lub kabel USB).
4. Otwórz plik na telefonie i zezwól na instalację z nieznanego źródła, gdy system o to zapyta.

Wersja debug ma identyfikator `com.floppytap.tetherloom.debug`, więc można ją mieć
zainstalowaną obok wersji ze Sklepu Play.

Raporty z testów i lint są w artefakcie `reports`.

## 2. Android Studio: emulator lub telefon przez USB

1. Zainstaluj Android Studio (aktualna wersja stabilna) z JDK 17 lub nowszym (jest wbudowany).
2. **File → Open** → wybierz katalog główny repozytorium (ten z `settings.gradle`).
3. Poczekaj na Gradle sync. Jeśli Android Studio poprosi o doinstalowanie SDK 36, zgódź się.
4. Urządzenie:
   - Emulator: **Device Manager → Create device** → np. Pixel 8, obraz systemu API 36.
     Warto mieć też drugi emulator z API 24 (minimalna obsługiwana wersja).
   - Telefon: Ustawienia → Informacje o telefonie → 7× stuknij „Numer kompilacji”,
     potem w Opcjach programisty włącz **Debugowanie USB** i podłącz kabel.
5. Kliknij **Run ▶** (Shift+F10).

## 3. Z linii poleceń

W katalogu repozytorium:

```bash
./gradlew testDebugUnitTest      # testy jednostkowe (14 testów)
./gradlew lintRelease            # analiza statyczna, raport: app/build/reports/lint-results-release.html
./gradlew assembleDebug          # APK debug: app/build/outputs/apk/debug/app-debug.apk
./gradlew installDebug           # instalacja na podłączonym urządzeniu/emulatorze (wymaga adb)
```

Raport testów: `app/build/reports/tests/testDebugUnitTest/index.html`.

Wersję release (po R8, tak jak trafi do użytkowników) też warto przetestować przed wysyłką:

```bash
./gradlew assembleRelease        # wymaga keystore.properties, patrz docs/WYDANIE.md
adb install -r app/build/outputs/apk/release/app-release.apk
```

## 4. Testy w Google Play (testy wewnętrzne i zamknięte)

To jest docelowy sposób testowania przed premierą. Opis w `docs/WYDANIE.md`, kroki 5 i 6.
Testerzy instalują grę ze Sklepu Play, a raporty awarii (ANR, crash) trafiają do Play Console.

## Co sprawdzić ręcznie

Pełna lista kontrolna jest w `README.md` (sekcja „Phase 1 QA Checklist”).
Dodatkowo, po aktualizacji do Android 16 (targetSdk 36), sprawdź:

- [ ] Przycisk **Pause** i wynik w grze nie chowają się pod paskiem stanu ani wycięciem aparatu.
- [ ] Przyciski na dole menu nie chowają się pod paskiem nawigacji (gesty i 3 przyciski).
- [ ] Ikony paska stanu są jasne (czytelne na ciemnym tle) w trybie jasnym i ciemnym systemu.
- [ ] Gest „wstecz” w trakcie gry otwiera pauzę; drugi gest wraca do menu; na ekranie
      „Run Ended” wraca do menu.
- [ ] Wersja release (`assembleRelease`) uruchamia się i działa tak samo jak debug
      (R8 może usunąć kod, którego używa się przez refleksję; tu go nie ma, ale warto potwierdzić).
- [ ] Zmiana języka na polski działa po instalacji z Google Play (bundle zawiera wszystkie języki).
- [ ] Na tablecie / składanym telefonie gra zostaje w pionie. Android 16 ignoruje blokadę
      orientacji na dużych ekranach, ale gry (`android:appCategory="game"` w manifeście) są
      z tego zwolnione. Emulator: „Pixel Tablet”.

Urządzenia minimum: jeden telefon/emulator z API 24, jeden z API 36, jeden tablet.
