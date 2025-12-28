package com.cfloresh.springboot.app.personalfinance.model.transactions;

import org.springframework.data.jpa.domain.Specification;

public class TransactionsSpeficiations {
    public static Specification<Transaction> hasCategory(String category) {
        return (root, query, builder) -> {
            if (category == null || category.isEmpty() || "ALL".equalsIgnoreCase(category)) {
                return builder.conjunction();
            }

            return builder.equal(builder.lower(root.get("category")), category.toLowerCase());
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
