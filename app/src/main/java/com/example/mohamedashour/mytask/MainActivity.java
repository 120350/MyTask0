package com.example.mohamedashour.mytask;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    Calendar myCalendar;


    LinearLayout timerContainer;
    TextView txtDays, txtHours, txtMins, txtSec, addCoverText;
    ImageView coverPhoto;

    long milliseconds;
    long startTime;
    CountDownTimer mcountDownTimer;

    RequestQueue requestQueue;

    RecyclerView tibsRecyclerView;
    GridLayoutManager tibsGridLayoutManager;
    tibsAdapter tibsAdapter;

    RecyclerView toDoListRecyclerView;
    GridLayoutManager toDoListGridLayoutManager;
    toDoListAdapter toDoListAdapter;

    RecyclerView plansRecyclerView;
    GridLayoutManager plansGridLayoutManager;
    plansAdapter plansAdapter;

    FloatingActionButton fab;

    boolean isRunning = false;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBBar);
        toolbar.setCollapsible(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        myCalendar = Calendar.getInstance();


        // show DatePickerDialog to select your date
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (isRunning){
                    mcountDownTimer.cancel();
                    updateTimer();
                }else {
                    updateTimer();
                }
            }
        };

        //define widgets
        requestQueue = Volley.newRequestQueue(this);
        tibsRecyclerView = (RecyclerView) findViewById(R.id.tibs_recyclerView);
        tibsGridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        tibsRecyclerView.setLayoutManager(tibsGridLayoutManager);
        tibsRecyclerView.setNestedScrollingEnabled(false);
        toDoListRecyclerView = (RecyclerView) findViewById(R.id.todolist_recyclerView);
        toDoListGridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        toDoListRecyclerView.setLayoutManager(toDoListGridLayoutManager);
        toDoListRecyclerView.setNestedScrollingEnabled(false);
        plansRecyclerView = (RecyclerView) findViewById(R.id.plansCardView);
        plansGridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        plansRecyclerView.setLayoutManager(plansGridLayoutManager);
        plansRecyclerView.setNestedScrollingEnabled(false);
        timerContainer = (LinearLayout) findViewById(R.id.timerContainer);
        txtDays = (TextView) findViewById(R.id.txtDays);
        txtHours = (TextView) findViewById(R.id.txtHours);
        txtMins = (TextView) findViewById(R.id.txtMins);
        txtSec = (TextView) findViewById(R.id.txtSec);
        addCoverText = (TextView) findViewById(R.id.addCoverText);
        coverPhoto = (ImageView) findViewById(R.id.coverPhoto);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // get plans and display them
        getPlans();

        // get tibs and todolists if there is internet
        if (isNetworkAvailable()){
            getTibs();
            getToDoList();
        }

        // click to get date
        timerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //click to select cover photo
        addCoverText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 5);
            }
        });
        // click to add plan
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, plan_screen.class);
                startActivity(intent);
            }
        });

        // load old image
        loadOldImage();

        // load old date
        loadOldDate();
    }

    private void updateTimer() {

        // date format
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        formatter.setLenient(false);
        String oldTime = formatter.format(myCalendar.getTime())+", "+myCalendar.getTime().getHours()+":"+myCalendar.getTime().getMinutes()+":"+myCalendar.getTime().getSeconds();
        Date oldDate;
        try {
            oldDate = formatter.parse(oldTime);
            milliseconds = oldDate.getTime();
            startTime=System.currentTimeMillis();

            // save your date for next launch
            if (milliseconds > startTime){
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putLong("milliseconds", milliseconds).apply();

                mcountDownTimer = new CountDownTimer(milliseconds, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        startTime=startTime-1;
                        Long serverUptimeSeconds = (millisUntilFinished - startTime) / 1000;

                        // update texts
                        String daysLeft = String.format("%d", serverUptimeSeconds / 86400);
                        txtDays.setText(daysLeft);

                        String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                        txtHours.setText(hoursLeft);

                        String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);
                        txtMins.setText(minutesLeft);

                        String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                        txtSec.setText(secondsLeft);
                        isRunning= true;
                    }

                    @Override
                    public void onFinish() {
                        Toast.makeText(MainActivity.this, "Your time is now :)", Toast.LENGTH_SHORT).show();
                        txtDays.setText("00");
                        txtHours.setText("00");
                        txtMins.setText("00");
                        txtSec.setText("00");
                        isRunning= false;
                    }
                }.start();
            }else Toast.makeText(MainActivity.this, "You can't choose old date", Toast.LENGTH_LONG).show();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Log.e("errrrrrrrrrrr", e.getMessage());
        }

    }

    public void getTibs(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, "http://www.thejerb.com/jerb/public/api/tips",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // make model to save data and save it to arraylist
                            ArrayList<tibsModel> models = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i=0;i < jsonArray.length();i++){

                                tibsModel model = new tibsModel();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                model.setTibsTitles(jsonObject.getString("title"));
                                model.setTibsID(jsonObject.getString("id"));
                                model.setTibsImages(jsonObject.getString("image"));
                                models.add(model);
                            }
                            //start adapter to load parsing data
                            tibsAdapter = new tibsAdapter(MainActivity.this, models);
                            tibsRecyclerView.setAdapter(tibsAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public void getToDoList(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://www.thejerb.com/jerb/public/api/guest_todos",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // make model to save data and save it to arraylist
                        ArrayList<toDoListModel> models = new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i=0;i < jsonArray.length();i++){

                                toDoListModel model = new toDoListModel();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                model.setID(jsonObject.getString("id"));
                                model.setTitle(jsonObject.getString("title"));
                                models.add(model);
                            }
                            //start adapter to load parsing data
                            toDoListAdapter = new toDoListAdapter(MainActivity.this, models);
                            toDoListRecyclerView.setAdapter(toDoListAdapter);
                        } catch (JSONException e) {
                            Log.e("tooooooo", e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("messssssss", error.getMessage());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public void getPlans(){
        try{
            //get old plans
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("plans", " ");
            if (!json.equals(" ")){
                Type type = new TypeToken<ArrayList<Plans>>() {}.getType();
                ArrayList<Plans> myPlans = gson.fromJson(json, type);
                if (myPlans.size() > 0){
                    plansRecyclerView.setVisibility(View.VISIBLE);
                    //start adapter to load parsing data
                    plansAdapter = new plansAdapter(MainActivity.this, myPlans);
                    plansRecyclerView.setAdapter(plansAdapter);
                }
            }
        }catch (Exception e){
            Log.e("errrrrrrrrrrrrrrr", e.getMessage());
        }
    }

    public void loadOldImage() {
        // get last selected image url
        String imageUrl = PreferenceManager.getDefaultSharedPreferences(this).getString("imageURL", " ");
        if (!imageUrl.equals(" ")){
            //display image using glade
            Glide.with(MainActivity.this)
                    .load(imageUrl)
                    .crossFade()
                    .into(coverPhoto);
        }
    }
    public void loadOldDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        formatter.setLenient(false);
        // get last selected date
        milliseconds = PreferenceManager.getDefaultSharedPreferences(this).getLong("milliseconds", 0);
        if (milliseconds > 0){
            try {

                startTime=System.currentTimeMillis();

                mcountDownTimer = new CountDownTimer(milliseconds, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        startTime=startTime-1;
                        Long serverUptimeSeconds = (millisUntilFinished-startTime) / 1000;

                        //update texts
                        String daysLeft = String.format("%d", serverUptimeSeconds / 86400);
                        txtDays.setText(daysLeft);

                        String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                        txtHours.setText(hoursLeft);

                        String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);
                        txtMins.setText(minutesLeft);

                        String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                        txtSec.setText(secondsLeft);
                        isRunning = true;
                    }
                    @Override
                    public void onFinish() {
                        Toast.makeText(MainActivity.this, "Your time is now :)", Toast.LENGTH_SHORT).show();
                        txtDays.setText("00");
                        txtHours.setText("00");
                        txtMins.setText("00");
                        txtSec.setText("00");
                        isRunning= false;
                    }
                }.start();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.e("errrrrrrrrrrr", e.getMessage());
            }
        }else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            try {
                // get elected image url then save it for next launch then display it
                Uri selectedImageUri = data.getData();
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("imageURL", selectedImageUri.toString()).apply();
                Glide.with(MainActivity.this)
                            .load(selectedImageUri)
                            .crossFade()
                            .into(coverPhoto);
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
