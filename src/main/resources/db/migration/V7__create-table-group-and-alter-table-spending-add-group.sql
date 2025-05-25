CREATE TABLE groupspending(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    user_id BIGINT,
    total_value DOUBLE,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

ALTER TABLE spending ADD group_id BIGINT;

UPDATE spending SET group_id = NULL WHERE group_id IS NULL;

ALTER TABLE spending ADD CONSTRAINT fk_spending_groupspending FOREIGN KEY (group_id) REFERENCES groupspending(id);