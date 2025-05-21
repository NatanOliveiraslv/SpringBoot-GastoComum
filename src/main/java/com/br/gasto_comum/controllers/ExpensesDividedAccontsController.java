package com.br.gasto_comum.controllers;

import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsPayDTO;
import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsRequestDTO;
import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsResponseDTO;
import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsResponseListDTO;
import com.br.gasto_comum.services.ExpensesDividedAccontsService;
import com.br.gasto_comum.models.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/expenses-divided-accounts")
public class ExpensesDividedAccontsController {

    @Autowired
    private ExpensesDividedAccontsService expensesDividedAccontsService;

    @PostMapping
    @Transactional
    public ResponseEntity<ExpensesDividedAccontsResponseDTO> createExpensesDividedAcconts(@RequestBody @Valid ExpensesDividedAccontsRequestDTO data, UriComponentsBuilder uriBuilder, @AuthenticationPrincipal User user) {
        var ExpensesDividedAcconts = expensesDividedAccontsService.createExpensesDividedAcconts(data, user);
        var uri  = uriBuilder.path("/expenses-divided-accounts/{id}").buildAndExpand(ExpensesDividedAcconts.id()).toUri();
        return ResponseEntity.created(uri).body(ExpensesDividedAcconts);
    }

    @GetMapping("/spending")
    public ResponseEntity<List<ExpensesDividedAccontsResponseListDTO>> listSpendingByUserId(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(expensesDividedAccontsService.listSpendingByUserId(user));
    }

    @PutMapping("/pay/{id}")
    @Transactional
    public ResponseEntity<ExpensesDividedAccontsResponseDTO> payExpensesDividedAcconts(@PathVariable Long id, @RequestBody @Valid ExpensesDividedAccontsPayDTO data, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(expensesDividedAccontsService.payExpensesDividedAcconts(id, data.value(), user));
    }
}
