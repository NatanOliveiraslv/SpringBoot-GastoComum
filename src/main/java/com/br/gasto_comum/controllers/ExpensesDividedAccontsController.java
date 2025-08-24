package com.br.gasto_comum.controllers;

import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsPayDTO;
import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsRequestDTO;
import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsResponseDTO;
import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsResponseListDTO;
import com.br.gasto_comum.queryFilters.ExpensesDividedAccontsQueryFilter;
import com.br.gasto_comum.services.ExpensesDividedAccontsService;
import com.br.gasto_comum.models.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/expenses-divided-accounts")
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

    @GetMapping()
    public ResponseEntity<Page<ExpensesDividedAccontsResponseListDTO>> listSpendingByUserId(ExpensesDividedAccontsQueryFilter filter, @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable, @AuthenticationPrincipal User user) {
        Page<ExpensesDividedAccontsResponseListDTO> expensesDividedAccontsPage;
        expensesDividedAccontsPage = expensesDividedAccontsService.listExpensesDividedAcconts(user, pageable, filter);
        return ResponseEntity.ok(expensesDividedAccontsPage);
    }

    @PutMapping("/pay/{id}")
    @Transactional
    public ResponseEntity<ExpensesDividedAccontsResponseDTO> payExpensesDividedAcconts(@PathVariable UUID id, @RequestBody @Valid ExpensesDividedAccontsPayDTO data, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(expensesDividedAccontsService.payExpensesDividedAcconts(id, data.value(), user));
    }
}
