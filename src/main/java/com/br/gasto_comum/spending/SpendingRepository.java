package com.br.gasto_comum.spending;

import com.br.gasto_comum.expensesDividedAcconts.ExpensesDividedAcconts;
import com.br.gasto_comum.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface SpendingRepository extends JpaRepository<Spending, Long> {
    List<Spending> findByUser(User user);
}
