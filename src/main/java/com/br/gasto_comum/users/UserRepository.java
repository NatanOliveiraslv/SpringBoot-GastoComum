package com.br.gasto_comum.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByLogin(String username);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
}
