package com.narutomatvey.financialaccount.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.narutomatvey.financialaccount.R;
import com.narutomatvey.financialaccount.activity.activity.AddingFinanceActivity;
import com.narutomatvey.financialaccount.activity.activity.CategoryChoiceActivity;
import com.narutomatvey.financialaccount.activity.activity.HomeActivity;
import com.narutomatvey.financialaccount.activity.helper.SPHelper;


public class MainActivity extends AppCompatActivity {

    private SPHelper sp;
    private MaterialTextView balanceAmount;

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
                    Fragment setectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.menu_home:
                            setectedFragment = new HomeActivity();
                            break;
                        case R.id.menu_income:
                        case R.id.menu_expenses:
                        case R.id.menu_moneybox:
                            setectedFragment = new AddingFinanceActivity();
                            break;
                    }
//                    Intent intent = new Intent(MainActivity.this, CategoryChoiceActivity.class);
//                    startActivity(intent);

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            setectedFragment).commit();
                    return true;
                }
            };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
