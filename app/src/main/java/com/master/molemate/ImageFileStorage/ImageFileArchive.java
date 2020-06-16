package com.master.molemate.ImageFileStorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.master.molemate.Adapter.MoleMateFragmentStatePagerAdapter;
import com.master.molemate.Adapter.MoleMateRecyclerViewAdpter;
import com.master.molemate.ChooseActionScreen;
import com.master.molemate.DataSecurity.TextConverter;
import com.master.molemate.DiagnosisTool.Diagnosis_Tool;
import com.master.molemate.HomeScreen.HomeScreen;
import com.master.molemate.ImageFileStorage.SupporterClasses.RecyclerViewMoleImageItem;
import com.master.molemate.ImageFileStorage.SupporterClasses.ViewModel_ImageArchive_Communication;
import com.master.molemate.Impressum.Impressum;
import com.master.molemate.LoginProcess.LoginActivity;
import com.master.molemate.Prevention.Prevention;
import com.master.molemate.R;
import com.master.molemate.RoomDB.Entities.EntityMix_User_MoleLib;
import com.master.molemate.RoomDB.Entities.Entity_Mole_Library;
import com.master.molemate.RoomDB.MoleMateDB_ViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageFileArchive extends AppCompatActivity implements MoleMateRecyclerViewAdpter.OnMoleListener {

    private static final String TAG = "ImageFileArchive";

    public static final String SELECTED_BODYPART = "selected_bodyPart";
    public static final String SELECT_A_BODYPART = "select_a_bodyPart";

    private SharedPreferences sharedPref;
    private TextConverter encrypter;


    NavigationView mainMenu;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    MoleMateFragmentStatePagerAdapter adapter;
    ViewPager fragmentContainerArchive;
    DrawerLayout drawer;

    //Variables for RecyclerView
    private MoleMateRecyclerViewAdpter moleRecyclerViewAdapter;


    public int currentUser;

    private ViewModel_ImageArchive_Communication fragmentViewModelCom;
    private MoleMateDB_ViewModel dbCom;
    private List<EntityMix_User_MoleLib> moleLibCurrentUser;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    private ArrayList<RecyclerViewMoleImageItem> moleItemList;
    private RecyclerView moleRecyclerView;
    private RecyclerView.LayoutManager moleRecyclerViewLayoutManager;
    private List<EntityMix_User_MoleLib> moleLib;

    private File dir;


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

        SharedPreferences sharedPref = this.getSharedPreferences(this.getString(R.string.preference_file), Context.MODE_PRIVATE);

        encrypter = new TextConverter(Objects.requireNonNull(sharedPref.getString("key", "")));
        //Adding Toolbar and Title to Toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.yourArchive);
        setSupportActionBar(toolbar);


        //Adding the Burgermenu to Toolbar
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_opening, R.string.nav_closing);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Creating FragmentAdapter - Handler for Fragments + Creating ViewPager
        //fragmentContainerArchive = findViewById(R.id.fragment_container_image_archive);

        //Create Com Tool for ArchiveFragments
        fragmentViewModelCom = new ViewModelProvider(this).get(ViewModel_ImageArchive_Communication.class);

        //Create Handler for Saved images
        moleItemList = new ArrayList<>();
        moleRecyclerView = findViewById(R.id.recyclerViewMoleImages);
        moleRecyclerViewAdapter = new MoleMateRecyclerViewAdpter(moleItemList, this);
        moleRecyclerViewLayoutManager = new LinearLayoutManager(this);

        moleRecyclerView.setLayoutManager(moleRecyclerViewLayoutManager);
        moleRecyclerView.setAdapter(moleRecyclerViewAdapter);


        currentUser = getIntent().getIntExtra("currentUser", 1);


        //addFragmentsToViewPager();

        //selectFragmentToShow(0);


        dbCom = new ViewModelProvider(this).get(MoleMateDB_ViewModel.class);
        dbCom.getAllMolesFromUser(currentUser).observe(this, new Observer<List<EntityMix_User_MoleLib>>() {
            @Override
            public void onChanged(List<EntityMix_User_MoleLib> entityMix_user_moleLibs) {
                Log.d(TAG, "onChanged: got Users Moles! " + entityMix_user_moleLibs.size());
                fragmentViewModelCom.setUserMoleLib(entityMix_user_moleLibs);
                moleLib = entityMix_user_moleLibs;

                for(EntityMix_User_MoleLib moleEntry : moleLib){

                    Log.d(TAG, "extractingImagesAccordingBodyPart: foundMole " + moleEntry.mole_library.getMoleImageUri());

                    decryptData(moleEntry);

                    moleItemList.add(new RecyclerViewMoleImageItem(
                            Uri.parse(moleEntry.mole_library.getMoleImageUri()),
                            moleEntry.mole_library.getDateMoleImageCreation(),
                            moleEntry.mole_library.getDiagnosis(),
                            moleEntry.mole_library.getMolePosText(),
                            moleEntry.mole_library.getMoleID(),
                            moleEntry.mole_library.getDiagnosedPropability(),
                            moleEntry.mole_library.isHandled()
                    ));
                }

                moleRecyclerViewAdapter.setFullList();
                moleRecyclerViewAdapter.notifyDataSetChanged();


                Log.d(TAG, "onChanged: moleLib " + moleLib.size());
            }
        });

        moleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "onClick: View ID " + view.getId());
                Log.e(TAG, "onClick: View insights " + view.toString() );
            }
        });
    }

    private void decryptData(EntityMix_User_MoleLib moleEntry) {

            try {
                moleEntry.mole_library.setMolePosText(encrypter.decrypt(moleEntry.mole_library.getMolePosText()));
                moleEntry.mole_library.setMoleImageUri(encrypter.decrypt(moleEntry.mole_library.getMoleImageUri()));
                moleEntry.mole_library.setMolePosBitmapUri(encrypter.decrypt(moleEntry.mole_library.getMolePosBitmapUri()));
                moleEntry.mole_library.setDiagnosis(encrypter.decrypt(moleEntry.mole_library.getDiagnosis()));
                moleEntry.mole_library.setMolePosText(encrypter.decrypt(moleEntry.mole_library.getMolePosText()));

                Log.d(TAG, "encryptMoleEntry: enc " + moleEntry.mole_library.getMolePosText() + " dec: " + encrypter.decrypt(moleEntry.mole_library.getMolePosText()));
                Log.d(TAG, "encryptMoleEntry: enc " + moleEntry.mole_library.getMoleImageUri() + " dec: " + encrypter.decrypt(moleEntry.mole_library.getMoleImageUri()));
                Log.d(TAG, "encryptMoleEntry: enc " + moleEntry.mole_library.getMolePosBitmapUri() + " dec: " + encrypter.decrypt(moleEntry.mole_library.getMolePosBitmapUri()));
                Log.d(TAG, "encryptMoleEntry: enc " + moleEntry.mole_library.getDiagnosis() + " dec: " + encrypter.decrypt(moleEntry.mole_library.getDiagnosis()));
                Log.d(TAG, "encryptMoleEntry: enc " + moleEntry.mole_library.getDateMoleImageCreation() + " dec: " + encrypter.decrypt(moleEntry.mole_library.getDateMoleImageCreation()));

            } catch (Exception e) {
                e.printStackTrace();
            }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchMenu);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                moleRecyclerViewAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
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
        super.onBackPressed();
    }


    @Override
    public void onMoleClick(int position) {
        RecyclerViewMoleImageItem moleEntry = moleItemList.get(position);

        Log.d(TAG, "onMoleClick: moleEntry: " + moleEntry.getCardViewDate());
        Log.d(TAG, "onMoleClick: moleEntry: MoleID " + moleEntry.getMoleId());

        Intent intent = new Intent(this, ImageArchiveShowMole.class);
        intent.putExtra("mole_id",  moleEntry.getMoleId());
        startActivity(intent);
    }
}
