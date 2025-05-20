package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.models.ExpensesDividedAcconts;
import com.br.gasto_comum.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpensesDividedAccontsRepository extends JpaRepository<ExpensesDividedAcconts, Long> {
    List<ExpensesDividedAcconts> findByUser(User user);
}
