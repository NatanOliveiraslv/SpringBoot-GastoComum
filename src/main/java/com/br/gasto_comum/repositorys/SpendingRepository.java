package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpendingRepository extends JpaRepository<Spending, UUID>, JpaSpecificationExecutor<Spending> {
    Page<Spending> findByUser(User user, Pageable pageable, Specification<Spending> spec);
}
