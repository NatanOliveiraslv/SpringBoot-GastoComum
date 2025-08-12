package com.br.gasto_comum.services;

import com.br.gasto_comum.dtos.spending.SpendingRequestDTO;
import com.br.gasto_comum.dtos.spending.SpendingResponseDTO;
import com.br.gasto_comum.dtos.spending.SpendingResponseDetailDTO;
import com.br.gasto_comum.dtos.spending.SpendingUpdateDTO;
import com.br.gasto_comum.exceptions.ObjectNotFound;
import com.br.gasto_comum.exceptions.UnauthorizedUser;
import com.br.gasto_comum.models.ExpensesDividedAcconts;
import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.repositorys.ExpensesDividedAccontsRepository;
import com.br.gasto_comum.repositorys.SpendingRepository;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Service
public class SpendingService {

    @Autowired
    private SpendingRepository spendingRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileSystemStorageService fileSystemStorageService;
    @Autowired
    ExpensesDividedAccontsService expensesDividedAccontsService;

    public SpendingResponseDTO createSpending(SpendingRequestDTO data, User user, MultipartFile file) throws NoSuchAlgorithmException, IOException {
        var spendingEntity = new Spending(data);
        spendingEntity.setUser(user);
        if(file != null) {
            spendingEntity.setVoucher(fileService.uploadFile(file));
        }

        spendingRepository.save(spendingEntity);

        if (data.participantIds() != null && !data.participantIds().isEmpty()) {
            for (var participantId : data.participantIds()) {
                expensesDividedAccontsService.addExpensesDividedAcconts(spendingEntity, participantId);
            }
        }
        return new SpendingResponseDTO(spendingEntity);
    }

    public Page<SpendingResponseDTO> listSpending(User user, Pageable pageable, boolean onlyWithoutGroup) {
        if (onlyWithoutGroup) {
            return spendingRepository
                    .findByUserAndGroupIsNull(user, pageable).map(SpendingResponseDTO::new);
        }
        return spendingRepository.findByUserAndGroupIsNull(user, pageable).map(SpendingResponseDTO::new);
    }

    public Page<SpendingResponseDTO> searchSpending(String searchQuery, Pageable pageable, User user) {
        return spendingRepository.findByUserAndTitleContainingIgnoreCaseAndGroupIsNull(user, searchQuery, pageable).map(SpendingResponseDTO::new);
    }

    public SpendingResponseDTO updateSpending(SpendingUpdateDTO data, User user, MultipartFile file) throws NoSuchAlgorithmException, IOException {
        var spendingEntity = spendingRepository.findById(data.id()).orElseThrow(() -> new ObjectNotFound("Gasto não encontrado"));
        if (!spendingEntity.getUser().equals(user)) {
            throw new UnauthorizedUser(); // Forbidden
        }
        if(file != null) {
            spendingEntity.setVoucher(fileService.uploadFile(file));
        }
        spendingEntity.update(data);
        return new SpendingResponseDTO(spendingEntity);
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


    public Resource downloadFile(String systemFileName) {
        return fileSystemStorageService.loadFileAsResource(systemFileName);
    }

}
