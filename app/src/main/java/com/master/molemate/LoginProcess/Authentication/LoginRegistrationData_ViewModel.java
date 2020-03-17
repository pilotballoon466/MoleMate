package com.master.molemate.LoginProcess.Authentication;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.master.molemate.RoomDB.MoleMateDB_Repository;

public class LoginRegistrationData_ViewModel extends AndroidViewModel {

    private static final String TAG = "LoginRegistrationData_VM";

    private MoleMateDB_Repository repository;

    private SharedPreferences sharedPrefMiniDB;

    private int uid;

    public LoginRegistrationData_ViewModel(@NonNull Application application) {
        super(application);
        this.repository = new MoleMateDB_Repository(application);

    }


}
