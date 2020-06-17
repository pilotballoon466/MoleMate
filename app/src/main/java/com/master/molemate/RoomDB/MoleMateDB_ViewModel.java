package com.master.molemate.RoomDB;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.master.molemate.DataSecurity.TextConverter;
import com.master.molemate.R;
import com.master.molemate.RoomDB.Entities.EntityMix_User_MoleLib;
import com.master.molemate.RoomDB.Entities.Entity_Mole_Library;
import com.master.molemate.RoomDB.Entities.Entity_Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO: Need a LoginScreen and a LoginProcess!
public class MoleMateDB_ViewModel extends AndroidViewModel {

    private static final String TAG = "MoleUserViewModel_For_D";

    private MoleMateDB_Repository repository;

    private String key;
    private static TextConverter encrypter;

    private int uid;


    /*
        Constructor
     */
    public MoleMateDB_ViewModel(Application application) {
        super(application);

        //sharedPrefMiniDB = application.getApplicationContext().getSharedPreferences("UserPref", 0); // 0 for private mode

        SharedPreferences sharedPref = application.getSharedPreferences(application.getString(R.string.preference_file),Context.MODE_PRIVATE);

        encrypter = new TextConverter(Objects.requireNonNull(sharedPref.getString("key", "")));

        this.repository = new MoleMateDB_Repository(application);

    }

    public LiveData<List<Entity_Users>> getAllUsers(){
        return repository.getAllUsers();
    }

    public LiveData<Entity_Users> getUserByMail(String mail){
        mail = encryptMail(mail);
        return repository.getUserByMail(mail);
    }

    private String encryptMail(String mail) {
        try {
            mail = encrypter.encrypt(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mail;
    }

    public LiveData<List<Entity_Mole_Library>> getAllMolesFromMoleIds(int[] moleIDs){
        return repository.getAllMolesFromMoleIds(moleIDs);
    }

    public void insertUser(Entity_Users user_Entry){
        encryptUser(user_Entry);
        repository.insertUser(user_Entry);
    }

    public void insertMole(Entity_Mole_Library moleLib_Entry){
        encryptMoleEntry(moleLib_Entry);
        repository.insertMole(moleLib_Entry);
    }

    public void updateMole(Entity_Mole_Library moleLib_Entry){
        encryptMoleEntry(moleLib_Entry);
        repository.updateMole(moleLib_Entry);
    }


    public LiveData<List<EntityMix_User_MoleLib>> getAllMolesFromUser(int uid){
        Log.d(TAG, "getAllMolesFromUser: Triggered to get Moles from " + uid);
        return repository.getAllMolesFromUser(uid);
    }

    public void deleteMole(Entity_Mole_Library moleLib_Entry){
        encryptMoleEntry(moleLib_Entry);
        repository.deleteMole(moleLib_Entry);
    }

    public void deleteUser(Entity_Users user_Entry){
        encryptUser(user_Entry);
        repository.deleteUser(user_Entry);
    }

    private void encryptUser(Entity_Users user_entry) {

        try {
            user_entry.setFirstName(encrypter.encrypt(user_entry.getFirstName()));
            user_entry.setLastName(encrypter.encrypt(user_entry.getLastName()));
            user_entry.setMail(encrypter.encrypt(user_entry.getMail()));
            user_entry.setUserName(encrypter.encrypt(user_entry.getUserName()));


            Log.d(TAG, "encryptMoleEntry: enc " + user_entry.getUserName() + " dec: " + encrypter.encrypt(user_entry.getUserName()));
            Log.d(TAG, "encryptMoleEntry: enc " + user_entry.getFirstName() + " dec: " + encrypter.encrypt(user_entry.getFirstName()));
            Log.d(TAG, "encryptMoleEntry: enc " + user_entry.getLastName() + " dec: " + encrypter.encrypt(user_entry.getLastName()));
            Log.d(TAG, "encryptMoleEntry: enc " + user_entry.getMail() + " dec: " + encrypter.encrypt(user_entry.getMail()));

            Log.d(TAG, "encryptMoleEntry: enc " + user_entry.getUserName() + " enc again: " + encrypter.decrypt(encrypter.encrypt(user_entry.getUserName())));
            Log.d(TAG, "encryptMoleEntry: enc " + user_entry.getFirstName() + " enc again: " + encrypter.decrypt(encrypter.encrypt(user_entry.getFirstName())));
            Log.d(TAG, "encryptMoleEntry: enc " + user_entry.getLastName() + " enc again: " + encrypter.decrypt(encrypter.encrypt(user_entry.getLastName())));
            Log.d(TAG, "encryptMoleEntry: enc " + user_entry.getMail() + " enc again: " + encrypter.decrypt(encrypter.encrypt(user_entry.getMail())));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void encryptMoleEntry(Entity_Mole_Library moleLib_entry) {


        try {
            moleLib_entry.setMolePosText(encrypter.encrypt(moleLib_entry.getMolePosText()));
            moleLib_entry.setMoleImageUri(encrypter.encrypt(moleLib_entry.getMoleImageUri()));
            moleLib_entry.setMolePosBitmapUri(encrypter.encrypt(moleLib_entry.getMolePosBitmapUri()));
            moleLib_entry.setDiagnosis(encrypter.encrypt(moleLib_entry.getDiagnosis()));
            moleLib_entry.setMolePosText(encrypter.encrypt(moleLib_entry.getMolePosText()));

            Log.d(TAG, "encryptMoleEntry: enc " + moleLib_entry.getMolePosText() + " dec: " + encrypter.encrypt(moleLib_entry.getMolePosText()));
            Log.d(TAG, "encryptMoleEntry: enc " + moleLib_entry.getMoleImageUri() + " dec: " + encrypter.encrypt(moleLib_entry.getMoleImageUri()));
            Log.d(TAG, "encryptMoleEntry: enc " + moleLib_entry.getMolePosBitmapUri() + " dec: " + encrypter.encrypt(moleLib_entry.getMolePosBitmapUri()));
            Log.d(TAG, "encryptMoleEntry: enc " + moleLib_entry.getDiagnosis() + " dec: " + encrypter.encrypt(moleLib_entry.getDiagnosis()));
            Log.d(TAG, "encryptMoleEntry: enc " + moleLib_entry.getDateMoleImageCreation() + " dec: " + encrypter.encrypt(moleLib_entry.getDateMoleImageCreation()));

            Log.d(TAG, "encryptMoleEntry: enc " + moleLib_entry.getMolePosText() + " enc again: " + encrypter.decrypt(encrypter.encrypt(moleLib_entry.getMolePosText()).trim()));
            Log.d(TAG, "encryptMoleEntry: enc " + moleLib_entry.getMoleImageUri() + " enc again: " + encrypter.decrypt(encrypter.encrypt(moleLib_entry.getMoleImageUri())));
            Log.d(TAG, "encryptMoleEntry: enc " + moleLib_entry.getMolePosBitmapUri() + " enc again: " + encrypter.decrypt(encrypter.encrypt(moleLib_entry.getMolePosBitmapUri())));
            Log.d(TAG, "encryptMoleEntry: enc " + moleLib_entry.getDiagnosis() + " enc again: " + encrypter.decrypt(encrypter.encrypt(moleLib_entry.getDiagnosis())));
            Log.d(TAG, "encryptMoleEntry: enc " + moleLib_entry.getDateMoleImageCreation() + " enc again: " + encrypter.decrypt(encrypter.encrypt(moleLib_entry.getDateMoleImageCreation())));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}