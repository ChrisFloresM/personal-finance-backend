package com.cfloresh.springboot.app.personalfinance.dto;

public class TransactionDTO {
    private String avatar;
    private String name;
    private String category;
    private String date;
    private double amount;
    private boolean recurring;

    public TransactionDTO(String avatar, String name, String category, String date, double amount, boolean recurring) {
        this.avatar = avatar;
        this.name = name;
        this.category = category;
        this.date = date;
        this.amount = amount;
        this.recurring = recurring;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isRecurring() {
        return recurring;
    }
}
