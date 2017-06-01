package com.example.kira.homebudgetcalculator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BudgetListActivity extends AppCompatActivity {

    private static  final String strMonth = ExpensesDB.colBudget_month;
    private static  final String strTotal = ExpensesDB.colBudget_total;
    private static  final String strStatus = ExpensesDB.colStatus;


    ExpensesDB dbExpense;
    ListView lvExp;
    ArrayList<HashMap<String, String>> alExp;
    WebServiceCall wsc = new WebServiceCall();
    JSONObject jsnObj = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_list);

        dbExpense = new ExpensesDB(getApplicationContext());
        lvExp = (ListView) findViewById(R.id.listViewBudget);
        alExp = new ArrayList<HashMap<String, String>>();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                String strSql = "Select * from " + ExpensesDB.tblName;
                Cursor currExp = dbExpense.getReadableDatabase().rawQuery(strSql, null);
                while (currExp.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(strMonth, currExp.getString(currExp.getColumnIndex(ExpensesDB.colBudget_month)));
                    map.put(strTotal, currExp.getString(currExp.getColumnIndex(ExpensesDB.colBudget_total)));
                    map.put(strStatus, currExp.getString(currExp.getColumnIndex(ExpensesDB.colStatus)));


                    alExp.add(map);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListAdapter adapter = new SimpleAdapter(BudgetListActivity.this, alExp, R.layout.content_budget_info,
                                new String[]{strMonth, strTotal, strStatus},
                                new int[]{R.id.txtViewMonth, R.id.txtViewTotal, R.id.txtViewStatus});
                        lvExp.setAdapter(adapter);
                    }
                });
            }
        };
        Thread thr = new Thread(run);
        thr.start();


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_budget_list);
//
//
//
//    }
    }
}
