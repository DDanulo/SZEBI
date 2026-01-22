
# Moduł analizy danych i raportowania

## Projektanci: 
```
Remigiusz Bartczak 251677,252926
Jan Kozłowski 251562
```
# Dokumentacja techniczna

## Opis funkcjonalny

### Opis przeznaczenia modułu
Moduł pobiera, przetwarza i analizuje, a następnie wizualizuje dane. Dotyczą one zużycia i produkcji energii poszczególnych lub wszystkich urządzeń w budynku na przestrzeni danego okresu w czasie. Dostarcza on Administratorom i Inżynierom niezbędne informacje do monitorowania oraz optymalizacji działania poszczególnych urządzeń.


### Opis możliwości funkcjonalnych modułu
* Generowanie raportu zużycia energii (w kWh) w wybranym zakresie dat dla np. Lodówki.
* Generowanie raportu produkcji energii (w kWh) np. dla paneli słonecznych w wybranym zakresie dat.
* Wybranie rodzaju raportu (liniowy, słupkowy, kołowy) oraz filtrowanie po odpowiednik okresie w czasie, oraz po poszczególnych urządzeniach.
* Zapis dowolnie wygenerowanego raportu analitycznego do pliku w formacie PDF na dysk użytkownika.
* Interfejs udostępniający generowanie oraz pobieranie raportów wraz z odpowiednimi opcjami wybranymi przez użytkownika.

### Opis możliwości niefunkcjonalnych modułu
* Złożone zapytania raportowe (np. roczne zestawienie zużycia wg urządzenia) muszą być przetworzone i zwrócone z bazy danych PostgreSQL w czasie skończonym (raport musi zostać wygenerowany, o ile dane istnieją).
* Wartości zużycia i produkcji energii (w kWh) prezentowane w raporcie muszą być zgodne w 100% z danymi źródłowymi zapisanymi przez Moduł Symulacji.
* W przypadku błędu zapisu pliku PDF system musi wyświetlić dedykowany komunikat o błędzie (np. "Błąd zapisu: ścieżka niedostępna" lub "Brak wolnego miejsca") zamiast ogólnej awarii aplikacji.
* Dostęp do raportów musi być ściśle ograniczony wyłącznie dla ról Administratora oraz Inżyniera. Pozostali użytkownicy nie mają dostępu do tej funkcjonalności.

# Diagramy przypadków użycia

## Diagram 1 - przypadki użycia dla inżyniera

![Diagram przypadków użycia dla inżyniera](img/diagram-przypadkow-uzycia-1_inzynier.png)

Diagram przypadków użycia przedstawia system zarządzania energią w budynkach inteligentnych. Aktorem jest inżynier, który może generować raport zużycia lub produkcji energii, wybrać do niego odpowiedni zakres dat i urządzenia, dla których ma zostać wygenerowany raport. Ma też możliwość zapisu raportu na dysku jako plik PDF.

## Diagram 2 - przypadki użycia dla administratora

![Diagram przypadków użycia dla administratora](img/diagram-przypadkow-uzycia-2_administrator.png)

Diagram przypadków użycia przedstawia system zarządzania energią w budynkach inteligentnych. Aktorem jest administrator, który bardzo podobnie do inżyniera może generować raport zużycia lub produkcji energii, wybrać do niego odpowiedni zakres dat i urządzenia, dla których ma zostać wygenerowany raport. Ma też możliwość zapisu raportu na dysku jako plik PDF.

# Diagramy klas

![Diagram klas](img/diagram-klas.png)

Diagram klas przedstawia architekturę systemu raportowania, którego fundamentem są klasy modelu DataPoint i DeviceInfo oraz definiujący rodzaj pomiaru typ wyliczeniowy DataType.

Za warstwę danych i logiki odpowiadają: DataFetcher, który pobiera surowe informacje z bazy, oraz DataProcessor, który wykonuje na nich obliczenia matematyczne i agregacje. Te dwa komponenty są wykorzystywane przez główny serwis DataProvider (implementujący interfejs IData), który integruje pobieranie z przetwarzaniem.

Całością steruje ReportController, który udostępnia dane poprzez API i – przy wsparciu klasy PdfReportGenerator – generuje gotowe raporty w formacie PDF.

# Diagramy interakcji

## Scenariusz 1

| Pole                                | Treść                                                                                                                                                                                                                                                                       |
|:------------------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Nazwa:**                          | Generowanie raportu zużycia energii                                                                                                                                                                                                                                         |
| **Numer:**                          | 1                                                                                                                                                                                                                                                                           |
| **Twórca:**                         | Remigiusz Bartczak 251677,252926; Jan Kozłowski 251562                                                                                                                                                                                                                      |
| **Poziom ważności:**                | Wysoki                                                                                                                                                                                                                                                                      |
| **Typ przypadku użycia:**           | Ogólny                                                                                                                                                                                                                                                                      |
| **Aktorzy:**                        | Administrator, inżynier                                                                                                                                                                                                                                                     |
| **Krótki opis:**                    | Generowanie raportu zużycia energii dla wybranego okresu w czasie.                                                                                                                                                                                                          |
| **Warunki wstępne:**                | Użytkownik bazodanowy jest zalogowany i posiada uprawnienia (jest administratorem lub inżynierem). W bazie danych PostgreSQL istnieją dane dotyczące zużycia energii w danym okresie czasu z Modułu Symulacji.                                                              |
| **Warunki końcowe:**                | Raport zostaje wygenerowany i wyświetlony.                                                                                                                                                                                                                                  |
| **Główny przepływ zdarzeń:**        | 1. Administrator/inżynier wybiera raport zużycia energii. <br> 2. Zalogowany użytkownik wybiera dany okres czasu. <br> 3. Użytkownik wybiera opcję "Generuj raport" <br/> 4. System pobiera dane z bazy PostgreSQL, przetwarza je. <br/> 5. System wyświetla gotowy raport. |
| **Alternatywne przepływy zdarzeń:** | 5a. Użytkownik pobiera raport jako plik PDF.                                                                                                                                                                                                                                |
| **Specjalne wymagania:**            | -                                                                                                                                                                                                                                                                           |
| **Notatki i kwestie:**              | Scenariusz ten będzie zilustrowany na Diagramie Sekwencji 1.                                                                                                                                                                                                                |

