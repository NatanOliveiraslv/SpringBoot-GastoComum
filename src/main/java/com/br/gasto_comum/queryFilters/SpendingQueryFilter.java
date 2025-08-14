package com.br.gasto_comum.queryFilters;

import com.br.gasto_comum.models.Spending;
import com.br.gasto_comum.models.User; // Importe a classe User
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import static com.br.gasto_comum.specification.SpendingSpecification.searchByGroupIsNull;
import static com.br.gasto_comum.specification.SpendingSpecification.searchByTitle;

@Data
public class SpendingQueryFilter {

    private String title;
    private Boolean containsGroup;

    public Specification<Spending> toSpecification(User user) {
        Specification<Spending> spec = (root, query, builder) -> builder.equal(root.get("user"), user);

        // Adiciona o filtro de título se ele existir
        if (title != null && !title.trim().isEmpty()) {
            spec = spec.and(searchByTitle(title));
        }

        // Adiciona o filtro de grupo se ele existir
        if (containsGroup != null) {
            spec = spec.and(searchByGroupIsNull(containsGroup));
        }

        return spec;
    }
}