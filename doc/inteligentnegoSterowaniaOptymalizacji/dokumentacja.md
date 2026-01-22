
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

![Diagram przypadków użycia dla aktora Mieszkaniec](img/Diagram_przypadkiużycia_mieszkaniec.png)
Diagram przypadków użycia przedstawia system zarządzania urządzeniami domowymi z perspektywy aktora Mieszkaniec. Mieszkaniec ten może planować harmonogramy pracy sprzętu, definiując czasy włączenia i wyłączenia, a także przeglądać i usuwać istniejące harmonogramy. Mieszkaniec wyświetla listę urządzeń oraz bezpośrednio steruje ich zasilaniem (włączanie/wyłączanie). Diagram obrazuje również proces zgłaszania próśb o dodanie nowych urządzeń (określając ich parametry) lub usunięcie istniejących z systemu.

![Diagram przypadków użycia dla aktorów Administrator i Inżynier](img/Diagram_przypadkiużycia_admin.png)
Diagram przedstawia praktycznie te same interakcje, tylko różnica jest taka, żę Inżynier/Administrator nie wysyłają prośbę o dodaniu/usunięciu urządzenia. a mają bezpośredni dostęp do tego.



# Diagramy klas
[diagram(y) klas (obejmują wszystkie klasy)]

Miejsce na diagram

Opis diagramu


![Diagram klas](img/Diagram_klas.png)


# Diagramy interakcji
[diagramy interakcji (sekwencji lub komunikacji) dla wybranych przypadków użycia z diagramu(ów) przypadków użycia, dla których zdefiniowano wcześniej scenariusze]

## Scenariusz 1

[do wypełnienia szablon scenariusza]

| Pole | Treść                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| :--- |:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Nazwa:** | Włącz urządzenie                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| **Numer:** | 1                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| **Twórca:** | Vladyslav Shpyhariev, Adam Jędrzejek                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| **Poziom ważności:** | Średni                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| **Typ przypadku użycia:** | Podstawowy                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| **Aktorzy:** | Mieszkaniec                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| **Krótki opis:** | Wyłączone urządzenie mieszkaniec włączy                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| **Warunki wstępne:** | Mieszkaniec jest zalogowany na konto, ma aktualny token,Mieszkaniec ma dostęp do tego urządzenia, urządzenie jest wyłączone                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| **Warunki końcowe:** | Urządzenie jest włączone                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| **Główny przepływ zdarzeń:** | 1.Użytkownik wybiera wyłączone urządzenie z listy w panelu sterowania i klika przycisk "ON".<br> 2. Aplikacja frontendowa wysyła żądanie do serwera, który deleguje zadanie do odpowiedniego menedżera sprzętu. <br> 3. System wysyła sygnał sterujący do modułu symulacji, aby fizycznie uruchomić urządzenie. <br> 4.Po potwierdzeniu włączenia przez symulację, serwer aktualizuje status urządzenia w bazie danych na aktywny. <br> 5.Backend zwraca do aplikacji potwierdzenie pomyślnego wykonania operacji. <br> 6. Interfejs użytkownika automatycznie odświeża listę, wyświetlając urządzenie jako włączone. |
| **Alternatywne przepływy zdarzeń:** | brak                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| **Specjalne wymagania:** | Mieszkaniec ma dostęp do tego urządzenia, urządzenie jest wyłączone                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| **Notatki i kwestie:** | brak                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |

## Diagram interakcji 1

![Diagram Sekwencji włączenia urządzenia](img/Diagram_Sekwencji_1.png)
Diagram Sekwencji nr. 1

Diagram sekwencji przedstawia proces aktywacji urządzenia, w którym żądanie aktora Mieszkaniec z warstwy frontendowej jest przetwarzane przez kontroler i delegowane do odpowiedniego menedżera sprzętu. System realizuje operację dwutorowo, aktualizując status w bazie danych oraz wysyłając fizyczny sygnał do API symulacji, a następnie odświeża widok aplikacji po potwierdzeniu sukcesu.

## Scenariusz 2

