package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.dtos.users.UserRequestDTO;
import com.br.gasto_comum.models.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Deve encontrar um usuário pelo nome de usuário")
    void findByUsernameCase1() {
        String username = "joao123";
        UserRequestDTO data = new UserRequestDTO(username, "senhaSegura", "João", "Silva", "joao@gmail.com");
        this.createUser(data);

        Optional<User> result = this.userRepository.findByUsername(username);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Não deve encontrar um usuário inexistente pelo nome de usuário")
    void findByUsernameCase2() {
        String username = "joao1234";
        Optional<User> result = this.userRepository.findByUsername(username);

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve retornar true se existir usuário pelo username")
    void existsByUsernameCase1() {
        String username = "maria123";
        UserRequestDTO data = new UserRequestDTO(username, "senha", "Maria", "Oliveira", "maria@gmail.com");
        this.createUser(data);

        boolean exists = this.userRepository.existsByUsername(username);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false se não existir usuário pelo username")
    void existsByUsernameCase2() {
        boolean exists = this.userRepository.existsByUsername("naoexiste");
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve retornar true se existir usuário pelo email")
    void existsByEmailCase1() {
        String email = "carlos@gmail.com";
        UserRequestDTO data = new UserRequestDTO("carlos123", "senha", "Carlos", "Souza", email);
        this.createUser(data);

        boolean exists = this.userRepository.existsByEmail(email);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false se não existir usuário pelo email")
    void existsByEmailCase2() {
        boolean exists = this.userRepository.existsByEmail("naoexiste@gmail.com");
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve listar usuários excluindo o id informado")
    void findByIdIsNotCase1() {
        User user1 = this.createUser(new UserRequestDTO("natan123", "senha", "Ana", "Lima", "ana@gmail.com"));
        User user2 = this.createUser(new UserRequestDTO("natan456", "senha", "Bruno", "Costa", "bruno@gmail.com"));

        var page = this.userRepository.findByIdIsNot(user1.getId(), org.springframework.data.domain.PageRequest.of(0, 10));

        assertThat(page.getContent()).contains(user2);
        assertThat(page.getContent()).doesNotContain(user1);
    }

    @Test
    @DisplayName("Deve buscar usuários por nome ou email ignorando case e excluindo id")
    void findByFirstNameOrEmailContainingIgnoreCaseAndIdIsNotCase1() {
        User user1 = this.createUser(new UserRequestDTO("vigarista123", "senha", "Lucas", "Ferreira", "lucas@gmail.com"));
        User user2 = this.createUser(new UserRequestDTO("vigarista456", "senha", "Luana", "Silva", "luana@gmail.com"));

        var page = this.userRepository.findByFirstNameOrEmailContainingIgnoreCaseAndIdIsNot(
                "lu", user1.getId(), org.springframework.data.domain.PageRequest.of(0, 10));

        assertThat(page.getContent()).contains(user2);
        assertThat(page.getContent()).doesNotContain(user1);
    }

    private User createUser(UserRequestDTO data){
        User user = new User(data);
        this.userRepository.save(user);
        return user;
    }
}