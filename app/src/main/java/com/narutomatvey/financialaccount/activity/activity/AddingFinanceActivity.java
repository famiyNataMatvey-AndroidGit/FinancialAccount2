package com.narutomatvey.financialaccount.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.narutomatvey.financialaccount.R;
import com.narutomatvey.financialaccount.activity.MainActivity;
import com.narutomatvey.financialaccount.activity.enums.FinanceType;
import com.narutomatvey.financialaccount.activity.fragment.CalendarFragment;

public class AddingFinanceActivity extends Fragment {
    private MainActivity _main;
    private FinanceType financeMode;

    public AddingFinanceActivity(FinanceType financeMode) {
        this.financeMode = financeMode;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_adding_finance, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _main = (MainActivity) getActivity();

        if (financeMode.ordinal() == FinanceType.EXPENSES.ordinal()) {
            TextInputLayout inputComment = _main.findViewById(R.id.input_comment);
            inputComment.setVisibility(View.VISIBLE);
        }

        TextInputEditText datePicker = _main.findViewById(R.id.date_picker);
        datePicker.setOnClickListener(dateClick);

        TextInputEditText currency = _main.findViewById(R.id.currency);
        currency.setOnClickListener(currencyClick);

        TextInputEditText category = _main.findViewById(R.id.category);
        category.setOnClickListener(categoryClick);

    }

    private View.OnClickListener dateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager manager = _main.getSupportFragmentManager();
            CalendarFragment calendarFragment = new CalendarFragment();
            calendarFragment.show(manager, "dialog");
        }
    };

    private View.OnClickListener currencyClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(_main, CurrencyChoiceActivity.class));
        }
    };

    private View.OnClickListener categoryClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(_main, CategoryChoiceActivity.class);
            intent.putExtra("financeMode", financeMode);
            startActivity(intent);
        }
    };


}
