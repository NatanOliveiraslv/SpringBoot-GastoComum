package com.br.gasto_comum.controllers;

import com.br.gasto_comum.spending.*;
import com.br.gasto_comum.users.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/spending")
public class SpendingController {

    @Autowired
    private SpendingRepository spendingRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<SpendingResponseDTO> createSpending(@RequestBody @Valid SpendingRequestDTO data, UriComponentsBuilder uriBuilder) {
        var spendingEntity = new Spending(data);

        System.out.println("ID do usuário: " + data.userId());

        var user = userRepository.findById(data.userId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));;

        spendingEntity.setUser(user);
        spendingRepository.save(spendingEntity);

        var uri = uriBuilder.path("/spending/{id}").buildAndExpand(spendingEntity.getId()).toUri();

        return ResponseEntity.created(uri).body(new SpendingResponseDTO(spendingEntity));
    }

    @GetMapping
    public ResponseEntity<List<SpendingResponseDTO>> listSpending() {
        var spending = spendingRepository.findAll().stream().map(SpendingResponseDTO::new).toList();
        return ResponseEntity.ok(spending);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<SpendingResponseDetailDTO> updateSpending(@RequestBody @Valid SpendingUpdateDTO data) {
        var spendingEntity = spendingRepository.getReferenceById(data.id());
        spendingEntity.update(data);
        return ResponseEntity.ok(new SpendingResponseDetailDTO(spendingEntity));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteSpending(@PathVariable Long id, @RequestBody @Valid SpendingDeleteDTO data) {
        if (!spendingRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        var user = userRepository.findById(data.userId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        var spendingEntity = spendingRepository.getReferenceById(id);

        if (!spendingEntity.getUser().equals(user)) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        spendingRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
