
# Nazwa modułu
Moduł administracyjny

## Projektanci: 
```
Maciej Walczak 251655
Mikita Karabeika 252496
```
# Dokumentacja techniczna

## Opis funkcjonalny

### Opis przeznaczenia modułu
Moduł administracyjny ma za zadanie zarządzać działaniem programu oraz urządzeń podczas użytkowania.

### Opis możliwości funkcjonalnych modułu
Co realizuje dany moduł, wypunktowanie przypadków użycia wraz z opisami, trzeba podzielić fragmentami co może robić dany aktor

## Aktor - Użytkownik niezalogowany

- logowanie użytkowników w systemie.

Użytkownik niezalogowany może zalogować się do systemu przy użyciu login oraz hasła. Po poprawnym uwierzytelnieniu uzyskuje dostęp do funkcjonalności zgodnych z przypisaną rolą. 

- Odzyskiwanie/Zmiana hasła poprzez wysłanie linku do zresetowania hasła poprzez pocztę elektroniczną.

Użytkownik niezalogowany może skorzystać z funkcji odzyskiwania hasła. System wysyła na podany adres e-mail link umożliwiający zresetowanie hasła.

- rejestracja mieszkańca w systemie

Tylko mieszkaniec może samodzielnie stworzyć konto w systemie, które musi być aktywowany przez adminstratora, żeby mieszkaniec mógł się uwierzytelnić.


## Aktor - Administrator

- Zarządzanie kontami użytkowników.

Administrator może:
- tworzyć konta użytkowników (mieszkańców, administratorów, inżynierów),
- edytować konta użytkowników (zmienić hasło oraz informacje o użytkowniku).
- usuwać konta użytkowników,
- aktywować i dezaktywować konta.

- Nadawanie uprawnień do korzystania z urządzeń (przez administratora).

Administrator zatwierdza dodanie lub usunięcie urządzenia przez Mieszkańca.

### Opis możliwości niefunkcjonalnych modułu

- Dane użytkowników będą szyfrowane korzystając z biblioteki BcryptPasswordEncoder.

- Wymóg silnych haseł (minimum 8 znaków, kombinacja małych i wielkich liter, cyfr oraz znaków specjalnych).

- Sesje użytkowników będą wygasać po 15 minutach nieaktywności.

- System informuje użytkownika o błędach logowania w sposób zrozumiały, nie ujawniając szczegółów bezpieczeństwa.

# Diagramy przypadków użycia


## Przypadki użycia dla użytkownika niezalogowanego


<img src="img/notloggeduser.drawio.png">

Diagram 1

Opis diagramu

Diagram przypadków użycia przedstawia system logowania do aplikacji. Aktorem jest Użytkownik niezalogowany, który może zalogować się do systemu, zarejestrować się (tylko jako mieszkaniec) oraz zresetować swoje hasło. Diagram pokazuje sposób, w jaki użytkownik uwierzytelnia się do systemu.

## Przypdaki użycia dla Mieszkańca, Inżyniera oraz Administratora

<img src="img/loggeduser.drawio.png">

Diagram 2

Opis diagramu 

Diagram przypadków użycia przedstawia system zarządzania uprawnieniami oraz użytkownikami w aplikacji. Aktorami są Mieszkaniec, Inżynier oraz Administrator, którzy mogą się wylogować, tylko Administrator może dodać, usunąć, aktywować, edytować konta użytkowników. Administrator również może zatwierdzać dodanie lub usuniecie urządzenia systemu przez Mieszkańca. Digram pokazuje, w jaki sposób Administrator zarządza systemem.


# Diagramy klas

<img src="./img/package.png">

Diagram 3

Diagram klas przedstawia aplikacje REST, która umożliwia, resetowanie hasła, poprzez wysłanie linku na poczte, tworzenie, usuwanie, edycje użytkowników o różnym dostępie do systemu, tworzenie Tokena JWT oraz filtrowanie wg. niego dostępu do poszczególnych metod, hashowanie haseł, logowanie oraz rejestracje do aplikacji. 

# Diagramy interakcji


## Scenariusz 1

[do wypełnienia szablon scenariusza]

