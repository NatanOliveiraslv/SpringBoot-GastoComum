package com.br.gasto_comum.services;

import com.br.gasto_comum.property.FileStorageProperty;
import com.br.gasto_comum.repositorys.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileSystemStorageService implements FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileSystemStorageService(FileStorageProperty fileStorageProperty) throws IOException {
        this.fileStorageLocation = Paths.get(fileStorageProperty.getUploadDirectory()).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
        String fileName = UUID.randomUUID().toString() + "_" + "imagem." + extension;

        try {
            // Verifica se o arquivo contém caracteres inválidos
            if (fileName.contains("..")) {
                throw new IOException("Nome de arquivo inválido: " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new IOException("Não foi possível armazenar o arquivo " + originalFileName, ex);
        }
    }

    @Override
    public Path loadFileAsPath(String fileName) {
        return this.fileStorageLocation.resolve(fileName).normalize();
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = loadFileAsPath(fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Arquivo não encontrado " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Arquivo não encontrado " + fileName, ex);
        }
    }

    @Override
    public boolean deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            // Logar o erro, mas não necessariamente lançar uma exceção se a intenção for apenas tentar apagar
            System.err.println("Erro ao deletar o arquivo " + fileName + ": " + ex.getMessage());
            return false;
        }
    }
}