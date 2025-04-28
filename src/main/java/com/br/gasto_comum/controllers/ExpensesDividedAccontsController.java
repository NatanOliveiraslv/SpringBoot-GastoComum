package com.br.gasto_comum.controllers;

import com.br.gasto_comum.ExpensesDividedAcconts.ExpensesDividedAcconts;
import com.br.gasto_comum.ExpensesDividedAcconts.ExpensesDividedAccontsRepository;
import com.br.gasto_comum.ExpensesDividedAcconts.ExpensesDividedAccontsRequestDTO;
import com.br.gasto_comum.ExpensesDividedAcconts.ExpensesDividedAccontsResponseDTO;
import com.br.gasto_comum.spending.Spending;
import com.br.gasto_comum.spending.SpendingRepository;
import com.br.gasto_comum.spending.SpendingResponseDTO;
import com.br.gasto_comum.spending.SpendingResponseDetailDTO;
import com.br.gasto_comum.users.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/expenses-divided-accounts")
public class ExpensesDividedAccontsController {

    @Autowired
    private ExpensesDividedAccontsRepository expensesDividedAccontsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpendingRepository spendingRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<ExpensesDividedAccontsResponseDTO> createExpensesDividedAcconts(@RequestBody @Valid ExpensesDividedAccontsRequestDTO data, UriComponentsBuilder uriBuilder) {

        //Aqui cria um novo objeto de despesas divididas
        var spending = spendingRepository.getReferenceById(data.spendingId());
        var user = userRepository.getReferenceById(data.userId());
        spending.checkIfTheUserIsSpending(user);
        var expensesDividedAcconts = new ExpensesDividedAcconts(user, spending);
        expensesDividedAccontsRepository.save(expensesDividedAcconts);

        //Aqui adiciona o objeto de despesas divididas na lista de despesas do objeto de despesas
        spending.addExpensesDividedAcconts(expensesDividedAcconts);
        spendingRepository.save(spending);

        //Aqui seta o valor da despesa dividida
        double value = spending.getValue() / (spending.getExpensesDividedAcconts().size() + 1);
        for (ExpensesDividedAcconts e : spending.getExpensesDividedAcconts()){
            e.setValue(value);
            expensesDividedAccontsRepository.save(e);
        }

        var uri  = uriBuilder.path("/expenses-divided-accounts/{id}").buildAndExpand(expensesDividedAcconts.getId()).toUri();

        return ResponseEntity.created(uri).body(new ExpensesDividedAccontsResponseDTO(expensesDividedAcconts));
    }

    @GetMapping("/spending/{userId}")
    public ResponseEntity<List<SpendingResponseDTO>> listSpendingByUserId(@PathVariable Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        var expenses = expensesDividedAccontsRepository.findByUser(user);
        var spendings = expenses.stream()
                .map(ExpensesDividedAcconts::getSpending)
                .map(SpendingResponseDTO::new)
                .toList();
        return ResponseEntity.ok(spendings);
    }
}
