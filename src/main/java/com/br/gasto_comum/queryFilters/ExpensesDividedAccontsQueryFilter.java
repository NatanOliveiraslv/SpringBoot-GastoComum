package com.br.gasto_comum.queryFilters;

import com.br.gasto_comum.enums.Status;
import com.br.gasto_comum.models.ExpensesDividedAcconts;
import com.br.gasto_comum.models.User;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import static com.br.gasto_comum.specification.ExpensesDividedAccontsSpecification.searchByStatus;
import static com.br.gasto_comum.specification.ExpensesDividedAccontsSpecification.searchByTitle;

@Data
public class ExpensesDividedAccontsQueryFilter {

    private String title;
    private Status status;

    public Specification<ExpensesDividedAcconts> toSpecification(User user) {
        Specification<ExpensesDividedAcconts> spec = (root, query, builder) -> builder.equal(root.get("user"), user);

        // Adiciona o filtro de título se ele existir
        if (title != null && !title.trim().isEmpty()) {
            spec = spec.and(searchByTitle(title));
        }

        // Adiciona o filtro de grupo se ele existir
        if (status != null) {
            spec = spec.and(searchByStatus(status));
        }

        return spec;
    }
}