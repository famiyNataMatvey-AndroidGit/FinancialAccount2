package com.narutomatvey.financialaccount.activity.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.narutomatvey.financialaccount.R;
import com.narutomatvey.financialaccount.activity.adabter.CurrencyAdapter;
import com.narutomatvey.financialaccount.activity.helper.DBHelper;
import com.narutomatvey.financialaccount.activity.models.Currency;

public class CurrencyChoiceActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_any_item_lists);

        DBHelper db = new DBHelper(this);
        createRecyclerView(db.getCurrencies());
    }

    private void createRecyclerView(Currency[] currencies) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CurrencyAdapter(currencies));
    }
}
