package com.narutomatvey.financialaccount.activity.models;

public final class Category {

    private int pk;
    private int type;
    private String name;

    public Category(Integer pk, String name, int type) {
        if(pk != null) {
            this.pk = pk;
        }
        this.name = name;
        this.type = type;
    }
}
