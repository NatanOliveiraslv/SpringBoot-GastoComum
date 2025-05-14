package com.br.gasto_comum.services;

import com.br.gasto_comum.document.Document;
import com.br.gasto_comum.document.DocumentRepository;
import com.br.gasto_comum.exceptions.ObjectNotFound;
import com.br.gasto_comum.exceptions.UnauthorizedUser;
import com.br.gasto_comum.spending.*;
import com.br.gasto_comum.users.User;
import com.br.gasto_comum.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class SpendingService {

    @Autowired
    private SpendingRepository spendingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DocumentRepository documentRepository;

    public SpendingResponseDTO createSpending(SpendingRequestDTO data, User user, MultipartFile file) throws NoSuchAlgorithmException {
        var spendingEntity = new Spending(data);
        spendingEntity.setUser(user);
        if(file != null) {
            saveDocument(file, spendingEntity);
        }
        spendingRepository.save(spendingEntity);
        return new SpendingResponseDTO(spendingEntity);
    }

    private void saveDocument(MultipartFile file, Spending spending) throws NoSuchAlgorithmException {
        if (file != null && !file.isEmpty()) {
            var document = new Document();
            document.setName(file.getOriginalFilename());
            document.setMimeType(file.getContentType());
            document.setSize(file.getSize());
            document.setHash();
            spending.setVoucher(document);
            documentRepository.save(document);
        }
    }

    public List<SpendingResponseDTO> listSpending(User user) {
        return spendingRepository.findByUser(user).stream().map(SpendingResponseDTO::new).toList();
    }

    public SpendingResponseDetailDTO updateSpending(SpendingUpdateDTO data, User user, MultipartFile file) throws NoSuchAlgorithmException {
        var spendingEntity = spendingRepository.findById(data.id()).orElseThrow(() -> new ObjectNotFound("Gasto não encontrado"));
        if (!spendingEntity.getUser().equals(user)) {
            throw new UnauthorizedUser(); // Forbidden
        }
        if(file != null) {
            saveDocument(file, spendingEntity);
        }
        spendingEntity.update(data);
        return new SpendingResponseDetailDTO(spendingEntity);
    }

    public SpendingResponseDetailDTO detailSpending(Long id, User user) {
        var spendingEntity = spendingRepository.findById(id).orElseThrow(() -> new ObjectNotFound("Gasto não encontrado"));
        if (!spendingEntity.getUser().equals(user)) {
            throw new UnauthorizedUser();  // Forbidden
        }
        return new SpendingResponseDetailDTO(spendingEntity);
    }

    public void deleteSpending(Long id, User user) {
        var spendingEntity = spendingRepository.findById(id).orElseThrow(() -> new ObjectNotFound("Gasto não encontrado"));

        if (!spendingEntity.getUser().equals(user)) {
            throw new UnauthorizedUser();  // Forbidden
        }

        spendingRepository.deleteById(id);
    }

}