| Pole | Treść                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| :--- |:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Nazwa:** | Dodaj urządzenie                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| **Numer:** | 2                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| **Twórca:** | Vladyslav Shpyhariev, Adam Jędrzejek                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| **Poziom ważności:** | Wysoki                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| **Typ przypadku użycia:** | Podstawowy                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| **Aktorzy:** | Administrator                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| **Krótki opis:** | Administrator dodaje nowe urządzenie typu AGD                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| **Warunki wstępne:** | Administator jest zalogowany, ma aktualny token                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| **Warunki końcowe:** | Urządzenie zostanie dodane                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| **Główny przepływ zdarzeń:** | 1. Administrator wpisuje nazwę urządzenia (np. "Pralka").<br> 2.. Administrator wybiera typ "AGD (Zużywa)". <br> 3. Administrator wpisuje powierzchnię (m²) > 0. i klika przycisk Dodaj urządzenie <br> 4. System (FE) sprawdza poprawność znaków w nazwie (tylko litery, cyfry, spacje). <br> 5. System (FE)  sprawdza, czy nazwa nie jest duplikatem na liście pobranej z API. <br> 6.System (FE) sprawdza, czy powierzchnia jest liczbą dodatnią. <br> 7. System (FE) wysyła żądanie POST /api/devices. <br> 8. System (BE) weryfikuje dane i tworzy urządzenie w symulacji. <br> 9. System (FE) wyświetla komunikat "Urządzenie dodane pomyślnie!" i odświeża listę urządzeń. | 
| **Alternatywne przepływy zdarzeń:** | 4a. Administrator wpisał znaki specjale (np. "Pralka$"). Frontend przerywa proces i wyświetla alert("Nazwa zawiera niedozwolone znaki!"). <br> 5a. Administrator wpisał nazwę urządzenia która jest duplikatem i  już istnieje na liście. Frontend przerywa proces i wyświetla alert("Urządzenie o takiej nazwie już istnieje!..."). <br> 6a. Administrator wpisał "0" lub "-5". Frontend przerywa proces i wyświetla alert("Powierzchnia musi być większa od 0.").                                                                                                                                                                                                               |
| **Specjalne wymagania:** | brak                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| **Notatki i kwestie:** | Skoro Administrator dodaje urządzenie typu AGD to system waliduje powierzchnię, gdyby dodawał urządzenie typu Wiatrak to ten krok byłby pominięty                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |

## Diagram interakcji 2

![Diagram Sekwencji dla aktora Administrator](img/Diagram_Sekwencji_2.png)

Diagram Sekwencji nr.2

Diagram przedstawia proces dodawania urządzenia przez Administratora, który rozpoczyna się od rygorystycznej walidacji danych po stronie interfejsu (sprawdzenie znaków specjalnych, unikalności nazwy oraz poprawności wartości liczbowych). 

Dopiero po pomyślnej weryfikacji frontend wysyła żądanie do serwera, gdzie kontroler deleguje zadanie utworzenia obiektu do odpowiedniego menedżera, inicjalizując sprzęt w symulacji i aktualizując bazę danych. Całość kończy się zwrotem potwierdzenia do aplikacji i automatycznym odświeżeniem listy urządzeń dla Administratora.


# Diagram czynności 

![Diagam Czynności dodawania urządzenia](img/Diagram_Czynności.png)
Diagram przedstawia sekwencyjny proces walidacji danych podczas dodawania urządzenia przez Administrtora, w którym niespełnienie któregokolwiek z warunków (poprawność znaków, unikalność nazwy, dodatnia powierzchnia) lub błąd serwera prowadzi do wyświetlenia odpowiedniego komunikatu i natychmiastowego zakończenia przepływu. 

Dopiero pozytywna weryfikacja na wszystkich krokach pozwala na pomyślne zakończenie operacji dodania urządzenia.

# Diagram maszyny stanowej [minimum 1]

![Diagram Maszyny Stanowej wysłanie prośby o dodaniu urządzenia](img/Diagram_Maszyny_Stanowej.png)

Diagram przedstawia cykl życia prośby o dodaniu urządzenia. Prośba rozpoczyna się w stanie oczekiwania, z którego pod wpływem decyzji administratora przechodzi do stanu zatwierdzenia (skutkującego zmianami w systemie) lub odrzucenia, co kończy jego proces przetwarzania.

# Diagram komponentów
![Diagram Komponentów](img/Diagram_Komponentów.png)

Nasz moduł komunikuje się z:

● Modułem Symulacji: Pobieranie danych o urządzeniach
symulacji.

● Modułem Administracji: Pobieranie uprawnień
użytkowników.

# Diagram pakietów

![Diagram Pakietów](img/Diagram_Pakietów.png)

Przedstawiony diagram pakietów ilustruje modułową architekturę systemu, podzieloną na główną przestrzeń naszego modułu(DeviceControl) odpowiedzialną za logikę biznesową oraz część modułu symulacji(Simulation) obsługującą interfejsy urządzeń.

Wewnątrz głównego modułu widoczny jest podział na pakiety kontrolerów, serwisów i repozytoriów, współpracujących z dedykowanymi pakietami zarządczymi (managers) oraz pakietem zadań w tle (executor). Strzałki zależności ukazują przepływ danych i sterowania, w którym logika aplikacji wykorzystuje niższe warstwy oraz zewnętrzne API symulacji do zarządzania stanem urządzeń.

# Diagram przeglądu interakcji

![Diagram Przeglądu Interakcji](img/Diagram_Przeglądu_Interakcji.png)
Diagram przeglądu interakcji ilustruje przepływ sterowania procesem dodawania urządzenia, w którym złożone sekwencje interakcji zostały zastąpione czytelnymi odnośnikami (ref) do osobnych diagramów.


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

