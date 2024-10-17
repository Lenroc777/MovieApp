-- Tworzenie bazy danych
CREATE DATABASE IF NOT EXISTS MovieApp;
USE MovieApp;

-- Tabela dla użytkowników
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    isAdmin BOOLEAN DEFAULT FALSE,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela dla języków
CREATE TABLE Languages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Tabela dla kategorii
CREATE TABLE Categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);
-- Tabela dla filmów
CREATE TABLE Movies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    releaseDate DATE,
    adminId INT,
    languageId INT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (adminId) REFERENCES Users(id),
    FOREIGN KEY (languageId) REFERENCES Languages(id)
);



-- Tabela powiązań film-kategoria (wiele do wielu)
CREATE TABLE MovieCategories (
    movieId INT,
    categoryId INT,
    PRIMARY KEY (movieId, categoryId),
    FOREIGN KEY (movieId) REFERENCES Movies(id),
    FOREIGN KEY (categoryId) REFERENCES Categories(id)
);


-- Tabela dla opinii użytkowników o filmach
CREATE TABLE Reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userId INT,
    movieId INT,
    rating INT CHECK (rating >= 1 AND rating <= 10),
    comment TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES Users(id),
    FOREIGN KEY (movieId) REFERENCES Movies(id)
);

-- Tabela dla filmów oznaczonych jako obejrzane przez użytkowników
CREATE TABLE WatchedMovies (
    userId INT,
    movieId INT,
    watchedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (userId, movieId),
    FOREIGN KEY (userId) REFERENCES Users(id),
    FOREIGN KEY (movieId) REFERENCES Movies(id)
);
