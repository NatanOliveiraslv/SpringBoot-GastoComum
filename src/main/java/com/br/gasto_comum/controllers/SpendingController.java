package com.br.gasto_comum.controllers;

import com.br.gasto_comum.spending.Spending;
import com.br.gasto_comum.spending.SpendingRequestDTO;
import com.br.gasto_comum.spending.SpendingRepository;
import com.br.gasto_comum.spending.SpendingResponseDTO;
import com.br.gasto_comum.users.UserRepository;
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

        var user = userRepository.findById(data.userID()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));;

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

}
