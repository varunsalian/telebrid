DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS authentication;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS token;

CREATE TABLE authentication (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  device_code VARCHAR(250) NOT NULL,
  user_code VARCHAR(250) NOT NULL,
  time_interval INT,
  expires_in INT,
  verification_url VARCHAR(250)
);

CREATE TABLE client (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  client_id VARCHAR(250) NOT NULL,
  client_secret VARCHAR(250) NOT NULL
);

CREATE TABLE token (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  access_token VARCHAR(250) NOT NULL,
  expires_in VARCHAR(250),
  token_type VARCHAR(250),
  refresh_token VARCHAR(250)
);

CREATE TABLE user (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  user_name VARCHAR(250) NOT NULL,
  telegram_id VARCHAR(250) NOT NULL,
  authentication_id INT,
  client_id INT,
  token_id INT,
  FOREIGN KEY (authentication_id) REFERENCES authentication(id),
  FOREIGN KEY (client_id) REFERENCES client(id),
  FOREIGN KEY (token_id) REFERENCES token(id)
);