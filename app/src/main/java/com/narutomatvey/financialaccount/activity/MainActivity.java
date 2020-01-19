package com.narutomatvey.financialaccount.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.narutomatvey.financialaccount.R;
import com.narutomatvey.financialaccount.activity.activity.AddingFinanceActivity;
import com.narutomatvey.financialaccount.activity.activity.HomeActivity;
import com.narutomatvey.financialaccount.activity.helper.SPHelper;


public class MainActivity extends AppCompatActivity {

    private SPHelper sp;
    private Menu _menu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = new SPHelper(this);
        sp.checkFirstLaunch(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeActivity()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = new AddingFinanceActivity();
                    setVisibleFalseItemsToolbar();

                    switch (item.getItemId()) {
                        case R.id.menu_home:
                            _menu.findItem(R.id.menu_setting).setVisible(true);
                            selectedFragment = new HomeActivity();
                            break;
                        case R.id.menu_expenses:
                            _menu.findItem(R.id.menu_qrcode).setVisible(true);
                        case R.id.menu_income:
                        case R.id.menu_moneybox:
                            _menu.findItem(R.id.menu_statistic).setVisible(true);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

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
}
