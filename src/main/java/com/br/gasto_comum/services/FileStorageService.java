package com.br.gasto_comum.services;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    /**
     * Armazena um arquivo e retorna o nome único gerado ou o caminho/URL.
     * @param file O arquivo MultipartFile a ser armazenado.
     * @return Uma string contendo o nome único/caminho/URL do arquivo armazenado.
     * @throws IOException Se ocorrer um erro durante o armazenamento do arquivo.
     */
    String storeFile(MultipartFile file) throws IOException;

    /**
     * Carrega um arquivo como um recurso (Resource) para download.
     * @param fileName O nome único/caminho do arquivo a ser carregado.
     * @return Um Resource representando o arquivo.
     * @throws IOException Se o arquivo não for encontrado ou ocorrer um erro de leitura.
     */
    Path loadFileAsPath(String fileName); // Retorna Path para uso interno ou Resource para API

    /**
     * Exclui um arquivo do sistema de armazenamento.
     * @param fileName O nome único/caminho do arquivo a ser excluído.
     * @return true se o arquivo foi excluído com sucesso, false caso contrário.
     */
    boolean deleteFile(String fileName);
}