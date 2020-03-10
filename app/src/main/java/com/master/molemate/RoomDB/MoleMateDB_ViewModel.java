package com.master.molemate.RoomDB;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.master.molemate.RoomDB.Entities.EntityMix_User_MoleLib;
import com.master.molemate.RoomDB.Entities.Entity_Mole_Library;
import com.master.molemate.RoomDB.Entities.Entity_Users;

import java.util.List;

//TODO: Need a LoginScreen and a LoginProcess!
public class MoleMateDB_ViewModel extends ViewModel {

    private static final String TAG = "MoleUserViewModel_For_D";

    private MoleMateDB_Repository repository;
    private Entity_Users user;
    private LiveData<List<EntityMix_User_MoleLib>> allMolesFromUser;

    private SharedPreferences sharedPrefMiniDB;

    private int uid;


    /*
        Constructor
     */
    public MoleMateDB_ViewModel(MoleMateDB_Repository repository) {

        //sharedPrefMiniDB = application.getApplicationContext().getSharedPreferences("UserPref", 0); // 0 for private mode

        this.repository = repository;
    }

    public LiveData<List<Entity_Users>> getAllUsers(){
        LiveData<List<Entity_Users>> users = repository.getAllUsers();
        return users;
    }

    public void insertUser(Entity_Users user_Entry){
        repository.insertUser(user_Entry);
    }
}