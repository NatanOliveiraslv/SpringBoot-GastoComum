CREATE TABLE groupspending(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    user_id CHAR(36),
    total_value DOUBLE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

ALTER TABLE spending ADD group_id BIGINT;

UPDATE spending SET group_id = NULL WHERE group_id IS NULL;

ALTER TABLE spending ADD CONSTRAINT fk_spending_groupspending FOREIGN KEY (group_id) REFERENCES groupspending(id);