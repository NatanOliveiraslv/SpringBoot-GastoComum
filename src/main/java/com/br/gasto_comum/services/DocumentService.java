package com.br.gasto_comum.services;

import com.br.gasto_comum.dtos.Document.DocumentResponseDTO;
import com.br.gasto_comum.exceptions.FileIsTooLarge;
import com.br.gasto_comum.models.Document;
import com.br.gasto_comum.repositorys.DocumentRepository;
import com.br.gasto_comum.property.DocumentStorageProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

@Service
public class DocumentService {

    private final Path docStorageLocation;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentStorageProperty documentStorageProperty) throws IOException {
        this.docStorageLocation = Paths.get(documentStorageProperty.getUploadDirectory()).toAbsolutePath().normalize();
        Files.createDirectories(this.docStorageLocation);
    }


    public Document saveDocument(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        if (file != null && !file.isEmpty()) {
            if (file.getSize() > 1_000_000) { // 1 MB limit
                throw new FileIsTooLarge();
            }
            var document = new Document();
            document.setName(file.getOriginalFilename());
            document.setMimeType(file.getContentType());
            document.setSize(file.getSize());
            document.setHash();
            storeDocument(file, document.getHash());
            documentRepository.save(document);
            return document;
        }
        return null;
    }

    private void storeDocument(MultipartFile file, String hash) throws IOException {
        Path targetLocation = docStorageLocation.resolve(hash);
        Files.copy(file.getInputStream(), targetLocation);
    }

    public String load(String hash) {
        try {
            Path file = docStorageLocation.resolve(hash);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                String fileDownloadLink = ServletUriComponentsBuilder.
                        fromCurrentContextPath()
                        .path("/files/download/")
                        .path(hash).
                        toString();
                return fileDownloadLink;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }


}
