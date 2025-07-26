package com.br.gasto_comum.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity(name = "File")
@Table(name = "files")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;

    @Column(name = "file_system", nullable = false, unique = true)
    private String systemFileName;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(nullable = false)
    private long size;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    public File(String originalFilename, String systemFileName, String contentType, long size) {
        this.originalFileName = originalFilename;
        this.systemFileName = systemFileName;
        this.mimeType = contentType;
        this.size = size;
    }
}
