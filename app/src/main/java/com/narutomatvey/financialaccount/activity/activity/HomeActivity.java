package com.narutomatvey.financialaccount.activity.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;
import com.narutomatvey.financialaccount.R;
import com.narutomatvey.financialaccount.activity.MainActivity;

public class HomeActivity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    @Override
    public void onStart() {
        MainActivity main = (MainActivity) getActivity();
        MaterialTextView balance = main.findViewById(R.id.balance_amount);
        MaterialTextView income= main.findViewById(R.id.balance_income);
        balance.setText("7000");
        income.setText("6999");
        super.onStart();
    }
}
