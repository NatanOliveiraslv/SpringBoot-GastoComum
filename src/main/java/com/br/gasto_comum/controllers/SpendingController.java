package com.br.gasto_comum.controllers;

import com.br.gasto_comum.services.SpendingService;
import com.br.gasto_comum.spending.*;
import com.br.gasto_comum.users.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/spending")
public class SpendingController {

    @Autowired
    private SpendingService spendingService;

    @PostMapping
    public ResponseEntity<SpendingResponseDTO> createSpending(@RequestBody @Valid SpendingRequestDTO data, UriComponentsBuilder uriBuilder, @AuthenticationPrincipal User user) {
        var spending = spendingService.createSpending(data, user);
        var uri = uriBuilder.path("/spending/{id}").buildAndExpand(spending.id()).toUri();
        return ResponseEntity.created(uri).body(spending);
    }

    @GetMapping
    public ResponseEntity<List<SpendingResponseDTO>> listSpending(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(spendingService.listSpending(user));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<SpendingResponseDetailDTO> updateSpending(@RequestBody @Valid SpendingUpdateDTO data, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(spendingService.updateSpending(data,user));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<SpendingResponseDetailDTO> detailSpending(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(spendingService.detailSpending(id, user));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteSpending(@PathVariable Long id, @AuthenticationPrincipal User user) {
        spendingService.deleteSpending(id, user);
        return ResponseEntity.noContent().build();
    }

}
