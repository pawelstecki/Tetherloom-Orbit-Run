# Prompty do grafik sklepowych

Model: **Nano Banana 2**. Zalecany wysiłek (effort): **wysoki** dla ikony (musi być czytelna
w 48 px), **średni** dla grafiki promocyjnej.

Paleta gry (z `GameView.java` i `colors.xml`), podaj ją modelowi dosłownie:
tło `#10131F`, mgła `#1B2440`, kotwica cyjan `#72E6E1`, Lumen złoty `#FFD36E`,
akcent fiolet `#A98CFF`.

Po wygenerowaniu: przeskaluj do dokładnych wymiarów, zapisz jako PNG,
sprawdź ikonę w rozmiarze 48×48 px, czy dalej jest czytelna.

## 1. Ikona aplikacji: 512×512 PNG

```
Mobile game app icon, square 1:1, flat vector style with soft glow, no text, no letters.
A small glowing golden orb (#FFD36E) with a short luminous trail is mid-flight, having just
launched off a thin circular orbit ring that surrounds a solid cyan anchor dot (#72E6E1).
The orbit ring is a faint cyan line. Background: deep navy (#10131F) with a subtle radial
gradient to #1B2440 at the center. Minimalist, geometric, high contrast, centered composition
with generous padding (keep all important elements inside the central 66% circle so it
survives circular and squircle masks). No border, no drop shadow outside the square,
no photorealism, no 3D bevel.
```

## 2. Grafika promocyjna (feature graphic): 1024×500 PNG/JPG

```
Wide banner for a mobile game store page, exact aspect ratio 1024:500, no text, no logos.
Minimalist night-sky scene in deep navy (#10131F) fading to #1B2440 with faint violet
(#A98CFF) mist. A vertical path of four or five cyan anchor points (#72E6E1), each with a
thin translucent orbit ring, rises from lower left to upper right. A glowing golden orb
(#FFD36E) with a bright tangential light trail is flying between two anchors, suggesting
upward motion. Flat vector art with soft bloom, clean geometric shapes, calm and premium mood.
Keep the central area visually quiet; Google Play may overlay a play button in the middle.
No UI elements, no characters, no photorealism.
```

## 3. Zrzuty ekranu

Nie generuj ich modelem. Zasady Google Play wymagają, żeby zrzuty pokazywały prawdziwą grę.
Zrób 4–6 zrzutów z emulatora Pixel (pion, 1080×2400): menu główne, lot Lumena między
kotwicami, combo w trakcie, ekran „Run Ended” z nowym rekordem, ekran „How to Play”.
Opcjonalnie możesz dodać ramkę i krótki podpis w narzędziu graficznym.
