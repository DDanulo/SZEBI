
# Moduł prognozowania

## Projektanci: 
```
Michał Domagała 251505
Mikołaj Pawłoś 258681
```
# Dokumentacja techniczna

## Opis funkcjonalny

### Opis przeznaczenia modułu
Moduł prognozowania pobiera dane zużycia energii elektrycznej z poprzednich dni wygenerowane przez moduł symulacji, trenuje na ich podstawie algorytm uczenia maszynowego, a następnie generuje prognozy przyszłego zużycia energii elektrycznej w formie średniego zużycia na dzień.

### Opis możliwości funkcjonalnych modułu
Aktorzy czasowi (Interwał prognozy i Interwał treningu), Administrator oraz Inżynier mogą wykonywać następujące czynności:
- Pobierz dane zużycia energii elektrycznej od modułu symulacji — Moduł może pobierać od modułu symulacji dane zużycia energii elektrycznej z poprzednich dni
- Przygotuj dane do treningu modelu uczenia maszynowego — Moduł może odpowiednio przetwarzać pobrane od modułu symulacji dane zużycia energii elektrycznej z poprzednich dni
- Przekaż prognozę zużycia energii elektrycznej do modułu alarmów — Moduł może oferować do odczytu najnowsze prognozy modułowi alarmowania i alertów
- Wygeneruj prognozę zużycia energii elektrycznej — Moduł może prognozować prawdopodobne zużycie energii elektrycznej na kolejne 7 dni automatycznie co określony czas lub po otrzymaniu polecenia od administratora bądź inżyniera
- Zapisz prognozę zużycia energii elektrycznej — Moduł może zapisywać wygenerowane prognozy zużycia energii elektrycznej do bazy danych
- Wytrenuj model uczenia maszynowego — Moduł może automatycznie co określony czas trenować algorytm uczenia maszynowego 
- Sprawdź poprawność prognozy zużycia energii elektrycznej — Moduł może, w przypadku wytworzenia prognozy wybiegającej znacznie poza akceptowalny przedział, ponownie wytrenować algorytm uczenia maszynowego i jeszcze raz wygenerować prognozę

Dodatkowo Administrator i Inżynier mogą jeszcze:
- Odczytaj prognozę zużycia energii elektrycznej — Moduł może odczytywać zapisane wcześniej prognozy
- Wyświetl prognozę zużycia energii elektrycznej — Moduł może wyświetlać prognozę zużycia energii elektrycznej na kolejne 7 dni w czytelnej formie administratorowi i inżynierowi


### Opis możliwości niefunkcjonalnych modułu

- Użyty do prognozowania model uczenia maszynowego to algorytm RandomForest zaimplementowany w używanej przez moduł bibliotece Smile
- Przetwarzanie danych zużycia energii elektrycznej z poprzednich dni polega na agregacji danych zużycia do pełnych godzin
- Najnowsze prognozy zużycia energii elektrycznej mogą zostać wyświetlone w formie wykresu liniowego oraz tabeli
- Można przeglądać poprzednie prognozy zużycia energii elektrycznej podając zakres dat wygenerowania
- Wartość prognozy zużycia energii elektrycznej to średnie dzienne zużycie energii elektrycznej na określony dzień

# Diagramy przypadków użycia

## Okresowe generowanie prognoz zużycia energii elektrycznej oraz trenowanie modelu uczenia maszynowego.

![Diagram przypadków użycia dla aktorów "czasowych"](./img/io_use_case_diagram_time_actors.png)
Diagram 1

Ten diagram przypadków użycia przedstawia automatyczne generowanie prognozy zużycia energii elektrycznej oraz trening modelu uczenia maszynowego.
Głównymi aktorami w tym diagramie są interwał prognozy oraz interwał treningu, które to odpowiadają odpowiednio za częstotliwość generowania prognozy zużycia energii elektrycznej i częstotliwość wykonywania treningu modelu uczenia maszynowego.

## Generowanie i wyświetlanie prognoz zużycia energii elektrycznej.

![Diagram przypadków użycia dla ludzkich aktorów](./img/io_use_case_diagram_actors.png)
Diagram 2

Powyższy diagram przypadków użycia przedstawia generowanie prognozy zużycia energii elektrycznej na żądanie oraz wyświetlanie jej.
Główni aktorzy tego diagramu to Inżynier oraz Administrator. 
Mogą oni wygenerować prognozę, co wiąże się z pobraniem danych zużycia energii elektrycznej z poprzednich dni, przetworzeniem ich i zapisaniem wygenerowanej prognozy zużycia energii elektrycznej.
Gdy podczas sprawdzania poprawności prognozy zużycia energii elektrycznej dojdzie do wykrycia nieprawidłowości, zostanie przeprowadzony trening modelu uczenia maszynowego.
Inna czynności dostępna dla tych aktorów to wyświetlenie prognozy zużycia energii elektrycznej, co wiąże się z odczytaniem poprzednio wygenerowanych i zapisanych prognoz.

# Diagramy klas

![Diagram klas](img/class_diagram.jpg)
Diagram 3

Na przedstawionym diagramie zostały uwidocznione klasy związane z modułem prognozowania
Najistotniejszą klasą w tym module jest klasa **ForecastServiceImpl** implementująca interfejs **ForecastService**.
Steruje ona przepływem danych pomiędzy repozytorium **ForecastRepository**, a algorytmem uczenia maszynowego zdefiniowanym w **ForecasterModel**.
Klasy pomocnicze **UsageRecord** i **Forecast** służą do przechowywania odpowiednio przeszłego i prognozowanego zużycia energii elektrycznej.
**DataPredictionController** zapewnia komunikację modułu prognozowania z warstwą prezentacji.

# Diagramy interakcji
[diagramy interakcji (sekwencji lub komunikacji) dla wybranych przypadków użycia z diagramu(ów) przypadków użycia, dla których zdefiniowano wcześniej scenariusze]

## Scenariusz 1

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

