package com.br.gasto_comum.specification;

import com.br.gasto_comum.models.Spending;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


public class SpendingSpecification implements Specification<Spending> {

    public String title;
    public String groupName;
    boolean containsGroup;

    public SpendingSpecification(String title, String groupName, boolean containsGroup) {
        this.title = title;
        this.groupName = groupName;
        this.containsGroup = containsGroup;
    }

    @Override
    public Predicate toPredicate(Root<Spending> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(this.title)) {
            predicates.add(criteriaBuilder.like(root.get("title"), "%" + this.title + "%"));
        }
        if (StringUtils.isNotEmpty(this.groupName)) {
            predicates.add(criteriaBuilder.equal(root.get("group").get("name"), this.groupName));
        }
        return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    }
}
