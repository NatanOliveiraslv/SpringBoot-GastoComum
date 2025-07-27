CREATE TABLE files (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_file_name VARCHAR(255) NOT NULL,
    file_system VARCHAR(255) NOT NULL UNIQUE,
    mime_type VARCHAR(255),
    size BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_files_filesystem ON files(file_system);