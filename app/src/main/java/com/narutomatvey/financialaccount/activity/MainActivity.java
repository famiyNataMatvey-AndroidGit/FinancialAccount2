package com.narutomatvey.financialaccount.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.narutomatvey.financialaccount.R;
import com.narutomatvey.financialaccount.activity.activity.AddingFinanceActivity;
import com.narutomatvey.financialaccount.activity.activity.HomeActivity;
import com.narutomatvey.financialaccount.activity.activity.StatisticsActivity;
import com.narutomatvey.financialaccount.activity.enums.FinanceType;
import com.narutomatvey.financialaccount.activity.helper.SPHelper;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Menu _menu = null;
    private FinanceType financeMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SPHelper sp = new SPHelper(this);
        sp.checkFirstLaunch(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeActivity()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    setFinanceMode(item);
                    Fragment selectedFragment;

                    if (item.getItemId() == R.id.menu_home) {
                        selectedFragment = new HomeActivity();
                    } else {
                        selectedFragment = new AddingFinanceActivity(financeMode);
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };


    private void setFinanceMode(MenuItem item) {
        setVisibleFalseItemsToolbar();
        switch (item.getItemId()) {
            case R.id.menu_home:
                _menu.findItem(R.id.menu_setting).setVisible(true);
                financeMode = null;
                break;
            case R.id.menu_expenses:
                financeMode = FinanceType.EXPENSES;
                _menu.findItem(R.id.menu_qrcode).setVisible(true);
                _menu.findItem(R.id.menu_statistic).setVisible(true);
                break;
            case R.id.menu_income:
                financeMode = FinanceType.INCOME;
                _menu.findItem(R.id.menu_statistic).setVisible(true);
                break;
            case R.id.menu_moneybox:
                financeMode = FinanceType.MONEYBOX;
                _menu.findItem(R.id.menu_statistic).setVisible(true);
                break;
        }

    }

    private void setVisibleFalseItemsToolbar() {
        _menu.findItem(R.id.menu_qrcode).setVisible(false);
        _menu.findItem(R.id.menu_setting).setVisible(false);
        _menu.findItem(R.id.menu_statistic).setVisible(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        _menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_statistic:
                Intent intent = new Intent(this, StatisticsActivity.class);
                intent.putExtra("financeMode", financeMode);
                startActivity(intent);
                break;
            case R.id.menu_qrcode:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        TextInputEditText datePicker = findViewById(R.id.date_picker);
        datePicker.setText(calendar.getTime().toString());
        datePicker.setTextColor(Color.BLACK);
    }
}
