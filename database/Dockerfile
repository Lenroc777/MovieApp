# Pobieramy oficjalny obraz MySQL
FROM mysql:latest

# Ustawiamy zmienne środowiskowe dla hasła root oraz nazwy bazy danych

# Kopiujemy plik init.sql do katalogu /docker-entrypoint-initdb.d/
# To sprawi, że plik zostanie automatycznie wykonany przy pierwszym uruchomieniu kontenera
COPY init.sql /docker-entrypoint-initdb.d/

# Expose port 3306
EXPOSE 3306
