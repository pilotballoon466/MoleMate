package com.master.molemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import com.google.android.material.navigation.NavigationView;

import com.master.molemate.DiagnosisTool.Diagnosis_Tool;
import com.master.molemate.ImageFileStorage.ImageFileArchive;
import com.master.molemate.LoginProcess.LoginActivity;

public class ChooseActionScreen extends AppCompatActivity {

    private static final String TAG = "ChooseActionScreen";

    ImageButton fileStorageButton;
    ImageButton makePicButton;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action_screen);

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

        settingUpImageButtons();
    }

    private void settingUpImageButtons() {
        fileStorageButton = (ImageButton) findViewById(R.id.fileStructureButton);
        makePicButton = (ImageButton) findViewById(R.id.takeMolePicButton);

        fileStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ImageFileArchive.class);
                int uid = getIntent().getIntExtra("currentUser",1);
                intent.putExtra("currentUser", uid );
                startActivity(intent);
            }
        });

        makePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Diagnosis_Tool.class);
                int uid = getIntent().getIntExtra("currentUser",1);
                intent.putExtra("currentUser", uid );
                startActivity(intent);
            }
        });
    }

    private void onItemSelected(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()){
            case R.id.menu_item_home:
                intent = new Intent(this, ChooseActionScreen.class);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: We're back!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: We're dead");

    }
}
