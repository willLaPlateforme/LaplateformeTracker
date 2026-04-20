-- ============================
--   BASE DE DONNÉES TRACKER
-- ============================

-- Supprimer les tables si elles existent déjà
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS users;

-- ============================
--   TABLE USERS (LOGIN)
-- ============================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

-- ============================
--   TABLE STUDENT
-- ============================
CREATE TABLE student (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    grade DOUBLE PRECISION NOT NULL,
    email VARCHAR(255) NULL  -- email optionnel
);

-- ============================
--   UTILISATEUR PAR DÉFAUT
-- ============================
-- username : admin
-- password : admin
INSERT INTO users (username, password_hash)
VALUES ('admin', '$2a$10$7Q0yqjZk9nE2xv0oQj1N0u8u3Yk8p6Q1Z9p7xFJ9Z9x7Q0yqjZk9m');
