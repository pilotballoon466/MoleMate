package com.master.molemate.ImageFileStorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.master.molemate.Adapter.MoleMateFragmentStatePagerAdapter;
import com.master.molemate.ChooseActionScreen;
import com.master.molemate.HomeScreen.HomeScreen;
import com.master.molemate.ImageFileStorage.SupporterClasses.ViewModel_ImageArchive_Communication;
import com.master.molemate.LoginProcess.LoginActivity;
import com.master.molemate.R;
import com.master.molemate.RoomDB.Entities.EntityMix_User_MoleLib;
import com.master.molemate.RoomDB.MoleMateDB_ViewModel;

import java.util.List;

public class ImageFileArchive extends AppCompatActivity {

    private static final String TAG = "ImageFileArchive";

    public static final String SELECTED_BODYPART = "selected_bodyPart";
    public static final String SELECT_A_BODYPART = "select_a_bodyPart";


    NavigationView mainMenu;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    MoleMateFragmentStatePagerAdapter adapter;
    ViewPager fragmentContainerArchive;
    DrawerLayout drawer;

    public int currentUser;

    private ViewModel_ImageArchive_Communication fragmentViewModelCom;
    private MoleMateDB_ViewModel dbCom;
    private List<EntityMix_User_MoleLib> moleLibCurrentUser;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_file_storage);

        //Creating Menu
        mainMenu = findViewById(R.id.mainMenu);
        mainMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                onItemSelected(menuItem);
                return false;
            }
        });

        mainMenu.setItemIconTintList(null);

        //Adding Toolbar and Title to Toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);


        //Adding the Burgermenu to Toolbar
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_opening, R.string.nav_closing);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Creating FragmentAdapter - Handler for Fragments + Creating ViewPager
        fragmentContainerArchive = findViewById(R.id.fragment_container_image_archive);

        //Create Com Tool for ArchiveFragments
        fragmentViewModelCom = new ViewModelProvider(this).get(ViewModel_ImageArchive_Communication.class);

        currentUser = getIntent().getIntExtra("currentUser", 0);

        addFragmentsToViewPager();

        selectFragmentToShow(0);

        dbCom = new ViewModelProvider(this).get(MoleMateDB_ViewModel.class);
        dbCom.getAllMolesFromUser(currentUser).observe(this, new Observer<List<EntityMix_User_MoleLib>>() {
            @Override
            public void onChanged(List<EntityMix_User_MoleLib> entityMix_user_moleLibs) {
                fragmentViewModelCom.setUserMoleLib(entityMix_user_moleLibs);
            }
        });


    }

    public void selectFragmentToShow(int fragmentID) {
        fragmentContainerArchive.setCurrentItem(fragmentID);
    }

    public void selectFragmentToShowWithTitle(String title){
        fragmentContainerArchive.setCurrentItem(adapter.getItemWithTitle(title));
    }


    private void addFragmentsToViewPager() {
        adapter = new MoleMateFragmentStatePagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new Fragment_SelectBodyPart(), SELECT_A_BODYPART);
        adapter.addFragment(new Fragment_Selected_BodyPart_Archive(), SELECTED_BODYPART);

        fragmentContainerArchive.setAdapter(adapter);
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
        int currentFragmentID = fragmentContainerArchive.getCurrentItem();
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

}
