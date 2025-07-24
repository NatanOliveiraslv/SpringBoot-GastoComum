package com.br.gasto_comum.services;

import com.br.gasto_comum.dtos.Document.DocumentResponseDTO;
import com.br.gasto_comum.dtos.spending.SpendingRequestDTO;
import com.br.gasto_comum.dtos.spending.SpendingResponseDTO;
import com.br.gasto_comum.dtos.spending.SpendingResponseDetailDTO;
import com.br.gasto_comum.dtos.spending.SpendingUpdateDTO;
import com.br.gasto_comum.exceptions.ObjectNotFound;
import com.br.gasto_comum.exceptions.UnauthorizedUser;
import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.repositorys.SpendingRepository;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Service
public class SpendingService {

    @Autowired
    private SpendingRepository spendingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DocumentService documentService;

    public SpendingResponseDTO createSpending(SpendingRequestDTO data, User user, MultipartFile file) throws NoSuchAlgorithmException, IOException {
        var spendingEntity = new Spending(data);
        spendingEntity.setUser(user);
        System.out.println(user.getUsername());
        if(file != null) {
            spendingEntity.setVoucher(documentService.saveDocument(file));
        }
        spendingRepository.save(spendingEntity);
        return new SpendingResponseDTO(spendingEntity);
    }

    public List<SpendingResponseDTO> listSpending(User user) {
        return spendingRepository.findByUser(user).stream().map(SpendingResponseDTO::new).toList();
    }

    public SpendingResponseDetailDTO updateSpending(SpendingUpdateDTO data, User user, MultipartFile file) throws NoSuchAlgorithmException, IOException {
        var spendingEntity = spendingRepository.findById(data.id()).orElseThrow(() -> new ObjectNotFound("Gasto não encontrado"));
        if (!spendingEntity.getUser().equals(user)) {
            throw new UnauthorizedUser(); // Forbidden
        }
        if(file != null) {
            spendingEntity.setVoucher(documentService.saveDocument(file));
        }
        spendingEntity.update(data);
        return new SpendingResponseDetailDTO(spendingEntity);
    }

    public SpendingResponseDetailDTO detailSpending(UUID id, User user) {
        var spendingEntity = spendingRepository.findById(id).orElseThrow(() -> new ObjectNotFound("Gasto não encontrado"));
        if (!spendingEntity.getUser().equals(user)) {
            throw new UnauthorizedUser();  // Forbidden
        }
        return new SpendingResponseDetailDTO(spendingEntity);
    }

    public void deleteSpending(UUID id, User user) {
        var spendingEntity = spendingRepository.findById(id).orElseThrow(() -> new ObjectNotFound("Gasto não encontrado"));

        if (!spendingEntity.getUser().equals(user)) {
            throw new UnauthorizedUser();  // Forbidden
        }

        spendingRepository.deleteById(id);
    }

    public DocumentResponseDTO returnVoucher(UUID id, User user) {
        var spendingEntity = spendingRepository.findById(id).orElseThrow(() -> new ObjectNotFound("Gasto não encontrado"));
        if (!spendingEntity.getUser().equals(user)) {
            throw new UnauthorizedUser();  // Forbidden
        }

        DocumentResponseDTO documentResponseDTO = new DocumentResponseDTO(documentService.load(spendingEntity.getVoucher().getHash()));

        return documentResponseDTO;
    }

}
