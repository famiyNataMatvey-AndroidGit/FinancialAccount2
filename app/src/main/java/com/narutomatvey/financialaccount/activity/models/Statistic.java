package com.narutomatvey.financialaccount.activity.models;

public class Statistic {
    private Category category;
    private Double amount;

    public Statistic(Category category, Double amount){
        this.category = category;
        this.amount = amount;
    }

    public String getCategoryName(){
        return category.getName();
    }

    public Double getAmount(){
        return amount;
    }
}
