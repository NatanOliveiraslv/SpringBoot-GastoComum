package com.br.gasto_comum.specification;

import com.br.gasto_comum.models.Spending;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;


public class SpendingSpecification{

    public static Specification<Spending> searchByTitle(String title) {

        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(title)) {
                return null;
            }
            return builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<Spending> searchByGroupIsNull(Boolean containsGroup) {
        return (root, query, builder) -> {
            if (containsGroup == null || !containsGroup) {
                return null;
            }
            return builder.isNull(root.get("group"));
        };
    }
}
