package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.dtos.dashboard.DashboardCategoriesSpendingsDTO;
import com.br.gasto_comum.dtos.spending.SpendingRequestDTO;
import com.br.gasto_comum.dtos.users.UserRequestDTO;
import com.br.gasto_comum.enums.Type;
import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class SpendingRepositoryTest {

    @Autowired
    SpendingRepository spendingRepository;
    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        this.user = new User(new UserRequestDTO("natantest", "password", "Test", "User", "teste@gmail.com"));
        userRepository.save(this.user);

        Spending s1 = new Spending(new SpendingRequestDTO(Type.COMIDA, "Comida", 100.0, "Almoço", LocalDate.now(), null));
        s1.setUser(this.user);
        spendingRepository.save(s1);

        Spending s2 = new Spending(new SpendingRequestDTO(Type.TRANSPORTE, "Transporte", 50.0, "Almoço", LocalDate.now(), null));
        s2.setUser(this.user);
        spendingRepository.save(s2);
    }

    @Test
    @DisplayName("Deve retornar os gastos do usuário")
    void findByUserCase1() {
        List<Spending> spendings = spendingRepository.findByUser(this.user);
        assertEquals(2, spendings.size());
        assertTrue(spendings.stream().allMatch(s -> s.getUser().equals(this.user)));
    }

    @Test
    @DisplayName("Deve retornar lista vazia se o usuário não tiver gastos")
    void findByUserCase2() {
        User newUser = new User(new UserRequestDTO("newUser", "password", "New", "User", "test@teste.com"));
        userRepository.save(newUser);
        List<Spending> spendings = spendingRepository.findByUser(newUser);
        assertTrue(spendings.isEmpty(), "O usuário não deve ter gastos");
    }

    @Test
    @DisplayName("Deve retornar a quantidade de gastos do usuário")
    void countByUser() {
        Long count = spendingRepository.countByUser(this.user);
        assertEquals(2L, count, "O numero de gastos do usuários deve ser 2");
    }

    @Test
    @DisplayName("Deve retornar o valor total dos gastos do usuário")
    void totalValueSpendingsByUser() {
        Double total = spendingRepository.totalValueSpendingsByUser(this.user);
        assertEquals(150.0, total, "O valor total dos gastos do usuário deve ser 150.0");
    }

    @Test
    @DisplayName("Deve retornar os gastos por categoria do usuário")
    void categoriesSpendingsByUser() {
        List<DashboardCategoriesSpendingsDTO> result = spendingRepository.categoriesSpendingsByUser(this.user);

        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(dto -> dto.type().equals("Comida")));
        assertTrue(result.stream().anyMatch(dto -> dto.type().equals("Transporte")));
    }
}