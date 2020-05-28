package com.master.molemate.HomeScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.master.molemate.ChooseActionScreen;
import com.master.molemate.LoginProcess.LoginActivity;
import com.master.molemate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class HomeScreen extends AppCompatActivity {

    private static final String TAG = "HomeScreen";


    static OkHttpClient client = new OkHttpClient();
    DrawerLayout drawer;

    Dialog uvInfoPopUp;

    static TextView uvIndexView;
    static TextView tempView;
    static ImageView weatherIconView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        settingUpBasics();

        uvInfoPopUp = new Dialog(this);
        uvIndexView = findViewById(R.id.uv_index);
        tempView = findViewById(R.id.temp_text);
        weatherIconView = findViewById(R.id.weatherIcon);

        uvInfoPopUp.setContentView(R.layout.popup_uv_index_info);


        UVCall uvCall = new UVCall();
        uvCall.start();

        WeatherCall weatherCall = new WeatherCall();
        weatherCall.start();

    }

    public void getIndexInfo(View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        TextView header = uvInfoPopUp.findViewById(R.id.uv_index_header);
        TextView infoText = uvInfoPopUp.findViewById(R.id.uv_index_low_risk_text);
        Button moreInfoBut = uvInfoPopUp.findViewById(R.id.moreInfoBut);
        int uvIndexInt = Integer.parseInt((String) uvIndexView.getText());

        header.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (width * 0.1));
        infoText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (width * 0.04));
        moreInfoBut.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (width * 0.04));

        if (uvIndexInt < 3) {
            infoText.setText(R.string.uv_index_low_risk);
        } else if (uvIndexInt <= 7 && uvIndexInt >= 3) {
            infoText.setText(R.string.uv_index_mid_risk);
        } else if (uvIndexInt >= 8) {
            infoText.setText(R.string.uv_index_high_risk);
        }

        uvInfoPopUp.show();

    }


    private void onItemSelected(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.menu_item_home:
                intent = new Intent(this, HomeScreen.class);
                startActivity(intent);
                break;
            case R.id.menu_item_impressum:
                intent = new Intent(this, ChooseActionScreen.class);
                startActivity(intent);
                break;
            case R.id.menu_item_health:
                intent = new Intent(this, ChooseActionScreen.class);
                startActivity(intent);
                break;
            case R.id.menu_item_cancer_free:
                intent = new Intent(this, ChooseActionScreen.class);
                startActivity(intent);
                break;
            case R.id.menu_item_logout:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void settingUpBasics() {
        //Creating Menu
        NavigationView mainMenu = findViewById(R.id.mainMenu);
        mainMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                onItemSelected(menuItem);
                return false;
            }
        });

        mainMenu.setItemIconTintList(null);

        //Adding Toolbar and Title to Toolabar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.cancerCheckerClass);
        setSupportActionBar(toolbar);

        //Adding the Burgermenu to Toolbar
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_opening, R.string.nav_closing);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }


    public class UVCall extends Thread {

        private String url;

        public UVCall() {
        }

        public void run() {

            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());

            String url = "https://api.openuv.io/api/v1/uv?lat=52.27&lng=8.0498&dt=";

            url = url + nowAsISO;

            Log.i(TAG, "run: URL" + url);

            UVCrawlerAsyncTask uvCrawlerAsyncTask = new UVCrawlerAsyncTask(url);
            uvCrawlerAsyncTask.execute();


        }
    }

    public class WeatherCall extends Thread {

        private String url;

        public WeatherCall() {
        }

        public void run() {

            //api.openweathermap.org/data/2.5/weather?lat=52.27&lon=8.0498&appid={your api key}

            String url = "https://api.openweathermap.org/data/2.5/weather?lat=52.27&lon=8.0498&appid=8ce74a827023ed945d0e3ed8ed7db049";


            Log.i(TAG, "run: URL" + url);

            WeatherCrawlerAsyncTask weatherCrawlerAsyncTask = new WeatherCrawlerAsyncTask(url);
            weatherCrawlerAsyncTask.execute();


        }
    }


    private static class UVCrawlerAsyncTask extends AsyncTask<String, String, String> {

        private String url;
        private String result;

        private UVCrawlerAsyncTask(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(String... strings) {
            Request request = new Request.Builder()
                    .header("x-access-token", "bee4d16f4445fabd564ea59ffb53ca0b")
                    .url(url)
                    .build();

            try (okhttp3.Response response = client.newCall(request).execute()) {
                result = response.body().string();
                Log.i(TAG, result);
                //result = response.body().string();
                return result;

            } catch (IOException ex) {
                Log.d(TAG, "doInBackground: " + ex.getMessage());
            }

            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if (response != "") {
                if (!response.contains("error")) {

                    JSONObject jobj;
                    int uvIndex = 0;
                    try {
                        jobj = new JSONObject(response);
                        Double uvIndexDouble = jobj.getJSONObject("result").getDouble("uv");
                        uvIndex = (int) Math.round(uvIndexDouble);

                    } catch (JSONException err) {
                        Log.d("Error", err.toString());
                    }


                    if (uvIndex <= 3) {
                        uvIndexView.setTextColor(Color.BLUE);
                    } else if (uvIndex <= 6 && uvIndex > 3) {
                        uvIndexView.setTextColor(Color.GREEN);
                    } else if (uvIndex <= 8 && uvIndex > 6) {
                        uvIndexView.setTextColor(Color.YELLOW);
                    } else if (uvIndex <= 11 && uvIndex > 8) {
                        uvIndexView.setTextColor(Color.RED);
                    } else if (uvIndex > 11) {
                        uvIndexView.setTextColor(Color.BLACK);
                    }

                    uvIndexView.setText(Integer.toString(uvIndex));
                }
            }
        }
    }

    private static class WeatherCrawlerAsyncTask extends AsyncTask<String, String, String> {

        private String url;
        private String result;

        private WeatherCrawlerAsyncTask(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(String... strings) {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (okhttp3.Response response = client.newCall(request).execute()) {
                result = response.body().string();
                Log.i(TAG, result);
                //result = response.body().string();
                return result;

            } catch (IOException ex) {
                Log.d(TAG, "doInBackground: " + ex.getMessage());
            }

            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);


            if (response != "") {
                if (!response.contains("error")) {

                    JSONObject jobj;
                    int actTempC = -100;
                    String descrp = "";
                    int sunrise=0;
                    int sunset=0;
                    int actTime=0;

                    try {
                        jobj = new JSONObject(response);
                        descrp = ((JSONObject)jobj.getJSONArray("weather").get(0)).getString("description");

                        Double actTempDoubK = jobj.getJSONObject("main").getDouble("temp");
                        actTempC = (int) Math.round((actTempDoubK-273.15)); //Convert Kalvin to Celsius °Kalvin-273.15

                        sunrise = jobj.getJSONObject("sys").getInt("sunrise");
                        sunset = jobj.getJSONObject("sys").getInt("sunset");
                        actTime = jobj.getInt("dt");

                    } catch (JSONException err) {
                        Log.d("Error", err.toString());
                    }

                    if(actTempC != -100){
                        tempView.setText(actTempC+"°C");
                    }

                    setWeatherIcon(sunrise, sunset, actTime, descrp);

                }
            }
        }
    }

    /**
     * clear sky
     * few clouds
     * scattered clouds
     * broken clouds
     * shower rain
     * rain
     * thunderstorm
     * snow
     * mist
     */

    private static void setWeatherIcon(int sunrise, int sunset, int actTime, String descrp){

        int weatherIconID = 0;

        if(sunrise != 0 && sunset != 0 && actTime != 0) {
            if (sunrise < actTime && sunset > actTime) {
                if (descrp.equals("clear sky")) {
                    weatherIconID = R.drawable.sunny;
                } else if (descrp.equals("few clouds")) {
                    weatherIconID = R.drawable.lightly_cloudy;
                } else if(descrp.equals("scattered clouds")){
                    weatherIconID = R.drawable.partly_cloudy_moon;
                } else if (descrp.equals("broken clouds")) {
                    weatherIconID = R.drawable.cloundy;
                } else if (descrp.equals("rain")) {
                    weatherIconID = R.drawable.rainy;
                } else if (descrp.equals("thunderstorm")) {
                    weatherIconID = R.drawable.thunderstorm;
                } else if (descrp.equals("snow")) {
                    weatherIconID = R.drawable.snowy;
                } else if (descrp.equals("mist")) {
                    weatherIconID = R.drawable.foggy;
                }
            } else if (sunset < actTime) {
                if (descrp.equals("clear sky")) {
                    weatherIconID = R.drawable.moon;
                } else if (descrp.equals("few clouds")) {
                    weatherIconID = R.drawable.lightly_cloudy_moon;
                } else if(descrp.equals("scattered clouds")){
                    weatherIconID = R.drawable.partly_cloudy_moon;
                } else if (descrp.equals("broken clouds")) {
                    weatherIconID = R.drawable.cloundy;
                } else if (descrp.equals("rain")) {
                    weatherIconID = R.drawable.rainy;
                } else if (descrp.equals("thunderstorm")) {
                    weatherIconID = R.drawable.thunderstorm;
                } else if (descrp.equals("snow")) {
                    weatherIconID = R.drawable.snowy;
                } else if (descrp.equals("mist")) {
                    weatherIconID = R.drawable.foggy;
                }
            }

            if(weatherIconID != 0) {
                weatherIconView.setImageResource(weatherIconID);
            }

        }

    }
}
