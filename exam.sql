-- Create the database
CREATE DATABASE IF NOT EXISTS exam;
USE exam;

-- Create the questions table
CREATE TABLE IF NOT EXISTS questions (
    questionID INT(11) AUTO_INCREMENT PRIMARY KEY,
    questionText TEXT COLLATE utf8mb4_general_ci NOT NULL,
    option1 TEXT COLLATE utf8mb4_general_ci NOT NULL,
    option2 TEXT COLLATE utf8mb4_general_ci NOT NULL,
    option3 TEXT COLLATE utf8mb4_general_ci NOT NULL,
    option4 TEXT COLLATE utf8mb4_general_ci NOT NULL,
    correctOption INT(11) NOT NULL
);

-- Create the students table
CREATE TABLE IF NOT EXISTS students (
    regNumber VARCHAR(50) COLLATE utf8mb4_general_ci PRIMARY KEY,
    fullName VARCHAR(100) COLLATE utf8mb4_general_ci NOT NULL,
    phoneNumber VARCHAR(15) COLLATE utf8mb4_general_ci NULL,
    birthDate DATE NULL,
    bloodType VARCHAR(3) COLLATE utf8mb4_general_ci NULL,
    address VARCHAR(255) COLLATE utf8mb4_general_ci NULL,
    password VARCHAR(255) COLLATE utf8mb4_general_ci NULL,
    schoolName VARCHAR(100) COLLATE utf8mb4_general_ci NULL,
    photoPath VARCHAR(255) COLLATE utf8mb4_general_ci NULL,
    score INT(11) NULL
);

-- Create the users table
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) COLLATE utf8mb4_general_ci PRIMARY KEY,
    password VARCHAR(255) COLLATE utf8mb4_general_ci NOT NULL,
    role VARCHAR(20) COLLATE utf8mb4_general_ci NOT NULL
);
