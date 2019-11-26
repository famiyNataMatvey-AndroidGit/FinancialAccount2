package com.narutomatvey.financialaccount.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;
import com.narutomatvey.financialaccount.R;
import com.narutomatvey.financialaccount.activity.helper.DBHelper;
import com.narutomatvey.financialaccount.activity.helper.SPHelper;


public class MainActivity extends AppCompatActivity {

    private DBHelper db;
    private SPHelper sp;
    private MaterialTextView balanceAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);
        sp = new SPHelper(this);
        sp.checkFirstLaunch();

        balanceAmount = findViewById(R.id.balanceAmount);

        findViewById(R.id.menu_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TestTag:", "1");
            }
        });

        findViewById(R.id.menu_income).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TestTag:", "2");
            }
        });


        findViewById(R.id.menu_expenses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TestTag:", "3");
            }
        });

        findViewById(R.id.menu_moneybox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TestTag:", "4");
            }
        });

        findViewById(R.id.menu_statistics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TestTag:", "5");
            }
        });
    }

    @Override
    protected void onStart() {
        if (sp.getBalanceStatus()) {
//            balanceAmount.setText(db.getBalance());
            sp.setBalanceFalse();
        }
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
