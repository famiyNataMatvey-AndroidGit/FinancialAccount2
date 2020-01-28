package com.narutomatvey.financialaccount.activity.adabter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.narutomatvey.financialaccount.R;
import com.narutomatvey.financialaccount.activity.models.Currency;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private Currency[] currencies;

    public CurrencyAdapter(Currency[] currencies) {
        this.currencies = currencies;
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.text_frame, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        holder.bind(currencies[position].name);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return currencies.length;
    }

    class CurrencyViewHolder extends RecyclerView.ViewHolder {

        TextView currencyName;

        CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyName = itemView.findViewById(R.id.text_frame);
        }

        void bind(String name) {
            currencyName.setText(name);
        }
    }
}
