package com.narutomatvey.financialaccount.activity.models;

import com.narutomatvey.financialaccount.activity.enums.FinanceType;
import com.narutomatvey.financialaccount.activity.helper.DBHelper;

public final class Category {

    private int pk;
    private FinanceType type;
    private String name;

    public Category(Integer pk, String name, FinanceType type) {
        if(pk != null) {
            this.pk = pk;
        }
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public FinanceType getType() {
        return type;
    }

    public void createNewCategory(DBHelper db){
        pk = db.createCategory(this);
    }
}
