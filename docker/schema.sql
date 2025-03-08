-- Create the database
CREATE DATABASE IF NOT EXISTS nsa_db;

-- Use the newly created database
USE nsa_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE businesses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE user_rewards (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    business_id INT,
    points INT DEFAULT 0,
    wallet_balance INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (business_id) REFERENCES businesses(id)
);

-- Add sample entries for verification

