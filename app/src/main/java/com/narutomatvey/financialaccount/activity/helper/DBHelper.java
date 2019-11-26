package com.narutomatvey.financialaccount.activity.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.narutomatvey.financialaccount.activity.enums.FinanceType;

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
                + KEY_FINANCE_DATE + " text not null,"
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
            ContentValues contentCategoryValues = new ContentValues();
            contentCategoryValues.put(KEY_NAME, incomeCategoryName);
            contentCategoryValues.put(KEY_CATEGORY_TYPE, FinanceType.INCOME.ordinal());
            database.insert(TABLE_NAME_CATEGORY, null, contentCategoryValues);
        }

        for (String expenseCategoryName : expenseCategoryNames) {
            ContentValues contentCategoryValues = new ContentValues();
            contentCategoryValues.put(KEY_NAME, expenseCategoryName);
            contentCategoryValues.put(KEY_CATEGORY_TYPE, FinanceType.EXPENSES.ordinal());
            database.insert(TABLE_NAME_CATEGORY, null, contentCategoryValues);
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
}
