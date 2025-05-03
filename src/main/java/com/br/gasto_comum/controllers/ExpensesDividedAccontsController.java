package com.br.gasto_comum.controllers;

import com.br.gasto_comum.expensesDividedAcconts.*;
import com.br.gasto_comum.service.ExpensesDividedAccontsService;
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
@RequestMapping("/expenses-divided-accounts")
public class ExpensesDividedAccontsController {

    @Autowired
    private ExpensesDividedAccontsService expensesDividedAccontsService;

    @PostMapping
    @Transactional
    public ResponseEntity<ExpensesDividedAccontsResponseDTO> createExpensesDividedAcconts(@RequestBody @Valid ExpensesDividedAccontsRequestDTO data, UriComponentsBuilder uriBuilder) {
        var ExpensesDividedAcconts = expensesDividedAccontsService.createExpensesDividedAcconts(data);
        var uri  = uriBuilder.path("/expenses-divided-accounts/{id}").buildAndExpand(ExpensesDividedAcconts.id()).toUri();
        return ResponseEntity.created(uri).body(ExpensesDividedAcconts);
    }

    @GetMapping("/spending")
    public ResponseEntity<List<ExpensesDividedAccontsResponseListDTO>> listSpendingByUserId(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(expensesDividedAccontsService.listSpendingByUserId(user));
    }

    @PutMapping("/pay/{id}")
    @Transactional
    public ResponseEntity<ExpensesDividedAccontsResponseDTO> payExpensesDividedAcconts(@PathVariable Long id, @RequestBody @Valid ExpensesDividedAccontsPayDTO data) {
        return ResponseEntity.ok(expensesDividedAccontsService.payExpensesDividedAcconts(id, data));
    }
}
