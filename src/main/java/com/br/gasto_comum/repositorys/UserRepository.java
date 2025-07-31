package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.models.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    Page<User> findByIdIsNot(UUID id, Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "u.id <> :excludedId")
    Page<User> findByFirstNameOrEmailContainingIgnoreCaseAndIdIsNot(
            String search, // Termo de busca para nome ou email
            UUID excludedId, // ID do usuário a ser excluído
            Pageable pageable // Parâmetro de paginação
    );
}
