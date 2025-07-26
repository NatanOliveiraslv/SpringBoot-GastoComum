CREATE TABLE spending (
    id CHAR(36) PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    value DOUBLE NOT NULL,
    description VARCHAR(255),
    user_id CHAR(36) NOT NULL,
    voucher_id BIGINT,
    group_id CHAR(36),
    date_spending DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (voucher_id) REFERENCES files(id),
    CONSTRAINT fk_spending_groupspending FOREIGN KEY (group_id) REFERENCES groupspending(id)
);

CREATE INDEX idx_spending_title ON spending(title);