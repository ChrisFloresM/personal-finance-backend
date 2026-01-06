package com.cfloresh.springboot.app.personalfinance.model.transactions;

import org.springframework.data.jpa.domain.Specification;

public class TransactionsSpeficiations {
    public static Specification<Transaction> hasCategory(Long categoryId) {
        return (root, query, builder) -> {
            if (categoryId == null || categoryId == 0) {
                return builder.conjunction();
            }

            return builder.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<Transaction> hasUserId(Long userId) {
        return (root, query, builder) -> {
            if (userId == null) return builder.conjunction();

            return builder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Transaction> includeSearch(String search) {
        return (root, query, builder) -> {
            if (search == null || search.isEmpty()) {
                return builder.conjunction();
            }

            return builder.like(builder.lower(root.get("name")), "%" + search.toLowerCase() + "%");
        };
    }
}
