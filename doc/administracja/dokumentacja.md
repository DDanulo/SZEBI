
# Nazwa modułu
Moduł administracyjny

## Projektanci: 
```
Maciej Walczak 251655
Mikita Karabeika
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
diagram 1

Opis diagramu

Diagram przypadków użycia przedstawia system logowania do aplikacji. Aktorem jest Użytkownik niezalogowany, który może zalogować się do systemu, zarejestrować się (tylko jako mieszkaniec) oraz zresetować swoje hasło. Diagram pokazuje sposób, w jaki użytkownik uwierzytelnia się do systemu.

## Przypdaki użycia dla Mieszkańca, Inżyniera oraz Administratora

<img src="img/loggeduser.drawio.png">
diagram 2

Opis diagramu 

Diagram przypadków użycia przedstawia system zarządzania uprawnieniami oraz użytkownikami w aplikacji. Aktorami są Mieszkaniec, Inżynier oraz Administrator, którzy mogą się wylogować, tylko Administrator może dodać, usunąć, aktywować, edytować konta użytkowników. Administrator również może zatwierdzać dodanie lub usuniecie urządzenia systemu przez Mieszkańca. Digram pokazuje, w jaki sposób Administrator zarządza systemem.


# Diagramy klas

<img src="./img/package.png">

Diagram klas przedstawia aplikacje REST, która umożliwia, resetowanie hasła, poprzez wysłanie linku na poczte, tworzenie, usuwanie, edycje użytkowników o różnym dostępie do systemu, tworzenie Tokena JWT oraz filtrowanie wg. niego dostępu do poszczególnych metod, hashowanie haseł, logowanie oraz rejestracje do aplikacji. 

# Diagramy interakcji


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

