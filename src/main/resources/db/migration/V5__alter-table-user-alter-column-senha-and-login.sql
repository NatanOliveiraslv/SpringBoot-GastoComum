ALTER TABLE user
    CHANGE COLUMN senha password VARCHAR(100) NOT NULL;

ALTER TABLE user
    ADD CONSTRAINT unique_login UNIQUE (login)