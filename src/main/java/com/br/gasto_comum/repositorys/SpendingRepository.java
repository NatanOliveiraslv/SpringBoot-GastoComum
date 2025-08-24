package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.dtos.dashboard.DashboardCategoriesSpendingsDTO;
import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpendingRepository extends JpaRepository<Spending, UUID>, JpaSpecificationExecutor<Spending> {
    List<Spending> findByUser(User user);
    Long countByUser(User user);
    @Query("SELECT SUM(s.value) FROM Spending s WHERE s.user = :user")
    Double totalValueSpendingsByUser(@Param("user") User user);

    // Custom query to get spending by categories
    @Query("SELECT new com.br.gasto_comum.dtos.dashboard.DashboardCategoriesSpendingsDTO(s.type, COUNT(s)) " +
            "FROM Spending s " +
            "WHERE s.user = :user " +
            "GROUP BY s.type")
    List<DashboardCategoriesSpendingsDTO> categoriesSpendingsByUser(@Param("user") User user);
}
