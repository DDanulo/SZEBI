
# Nazwa modułu
Moduł Inteligentnego Sterowania i Optymalizacji
[usuńcie wszystkie wpisy w kwadratowych nawiasach! sa to dodatkowe pomocnicze opisy]
[wpisy bez nawiasów są do zastąpienia zawartością]

## Projektanci: 
```
Vladyslav Shpyhariev 253830
Adam Jędrzejek 251537
```
# Dokumentacja techniczna

## Opis funkcjonalny

### Opis przeznaczenia modułu
Moduł ma za zadanie sterować urządzeniami (włącz/wyłącz), dodawać je albo usuwać. Także tworzy harmonogramy (włącz/wyłącz) dla urządzeń o konkretnej porze dnia z możliwością cykliczności.

### Opis możliwości funkcjonalnych modułu
Co realizuje dany moduł, wypunktowanie przypadków uzycia wraz z opisami, trzeba podzielic fragmentami co moze robic dany aktor
Aktor: Mieszkaniec
1) Może wysłać prośbę o dodanie urządzenia ale przed tym musi wpisać nazwę, wybrać typ urządzenia i opcjonalnie wpisać pole powierzchni.
2) Może wysłać prośbę o usunięciu urządzenia.
3) Może wyłączyć urządzenie (tylko do którego ma dostęp).
4) Może włączyć urządzenie (tylko do którego ma dostęp).
5) Może stworzyć harmonogram dla urządzenia, gdzie musi wskazać czas włączenia, czas wyłączenia urządzenia.

Aktorzy: Administrator, Inżynier
1) Mogą dodać urządzenie ale przed tym muszą wpisać nazwę, wybrać typ urządzenia i opcjonalnie wpisać pole powierzchni.
2) Mogą usunąć urządzenie.
3) Mogą wyłączyć urządzenie.
4) Mogą włączyć urządzenie.
5) Mogą stworzyć harmonogram dla urządzenia, gdzie muszą wskazać czas włączenia, czas wyłączenia urządzenia.


### Opis możliwości niefunkcjonalnych modułu
System informuje w czytelny sposób o aktualnym stanie urządzeń (włączone/wyłączone) oraz komunikaty są zrozumiałe i czytelne.
Jest możliwość codziennego powtarzania się harmonogramu.
Pod każdym urządzeniem jest informacja ile zużyło/wyprodukowało energii.
Nazwa urządzenia musi być unikalna, a czas włączenia albo wyłączenia nie może być w przeszłości (w momencie tworzenia harmonogramu).
Data włączenia może być po dacie wyłączenia.

# Diagramy przypadków użycia
[Diagramy przypadków użycia (obejmują wszystkie przypadki użycia!)]
## Nazwa przypadku użycia
Podpis z numeracją (wystarczy diagram 1,2,3...)

Opis diagramu
np.:
Diagram przypadków użycia przedstawia system sklepu internetowego. Aktorem jest Klient, który może przeglądać ofertę, dodawać produkty do koszyka oraz składać zamówienie. Dodatkowym aktorem jest Administrator, odpowiedzialny za zarządzanie produktami i realizację zamówień. Diagram pokazuje podstawowe funkcjonalności systemu oraz interakcje użytkowników z systemem.

[powtórzyć dla każdego diagramu, tak samo nagłówki]

