package com.br.gasto_comum.services;

import com.br.gasto_comum.exceptions.ObjectNotFound;
import com.br.gasto_comum.exceptions.UnauthorizedUser;
import com.br.gasto_comum.spending.*;
import com.br.gasto_comum.users.User;
import com.br.gasto_comum.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpendingService {

    @Autowired
    private SpendingRepository spendingRepository;
    @Autowired
    private UserRepository userRepository;

    public SpendingResponseDTO createSpending(SpendingRequestDTO data, User user) {
        var spendingEntity = new Spending(data);
        spendingEntity.setUser(user);
        spendingRepository.save(spendingEntity);
        return new SpendingResponseDTO(spendingEntity);
    }

    public List<SpendingResponseDTO> listSpending(User user) {
        return spendingRepository.findByUser(user).stream().map(SpendingResponseDTO::new).toList();
    }

    public SpendingResponseDetailDTO updateSpending(SpendingUpdateDTO data, User user) {
        var spendingEntity = spendingRepository.findById(data.id()).orElseThrow(() -> new ObjectNotFound("Gasto não encontrado"));
        if (!spendingEntity.getUser().equals(user)) {
            throw new UnauthorizedUser(); // Forbidden
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
