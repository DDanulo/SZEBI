# IO_IAS_25
## Struktura projektu
projekt składa się z dwóch części - server oraz client.
### Server
Technologia główna: Spring boot\
Baza danych: PostgreSQL 17\
Technologie pomocnicze: 
  - docker-compose: uruchamia bazę danych
  - flyway: kontrola migracji baz danych
  - ...

Wewnątrz utworzonego projektu Spring boot zostały utworzone katalogi za ścieżką `server/src/main/java/com/example/server` odpowiadające nazwom modułów projektu.
### Client
Technologia główna: ReactJS\
Technologie pomocnicze:
  - Tailwind: CSS z predefiniowanymi klasami, ułatwiającymi pisanie
  - ...

Wewnątrz utworzonego projektu Spring boot zostały utworzone katalogi za ścieżką `client/src/components/` odpowiadające nazwom modułów projektu.
## Jak zacząć?
Żeby zacząć pracę nad projektem, sklonujcie to repozytorium i znajdżcie katalog z nazwą swojego modułu wewnątrz katalogów server oraz client odpowiadających za swoje moduły.\
\
Uruchomić bazę danych można za pomocą polecenia `docker compose up -d`. (Trzeba mieć zainstalowany docker na maszynie)\
\
Dla pierwszego uruchomienia front-endu powinniście posiadać na maszynie [node.js i npm](https://nodejs.org/en/download/) i zainstalować potrzebne biblioteki npm za pomocą polecenia `npm install` wewnątrz katalogu `client`.\
\
Uruchomić front-end można za pomocą polecenia `npm run dev`.\
\
Żeby wprowadzać zmiany do bazy danych, **powinniście** stosować flyway, krótki tutorial do tej technologii znajdziecie [tutaj](https://www.youtube.com/watch?v=AMopB9C2bH8) (serio lepszego nie znalazłem XD). Ale polecam zapoznać się z nią też samodzielnie.\
\
Także proszę stosować podział na gałęzie w github dla ulatwienia pracy dla wszystkich.
