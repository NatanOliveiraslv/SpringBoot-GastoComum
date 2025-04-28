package com.br.gasto_comum.ExpensesDividedAcconts;

import com.br.gasto_comum.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpensesDividedAccontsRepository extends JpaRepository<ExpensesDividedAcconts, Long> {
    List<ExpensesDividedAcconts> findByUser(User user);
}
