package com.br.gasto_comum.service;

import com.br.gasto_comum.expensesDividedAcconts.*;
import com.br.gasto_comum.infra.TokenService;
import com.br.gasto_comum.spending.SpendingRepository;
import com.br.gasto_comum.users.User;
import com.br.gasto_comum.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpensesDividedAccontsService {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private ExpensesDividedAccontsRepository expensesDividedAccontsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpendingRepository spendingRepository;

    public ExpensesDividedAccontsResponseDTO createExpensesDividedAcconts(ExpensesDividedAccontsRequestDTO data) {

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

        return new ExpensesDividedAccontsResponseDTO(expensesDividedAcconts);
    }

    public List<ExpensesDividedAccontsResponseListDTO> listSpendingByUserId(User user) {
        return expensesDividedAccontsRepository.findByUser(user).stream().map(ExpensesDividedAccontsResponseListDTO::new).toList();
    }

    public ExpensesDividedAccontsResponseDTO payExpensesDividedAcconts(Long id, ExpensesDividedAccontsPayDTO data) {

        var expensesDividedAcconts = expensesDividedAccontsRepository.getReferenceById(id);

        var user = userRepository.findById(data.userId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (!expensesDividedAcconts.getUser().equals(user)) {
           ResponseEntity.status(403).build(); // Forbidden
        }

        expensesDividedAcconts.makePayment();
        return new ExpensesDividedAccontsResponseDTO(expensesDividedAcconts);
    }
}
