package com.narutomatvey.financialaccount.activity.adabter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.narutomatvey.financialaccount.R;
import com.narutomatvey.financialaccount.activity.models.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Category[] categories;

    public CategoryAdapter(Category[] categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.text_frame, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Log.d("MY_TAG", "Element " + position + " set.");
        holder.bind(categories[position].getName());
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.text_frame);
        }

        void bind(String name) {
            categoryName.setText(name);
        }
    }
}
