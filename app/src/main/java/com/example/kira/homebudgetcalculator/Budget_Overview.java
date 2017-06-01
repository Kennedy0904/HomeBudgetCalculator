package com.example.kira.homebudgetcalculator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

public class Budget_Overview extends AppCompatActivity {

    SQLiteDatabase dbMyBudgets;
    String strMonth, strTotal, strBudget = "";
    private TextView txtBudgets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget__overview);

        txtBudgets =(TextView)findViewById(R.id.txtBudgets);

        Runnable run = new Runnable() {
            @Override
            public void run() {
                dbMyBudgets = openOrCreateDatabase("dbMyBudgets", MODE_PRIVATE, null);
                Cursor resultSet = dbMyBudgets.rawQuery("Select * from budgets;", null);

                if(resultSet.moveToFirst()) {
                    do {
                        strMonth = resultSet.getString(resultSet.getColumnIndex("budget_month"));
                        strTotal = resultSet.getString(resultSet.getColumnIndex("budget_total"));
                        strBudget += strMonth + ":RM" + strTotal + "\n";
                    } while (resultSet.moveToNext());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtBudgets.setText(strBudget);
                        txtBudgets.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                    }
                });
            }
        };

        Thread thr = new Thread(run);
        thr.start();
    }
}