![Diagram przypadków użycia dla aktora Mieszkaniec](https://github.com/DDanulo/IO_IAS_25/blob/DeviceControl-vlad-Adam/doc/inteligentnegoSterowaniaOptymalizacji/img/przypadki%20mieszaniec%20diagram.png)

Diagram 1
Diagram przypadków użycia przedstawia system zarządzania urządzeniami domowymi z perspektywy aktora Mieszkaniec. Mieszkaniec ten może planować harmonogramy pracy sprzętu, definiując czasy włączenia i wyłączenia, a także przeglądać i usuwać istniejące harmonogramy. Mieszkaniec wyświetla listę urządzeń oraz bezpośrednio steruje ich zasilaniem (włączanie/wyłączanie). Diagram obrazuje również proces zgłaszania próśb o dodanie nowych urządzeń (określając ich parametry) lub usunięcie istniejących z systemu.

![Diagram przypadków użycia dla aktorów Administrator i Inżynier](https://github.com/DDanulo/IO_IAS_25/blob/DeviceControl-vlad-Adam/doc/inteligentnegoSterowaniaOptymalizacji/img/Przypadki%20admin%20diagram.png)

Diagram 2
Diagram przedstawia praktycznie te same interakcje, tylko różnica jest taka, żę Inżynier/Administrator nie wysyłają prośbę o dodaniu/usunięciu urządzenia. a mają bezpośredni dostęp do tego.



# Diagramy klas
[diagram(y) klas (obejmują wszystkie klasy)]

Miejsce na diagram

Opis diagramu


![Diagram UML wszystkich klas](https://github.com/DDanulo/IO_IAS_25/blob/DeviceControl-vlad-Adam/doc/inteligentnegoSterowaniaOptymalizacji/img/package.png)
Są na diagramie dwa repozytorium  w których są przechowywane harmonogramy i prośby o usunięciu/dodania urządzeń, dwa serwisy zawierające logikę biznesową dla poszczególnej "dziedziny" (prośby, harmonogramy), 3 kontrolery API dla każdej dziedzimy, dane każdej dziedziny oraz menedżery dla każdego typu urządzeń (wiatraki, agd, panele słoneczne), implementujące interfejs IDeviceAuth, który jest wykorzystywany przez moduł Administracji. ScheduleExecutor: Komponent działający w tle i cyklicznie sprawdza, czy nadszedł czas wykonania zadania.

# Diagramy interakcji
[diagramy interakcji (sekwencji lub komunikacji) dla wybranych przypadków użycia z diagramu(ów) przypadków użycia, dla których zdefiniowano wcześniej scenariusze]

## Scenariusz 1

[do wypełnienia szablon scenariusza]

| Pole | Treść |
| :--- | :--- |
| **Nazwa:** | |
| **Numer:** | |
| **Twórca:** | |
| **Poziom ważności:** | |
| **Typ przypadku użycia:** | |
| **Aktorzy:** | |
| **Krótki opis:** | |
| **Warunki wstępne:** | |
| **Warunki końcowe:** | |
| **Główny przepływ zdarzeń:** | 1. <br> 2. <br> 3. |
| **Alternatywne przepływy zdarzeń:** | 1a. <br> 1b. <br> 3a.|
| **Specjalne wymagania:** | |
| **Notatki i kwestie:** | |

## Diagram interakcji 1

Miejsce na diagram interakcji

Miejsce na podpis

Miejsce na opis diagramu

## Scenariusz 2

| Pole | Treść |
| :--- | :--- |
| **Nazwa:** | |
| **Numer:** | |
| **Twórca:** | |
| **Poziom ważności:** | |
| **Typ przypadku użycia:** | |
| **Aktorzy:** | |
| **Krótki opis:** | |
| **Warunki wstępne:** | |
| **Warunki końcowe:** | |
| **Główny przepływ zdarzeń:** | 1. <br> 2. <br> 3. |
| **Alternatywne przepływy zdarzeń:** | 1a. <br> 1b. <br> 3a.|
| **Specjalne wymagania:** | |
| **Notatki i kwestie:** | |

## Diagram interakcji 2

Miejsce na diagram interakcji

Miejsce na podpis

Miejsce na opis diagramu

# Diagram czynności [minimum 1]

Miejsce na diagram

Miejsce na opis diagramu

# Diagram maszyny stanowej [minimum 1]

Miejsce na diagram

Miejsce na opis diagramu

# Diagram komponentów [z czym dany moduł się łączy (wycinek)]

Miejsce na diagram

Miejsce na opis diagramu

# Diagram pakietów

Miejsce na diagram

Miejsce na opis diagramu

# Diagram przeglądu interakcji

Miejsce na diagram

Miejsce na opis diagramu

# Diagram strukturalny

Miejsce na diagram

Miejsce na opis diagramu

# Diagram harmonogramowania

Miejsce na diagram

Miejsce na opis diagramu

# Dokumentacja użytkownika

## Przypadek użycia 1 - [nazwa]

Instrukcja z zrzutami ekranu jak wygląda GUI (jeśli jest):

I kroki opisane np.
Zaloguj się lub przejdź do sklepu jako gość.
Zrzut ekranu
Przeglądaj ofertę i wybierz interesujący Cię produkt.
Zrzut ekranu
Kliknij na produkt, aby zobaczyć szczegóły.
Zrzut ekranu
Wybierz ilość (oraz wariant, jeśli jest dostępny).
Zrzut ekranu
Kliknij przycisk „Dodaj do koszyka”
Zrzut ekranu
Produkt zostanie dodany do koszyka, który możesz sprawdzić, klikając ikonę koszyka.
Zrzut ekranu

[najwazniejsze przypadki uzycia wybrac ze 2/3 wystarcza]

## Obsługa błędów, sytuacji wyjątkowych
Opisać zastosowane zabezpieczenia i ewentualnie co jesli jakis blad wystapi to mozna zrobic albo np. jak sa wprowadzone dane to jak sa walidowane itp.

## Podsumowanie

[Słowa końcowe jakieś, jak to konfigurowac zarzadzac tym]

