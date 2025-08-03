package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpendingRepository extends JpaRepository<Spending, UUID> {
    Page<Spending> findByUser(User user, Pageable pageable);
    Page<Spending> findByUserAndTitleContainingIgnoreCase(User user, String title, Pageable pageable);
}
