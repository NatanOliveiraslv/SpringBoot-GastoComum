package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpendingRepository extends JpaRepository<Spending, Long> {
    List<Spending> findByUser(User user);
}
