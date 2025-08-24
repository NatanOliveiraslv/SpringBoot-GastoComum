package com.br.gasto_comum.specification;

import com.br.gasto_comum.enums.Status;
import com.br.gasto_comum.models.ExpensesDividedAcconts;
import com.br.gasto_comum.models.Spending;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class ExpensesDividedAccontsSpecification {

    public static Specification<ExpensesDividedAcconts> searchByTitle(String title) {

        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(title)) {
                return null;
            }
            return builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<ExpensesDividedAcconts> searchByStatus(Status status) {
        return (root, query, builder) -> {
            if (status == null) {
                return null;
            }
            return builder.equal(root.get("status"), status);
        };
    }

}
