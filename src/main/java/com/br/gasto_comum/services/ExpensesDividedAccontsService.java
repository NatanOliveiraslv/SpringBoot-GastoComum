package com.br.gasto_comum.services;

import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsRequestDTO;
import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsResponseDTO;
import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsResponseListDTO;
import com.br.gasto_comum.exceptions.ObjectNotFound;
import com.br.gasto_comum.exceptions.UnauthorizedUser;
import com.br.gasto_comum.exceptions.UserIsAlreadyInExpense;
import com.br.gasto_comum.infra.security.TokenService;
import com.br.gasto_comum.models.ExpensesDividedAcconts;
import com.br.gasto_comum.repositorys.ExpensesDividedAccontsRepository;
import com.br.gasto_comum.repositorys.SpendingRepository;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public ExpensesDividedAccontsResponseDTO createExpensesDividedAcconts(ExpensesDividedAccontsRequestDTO data, User user) {

        //Aqui cria um novo objeto de despesas divididas
        var spending = spendingRepository.findById(data.spendingId()).orElseThrow(() -> new ObjectNotFound("Gasto não encontrado"));
        if (spending.checkIfTheUserIsSpending(user)) {
            throw new UserIsAlreadyInExpense();
        }
        var dataUser = userRepository.findById(data.userId()).orElseThrow(() -> new ObjectNotFound("Usuário não encontrado"));
        var expensesDividedAcconts = new ExpensesDividedAcconts(dataUser, spending);
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

    public ExpensesDividedAccontsResponseDTO payExpensesDividedAcconts(UUID id, double value, User user) {

        var expensesDividedAcconts = expensesDividedAccontsRepository.findById(id).orElseThrow(() -> new ObjectNotFound("Divida não encontrada"));

        if (!expensesDividedAcconts.getUser().equals(user)) {
           throw new UnauthorizedUser(); // Forbidden
        }

        expensesDividedAcconts.makePayment(value);
        return new ExpensesDividedAccontsResponseDTO(expensesDividedAcconts);
    }
}
