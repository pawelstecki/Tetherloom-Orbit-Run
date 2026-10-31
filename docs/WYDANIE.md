# Wydanie gry w Google Play

Stan projektu: gotowy technicznie do wysłania (targetSdk 36, podpisywanie, R8, AAB).
Poniżej kroki, które musisz wykonać sam, bo wymagają Twojego konta i decyzji.

## Co zostało przygotowane w repozytorium

| Element | Gdzie |
|---|---|
| compileSdk/targetSdk 36 (wymóg Google Play od 31.08.2026) | `app/build.gradle` |
| AGP 8.13.2 + Gradle 8.14.3 (wrapper `./gradlew`) | `build.gradle`, `gradle/wrapper/` |
| Podpisywanie release z `keystore.properties` lub zmiennych środowiskowych | `app/build.gradle` |
| R8 (minifikacja + usuwanie nieużywanych zasobów), plik `mapping.txt` | `app/build.gradle`, `app/proguard-rules.pro` |
| Wszystkie języki w każdej instalacji (przełącznik języka w grze) | `bundle { language { enableSplit = false } }` |
| Edge-to-edge: treść nie chowa się pod paskami systemu | `util/SystemBarsHelper.java` |
| Gest „wstecz” zgodny z Android 16 (predictive back) | `GameActivity.java` |
| `appCategory="game"`, poprawiona ikona okrągła | `AndroidManifest.xml` |
| CI: testy, lint, APK debug, podpisany AAB na żądanie | `.github/workflows/android.yml` |
| Opisy do sklepu PL/EN | `docs/store-listing/` |
| Polityka prywatności PL/EN | `docs/privacy-policy.md` |
| Prompty do grafik sklepowych | `docs/grafiki-prompty.md` |

## Krok 1. Klucz do podpisywania (upload key)

Wykonaj raz, na swoim komputerze (wymaga JDK, jest w Android Studio):

```bash
mkdir -p release
keytool -genkeypair -v -keystore release/tetherloom-upload.jks \
  -alias upload -keyalg RSA -keysize 2048 -validity 10000
```

Potem skopiuj `keystore.properties.example` jako `keystore.properties` i wpisz hasła.
Oba pliki są w `.gitignore`. **Nie wysyłaj ich do repozytorium.**

Zrób kopię zapasową `.jks` i haseł w menedżerze haseł. Przy Play App Signing (krok 4)
utratę klucza uploadu da się odkręcić przez Play Console, ale zajmuje to kilka dni.

## Krok 2. Zbudowanie AAB

Lokalnie:

```bash
./gradlew bundleRelease
# wynik: app/build/outputs/bundle/release/app-release.aab
```

Albo w GitHub Actions (bez Android Studio). Dodaj sekrety w
**Settings → Secrets and variables → Actions**:

| Sekret | Wartość |
|---|---|
| `TETHERLOOM_KEYSTORE_BASE64` | wynik `base64 -w0 release/tetherloom-upload.jks` |
| `TETHERLOOM_KEYSTORE_PASSWORD` | hasło keystore |
| `TETHERLOOM_KEY_ALIAS` | `upload` |
| `TETHERLOOM_KEY_PASSWORD` | hasło klucza |

Następnie **Actions → Android → Run workflow**, zaznacz „Build a signed release AAB”.
Artefakt `tetherloom-release-aab` zawiera `app-release.aab` i `mapping.txt`.

Przed każdą kolejną wysyłką zwiększ `versionCode` o 1 w `app/build.gradle`
(i ewentualnie `versionName`). Play odrzuca plik z już użytym `versionCode`.

## Krok 3. Konto Google Play Console

1. Załóż konto na https://play.google.com/console (jednorazowa opłata 25 USD).
2. Wybierz typ konta: **osobiste** albo **organizacja** (organizacja wymaga numeru D-U-N-S).
3. Przejdź weryfikację tożsamości.

