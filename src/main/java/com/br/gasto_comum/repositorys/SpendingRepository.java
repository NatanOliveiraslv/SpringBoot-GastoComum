package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpendingRepository extends JpaRepository<Spending, UUID> {
    List<Spending> findByUser(User user);
}
