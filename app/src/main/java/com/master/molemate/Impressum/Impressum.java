package com.master.molemate.Impressum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.master.molemate.DiagnosisTool.Diagnosis_Tool;
import com.master.molemate.HomeScreen.HomeScreen;
import com.master.molemate.ImageFileStorage.ImageFileArchive;
import com.master.molemate.LoginProcess.LoginActivity;
import com.master.molemate.Prevention.Prevention;
import com.master.molemate.R;

public class Impressum extends AppCompatActivity {

    private static final String TAG = "Impressum";

    DrawerLayout drawer;
    private Toolbar toolbar;
    private Button sendMail;

    private final String mail = "molemate@web.de";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressum);

        settingUpBasics();

        sendMail = findViewById(R.id.impressum_sendMailButton);
        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });
    }


    private void onItemSelected(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()){
            case R.id.menu_item_home:
                intent = new Intent(this, HomeScreen.class);
                startActivity(intent);
                break;
            case R.id.menu_item_cancer_free:
                intent = new Intent(this, Diagnosis_Tool.class);
                startActivity(intent);
                break;
            case R.id.menu_item_health:
                intent = new Intent(this, ImageFileArchive.class);
                startActivity(intent);
                break;
            case R.id.menu_item_prev:
                intent = new Intent(this, Prevention.class);
                startActivity(intent);
                break;
            case R.id.menu_item_impressum:
                intent = new Intent(this, Impressum.class);
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
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.cancerCheckerClass);
        setSupportActionBar(toolbar);

        //Adding the Burgermenu to Toolbar
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_opening, R.string.nav_closing);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_doc, menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.menu_item_add)
                    sendMail();

                return true;
            }
        });

        return true;
    }


    public void sendMail() {
        /* Create the Intent */
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        /* Fill it with Data */
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mail});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback MoleMate");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hier dein Feedback...");

        /* Send it off to the Activity-Chooser */
        this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

    }
}
