package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.dtos.group.GroupRequestDTO;
import com.br.gasto_comum.dtos.users.UserRequestDTO;
import com.br.gasto_comum.models.Group;
import com.br.gasto_comum.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class GroupRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;

    @Test
    @DisplayName("Deve encontrar grupos pelo usuário")
    void findByUserCas1() {
        User user = new User(new UserRequestDTO("usuario1", "senha123", "Test", "testando", "teste@teste.com"));
        userRepository.save(user);
        Group group = new Group(new GroupRequestDTO("Grupo Teste", "Descrição do grupo teste", null));
        group.setUser(user);
        groupRepository.save(group);

        var result = groupRepository.findByUser(user, null);

        assertEquals(1, result.getTotalElements());
        assertThat(result.getContent()).contains(group);
    }

    @Test
    @DisplayName("Não deve encontrar grupos para usuário sem grupos")
    void findByUserCas2() {
        User user = new User(new UserRequestDTO("usuario1", "senha123", "Test", "testando", "teste@teste.com"));
        userRepository.save(user);

        var result = groupRepository.findByUser(user, null);
        assertEquals(0, result.getTotalElements(), "Deve retornar zero grupos para usuário sem grupos");
    }
}