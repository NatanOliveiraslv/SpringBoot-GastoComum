package com.br.gasto_comum.dtos.dashboard;

import java.util.List;

public record DashboardResponseDTO(
        Long totalSpendings,
        Double totalValueSpendings,
        Long totalExpensesDividedAccounts,
        Double totalValueExpensesDividedAccounts,
        List<DashboardCategoriesSpendingsDTO> categoriesSpendings
) {
    public DashboardResponseDTO(Long totalSpendings, Double totalValueSpendings, Long totalExpensesDividedAccounts, Double totalValueExpensesDividedAccounts, List<DashboardCategoriesSpendingsDTO> categoriesSpendings) {
        this.totalSpendings = totalSpendings;
        this.totalValueSpendings = totalValueSpendings;
        this.totalExpensesDividedAccounts = totalExpensesDividedAccounts;
        this.totalValueExpensesDividedAccounts = totalValueExpensesDividedAccounts;
        this.categoriesSpendings = categoriesSpendings;
    }
}
