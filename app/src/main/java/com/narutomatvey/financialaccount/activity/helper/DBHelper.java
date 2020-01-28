package com.narutomatvey.financialaccount.activity.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.narutomatvey.financialaccount.activity.enums.FinanceType;
import com.narutomatvey.financialaccount.activity.models.Category;
import com.narutomatvey.financialaccount.activity.models.Currency;
import com.narutomatvey.financialaccount.activity.models.Statistic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "dbFinancialAccount";
    private final static int DATABASE_VERSION = 1;

    private final String TABLE_NAME_CURRENCY = "currency";
    private final String TABLE_NAME_CATEGORY = "category";
    private final String TABLE_NAME_FINANCE = "finance";

    private final String KEY_ID = "_id";
    private final String KEY_NAME = "name";

    private final String KEY_SHORT_NAME = "short_name";
    private final String KEY_COEFFICIENT = "coefficient";

    private final String KEY_CATEGORY_TYPE = "category_type";

    private final String KEY_FINANCE_TYPE = "finance_type";
    private final String KEY_FINANCE_DATE = "finance_date";
    private final String KEY_FINANCE_AMOUNT = "finance_amount";
    private final String KEY_FINANCE_COMMENT = "finance_comment";
    private final String KEY_FINANCE_CATEGORY = "category_id";
    private final String KEY_FINANCE_CURRENCY = "currency_id";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate() {
        onCreate(this.getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME_CURRENCY + "("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_NAME + " text not null,"
                + KEY_SHORT_NAME + " text,"
                + KEY_COEFFICIENT + " real not null"
                + " check (" + KEY_COEFFICIENT + " >= 0)" + ")");
        sqLiteDatabase.execSQL("create table " + TABLE_NAME_CATEGORY + "("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_NAME + " text not null,"
                + KEY_CATEGORY_TYPE + " integer not null" + ")");

        sqLiteDatabase.execSQL("create table " + TABLE_NAME_FINANCE + "("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_FINANCE_TYPE + " integer not null"
                + " check (" + KEY_FINANCE_TYPE + " >= 1 and " + KEY_FINANCE_TYPE + " <= 3),"
                + KEY_FINANCE_DATE + " integer not null,"
                + KEY_FINANCE_COMMENT + " text,"
                + KEY_FINANCE_AMOUNT + " real not null" + " check (" + KEY_FINANCE_AMOUNT + " >= 0),"
                + KEY_FINANCE_CATEGORY + " integer not null,"
                + KEY_FINANCE_CURRENCY + " integer not null,"
                + "foreign key "
                + "(" + KEY_FINANCE_CATEGORY + ")" + " references "
                + TABLE_NAME_CATEGORY + "(" + "id" + "),"
                + "foreign key "
                + "(" + KEY_FINANCE_CURRENCY + ")" + " references "
                + TABLE_NAME_CURRENCY + "(" + "id" + ")" + ")");
        onBaseInsertDatabase(sqLiteDatabase);
    }

    private void onBaseInsertDatabase(SQLiteDatabase database) {
        String[] incomeCategoryNames = {
                "Аванс", "Зарплата", "Пенсия", "Подарки", "Помощь",
                "Премия", "Выйгрыш", "Подработка", "Степендия"
        };
        String[] expenseCategoryNames = {
                "Фрукты", "Овощи", "Кисломолочные продукты", "Напитки",
                "Мебель", "Отпуск", "Хозяйственные расходы", "Лекарства и медицина", "Транспорт",
                "Крупная бытовая техника", "Одежда", "Алкоголь", "Мясо и птица", "Рыба и морепродукты"
        };

        for (String incomeCategoryName : incomeCategoryNames) {
            ContentValues cv = createCategory(incomeCategoryName, FinanceType.INCOME);
            database.insert(TABLE_NAME_CATEGORY, null, cv);
        }

        for (String expenseCategoryName : expenseCategoryNames) {
            ContentValues cv = createCategory(expenseCategoryName, FinanceType.EXPENSES);
            database.insert(TABLE_NAME_CATEGORY, null, cv);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME_CURRENCY);
            sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME_CATEGORY);
            sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME_FINANCE);
            onCreate(sqLiteDatabase);
        }
    }

    private ContentValues createCategory(String name, FinanceType type) {
        ContentValues contentCategoryValues = new ContentValues();
        contentCategoryValues.put(KEY_NAME, name);
        contentCategoryValues.put(KEY_CATEGORY_TYPE, type.ordinal());
        return contentCategoryValues;
    }

    public float getBalance() {
        String[] columns = {
                "CASE WHEN " +
                        TABLE_NAME_FINANCE + "." + KEY_FINANCE_TYPE + " = " + FinanceType.INCOME.ordinal() +
                        " THEN " + TABLE_NAME_FINANCE + "." + KEY_FINANCE_AMOUNT +
                        " ELSE (-1 * " + TABLE_NAME_FINANCE + "." + KEY_FINANCE_AMOUNT
                        + ") END AS balance"};

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME_FINANCE, columns, null, null, null, null, null);
        int balanceIndex = cursor.getColumnIndex("balance");

        float total_balance = 0;
        if (cursor.moveToFirst()) {
            do {
                total_balance += cursor.getFloat(balanceIndex);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return total_balance;
    }

    private Cursor getDataBaseMethod(String table_name, @Nullable String[] columns, @Nullable String selection, @Nullable String groupBy) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(table_name, columns, selection, null, groupBy, null, null);
    }

    private Currency createCurrency(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(KEY_ID);
        int nameIndex = cursor.getColumnIndex(KEY_NAME);
        int shortNameIndex = cursor.getColumnIndex(KEY_SHORT_NAME);
        int coefficientIndex = cursor.getColumnIndex(KEY_COEFFICIENT);

        return new Currency(cursor.getInt(idIndex),
                cursor.getString(nameIndex),
                cursor.getString(shortNameIndex),
                cursor.getFloat(coefficientIndex));
    }

    public Currency getCurrency(int pk) {
        Cursor cursor = getDataBaseMethod(TABLE_NAME_CURRENCY, null, KEY_ID + " = " + pk, null);
        if (cursor.moveToFirst()) {
            return createCurrency(cursor);
        }
        return null;
    }

    public Currency[] getCurrencies() {
        Cursor cursor = getDataBaseMethod(TABLE_NAME_CURRENCY, null, null, null);
        Currency[] currencies = new Currency[cursor.getCount()];

        if (cursor.moveToFirst()) {
            int index = 0;
            do {
                currencies[index++] = createCurrency(cursor);
            } while (cursor.moveToNext());
        }
        return currencies;
    }

    private Category createCategory(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(KEY_ID);
        int nameIndex = cursor.getColumnIndex(KEY_NAME);
        int categoryTypeIndex = cursor.getColumnIndex(KEY_CATEGORY_TYPE);

        return new Category(cursor.getInt(idIndex),
                cursor.getString(nameIndex),
                FinanceType.values()[cursor.getInt(categoryTypeIndex)]);
    }

    public Category getCategory(int pk) {
        Cursor cursor = getDataBaseMethod(TABLE_NAME_CATEGORY, null, KEY_ID + " = " + pk, null);
        if (cursor.moveToFirst()) {
            return createCategory(cursor);
        }
        return null;
    }

    public Category[] getCategories(FinanceType type, String categoryName) {
        String selection = KEY_CATEGORY_TYPE + " = " + type.ordinal();
        if (categoryName != null) {
            selection += " AND " + KEY_NAME + " LIKE '" + categoryName + "%'";
        }

        Cursor cursor = getDataBaseMethod(TABLE_NAME_CATEGORY, null, selection, null);
        Category[] categories = new Category[cursor.getCount()];

        if (cursor.moveToFirst()) {
            int index = 0;
            do {
                categories[index++] = createCategory(cursor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    public int createCategory(Category category) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = createCategory(category.getName(), category.getType());
        return (int) database.insert(TABLE_NAME_CATEGORY, null, cv);
    }

    public List<Statistic> getStatisticsByCategory(FinanceType type, Date start, Date end) {
        String AS_CATEGORY_NAME = "category_name";
        String AS_FINANCE_SUM = "amount_sum";

        String groupBy = TABLE_NAME_FINANCE + "." + KEY_FINANCE_CATEGORY;
        String table = TABLE_NAME_FINANCE + " left outer join " + TABLE_NAME_CATEGORY + " on "
                + TABLE_NAME_FINANCE + "." + KEY_FINANCE_CATEGORY + " = "
                + TABLE_NAME_CATEGORY + "." + KEY_ID;
        String[] columns = {
                TABLE_NAME_CATEGORY + "." + KEY_NAME + " as " + AS_CATEGORY_NAME,
                "sum(" + TABLE_NAME_FINANCE + "." + KEY_FINANCE_AMOUNT + ") as " + AS_FINANCE_SUM,
                TABLE_NAME_FINANCE + "." + KEY_FINANCE_CATEGORY};

        String selection = TABLE_NAME_FINANCE + "." + KEY_FINANCE_TYPE + " = " + type.ordinal();
        if (start != null & end != null) {
            selection += " AND ";
            selection += TABLE_NAME_FINANCE + "." + KEY_FINANCE_DATE
                    + " BETWEEN " + start.getTime()
                    + " AND " + end.getTime();
        } else if (end != null) {
            selection += " AND ";
            selection += TABLE_NAME_FINANCE + "." + KEY_FINANCE_DATE + " = " + end.getTime();
        }

        Cursor cursor = getDataBaseMethod(table, columns, selection, groupBy);

        List<Statistic> statistics = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int categoryIndex = cursor.getColumnIndex(KEY_FINANCE_CATEGORY);
            int categoryNameIndex = cursor.getColumnIndex(AS_CATEGORY_NAME);
            int sumIndex = cursor.getColumnIndex(AS_FINANCE_SUM);
            do {
                statistics.add(new Statistic(
                        new Category(
                                cursor.getInt(categoryIndex),
                                cursor.getString(categoryNameIndex),
                                type
                        ),
                        cursor.getDouble(sumIndex)
                ));

            } while (cursor.moveToNext());
            cursor.close();
        }
        return statistics;
    }
}
