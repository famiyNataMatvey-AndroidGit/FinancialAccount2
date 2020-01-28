package com.narutomatvey.financialaccount.activity.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    DBHelper db;
    private Category[] categories;
    private RecyclerView recyclerView ;
    private FinanceType financeMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_any_item_lists);

        financeMode = (FinanceType) getIntent().getExtras().getSerializable("financeMode");
        db = new DBHelper(this);
        categories = db.getCategories(financeMode, null);

        createRecyclerView(categories);

        FrameLayout searchCategoryFrame = findViewById(R.id.search_category_frame);
        searchCategoryFrame.setVisibility(View.VISIBLE);
        search_category_text_input = findViewById(R.id.search_category_text_input);
        search_category_name = findViewById(R.id.search_category_name);
        search_category_name.addTextChangedListener(categoryChangedListener);
        bindButton();

    }

    private TextWatcher categoryChangedListener =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    FloatingActionButton categoryAddingButton = findViewById(R.id.category_adding_button);
                    if (editable.length() == 0) {
                        categoryAddingButton.setImageResource(R.drawable.ic_close);
                        categoryAddingButton.setOnClickListener(categorySearchCloseListener);
                    } else if (editable.length() == 1) {
                        categoryAddingButton.setImageResource(R.drawable.ic_plus);
                        categoryAddingButton.setOnClickListener(categoryAddListener);
                    }
                    categories = db.getCategories(financeMode, String.valueOf(editable));
                    recyclerView.setAdapter(new CategoryAdapter(categories));
                }
            };

    private void createRecyclerView(Category[] categories) {
        recyclerView = findViewById(R.id.recycler_view);
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
                    Category new_category = new Category(null, search_category_name.getText().toString(), financeMode);
                    new_category.createNewCategory(db);
                    finish();
                }
            };
}
