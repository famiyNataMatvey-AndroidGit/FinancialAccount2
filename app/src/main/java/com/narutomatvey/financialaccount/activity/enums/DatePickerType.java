package com.narutomatvey.financialaccount.activity.enums;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;


public enum DatePickerType {
    DAY("День", null, "EEE, dd MMM y"),
    WEEK("Неделя", "dd MMM", "dd MMM y"),
    MONTH("Месяц", null, "MMMM y"),
    YEAR("Год", null, "y"),
    PERIOD("Промежуток", "dd MMM y", "dd MMM y"),
    PERIOD_ALL("Весь период", null, null);

    private String title;
    private SimpleDateFormat fromFormat = null;
    private SimpleDateFormat toFormat = null;

    @SuppressLint("SimpleDateFormat")
    DatePickerType(String title, String fromFormat, String toFormat) {
        this.title = title;
        if (fromFormat != null) {
            this.fromFormat = new SimpleDateFormat(fromFormat);
        }
        if (toFormat != null) {
            this.toFormat = new SimpleDateFormat(toFormat);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDateFrom(Date date_from) {
        return fromFormat.format(date_from);
    }

    public String getDateTo(Date date_to) {
        return toFormat.format(date_to);
    }
}
