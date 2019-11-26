package com.narutomatvey.financialaccount.activity.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.narutomatvey.financialaccount.R;

import java.util.Calendar;

public class AddingFinanceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_finance);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }
}