Konta osobiste założone po 13.11.2023 muszą przed publikacją przeprowadzić test zamknięty
z co najmniej 12 testerami, którzy są zapisani nieprzerwanie przez 14 dni
([źródło](https://support.google.com/googleplay/android-developer/answer/14151465)).
Zaplanuj na to minimum 2 tygodnie. Konta organizacji tego wymogu nie mają.

## Krok 4. Utworzenie aplikacji

**Utwórz aplikację** → nazwa „Tetherloom: Orbit Run”, język domyślny, **Gra**, **Bezpłatna**.
Play App Signing jest włączane automatycznie przy pierwszym AAB. Zostaw to: Google
przechowuje klucz aplikacji, a Ty podpisujesz kluczem uploadu z kroku 1.

## Krok 5. Testy wewnętrzne (szybki start, do 100 osób)

**Testowanie → Testy wewnętrzne → Utwórz wersję** → wgraj `app-release.aab` →
dodaj listę adresów e-mail testerów → udostępnij im link.
Wersja jest dostępna zwykle po kilku minutach, bez pełnej weryfikacji Google.

## Krok 6. Test zamknięty (wymagany dla kont osobistych)

**Testowanie → Testy zamknięte** → wgraj ten sam lub nowszy AAB → dodaj min. 12 testerów
(lista e-mail lub Grupa Google) → po 14 dniach złóż wniosek o dostęp do wersji produkcyjnej
w **Panelu** Play Console. Google pyta wtedy o przebieg testów, więc zbieraj opinie testerów.

## Krok 7. Formularze wymagane przed publikacją

W **Panelu → Skonfiguruj aplikację**:

- **Polityka prywatności**: opublikuj `docs/privacy-policy.md` pod publicznym adresem URL
  (np. GitHub Pages) i wklej link. Uzupełnij w niej adres e-mail kontaktowy.
- **Dostęp do aplikacji**: wszystkie funkcje dostępne bez logowania.
- **Reklamy**: nie zawiera reklam.
- **Ocena treści (IARC)**: kwestionariusz; dla tej gry: brak przemocy, czatu, zakupów,
  danych lokalizacji. Oczekiwany wynik: PEGI 3.
- **Grupa docelowa**: wybierz 13+ albo 18+, chyba że świadomie celujesz w dzieci.
  Wybranie grup poniżej 13 lat włącza zasady programu „Families” i dodatkowe wymagania.
- **Bezpieczeństwo danych (Data safety)**: aplikacja nie zbiera i nie udostępnia danych.
  Nie ma uprawnień internetu, analityki ani reklam. Ustawienia i wyniki są tylko na urządzeniu.
  Kopia zapasowa Androida (`allowBackup`) nie jest zbieraniem danych przez dewelopera.
- **Aplikacje rządowe / finansowe / zdrowotne**: nie dotyczy.

## Krok 8. Strona w sklepie

**Rozwój → Strona główna w sklepie**. Treści są w `docs/store-listing/pl.md` i `en.md`.

| Zasób | Wymagania |
|---|---|
| Ikona | 512×512 PNG, 32-bit, do 1 MB |
| Grafika promocyjna | 1024×500 PNG lub JPG, bez przezroczystości |
| Zrzuty ekranu telefonu | min. 2, PNG/JPG, boki 320–3840 px, proporcje max. 2:1 |
| Zrzuty tabletu (zalecane) | 7" i 10", jeśli chcesz lepszą widoczność na tabletach |

Zrzuty ekranu rób z prawdziwej gry (emulator: przycisk aparatu w panelu emulatora).
Grafiki do ikony i grafiki promocyjnej: patrz `docs/grafiki-prompty.md`.

## Krok 9. Produkcja

Po uzyskaniu dostępu: **Produkcja → Utwórz wersję** → wybierz AAB z testów → kraje →
**Wyślij do sprawdzenia**. Pierwsza weryfikacja trwa zwykle od kilku dni do ok. tygodnia.

## Rzeczy do rozważenia przed premierą (nie blokują wysyłki)

- **Ustawienia bez działania**: Muzyka i Efekty dźwiękowe można przełączać, ale gra nie ma
  jeszcze dźwięku. Recenzenci i gracze mogą to uznać za błąd; ukryj je do fazy 2 albo dodaj dźwięk.
- **Przyciski „wkrótce”**: Daily Pattern i Practice pokazują tylko komunikat. To dopuszczalne,
  ale wygląda na niedokończoną grę w recenzjach.
- **Ikona adaptacyjna**: brak `mipmap-anydpi-v26/ic_launcher.xml`. Na wielu launcherach ikona
  będzie w białej ramce (lint: `IconLauncherShape`).
- **Podwójny splash na Android 12+**: system pokazuje własny ekran startowy, potem
  `SplashActivity` pokazuje logo przez 1,4 s. Biblioteka `androidx.core:core-splashscreen`
  pozwala to połączyć w jeden ekran.
