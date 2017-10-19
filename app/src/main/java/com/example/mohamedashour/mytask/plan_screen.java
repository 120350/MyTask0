package com.example.mohamedashour.mytask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class plan_screen extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    SeekBar seekBar;
    TextView seekBarVal;
    AutoCompleteTextView locationEditText;
    CheckBox locationCheckBox;
    EditText moneyEditText;

    ArrayList<Plans> plansList;

    int step = 50;
    int max = 1000;
    int min = 100;
    long plansCounter = 0;
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_screen);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        // define widgets
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBarVal = (TextView) findViewById(R.id.seekBarVal);
        locationEditText = (AutoCompleteTextView) findViewById(R.id.locationEditText);
        locationCheckBox = (CheckBox) findViewById(R.id.locationRadioButton);
        moneyEditText = (EditText) findViewById(R.id.moneyEditText);

        // get autocomplete list of places
        String[] cities = getResources().getStringArray(R.array.cities);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        locationEditText.setAdapter(adapter);

        // initialize seekbar
        seekBar.setMax((max - min) / step);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = min + (progress * step);
                // update text above seekbar
                seekBarVal.setText(value+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //get updating plans
        getUpdatingPlans();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.next:
                // get data from user and pass it to main activity for displaying
                nextMethod();
                break;
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void getUpdatingPlans(){
        try{
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("plans", " ");
            if (!json.equals(" ")){
                // get old plans first then add new ones
                Type type = new TypeToken<ArrayList<Plans>>() {}.getType();
                plansList = gson.fromJson(json, type);
            }else {
                plansList = new ArrayList<>();
            }

        }catch (Exception e){
            Log.e("mnnnnnnnnn", e.getMessage());
        }
    }
    public void nextMethod(){
        if (!moneyEditText.getText().toString().equals("")){
            int num = Integer.parseInt(moneyEditText.getText().toString());
            if (num < 10000 || num > 100000000){
                Toast.makeText(plan_screen.this, "Please Enter range between 10000 & 100000000", Toast.LENGTH_SHORT).show();
            }else if ( locationEditText.getText().toString().equals("") && !locationCheckBox.isChecked()){
                Toast.makeText(plan_screen.this, "Please Enter Your Location", Toast.LENGTH_SHORT).show();
            }else {
                try {
                    // create new plans model save data with it and add it to plans arraylist
                    Plans p = new Plans();

                    // way to set plans names alphabetically
                    plansCounter = PreferenceManager.getDefaultSharedPreferences(this).getLong("counter", 0);
                    if (plansCounter > 26){
                        plansCounter = 0;
                        PreferenceManager.getDefaultSharedPreferences(plan_screen.this).edit().putLong("counter", plansCounter).apply();
                    }else {
                        p.setPlanName(alphabet.charAt((int) plansCounter)+"");
                        plansCounter++;
                        PreferenceManager.getDefaultSharedPreferences(plan_screen.this).edit().putLong("counter", plansCounter).apply();
                    }
                    if (locationCheckBox.isChecked()){
                        p.setLocation("Any");
                    }else{
                        p.setLocation(locationEditText.getText().toString());
                    }
                    p.setNoFriends((min + (seekBar.getProgress() * step)) + "");
                    p.setMoney(moneyEditText.getText().toString());

                    plansList.add(p);

                    // save latest plans before leaving
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(plan_screen.this);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();

                    String json = gson.toJson(plansList);

                    editor.putString("plans", json);
                    editor.commit();

                    // start new activity
                    Intent intent = new Intent(plan_screen.this, MainActivity.class);
                    startActivity(intent);
                }catch (Exception e){
                    Log.e("mmmmmmmmmmmm", e.getMessage());
                }
            }
        }
        else if (locationEditText.getText().toString().equals("") || moneyEditText.getText().toString().equals("")){
            Toast.makeText(plan_screen.this, "Please Enter Your Needs Correctly", Toast.LENGTH_SHORT).show();
        }
    }
}
