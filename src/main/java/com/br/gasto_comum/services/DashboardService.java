package com.br.gasto_comum.services;

import com.br.gasto_comum.dtos.dashboard.DashboardCategoriesSpendingsDTO;
import com.br.gasto_comum.dtos.dashboard.DashboardResponseDTO;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.repositorys.ExpensesDividedAccontsRepository;
import com.br.gasto_comum.repositorys.SpendingRepository;
import com.br.gasto_comum.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private SpendingRepository spendingRepository;
    @Autowired
    private ExpensesDividedAccontsRepository expensesDividedAccontsRepository;

    public DashboardResponseDTO dataDashboard(User user) {
        Long totalSpendings = spendingRepository.countByUser(user);
        Double totalValueSpendings = spendingRepository.totalValueSpendingsByUser(user);
        Long totalExpensesDividedAccounts = expensesDividedAccontsRepository.countByUser(user);
        Double totalValueExpensesDividedAccounts = expensesDividedAccontsRepository.totalValueExpensesDividedAccountsByUser(user);
        List<DashboardCategoriesSpendingsDTO> categoriesSpendings = spendingRepository.categoriesSpendingsByUser(user);

        return new DashboardResponseDTO(
                totalSpendings,
                totalValueSpendings != null ? totalValueSpendings : 0.0,
                totalExpensesDividedAccounts,
                totalValueExpensesDividedAccounts != null ? totalValueExpensesDividedAccounts : 0.0,
                categoriesSpendings
        );
    }

}
