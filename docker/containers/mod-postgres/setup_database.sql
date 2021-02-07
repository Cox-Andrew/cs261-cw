\c postgres postgres
DROP DATABASE IF EXISTS mood;
DROP USER IF EXISTS mooduser;
CREATE USER mooduser WITH PASSWORD 'password';
CREATE DATABASE mood;
GRANT ALL PRIVILEGES ON DATABASE mood TO mooduser;
\c mood mooduser


DROP TABLE IF EXISTS USERS CASCADE;

CREATE TABLE USERS (
  UserID INTEGER,
  FName VARCHAR(30) NOT NULL,
  LName VARCHAR(30) NOT NULL,
  PRIMARY KEY(UserID)
);
-- define a sequence to generate user ids
CREATE SEQUENCE UsersUserID
START 1
INCREMENT 1
MINVALUE 1
OWNED BY USERS.UserID;

-- Give the db some users
INSERT INTO
    USERS(UserID, FName, LName)
VALUES
    (nextval('UsersUserID'),'Wilfredo', 'Caballero'),
    (nextval('UsersUserID'),'Edouard', 'Mendy'),
    (nextval('UsersUserID'),'Karlo', 'Ziger'),
    (nextval('UsersUserID'),'Baba', 'Rahman'),
    (nextval('UsersUserID'),'Dujon', 'Sterling'),
    (nextval('UsersUserID'),'Antonio', 'Rudiger'),
    (nextval('UsersUserID'),'Marcos', 'Alonso'),
    (nextval('UsersUserID'),'Andreas', 'Christensen'),
    (nextval('UsersUserID'),'Thiago', 'Silva'),
    (nextval('UsersUserID'),'Fikayo', 'Tomori'),
    (nextval('UsersUserID'),'Kurt', 'Zouma'),
    (nextval('UsersUserID'),'Ben', 'Chilwell'),
    (nextval('UsersUserID'),'Reece', 'James'),
    (nextval('UsersUserID'),'Cesar', 'Azpilicueta'),
    (nextval('UsersUserID'),'Dynel', 'Simeu'),
    (nextval('UsersUserID'),'Charly', 'Musonda'),
    (nextval('UsersUserID'),'Danny', 'Drinkwater'),
    (nextval('UsersUserID'),'Christian', 'Pulisic'),
    (nextval('UsersUserID'),'Mateo', 'Kovacic'),
    (nextval('UsersUserID'),'Jason', 'Mount'),
    (nextval('UsersUserID'),'Callum', 'Hudson-Odoi'),
    (nextval('UsersUserID'),'Hakim', 'Ziyech'),
    (nextval('UsersUserID'),'Billy', 'Gilmour'),
    (nextval('UsersUserID'),'Kai', 'Havertz'),
    (nextval('UsersUserID'),'George', 'McEachran'),
    (nextval('UsersUserID'),'Henry', 'Lawrence'),
    (nextval('UsersUserID'),'Lewis', 'Bate'),
    (nextval('UsersUserID'),'Faustino', 'Anjorin'),
    (nextval('UsersUserID'),'Myles', 'Peart-Harris'),
    (nextval('UsersUserID'),'Harvey', 'Vale'),
    (nextval('UsersUserID'),'Tammy', 'Abraham'),
    (nextval('UsersUserID'),'Timo', 'Werner')
    ;
