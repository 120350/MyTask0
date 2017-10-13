package com.example.mohamedashour.mytask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class plan_screen extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    SeekBar seekBar;
    TextView seekBarVal;
    AutoCompleteTextView locationEditText;
    RadioButton locationRadioButton;
    EditText moneyEditText;

    ArrayList<Plans> plansList;

    int step = 50;
    int max = 1000;
    int min = 100;
    boolean checked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_screen);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBarVal = (TextView) findViewById(R.id.seekBarVal);
        locationEditText = (AutoCompleteTextView) findViewById(R.id.locationEditText);
        locationRadioButton = (RadioButton) findViewById(R.id.locationRadioButton);
        moneyEditText = (EditText) findViewById(R.id.moneyEditText);

        String[] cities = getResources().getStringArray(R.array.cities);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        locationEditText.setAdapter(adapter);
        locationRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationRadioButton.isChecked() && checked) {
                    locationEditText.setAdapter(adapter);
                    locationRadioButton.setChecked(false);
                    checked = false;
                } else {
                    locationRadioButton.setChecked(true);
                    locationEditText.setAdapter(null);
                    checked = true;
                }
            }
        });

        seekBar.setMax((max - min) / step);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = min + (progress * step);
                seekBarVal.setText(value+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        try{
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("plans", " ");
            if (!json.equals(" ")){
                Type type = new TypeToken<ArrayList<Plans>>() {}.getType();
                plansList = gson.fromJson(json, type);
            }else {
                plansList = new ArrayList<>();
            }

        }catch (Exception e){
            Log.e("mnnnnnnnnn", e.getMessage());
        }
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
                if (!moneyEditText.getText().toString().equals("")){
                    int num = Integer.parseInt(moneyEditText.getText().toString());
                    if (num < 10000 || num > 100000000){
                        Toast.makeText(plan_screen.this, "Please Enter range between 10000 & 100000000", Toast.LENGTH_SHORT).show();
                    }
                }
                if (locationEditText.getText().toString().equals("") || moneyEditText.getText().toString().equals("")){
                    Toast.makeText(plan_screen.this, "Please Enter Your Needs Correctly", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        Plans p = new Plans();

                        p.setLocation(locationEditText.getText().toString());
                        p.setNoFriends((min + (seekBar.getProgress() * step)) + "");
                        p.setMoney(moneyEditText.getText().toString());

                        plansList.add(p);

                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(plan_screen.this);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        Gson gson = new Gson();

                        String json = gson.toJson(plansList);

                        editor.putString("plans", json);
                        editor.commit();


                        Intent intent = new Intent(plan_screen.this, MainActivity.class);
                        startActivity(intent);
                    }catch (Exception e){
                        Log.e("mmmmmmmmmmmm", e.getMessage());
                    }
                }
                break;
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
