package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.models.ExpensesDividedAcconts;
import com.br.gasto_comum.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpensesDividedAccontsRepository extends JpaRepository<ExpensesDividedAcconts, UUID>, JpaSpecificationExecutor<ExpensesDividedAcconts> {
    List<ExpensesDividedAcconts> findByUser(User user);
    Long countByUser(User user);
    @Query("SELECT SUM(s.value) FROM ExpensesDividedAcconts s WHERE s.user = :user")
    Double totalValueExpensesDividedAccountsByUser(User user);
}
