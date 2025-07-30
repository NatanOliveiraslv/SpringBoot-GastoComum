ALTER TABLE users ADD profile_picture_id BIGINT;
ALTER TABLE users ADD FOREIGN KEY (profile_picture_id) REFERENCES files(id);