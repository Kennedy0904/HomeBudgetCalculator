package com.example.kira.homebudgetcalculator;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Joshua Yang on 16/3/2017.
 */

public class ExpensesDB extends SQLiteOpenHelper {
    public static final String dbName = "dbMyBudgets";
    public static final String tblName = "budgets";
    public static final String colBudget_month = "budget_month";
    public static final String colBudget_total = "budget_total";
    public static final String colStatus = "status";

    public ExpensesDB(Context context){
        super(context,dbName,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS budgets(budget_month VARCHAR, budget_total VARCHAR, status VARCHAR);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS budgets");
        onCreate(db);
    }

    public Cursor getDataById(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from "+tblName,null);

        return cur;
    }

    public void fnExecuteSql(String strSql, Context appContext)
    {
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL(strSql);

        }catch (Exception e){
            Log.d("unable to run query", "error!");
        }
    }

    public  int fnTotalRow()
    {
        int intRow;
        SQLiteDatabase db = this.getReadableDatabase();
        intRow = (int) DatabaseUtils.queryNumEntries(db,tblName);

        return  intRow;
    }
}
