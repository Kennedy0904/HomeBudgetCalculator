package com.example.kira.homebudgetcalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BudgetMainActivity extends AppCompatActivity {
    static final String[] Months = new String[] { "January", "February",
            "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December" };
    EditText edWaterBills,edGasBills,edRents,edElectricals;
    Spinner edMonths;
    TextView totalBudget, status;
    SQLiteDatabase dbMyBudgets;
    WebServiceCall wsc = new WebServiceCall();
    ExpensesDB dbMyExpenses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbMyExpenses = new ExpensesDB(getApplicationContext());
        edWaterBills= (EditText) findViewById(R.id.idWaterBills);
        edGasBills=(EditText) findViewById(R.id.idGasBill);
        edRents= (EditText) findViewById(R.id.idRental);
        edElectricals =(EditText) findViewById(R.id.idElectricalBill);
        edMonths =(Spinner) findViewById(R.id.idMonth);
        totalBudget=(TextView) findViewById(R.id.idTotal);
        status=(TextView) findViewById(R.id.status);

        //dbMyBudgets = openOrCreateDatabase("dbMyBudget",MODE_PRIVATE,null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder inputAlert = new AlertDialog.Builder(BudgetMainActivity.this);
                inputAlert.setTitle("Estimation Budget");
                inputAlert.setMessage("Set Your Estimation Budget");

                final View ken=getLayoutInflater().inflate(R.layout.content_estimation_budget,null);

                inputAlert.setPositiveButton("Save", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        EditText estBudget= (EditText) ken.findViewById(R.id.idEstBudget);
                        final String budget= estBudget.getText().toString();
                        Spinner monthSpinner = (Spinner) findViewById(R.id.idMonth);
                        final String month = monthSpinner.getSelectedItem().toString();

                        Runnable run = new Runnable() {
                            @Override
                            public void run() {
                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("selectFn","saveEstimate"));
                                params.add(new BasicNameValuePair("month",month));
                                params.add(new BasicNameValuePair("estimate",budget));
                                try {
                                    final String respond = wsc.makeHttpRequest(wsc.fnGetURL(), "POST", params);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            toastMsg(respond);
                                        }
                                    });
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        };
                        Thread thread = new Thread(run);
                        thread.start();
                    }
                    });

                inputAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                inputAlert.setView(ken);
                AlertDialog alertDialog = inputAlert.create();
                alertDialog.show();
            }
        });


        ArrayAdapter<String> adapterMonths = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Months);
        adapterMonths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinYear = (Spinner)findViewById(R.id.idMonth);
        spinYear.setAdapter(adapterMonths);
    }

    public void toastMsg(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    public void fnCalculate(View view)
    {
        Runnable run = new Runnable() {

            @Override
            public void run() {
                double waterBills = Double.parseDouble(edWaterBills.getText().toString());
                double gasBills = Double.parseDouble(edGasBills.getText().toString());
                double electrilBills = Double.parseDouble(edElectricals.getText().toString());
                double rent = Double.parseDouble(edRents.getText().toString());
                double budget = 0;
                budget = waterBills + gasBills + electrilBills + rent;
                final double finalSum = budget;
                Spinner monthSpinner = (Spinner) findViewById(R.id.idMonth);
                final String month = monthSpinner.getSelectedItem().toString();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("selectFn","calEstimate"));
                params.add(new BasicNameValuePair("month",month));
                params.add(new BasicNameValuePair("actual",Double.toString(finalSum)));
                try {
                    final String respond = wsc.makeHttpRequest(wsc.fnGetURL(), "POST", params);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String total = String.valueOf(finalSum);

                            totalBudget.setText(total);
                            toastMsg(respond);
                            status.setText(respond);


                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }






            }
        };
        Thread thrSave=new Thread(run);
        thrSave.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_burget_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.idListBudget) {
            Intent intent= new Intent(this,BudgetListActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.idBudgetOverview) {
            Intent intent= new Intent(this,Budget_Overview.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fnSave(View  vw)
    {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                String strMonth = edMonths.getSelectedItem().toString();
                String strTotal = totalBudget.getText().toString();
                String strStatus = status.getText().toString();

                //dbMyBudgets.execSQL("CREATE TABLE IF NOT EXISTS budgets(budget_month VARCHAR, budget_total VARCHAR);");
                String strQry = "Insert into "+ExpensesDB.tblName+" values('"+strMonth+"','"+strTotal+"', '"+strStatus+"');";
                dbMyExpenses.fnExecuteSql(strQry,getApplicationContext());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast showSuccess = Toast.makeText(getApplicationContext(),"Information Successfully Saved! ", Toast.LENGTH_SHORT);
                        showSuccess.show();
                    }
                });
            }
        };

        Thread thrSave = new Thread(run);
        thrSave.start();
    }
}
