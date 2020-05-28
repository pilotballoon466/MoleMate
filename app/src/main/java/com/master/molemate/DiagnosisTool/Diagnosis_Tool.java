package com.master.molemate.DiagnosisTool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.master.molemate.ChooseActionScreen;
import com.master.molemate.DiagnosisTool.DiagnosisFragments.Fragment_Check_Image;
import com.master.molemate.DiagnosisTool.DiagnosisFragments.Fragment_Determine_Mole_Position;
import com.master.molemate.DiagnosisTool.DiagnosisFragments.Fragment_Diagnosis;
import com.master.molemate.DiagnosisTool.DiagnosisFragments.Fragment_Save_Mole_Diagnosis;
import com.master.molemate.DiagnosisTool.DiagnosisFragments.Fragment_Take_Picture;
import com.master.molemate.ImageFileStorage.Fragment_Selected_BodyPart_Archive;
import com.master.molemate.R;

public class Diagnosis_Tool extends AppCompatActivity {

    private static final String TAG = "Diagnosis_Tool";

    ViewPager fragmentContainer;
    DiagnosisStatePageAdapter adapter;

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_tool);

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

        //Adding Toolbar and Title to Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        //Adding the Burgermenu to Toolbar
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_opening, R.string.nav_closing);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Creating FragmentAdapter - Handler for Fragments + Creating ViewPager
        fragmentContainer = findViewById(R.id.container_cancer_free);

        setFragmentsInViewPagerAdapter();
        selectFragmentToShow(0);

        //Dismiss Swiping between fragments
        fragmentContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }

        });

    }

    public void selectFragmentToShow(int fragmentID) {
        fragmentContainer.setCurrentItem(fragmentID);
    }

    public void selectFragmentToShowWithTitle(String title){
        fragmentContainer.setCurrentItem(adapter.getItemWithTitle(title));
    }

    private void setFragmentsInViewPagerAdapter(){
        adapter = new DiagnosisStatePageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Take_Picture(), "take_picture");
        adapter.addFragment(new Fragment_Check_Image(), "check_Image");
        adapter.addFragment(new Fragment_Determine_Mole_Position(), "body_position");
        adapter.addFragment(new Fragment_Diagnosis(), "diagnosis");
        adapter.addFragment(new Fragment_Save_Mole_Diagnosis(), "check_and_save");
        fragmentContainer.setAdapter(adapter);
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
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        int currentFragmentID = fragmentContainer.getCurrentItem();
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if (currentFragmentID >0) {
            selectFragmentToShow(0);
        }else
            super.onBackPressed();
    }


    protected void showBackButton(boolean show) {

        if (show) {
            // Remove hamburger
            toggle.setDrawerIndicatorEnabled(false);
            // Show back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if (!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            // Remove back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            // Show hamburger
            toggle.setDrawerIndicatorEnabled(true);
            // Remove the/any drawer toggle listener
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }

    public void setDrawerIndicatorEnable(boolean b) {
        toggle.setDrawerIndicatorEnabled(b);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        return true;
    }

    public void btnTakePic(View view) {

    }
}
