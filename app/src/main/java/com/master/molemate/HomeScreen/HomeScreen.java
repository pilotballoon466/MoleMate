package com.master.molemate.HomeScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.master.molemate.ChooseActionScreen;
import com.master.molemate.LoginProcess.LoginActivity;
import com.master.molemate.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class HomeScreen extends AppCompatActivity {

    private static final String TAG = "HomeScreen";

    OkHttpClient client = new OkHttpClient();
    DrawerLayout drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

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

        //Adding Toolbar and Title to Toolabr
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.cancerCheckerClass);
        setSupportActionBar(toolbar);

        //Adding the Burgermenu to Toolbar
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_opening, R.string.nav_closing);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        String url = "https://api.openuv.io/api/v1/uv?lat=-33.34&lng=115.342&dt=2018-01-24T10%3A50%3A52.283Z";



        UVCall uvCall = new UVCall(url);
        uvCall.start();

    }

    private void onItemSelected(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()){
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
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }


    public class UVCall extends Thread {

        private String url;

        public UVCall(String url) {
            this.url = url;
        }

        public void run() {
            Request request = new Request.Builder()
                    .header("x-access-token", "bee4d16f4445fabd564ea59ffb53ca0b")
                    .url(url)
                    .build();

            try (okhttp3.Response response = client.newCall(request).execute()) {
                Log.i(TAG, response.body().string());
            }catch (IOException ex){
                Log.d(TAG, "run: " + ex.getMessage());
            }
        }
    }






}
