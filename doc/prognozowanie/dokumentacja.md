
# Moduł prognozowania

## Projektanci: 
```
Michał Domagała 251505
Mikołaj Pawłoś 258681
```
# Dokumentacja techniczna

## Opis funkcjonalny

### Opis przeznaczenia modułu
Moduł prognozowania zbiera dane zużycia energii elektrycznej z poprzednich dni, trenuje na ich podstawie algorytm uczenia maszynowego, a następnie generuje prognozy przyszłego zużycia energii elektrycznej w formie średniego zużycia na dzień.

### Opis możliwości funkcjonalnych modułu
"Co realizuje dany moduł, wypunktowanie przypadków użycia wraz z opisami, trzeba podzielic fragmentami co moze robic dany aktor"

- Pobierz dane zużycia energii elektrycznej od modułu symulacji — Moduł może pobierać od modułu symulacji dane zużycia energii elektrycznej z poprzednich dni
- Przygotuj dane do treningu modelu uczenia maszynowego — Moduł może odpowiednio przetwarzać pobrane od modułu symulacji dane zużycia energii elektrycznej z poprzednich dni
- Wygeneruj prognozę zużycia energii elektrycznej — Moduł może prognozować prawdopodobne zużycie energii elektrycznej na kolejne 7 dni automatycznie co określony czas lub po otrzymaniu polecenia od administratora bądź inżyniera 
- Wyświetl prognozę zużycia energii elektrycznej — Moduł może wyświetlać prognozę zużycia energii elektrycznej na kolejne 7 dni w czytelnej formie administratorowi i inżynierowi
- Zapisz prognozę zużycia energii elektrycznej — Moduł może zapisywać wygenerowane prognozy zużycia energii elektrycznej
- Wytrenuj model uczenia maszynowego — Moduł może automatycznie co określony czas trenować algorytm uczenia maszynowego 
- Sprawdź poprawność prognozy zużycia energii elektrycznej — Moduł może, w przypadku wytworzenia prognozy wybiegającej znacznie poza akceptowalny przedział, ponownie wytrenować algorytm uczenia maszynowego i jeszcze raz wygenerować prognozę
- Odczytaj prognozę energii elektrycznej — Moduł może odczytywać zapisane wcześniej prognozy
- Przekaż prognozę zużycia energii elektrycznej do modułu alarmów — Moduł może oferować do odczytu najnowsze prognozy modułowi alarmowania i alertów


### Opis możliwości niefunkcjonalnych modułu

- Użyty do prognozowania model uczenia maszynowego to algorytm RandomForest zaimplementowany w używanej przez moduł bibliotece Smile
- Przetwarzanie danych zużycia energii elektrycznej z poprzednich dni polega na agregacji danych zużycia do pełnych godzin
- Najnowsze prognozy zużycia energii elektrycznej mogą zostać wyświetlone w formie wykresu liniowego oraz tabeli
- Wartość prognozy zużycia energii elektrycznej to średnie dzienne zużycie energii elektrycznej na określony dzień

# Diagramy przypadków użycia
[Diagramy przypadków użycia (obejmują wszystkie przypadki użycia!)]
## Nazwa przypadku użycia

Tutaj miejsce na diagram

Podpis z numeracją (wystarczy diagram 1,2,3...)

Opis diagramu

np.:
Diagram przypadków użycia przedstawia system sklepu internetowego. Aktorem jest Klient, który może przeglądać ofertę, dodawać produkty do koszyka oraz składać zamówienie. Dodatkowym aktorem jest Administrator, odpowiedzialny za zarządzanie produktami i realizację zamówień. Diagram pokazuje podstawowe funkcjonalności systemu oraz interakcje użytkowników z systemem.

[powtórzyć dla każdego diagramu, tak samo nagłówki]

# Diagramy klas
[diagram(y) klas (obejmują wszystkie klasy)]

Miejsce na diagram

Opis diagramu

# Diagramy interakcji
[diagramy interakcji (sekwencji lub komunikacji) dla wybranych przypadków użycia z diagramu(ów) przypadków użycia, dla których zdefiniowano wcześniej scenariusze]

## Scenariusz 1

[do wypełnienia szablon scenariusza]

| Pole | Treść                                                                                                                                                                                                                                                         |
| :--- |:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Nazwa:** | Generowanie prognozy zużycia energii elektrycznej                                                                                                                                                                                                             |
| **Numer:** | 1                                                                                                                                                                                                                                                             |
| **Twórca:** | Projektanci: Michał Domagała 251505, Mikołaj Pawłoś 258681                                                                                                                                                                                                    |
| **Poziom ważności:** | Wysoki                                                                                                                                                                                                                                                        |
| **Typ przypadku użycia:** | Istotny                                                                                                                                                                                                                                                       |
| **Aktorzy:** | Interwał prognozy                                                                                                                                                                                                                                             |
| **Krótki opis:** | Wygenerowanie prognozy na podstawie danych zużycia energii elektrycznej z poprzednich dni                                                                                                                                                                     |
| **Warunki wstępne:** | 1. Model uczenia maszynowego jest wytrenowany <br> 2. Istnieją dane zużycia energii elektrycznej sprzed przynajmniej 7 dni                                                                                                                                    |
| **Warunki końcowe:** | System zapisuje wygenerowaną prognozę zużycia energii elektrycznej                                                                                                                                                                                            |
| **Główny przepływ zdarzeń:** | 1. Pobranie danych zużycia energii elektrycznej <br> 2. Przetworzenie danych zużycia energii elektrycznej 3. Wygenerowanie prognozy zużycia energii elektrycznej na następne 7 dni <br> 4. Sprawdza poprawność prognozy zużycia energii elektrycznej <br> 5.  |
| **Alternatywne przepływy zdarzeń:** | 1a. <br> 1b. <br> 3a.                                                                                                                                                                                                                                         |
| **Specjalne wymagania:** |                                                                                                                                                                                                                                                               |
| **Notatki i kwestie:** |                                                                                                                                                                                                                                                               |

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

