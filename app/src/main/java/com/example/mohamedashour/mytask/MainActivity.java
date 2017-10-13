package com.example.mohamedashour.mytask;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    Calendar myCalendar;
    Date date;

    RelativeLayout weddingDateContainer;
    LinearLayout timerContainer;
    TextView txtDays, txtHours, txtMins, txtSec, addCoverText;

    long diff;
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

    ListView lv;

    ArrayList<String> tibsImages;
    ArrayList<String> tibsTitles;
    ArrayList<String> tibsIds;

    ArrayList<String> toDoListTitles;
    ArrayList<String> toDoListIds;

    FloatingActionButton fab;

    ArrayList<Plans> myPlans;

    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String fd = dayOfMonth+"."+monthOfYear+"."+year;
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("futureDate", fd).apply();
                Log.e("futureDate", fd);

                if (isRunning){
                    mcountDownTimer.cancel();
                    updateTimer();
                }else {
                    updateTimer();
                }

            }
        };
        requestQueue = Volley.newRequestQueue(this);

        tibsRecyclerView = (RecyclerView) findViewById(R.id.tibs_recyclerView);
        tibsGridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        tibsRecyclerView.setLayoutManager(tibsGridLayoutManager);

        toDoListRecyclerView = (RecyclerView) findViewById(R.id.todolist_recyclerView);
        toDoListGridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        toDoListRecyclerView.setLayoutManager(toDoListGridLayoutManager);

        lv = (ListView) findViewById(R.id.plansCardView);

        tibsIds = new ArrayList<String>();
        tibsImages = new ArrayList<String>();
        tibsTitles = new ArrayList<String>();

        toDoListIds = new ArrayList<String>();
        toDoListTitles = new ArrayList<String>();

        weddingDateContainer = (RelativeLayout) findViewById(R.id.weddingDateContainer);
        timerContainer = (LinearLayout) findViewById(R.id.timerContainer);
        txtDays = (TextView) findViewById(R.id.txtDays);
        txtHours = (TextView) findViewById(R.id.txtHours);
        txtMins = (TextView) findViewById(R.id.txtMins);
        txtSec = (TextView) findViewById(R.id.txtSec);
        addCoverText = (TextView) findViewById(R.id.addCoverText);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        try{
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("plans", " ");
            if (!json.equals(" ")){
                Type type = new TypeToken<ArrayList<Plans>>() {}.getType();
                ArrayList<Plans> myPlans = gson.fromJson(json, type);
                listArrayAdapter list = new listArrayAdapter(MainActivity.this, 0, myPlans);
                lv.setAdapter(list);
            }
            lv.setOnTouchListener(new ListView.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });
        }catch (Exception e){
            Log.e("errrrrrrrrrrrrrrr", e.getMessage());
        }

        if (isNetworkAvailable()){
            getTibs();
            getToDoList();
        }

        timerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        addCoverText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 5);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, plan_screen.class);
                startActivity(intent);
            }
        });

        SharedPreferences myPrefrence = getPreferences(MODE_PRIVATE);
        String imageS = myPrefrence.getString("image_saved", "");
        Bitmap imageB;
        if(!imageS.equals("")) {
            imageB = decodeToBase64(imageS);
            Drawable dr = new BitmapDrawable(imageB);
            weddingDateContainer.setBackgroundDrawable(dr);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        formatter.setLenient(false);
        String oldTime = PreferenceManager.getDefaultSharedPreferences(this).getString("futureTime", " ");
        if (!oldTime.equals("")){
            Date oldDate;
            try {
                oldDate = formatter.parse(oldTime);
                milliseconds = PreferenceManager.getDefaultSharedPreferences(this).getLong("milliseconds", 0);
                startTime=System.currentTimeMillis();

                mcountDownTimer = new CountDownTimer(milliseconds, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        startTime=startTime-1;
                        Long serverUptimeSeconds = (millisUntilFinished-startTime) / 1000;

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

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                Log.e("errrrrrrrrrrr", e.getMessage());
            }
        }else {
            Toast.makeText(MainActivity.this, "empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTimer() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        formatter.setLenient(false);
        String oldTime = formatter.format(myCalendar.getTime())+", "+myCalendar.getTime().getHours()+":"+myCalendar.getTime().getMinutes()+":"+myCalendar.getTime().getSeconds();
        Date oldDate;
        try {
            oldDate = formatter.parse(oldTime);
            milliseconds = oldDate.getTime();
            startTime=System.currentTimeMillis();

            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putLong("milliseconds", milliseconds).apply();

            mcountDownTimer = new CountDownTimer(milliseconds, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    startTime=startTime-1;
                    Long serverUptimeSeconds = (millisUntilFinished - startTime) / 1000;

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
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i=0;i < jsonArray.length();i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                tibsTitles.add(jsonObject.getString("title"));
                                tibsIds.add(jsonObject.getString("id"));
                                tibsImages.add( jsonObject.getString("image"));
                            }
                            tibsAdapter = new tibsAdapter(MainActivity.this, tibsIds, tibsImages, tibsTitles);
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
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i=0;i < jsonArray.length();i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                toDoListTitles.add(jsonObject.getString("title"));
                                toDoListIds.add(jsonObject.getString("id"));
                            }
                            toDoListAdapter = new toDoListAdapter(MainActivity.this, toDoListIds, toDoListTitles);
                            toDoListRecyclerView.setAdapter(toDoListAdapter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            InputStream stream;
            try {

                stream = getContentResolver().openInputStream(data.getData());
                Bitmap realImage = BitmapFactory.decodeStream(stream);
                Drawable dr = new BitmapDrawable(realImage);
                weddingDateContainer.setBackgroundDrawable(dr);

                SharedPreferences myPrefrence = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefrence.edit();
                editor.putString("image_saved", encodeToBase64(realImage));

                editor.commit();
            }
            catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
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


    @Override
    protected void onPause() {
        try {
            String futureDate = PreferenceManager.getDefaultSharedPreferences(this).getString("futureDate", " ");
            if (!futureDate.equals("")){
                String odlTime = futureDate+", "+myCalendar.getTime().getHours()+":"+myCalendar.getTime().getMinutes()+":"+myCalendar.getTime().getSeconds();;
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("futureTime", odlTime).apply();
            }
        }catch (Exception e){
            Log.e("sttttttttttp", e.getMessage());
        }
        super.onPause();
    }

}
