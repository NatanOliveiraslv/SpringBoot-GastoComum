create table expensesDividedAcconts(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id CHAR(36),
    status VARCHAR(255),
    value DOUBLE,
    date_payment DATETIME,
    spending_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    CoNSTRAINT fk_expensesDividedAcconts_spending FOREIGN KEY (spending_id) REFERENCES spending(id)
);