package com.cfloresh.springboot.app.personalfinance.model.transactions;


import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public enum TransactionSort {
    LATEST(Sort.by(Sort.Direction.DESC, "date", "id")),
    OLDEST(Sort.by(Sort.Direction.ASC, "date", "id")),
    ATOZ(Sort.by(Sort.Direction.ASC, "name")),
    HIGHEST(Sort.by(Sort.Direction.DESC, "amount")),
    LOWEST(Sort.by(Sort.Direction.ASC, "amount"));


    private final Sort sort;

    TransactionSort(Sort sort) {
        this.sort = sort;
    }

}
