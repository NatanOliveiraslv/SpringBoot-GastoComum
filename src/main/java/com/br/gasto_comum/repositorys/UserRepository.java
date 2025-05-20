package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByLogin(String username);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
}
