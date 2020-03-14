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
public class MoleMateDB_ViewModel extends AndroidViewModel {

    private static final String TAG = "MoleUserViewModel_For_D";

    private MoleMateDB_Repository repository;

    private SharedPreferences sharedPrefMiniDB;

    private int uid;


    /*
        Constructor
     */
    public MoleMateDB_ViewModel(Application application) {
        super(application);

        //sharedPrefMiniDB = application.getApplicationContext().getSharedPreferences("UserPref", 0); // 0 for private mode

        this.repository = new MoleMateDB_Repository(application);
    }

    public LiveData<List<Entity_Users>> getAllUsers(){
        return repository.getAllUsers();
    }

    public void insertUser(Entity_Users user_Entry){
        repository.insertUser(user_Entry);
    }

    public void insertMole(Entity_Mole_Library moleLib_Entry){
        repository.insertMole(moleLib_Entry);
    }

    public LiveData<List<EntityMix_User_MoleLib>> getAllMolesFromUser(int uid){
        return repository.getAllMolesFromUser(uid);
    }

    public void deleteMole(Entity_Mole_Library moleEntry){
        repository.deleteMole(moleEntry);
    }

    public void deleteUser(Entity_Users user){
        repository.deleteUser(user);
    }
}