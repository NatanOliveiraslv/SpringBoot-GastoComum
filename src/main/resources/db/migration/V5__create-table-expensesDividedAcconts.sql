create table expensesDividedAcconts(
    id CHAR(36) PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    status VARCHAR(255) NOT NULL,
    value DOUBLE NOT NULL,
    date_payment DATETIME,
    spending_id CHAR(36) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_expensesDividedAcconts_spending FOREIGN KEY (spending_id) REFERENCES spending(id) ON DELETE CASCADE
);