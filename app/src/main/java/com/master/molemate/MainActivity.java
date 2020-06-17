package com.master.molemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.Toolbar;

import com.master.molemate.DataSecurity.TextConverter;
import com.master.molemate.HomeScreen.HomeScreen;
import com.master.molemate.LoginProcess.LoginActivity;
import com.master.molemate.RoomDB.DAO_Interfaces.DAO_Interface_Mole_Library;
import com.master.molemate.RoomDB.Entities.EntityMix_User_MoleLib;
import com.master.molemate.RoomDB.Entities.Entity_Mole_Library;
import com.master.molemate.RoomDB.Entities.Entity_Users;
import com.master.molemate.RoomDB.MoleMateDB;
import com.master.molemate.RoomDB.MoleMateDB_Repository;
import com.master.molemate.RoomDB.MoleMateDB_ViewModel;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MoleMateDB_ViewModel moleMateDBViewModel;
    private Entity_Users user;
    private List<EntityMix_User_MoleLib> moleUserMix;
    private String userMail;
    private static int uid;
    private TextConverter encrypter;
    private String userKey;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Erstelle statische Klasse für die unteren drei Aufrufe
        //dbFactory in der Art, die die Instance immer wieder anfragt

        sharedPref = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        Intent tmpIntent = getIntent();

        userMail = tmpIntent.getStringExtra("mail");
        userKey = tmpIntent.getStringExtra("key");

        Log.d(TAG, "onCreate: key: " + userKey);

        encrypter = new TextConverter(userKey);


        moleMateDBViewModel = new ViewModelProvider(this).get(MoleMateDB_ViewModel.class);

        moleMateDBViewModel.getUserByMail(userMail).observe(this, new Observer<Entity_Users>() {
            @Override
            public void onChanged(Entity_Users entity_users) {
                user = entity_users;
                if(user != null)
                {
                    if(decryptUser(user))
                    {
                        uid = user.getUid();
                        Log.d(TAG, "collectedAllUsers() size: " + user.getUserName() + " with id " + uid);
                        getMolesForUser(uid);

                    }else {
                        Toast.makeText(getApplication(), "Dein Nutzer konnte nicht gefunden werden, versuche es bitte erneut!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }

                }else {
                    Log.d(TAG, "No User!");

                    if(userMail.equals("default@default.com")){
                        new PopulatDefaultUserToDBAsyncTask(moleMateDBViewModel).execute();
                    }

                    //Toast.makeText(getApplication(), "Der Nutzer konnte nicht gefunden werden, versuche es bitte erneut!", Toast.LENGTH_LONG).show();
                    //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    //startActivity(intent);
                }
            }
        });
        Log.d(TAG, "onCreate: This should be the User! " + uid);

    }

    private boolean decryptUser(Entity_Users userEntity) {

        try {
            userEntity.setUserName(encrypter.decrypt(userEntity.getUserName()));
            userEntity.setMail(encrypter.decrypt(userEntity.getMail()));
            userEntity.setLastName(encrypter.decrypt(userEntity.getLastName()));
            userEntity.setFirstName(encrypter.decrypt(userEntity.getFirstName()));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    private void getMolesForUser(int uid) {
        final int tmpUid = uid;
        moleMateDBViewModel.getAllMolesFromUser(uid).observe(this, new Observer<List<EntityMix_User_MoleLib>>() {
            @Override
            public void onChanged(List<EntityMix_User_MoleLib> entityMix_user_moleLibs) {
                moleUserMix = entityMix_user_moleLibs;
                if(moleUserMix.size() == 0){

                }

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("uid", tmpUid);
                editor.putString("key", userKey);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                intent.putExtra("currentUser", tmpUid);
                startActivity(intent);
            }
        });

    }


    private static class PopulatDefaultUserToDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private MoleMateDB_ViewModel moleLibDao;

        private PopulatDefaultUserToDBAsyncTask(MoleMateDB_ViewModel moleUserDB){
            this.moleLibDao = moleUserDB;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            moleLibDao.insertUser(new Entity_Users(
                    "default",
                    "default",
                    "default",
                    "default@default.com"
            ));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute: inserted default User");
        }
    }


}
