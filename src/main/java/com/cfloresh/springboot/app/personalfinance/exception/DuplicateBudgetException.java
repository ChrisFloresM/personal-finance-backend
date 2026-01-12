package com.cfloresh.springboot.app.personalfinance.exception;

public class DuplicateBudgetException extends RuntimeException {
    public DuplicateBudgetException(String message) {
        super(message);
    }
}
