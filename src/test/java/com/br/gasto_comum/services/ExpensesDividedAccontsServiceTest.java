package com.br.gasto_comum.services;

import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsRequestDTO;
import com.br.gasto_comum.dtos.expensesDividedAcconts.ExpensesDividedAccontsResponseDTO;
import com.br.gasto_comum.dtos.spending.SpendingRequestDTO;
import com.br.gasto_comum.enums.Type;
import com.br.gasto_comum.exceptions.ObjectNotFound;
import com.br.gasto_comum.exceptions.UnauthorizedUser;
import com.br.gasto_comum.exceptions.UserIsAlreadyInExpense;
import com.br.gasto_comum.models.ExpensesDividedAcconts;
import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.queryFilters.ExpensesDividedAccontsQueryFilter;
import com.br.gasto_comum.queryFilters.SpendingQueryFilter;
import com.br.gasto_comum.repositorys.ExpensesDividedAccontsRepository;
import com.br.gasto_comum.repositorys.SpendingRepository;
import com.br.gasto_comum.repositorys.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
class ExpensesDividedAccontsServiceTest {

    @InjectMocks
    private ExpensesDividedAccontsService service;

    @Mock
    private ExpensesDividedAccontsRepository expensesDividedAccontsRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SpendingRepository spendingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Criar ExpensesDividedAcconts - Sucesso")
    void createExpensesDividedAcconts_success() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        ExpensesDividedAccontsRequestDTO dto = mock(ExpensesDividedAccontsRequestDTO.class);
        SpendingRequestDTO spendingDto = new SpendingRequestDTO(Type.COMIDA, "Jantar", 80.0, "Jantar em família", LocalDate.now(), null);

        Spending spending = spy(new Spending(spendingDto));
        UUID spendingId = UUID.randomUUID();

        when(dto.spendingId()).thenReturn(spendingId);
        when(spendingRepository.findById(spendingId)).thenReturn(Optional.of(spending));
        doReturn(false).when(spending).checkIfTheUserIsSpending(user);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ExpensesDividedAccontsResponseDTO response = service.createExpensesDividedAcconts(dto, user);

