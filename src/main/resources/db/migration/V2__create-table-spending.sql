CREATE TABLE spending (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      type VARCHAR(255),
      value DOUBLE,
      description VARCHAR(255),
      user_id CHAR(36),
      registration_date DATETIME,
      FOREIGN KEY (user_id) REFERENCES users(id)
);
