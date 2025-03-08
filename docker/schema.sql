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
    user_id INT,
    business_id INT,
    points INT DEFAULT 0,
    wallet_balance INT DEFAULT 0,
    PRIMARY KEY (user_id, business_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (business_id) REFERENCES businesses(id)
);

-- Trigger to create a user_rewards entry for each new user
-- DELIMITER //
-- CREATE TRIGGER after_user_insert
-- AFTER INSERT ON users
-- FOR EACH ROW
-- BEGIN
--     INSERT INTO user_rewards (user_id) VALUES (NEW.id);
-- END;
-- //
-- DELIMITER ;

-- Insert hardcoded businesses
INSERT INTO businesses (name) VALUES ('Pizza Place');
INSERT INTO businesses (name) VALUES ('Hardware Store');
INSERT INTO businesses (name) VALUES ('Antique Shop');

