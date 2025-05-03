package com.br.gasto_comum.controllers;

import com.br.gasto_comum.service.SpendingService;
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
    private SpendingService spendingService;

    @PostMapping
    public ResponseEntity<SpendingResponseDTO> createSpending(@RequestBody @Valid SpendingRequestDTO data, UriComponentsBuilder uriBuilder) {
        var spending = spendingService.createSpending(data);
        var uri = uriBuilder.path("/spending/{id}").buildAndExpand(spending.id()).toUri();
        return ResponseEntity.created(uri).body(spending);
    }

    @GetMapping
    public ResponseEntity<List<SpendingResponseDTO>> listSpending() {
        return ResponseEntity.ok(spendingService.listSpending());
    }

    @PutMapping
    @Transactional
    public ResponseEntity<SpendingResponseDetailDTO> updateSpending(@RequestBody @Valid SpendingUpdateDTO data) {
        return ResponseEntity.ok(spendingService.updateSpending(data));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<SpendingResponseDetailDTO> detailSpending(@PathVariable Long id) {
        return ResponseEntity.ok(spendingService.detailSpending(id));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteSpending(@PathVariable Long id, @RequestBody @Valid SpendingDeleteDTO data) {
        spendingService.deleteSpending(id, data);
        return ResponseEntity.noContent().build();
    }

}
