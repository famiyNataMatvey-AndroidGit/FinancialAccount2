package com.narutomatvey.financialaccount.activity.enums;

public enum DataPickerType {
    DAY ("День"),
    WEEK ("Неделя"),
    MONTH ("Месяц"),
    YEAR ("Год"),
    PERIOD ("Промежуток"),
    PERIODALL ("Весь период");

    private String title;

    DataPickerType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
