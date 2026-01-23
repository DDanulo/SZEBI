
# Nazwa modułu
[usuńcie wszystkie wpisy w kwadratowych nawiasach! sa to dodatkowe pomocnicze opisy]
[wpisy bez nawiasów są do zastąpienia zawartością]

## Projektanci: 
```
imie, nazwisko numer indeksu
imie, nazwisko numer indeksu
```
# Dokumentacja techniczna

## Opis funkcjonalny

### Opis przeznaczenia modułu
Celem modułu jest generowanie danych dotyczących produkcji energii przez odnawiane źródła oraz zużycia energii przez sprzęt.

### Opis możliwości funkcjonalnych modułu
Administrator:
* Ma możliwość pobrania aktualnych parametrów symulacji.
* Ma możliwość zmiany pory roku oraz dnia.

Moduł Zarządzania:
* Ma możliwość pobrania danych o wszytskich urządzeniach lub tylko o konkretnych.
* Ma możliwość dodawania, usuwania, włączania oraz wyłączania wszystkich urządzeń.

### Opis możliwości niefunkcjonalnych modułu
* Dane będą generowane w czasie rzeczywistym co T = 5 min.
* Generowanie danych dotyczących zużycia i generowania energii(w kWh) na podstawie czynników takich jak pora dnia, pora roku, warunki atmosferyczne.
* Zapis wygenerowanych danych do bazy danych PostgreSQL.
* Możliwość zmiany ustawień symulacji (pora roku, pora dnia) musi być ściśle ograniczona i dostępna wyłączeni dla roli Administrator.
* Możliwość zmiany ustawień, dodawania i usuwania urządzeń musi być ściśle ograniczona i udostępniona tylko dla modułu zarządzania poprzez odpowiedni interface.

# Diagramy przypadków użycia

<img src="img/diagram_przypadkow_uzycia.png">

Diagram 1

Dirgram przypadków użycia przedstawia moduł symulacji. Aktorami są użtkownika z rolą Administrator, który może zmieniać porę roku i dnia i pobierać aktualne parametry, oraz Moduł Zarządzania, który może dodawać, usuwać, włączać, wyłączać wszystkoe urządzenia oraz pobierać wszystkie urządzenia lub listę konkretnych urządzeń.

[powtórzyć dla każdego diagramu, tak samo nagłówki]

# Diagramy klas
[diagram(y) klas (obejmują wszystkie klasy)]

![img.png](img/klas-1.png)

Diagram X.Y.Z Pierwsza część diagramu klas

![img.png](img/klas-2.png)
Diagram X.Y.Z Pierwsza część diagramu klas



# Diagramy interakcji
[diagramy interakcji (sekDiagram X.Y.Z Piersza część diagramu klasencji lub komunikacji) dla wybranych przypadków użycia z diagramu(ów) przypadków użycia, dla których zdefiniowano wcześniej scenariusze]

## Scenariusz 1

[do wypełnienia szablon scenariusza]

| Pole                                | Treść                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|:------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Nazwa:**                          | Zmień porę roku                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| **Numer:**                          | 1                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| **Twórca:**                         | Jędrzej Bartoszewski 251482, Kacper Maziarz 251586                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| **Poziom ważności:**                | średni                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| **Typ przypadku użycia:**           | szczegółoy, przeciętnie istotny                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| **Aktorzy:**                        | Administrator                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| **Krótki opis:**                    | Zmiana pory roku przez aktora powodująca zmianę w parametrach symulacji.                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| **Warunki wstępne:**                | Aktor jest uwierzytelniony w systemie oraz posiada odpowiedni poziom dostępu (Administrator).                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| **Warunki końcowe:**                | Poprawnie dokonano zmiany pory roku, nowa pora roku wraz z pozostałymi, zaktualizowanymi parametrami symulacji są widoczne w GUI.                                                                                                                                                                                                                                                                                                                                                                                               |
| **Główny przepływ zdarzeń:**        | 1. Aktor dokonuje wyboru nowej pory roku w GUI i zatwierdza wybór.<br/> 2. System przyjmuje żądanie HTTP oraz dokonuje walidacji otrzymanych danych (nazwy pory roku). <br> 3. Dokonana zostaje zmiana pory roku w obiekcie symulacji na nową wartość. <br/> 4. Licznik odpowiadający za zliczanie iteracji i zmianę pory roku zostaje wyzerowany. <br/> 5. Aktor otrzymuje kod odpowiedzi 200. <br/>6. GUI wysyła żądanie pobrania aktualnych parametrów symulacji. <br/>7. System zwraca aktualne parametry symulacji do GUI. |
| **Alternatywne przepływy zdarzeń:** | 3a. System wychwytuje niepoprawne dane. <br/> 3b. System zwraca kod błędu 400.                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| **Specjalne wymagania:**            | nie dotyczy                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| **Notatki i kwestie:**              | Zmiana pory roku nie wpływa na harmonogram generowania zużycia/produkcji energii - nowe parametry będą uwzględnione przy najbliższej iteracji.                                                                                                                                                                                                                                                                                                                                                                                  |

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

<img src="img/diagram_czynnosci.png">

Diagram 5

Diagram przedstawia kolejne czynności będące częścią cyklu generowania danych produkcji i zużycia energii oraz zapisania ich do bazy danych. Czynnikiem rozpoczynającym cały przebieg jest w tym wypadku określony odstęp czasu, który musi upłynąć między kolejnymi cyklami (5 minut).

# Diagram maszyny stanowej [minimum 1]

Miejsce na diagram

Miejsce na opis diagramu

# Diagram komponentów [z czym dany moduł się łączy (wycinek)]

<img src="img/diagram_komponentow.png">

Diagram 7

Diagram komponentów przedstawia powiązania z 3 innymi modułami (zarządzania, alalizy danych, predykcji) oraz interfejsy, za pomocą których te powiązania są realizowane.

# Diagram pakietów

<img src="img/diagram_pakietow.png">

Diagram 8

Diagram pakietów przedstawia jakie pakiety zawiera pakiet Symulacji.

# Diagram przeglądu interakcji

Miejsce na diagram

Miejsce na opis diagramu

# Diagram strukturalny

<img src="img/diagram_strukturalny.png">

Diagram 10

Diagram pokazuje powiązanie między obeiktami uczestniczącymi w procesie generowania danych o produkcji i zużyciu energii.

# Diagram harmonogramowania

<img src="img/diagram_harmonogramowania.png">

Diagram 11

Diagram przedstawia szacukowy czas w jakim przebiegają kolejne etapy symulowania produkcji oraz zużycia energii.

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
Dzięki ograniczeniu możliwości wyboru pór roku i dnia do listy konkretnych opcji, zamiast wpisywania dowolnej wartości, nie ma możliwośći wystąpienia błędów w tym zakresie.


## Podsumowanie

Zarządzanie modułem symulacji sprowadza się do nadzorowania generowanych danych i ewentualnych zmian parametrów(pory roku i dnia) w celu zmiany zmiany wyliczanych wartości.
Należy również kontrolować stan bazy danych gdzyż ilość generowanych danych przy braku kontroli może doprowadzić do przepełnienia. W takim wypadku może być potrzebne wyczyszczenie lub zwiększenie zasobów pamięci przydzielonych dla naszej bazy.
