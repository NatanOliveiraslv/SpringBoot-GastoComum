CREATE TABLE document (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    mime_type VARCHAR(255),
    size BIGINT,
    hash VARCHAR(255)
);

ALTER TABLE spending ADD voucher_id BIGINT;

ALTER TABLE spending ADD FOREIGN KEY (voucher_id) REFERENCES document(id);

UPDATE spending SET voucher_id = NULL WHERE voucher_id IS NULL;