package com.br.gasto_comum.services;

import com.br.gasto_comum.models.File;
import com.br.gasto_comum.repositorys.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;

    public FileService(FileRepository fileRepository, FileStorageService fileStorageService) {
        this.fileRepository = fileRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public File uploadFile(MultipartFile multipartFile) throws IOException {
        String systemFileName = fileStorageService.storeFile(multipartFile);

        File arquivo = new File(
                multipartFile.getOriginalFilename(),
                systemFileName,
                multipartFile.getContentType(),
                multipartFile.getSize()
        );
        return fileRepository.save(arquivo);
    }


    @Transactional
    public void deleteFile(Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado com ID: " + id));

        fileStorageService.deleteFile(file.getSystemFileName());
        fileRepository.delete(file);
    }

    public File SearchFileById(Long id) {
        return fileRepository.findById(id).orElse(null);
    }
}