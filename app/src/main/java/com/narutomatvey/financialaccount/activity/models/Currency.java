package com.narutomatvey.financialaccount.activity.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class Currency {

    public final int pk;
    public final String name;
    public final String short_name;
    public final float coefficient;

    public Currency(int pk, @NonNull String name, @Nullable String short_name, float coefficient){
        this.pk = pk;
        this.name = name;
        if (short_name != null){
            this.short_name = short_name;
        }
        else {
            this.short_name = "";
        }
        this.coefficient = coefficient;
    }
}
