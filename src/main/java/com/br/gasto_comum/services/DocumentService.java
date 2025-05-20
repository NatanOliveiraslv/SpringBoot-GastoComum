package com.br.gasto_comum.services;

import com.br.gasto_comum.models.Document;
import com.br.gasto_comum.repositorys.DocumentRepository;
import com.br.gasto_comum.property.DocumentStorageProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public Resource load(String hash) {
        try {
            Path file = docStorageLocation.resolve(hash);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }


}
