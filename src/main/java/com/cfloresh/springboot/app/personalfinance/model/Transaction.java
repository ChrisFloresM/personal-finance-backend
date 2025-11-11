package com.cfloresh.springboot.app.personalfinance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public class Transaction {
    private String avatar;
    private String name;
    private String category;
    private String date;
    private double amount;
    private boolean recurring;

    public Transaction(String avatar, String name, String category, String date, double amount, boolean recurring) {
        this.avatar = avatar;
        this.name = name;
        this.category = category;
        this.date = date;
        this.amount = amount;
        this.recurring = recurring;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public boolean isRecurring() {
        return recurring;
    }
}

