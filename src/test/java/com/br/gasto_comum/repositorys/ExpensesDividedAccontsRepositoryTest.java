package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.dtos.spending.SpendingRequestDTO;
import com.br.gasto_comum.dtos.users.UserRequestDTO;
import com.br.gasto_comum.enums.Type;
import com.br.gasto_comum.models.ExpensesDividedAcconts;
import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ExpensesDividedAccontsRepositoryTest {

    @Autowired
    ExpensesDividedAccontsRepository EdaRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SpendingRepository spendingRepository;

    private User user;

    @BeforeEach
    void setup() {
        this.user = new User(new UserRequestDTO("usuario1", "senha123", "Test", "testando", "teste@teste.com"));
        userRepository.save(user);

        Spending spending = new Spending(new SpendingRequestDTO(Type.COMIDA, "Comida", 100.0, "Almoço", LocalDate.now(), null));
        spending.setUser(this.user);
        spendingRepository.save(spending);

        ExpensesDividedAcconts e1 = new ExpensesDividedAcconts(user, spending);
        ExpensesDividedAcconts e2 = new ExpensesDividedAcconts(user, spending);
        e1.setValue(200.0);
        e2.setValue(300.0);
        EdaRepository.save(e1);
        EdaRepository.save(e2);
    }

    @Test
    @DisplayName("Deve contar despesas divididas por usuário")
    void countByUser() {
        Long count = EdaRepository.countByUser(this.user);
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Retorna o total de despesas do usuários")
    void totalValueExpensesDividedAccountsByUser() {
        Double total = EdaRepository.totalValueExpensesDividedAccountsByUser(this.user);
        assertEquals(500.0, total);
    }
}