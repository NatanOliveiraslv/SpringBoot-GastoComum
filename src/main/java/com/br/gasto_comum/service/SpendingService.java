package com.br.gasto_comum.service;

import com.br.gasto_comum.spending.*;
import com.br.gasto_comum.users.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class SpendingService {

    @Autowired
    private SpendingRepository spendingRepository;
    @Autowired
    private UserRepository userRepository;

    public SpendingResponseDTO createSpending(SpendingRequestDTO data) {
        var spendingEntity = new Spending(data);
        var user = userRepository.findById(data.userId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));;
        spendingEntity.setUser(user);
        spendingRepository.save(spendingEntity);
        return new SpendingResponseDTO(spendingEntity);
    }

    public List<SpendingResponseDTO> listSpending() {
        return spendingRepository.findAll().stream().map(SpendingResponseDTO::new).toList();
    }

    public SpendingResponseDetailDTO updateSpending(SpendingUpdateDTO data) {
        var spendingEntity = spendingRepository.getReferenceById(data.id());
        spendingEntity.update(data);
        return new SpendingResponseDetailDTO(spendingEntity);
    }

    public SpendingResponseDetailDTO detailSpending(Long id) {
        return new SpendingResponseDetailDTO(spendingRepository.getReferenceById(id));
    }

    public void deleteSpending(Long id, SpendingDeleteDTO data) {
        if (!spendingRepository.existsById(id)) {
            ResponseEntity.notFound().build();
        }

        var user = userRepository.findById(data.userId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var spendingEntity = spendingRepository.getReferenceById(id);

        if (!spendingEntity.getUser().equals(user)) {
            ResponseEntity.status(403).build(); // Forbidden
        }

        spendingRepository.deleteById(id);
    }

}
