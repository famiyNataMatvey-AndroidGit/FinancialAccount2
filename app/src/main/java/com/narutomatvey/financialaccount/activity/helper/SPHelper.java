package com.narutomatvey.financialaccount.activity.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.narutomatvey.financialaccount.activity.enums.DatePickerType;

public class SPHelper {
    private static final String APP_PREFERENCES_NAME = "dbSettings";

    private final String APP_FIRST_START = "first_start";
    private final String BALANCE = "balance";
    private final String DEFAULT_CURRENCY = "default_currency";

    private final String DATE_PICKER_TYPE = "date_picker_type";

    private final String FNS_IS_REGISTRATION = "registration_fns";
    private final String FNS_PHONE = "fns_phone";
    private final String FNS_EMAIL = "fns_email";
    private final String APP_PREFERENCES_FNS_NAME = "fns_name";
    private final String APP_PREFERENCES_FNS_PASSWORD = "fns_password";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SPHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void checkFirstLaunch(Context context) {
        if (!sharedPreferences.contains(APP_FIRST_START)) {
            editor = sharedPreferences.edit();
            editor.putBoolean(BALANCE,true);
            editor.putBoolean(APP_FIRST_START, true);
            editor.putBoolean(FNS_IS_REGISTRATION, false);
            editor.putLong(DEFAULT_CURRENCY, 1);
            editor.apply();

            new DBHelper(context).onCreate();
        }
    }

    public boolean getBalanceStatus(){
        return sharedPreferences.getBoolean(BALANCE, false);
    }

    public void setBalanceTrue(){
        if (!sharedPreferences.getBoolean(BALANCE, false)) {
            editor = sharedPreferences.edit();
            editor.putBoolean(BALANCE, true);
            editor.apply();
        }
    }

    public void setBalanceFalse(){
        if (!sharedPreferences.getBoolean(BALANCE, false)) {
            editor = sharedPreferences.edit();
            editor.putBoolean(BALANCE, false);
            editor.apply();
        }
    }


    public void setDataPickerType(DatePickerType dataPickerType) {
        editor = sharedPreferences.edit();
        editor.putInt(DATE_PICKER_TYPE, dataPickerType.ordinal());
        editor.apply();
    }

    public DatePickerType getDataPickerType() {
        int ordinal = sharedPreferences.getInt(DATE_PICKER_TYPE, DatePickerType.WEEK.ordinal());
        return DatePickerType.values()[ordinal];
    }
}
