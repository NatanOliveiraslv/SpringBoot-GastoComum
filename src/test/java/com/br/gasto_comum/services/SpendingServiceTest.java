package com.br.gasto_comum.services;

import com.br.gasto_comum.dtos.spending.SpendingRequestDTO;
import com.br.gasto_comum.dtos.spending.SpendingResponseDTO;
import com.br.gasto_comum.dtos.users.UserRequestDTO;
import com.br.gasto_comum.enums.Type;
import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.repositorys.SpendingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
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

    @Autowired
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
}