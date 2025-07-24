CREATE TABLE document (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    mime_type VARCHAR(255),
    size BIGINT,
    hash VARCHAR(255)
);