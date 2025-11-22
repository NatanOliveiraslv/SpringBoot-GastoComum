package com.br.gasto_comum.services;

import com.br.gasto_comum.dtos.group.GroupRequestAddSpendingDTO;
import com.br.gasto_comum.dtos.group.GroupRequestDTO;
import com.br.gasto_comum.dtos.group.GroupResponseDatailDTO;
import com.br.gasto_comum.dtos.users.UserRequestDTO;
import com.br.gasto_comum.exceptions.ObjectNotFound;
import com.br.gasto_comum.exceptions.UnauthorizedUser;
import com.br.gasto_comum.models.Group;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.repositorys.GroupRepository;
import com.br.gasto_comum.repositorys.SpendingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private SpendingRepository spendingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve criar um grupo com sucesso")
    void createGroup() {
        User user = new User();
        GroupRequestDTO dto = mock(GroupRequestDTO.class);
        when(dto.spendingIds()).thenReturn(Collections.emptyList());
        Group group = new Group(dto);
        group.setUser(user);

        when(groupRepository.save(any(Group.class))).thenReturn(group);

        var response = groupService.createGroup(dto, user);

        assertNotNull(response);
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    @DisplayName("Deve adicionar gastos ao grupo com sucesso")
    void addSpendingToGroup_success() {
        User user = new User();
        UUID groupId = UUID.randomUUID();
        Group group = mock(Group.class);
        when(group.getUser()).thenReturn(user);

        List<UUID> spendingIds = List.of(UUID.randomUUID());
        GroupRequestAddSpendingDTO dto = mock(GroupRequestAddSpendingDTO.class);
        when(dto.groupId()).thenReturn(groupId);
        when(dto.spendingIds()).thenReturn(spendingIds);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        var spending = mock(com.br.gasto_comum.models.Spending.class);
        when(spending.getUser()).thenReturn(user);
        when(spendingRepository.findById(any(UUID.class))).thenReturn(Optional.of(spending));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        var response = groupService.addSpendingToGroup(dto, user);

        assertNotNull(response);
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    @DisplayName("Deve lançar UnauthorizedUser ao adicionar gastos ao grupo sem autorização")
    void addSpendingToGroup_unauthorized() {
        User user = new User();
        user.setId(UUID.randomUUID());
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());
        Group group = new Group(new GroupRequestDTO("Grupo Teste", "teste",null));
        group.setUser(user);
        UUID groupId = UUID.randomUUID();

        GroupRequestAddSpendingDTO dto = mock(GroupRequestAddSpendingDTO.class);

        when(dto.groupId()).thenReturn(groupId);
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        assertThrows(UnauthorizedUser.class, () -> groupService.addSpendingToGroup(dto, otherUser));
    }

    @Test
    @DisplayName("Deve listar grupos do usuário com sucesso")
    void listGroup() {
        User user = new User();
        Pageable pageable = Pageable.unpaged();
        Page<Group> page = new PageImpl<>(List.of(mock(Group.class)));
        when(groupRepository.findByUser(user, pageable)).thenReturn(page);

        var result = groupService.listGroup(user, pageable);

        assertNotNull(result);
        verify(groupRepository).findByUser(user, pageable);
    }

    @Test
    @DisplayName("Deve detalhar grupo com sucesso")
    void detailGroup_success() {
        User user = new User();
        UUID groupId = UUID.randomUUID();
        Group group = mock(Group.class);
        when(group.getUser()).thenReturn(user);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        var result = groupService.detailGroup(groupId, user);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve lançar UnauthorizedUser ao detalhar grupo sem autorização")
    void detailGroup_unauthorized() {
        User user = new User();
        user.setId(UUID.randomUUID());
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());
        Group group = new Group(new GroupRequestDTO("Grupo Teste", "teste",null));
        group.setUser(user);
        UUID groupId = UUID.randomUUID();

        GroupRequestAddSpendingDTO dto = mock(GroupRequestAddSpendingDTO.class);

        when(dto.groupId()).thenReturn(groupId);
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        assertThrows(UnauthorizedUser.class, () -> groupService.detailGroup(groupId, otherUser));
    }

    @Test
    @DisplayName("Deve lançar ObjectNotFound ao detalhar grupo inexistente")
    void detailGroup_notFound() {
        User user = new User();
        UUID groupId = UUID.randomUUID();

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFound.class, () -> groupService.detailGroup(groupId, user));
    }
}