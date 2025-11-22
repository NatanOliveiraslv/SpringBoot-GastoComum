package com.br.gasto_comum.services;

import com.br.gasto_comum.dtos.spending.SpendingRequestDTO;
import com.br.gasto_comum.dtos.spending.SpendingResponseDTO;
import com.br.gasto_comum.dtos.spending.SpendingUpdateDTO;
import com.br.gasto_comum.dtos.users.UserRequestDTO;
import com.br.gasto_comum.enums.Type;
import com.br.gasto_comum.exceptions.ObjectNotFound;
import com.br.gasto_comum.exceptions.UnauthorizedUser;
import com.br.gasto_comum.models.File;
import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.queryFilters.SpendingQueryFilter;
import com.br.gasto_comum.repositorys.SpendingRepository;
import com.br.gasto_comum.specification.SpendingSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
class SpendingServiceTest {

    @Mock
    private SpendingRepository spendingRepository;

    @Mock
    private FileService fileService;

    @Mock
    private FileSystemStorageService fileSystemStorageService;

    @Mock
    private ExpensesDividedAccontsService expensesDividedAccontsService;

    @InjectMocks
    private SpendingService spendingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve criar um gasto com sucesso")
    void createSpendingCase1() throws NoSuchAlgorithmException, IOException {
        User user = new User(new UserRequestDTO("maria123", "senhaForte", "Maria", "Oliveira", "email@gmail.com"));
        SpendingRequestDTO spending = new SpendingRequestDTO(Type.COMIDA, "Almoço", 50.0, "Almoço com amigos", LocalDate.now(), null);
        MultipartFile file = mock(MultipartFile.class);

        when(fileService.uploadFile(file)).thenReturn(null);

        SpendingResponseDTO result = spendingService.createSpending(spending, user, file);

        verify(fileService, times(1)).uploadFile(file);
        verify(spendingRepository, times(1)).save(any(Spending.class));

        assertEquals(spending.title(), result.title(), "O título do gasto deve ser igual ao esperado");
    }

    @Test
    @DisplayName("Deve listar gastos do usuário com filtro")
    void listSpendingCase1() {
        User user = new User();
        Pageable pageable = Pageable.unpaged();
        Spending spending = new Spending(new SpendingRequestDTO(Type.COMIDA, "Jantar", 80.0, "Jantar em família", LocalDate.now(), null));
        SpendingQueryFilter filter = mock(SpendingQueryFilter.class);
        when(spendingRepository.findAll(filter.toSpecification(user), pageable)).thenReturn(new PageImpl<>(List.of(spending)));
        var result = spendingService.listSpending(user, pageable, filter);

        assertNotNull(result);
        verify(spendingRepository).findAll(filter.toSpecification(user), pageable);
    }

    @Test
    @DisplayName("Deve atualizar gasto com sucesso")
    void updateSpendingCase1() throws Exception {
        User user = new User();
        Spending spending = new Spending(new SpendingRequestDTO(Type.COMIDA, "Jantar", 80.0, "Jantar em família", LocalDate.now(), null));
        spending.setUser(user);
        SpendingUpdateDTO dto = mock(SpendingUpdateDTO.class);
        when(dto.id()).thenReturn(UUID.randomUUID());
        MultipartFile file = mock(MultipartFile.class);

        when(spendingRepository.findById(dto.id())).thenReturn(Optional.of(spending));
        when(fileService.uploadFile(file)).thenReturn(null);

        var result = spendingService.updateSpending(dto, user, file);

        assertNotNull(result);
        verify(spendingRepository).findById(dto.id());
        verify(fileService).uploadFile(file);
    }

    @Test
    @DisplayName("Deve lançar UnauthorizedUser ao atualizar gasto de outro usuário")
    void updateSpendingUnauthorized() {
        User user = new User();
        user.setId(UUID.randomUUID());
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());

        Spending spending = new Spending();
        spending.setUser(user);
        UUID spendingId = UUID.randomUUID();

        SpendingUpdateDTO dto = mock(SpendingUpdateDTO.class);
        when(dto.id()).thenReturn(spendingId);

        when(spendingRepository.findById(spendingId)).thenReturn(Optional.of(spending));

        assertThrows(UnauthorizedUser.class, () -> spendingService.updateSpending(dto, otherUser, null));
    }

    @Test
    @DisplayName("Deve detalhar gasto com sucesso")
    void detailSpendingCase1() {
        User user = new User();
        Spending spending = new Spending(new SpendingRequestDTO(Type.COMIDA, "Jantar", 80.0, "Jantar em família", LocalDate.now(), null));

        spending.setUser(user);
        UUID id = UUID.randomUUID();

        when(spendingRepository.findById(id)).thenReturn(Optional.of(spending));

        var result = spendingService.detailSpending(id, user);

        assertNotNull(result);
        verify(spendingRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar UnauthorizedUser ao detalhar gasto de outro usuário")
    void detailSpendingUnauthorized() {
        User user = new User();
        user.setId(UUID.randomUUID());
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());
        Spending spending = new Spending();
        spending.setUser(otherUser);
        UUID id = UUID.randomUUID();

        when(spendingRepository.findById(id)).thenReturn(Optional.of(spending));

        assertThrows(UnauthorizedUser.class, () -> spendingService.detailSpending(id, user));
    }

    @Test
    @DisplayName("Deve lançar ObjectNotFound ao detalhar gasto inexistente")
    void detailSpendingNotFound() {
        User user = new User();
        UUID id = UUID.randomUUID();

        when(spendingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFound.class, () -> spendingService.detailSpending(id, user));
    }

    @Test
    @DisplayName("Deve deletar gasto com sucesso")
    void deleteSpendingCase1() {
        User user = new User();
        Spending spending = new Spending();
        spending.setUser(user);
        UUID id = UUID.randomUUID();

        when(spendingRepository.findById(id)).thenReturn(Optional.of(spending));

        spendingService.deleteSpending(id, user);

        verify(spendingRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar UnauthorizedUser ao deletar gasto de outro usuário")
    void deleteSpendingUnauthorized() {
        User user = new User();
        user.setId(UUID.randomUUID());
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());
        Spending spending = new Spending();
        spending.setUser(otherUser);
        UUID id = UUID.randomUUID();

        when(spendingRepository.findById(id)).thenReturn(Optional.of(spending));

        assertThrows(UnauthorizedUser.class, () -> spendingService.deleteSpending(id, user));
    }

    @Test
    @DisplayName("Deve lançar ObjectNotFound ao deletar gasto inexistente")
    void deleteSpendingNotFound() {
        User user = new User();
        UUID id = UUID.randomUUID();

        when(spendingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFound.class, () -> spendingService.deleteSpending(id, user));
    }

    @Test
    @DisplayName("Deve baixar arquivo do comprovante")
    void downloadFileCase1() {
        String fileName = "voucher.png";
        Resource resource = mock(Resource.class);

        when(fileSystemStorageService.loadFileAsResource(fileName)).thenReturn(resource);

        var result = spendingService.downloadFile(fileName);

        assertNotNull(result);
        verify(fileSystemStorageService).loadFileAsResource(fileName);
    }
}