package com.narutomatvey.financialaccount.activity.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.narutomatvey.financialaccount.R;
import com.narutomatvey.financialaccount.activity.adabter.CategoryAdapter;
import com.narutomatvey.financialaccount.activity.enums.FinanceType;
import com.narutomatvey.financialaccount.activity.helper.DBHelper;
import com.narutomatvey.financialaccount.activity.models.Category;

public class CategoryChoiceActivity extends AppCompatActivity {

    TextInputLayout search_category_text_input;
    TextInputEditText search_category_name;
    Category[] categories;
    DBHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_any_item_lists);

        db = new DBHelper(this);
        categories = db.getCategories(FinanceType.INCOME, null);

        createRecyclerView(categories);

        FrameLayout searchCategoryFrame = findViewById(R.id.search_category_frame);
        searchCategoryFrame.setVisibility(View.VISIBLE);
        search_category_text_input = findViewById(R.id.search_category_text_input);
        search_category_name = findViewById(R.id.search_category_name);
        search_category_name.addTextChangedListener(testListener);
        bindButton();

    }

    private TextWatcher testListener =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.d("MY_TAG", "Hf");
                    FloatingActionButton categoryAddingButton = findViewById(R.id.category_adding_button);
                    if (editable.length() == 0) {
                        categoryAddingButton.setImageResource(R.drawable.ic_close);
                        categoryAddingButton.setOnClickListener(categorySearchCloseListener);
                    } else if (editable.length() == 1) {
                        categoryAddingButton.setImageResource(R.drawable.ic_plus);
                        categoryAddingButton.setOnClickListener(categoryAddListener);
                    }
                    categories = db.getCategories(FinanceType.INCOME, String.valueOf(editable));
                    Log.d("MY_TAG", String.valueOf(categories.length));
                }
            };

    private void createRecyclerView(Category[] categories) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CategoryAdapter(categories));
    }

    private void bindButton() {
        FloatingActionButton categoryAddingButton = findViewById(R.id.category_adding_button);
        categoryAddingButton.setOnClickListener(categorySearchListener);
        categoryAddingButton.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener categorySearchListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FloatingActionButton button = (FloatingActionButton) view;

                    button.setImageResource(R.drawable.ic_close);
                    search_category_text_input.setVisibility(View.VISIBLE);
                    button.setOnClickListener(categorySearchCloseListener);

                }
            };

    private View.OnClickListener categorySearchCloseListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FloatingActionButton button = (FloatingActionButton) view;

                    button.setImageResource(R.drawable.ic_search);
                    search_category_text_input.setVisibility(View.GONE);
                    button.setOnClickListener(categorySearchListener);

                }
            };

    private View.OnClickListener categoryAddListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Category test_category = new Category(null, search_category_name.getText().toString(), FinanceType.INCOME);
                    test_category.createNewCategory(db);
                    finish();
                }
            };
}