| Pole                                | Treść                                                                                                                                                                                                                                                                                                                               |
|:------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Nazwa:**                          | Logowanie użytkownika do systemu                                                                                                                                                                                                                                                                                                    |
| **Numer:**                          | 1                                                                                                                                                                                                                                                                                                                                   |
| **Twórca:**                         | Mikita Karabeika 252496, Maciej Walczak 251655 - projektanci                                                                                                                                                                                                                                                                        |
| **Poziom ważności:**                | Wysoki                                                                                                                                                                                                                                                                                                                              |
| **Typ przypadku użycia:**           | Szczegółowy niezbędny                                                                                                                                                                                                                                                                                                               |
| **Aktorzy:**                        | Użytkownik niezalogowany                                                                                                                                                                                                                                                                                                            |
| **Krótki opis:**                    | Użytkownik loguje się do systemu za pomocą loginu lub e-maila i hasła, a system weryfikuje dane.                                                                                                                                                                                                                                    |
| **Warunki wstępne:**                | 1. Konto użytkownika istnieje w systemie.<br/>2. Konto użytkownika jest aktywne.                                                                                                                                                                                                                                                      |
| **Warunki końcowe:**                | Użytkownik zostaje zalogowany i może korzystać z systemu zgodnie ze swoimi uprawnieniami, lub logowanie nie powiodło się i użytkownik otrzymuje odpowiedni komunikat                                                                                                                                                                |
| **Główny przepływ zdarzeń:**        | 1. Użytkownik wprowadza login/e-mail i hasło.<br> 2. System weryfikuje poprawność danych. <br>3.Jeśli dane są poprawne, generuje unikalne ID sesji. <br> 4. System zapisuje informacje o logowaniu (czas, ID sesji, adres IP).<br> 5. Użytkownik uzyskuje dostęp do swojego pulpitu i modułów systemu, do których ma uprawnienia. |
| **Alternatywne przepływy zdarzeń:** | 1. Jeśli login/e-mail lub hasło są niepoprawne, system wyświetla komunikat o błędzie i zapisuje próbę logowania. <br> 2. Jeśli konto jest dezaktywowane, system blokuje logowanie i wyświetla odpowiedni komunikat.                                                                                                                 |
| **Specjalne wymagania:**            | Hasła muszą być bezpiecznie przechowywane w postaci hash-u. <br> Sesja musi mieć limit czasu bezczynności (20 min). <br> System musi logować wszystkie próby logowania i generować ID sesji                                                                                                                                         |
| **Notatki i kwestie:**              | Scenariusz 1 odpowiada diagramowi sekwencji 1.                                                                                                                                                                                                                                                                                      |

## Diagram interakcji 1

<img src="img/przypadek1.drawio.png">

Diagram 4

Miejsce na opis diagramu

## Scenariusz 2

| Pole | Treść                                                                                                                                                                                                                    |
| :--- |:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Nazwa:** | Dodanie konta inżyniera                                                                                                                                                                                                  |
| **Numer:** | 2                                                                                                                                                                                                                        |
| **Twórca:** | Mikita Karabeika 252496, Maciej Walczak 251655 - projektanci                                                                                                                                                             |
| **Poziom ważności:** | Wysoki                                                                                                                                                                                                                   |
| **Typ przypadku użycia:** | Szczegółowy niezbędny                                                                                                                                                                                                    |
| **Aktorzy:** | Administrator, Inżynier                                                                                                                                                                                                  |
| **Krótki opis:** | Administrator próbuje dodać konto inżyniera, a system weryfikuje wszystkie warunki przed dokonaniem dodania.                                                                                                             |
| **Warunki wstępne:** | 1. Administrator jest zalogowany do systemu. <br> 2. login konta inżyniera jest unikalny w systemie.                                                                                                                     |
| **Warunki końcowe:** | Konto inżyniera jest dodane do systemu, lub operacja jest zablokowana z powodu niespełnienia warunków.                                                                                                                   |
| **Główny przepływ zdarzeń:** | 1. Administrator wypełnia formularz dodania inżyniera. <br> 2. System sprawdza, czy login inżyniera jest unikalny i czy wszystkie pola w formularzu są wypełnione. <br> 3. System dodaje konto inżyniera do bazy danych. |
| **Alternatywne przepływy zdarzeń:** | System blokuje operację, jeżeli login przypisany do konta inżyniera jest nieunikalny.                                                                                                                                    |
| **Specjalne wymagania:** | Operacja powinna być atomowa — w przypadku błędu żadne częściowe dane nie powinny pozostać.                                                                                                                              |
| **Notatki i kwestie:** | Scenariusz 2 odpowiada diagramowi sekwencji 2                                                                                                                                                                            |

## Diagram interakcji 2

<img src="img/przypadek2.drawio.png">

Diagram 5

Miejsce na opis diagramu

# Diagram czynności - Rejestracja 

<img src="./img/dgrczynności.drawio(1).png">

Diagram 6

Miejsce na opis diagramu

# Diagram maszyny stanowej

<img src="./img/stanu.drawio(2).png">

Diagram 7

Miejsce na opis diagramu

# Diagram komponentów

<img src="img/komp.png">

Diagram 8

Miejsce na opis diagramu

# Diagram pakietów

<img src="img/packet.drawio.png">

Diagram 9

Miejsce na opis diagramu

# Diagram przeglądu interakcji

<img src="img/przebieg.drawio.png">

Diagram 10

Miejsce na opis diagramu

# Diagram strukturalny - Dodanie konta inżyniera

<img src="img/strukturalny.drawio.png">

Diagram 11

Miejsce na opis diagramu

# Diagram harmonogramowania - Dodanie konta inżyniera

<img src="img/harmonogram.drawio.png">

Diagram 12

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