## Diagram interakcji 1

![Diagram interakcji 1](img/diagram-interakcji-1.png)

Diagram 1: Generowanie raportu zużycia energii

Diagram ten ilustruje przepływ danych rozpoczynający się od żądania użytkownika w ReportController. Proces rozpoczyna się od wspólnego kroku pobrania surowych danych z bazy PostgreSQL za pośrednictwem DataFetcher. Następnie zastosowano blok alternatywny (alt), który rozdziela logikę na dwie ścieżki:

Wyświetlenie wykresu: Dane trafiają do DataProcessor w celu agregacji (np. miesięcznej), a wynik jest zwracany jako JSON.

Pobranie PDF: Surowe dane są przekazywane do PdfReportGenerator, który tworzy i zwraca plik dokumentu.

## Scenariusz 2

| Pole                                | Treść                                                                                                                                                                                                                                                                             |
|:------------------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Nazwa:**                          | Generowanie raportu produkcji energii                                                                                                                                                                                                                                             |
| **Numer:**                          | 2                                                                                                                                                                                                                                                                                 |
| **Twórca:**                         | Remigiusz Bartczak 251677,252926; Jan Kozłowski 251562                                                                                                                                                                                                                            |
| **Poziom ważności:**                | Wysoki                                                                                                                                                                                                                                                                            |
| **Typ przypadku użycia:**           | Ogólny                                                                                                                                                                                                                                                                            |
| **Aktorzy:**                        | Administrator, inżynier                                                                                                                                                                                                                                                           |
| **Krótki opis:**                    | Generowanie raportu produkcji energii dla wybranych urządzeń.                                                                                                                                                                                                                     |
| **Warunki wstępne:**                | Użytkownik bazodanowy jest zalogowany i posiada uprawnienia (jest administratorem lub inżynierem). W bazie danych PostgreSQL istnieją dane dotyczące produkcji energii dla danych urządzeń z Modułu Symulacji.                                                                    |
| **Warunki końcowe:**                | Raport zostaje wygenerowany i wyświetlony.                                                                                                                                                                                                                                        |
| **Główny przepływ zdarzeń:**        | 1. Administrator/inżynier wybiera raport produkcji energii. <br> 2. Zalogowany użytkownik wybiera konkretne urządzenie. <br> 3. Użytkownik wybiera opcję "Generuj raport" <br/> 4. System pobiera dane z bazy PostgreSQL, przetwarza je. <br/> 5. System wyświetla gotowy raport. |
| **Alternatywne przepływy zdarzeń:** | 2a. Zalogowany użytkownik wybiera "Wszystkie urządzenia"                                                                                                                                                                                                                          |
| **Specjalne wymagania:**            | -                                                                                                                                                                                                                                                                                 |
| **Notatki i kwestie:**              | Scenariusz ten będzie zilustrowany na Diagramie Sekwencji 2.                                                                                                                                                                                                                      |

## Diagram interakcji 2

![Diagram interakcji 2](img/diagram-interakcji-2.png)

Diagram 2: Generowanie raportu produkcji energii

Ten diagram koncentruje się na różnicach w sposobie pozyskiwania danych w zależności od kryteriów wyboru urządzenia. Blok alternatywny (alt) występuje tutaj na samym początku interakcji z warstwą danych:

Konkretne urządzenie: System wywołuje metodę queryMeasure w DataFetcher, filtrując wyniki po ID urządzenia.

Wszystkie urządzenia: System używa ogólnej metody fetchData, pobierając pełen zakres danych. Niezależnie od wybranej ścieżki, uzyskana lista jest następnie przetwarzana przez DataProcessor (sumowanie, agregacja dzienna) przed finalną prezentacją użytkownikowi.

# Diagram czynności 1 [minimum 1]

![Diagram czynnosci 1](img/diagram-czynnosci-pdf.png)

Miejsce na opis diagramu

# Diagram czynności 2 [minimum 1]

![Diagram czynnosci 2](img/diagram-czynnosci-przebieg-analizy-danych-i-agregacji.png)

Miejsce na opis diagramu

# Diagram maszyny stanowej [minimum 1]

![Diagram maszyny stanowej](img/diagram-maszyny-stanowej.png)

Miejsce na opis diagramu

# Diagram komponentów

![Diagram komponetów](img/diagram-komponentow.png)

Moduł analizy danych odczytuje dane historyczne (zużycie, produkcja) zapisane przez Moduł Symulacji w bazie dancyh PostgreSQL. Stanowi to surowiec do wszelkich analiz, wykresów i raportów.
Udostępnia on również bieżące, przetworzone i zweryfikowane dane (np. zużycie z ostatniego tygodnia), umożliwiając Modułowi Alarmowania i Alertów szybkie wykrywanie anomalii i awarii (np. nagłe skoki zużycia energii przez dane urządzenie).

# Diagram pakietów

![Diagram pakietów](img/diagram-pakietow.png)

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

