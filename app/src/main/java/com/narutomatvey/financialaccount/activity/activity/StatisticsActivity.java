package com.narutomatvey.financialaccount.activity.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.narutomatvey.financialaccount.R;
import com.narutomatvey.financialaccount.activity.enums.DatePickerType;
import com.narutomatvey.financialaccount.activity.enums.FinanceType;
import com.narutomatvey.financialaccount.activity.fragment.CalendarFragment;
import com.narutomatvey.financialaccount.activity.fragment.PieGraph;
import com.narutomatvey.financialaccount.activity.helper.DBHelper;
import com.narutomatvey.financialaccount.activity.helper.SPHelper;
import com.narutomatvey.financialaccount.activity.models.Statistic;

import org.achartengine.GraphicalView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private DBHelper db;
    private SPHelper sp;

    private Date dateFrom = null;
    private Date dateTo = null;

    private FinanceType financeMode = null;
    private DatePickerType dataPickerMode = null;

    private LinearLayout standardFilterByDate = null;
    private ImageButton stepBack = null;
    private TextView textFilterByDate = null;
    private ImageButton standardFilterDataPicker = null;
    private ImageButton stepForward = null;

    private LinearLayout customFilterByDate = null;
    private TextView textFilterByDateFrom = null;
    private ImageButton customFilterDataPickerFrom = null;
    private TextView textFilterByDateTo = null;
    private ImageButton customFilterDataPickerTo = null;

    private FrameLayout graphFrame = null;
    private RecyclerView amountByCategoryList = null;

    private boolean isDatePickerTo = true;
    private boolean isDataChanged = true;
    private PieGraph pieGraph;

    private List<String> categoryNames = new ArrayList<String>();
    private List<Double> categoryAmounts = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        financeMode = (FinanceType) getIntent().getExtras().getSerializable("financeMode");

        db = new DBHelper(this);
        sp = new SPHelper(this);
        pieGraph = new PieGraph(this);

        standardFilterByDate = findViewById(R.id.standard_filter_by_date);
        textFilterByDate = findViewById(R.id.text_filter_by_date);
        standardFilterDataPicker = findViewById(R.id.standard_filter_data_picker);
        stepBack = findViewById(R.id.step_back);
        stepForward = findViewById(R.id.step_forward);

        stepBack.setOnClickListener(moveBack);
        stepForward.setOnClickListener(moveForward);

        customFilterByDate = findViewById(R.id.custom_filter_by_date);
        textFilterByDateFrom = findViewById(R.id.text_filter_by_date_from);
        customFilterDataPickerFrom = findViewById(R.id.custom_filter_data_picker_from);
        textFilterByDateTo = findViewById(R.id.text_filter_by_date_to);
        customFilterDataPickerTo = findViewById(R.id.custom_filter_data_picker_to);

        customFilterDataPickerFrom.setOnClickListener(startCalendarFragmentFrom);
        customFilterDataPickerTo.setOnClickListener(startCalendarFragmentTo);
        standardFilterDataPicker.setOnClickListener(startCalendarFragmentTo);

        graphFrame = findViewById(R.id.graph_frame);
        amountByCategoryList = findViewById(R.id.amount_by_category_list);

        dataPickerMode = sp.getDataPickerType();
        customizedView();
    }

    private View.OnClickListener startCalendarFragmentTo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isDatePickerTo = true;
            startCalendarFragment();
        }
    };

    private View.OnClickListener startCalendarFragmentFrom = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isDatePickerTo = false;
            startCalendarFragment();
        }
    };

    private View.OnClickListener moveBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateDate(-1);
            setFilterText();
        }
    };

    private View.OnClickListener moveForward = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateDate(1);
            setFilterText();
        }
    };

    private void startCalendarFragment(){
        FragmentManager manager = getSupportFragmentManager();
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.show(manager, "dialog");
    }


    private void customizedView() {
        this.setTitle(dataPickerMode.getTitle());
        Calendar calendar = Calendar.getInstance();
        dateFrom = null;
        dateTo = calendar.getTime();

        standardFilterByDate.setVisibility(View.GONE);
        customFilterByDate.setVisibility(View.GONE);
        isDataChanged = true;
        switch (dataPickerMode) {
            case PERIOD_ALL:
                isDataChanged = false;
                dateTo = null;
                break;
            case PERIOD:
                dateFrom = (Date) dateTo.clone();
                customFilterByDate.setVisibility(View.VISIBLE);
                break;
            default:
                standardFilterByDate.setVisibility(View.VISIBLE);
        }
        if(dataPickerMode != DatePickerType.PERIOD_ALL){
            updateDate(0);
        }
        setFilterText();
    }

    private void updateDate(int sign){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTo);
        isDataChanged = true;
        switch (dataPickerMode) {
            case DAY:
                calendar.add(Calendar.DAY_OF_MONTH, sign);
                dateTo = calendar.getTime();
                break;
            case WEEK:
                calendar.add(Calendar.WEEK_OF_MONTH, sign);
                dateTo = calendar.getTime();
                calendar.add(Calendar.WEEK_OF_MONTH, -1);
                dateFrom = calendar.getTime();
                break;
            case MONTH:
                calendar.add(Calendar.MONTH, sign);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                dateTo = calendar.getTime();
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                dateFrom = calendar.getTime();
                break;
            case YEAR:
                calendar.add(Calendar.YEAR, sign);
                calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
                dateTo = calendar.getTime();
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                dateFrom = calendar.getTime();
                break;
        }
    }

    private void setFilterText() {
        switch (dataPickerMode) {
            case WEEK:
                textFilterByDate.setText(String.format("%s - %s", dataPickerMode.getDateFrom(dateFrom), dataPickerMode.getDateTo(dateTo)));
                break;
            case DAY:
            case MONTH:
            case YEAR:
                textFilterByDate.setText(dataPickerMode.getDateTo(dateTo));
                break;
            case PERIOD:
                textFilterByDateFrom.setText(dataPickerMode.getDateFrom(dateFrom));
                textFilterByDateTo.setText(dataPickerMode.getDateTo(dateTo));
                break;
            default:
                break;
        }
        fillInGraph();
    }

    @Override
    protected void onStop() {
        sp.setDataPickerType(dataPickerMode);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statistics_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_day:
                dataPickerMode = DatePickerType.DAY;
                break;
            case R.id.menu_week:
                dataPickerMode = DatePickerType.WEEK;
                break;
            case R.id.menu_month:
                dataPickerMode = DatePickerType.MONTH;
                break;
            case R.id.menu_year:
                dataPickerMode = DatePickerType.YEAR;
                break;
            case R.id.menu_period:
                dataPickerMode = DatePickerType.PERIOD;
                break;
            default:
                dataPickerMode = DatePickerType.PERIOD_ALL;
                break;
        }
        customizedView();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if(isDatePickerTo){
            dateTo = calendar.getTime();
            updateDate(0);
        } else {
            dateFrom = calendar.getTime();
        }

        setFilterText();
    }

    private void fillInGraph(){
        if (isDataChanged){
            isDataChanged = false;
            categoryNames.clear();
            categoryAmounts.clear();
            List <Statistic> statistics = db.getStatisticsByCategory(financeMode, dateFrom, dateTo);
            for(int i = 0; i < statistics.size(); i++){
                Statistic statistic = statistics.get(i);
                categoryNames.add(statistic.getCategoryName());
                categoryAmounts.add(statistic.getAmount());
            }
            generateGraph(categoryNames, categoryAmounts);
        }
    }

    private void generateGraph(List<String> categoryNames, List<Double> categoryAmounts){
        GraphicalView graphicalView = pieGraph.getGraphicalView(categoryNames, categoryAmounts);
        graphFrame.removeAllViews();
        graphFrame.addView(graphicalView);
    }
}