        assertNotNull(response);
        assertEquals(40.0, response.value());
        verify(spendingRepository).findById(spendingId);
        verify(spendingRepository).save(any(Spending.class));
        verify(expensesDividedAccontsRepository, atLeastOnce()).save(any(ExpensesDividedAcconts.class));
    }


    @Test
    @DisplayName("Criar ExpensesDividedAcconts - Gasto não encontrado")
    void createExpensesDividedAcconts_spendingNotFound() {
        User user = mock(User.class);
        UUID spendingId = UUID.randomUUID();
        ExpensesDividedAccontsRequestDTO dto = mock(ExpensesDividedAccontsRequestDTO.class);
        when(dto.spendingId()).thenReturn(spendingId);

        when(spendingRepository.findById(spendingId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFound.class, () -> service.createExpensesDividedAcconts(dto, user));
    }

    @Test
    @DisplayName("Criar ExpensesDividedAcconts - Usuário já está no gasto")
    void createExpensesDividedAcconts_userAlreadyInExpense() {
        User user = mock(User.class);
        UUID spendingId = UUID.randomUUID();
        ExpensesDividedAccontsRequestDTO dto = mock(ExpensesDividedAccontsRequestDTO.class);
        when(dto.spendingId()).thenReturn(spendingId);

        Spending spending = mock(Spending.class);
        when(spendingRepository.findById(spendingId)).thenReturn(Optional.of(spending));
        when(spending.checkIfTheUserIsSpending(user)).thenReturn(true);

        assertThrows(UserIsAlreadyInExpense.class, () -> service.createExpensesDividedAcconts(dto, user));
    }

    @Test
    @DisplayName("Listar ExpensesDividedAcconts - Sucesso")
    void listExpensesDividedAcconts_success() {

        User user = new User();
        Spending spending = new Spending(new SpendingRequestDTO(Type.COMIDA, "Jantar", 80.0, "Jantar em família", LocalDate.now(), null));
        Pageable pageable = Pageable.unpaged();
        ExpensesDividedAcconts expensesDividedAcconts = new ExpensesDividedAcconts(user, spending);

        ExpensesDividedAccontsQueryFilter filter = mock(ExpensesDividedAccontsQueryFilter.class);
        when(expensesDividedAccontsRepository.findAll(filter.toSpecification(user), pageable)).thenReturn(new PageImpl<>(List.of(expensesDividedAcconts)));

        var result = service.listExpensesDividedAcconts(user, pageable, filter);

        assertNotNull(result);
        verify(expensesDividedAccontsRepository).findAll(filter.toSpecification(user), pageable);
    }

    @Test
    @DisplayName("Adicionar ExpensesDividedAcconts - Sucesso")
    void addExpensesDividedAcconts_success() {
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Spending spending = mock(Spending.class);
        ExpensesDividedAcconts expensesDividedAcconts = mock(ExpensesDividedAcconts.class);

        when(spending.getValue()).thenReturn(100.0);
        when(spending.getExpensesDividedAcconts()).thenReturn(new ArrayList<>());
        doNothing().when(spending).addExpensesDividedAcconts(any());
        when(expensesDividedAccontsRepository.save(any())).thenReturn(expensesDividedAcconts);
        when(spendingRepository.save(any())).thenReturn(spending);

        var result = service.addExpensesDividedAcconts(spending, userId);

        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(expensesDividedAccontsRepository, atLeastOnce()).save(any());
        verify(spendingRepository).save(spending);
    }

    @Test
    @DisplayName("Adicionar ExpensesDividedAcconts - Usuário não encontrado")
    void addExpensesDividedAcconts_userNotFound() {
        UUID userId = UUID.randomUUID();
        Spending spending = mock(Spending.class);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFound.class, () -> service.addExpensesDividedAcconts(spending, userId));
    }

    @Test
    @DisplayName("Pagar ExpensesDividedAcconts - Sucesso")
    void payExpensesDividedAcconts_success() {
        UUID id = UUID.randomUUID();
        double value = 50.0;
        User user = new User();
        Spending spending = new Spending(new SpendingRequestDTO(Type.COMIDA, "Jantar", 80.0, "Jantar em família", LocalDate.now(), null));
        ExpensesDividedAcconts expensesDividedAcconts = new ExpensesDividedAcconts(user, spending);
        expensesDividedAcconts.setValue(80.0);

        when(expensesDividedAccontsRepository.findById(id)).thenReturn(Optional.of(expensesDividedAcconts));

        var result = service.payExpensesDividedAcconts(id, value, user);

        assertNotNull(result);
        verify(expensesDividedAccontsRepository).findById(id);
        assertEquals(30.0, result.value());
    }

    @Test
    @DisplayName("Pagar ExpensesDividedAcconts - Com valor maior que o saldo")
    void payExpensesDividedAcconts_valueExceedsBalance() {
        UUID id = UUID.randomUUID();
        double value = 50.0;
        User user = new User();
        Spending spending = new Spending(new SpendingRequestDTO(Type.COMIDA, "Jantar", 20.0, "Jantar em família", LocalDate.now(), null));
        ExpensesDividedAcconts expensesDividedAcconts = new ExpensesDividedAcconts(user, spending);
        expensesDividedAcconts.setValue(20.0);

        when(expensesDividedAccontsRepository.findById(id)).thenReturn(Optional.of(expensesDividedAcconts));
        assertThrows(IllegalArgumentException.class, () -> service.payExpensesDividedAcconts(id, value, user));
    }

    @Test
    @DisplayName("Pagar ExpensesDividedAcconts - Usuário não autorizado")
    void payExpensesDividedAcconts_unauthorized() {
        UUID id = UUID.randomUUID();
        double value = 50.0;
        User user = new User();
        user.setId(UUID.randomUUID());
        User otherUser = new User();
        user.setId(UUID.randomUUID());
        ExpensesDividedAcconts expensesDividedAcconts = mock(ExpensesDividedAcconts.class);
        when(expensesDividedAccontsRepository.findById(id)).thenReturn(Optional.of(expensesDividedAcconts));
        when(expensesDividedAcconts.getUser()).thenReturn(otherUser);

        assertThrows(UnauthorizedUser.class, () -> service.payExpensesDividedAcconts(id, value, user));
    }

    @Test
    @DisplayName("Pagar ExpensesDividedAcconts - ExpensesDividedAcconts não encontrado")
    void payExpensesDividedAcconts_notFound() {
        UUID id = UUID.randomUUID();
        double value = 50.0;
        User user = new User();

        when(expensesDividedAccontsRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFound.class, () -> service.payExpensesDividedAcconts(id, value, user));
    }
}