package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.models.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    @NotNull Page<User> findAll(@NotNull Pageable pageable);
    boolean existsByUsername(String login);
    boolean existsByEmail(String email);
    Page<User> findByFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String nameSearch, String emailSearch, Pageable pageable);
}
