package com.br.gasto_comum.controllers;

import com.br.gasto_comum.ExpensesDividedAcconts.ExpensesDividedAcconts;
import com.br.gasto_comum.ExpensesDividedAcconts.ExpensesDividedAccontsRepository;
import com.br.gasto_comum.ExpensesDividedAcconts.ExpensesDividedAccontsRequestDTO;
import com.br.gasto_comum.ExpensesDividedAcconts.ExpensesDividedAccontsResponseDTO;
import com.br.gasto_comum.spending.Spending;
import com.br.gasto_comum.spending.SpendingRepository;
import com.br.gasto_comum.users.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
        var expensesDividedAcconts = new ExpensesDividedAcconts(user, spending);
        expensesDividedAccontsRepository.save(expensesDividedAcconts);

        //Aqui adiciona o objeto de despesas divididas na lista de despesas do objeto de despesas
        spending.addExpensesDividedAcconts(expensesDividedAcconts);
        spendingRepository.save(spending);

        //Aqui seta o valor da despesa dividida
        expensesDividedAcconts.setValue(expensesDividedAcconts.dividedValue());
        expensesDividedAccontsRepository.save(expensesDividedAcconts);

        var uri  = uriBuilder.path("/expenses-divided-accounts/{id}").buildAndExpand(expensesDividedAcconts.getId()).toUri();

        return ResponseEntity.created(uri).body(new ExpensesDividedAccontsResponseDTO(expensesDividedAcconts));
    }

}
